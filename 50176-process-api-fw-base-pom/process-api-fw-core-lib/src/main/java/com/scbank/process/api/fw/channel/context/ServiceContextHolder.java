package com.scbank.process.api.fw.channel.context;

/**
 * 서비스 컨텍스트를 ThreadLocal로 관리하는 유틸리티 클래스
 *
 * <p>
 * 요청 처리 중 생성된 {@link IServiceContext} 객체를 현재 스레드에 보관하고,
 * 어디서든 접근 가능하도록 지원합니다.
 *
 * <p>
 * 요청 시작 시 {@link #setContext(IServiceContext)}로 설정하고,
 * 요청 종료 시 {@link #clear()}로 정리해야 합니다.
 *
 * <p>
 * 주의: ThreadLocal 누수 방지를 위해 반드시 clear() 호출 필요
 *
 * @author sungdon.choi
 */
public class ServiceContextHolder {

    /**
     * 현재 요청의 서비스 컨텍스트를 저장하는 ThreadLocal
     */
    public static ThreadLocal<IServiceContext> context = new ThreadLocal<>();

    /**
     * 현재 스레드에 저장된 서비스 컨텍스트를 반환합니다.
     *
     * @return {@link IServiceContext} 또는 null
     */
    public static IServiceContext getContext() {
        return context.get();
    }

    /**
     * 현재 스레드에 서비스 컨텍스트를 저장합니다.
     *
     * @param serviceContext 저장할 서비스 컨텍스트
     */
    public static void setContext(IServiceContext serviceContext) {
        context.set(serviceContext);
    }

    /**
     * 현재 스레드의 서비스 컨텍스트를 제거합니다.
     * <p>
     * 메모리 누수 방지를 위해 요청 종료 시 반드시 호출해야 합니다.
     */
    public static void clear() {
        context.remove();
    }
}
