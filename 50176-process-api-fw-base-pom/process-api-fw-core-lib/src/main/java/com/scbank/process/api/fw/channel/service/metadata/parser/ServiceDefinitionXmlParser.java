package com.scbank.process.api.fw.channel.service.metadata.parser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.springframework.core.io.Resource;
import org.springframework.util.CollectionUtils;

import com.scbank.process.api.fw.channel.service.metadata.ServiceDefinitionMetadata;
import com.scbank.process.api.fw.channel.service.metadata.ServiceDefinitionMetadata.AccessControlInfo;
import com.scbank.process.api.fw.channel.service.metadata.ServiceDefinitionMetadata.InterceptorInfo;
import com.scbank.process.api.fw.channel.service.metadata.ServiceDefinitionMetadata.ParameterInfo;
import com.scbank.process.api.fw.channel.service.metadata.ServiceDefinitionMetadata.ServiceInfo;
import com.scbank.process.api.fw.channel.service.metadata.ServiceDefinitionMetadata.ServiceTimeInfo;
import com.scbank.process.api.fw.channel.service.metadata.ServiceDefinitionMetadata.ServiceTimeInfo.TimeRange;
import com.scbank.process.api.fw.core.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * XML 기반의 서비스 정의 파일을 파싱하여 {@link ServiceDefinitionMetadata}로 변환하는 파서 클래스입니다.
 * Dispatcher가 서비스 메타데이터를 로드할 때, 설정된 서비스 정의 XML을 이 파서를 통해 읽어들입니다.
 *
 * 주요 역할:
 * - endpoint 정보 추출 (url, 설명)
 * - 각 서비스별 컴포넌트 정보 파싱
 * - 서비스 시간 제어
 * - 접근 제어 및 인터셉터 구성
 * </pre>
 *
 * <p>
 * 관련 XML 파일은 service-definition.xsd 스키마를 따릅니다.
 * </p>
 *
 * @author sungdon.choi
 * @since 2025.04.16
 */
@Slf4j
public class ServiceDefinitionXmlParser {

    private static InterceptorParser interceptorParser = new InterceptorParser();
    private static AccessControlParser accessControlParser = new AccessControlParser();
    private static ServiceTimeParser serviceTimeParser = new ServiceTimeParser();
    private static ParameterParser parameterParser = new ParameterParser();

    /**
     * 서비스 정의 XML들을 파싱합니다.
     *
     * @param serviceId 서비스 ID
     * @param resources service-definition.xml 리소스 배열
     * @return 파싱된 {@link ServiceDefinitionMetadata} 리스트
     */
    public List<ServiceDefinitionMetadata> parse(String serviceId, Resource[] resources) {
        if (resources == null || resources.length == 0) {
            return List.of();
        }

        return Arrays.stream(resources)
                .flatMap(r -> {
                    try {
                        return parse(serviceId, r).stream();
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                        return null;
                    }
                }).toList();
    }

    /**
     * 단일 XML 리소스를 파싱합니다.
     *
     * @param serviceId 서비스 ID
     * @param resource  XML 리소스
     * @return 서비스 메타데이터 객체
     */
    private List<ServiceDefinitionMetadata> parse(String serviceId, Resource resource) throws Exception {
        try (InputStream in = resource.getInputStream()) {
            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(in);

            Element root = document.getRootElement();
            Namespace namespace = root.getNamespace();

            List<Element> endpointList = root.getChildren("endpoint", namespace);
            if (CollectionUtils.isEmpty(endpointList)) {
                return null;
            }

            List<ServiceDefinitionMetadata> serviceDefinitionMetadataList = new ArrayList<>();
            for (Element endpoint : endpointList) {
                String url = endpoint.getAttributeValue("url");
                String description = endpoint.getAttributeValue("description");

                List<InterceptorInfo> globalInterceptors = interceptorParser.parse(endpoint, namespace);
                ServiceTimeInfo serviceTimeInfo = serviceTimeParser.parse(endpoint, namespace);
                AccessControlInfo accessControlInfo = accessControlParser.parse(endpoint, namespace);
                List<ParameterInfo> serviceParameters = parameterParser.parse(endpoint, namespace);

                Element services = endpoint.getChild("services", namespace);
                List<Element> serviceElList = services.getChildren();
                List<ServiceInfo> serviceInfos = serviceElList.stream()
                        .map(el -> new ServiceElementParser().parse(el, namespace))
                        .toList();

                serviceDefinitionMetadataList.add(ServiceDefinitionMetadata.builder()
                        .serviceId(serviceId)
                        .url(url)
                        .description(description)
                        .services(serviceInfos)
                        .interceptors(globalInterceptors)
                        .accessControl(accessControlInfo)
                        .serviceTime(serviceTimeInfo)
                        .parameters(serviceParameters)
                        .build());
            }

            return serviceDefinitionMetadataList;
        }
    }

    /**
     * XML 요소를 파싱하는 파서 전략 인터페이스
     */
    @FunctionalInterface
    interface ServiceDefinitionElementParser<T> {
        T parse(Element parent, Namespace namespace);
    }

    /**
     * 개별 서비스 정의(service 요소)를 파싱합니다.
     */
    private static class ServiceElementParser implements ServiceDefinitionElementParser<ServiceInfo> {

        @Override
        public ServiceInfo parse(Element element, Namespace namespace) {
            String description = StringUtils.defaultIfEmpty(element.getAttributeValue("description"), "");
            String condition = StringUtils.defaultIfEmpty(element.getAttributeValue("condition"), "");
            int priority = Integer.parseInt(element.getAttributeValue("priority", "100"));
            boolean fallback = Boolean.parseBoolean(element.getAttributeValue("fallback", "false"));
            String component = StringUtils.defaultIfEmpty(element.getAttributeValue("component"), "");
            String fallbackRef = element.getAttributeValue("fallback-ref");

            return ServiceInfo.builder()
                    .condition(condition)
                    .priority(priority)
                    .component(component)
                    .fallback(fallback)
                    .fallbackRef(fallbackRef)
                    .serviceTime(serviceTimeParser.parse(element, namespace))
                    .interceptors(interceptorParser.parse(element, namespace))
                    .accessControl(accessControlParser.parse(element, namespace))
                    .description(description)
                    .build();
        }
    }

    /**
     * service-time 요소를 파싱합니다.
     */
    private static class ServiceTimeParser implements ServiceDefinitionElementParser<ServiceTimeInfo> {

        private final TimeRangeParser timeRangeParser = new TimeRangeParser();

        @Override
        public ServiceTimeInfo parse(Element parent, Namespace namespace) {
            Element serviceTime = parent.getChild("service-time", namespace);
            if (serviceTime == null)
                return null;

            boolean enabled = Boolean.parseBoolean(
                    StringUtils.defaultIfEmpty(serviceTime.getAttributeValue("enabled"), "true"));

            TimeRange businessDay = timeRangeParser.parse(serviceTime.getChild("businessDay", namespace), namespace);
            TimeRange holiday = timeRangeParser.parse(serviceTime.getChild("holiday", namespace), namespace);

            return new ServiceTimeInfo(enabled, businessDay, holiday);
        }
    }

    /**
     * 시간 범위 요소 (businessDay, holiday 등)를 파싱합니다.
     */
    private static class TimeRangeParser implements ServiceDefinitionElementParser<TimeRange> {

        @Override
        public TimeRange parse(Element element, Namespace namespace) {
            if (element == null) {
                return null;
            }
            String startTime = StringUtils.defaultIfEmpty(element.getAttributeValue("startTime"), "0000");
            String endTime = StringUtils.defaultIfEmpty(element.getAttributeValue("endTime"), "0000");
            return new TimeRange(startTime, endTime);
        }
    }

    /**
     * interceptors 요소를 파싱하여 interceptor 리스트를 반환합니다.
     */
    private static class InterceptorParser implements ServiceDefinitionElementParser<List<InterceptorInfo>> {

        @Override
        public List<InterceptorInfo> parse(Element parent, Namespace namespace) {
            Element interceptors = parent.getChild("interceptors", namespace);
            if (interceptors == null)
                return List.of();

            List<Element> interceptorList = interceptors.getChildren("interceptor");
            if (CollectionUtils.isEmpty(interceptorList))
                return List.of();

            return interceptorList.stream()
                    .map(el -> new InterceptorInfo(el.getAttributeValue("id")))
                    .toList();
        }
    }

    /**
     * access-control 요소를 파싱하여 접근 제어 정보를 반환합니다.
     */
    private static class AccessControlParser implements ServiceDefinitionElementParser<AccessControlInfo> {

        @Override
        public AccessControlInfo parse(Element parent, Namespace namespace) {
            Element accessControl = parent.getChild("access-control", namespace);
            if (accessControl == null)
                return null;

            boolean requiredLogin = Boolean.parseBoolean(
                    StringUtils.defaultIfEmpty(accessControl.getAttributeValue("requiredLogin"), "false"));
            String[] allowedChannelArr = StringUtils
                    .defaultIfEmpty(accessControl.getAttributeValue("channels"), "").split(",");
            return new AccessControlInfo(requiredLogin, allowedChannelArr);
        }
    }

    private static class ParameterParser implements ServiceDefinitionElementParser<List<ParameterInfo>> {

        @Override
        public List<ParameterInfo> parse(Element parent, Namespace namespace) {
            Element parameters = parent.getChild("parameters");
            if (parameters == null)
                return List.of();

            List<Element> parameterList = parameters.getChildren("parameter");
            if (CollectionUtils.isEmpty(parameterList))
                return List.of();

            return parameterList.stream()
                    .map(el -> new ParameterInfo(el.getAttributeValue("name"), el.getAttributeValue("value")))
                    .toList();
        }

    }
}
