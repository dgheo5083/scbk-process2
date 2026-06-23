package com.scbank.process.api.fw.base.store.secure;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Constructor;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.base.store.secure.vo.SecureContext;

/**
 * Generated unit test for {@link SecureContextStore}.
 */
class SecureContextStoreTest {

    @AfterEach
    void tearDown() {
        SecureContextStore.clearContext();
    }

    @Test
    void getContextEmptyWhenUnset() {
        SecureContextStore.clearContext();
        assertThat(SecureContextStore.getContext()).isEmpty();
    }

    @Test
    void setAndClearContext() {
        SecureContext context = new SecureContext();
        SecureContextStore.setContext(context);
        assertThat(SecureContextStore.getContext()).containsSame(context);

        SecureContextStore.clearContext();
        assertThat(SecureContextStore.getContext()).isEmpty();
    }

    @Test
    void privateConstructorIsInvocable() throws Exception {
        Constructor<SecureContextStore> constructor = SecureContextStore.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        assertThat(constructor.newInstance()).isNotNull();
    }
}
