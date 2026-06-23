package com.scbank.process.api.svc.shared.config;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.ClientTlsStrategyBuilder;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.TlsSocketStrategy;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.ssl.TrustStrategy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.scbank.process.api.fw.core.component.ComponentOperation;

/**
 * HTTP 외부연계 처리 기본 RestClient Spring Configuration
 */
@Configuration
public class HttpClientConfig {

    /**
     * 일반 httpClient 빈
     * 
     * @return
     */
    @Bean("httpClient")
    public CloseableHttpClient httpClient() {
        int maxTotal = 200;
        int maxPerRoute = 50;

        PoolingHttpClientConnectionManager cm = PoolingHttpClientConnectionManagerBuilder.create()
                .setMaxConnTotal(maxTotal)
                .setMaxConnPerRoute(maxPerRoute)
                .build();
        return HttpClients.custom()
                .setConnectionManager(cm)
                .build();
    }

    /**
     * secure httpClient 빈
     * 
     * @return
     * @throws Exception
     */
    @Bean("httpsClient")
    public CloseableHttpClient httpsClient() throws Exception {
        int maxTotal = 200;
        int maxPerRoute = 50;

        // trustStrategy
        TrustStrategy trustStrategy = new TrustStrategy() {
            @Override
            public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                return true;
            }
        };

        // sslContext
        SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, trustStrategy).build();

        TlsSocketStrategy tlsStrategy = ClientTlsStrategyBuilder.create()
                .setSslContext(sslContext)
                .setHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                .buildClassic();

        PoolingHttpClientConnectionManager cm = PoolingHttpClientConnectionManagerBuilder.create()
                .setMaxConnTotal(maxTotal)
                .setMaxConnPerRoute(maxPerRoute)
                .setTlsSocketStrategy(tlsStrategy)
                .build();
        return HttpClients.custom()
                .setConnectionManager(cm)
                .build();
    }

    @Component
    public static class HttpClientFactory {
        /**
         * 일반 httpClient
         */
        private final CloseableHttpClient httpClient;

        /**
         * secure httpClient
         */
        private final CloseableHttpClient httpsClient;

        /**
         * 생성자
         * 
         * @param httpClient  일반 httpClient
         * @param httpsClient secure httpClient
         */
        public HttpClientFactory(
                @Qualifier("httpClient") CloseableHttpClient httpClient,
                @Qualifier("httpsClient") CloseableHttpClient httpsClient) {
            this.httpClient = httpClient;
            this.httpsClient = httpsClient;
        }

        @ComponentOperation(name = "createHttpClient")
        public CloseableHttpClient createHttpClient(String scheme) {
            if ("https".equals(scheme)) {
                return this.httpsClient;
            }
            return this.httpClient;
        }
    }
}
