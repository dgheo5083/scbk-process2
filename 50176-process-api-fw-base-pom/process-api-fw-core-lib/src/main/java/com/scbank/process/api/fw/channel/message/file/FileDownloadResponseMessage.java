package com.scbank.process.api.fw.channel.message.file;

import com.scbank.process.api.fw.channel.message.IResponseMessage;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * <pre>
 * 파일 다운로드 전용 응답 메시지 객체입니다.
 * 
 * 일반적인 {@link IResponseMessage}와 동일한 구조를 따르되,
 * 헤더는 사용하지 않고 바디만 {@link FileDownloadObject}로 구성됩니다.
 * 
 * 프레임워크의 응답 렌더러에서 이 타입을 감지하여
 * 바이너리 파일로 HTTP 응답을 구성할 수 있도록 설계되었습니다.
 * </pre>
 *
 * 주요 특징
 * <ul>
 * <li>헤더는 null (Void)로 처리됨</li>
 * <li>body는 {@link FileDownloadObject}로 구성</li>
 * <li>{@code FileDownloadResponseRenderer} 또는
 * {@code ResponseRendererComposite}에서 이 구조를 감지하여 처리</li>
 * </ul>
 *
 * @author
 */
@Data
public class FileDownloadResponseMessage implements IResponseMessage<Void, FileDownloadObject> {

	private static final long serialVersionUID = 1L;

	/**
	 * 응답 헤더부 (사용하지 않음)
	 *
	 * <p>
	 * 파일 다운로드 응답에서는 별도 채널 헤더를 포함하지 않기 때문에 Void 타입으로 설정됨
	 * </p>
	 */
	@MessageField(id = "header", name = "채널 헤더부")
	private Void header;

	/**
	 * 응답 바디부 (파일 다운로드 내용)
	 *
	 * <p>
	 * {@link FileDownloadObject}를 통해 파일명, Content-Type, 파일 바이트를 포함
	 * </p>
	 */
	@MessageField(id = "body", name = "채널 바디부")
	private FileDownloadObject body;
}
