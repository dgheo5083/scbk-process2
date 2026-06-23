package com.scbank.process.api.fw.openapi;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scbank.process.api.fw.channel.ChannelProperties.ServiceProperties;
import com.scbank.process.api.fw.channel.service.metadata.ServiceDefinitionMetadata;
import com.scbank.process.api.fw.channel.service.metadata.ServiceDefinitionMetadata.ServiceInfo;
import com.scbank.process.api.fw.channel.service.metadata.ServiceMethodMetadata;
import com.scbank.process.api.fw.channel.service.metadata.ServiceMethodMetadata.ParameterMetadata;
import com.scbank.process.api.fw.channel.service.registry.IServiceRegistrar;
import com.scbank.process.api.fw.core.component.ServiceComponent;
import com.scbank.process.api.fw.core.utils.ReflectionUtils;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.openapi.OpenApiHeaderProperties.Header;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 서비스 정의 메타데이터를 기반으로 각 서비스에 대한 {@link GroupedOpenApi} 인스턴스를 생성하는 팩토리 클래스.
 * <p>
 * 생성된 GroupedOpenApi는 SpringDoc에 수동으로 등록되어, 서비스별 Swagger 문서를 자동 구성하는 데 활용된다.
 *
 * @author sungdon.choi
 * @since 2025.05
 */
@Slf4j
@RequiredArgsConstructor
public class ServiceGroupedOpenApiBeanFactory {

	private final ObjectProvider<IServiceRegistrar> serviceRegistrar;

	private final OpenApiHeaderProperties openApiHeaderProperties;

	@Value("${server.servlet.context-path}")
	private String contextPath;

	/**
	 * 주어진 서비스 목록에 대해 GroupedOpenApi 목록을 생성한다.
	 *
	 * @param services 서비스 설정 목록
	 * @return GroupedOpenApi 목록
	 */
	public List<GroupedOpenApi> createGroupedOpenApis(List<ServiceProperties> services) {
		List<GroupedOpenApi> apis = new ArrayList<>();

		for (ServiceProperties service : services) {
			if (!service.enabled()) {
				continue;
			}

			String serviceId = service.serviceId();

			if (serviceId == null || serviceId.isBlank()) {
				continue;
			}

			String basePath = StringUtils.defaultIfEmpty(service.basePath(), "");
			String pathToMatch = "/**";

			// bat path 적용
			if (StringUtils.hasLength(basePath)) {
				pathToMatch = basePath + pathToMatch;
			}

			// context path 적용
			pathToMatch = StringUtils.hasLength(contextPath) ? contextPath + pathToMatch : "/" + pathToMatch;

			apis.add(
					GroupedOpenApi.builder()
							.group(serviceId)
							.pathsToMatch(pathToMatch + "/**")
							.addOpenApiCustomizer(openApi -> customizeOpenApi(service, serviceId, openApi))
							.build());
		}

		return apis;
	}

	/**
	 * OpenAPI 문서를 커스터마이징하여 operation 정보 및 schema 등을 등록한다.
	 *
	 * @param serviceId 대상 서비스 ID
	 * @param openApi   커스터마이징할 OpenAPI 객체
	 */
	private void customizeOpenApi(ServiceProperties service, String serviceId, OpenAPI openApi) {
		List<ServiceDefinitionMetadata> definitions = getServiceDefinitionsByServiceId(serviceId);
		for (ServiceDefinitionMetadata def : definitions) {
			try {
				for (ServiceInfo serviceInfo : def.getServices()) {
					if (serviceInfo.isFallback())
						continue;

					String path = def.getUrl();
					Operation operation = buildOperation(service, openApi, serviceInfo);

					this.addHeaders(operation);

					openApi.path(path, new PathItem().post(operation));
				}
			} catch (Exception e) {
				log.error("# [{}] OpenApi Customize 실패, URL: {}", serviceId, def.getUrl(), e);
			}
		}
	}

	/**
	 * 단일 서비스에 대한 Swagger Operation 객체를 생성한다.
	 *
	 * @param openApi     스키마 등록에 사용될 OpenAPI 객체
	 * @param serviceInfo 서비스 메타 정보
	 * @return 생성된 Operation 객체
	 */
	private Operation buildOperation(ServiceProperties service, OpenAPI openApi, ServiceInfo serviceInfo) {
		IServiceRegistrar registrar = this.serviceRegistrar.getObject();
		if (registrar == null) {
			return null;
		}
		ServiceMethodMetadata methodMetadata = registrar.getServiceMethodMetadata(serviceInfo.getComponent());
		if (methodMetadata == null)
			return null;

		Class<?> inputType = methodMetadata.getParameters() == null ? null
				: methodMetadata.getParameters().stream()
						.filter(ParameterMetadata::isBody)
						.map(ParameterMetadata::getType)
						.findFirst().orElse(null);
		Class<?> outputType = methodMetadata.getReturnType();

		this.registerSchemas(openApi, inputType, outputType);

		String inputSchema = inputType == null ? null : "#/components/schemas/" + inputType.getSimpleName();
		String outputSchema = outputType == null || outputType == Void.class || outputType == void.class ? null
				: "#/components/schemas/" + outputType.getSimpleName();

		RequestBody requestBody = this.buildRequestBody(inputType, inputSchema,
				service.serviceMapping().allowedContentTypes());

		ApiResponses responses = null;
		if (outputSchema != null) {
			responses = new ApiResponses()
					.addApiResponse(String.valueOf(HttpStatus.OK.value()), new ApiResponse().description("성공")
							.content(new Content().addMediaType(
									org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
									new MediaType().schema(new Schema<>().$ref(outputSchema)))));
		} else {
			responses = new ApiResponses()
					.addApiResponse(String.valueOf(HttpStatus.NO_CONTENT.value()),
							new ApiResponse().description("No Content"));
		}

		return new Operation()
				.summary(serviceInfo.getDescription())
				.operationId(methodMetadata.getDeclaringClass().getSimpleName() + "#" + methodMetadata.getMethodName())
				.requestBody(requestBody)
				.responses(responses)
				.tags(List.of(this.getTag(methodMetadata.getDeclaringClass())));
	}

	/**
	 * swagger ui operation custom header
	 * 
	 * @param operation Operation
	 */
	private void addHeaders(Operation operation) {
		if (operation == null) {
			return;
		}

		if (this.openApiHeaderProperties == null) {
			return;
		}

		List<Header> headers = this.openApiHeaderProperties.getHeaders();
		headers.stream().forEach(header -> {
			try {
				Parameter parameter = new Parameter()
						.in("header")
						.name(header.name())
						.description(header.description())
						.required(header.required())
						.schema(new Schema<>().type(header.type()));
				operation.addParametersItem(parameter);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		});
	}

	/**
	 * 
	 * @param inputType
	 * @param inputSchema
	 * @param contentTypes
	 * @return
	 */
	private RequestBody buildRequestBody(Class<?> inputType, String inputSchema, List<String> contentTypes) {
		if (inputType == null) {
			Content content = new Content();
			content.addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
					new MediaType().schema(new Schema<>()));

			return new RequestBody()
					.required(false)
					.content(content);
		}

		Content content = new Content();
		for (String contentType : contentTypes) {
			content.addMediaType(contentType, new MediaType().schema(new Schema<>().$ref(inputSchema)));
		}

		return new RequestBody()
				.required(true)
				.content(content);
	}

	/**
	 * 주어진 타입(Class)에 대해 OpenAPI Components.schemas에 자동 등록한다.
	 *
	 * @param openApi OpenAPI 객체
	 * @param classes 등록할 타입 목록 (예: 입력/출력 DTO)
	 */
	@SuppressWarnings("rawtypes")
	private void registerSchemas(OpenAPI openApi, Class<?>... classes) {
		Components components = openApi.getComponents();
		if (components == null) {
			components = new Components();
			openApi.setComponents(components);
		}
		if (components.getSchemas() == null) {
			components.setSchemas(new HashMap<>());
		}

		for (Class<?> clazz : classes) {
			try {
				if (clazz == null || clazz == Void.class || clazz == void.class)
					continue;

				Map<String, Schema> schemaMap = ModelConverters.getInstance()
						.readAllAsResolvedSchema(clazz).referencedSchemas;

				for (Map.Entry<String, Schema> entry : schemaMap.entrySet()) {
					String name = entry.getKey();
					Schema<?> schema = entry.getValue();

					// required 필드 수동 지정
					List<String> requiredFields = new ArrayList<>();
					Map<String, Object> exampleMap = new HashMap<>();

					for (Field field : clazz.getDeclaredFields()) {
						MessageField annotation = field.getAnnotation(MessageField.class);
						if (annotation == null) {
							continue;
						}

						if (annotation.required()) {
							requiredFields.add(field.getName());
						}

						String exampleRaw = annotation.example();
						Object exampleValue = (StringUtils.isEmpty(exampleRaw)) ? generateDefaultExample(field)
								: castExampleValue(field, exampleRaw);
						if (schema.getProperties() != null) {
							Schema<?> proSchema = (Schema<?>) schema.getProperties().get(field.getName());
							if (proSchema != null) {
								proSchema.setExample(exampleValue);
							}
						}
						exampleMap.put(field.getName(), exampleValue);
					}

					if (!requiredFields.isEmpty()) {
						schema.setRequired(requiredFields);
					}

					if (!exampleMap.isEmpty()) {
						schema.setExample(exampleMap);
					}

					if (!components.getSchemas().containsKey(name)) {
						components.addSchemas(name, schema);
						log.debug("# 스키마 자동 등록 (required 포함): {}", name);
					}
				}
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
	}

	/**
	 * 주어진 서비스 ID에 해당하는 서비스 정의 메타데이터 목록을 반환한다.
	 *
	 * @param serviceId 서비스 ID
	 * @return 해당 서비스 ID의 메타데이터 목록
	 */
	private List<ServiceDefinitionMetadata> getServiceDefinitionsByServiceId(String serviceId) {
		IServiceRegistrar registrar = this.serviceRegistrar.getObject();
		if (registrar == null) {
			return null;
		}
		
		List<ServiceDefinitionMetadata> result = new ArrayList<>();
		for (ServiceDefinitionMetadata metadata : registrar.getServiceDefinitions()) {
			if (serviceId.equals(metadata.getServiceId())) {
				result.add(metadata);
			}
		}
		return result;
	}

	private String getTag(Class<?> clazz) {
		String fqcn = clazz.getName();
		ServiceComponent anno = clazz.getAnnotation(ServiceComponent.class);
		if (anno == null) {
			return fqcn;
		}

		String tagNm = StringUtils.EMPTY;
		String name = anno.name();
		if (StringUtils.hasLength(name)) {
			tagNm = name;
		} else {
			String description = anno.description();
			if (StringUtils.hasLength(description)) {
				tagNm = description;
			}
		}

		if (!StringUtils.hasLength(tagNm)) {
			return fqcn;
		}

		return new StringBuffer().append(fqcn).append("\t[").append(tagNm).append("]").toString();
	}

	private <T> T castSimplTypeExampleValue(Class<T> type, String exampleString) {
		Object value = null;
		if (type == int.class || type == Integer.class) {
			value = Integer.valueOf(exampleString);
		} else if (type == long.class || type == Long.class) {
			value = Long.valueOf(exampleString);
		} else if (type == double.class || type == Double.class) {
			value = Double.valueOf(exampleString);
		} else if (type == BigDecimal.class) {
			value = new BigDecimal(exampleString);
		} else if (type == boolean.class || type == Boolean.class) {
			value = Boolean.valueOf(exampleString);
		} else if (type == String.class) {
			value = exampleString;
		}

		return type.cast(value);
	}

	/**
	 * 
	 * @param field
	 * @param exampleString
	 * @return
	 */
	private Object castExampleValue(Field field, String exampleString) {
		if (StringUtils.isEmpty(exampleString)) {
			return this.generateDefaultExample(field);
		}

		Class<?> type = field.getType();

		try {
			if (type == int.class || type == Integer.class) {
				return Integer.valueOf(exampleString);
			}

			if (type == long.class || type == Long.class) {
				return Long.valueOf(exampleString);
			}

			if (type == double.class || type == Double.class) {
				return Double.valueOf(exampleString);
			}

			if (type == BigDecimal.class) {
				return new BigDecimal(exampleString);
			}

			if (type == boolean.class || type == Boolean.class) {
				return Boolean.valueOf(exampleString);
			}

			if (type == String.class) {
				return exampleString;
			}

			if (List.class.isAssignableFrom(type)) {
				final Class<?> itemType = ReflectionUtils.getListItemType(field);

				if (!ReflectionUtils.isSimpleType(itemType)) {
					return null;
				}

				if (exampleString.contains(",")) {
					return Arrays.stream(exampleString.split(",")).map((v) -> {
						return castSimplTypeExampleValue(itemType, v);
					}).collect(Collectors.toList());
				}

				if (exampleString.startsWith("[") && exampleString.endsWith("]")) {
					ObjectMapper om = new ObjectMapper();
					return om.readValue(exampleString, List.class);
				}
				return List.of(exampleString);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return exampleString;
		}

		return exampleString;
	}

	/**
	 * swagger-ui에서 사용하는 기본 example 값 설정 및 반환
	 * 
	 * @param field
	 * @return
	 */
	private Object generateDefaultExample(Field field) {
		Class<?> type = field.getType();

		if (List.class.isAssignableFrom(type)) {
			return generateListExample(field, null);
		}

		if (ReflectionUtils.isSimpleType(type)) {
			return generateDefaultSimpleValue(type);
		}

		return generateDtoExample(type);
	}

	/**
	 * 필드 타입별 기본값을 가져온다.
	 * 
	 * @param <T>  필드타입
	 * @param type T 타입 클래스
	 * @return T 타입의 기본값
	 */
	@SuppressWarnings("unchecked")
	private <T> T generateDefaultSimpleValue(Class<T> type) {
		Object value = null;

		if (type == byte.class || type == Byte.class) {
			value = 0x00;
		}

		if (type == char.class || type == Character.class) {
			value = 0;
		}

		if (type == int.class || type == Integer.class) {
			value = 0;
		}

		if (type == long.class || type == Long.class) {
			value = 0L;
		}

		if (type == float.class || type == Float.class) {
			value = 0.0f;
		}

		if (type == double.class || type == Double.class) {
			value = 0.0d;
		}

		if (type == BigDecimal.class) {
			value = BigDecimal.ZERO;
		}

		if (type == boolean.class || type == Boolean.class) {
			value = false;
		}

		if (type == String.class) {
			value = StringUtils.EMPTY;
		}

		if (type.isEnum()) {
			Object[] constants = type.getEnumConstants();
			value = constants.length > 0 ? constants[0] : null;
		}

		if (type.isPrimitive()) {
			return (T) value;
		}

		return type.cast(value);
	}

	/**
	 * 
	 * @param field
	 * @param exampleString
	 * @return
	 */
	private Object generateListExample(Field field, String exampleString) {
		final Class<?> itemType = ReflectionUtils.getListItemType(field);

		log.debug("# itemType={}", itemType);
		
		if (!ReflectionUtils.isSimpleType(itemType)) {
			return List.of(generateDtoExample(itemType));
		}

		return Arrays.stream(StringUtils.defaultIfEmpty(exampleString, "").split(","))
				.map((v) -> {
					return castSimplTypeExampleValue(itemType, v);
				})
				.collect(Collectors.toList());
	}

	/**
	 * 
	 * @param dtoClass
	 * @return
	 */
	private Object generateDtoExample(Class<?> dtoClass) {
		
		log.debug("# generateDtoExample dtoClass={} ", dtoClass);
		Map<String, Object> exampleMap = new HashMap<>();

		for (Field f : dtoClass.getDeclaredFields()) {
			MessageField mf = f.getAnnotation(MessageField.class);
			
			//무한루프 오류 수정
			if (mf == null) {
				continue;
			}
			
			if (mf != null && !mf.example().isBlank()) {
				exampleMap.put(f.getName(), this.castExampleValue(f, mf.example()));
				continue;
			}

			if (ReflectionUtils.isListType(f)) {
				exampleMap.put(f.getName(), generateListExample(f, null));
			} else if (!ReflectionUtils.isSimpleType(f.getType())) {
				exampleMap.put(f.getName(), generateDtoExample(f.getType()));
			} else {
				exampleMap.put(f.getName(), generateDefaultExample(f));
			}
		}
		return exampleMap;
	}
}