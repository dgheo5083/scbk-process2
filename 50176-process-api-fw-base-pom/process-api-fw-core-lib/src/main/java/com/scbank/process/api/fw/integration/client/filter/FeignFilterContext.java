package com.scbank.process.api.fw.integration.client.filter;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

public class FeignFilterContext implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private String systemId;

    @Getter
    @Setter
    private String interfaceId;

    @Getter
    @Setter
    private byte[] responseBytese;

    @Getter
    @Setter
    private String responseContentType;

    private final Map<String, Object> attributtes = new LinkedHashMap<>();

    public void setAttr(String key, Object value) {
        attributtes.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T getAttr(String key) {
        return (T) attributtes.get(key);
    }

    public Map<String, Object> atttrs() {
        return this.attributtes;
    }

    public static class FeignFilterContextHolder {

        private static ThreadLocal<FeignFilterContext> threadLocal = new ThreadLocal<>();

        /**
         * 현재 스레드에 저장된 FeignFilterContext를 반환합니다.
         *
         * @return 현재 Context, 없으면 null
         */
        public static FeignFilterContext get() {
            return threadLocal.get();
        }

        /**
         * 현재 스레드에 FeignFilterContext를 설정합니다.
         *
         * @param ctx 설정할 IntegrationContext
         */
        public static void set(FeignFilterContext ctx) {
            threadLocal.set(ctx);
        }

        /**
         * 현재 스레드의 FeignFilterContext를 제거합니다.
         * (메모리 누수 방지를 위해 반드시 호출)
         */
        public static void clear() {
            threadLocal.remove();
        }
    }
}
