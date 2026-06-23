package com.scbank.process.api.fw.channel.context;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.scbank.process.api.fw.channel.device.IDevice;
import com.scbank.process.api.fw.channel.service.metadata.ServiceDefinitionMetadata;
import com.scbank.process.api.fw.session.ISessionContext;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * ClonedServiceContext Test Class
 */
@ExtendWith(MockitoExtension.class)
class ClonedServiceContextTest {

    private ClonedServiceContext clonedContext;

    @Mock
    private IServiceContext sourceContext;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private IDevice device;

    @Mock
    private ServiceDefinitionMetadata serviceDefinition;

    @Mock
    private ISessionContext session;

    @BeforeEach
    void setUp() {
        clonedContext = new ClonedServiceContext(sourceContext);
    }

    @Nested
    @DisplayName("Delegation tests")
    class DelegationTests {

        @Test
        @DisplayName("Should delegate channelId to source")
        void shouldDelegateChannelIdToSource() {
            when(sourceContext.channelId()).thenReturn("channel1");

            assertEquals("channel1", clonedContext.channelId());
            verify(sourceContext).channelId();
        }

        @Test
        @DisplayName("Should delegate requestUId to source")
        void shouldDelegateRequestUIdToSource() {
            when(sourceContext.requestUId()).thenReturn("request-123");

            assertEquals("request-123", clonedContext.requestUId());
            verify(sourceContext).requestUId();
        }

        @Test
        @DisplayName("Should delegate request to source")
        void shouldDelegateRequestToSource() {
            when(sourceContext.request()).thenReturn(request);

            assertSame(request, clonedContext.request());
            verify(sourceContext).request();
        }

        @Test
        @DisplayName("Should delegate response to source")
        void shouldDelegateResponseToSource() {
            when(sourceContext.response()).thenReturn(response);

            assertSame(response, clonedContext.response());
            verify(sourceContext).response();
        }

        @Test
        @DisplayName("Should delegate device to source")
        void shouldDelegateDeviceToSource() {
            when(sourceContext.device()).thenReturn(device);

            assertSame(device, clonedContext.device());
            verify(sourceContext).device();
        }

        @Test
        @DisplayName("Should delegate locale to source")
        void shouldDelegateLocaleToSource() {
            when(sourceContext.locale()).thenReturn(Locale.US);

            assertEquals(Locale.US, clonedContext.locale());
            verify(sourceContext).locale();
        }

        @Test
        @DisplayName("Should delegate serviceDefinition to source")
        void shouldDelegateServiceDefinitionToSource() {
            when(sourceContext.serviceDefinition()).thenReturn(serviceDefinition);

            assertSame(serviceDefinition, clonedContext.serviceDefinition());
            verify(sourceContext).serviceDefinition();
        }

        @Test
        @DisplayName("Should delegate session to source")
        void shouldDelegateSessionToSource() {
            when(sourceContext.session()).thenReturn(session);

            assertSame(session, clonedContext.session());
            verify(sourceContext).session();
        }
    }

    @Nested
    @DisplayName("attribute tests")
    class AttributeTests {

        @Test
        @DisplayName("Should delegate attribute(String) to source")
        void shouldDelegateAttributeByNameToSource() {
            when(sourceContext.attribute("key1")).thenReturn("value1");

            assertEquals("value1", clonedContext.attribute("key1"));
            verify(sourceContext).attribute("key1");
        }

        @Test
        @DisplayName("Should delegate attribute(String, Class) to source")
        void shouldDelegateAttributeByNameAndTypeToSource() {
            when(sourceContext.attribute("key1", String.class)).thenReturn("value1");

            assertEquals("value1", clonedContext.attribute("key1", String.class));
            verify(sourceContext).attribute("key1", String.class);
        }

        @Test
        @DisplayName("Should delegate setAttribute to source")
        void shouldDelegateSetAttributeToSource() {
            clonedContext.setAttribute("newKey", "newValue");

            verify(sourceContext).setAttribute("newKey", "newValue");
        }
    }

    @Nested
    @DisplayName("parameter tests")
    class ParameterTests {

        @Test
        @DisplayName("Should delegate parameter to source")
        void shouldDelegateParameterToSource() {
            when(sourceContext.parameter("param1")).thenReturn("paramValue1");

            assertEquals("paramValue1", clonedContext.parameter("param1"));
            verify(sourceContext).parameter("param1");
        }
    }

    @Nested
    @DisplayName("Record accessor tests")
    class RecordAccessorTests {

        @Test
        @DisplayName("Should return source context via source() accessor")
        void shouldReturnSourceContextViaSourceAccessor() {
            assertSame(sourceContext, clonedContext.source());
        }
    }

    @Nested
    @DisplayName("Interface implementation tests")
    class InterfaceTests {

        @Test
        @DisplayName("Should implement IServiceContext")
        void shouldImplementIServiceContext() {
            assertTrue(clonedContext instanceof IServiceContext);
        }
    }
}
