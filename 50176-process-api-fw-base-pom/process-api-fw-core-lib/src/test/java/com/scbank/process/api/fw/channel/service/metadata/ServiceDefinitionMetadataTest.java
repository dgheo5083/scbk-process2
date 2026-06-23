package com.scbank.process.api.fw.channel.service.metadata;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.channel.service.metadata.ServiceDefinitionMetadata.AccessControlInfo;
import com.scbank.process.api.fw.channel.service.metadata.ServiceDefinitionMetadata.InterceptorInfo;
import com.scbank.process.api.fw.channel.service.metadata.ServiceDefinitionMetadata.ParameterInfo;
import com.scbank.process.api.fw.channel.service.metadata.ServiceDefinitionMetadata.ServiceInfo;
import com.scbank.process.api.fw.channel.service.metadata.ServiceDefinitionMetadata.ServiceTimeInfo;
import com.scbank.process.api.fw.channel.service.metadata.ServiceDefinitionMetadata.ServiceTimeInfo.TimeRange;

/**
 * ServiceDefinitionMetadata Test Class
 */
class ServiceDefinitionMetadataTest {

    @Nested
    @DisplayName("ServiceDefinitionMetadata builder tests")
    class ServiceDefinitionMetadataBuilderTests {

        @Test
        @DisplayName("Should build ServiceDefinitionMetadata with all fields")
        void shouldBuildServiceDefinitionMetadataWithAllFields() {
            ServiceDefinitionMetadata metadata = ServiceDefinitionMetadata.builder()
                    .serviceId("TEST_SERVICE")
                    .url("/api/test")
                    .description("Test Service")
                    .services(List.of())
                    .accessControl(new AccessControlInfo(true, new String[]{"WEB"}))
                    .serviceTime(new ServiceTimeInfo(true,
                            new TimeRange("0900", "1800"),
                            new TimeRange("1000", "1600")))
                    .interceptors(List.of(new InterceptorInfo("testInterceptor")))
                    .parameters(List.of(new ParameterInfo("param1", "value1")))
                    .build();

            assertNotNull(metadata);
            assertEquals("TEST_SERVICE", metadata.getServiceId());
            assertEquals("/api/test", metadata.getUrl());
            assertEquals("Test Service", metadata.getDescription());
        }

        @Test
        @DisplayName("Should handle null values in builder")
        void shouldHandleNullValuesInBuilder() {
            ServiceDefinitionMetadata metadata = ServiceDefinitionMetadata.builder()
                    .serviceId(null)
                    .url(null)
                    .build();

            assertNotNull(metadata);
            assertNull(metadata.getServiceId());
            assertNull(metadata.getUrl());
        }
    }

    @Nested
    @DisplayName("ServiceInfo tests")
    class ServiceInfoTests {

        @Test
        @DisplayName("Should build ServiceInfo with all fields")
        void shouldBuildServiceInfoWithAllFields() {
            ServiceInfo serviceInfo = ServiceInfo.builder()
                    .description("Test Component")
                    .condition("true")
                    .priority(1)
                    .component("TestClass@testMethod")
                    .fallback(false)
                    .fallbackRef(null)
                    .accessControl(new AccessControlInfo(false, null))
                    .serviceTime(null)
                    .interceptors(null)
                    .build();

            assertNotNull(serviceInfo);
            assertEquals("Test Component", serviceInfo.getDescription());
            assertEquals("true", serviceInfo.getCondition());
            assertEquals(1, serviceInfo.getPriority());
            assertEquals("TestClass@testMethod", serviceInfo.getComponent());
            assertFalse(serviceInfo.isFallback());
        }

        @Test
        @DisplayName("Should compare by priority")
        void shouldCompareByPriority() {
            ServiceInfo lowPriority = ServiceInfo.builder().priority(10).build();
            ServiceInfo highPriority = ServiceInfo.builder().priority(1).build();

            assertTrue(highPriority.compareTo(lowPriority) < 0);
            assertTrue(lowPriority.compareTo(highPriority) > 0);
        }

        @Test
        @DisplayName("Should return 0 when priorities are equal")
        void shouldReturnZeroWhenPrioritiesEqual() {
            ServiceInfo service1 = ServiceInfo.builder().priority(5).build();
            ServiceInfo service2 = ServiceInfo.builder().priority(5).build();

            assertEquals(0, service1.compareTo(service2));
        }
    }

    @Nested
    @DisplayName("AccessControlInfo tests")
    class AccessControlInfoTests {

        @Test
        @DisplayName("Should create AccessControlInfo record")
        void shouldCreateAccessControlInfoRecord() {
            AccessControlInfo accessControl = new AccessControlInfo(true, new String[]{"WEB", "MOBILE"});

            assertTrue(accessControl.requiredLogin());
            assertArrayEquals(new String[]{"WEB", "MOBILE"}, accessControl.allowedChannels());
        }

        @Test
        @DisplayName("Should handle null allowed channels")
        void shouldHandleNullAllowedChannels() {
            AccessControlInfo accessControl = new AccessControlInfo(false, null);

            assertFalse(accessControl.requiredLogin());
            assertNull(accessControl.allowedChannels());
        }
    }

    @Nested
    @DisplayName("InterceptorInfo tests")
    class InterceptorInfoTests {

        @Test
        @DisplayName("Should create InterceptorInfo record")
        void shouldCreateInterceptorInfoRecord() {
            InterceptorInfo interceptor = new InterceptorInfo("authInterceptor");

            assertEquals("authInterceptor", interceptor.id());
        }
    }

    @Nested
    @DisplayName("ServiceTimeInfo tests")
    class ServiceTimeInfoTests {

        @Test
        @DisplayName("Should create ServiceTimeInfo record")
        void shouldCreateServiceTimeInfoRecord() {
            TimeRange businessDay = new TimeRange("0900", "1800");
            TimeRange holiday = new TimeRange("1000", "1600");
            ServiceTimeInfo serviceTime = new ServiceTimeInfo(true, businessDay, holiday);

            assertTrue(serviceTime.enabled());
            assertEquals("0900", serviceTime.businessDay().startTime());
            assertEquals("1800", serviceTime.businessDay().endTime());
            assertEquals("1000", serviceTime.holiday().startTime());
            assertEquals("1600", serviceTime.holiday().endTime());
        }
    }

    @Nested
    @DisplayName("ParameterInfo tests")
    class ParameterInfoTests {

        @Test
        @DisplayName("Should create ParameterInfo record")
        void shouldCreateParameterInfoRecord() {
            ParameterInfo parameter = new ParameterInfo("timeout", "30000");

            assertEquals("timeout", parameter.parameterName());
            assertEquals("30000", parameter.parameterValue());
        }
    }
}
