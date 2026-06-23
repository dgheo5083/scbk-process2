package com.scbank.process.api.fw.channel.service.metadata;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.channel.service.metadata.ServiceMethodMetadata.ParameterMetadata;
import com.scbank.process.api.fw.message.IMessageObject;

/**
 * ServiceMethodMetadata Test Class
 */
class ServiceMethodMetadataTest {

    private Method testMethod;
    private ServiceMethodMetadata metadata;

    @BeforeEach
    void setUp() throws Exception {
        testMethod = TestService.class.getMethod("process", String.class, Integer.class);
        ParameterMetadata param1 = new ParameterMetadata("input", String.class, new Annotation[]{}, true);
        ParameterMetadata param2 = new ParameterMetadata("count", Integer.class, new Annotation[]{}, false);

        metadata = new ServiceMethodMetadata(
                TestService.class,
                testMethod,
                "Test method description",
                List.of(param1, param2),
                String.class
        );
    }

    @Nested
    @DisplayName("Constructor and getter tests")
    class ConstructorAndGetterTests {

        @Test
        @DisplayName("Should return declaring class")
        void shouldReturnDeclaringClass() {
            assertEquals(TestService.class, metadata.getDeclaringClass());
        }

        @Test
        @DisplayName("Should return method")
        void shouldReturnMethod() {
            assertEquals(testMethod, metadata.getMethod());
        }

        @Test
        @DisplayName("Should return description")
        void shouldReturnDescription() {
            assertEquals("Test method description", metadata.getDescription());
        }

        @Test
        @DisplayName("Should return parameters")
        void shouldReturnParameters() {
            List<ParameterMetadata> params = metadata.getParameters();

            assertNotNull(params);
            assertEquals(2, params.size());
            assertEquals("input", params.get(0).getName());
            assertEquals("count", params.get(1).getName());
        }

        @Test
        @DisplayName("Should return return type")
        void shouldReturnReturnType() {
            assertEquals(String.class, metadata.getReturnType());
        }

        @Test
        @DisplayName("Should return method name")
        void shouldReturnMethodName() {
            assertEquals("process", metadata.getMethodName());
        }

        @Test
        @DisplayName("Should return component key")
        void shouldReturnComponentKey() {
            String expectedKey = TestService.class.getName() + "@process";
            assertEquals(expectedKey, metadata.getComponentKey());
        }
    }

    @Nested
    @DisplayName("ParameterMetadata tests")
    class ParameterMetadataTests {

        @Test
        @DisplayName("Should return parameter name")
        void shouldReturnParameterName() {
            ParameterMetadata param = new ParameterMetadata("testParam", String.class, new Annotation[]{}, true);

            assertEquals("testParam", param.getName());
        }

        @Test
        @DisplayName("Should return parameter type")
        void shouldReturnParameterType() {
            ParameterMetadata param = new ParameterMetadata("testParam", Integer.class, new Annotation[]{}, false);

            assertEquals(Integer.class, param.getType());
        }

        @Test
        @DisplayName("Should return annotations")
        void shouldReturnAnnotations() throws Exception {
            Method annotatedMethod = AnnotatedService.class.getMethod("annotatedMethod", String.class);
            Annotation[] annotations = annotatedMethod.getParameterAnnotations()[0];
            ParameterMetadata param = new ParameterMetadata("param", String.class, annotations, true);

            assertNotNull(param.getAnnotations());
            assertEquals(1, param.getAnnotations().length);
        }

        @Test
        @DisplayName("Should return body flag")
        void shouldReturnBodyFlag() {
            ParameterMetadata bodyParam = new ParameterMetadata("body", Object.class, new Annotation[]{}, true);
            ParameterMetadata nonBodyParam = new ParameterMetadata("other", String.class, new Annotation[]{}, false);

            assertTrue(bodyParam.isBody());
            assertFalse(nonBodyParam.isBody());
        }

        @Test
        @DisplayName("Should check if has annotation")
        void shouldCheckIfHasAnnotation() throws Exception {
            Method annotatedMethod = AnnotatedService.class.getMethod("annotatedMethod", String.class);
            Annotation[] annotations = annotatedMethod.getParameterAnnotations()[0];
            ParameterMetadata param = new ParameterMetadata("param", String.class, annotations, true);

            assertTrue(param.hasAnnotation(TestAnnotation.class));
            assertFalse(param.hasAnnotation(Override.class));
        }

        @Test
        @DisplayName("Should return false for hasAnnotation when annotations is null")
        void shouldReturnFalseForHasAnnotationWhenAnnotationsNull() {
            ParameterMetadata param = new ParameterMetadata("param", String.class, null, true);

            assertFalse(param.hasAnnotation(TestAnnotation.class));
        }

        @Test
        @DisplayName("Should return false for hasAnnotation when annotations is empty")
        void shouldReturnFalseForHasAnnotationWhenAnnotationsEmpty() {
            ParameterMetadata param = new ParameterMetadata("param", String.class, new Annotation[]{}, true);

            assertFalse(param.hasAnnotation(TestAnnotation.class));
        }

        @Test
        @DisplayName("Should get annotation by type")
        void shouldGetAnnotationByType() throws Exception {
            Method annotatedMethod = AnnotatedService.class.getMethod("annotatedMethod", String.class);
            Annotation[] annotations = annotatedMethod.getParameterAnnotations()[0];
            ParameterMetadata param = new ParameterMetadata("param", String.class, annotations, true);

            TestAnnotation annotation = param.getAnnotation(TestAnnotation.class);
            assertNotNull(annotation);
        }

        @Test
        @DisplayName("Should return null when annotation not found")
        void shouldReturnNullWhenAnnotationNotFound() {
            ParameterMetadata param = new ParameterMetadata("param", String.class, new Annotation[]{}, true);

            assertNull(param.getAnnotation(TestAnnotation.class));
        }

        @Test
        @DisplayName("Should return null for getAnnotation when annotations is null")
        void shouldReturnNullForGetAnnotationWhenAnnotationsNull() {
            ParameterMetadata param = new ParameterMetadata("param", String.class, null, true);

            assertNull(param.getAnnotation(TestAnnotation.class));
        }
    }

    // Test classes
    public static class TestService {
        public String process(String input, Integer count) {
            return input + count;
        }
    }

    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface TestAnnotation {
    }

    public static class AnnotatedService {
        public void annotatedMethod(@TestAnnotation String param) {
        }
    }
}
