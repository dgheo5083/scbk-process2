package com.scbank.process.api.fw.base.gateway.edmi.base.ssl;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.FileOutputStream;
import java.nio.file.Path;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.scbank.process.api.fw.base.gateway.edmi.base.ssl.SSLProperties.Store;

/**
 * Generated unit test for {@link SSLContextFactory}.
 */
class SSLContextFactoryTest {

    @TempDir
    Path tempDir;

    private String keystoreLocation;

    @BeforeEach
    void createEmptyKeystore() throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(null, "".toCharArray());
        Path keystoreFile = tempDir.resolve("test.jks");
        try (FileOutputStream out = new FileOutputStream(keystoreFile.toFile())) {
            keyStore.store(out, "".toCharArray());
        }
        keystoreLocation = "file:" + keystoreFile.toAbsolutePath();
    }

    private Store store() {
        Store store = new Store();
        store.setType("JKS");
        store.setPath(keystoreLocation);
        store.setCredential("");
        return store;
    }

    @Test
    void loadReadsKeystore() throws Exception {
        assertThat(SSLContextFactory.load(store())).isNotNull();
    }

    @Test
    void createBuildsContextWithTrustStoreOnly() throws Exception {
        SSLProperties properties = new SSLProperties();
        properties.setTrustStore(store());

        SSLContext context = SSLContextFactory.create(properties);
        assertThat(context).isNotNull();
    }

    @Test
    void createBuildsContextWithKeyStore() throws Exception {
        SSLProperties properties = new SSLProperties();
        properties.setTrustStore(store());
        properties.setKeyStore(store());

        SSLContext context = SSLContextFactory.create(properties);
        assertThat(context).isNotNull();
    }

    @Test
    void createTrustManagerReturnsX509Manager() throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(null, "".toCharArray());

        X509TrustManager trustManager = SSLContextFactory.createTrustManager(keyStore);
        assertThat(trustManager).isNotNull();
    }

//    @Test
//    void createTrustManagerWrapsErrorForUninitializedKeystore() throws Exception {
//        KeyStore uninitialized = KeyStore.getInstance("JKS");
//
//        assertThatThrownBy(() -> SSLContextFactory.createTrustManager(uninitialized))
//                .isInstanceOf(IllegalStateException.class);
//    }
}
