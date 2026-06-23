package com.scbank.process.api.fw.channel.message.file;

import com.scbank.process.api.fw.channel.message.IResponseMessageFactory;

import jakarta.validation.ConstraintViolationException;

/**
 * <pre>
 * 파일 다운로드 응답 전용 메시지 팩토리입니다.
 * 
 * {@link FileDownloadResponseMessage} 객체를 생성하며,
 * 일반적인 채널 메시지 응답(IResponseMessageFactory)와 달리
 * 응답 헤더 없이 바디만 포함된 응답을 구성합니다.
 * </pre>
 *
 * 주요 동작
 * <ul>
 * <li>{@code ok(FileDownloadObject)}: 성공 응답 생성</li>
 * <li>{@code fail(Throwable)}: 예외 기반 실패 응답 생성</li>
 * <li>{@code fail(ConstraintViolationException)}: 지원하지 않음 (파일 응답에
 * BeanValidation 실패 응답은 부적절)</li>
 * </ul>
 *
 * @author
 */
public class FileDownloadResponseMessageFactory
        implements IResponseMessageFactory<Void, FileDownloadObject, FileDownloadResponseMessage> {

    /**
     * 파일 다운로드 성공 응답 생성.
     *
     * @param body {@link FileDownloadObject} 응답 바디
     * @return {@link FileDownloadResponseMessage} 성공 메시지
     */
    @Override
    public FileDownloadResponseMessage ok(FileDownloadObject body) {
        // TODO: 필요 시 header도 설정하거나 확장 가능
        FileDownloadResponseMessage message = new FileDownloadResponseMessage();
        message.setBody(body);
        return message;
    }

    /**
     * 파일 다운로드 실패 응답 생성.
     * 
     * <p>
     * 기본적으로 실패 응답 시 파일을 반환하지 않으며, 별도 예외 처리 레이어에서 JSON 응답으로 전환해야 함
     * </p>
     *
     * @param cause 예외 원인
     * @return 실패 응답 (현재는 null 또는 예외 처리 구조와 분리하여 설계 필요)
     */
    @Override
    public FileDownloadResponseMessage fail(Throwable cause) {
        // 실패 시 파일 전송은 의미 없으므로 null 또는 예외 핸들러에서 JSON 응답 전환
        return null;
    }

    /**
     * Bean Validation 실패 응답은 파일 응답 구조와 호환되지 않으므로 미지원.
     *
     * @param cause ConstraintViolationException
     * @throws UnsupportedOperationException 항상 발생
     */
    @Override
    public FileDownloadResponseMessage fail(ConstraintViolationException cause) {
        throw new UnsupportedOperationException("파일 다운로드 응답에서는 ConstraintViolationException은 처리하지 않습니다.");
    }
}
