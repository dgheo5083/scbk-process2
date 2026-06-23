package com.scbank.process.api.fw.base.channel.advice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scbank.process.api.fw.base.store.secure.SecureContextStore;

/**
 * Generated unit test for {@link SecureContextRequestAdvice}.
 */
class SecureContextRequestAdviceTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final SecureContextRequestAdvice advice = new SecureContextRequestAdvice(objectMapper);

    @AfterEach
    void tearDown() {
        SecureContextStore.clearContext();
    }

    private HttpInputMessage message(String json) throws Exception {
        HttpInputMessage inputMessage = mock(HttpInputMessage.class);
        lenient().when(inputMessage.getBody())
                .thenReturn(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)));
        lenient().when(inputMessage.getHeaders()).thenReturn(new HttpHeaders());
        return inputMessage;
    }

    private String read(HttpInputMessage message) throws Exception {
        return new String(message.getBody().readAllBytes(), StandardCharsets.UTF_8);
    }

    @Test
    void supportsRequestBodyParameter() {
        MethodParameter parameter = mock(MethodParameter.class);
        when(parameter.hasParameterAnnotation(RequestBody.class)).thenReturn(true);
        assertThat(advice.supports(parameter, null, null)).isTrue();

        when(parameter.hasParameterAnnotation(RequestBody.class)).thenReturn(false);
        assertThat(advice.supports(parameter, null, null)).isFalse();
    }

    @Test
    void beforeBodyReadReturnsRootWhenNoCommon() throws Exception {
        HttpInputMessage result = advice.beforeBodyRead(message("{\"foo\":\"bar\"}"), null, null, null);
        assertThat(read(result)).contains("foo");
    }

    @Test
    void beforeBodyReadStoresSecureContextAndReturnsRootWhenNoBody() throws Exception {
        HttpInputMessage result = advice.beforeBodyRead(
                message("{\"common\":{\"sign\":{}}}"), null, null, null);

        assertThat(SecureContextStore.getContext()).isPresent();
        assertThat(read(result)).contains("common");
    }

    @Test
    void beforeBodyReadExtractsBodyWhenPresent() throws Exception {
        HttpInputMessage result = advice.beforeBodyRead(
                message("{\"common\":{\"sign\":{}},\"body\":{\"x\":1}}"), null, null, null);

        assertThat(SecureContextStore.getContext()).isPresent();
        assertThat(read(result)).isEqualTo("{\"x\":1}");
    }
}
