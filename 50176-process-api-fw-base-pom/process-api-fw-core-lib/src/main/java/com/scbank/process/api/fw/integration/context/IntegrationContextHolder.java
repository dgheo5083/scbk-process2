package com.scbank.process.api.fw.integration.context;

public class IntegrationContextHolder {

    private static ThreadLocal<IntegrationContext> threadLocal = new ThreadLocal<>();

    /**
     * 현재 스레드에 저장된 IntegrationContext 반환합니다.
     *
     * @return 현재 Context, 없으면 null
     */
    public static IntegrationContext get() {
        return threadLocal.get();
    }

    /**
     * 현재 스레드에 IntegrationContext를 설정합니다.
     *
     * @param ctx 설정할 IntegrationContext
     */
    public static void set(IntegrationContext ctx) {
        threadLocal.set(ctx);
    }

    /**
     * 현재 스레드의 IntegrationContext를 제거합니다.
     * (메모리 누수 방지를 위해 반드시 호출)
     */
    public static void clear() {
        threadLocal.remove();
    }
}
