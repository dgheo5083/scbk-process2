package com.scbank.process.api.fw.channel.context;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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
 * DefaultServiceContext Test Class
 */
@ExtendWith(MockitoExtension.class)
class DefaultServiceContextTest {

    private DefaultServiceContext context;

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

    private Map<String, Object> attributes;
    private Map<String, String> serviceParameters;

    @BeforeEach
    void setUp() {
        attributes = new HashMap<>();
        attributes.put("key1", "value1");
        attributes.put("key2", 123);

        serviceParameters = new HashMap<>();
        serviceParameters.put("param1", "paramValue1");
        serviceParameters.put("param2", "paramValue2");

        context = new DefaultServiceContext(
                "channel1",
                "request-uuid-123",
                request,
                response,
                device,
                Locale.KOREA,
                serviceDefinition,
                session,
                attributes,
                serviceParameters
        );
    }

    @Nested
    @DisplayName("Record accessor tests")
    class RecordAccessorTests {

        @Test
        @DisplayName("Should return correct channelId")
        void shouldReturnCorrectChannelId() {
            assertEquals("channel1", context.channelId());
        }

        @Test
        @DisplayName("Should return correct requestUId")
        void shouldReturnCorrectRequestUId() {
            assertEquals("request-uuid-123", context.requestUId());
        }

        @Test
        @DisplayName("Should return correct request")
        void shouldReturnCorrectRequest() {
            assertSame(request, context.request());
        }

        @Test
        @DisplayName("Should return correct response")
        void shouldReturnCorrectResponse() {
            assertSame(response, context.response());
        }

        @Test
        @DisplayName("Should return correct device")
        void shouldReturnCorrectDevice() {
            assertSame(device, context.device());
        }

        @Test
        @DisplayName("Should return correct locale")
        void shouldReturnCorrectLocale() {
            assertEquals(Locale.KOREA, context.locale());
        }

        @Test
        @DisplayName("Should return correct serviceDefinition")
        void shouldReturnCorrectServiceDefinition() {
            assertSame(serviceDefinition, context.serviceDefinition());
        }

        @Test
        @DisplayName("Should return correct session")
        void shouldReturnCorrectSession() {
            assertSame(session, context.session());
        }

        @Test
        @DisplayName("Should return correct attribute map")
        void shouldReturnCorrectAttributeMap() {
            assertEquals(attributes, context.attribute());
        }

        @Test
        @DisplayName("Should return correct serviceParameter map")
        void shouldReturnCorrectServiceParameterMap() {
            assertEquals(serviceParameters, context.serviceParameter());
        }
    }

    @Nested
    @DisplayName("attribute(String) tests")
    class AttributeByNameTests {

        @Test
        @DisplayName("Should return attribute value for existing key")
        void shouldReturnAttributeValueForExistingKey() {
            assertEquals("value1", context.attribute("key1"));
        }

        @Test
        @DisplayName("Should return null for non-existing key")
        void shouldReturnNullForNonExistingKey() {
            assertNull(context.attribute("nonExistingKey"));
        }

        @Test
        @DisplayName("Should return null for null key")
        void shouldReturnNullForNullKey() {
            assertNull(context.attribute(null));
        }
    }

    @Nested
    @DisplayName("attribute(String, Class) tests")
    class AttributeByNameAndTypeTests {

        @Test
        @DisplayName("Should return typed attribute value for existing key")
        void shouldReturnTypedAttributeValueForExistingKey() {
            String result = context.attribute("key1", String.class);
            assertEquals("value1", result);
        }

        @Test
        @DisplayName("Should return Integer typed attribute")
        void shouldReturnIntegerTypedAttribute() {
            Integer result = context.attribute("key2", Integer.class);
            assertEquals(123, result);
        }

        @Test
        @DisplayName("Should return null for non-existing key with type")
        void shouldReturnNullForNonExistingKeyWithType() {
            String result = context.attribute("nonExisting", String.class);
            assertNull(result);
        }
    }

    @Nested
    @DisplayName("setAttribute tests")
    class SetAttributeTests {

        @Test
        @DisplayName("Should set new attribute")
        void shouldSetNewAttribute() {
            context.setAttribute("newKey", "newValue");

            assertEquals("newValue", context.attribute("newKey"));
        }

        @Test
        @DisplayName("Should overwrite existing attribute")
        void shouldOverwriteExistingAttribute() {
            context.setAttribute("key1", "updatedValue");

            assertEquals("updatedValue", context.attribute("key1"));
        }
    }

    @Nested
    @DisplayName("parameter tests")
    class ParameterTests {

        @Test
        @DisplayName("Should return parameter value for existing key")
        void shouldReturnParameterValueForExistingKey() {
            assertEquals("paramValue1", context.parameter("param1"));
        }

        @Test
        @DisplayName("Should return null for non-existing parameter")
        void shouldReturnNullForNonExistingParameter() {
            assertNull(context.parameter("nonExisting"));
        }
    }

    @Nested
    @DisplayName("Interface implementation tests")
    class InterfaceTests {

        @Test
        @DisplayName("Should implement IServiceContext")
        void shouldImplementIServiceContext() {
            assertTrue(context instanceof IServiceContext);
        }
    }
}
