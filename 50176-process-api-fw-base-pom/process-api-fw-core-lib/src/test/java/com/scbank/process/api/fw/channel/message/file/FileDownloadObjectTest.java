package com.scbank.process.api.fw.channel.message.file;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.message.IMessageObject;

/**
 * FileDownloadObject Test Class
 */
class FileDownloadObjectTest {

    private FileDownloadObject fileDownloadObject;
    private byte[] testData;

    @BeforeEach
    void setUp() {
        testData = "Test file content".getBytes();
        fileDownloadObject = FileDownloadObject.builder()
                .filename("test-file.pdf")
                .contentType("application/pdf")
                .fileData(testData)
                .build();
    }

    @Nested
    @DisplayName("Builder tests")
    class BuilderTests {

        @Test
        @DisplayName("Should create object with all fields")
        void shouldCreateObjectWithAllFields() {
            assertNotNull(fileDownloadObject);
            assertEquals("test-file.pdf", fileDownloadObject.getFilename());
            assertEquals("application/pdf", fileDownloadObject.getContentType());
            assertArrayEquals(testData, fileDownloadObject.getFileData());
        }

        @Test
        @DisplayName("Should create object with default values")
        void shouldCreateObjectWithDefaultValues() {
            FileDownloadObject emptyObject = FileDownloadObject.builder().build();

            assertNotNull(emptyObject);
            assertNull(emptyObject.getFilename());
            assertNull(emptyObject.getContentType());
            assertNull(emptyObject.getFileData());
        }
    }

    @Nested
    @DisplayName("Getter/Setter tests")
    class GetterSetterTests {

        @Test
        @DisplayName("Should get and set filename")
        void shouldGetAndSetFilename() {
            fileDownloadObject.setFilename("new-file.xlsx");
            assertEquals("new-file.xlsx", fileDownloadObject.getFilename());
        }

        @Test
        @DisplayName("Should get and set contentType")
        void shouldGetAndSetContentType() {
            fileDownloadObject.setContentType("application/vnd.ms-excel");
            assertEquals("application/vnd.ms-excel", fileDownloadObject.getContentType());
        }

        @Test
        @DisplayName("Should get and set fileData")
        void shouldGetAndSetFileData() {
            byte[] newData = "New content".getBytes();
            fileDownloadObject.setFileData(newData);
            assertArrayEquals(newData, fileDownloadObject.getFileData());
        }
    }

    @Nested
    @DisplayName("Interface implementation tests")
    class InterfaceTests {

        @Test
        @DisplayName("Should implement IMessageObject")
        void shouldImplementIMessageObject() {
            assertTrue(fileDownloadObject instanceof IMessageObject);
        }
    }

    @Nested
    @DisplayName("Various content types tests")
    class ContentTypeTests {

        @Test
        @DisplayName("Should handle PDF content type")
        void shouldHandlePdfContentType() {
            fileDownloadObject.setContentType("application/pdf");
            assertEquals("application/pdf", fileDownloadObject.getContentType());
        }

        @Test
        @DisplayName("Should handle Excel content type")
        void shouldHandleExcelContentType() {
            fileDownloadObject.setContentType("application/vnd.ms-excel");
            assertEquals("application/vnd.ms-excel", fileDownloadObject.getContentType());
        }

        @Test
        @DisplayName("Should handle image content type")
        void shouldHandleImageContentType() {
            fileDownloadObject.setContentType("image/png");
            assertEquals("image/png", fileDownloadObject.getContentType());
        }

        @Test
        @DisplayName("Should handle JSON content type")
        void shouldHandleJsonContentType() {
            fileDownloadObject.setContentType("application/json");
            assertEquals("application/json", fileDownloadObject.getContentType());
        }
    }

    @Nested
    @DisplayName("File data tests")
    class FileDataTests {

        @Test
        @DisplayName("Should handle empty file data")
        void shouldHandleEmptyFileData() {
            fileDownloadObject.setFileData(new byte[0]);
            assertEquals(0, fileDownloadObject.getFileData().length);
        }

        @Test
        @DisplayName("Should handle large file data")
        void shouldHandleLargeFileData() {
            byte[] largeData = new byte[1024 * 1024]; // 1MB
            fileDownloadObject.setFileData(largeData);
            assertEquals(1024 * 1024, fileDownloadObject.getFileData().length);
        }

        @Test
        @DisplayName("Should handle null file data")
        void shouldHandleNullFileData() {
            fileDownloadObject.setFileData(null);
            assertNull(fileDownloadObject.getFileData());
        }
    }

    @Nested
    @DisplayName("equals and hashCode tests")
    class EqualsAndHashCodeTests {

        @Test
        @DisplayName("Should be equal to itself")
        void shouldBeEqualToItself() {
            assertEquals(fileDownloadObject, fileDownloadObject);
        }

        @Test
        @DisplayName("Should be equal to another object with same values")
        void shouldBeEqualToAnotherObjectWithSameValues() {
            FileDownloadObject another = FileDownloadObject.builder()
                    .filename("test-file.pdf")
                    .contentType("application/pdf")
                    .fileData(testData)
                    .build();

            assertEquals(fileDownloadObject, another);
        }
    }
}
