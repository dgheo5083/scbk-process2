package com.scbank.process.api.fw.base.gateway.edmi.base.ssl;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "edmi.gateway.default.ssl")
public class SSLProperties {

    /**
     * 사용여부
     */
    private boolean enabled;

    /**
     * 
     */
    private Store trustStore;

    /**
     * 
     */
    private Store keyStore;

    @Data
    public static class Store {

        private String path;

        private String credential;

        private String type;
    }
}
