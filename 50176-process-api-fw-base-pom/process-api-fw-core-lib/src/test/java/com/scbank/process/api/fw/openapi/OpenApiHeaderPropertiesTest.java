package com.scbank.process.api.fw.openapi;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.openapi.OpenApiHeaderProperties.Header;

/**
 * {@link OpenApiHeaderProperties} 및 {@link Header} 레코드 단위 테스트.
 */
class OpenApiHeaderPropertiesTest {

    @Test
    @DisplayName("headers getter/setter 동작")
    void headersGetterSetter() {
        OpenApiHeaderProperties properties = new OpenApiHeaderProperties();
        Header header = new Header("X-Trace-Id", "추적 ID", true, "string");
        properties.setHeaders(List.of(header));

        assertThat(properties.getHeaders()).containsExactly(header);
    }

    @Test
    @DisplayName("Header 레코드의 접근자 동작")
    void headerRecordAccessors() {
        Header header = new Header("Authorization", "인증 토큰", false, "string");

        assertThat(header.name()).isEqualTo("Authorization");
        assertThat(header.description()).isEqualTo("인증 토큰");
        assertThat(header.required()).isFalse();
        assertThat(header.type()).isEqualTo("string");
    }

    @Test
    @DisplayName("Header 레코드의 equals/hashCode/toString")
    void headerRecordEquality() {
        Header a = new Header("h", "d", true, "string");
        Header b = new Header("h", "d", true, "string");

        assertThat(a).isEqualTo(b);
        assertThat(a).hasSameHashCodeAs(b);
        assertThat(a.toString()).contains("Header");
    }
}
