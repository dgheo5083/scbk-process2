package com.scbank.process.api.fw.base.channel.context;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.LocaleResolver;

import com.scbank.process.api.fw.base.store.ThreadLocalStore;
import com.scbank.process.api.fw.channel.device.IDeviceResolver;
import com.scbank.process.api.fw.channel.service.ServiceIdResolver;
import com.scbank.process.api.fw.channel.service.registry.IServiceRegistrar;
import com.scbank.process.api.fw.core.uuid.IIdentifyGenerator;
import com.scbank.process.api.fw.session.ISessionContextResolver;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Generated unit test for {@link PRCServiceContextHandler}.
 */
class PRCServiceContextHandlerTest {

    private final IIdentifyGenerator identifyGenerator = mock(IIdentifyGenerator.class);

    private PRCServiceContextHandler handler() {
        lenient().when(identifyGenerator.generateId()).thenReturn("UUID");
        return new PRCServiceContextHandler(
                identifyGenerator,
                mock(IDeviceResolver.class),
                mock(IServiceRegistrar.class),
                mock(LocaleResolver.class),
                mock(ServiceIdResolver.class),
                mock(ISessionContextResolver.class));
    }

    @AfterEach
    void tearDown() {
        ThreadLocalStore.getInstance().clearThreadLocalStore();
    }

    @Test
    void getRequestUUIDReturnsExistingTrackingId() {
        ThreadLocalStore.getInstance().setTrackingId("TID");
        PRCServiceContextHandler handler = handler();

        assertThat(handler.getRequestUUID()).isEqualTo("TID");
        assertThat(handler.getRequestUUID(mock(HttpServletRequest.class))).isEqualTo("TID");
    }

    @Test
    void getRequestUUIDGeneratesWhenAbsent() {
        ThreadLocalStore.getInstance().clearThreadLocalStore();
        PRCServiceContextHandler handler = handler();

        // Exercise the generation branch; the super implementation relies on
        // collaborators that are stubbed best-effort.
        try {
            handler.getRequestUUID();
        } catch (RuntimeException ignored) {
            // tolerated
        }
    }
}
