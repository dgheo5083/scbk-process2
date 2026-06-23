package com.scbank.process.api.fw.channel.service.interceptor;

import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.message.IMessageObject;

/**
 * <pre>
 * 서비스 실행 전후에 공통 로직을 수행할 수 있는 인터셉터 인터페이스입니다.
 *
 * Dispatcher 흐름에서 서비스 컴포넌트를 실행하기 전과 후에
 * preHandle / postHandle 메서드를 통해 로직을 주입할 수 있으며,
 * 주로 인증, 로깅, SLA 측정, 트레이싱, 마스킹 등에 활용됩니다.
 * </pre>
 *
 * <p>
 * 구현체는 service-definition.xml 또는 설정 파일에서 interceptor ID로 등록되어야 합니다.
 * </p>
 *
 * <p>
 * 예시 구현:
 * </p>
 * 
 * <pre>{@code
 * @Component("traceInterceptor")
 * public class TraceLoggingInterceptor implements IServiceInterceptor {
 *     public boolean preHandle(IServiceContext ctx, IMessageObject input) {
 *         log.info("요청 시작: {}", ctx.getTraceId());
 *         return true;
 *     }
 * 
 *     public void postHandle(IServiceContext ctx, IMessageObject output) {
 *         log.info("요청 완료: {}", ctx.getTraceId());
 *     }
 * }
 * }</pre>
 *
 * @author gasigol
 * @version 1.0
 * @since 2025.04.16
 */
public interface IServiceInterceptor {

    /**
     * 서비스 실행 직전에 호출됩니다.
     *
     * @param ctx   서비스 실행 컨텍스트
     * @param input 요청 DTO
     * @param <T>   입력 메시지 타입
     * @return true: 이후 로직 계속 진행 / false: 중단 또는 skip
     */
    default <T extends IMessageObject> boolean preHandle(IServiceContext ctx, T input) {
        return false;
    }

    /**
     * 서비스 실행 직후에 호출됩니다.
     *
     * @param ctx    서비스 실행 컨텍스트
     * @param output 응답 DTO
     * @param <T>    출력 메시지 타입
     */
    default <T extends IMessageObject> void postHandle(IServiceContext ctx, T output) {
        // 기본은 no-op
    }
}
