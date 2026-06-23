package com.scbank.process.api.fw.message;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * {@link MessageFormatOptionConfig} 단위 테스트
 */
@DisplayName("MessageFormatOptionConfig 테스트")
class MessageFormatOptionConfigTest {

    private MessageFormatOptionConfig config;

    @BeforeEach
    void setUp() {
        config = new MessageFormatOptionConfig();
    }

    @Nested
    @DisplayName("serialization 속성 테스트")
    class SerializationPropertyTests {

        @Test
        @DisplayName("기본값은 null이다")
        void defaultSerializationIsNull() {
            // then
            assertNull(config.getSerialization());
        }

        @Test
        @DisplayName("serialization을 설정할 수 있다")
        void setSerialization() {
            // given
            Map<String, Object> serialization = new HashMap<>();
            serialization.put("PADDING", true);
            serialization.put("LENGTH_CHECK", true);

            // when
            config.setSerialization(serialization);

            // then
            assertNotNull(config.getSerialization());
            assertEquals(2, config.getSerialization().size());
            assertEquals(true, config.getSerialization().get("PADDING"));
            assertEquals(true, config.getSerialization().get("LENGTH_CHECK"));
        }

        @Test
        @DisplayName("빈 Map을 설정할 수 있다")
        void setEmptySerialization() {
            // when
            config.setSerialization(new HashMap<>());

            // then
            assertNotNull(config.getSerialization());
            assertTrue(config.getSerialization().isEmpty());
        }
    }

    @Nested
    @DisplayName("deserialization 속성 테스트")
    class DeserializationPropertyTests {

        @Test
        @DisplayName("기본값은 null이다")
        void defaultDeserializationIsNull() {
            // then
            assertNull(config.getDeserialization());
        }

        @Test
        @DisplayName("deserialization을 설정할 수 있다")
        void setDeserialization() {
            // given
            Map<String, Object> deserialization = new HashMap<>();
            deserialization.put("UNPADDING", true);
            deserialization.put("FIELD_TRIM", true);

            // when
            config.setDeserialization(deserialization);

            // then
            assertNotNull(config.getDeserialization());
            assertEquals(2, config.getDeserialization().size());
            assertEquals(true, config.getDeserialization().get("UNPADDING"));
            assertEquals(true, config.getDeserialization().get("FIELD_TRIM"));
        }

        @Test
        @DisplayName("빈 Map을 설정할 수 있다")
        void setEmptyDeserialization() {
            // when
            config.setDeserialization(new HashMap<>());

            // then
            assertNotNull(config.getDeserialization());
            assertTrue(config.getDeserialization().isEmpty());
        }
    }

    @Nested
    @DisplayName("Lombok 생성 메서드 테스트")
    class LombokMethodsTests {

        @Test
        @DisplayName("toString() 메서드가 정상 동작한다")
        void toStringMethod() {
            // when
            String result = config.toString();

            // then
            assertNotNull(result);
            assertTrue(result.contains("MessageFormatOptionConfig"));
        }

        @Test
        @DisplayName("equals() 메서드가 정상 동작한다")
        void equalsMethod() {
            // given
            MessageFormatOptionConfig other = new MessageFormatOptionConfig();

            // then
            assertEquals(config, other);
        }

        @Test
        @DisplayName("같은 값을 가진 객체는 equal하다")
        void equalObjectsWithSameValues() {
            // given
            Map<String, Object> serialization = new HashMap<>();
            serialization.put("PADDING", true);

            config.setSerialization(serialization);

            MessageFormatOptionConfig other = new MessageFormatOptionConfig();
            other.setSerialization(new HashMap<>(serialization));

            // then
            assertEquals(config, other);
        }

        @Test
        @DisplayName("hashCode() 메서드가 정상 동작한다")
        void hashCodeMethod() {
            // given
            MessageFormatOptionConfig other = new MessageFormatOptionConfig();

            // then
            assertEquals(config.hashCode(), other.hashCode());
        }
    }

    @Nested
    @DisplayName("복합 시나리오 테스트")
    class ComplexScenarioTests {

        @Test
        @DisplayName("serialization과 deserialization을 모두 설정할 수 있다")
        void setBothOptions() {
            // given
            Map<String, Object> serialization = new HashMap<>();
            serialization.put("PADDING", true);

            Map<String, Object> deserialization = new HashMap<>();
            deserialization.put("UNPADDING", true);

            // when
            config.setSerialization(serialization);
            config.setDeserialization(deserialization);

            // then
            assertNotNull(config.getSerialization());
            assertNotNull(config.getDeserialization());
            assertEquals(true, config.getSerialization().get("PADDING"));
            assertEquals(true, config.getDeserialization().get("UNPADDING"));
        }

        @Test
        @DisplayName("다양한 타입의 값을 설정할 수 있다")
        void setVariousValueTypes() {
            // given
            Map<String, Object> options = new HashMap<>();
            options.put("BOOLEAN_OPTION", true);
            options.put("STRING_OPTION", "UTF-8");
            options.put("INTEGER_OPTION", 10);
            options.put("DOUBLE_OPTION", 3.14);

            // when
            config.setSerialization(options);

            // then
            assertEquals(true, config.getSerialization().get("BOOLEAN_OPTION"));
            assertEquals("UTF-8", config.getSerialization().get("STRING_OPTION"));
            assertEquals(10, config.getSerialization().get("INTEGER_OPTION"));
            assertEquals(3.14, config.getSerialization().get("DOUBLE_OPTION"));
        }
    }
}
