package com.scbank.process.api.fw.channel.logging;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.context.ServiceContextHolder;
import com.scbank.process.api.fw.channel.logging.dto.HttpLogFactory;
import com.scbank.process.api.fw.channel.logging.dto.HttpRequestLog;
import com.scbank.process.api.fw.channel.logging.dto.HttpResponseLog;
import com.scbank.process.api.fw.channel.message.IResponseMessage;
import com.scbank.process.api.fw.channel.service.metadata.ServiceDefinitionMetadata.ServiceInfo;
import com.scbank.process.api.fw.channel.service.metadata.ServiceMethodMetadata;
import com.scbank.process.api.fw.channel.service.metadata.ServiceMethodMetadata.ParameterMetadata;
import com.scbank.process.api.fw.channel.service.registry.IServiceRegistrar;
import com.scbank.process.api.fw.channel.service.resolver.IServiceComponentResolver;
import com.scbank.process.api.fw.channel.service.resolver.impl.ServiceComponentResolver;
import com.scbank.process.api.fw.channel.support.ChannelMessageContextCreator;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.context.MessageContext;
import com.scbank.process.api.fw.message.enums.MessageFormat;
import com.scbank.process.api.fw.message.mapper.jackson.JacksonMessageMapper;
import com.scbank.process.api.fw.message.metadata.IIntegrationMessageMetadata;
import com.scbank.process.api.fw.message.metadata.registry.IIntegrationMessageMetadataRegistrar;
import com.scbank.process.api.fw.message.option.MessageFormatOptions.MessageFormatOption;
import com.scbank.process.api.fw.message.option.SerializationOptions;
import com.scbank.process.api.fw.message.utils.MessageUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 프로세스API 요청/응답 로깅 처리 Advice 추상 클래스
 * 
 * @author sungdon.choi
 */
@Slf4j
@Order(99)
@RestControllerAdvice(basePackages = "com.scbank")
public class RequestResponseBodyLoggingProcessor extends RequestBodyAdviceAdapter
        implements ResponseBodyAdvice<Object> {

    protected IServiceRegistrar serviceRegistrar;

    protected IServiceComponentResolver serviceComponentResolver = new ServiceComponentResolver();

    protected JacksonMessageMapper jacksonMessageMapper;

    protected HttpLogFactory logFactory = new HttpLogFactory();

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType,
            Class<? extends HttpMessageConverter<?>> converterType) {
        return methodParameter.hasParameterAnnotation(RequestBody.class);
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter,
            Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {

        try {
            this.writeRequestLog(null);
        } catch (Exception e) {
            log.error("요청 데이터 로그 출력 중 오류 발생 [" + e.getMessage() + "]", e);
        }
        return super.handleEmptyBody(body, inputMessage, parameter, targetType, converterType);
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
            Class<? extends HttpMessageConverter<?>> converterType) {
        if (!(body instanceof byte[])) {
            return body;
        }

        try {
            IServiceContext ctx = ServiceContextHolder.getContext();
            byte[] inputBytes = (byte[]) body;

            IMessageObject input = inputBytes.length == 0 ? null : this.converterInputMessage(ctx, inputBytes);
            // 요청 로그 출력
            this.writeRequestLog(input);
        } catch (Exception e) {
            log.error("요청 데이터 로그 출력 중 오류 발생 [" + e.getMessage() + "]", e);
        }
        return body;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
            ServerHttpResponse response) {
        if (!(body instanceof IResponseMessage)) {
            return body;
        }

        try {
            IResponseMessage responseMessage = (IResponseMessage) body;
            this.writeResponseLog(responseMessage);
        } catch (Exception e) {
            log.error("응답 데이터 로그 출력 중 오류 발생 [" + e.getMessage() + "]", e);
        }
        return body;
    }

    /**
     * HTTP 요청 바디로부터 입력 DTO를 읽어오는 메서드
     *
     * @param request               현재 요청 객체
     * @param serviceMethodMetadata 실행 대상 서비스 메서드 메타데이터
     * @param <T>                   입력 DTO 타입
     * @return 메시지 오브젝트 또는 null (입력이 없는 경우)
     * @throws Exception 메시지 바인딩 실패 시
     */
    @SuppressWarnings("unchecked")
    protected <T extends IMessageObject> T converterInputMessage(IServiceContext ctx, byte[] input)
            throws Exception {
        this.jacksonMessageMapper = RuntimeContext.getBean(JacksonMessageMapper.class);
        this.serviceRegistrar = RuntimeContext.getBean(IServiceRegistrar.class);

        ServiceInfo resolvedService = this.serviceComponentResolver.resolve(ctx);
        String component = resolvedService.getComponent();
        ServiceMethodMetadata resolvedServiceMetadata = this.serviceRegistrar.getServiceMethodMetadata(component);
        // 파라미터에서 body=true인 대상만 필터링
        List<ServiceMethodMetadata.ParameterMetadata> bodyParams = resolvedServiceMetadata.getParameters().stream()
                .filter(ServiceMethodMetadata.ParameterMetadata::isBody)
                .toList();

        if (bodyParams.isEmpty()) {
            log.debug("# 바디 파라미터가 존재하지 않음. 입력 DTO 생략.");
            return null;
        }

        if (bodyParams.size() > 1) {
            throw new UnsupportedOperationException("바디 파라미터는 하나만 허용됩니다. 현재: " + bodyParams.size());
        }

        ParameterMetadata bodyParam = bodyParams.get(0);
        Class<T> inputType = (Class<T>) bodyParam.getType();

        if (!IMessageObject.class.isAssignableFrom(inputType)) {
            throw new IllegalArgumentException("바디 파라미터 타입은 IMessageObject를 구현해야 합니다: " + inputType.getName());
        }

        return this.jacksonMessageMapper.deserialize(input, inputType, this.createMessageContext());
    }

    /**
     * 응답 메시지 객체를 byte 배열 형태로 변한다.
     * 
     * @param ctx
     * @param output
     * @return
     */
    @SuppressWarnings("rawtypes")
    protected <T extends IResponseMessage> byte[] convertOutputMessage(IServiceContext ctx, T response)
            throws Exception {
        this.jacksonMessageMapper = RuntimeContext.getBean(JacksonMessageMapper.class);
        ObjectMapper objectMapper = this.jacksonMessageMapper.getObjectMapper();
        IIntegrationMessageMetadataRegistrar metadataRegistrar = RuntimeContext
                .getBean(IIntegrationMessageMetadataRegistrar.class);
        ObjectNode rootNode = JsonNodeFactory.instance.objectNode();

        List<Field> fields = MessageUtils.getAllFields(response.getClass());
        for (Field field : fields) {
            Object child = MessageUtils.getFieldValue(response, field.getName());
            if (!(child instanceof IMessageObject msg)) {
                continue;
            }

            IIntegrationMessageMetadata metadata = metadataRegistrar.getMetadata(child.getClass());
            if (metadata == null) {
                continue;
            }

            byte[] bytes = this.jacksonMessageMapper.serialize(msg, metadata, this.createMessageContext());
            rootNode.set(field.getName(), objectMapper.readTree(bytes));
        }

        return objectMapper.writeValueAsBytes(rootNode);
    }

    /**
     * 로그 출력용 메시지 컨텍스트 객체 생성
     * 
     * @return 메시지 컨텍스트 객체 {@link MessageContext}
     */
    protected MessageContext createMessageContext() {
        ChannelMessageContextCreator messageContextCreator = RuntimeContext.getBean(ChannelMessageContextCreator.class);
        MessageContext messageContext = messageContextCreator.create(MessageFormat.JSON,
                RuntimeContext.getDefaultEncoding());

        Map<String, Object> extendedOptions = Map.of(
                MessageFormatOption.FIELD_MASK.name(), true,
                MessageFormatOption.PRETTY_FORMAT.name(), true,
                MessageFormatOption.FIELD_TRIM.name(), true,
                MessageFormatOption.FULLWIDTH_TO_HALFWIDTH.name(), true);
        SerializationOptions mergedOptions = SerializationOptions.merge(messageContext.getSerializationOptions(),
                extendedOptions);

        messageContext.setSerializationOptions(mergedOptions);

        return messageContext;
    }

    /**
     * 요청로그 출력
     * 
     * @param <T>   IMessageObject 인터페이스를 구현한 T 타입 클래스
     * @param ctx   서비스 요청 컨텍스트 객체
     * @param input 요청 데이터 객체
     * @throws Exception 요청 로그 출력 중 발생한 예외 객체
     */
    protected <T extends IMessageObject> void writeRequestLog(T input) throws Exception {
        IServiceContext ctx = ServiceContextHolder.getContext();
        String bodyString = "";
        if (input != null) {
            bodyString = new String(jacksonMessageMapper.serialize(input, this.createMessageContext()));
        }

        ObjectMapper objectMapper = this.jacksonMessageMapper != null ? this.jacksonMessageMapper.getObjectMapper()
                : new ObjectMapper();
        Map<String, Object> map = StringUtils.isBlank(bodyString) ? null
                : objectMapper.readValue(bodyString, new TypeReference<Map<String, Object>>() {
                });
        HttpRequestLog requestLog = logFactory.buildRequestLog(ctx, map == null ? "[EMPTY]" : map);

        if (log.isInfoEnabled()) {
            log.info(objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(requestLog));
        }
    }

    /**
     * 응답로그 출력
     * 
     * @param <T>    IResponseMessage 인터페이스를 구현한 T 타입 클래스
     * @param ctx    서비스 요청 컨텍스트 객체
     * @param output 응답 데이터 객체
     * @throws Exception 응답 로그 출력 중 발생한 예외 객체
     */
    @SuppressWarnings("rawtypes")
    protected <T extends IResponseMessage> void writeResponseLog(T output) throws Exception {
        IServiceContext ctx = ServiceContextHolder.getContext();
        String bodyString = "";
        if (output != null) {
            bodyString = new String(this.convertOutputMessage(ctx, output));
        }

        ObjectMapper objectMapper = this.jacksonMessageMapper != null ? this.jacksonMessageMapper.getObjectMapper()
                : new ObjectMapper();
        Map<String, Object> map = StringUtils.isBlank(bodyString) ? null
                : objectMapper.readValue(bodyString, new TypeReference<Map<String, Object>>() {
                });
        HttpResponseLog responseLog = this.logFactory.buildResponseLog(ctx, map == null ? "[EMPTY]" : map);

        if (log.isInfoEnabled()) {
            log.info(objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(responseLog));
        }
    }
}
