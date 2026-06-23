package com.scbank.process.api.fw.security.xss.processor;

import java.io.IOException;
import jakarta.servlet.ServletRequest;

/**
 * XSS 보호를 위한 Processor 인터페이스입니다.
 * <p>
 * 요청의 Content-Type에 따라 지원 여부를 판단하고,
 * 요청 내용을 XSS 필터링(Sanitizing)하여 반환합니다.
 * </p>
 *
 * @author
 */
public interface IXssProtectionProcessor {

    /**
     * 주어진 Content-Type에 대해 이 Processor가 지원하는지 여부를 반환합니다.
     *
     * @param contentType 요청의 Content-Type (예: "application/json",
     *                    "application/x-www-form-urlencoded")
     * @return true: 이 Processor가 처리할 수 있음, false: 처리할 수 없음
     */
    boolean supports(String contentType);

    /**
     * 주어진 요청을 XSS 보호 처리를 수행하여 새로운 ServletRequest로 반환합니다.
     *
     * @param request 원본 ServletRequest 객체
     * @return XSS 필터링이 적용된 새 ServletRequest 객체
     * @throws IOException 요청 Body 읽기나 변환 중 오류 발생 시
     */
    ServletRequest sanitize(ServletRequest request) throws IOException;
}
