package com.scbank.process.api.fw.channel.message.file;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.channel.message.IResponseMessage;

/**
 * FileDownloadResponseMessage Test Class
 */
class FileDownloadResponseMessageTest {

    private FileDownloadResponseMessage responseMessage;
    private FileDownloadObject fileDownloadObject;

    @BeforeEach
    void setUp() {
        responseMessage = new FileDownloadResponseMessage();
        fileDownloadObject = FileDownloadObject.builder()
                .filename("test-document.pdf")
                .contentType("application/pdf")
                .fileData("PDF content".getBytes())
                .build();
    }

    @Nested
    @DisplayName("Getter/Setter tests")
    class GetterSetterTests {

        @Test
        @DisplayName("Should get and set body")
        void shouldGetAndSetBody() {
            responseMessage.setBody(fileDownloadObject);

            assertSame(fileDownloadObject, responseMessage.getBody());
        }

        @Test
        @DisplayName("Should get and set header (always null for Void type)")
        void shouldGetAndSetHeader() {
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

        @Test
        @DisplayName("Should implement IResponseMessage with correct generic types")
        void shouldImplementIResponseMessageWithCorrectGenericTypes() {
            IResponseMessage<Void, FileDownloadObject> typedMessage = responseMessage;

            assertNotNull(typedMessage);
        }
    }

    @Nested
    @DisplayName("Header behavior tests")
    class HeaderBehaviorTests {

        @Test
        @DisplayName("Header should be null by default")
        void headerShouldBeNullByDefault() {
            assertNull(responseMessage.getHeader());
        }

        @Test
        @DisplayName("Header should remain null after body is set")
        void headerShouldRemainNullAfterBodyIsSet() {
            responseMessage.setBody(fileDownloadObject);

            assertNull(responseMessage.getHeader());
        }
    }

    @Nested
    @DisplayName("Body content tests")
    class BodyContentTests {

        @Test
        @DisplayName("Should access FileDownloadObject properties through body")
        void shouldAccessFileDownloadObjectPropertiesThroughBody() {
            responseMessage.setBody(fileDownloadObject);

            assertEquals("test-document.pdf", responseMessage.getBody().getFilename());
            assertEquals("application/pdf", responseMessage.getBody().getContentType());
            assertNotNull(responseMessage.getBody().getFileData());
        }
    }

    @Nested
    @DisplayName("equals and hashCode tests")
    class EqualsAndHashCodeTests {

        @Test
        @DisplayName("Should be equal to itself")
        void shouldBeEqualToItself() {
            assertEquals(responseMessage, responseMessage);
        }

        @Test
        @DisplayName("Should be equal to another message with same values")
        void shouldBeEqualToAnotherMessageWithSameValues() {
            FileDownloadResponseMessage another = new FileDownloadResponseMessage();
            another.setBody(fileDownloadObject);
            responseMessage.setBody(fileDownloadObject);

            assertEquals(responseMessage, another);
        }

        @Test
        @DisplayName("Should not be equal when body differs")
        void shouldNotBeEqualWhenBodyDiffers() {
            FileDownloadResponseMessage another = new FileDownloadResponseMessage();
            FileDownloadObject differentBody = FileDownloadObject.builder()
                    .filename("different.pdf")
                    .build();

            another.setBody(differentBody);
            responseMessage.setBody(fileDownloadObject);

            assertNotEquals(responseMessage, another);
        }
    }

    @Nested
    @DisplayName("toString tests")
    class ToStringTests {

        @Test
        @DisplayName("Should generate meaningful toString")
        void shouldGenerateMeaningfulToString() {
            responseMessage.setBody(fileDownloadObject);

            String toString = responseMessage.toString();

            assertNotNull(toString);
            // toString should include class information
        }
    }
}
