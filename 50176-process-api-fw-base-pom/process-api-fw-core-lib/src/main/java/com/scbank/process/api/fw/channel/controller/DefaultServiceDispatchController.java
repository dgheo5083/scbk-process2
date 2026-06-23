package com.scbank.process.api.fw.channel.controller;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.scbank.process.api.fw.channel.constants.ChannelConstants;
import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.context.ServiceContextHolder;
import com.scbank.process.api.fw.channel.message.IResponseMessage;
import com.scbank.process.api.fw.channel.service.executor.IServiceComponentExecutor;
import com.scbank.process.api.fw.channel.service.metadata.ServiceDefinitionMetadata.ServiceInfo;
import com.scbank.process.api.fw.channel.service.metadata.ServiceMethodMetadata;
import com.scbank.process.api.fw.message.IMessageObject;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 프레임워크 기본 서비스 디스패치 컨트롤러.
 *
 * 등록된 서비스 요청 URL에 대해 동적으로 컴포넌트를 매핑하고 실행하는 엔트리 컨트롤러입니다.
 * 요청 DTO → Bean Validation → 컴포넌트 실행 → 응답 DTO → ResponseEntity 반환까지 전체 흐름을
 * 처리합니다.
 *
 * 주요 처리 흐름:
 * <ol>
 * <li>{@link ServiceContextHolder}를 통해 {@link IServiceContext} 획득</li>
 * <li>{@link ServiceInfo}를 기반으로 실행 대상 컴포넌트 및 메서드 메타데이터 해석</li>
 * <li>{@link #readInputMessage(HttpServletRequest, ServiceMethodMetadata)}로 DTO
 * 역직렬화</li>
 * <li>JSR-380 기반 {@link #validateInputDto(IMessageObject)}로 입력 검증</li>
 * <li>{@link IServiceComponentExecutor}를 통해 실제 비즈니스 컴포넌트 실행</li>
 * <li>{@link #buildResponseMessage(IMessageObject)}로 응답 메시지 생성 후 HTTP 응답으로
 * 변환</li>
 * </ol>
 *
 * 해당 컨트롤러는 `framework.channel.enabled=true`일 때만 등록됩니다.
 *
 * @author sungdon.choi
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@ConditionalOnProperty(name = ChannelConstants.FRAMEWORK_CHANNEL_ENABLED, havingValue = "true")
public class DefaultServiceDispatchController extends AbstractServiceDispatchController {

    /**
     * 채널 요청 진입점.
     *
     * <p>
     * HTTP 요청을 기반으로 등록된 컴포넌트를 실행하고, 결과 DTO를 응답 메시지로 변환합니다.
     * </p>
     *
     * @param request HTTP 요청 객체
     * @return HTTP 200 OK + 업무 응답 메시지 (ResponseEntity)
     * @throws Exception 업무 컴포넌트 실행 또는 메시지 처리 중 예외 발생 가능
     */
    public ResponseEntity<?> dispatch(
            HttpServletRequest request,
            @RequestBody(required = false) byte[] inputBytes) throws Throwable {
        // 1. 서비스 컨텍스트 획득
        IServiceContext serviceContext = ServiceContextHolder.getContext();

        // 2. 컴포넌트/메서드 정보 해석
        ServiceInfo resolvedService = this.resolveService(serviceContext);
        if (log.isDebugEnabled()) {
            log.debug("# 선택된 서비스 정보: {}", resolvedService);
        }

        String component = resolvedService.getComponent();
        ServiceMethodMetadata resolvedServiceMetadata = this.getServiceRegistrar().getServiceMethodMetadata(component);

        // 3. 요청 메시지 → DTO 변환
        IMessageObject input = this.readInputMessage(request, inputBytes, resolvedServiceMetadata);
        if (input != null) {
            // 4. Bean Validation 수행
            this.validateInputDto(input);
        }

        // 5. 업무 컴포넌트 실행
        IServiceComponentExecutor serviceExecutor = this.getServiceExecutor(resolvedServiceMetadata);
        IMessageObject output = serviceExecutor.execute(serviceContext, input);

        if (log.isTraceEnabled()) {
            log.trace("# output: {}", output);
        }

        // 6. 응답 메시지
        IResponseMessage<?, ?> responseMessage = this.buildResponseMessage(output);
        // 7. ResponseEntity 변환
        return this.getResponseRendererComposite().render(responseMessage, serviceContext);
    }
}
