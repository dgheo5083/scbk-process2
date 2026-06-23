package com.scbank.process.api.fw.core.log.trace;

import java.util.Optional;

/**
 * <pre>
 * ThreadLocal 기반 TraceContext 저장소
 * 요청 스레드마다 TraceContext 인스턴스를 안전하게 격리시켜 관리합니다.
 * 주로 AOP 또는 인터셉터에서 set()/clear()를 호출하고,
 * TraceLogger, ExceptionResolver 등에서 get()으로 접근합니다.
 *
 * 반드시 요청 시작 시점에서 set(), 종료 시점에서 clear()를 호출해야 합니다.
 * </pre>
 *
 * @author sungdon.choi
 * @since 25. 4. 23.
 */
public class TraceContextHolder {

    /**
     * 요청 스레드마다 TraceContext 인스턴스를 보관하는 ThreadLocal
     */
    private static final ThreadLocal<TraceContext> holder = new ThreadLocal<>();

    /**
     * TraceContext를 현재 스레드에 설정합니다.
     *
     * @param context 설정할 TraceContext
     */
    public static void set(TraceContext context) {
        holder.set(context);
    }

    /**
     * TraceContext를 제거합니다. 반드시 요청 종료 시 호출해야 합니다.
     */
    public static void clear() {
        holder.remove();
    }

    /**
     * 현재 스레드의 TraceContext를 반환합니다. 초기화되지 않은 경우 IllegalStateException 발생
     *
     * @return 현재 TraceContext
     */
    // public static TraceContext get() {
    // TraceContext context = holder.get();
    // // if (context == null) {
    // // throw new IllegalStateException("TraceContext is not initialized.");
    // // }
    // return context;
    // }

    /**
     * 현재 스레드의 TraceContext를 반환합니다.
     * 
     * @return 현재 TraceContext
     */
    public static Optional<TraceContext> get() {
        TraceContext context = holder.get();
        return Optional.ofNullable(context);
    }
}
