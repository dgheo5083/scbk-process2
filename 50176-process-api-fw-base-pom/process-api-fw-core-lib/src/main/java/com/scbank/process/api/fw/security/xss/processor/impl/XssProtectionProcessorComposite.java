package com.scbank.process.api.fw.security.xss.processor.impl;

import java.io.IOException;
import java.util.List;

import com.scbank.process.api.fw.security.xss.processor.IXssProtectionProcessor;

import jakarta.servlet.ServletRequest;
import lombok.RequiredArgsConstructor;

/**
 * 등록된 여러 {@link IXssProtectionProcessor}를 Content-Type에 따라 위임 처리하는 Composite
 * 구현체입니다.
 */
@RequiredArgsConstructor
public class XssProtectionProcessorComposite {

    /**
     * 실제 XSS 처리 책임을 지는 개별 Processor 목록
     */
    private final List<IXssProtectionProcessor> processors;

    /**
     * 요청의 Content-Type에 맞는 Processor를 찾아 sanitize 처리합니다.
     *
     * @param request 원본 ServletRequest
     * @return 정화된 요청 객체 (필요 시 래핑)
     * @throws IOException           요청 스트림 읽기 실패 시
     * @throws IllegalStateException 지원되는 Processor가 없을 경우
     */
    public ServletRequest sanitize(ServletRequest request) throws IOException {
        String contentType = request.getContentType();
        for (IXssProtectionProcessor processor : processors) {
            if (processor.supports(contentType)) {
                return processor.sanitize(request);
            }
        }
        throw new IllegalStateException("지원되는 XSS Processor가 없습니다. contentType=" + contentType);
    }
}
