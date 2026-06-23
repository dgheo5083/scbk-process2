package com.scbank.process.api.fw.channel.response.impl;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;

import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.message.IResponseMessage;
import com.scbank.process.api.fw.channel.response.IResponseRenderer;
import com.scbank.process.api.fw.core.utils.StringUtils;

import jakarta.servlet.http.HttpServletRequest;

/**
 * <pre>
 * Accept 헤더 기반으로 JSON, XML, TEXT 응답을 처리하는 범용 렌더러.
 * 
 * 파일 타입 등 특정 DTO를 구분하지 않고,
 * 오직 클라이언트의 Accept 헤더와 지원 포맷의 호환성만으로 처리 여부를 결정합니다.
 * </pre>
 */
public class GenericResponseRenderer implements IResponseRenderer<IResponseMessage<?, ?>, Object> {

    /** 지원하는 응답 포맷 목록 */
    private static final List<MediaType> SUPPORTED_TYPES = List.of(
            MediaType.APPLICATION_JSON,
            MediaType.APPLICATION_XML,
            MediaType.TEXT_PLAIN);

    @Override
    public boolean supports(IResponseMessage<?, ?> data, IServiceContext context) {
        if (data == null)
            return false;

        HttpServletRequest request = context.request();
        String acceptHeader = request.getHeader("Accept");
        List<MediaType> acceptList = MediaType.parseMediaTypes(acceptHeader);

        // Accept 비어 있으면 JSON 기본 처리 허용
        if (acceptList == null || acceptList.isEmpty()) {
            return true;
        }

        return acceptList.stream()
                .anyMatch(accept -> SUPPORTED_TYPES.stream()
                        .anyMatch(supported -> supported.isCompatibleWith(accept)));
    }

    @Override
    public ResponseEntity<Object> render(IResponseMessage<?, ?> data, IServiceContext context) {
        HttpServletRequest request = context.request();
        String acceptHeader = request.getHeader("Accept");
        if (StringUtils.isEmpty(acceptHeader) || "*/*".equals(acceptHeader)) {
            acceptHeader = MediaType.APPLICATION_JSON_VALUE;
        }

        List<MediaType> acceptList = MediaType.parseMediaTypes(acceptHeader);

        MediaType responsType = MediaType.APPLICATION_JSON;
        if (!CollectionUtils.isEmpty(acceptList)) {
            responsType = acceptList.stream()
                    .filter(a -> SUPPORTED_TYPES.stream()
                            .anyMatch(s -> s.isCompatibleWith(a)))
                    .findFirst().orElse(MediaType.APPLICATION_JSON);
        }

        return ResponseEntity.ok()
                .contentType(responsType)
                .body(data);
    }
}
