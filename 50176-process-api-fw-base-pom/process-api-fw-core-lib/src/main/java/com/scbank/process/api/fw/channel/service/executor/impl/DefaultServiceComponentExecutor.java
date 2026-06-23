package com.scbank.process.api.fw.channel.service.executor.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.service.argument.ServiceMethodArgumentResolverComposite;
import com.scbank.process.api.fw.channel.service.executor.IServiceComponentExecutor;
import com.scbank.process.api.fw.channel.service.interceptor.ServiceInterceptorComposite;
import com.scbank.process.api.fw.channel.service.metadata.ServiceMethodMetadata;
import com.scbank.process.api.fw.message.IMessageObject;

import lombok.RequiredArgsConstructor;

/**
 * <pre>
 * 프레임워크 기본 서비스 실행자 구현체입니다.
 *
 * - 서비스 메타데이터(ServiceMethodMetadata)에 정의된 메서드 정보를 기반으로
 *   동적으로 서비스 메서드를 호출합니다.
 * - 입력 파라미터는 {@link ServiceMethodArgumentResolverComposite}를 통해
 *   {@link IServiceContext} 및 입력 DTO로부터 자동 추론/바인딩됩니다.
 * - 내부적으로 Java Reflection API를 사용하여 실제 Bean의 메서드를 호출합니다.
 * </pre>
 *
 * <p>
 * Dispatcher → ExecutorFactory → DefaultServiceComponentExecutor 구조에서 실행을 담당하는
 * 최종 실행 엔진입니다.
 * </p>
 *
 * 예:
 * 
 * <pre>{@code
 * executor.execute(context, new UserLoginInput());
 * }</pre>
 *
 * @author sungdon.choi
 * @since 2025.04.16
 */
@RequiredArgsConstructor
public class DefaultServiceComponentExecutor implements IServiceComponentExecutor {

    /**
     * 실행할 서비스 메서드의 메타데이터
     */
    private final ServiceMethodMetadata methodMetadata;

    /**
     * 메서드 파라미터 자동 바인딩을 위한 Argument Resolver
     */
    private final ServiceMethodArgumentResolverComposite argumentResolver;

    /**
     * Service Interceptor Composite
     */
    private final ServiceInterceptorComposite serviceInterceptorComposite;

    /**
     * 실제 서비스 컴포넌트 인스턴스
     */
    private final Object handlerBean;

    /**
     * 실행 대상 메서드 (Reflection 기반)
     */
    private final Method handlerMethod;

    /**
     * 서비스 컴포넌트의 메서드를 실행합니다.
     *
     * @param context 서비스 실행 컨텍스트 (트레이스, 디바이스 정보 등 포함)
     * @param input   입력 DTO 또는 바인딩 대상 객체
     * @param <I>     입력 타입
     * @param <O>     출력 타입
     * @return 출력 DTO (메서드 반환 결과)
     * @throws Exception 실행 중 리플렉션 예외 또는 사용자 예외 발생 가능
     */
    @SuppressWarnings("unchecked")
    @Override
    public <I extends IMessageObject, O extends IMessageObject> O execute(IServiceContext context, I input)
            throws Throwable {
        try {

            if (this.serviceInterceptorComposite != null) {
                this.serviceInterceptorComposite.preHandle(context, input);
            }

            // 메타데이터 기반으로 파라미터 구성
            Object[] args = argumentResolver.resolveArguments(methodMetadata.getParameters(), context, input);

            // 리플렉션을 통해 메서드 실행
            O output = (O) handlerMethod.invoke(handlerBean, args);

            if (this.serviceInterceptorComposite != null) {
                this.serviceInterceptorComposite.postHandle(context, output);
            }

            return output;
        } catch (InvocationTargetException e) {
            throw e.getTargetException() != null ? e.getTargetException() : e.getCause();
        } catch (Exception e) {
            throw e;
        } finally {

        }
    }
}
