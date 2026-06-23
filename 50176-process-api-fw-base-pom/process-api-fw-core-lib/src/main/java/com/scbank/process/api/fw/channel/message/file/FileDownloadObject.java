package com.scbank.process.api.fw.channel.message.file;

import com.scbank.process.api.fw.message.IMessageObject;

import lombok.Builder;
import lombok.Data;

/**
 * <pre>
 * 프레임워크 표준 파일 다운로드용 DTO 객체입니다.
 * 
 * IResponseMessage의 응답 바디로 사용되며,
 * filename, contentType, fileData를 포함하여 HTTP 파일 응답으로 직렬화됩니다.
 * 
 * 프레임워크 응답 구조(IResponseMessage) 및 커스텀 렌더러(FileDownloadResponseRenderer)와 연동됩니다.
 * </pre>
 *
 * 주요 용도
 * <ul>
 * <li>업무 응답으로 파일 다운로드가 필요한 경우 사용</li>
 * <li>JSON 대신 바이너리 응답으로 내려보내기 위한 구조</li>
 * <li>Swagger 문서화 및 로깅 대상이 될 수 있음</li>
 * </ul>
 *
 * @author
 */
@Data
@Builder
public class FileDownloadObject implements IMessageObject {

    private static final long serialVersionUID = 1L;

    /**
     * 클라이언트가 저장할 때 사용할 파일 이름.
     * Content-Disposition: attachment; filename="{filename}"에 사용됩니다.
     */
    private String filename;

    /**
     * 파일 MIME 타입.
     * 예: application/pdf, image/png, application/vnd.ms-excel 등
     */
    private String contentType;

    /**
     * 실제 파일 바이너리 데이터.
     * 보통 서비스에서 생성된 byte[]를 할당합니다.
     */
    private byte[] fileData;
}
