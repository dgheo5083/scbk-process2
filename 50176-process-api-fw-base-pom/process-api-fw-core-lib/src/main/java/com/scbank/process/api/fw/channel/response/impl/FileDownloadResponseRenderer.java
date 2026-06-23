package com.scbank.process.api.fw.channel.response.impl;

import java.nio.charset.Charset;
import java.util.List;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.message.file.FileDownloadObject;
import com.scbank.process.api.fw.channel.message.file.FileDownloadResponseMessage;
import com.scbank.process.api.fw.channel.response.IResponseRenderer;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;

import jakarta.servlet.http.HttpServletRequest;

/**
 * <pre>
 * FileDownloadObject 기반 응답 메시지를 HTTP 파일 응답으로 렌더링합니다.
 * 
 * 주요 특징:
 * - Accept 헤더 기반 렌더링 지원 여부 판단
 * - 브라우저가 표시 가능한 포맷은 inline, 그 외는 attachment로 설정
 * </pre>
 */
public class FileDownloadResponseRenderer
        implements IResponseRenderer<FileDownloadResponseMessage, byte[]> {

    /** 브라우저에서 미리보기 가능한 MIME 타입 목록 */
    private static final List<MediaType> INLINE_MEDIA_TYPES = List.of(
            MediaType.APPLICATION_PDF,
            MediaType.IMAGE_PNG,
            MediaType.IMAGE_JPEG,
            MediaType.IMAGE_GIF,
            MediaType.TEXT_PLAIN);

    @Override
    public boolean supports(FileDownloadResponseMessage data, IServiceContext context) {
        if (data == null || data.getBody() == null)
            return false;

        String contentType = data.getBody().getContentType();
        MediaType fileType = MediaType.parseMediaType(contentType);

        HttpServletRequest request = context.request();
        String acceptHeader = request.getHeader("Accept");

        // Accept 없으면: 파일 타입이 일반적인 바이너리(= 다운로드 대상)일 경우만 true
        if (acceptHeader == null || acceptHeader.isBlank()) {
            return MediaType.APPLICATION_OCTET_STREAM.includes(fileType) || isBinary(fileType);
        }

        // Accept 명시 시: 호환되는 Accept 타입이 있는지 확인
        try {
            List<MediaType> acceptList = MediaType.parseMediaTypes(acceptHeader);
            return acceptList.stream().anyMatch(accept -> accept.isCompatibleWith(fileType));
        } catch (Exception e) {
            return false;
        }
    }

    /** 기본 바이너리 MIME 타입 체크 */
    private boolean isBinary(MediaType type) {
        return type.getType().equalsIgnoreCase("application") &&
                !type.getSubtype().contains("json") &&
                !type.getSubtype().contains("xml") &&
                !type.getSubtype().contains("x-www-form-urlencoded");
    }

    @Override
    public ResponseEntity<byte[]> render(FileDownloadResponseMessage data, IServiceContext context) {
        FileDownloadObject file = data.getBody();
        MediaType mediaType = MediaType.parseMediaType(file.getContentType());

        // inline 가능 여부 판단
        boolean isInline = INLINE_MEDIA_TYPES.stream()
                .anyMatch(inline -> inline.isCompatibleWith(mediaType));

        ContentDisposition cd = ContentDisposition.builder(isInline ? "inline" : "attachment")
                .filename(file.getFilename(), Charset.forName(RuntimeContext.getDefaultEncoding()))
                .build();

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, cd.toString())
                .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(file.getFileData().length))
                .body(file.getFileData());
    }
}
