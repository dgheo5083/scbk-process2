package com.scbank.process.api.fw.channel.service.interceptor;

import java.util.List;

import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.message.IMessageObject;

import lombok.RequiredArgsConstructor;

/**
 * <pre>
 * {@link IServiceInterceptor} 구현체들을 묶어 순차적으로 실행하는 Composite 구현 클래스입니다.
 *
 * - preHandle()은 인터셉터 리스트를 순차 실행하며 하나라도 false를 반환하면 이후 흐름을 중단합니다.
 * - postHandle()은 전체 인터셉터를 순차 실행하며 모든 후처리를 수행합니다.
 *
 * Dispatcher 또는 Executor는 이 클래스를 통해 서비스 단위 또는 글로벌 인터셉터 체인을 실행합니다.
 * </pre>
 *
 * <p>
 * 프레임워크의 인터셉터 체인 패턴을 구현한 핵심 클래스입니다.
 * </p>
 *
 * @author gasigol
 * @version 1.0
 * @since 2025.04.16
 */
@RequiredArgsConstructor
public class ServiceInterceptorComposite implements IServiceInterceptor {

    /**
     * 인터셉터 목록 (순차 실행됨)
     */
    private final List<IServiceInterceptor> interceptors;

    /**
     * 모든 인터셉터의 preHandle을 순차적으로 실행합니다.
     * 하나라도 false를 반환하면 체인을 중단하고 false 반환합니다.
     *
     * @param ctx   서비스 실행 컨텍스트
     * @param input 입력 메시지 DTO
     * @return true: 체인 계속 실행 / false: 중단
     */
    @Override
    public <T extends IMessageObject> boolean preHandle(IServiceContext ctx, T input) {
        for (IServiceInterceptor interceptor : interceptors) {
            if (!interceptor.preHandle(ctx, input)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 모든 인터셉터의 postHandle을 순차적으로 실행합니다.
     * 예외가 발생하지 않으면 무조건 전부 실행됩니다.
     *
     * @param ctx    서비스 실행 컨텍스트
     * @param output 응답 메시지 DTO
     */
    @Override
    public <T extends IMessageObject> void postHandle(IServiceContext ctx, T output) {
        for (IServiceInterceptor interceptor : interceptors) {
            interceptor.postHandle(ctx, output);
        }
    }
}
