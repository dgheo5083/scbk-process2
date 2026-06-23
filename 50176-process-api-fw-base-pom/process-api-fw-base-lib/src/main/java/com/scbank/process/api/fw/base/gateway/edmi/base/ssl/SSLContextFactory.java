package com.scbank.process.api.fw.base.gateway.edmi.base.ssl;

import java.security.KeyStore;
import java.security.SecureRandom;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.springframework.core.io.DefaultResourceLoader;

import com.scbank.process.api.fw.base.gateway.edmi.base.ssl.SSLProperties.Store;
import com.scbank.process.api.fw.core.utils.StringUtils;

/**
 * 
 */
public class SSLContextFactory {

    /**
     * 
     * @param properties
     * @return
     * @throws Exception
     */
    public static SSLContext create(SSLProperties properties) throws Exception {
        KeyStore trust = load(properties.getTrustStore());

        KeyManager[] kms = null;
        if (properties.getKeyStore() != null) {
            Store keyStore = properties.getKeyStore();
            KeyStore key = load(properties.getKeyStore());
            var kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(key, keyStore.getCredential().toCharArray());
            kms = kmf.getKeyManagers();
        }

        var tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(trust);

        SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(kms, tmf.getTrustManagers(), new SecureRandom());
        return ctx;
    }
    
    /**
     * 
     * @param keyStore
     * @return
     */
    public static X509TrustManager createTrustManager(KeyStore keyStore) {
    	try {
    		TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    		tmf.init(keyStore);
    		
    		for (var tm : tmf.getTrustManagers()) {
                if (tm instanceof X509TrustManager x) {
                    return x;
                }
            }
    		throw new IllegalStateException("X509TrustManager not found");
    	} catch (IllegalStateException e) {
    		throw e;
    	} catch (Exception e) {
    		throw new IllegalStateException(e);
    	}
    }

    /**
     * 
     * @param trustStore
     * @return
     * @throws Exception
     */
    public static KeyStore load(Store trustStore) throws Exception {
        String type = StringUtils.defaultIfEmpty(trustStore.getType(), "");
        String path = StringUtils.defaultIfEmpty(trustStore.getPath(), "");
        String password = StringUtils.defaultIfEmpty(trustStore.getCredential(), "");

        KeyStore keyStore = KeyStore.getInstance(type);
        
        try (var in = new DefaultResourceLoader().getResource(path).getInputStream()) {
            keyStore.load(in, password.toCharArray());
        }
        return keyStore;
    }
}
