package com.scbank.process.api.fw.channel.service.executor;

import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.message.IMessageObject;

/**
 * <pre>
 * Dispatcher 또는 프레임워크 내부에서 실제 서비스 컴포넌트의 메서드를 실행하기 위한 실행 전략 인터페이스입니다.
 *
 * 입력 객체(input)와 서비스 실행 컨텍스트(context)를 전달받아,
 * 정의된 서비스 컴포넌트의 메서드를 실행하고 응답 객체(output)를 반환합니다.
 * </pre>
 *
 * <p>
 * 해당 인터페이스의 구현체는 {@link IServiceComponentExecutorFactory}를 통해 동적으로 생성됩니다.
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
@FunctionalInterface
public interface IServiceComponentExecutor {

    /**
     * 서비스 컴포넌트를 실행합니다.
     *
     * @param context 서비스 실행 컨텍스트 (트레이스, 사용자 정보 등 포함)
     * @param input   입력 DTO 객체 (없을 수 있음)
     * @param <I>     입력 타입
     * @param <O>     출력 타입
     * @return 응답 DTO 객체
     * @throws Exception 실행 중 오류 발생 시
     */
    <I extends IMessageObject, O extends IMessageObject> O execute(IServiceContext context, I input) throws Throwable;
}
