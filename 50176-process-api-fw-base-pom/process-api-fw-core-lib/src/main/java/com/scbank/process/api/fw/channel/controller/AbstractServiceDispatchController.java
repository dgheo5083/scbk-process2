package com.scbank.process.api.fw.channel.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.util.ContentCachingRequestWrapper;

import com.scbank.process.api.fw.channel.ChannelProperties;
import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.converter.HttpMessageConverterComposite;
import com.scbank.process.api.fw.channel.message.IResponseMessage;
import com.scbank.process.api.fw.channel.message.IResponseMessageFactory;
import com.scbank.process.api.fw.channel.response.ResponseRendererComposite;
import com.scbank.process.api.fw.channel.service.executor.IServiceComponentExecutor;
import com.scbank.process.api.fw.channel.service.executor.IServiceComponentExecutorFactory;
import com.scbank.process.api.fw.channel.service.metadata.ServiceDefinitionMetadata.ServiceInfo;
import com.scbank.process.api.fw.channel.service.metadata.ServiceMethodMetadata;
import com.scbank.process.api.fw.channel.service.metadata.ServiceMethodMetadata.ParameterMetadata;
import com.scbank.process.api.fw.channel.service.registry.IServiceRegistrar;
import com.scbank.process.api.fw.channel.service.resolver.IServiceComponentResolver;
import com.scbank.process.api.fw.channel.service.resolver.impl.ServiceComponentResolver;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.core.validation.IBeanValidator;
import com.scbank.process.api.fw.message.IMessageObject;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * 프레임워크 Dispatcher 컨트롤러의 공통 기능을 정의한 추상 클래스입니다.
 * 실제 Dispatcher 컨트롤러는 이를 상속하여 공통 흐름 기반으로 서비스를 실행합니다.
 * 
 * 주요 책임:
 * - 요청 파라미터 바인딩
 * - 입력 DTO 검증 (JSR-380 기반)
 * - 실행 대상 서비스 컴포넌트 조회 및 실행
 * - 응답 메시지 생성
 * </pre>
 * 
 * @author sungdon.choi
 */
@Slf4j
public class AbstractServiceDispatchController {

    /** 프레임워크 설정 (validation 사용 여부 등) */
    protected ChannelProperties properties;

    /** HTTP 요청/응답 메시지 변환을 담당하는 컨버터 */
    @SuppressWarnings("rawtypes")
    protected HttpMessageConverterComposite httpMessageConverterComposite;

    /** 응답 메시지를 생성하는 팩토리 */
    @SuppressWarnings("rawtypes")
    protected IResponseMessageFactory responseMessageFactory;

    /** Bean Validation 기반 유효성 검사기 */
    @SuppressWarnings("rawtypes")
    protected IBeanValidator beanValidator;

    /** 서비스 ID → 실제 Bean 및 Method 매핑을 위한 Resolver */
    protected IServiceComponentResolver serviceComponentResolver = new ServiceComponentResolver();

    protected IServiceRegistrar serviceRegistrar;

    /** 실제 서비스 컴포넌트를 실행하는 Executor 팩토리 */
    protected IServiceComponentExecutorFactory serviceComponentExecutorFactory;

    protected ResponseRendererComposite responseRendererComposite;

    protected ResponseRendererComposite getResponseRendererComposite() {
        if (this.responseRendererComposite == null) {
            this.responseRendererComposite = RuntimeContext.getBean(ResponseRendererComposite.class);
        }
        return this.responseRendererComposite;
    }

    protected IServiceRegistrar getServiceRegistrar() {
        if (this.serviceRegistrar == null) {
            this.serviceRegistrar = RuntimeContext.getBean(IServiceRegistrar.class);
        }
        return this.serviceRegistrar;
    }

    /**
     * HTTP 요청 바디로부터 입력 DTO를 읽어오는 메서드
     *
     * @param request     현재 요청 객체
     * @param serviceInfo 서비스 정보 메타데이터
     * @param <T>         입력 DTO 타입
     * @return 메시지 오브젝트 또는 null (입력이 없는 경우)
     * @throws Exception 메시지 바인딩 실패 시
     */
    protected <T extends IMessageObject> T readInputMessage(HttpServletRequest request, ServiceInfo serviceInfo)
            throws Exception {
        this.serviceRegistrar = RuntimeContext.getBean(IServiceRegistrar.class);

        String component = serviceInfo.getComponent();

        ServiceMethodMetadata serviceMethodMetadata = this.serviceRegistrar.getServiceMethodMetadata(component);
        return readInputMessage(request, serviceMethodMetadata);
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
    protected <T extends IMessageObject> T readInputMessage(HttpServletRequest request,
            ServiceMethodMetadata serviceMethodMetadata) throws Exception {
        this.httpMessageConverterComposite = RuntimeContext.getBean(HttpMessageConverterComposite.class);

        // 파라미터에서 body=true인 대상만 필터링
        List<ServiceMethodMetadata.ParameterMetadata> bodyParams = serviceMethodMetadata.getParameters().stream()
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
        Class<?> inputType = bodyParam.getType();

        if (!IMessageObject.class.isAssignableFrom(inputType)) {
            throw new IllegalArgumentException("바디 파라미터 타입은 IMessageObject를 구현해야 합니다: " + inputType.getName());
        }

        if (!hasRequestBody(request)) {
            log.debug("request.getContentLength(): {}", request.getContentLength());
            log.debug("request.getInputStream().available(): {}",
                    request.getInputStream().available());
            log.debug("request.getContentType(): {}", request.getContentType());
            log.debug("# 요청 바디가 없으므로 입력 바인딩 생략");
            return null;
        }

        return (T) this.httpMessageConverterComposite.read(
                (Class<? extends IMessageObject>) inputType,
                new ServletServerHttpRequest(request));
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
    protected <T extends IMessageObject> T readInputMessage(HttpServletRequest request, byte[] bytes,
            ServiceMethodMetadata serviceMethodMetadata) throws Exception {
        this.httpMessageConverterComposite = RuntimeContext.getBean(HttpMessageConverterComposite.class);

        // 파라미터에서 body=true인 대상만 필터링
        List<ServiceMethodMetadata.ParameterMetadata> bodyParams = serviceMethodMetadata.getParameters().stream()
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
        Class<?> inputType = bodyParam.getType();

        if (!IMessageObject.class.isAssignableFrom(inputType)) {
            throw new IllegalArgumentException("바디 파라미터 타입은 IMessageObject를 구현해야 합니다: " + inputType.getName());
        }

        if (!hasRequestBody(request)) {
            log.debug("request.getContentLength(): {}", request.getContentLength());
            log.debug("request.getInputStream().available(): {}",
                    request.getInputStream().available());
            log.debug("request.getContentType(): {}", request.getContentType());
            log.debug("# 요청 바디가 없으므로 입력 바인딩 생략");
            return null;
        }

        return (T) this.httpMessageConverterComposite.read(
                (Class<? extends IMessageObject>) inputType,
                new CustomServletServerHttpRequest(request, bytes));
    }

    /**
     * 요청 바디가 존재하는지 확인하는 유틸 메서드
     */
    private boolean hasRequestBody(HttpServletRequest request) throws IOException {
        if (request instanceof ContentCachingRequestWrapper req) {
            byte[] requestBytes = req.getContentAsByteArray();
            return requestBytes != null && requestBytes.length > 0;
        }
        return request.getContentLength() > 0 &&
                request.getInputStream().available() > 0 &&
                StringUtils.isNotBlank(request.getContentType());
    }

    /**
     * Bean Validation(JSR-380)을 사용하여 입력 DTO를 유효성 검사합니다.
     *
     * @param input 입력 DTO
     * @param <T>   DTO 타입
     */
    @SuppressWarnings("unchecked")
    protected <T extends IMessageObject> void validateInputDto(T input) {
        properties = RuntimeContext.getBean(ChannelProperties.class);

        // 설정에 따라 유효성 검사 비활성화 가능
        if (!properties.getValidation().enabled()) {
            log.debug("# 프레임워크 채널 입력 DTO 유효성 검증 스킵 처리");
            return;
        }

        beanValidator = RuntimeContext.getBean(IBeanValidator.class);
        if (beanValidator == null || input == null) {
            return;
        }

        beanValidator.validate(input);
    }

    /**
     * 서비스 ID에 해당하는 실제 서비스 Bean 및 Method 정보를 조회합니다.
     *
     * @param ctx 서비스 컨텍스트
     * @return 서비스 정보
     * @throws Exception 매핑 실패 시
     */
    protected ServiceInfo resolveService(IServiceContext ctx) throws Exception {
        return serviceComponentResolver.resolve(ctx);
    }

    /**
     * 서비스 실행자를 반환합니다.
     * 내부적으로 서비스 메소드에 대한 Proxy 또는 Reflect 기반 실행 구조를 생성합니다.
     *
     * @param methodMetadata 실행 서비스컴포넌트 클래스/메소드 메타데이터
     * @return 실행자
     * @throws Exception 생성 실패 시
     */
    protected IServiceComponentExecutor getServiceExecutor(ServiceMethodMetadata methodMetadata) throws Exception {
        this.serviceComponentExecutorFactory = RuntimeContext.getBean(IServiceComponentExecutorFactory.class);
        return this.serviceComponentExecutorFactory.create(methodMetadata);
    }

    /**
     * 서비스 응답 메시지를 생성합니다.
     *
     * @param <H>  응답 헤더 타입
     * @param <T>  응답 바디 타입
     * @param body 실제 응답 데이터
     * @return 응답 메시지 객체
     */
    @SuppressWarnings("unchecked")
    protected <H extends IMessageObject, T extends IMessageObject> IResponseMessage<H, T> buildResponseMessage(T body) {
        responseMessageFactory = RuntimeContext.getBean(IResponseMessageFactory.class);
        return responseMessageFactory.ok(body);
    }
    
    /**
     * 커스텀 HttpInputMessage 클래스
     */
	private static class CustomServletServerHttpRequest extends ServletServerHttpRequest {

        private InputStream body;

        public CustomServletServerHttpRequest(HttpServletRequest servletRequest, byte[] bodyContents) {
            super(servletRequest);
            this.body = new ByteArrayInputStream(bodyContents);
        }

        @Override
        public InputStream getBody() throws IOException {
            return this.body;
        }

    }
}