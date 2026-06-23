package com.scbank.process.api.fw.channel.message;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.scbank.process.api.fw.message.IMessageObject;

/**
 * DefaultResponseMessage Test Class
 */
@ExtendWith(MockitoExtension.class)
class DefaultResponseMessageTest {

    private DefaultResponseMessage<IMessageObject, IMessageObject> responseMessage;

    @Mock
    private IMessageObject header;

    @Mock
    private IMessageObject body;

    @BeforeEach
    void setUp() {
        responseMessage = new DefaultResponseMessage<>();
    }

    @Nested
    @DisplayName("Getter/Setter tests")
    class GetterSetterTests {

        @Test
        @DisplayName("Should get and set header")
        void shouldGetAndSetHeader() {
            responseMessage.setHeader(header);

            assertSame(header, responseMessage.getHeader());
        }

        @Test
        @DisplayName("Should get and set body")
        void shouldGetAndSetBody() {
            responseMessage.setBody(body);

            assertSame(body, responseMessage.getBody());
        }

        @Test
        @DisplayName("Should allow null header")
        void shouldAllowNullHeader() {
            responseMessage.setHeader(null);

            assertNull(responseMessage.getHeader());
        }

        @Test
        @DisplayName("Should allow null body")
        void shouldAllowNullBody() {
            responseMessage.setBody(null);

            assertNull(responseMessage.getBody());
        }
    }

    @Nested
    @DisplayName("Interface implementation tests")
    class InterfaceTests {

        @Test
        @DisplayName("Should implement IResponseMessage")
        void shouldImplementIResponseMessage() {
            assertTrue(responseMessage instanceof IResponseMessage);
        }
    }

    @Nested
    @DisplayName("Generic type tests")
    class GenericTypeTests {

        @Test
        @DisplayName("Should support typed header and body")
        void shouldSupportTypedHeaderAndBody() {
            DefaultResponseMessage<TestHeader, TestBody> typedMessage = new DefaultResponseMessage<>();

            TestHeader testHeader = new TestHeader();
            TestBody testBody = new TestBody();

            typedMessage.setHeader(testHeader);
            typedMessage.setBody(testBody);

            assertSame(testHeader, typedMessage.getHeader());
            assertSame(testBody, typedMessage.getBody());
        }
    }

    @Nested
    @DisplayName("equals and hashCode tests")
    class EqualsAndHashCodeTests {

        @Test
        @DisplayName("Should be equal to itself")
        void shouldBeEqualToItself() {
            responseMessage.setHeader(header);
            responseMessage.setBody(body);

            assertEquals(responseMessage, responseMessage);
        }

        @Test
        @DisplayName("Should be equal to another message with same values")
        void shouldBeEqualToAnotherMessageWithSameValues() {
            DefaultResponseMessage<IMessageObject, IMessageObject> anotherMessage = new DefaultResponseMessage<>();

            responseMessage.setHeader(header);
            responseMessage.setBody(body);

            anotherMessage.setHeader(header);
            anotherMessage.setBody(body);

            assertEquals(responseMessage, anotherMessage);
            assertEquals(responseMessage.hashCode(), anotherMessage.hashCode());
        }
    }

    // Helper classes for testing
    private static class TestHeader implements IMessageObject {
        private static final long serialVersionUID = 1L;
    }

    private static class TestBody implements IMessageObject {
        private static final long serialVersionUID = 1L;
    }
}
