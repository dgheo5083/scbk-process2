package com.scbank.process.api.fw.core.exception;

import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 프레임워크 공통 예외 클래스입니다.
 * <p>
 * 에러코드 기반 예외 처리 구조를 따르며,
 * 메시지 리졸버 또는 전역 예외 핸들러에서 에러코드 및 치환 파라미터를 활용해
 * 사용자 메시지를 생성할 수 있도록 설계되어 있습니다.
 * </p>
 * 
 * @see IExceptionInfo
 * @author sungdon.choi
 */
@Getter
@Setter
@ToString
public class FrameworkException extends Exception implements IExceptionInfo {

	private static final long serialVersionUID = 1L;

	/**
	 * 오류 코드 (예: BFE0401)
	 */
	private String errorCode;

	/**
	 * 오류 발생 위치 (예: MCI, CORE, WAS 등 - 선택사항)
	 */
	private String errorLocation;

	/**
	 * 에러모듈
	 */
	private String errorModule;

	/**
	 * 에러 메시지
	 */
	private String errorMessage;

	/**
	 * 에러 가이드 메시지
	 */
	private String errorGuideMessage;

	/**
	 * 메시지 템플릿에 사용할 치환 파라미터 목록
	 */
	private List<Object> messageArgs;
	
	/**
	 * 오류 출력이후 이동할 페이지
	 */
	private String nextPage;
	
	/**
	 * 오류 출력 이후 이동할 페이지 파라미터
	 */
	private Map<String, Object> nextPageParameters;
	
	/**
	 * 오류 페이지 이동여부에 대한 파라미터를 획득한다
	 */
	private Map<String, Object> errorPageParameters;
	

	/**
	 * 오류 코드만 지정하는 경우 (기본 메시지 템플릿 사용)
	 * 
	 * @param errorCode 오류 코드
	 */
	public FrameworkException(String errorCode) {
		this(errorCode, List.of(), null);
	}

	/**
	 * 원인 예외만 지정하는 경우
	 * 
	 * @param cause 원인 예외
	 */
	public FrameworkException(Throwable cause) {
		super(cause);
	}

	/**
	 * 오류 코드 + 메시지를 직접 지정하는 경우
	 *
	 * @param errorCode 오류 코드
	 * @param message   사용자 정의 메시지 (로그 확인용)
	 */
	public FrameworkException(String errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	/**
	 * 오류 코드 + 원인 예외를 지정하는 경우
	 *
	 * @param errorCode 오류 코드
	 * @param cause     원인 예외
	 */
	public FrameworkException(String errorCode, Throwable cause) {
		super(null, cause);
		this.errorCode = errorCode;
	}

	/**
	 * 오류 코드 + 메시지 파라미터 지정
	 *
	 * @param errorCode   오류 코드
	 * @param messageArgs 메시지 치환 인자
	 */
	public FrameworkException(String errorCode, List<Object> messageArgs) {
		this(errorCode, messageArgs, null);
	}

	/**
	 * 오류 코드 + 메시지 파라미터 + 원인 예외 지정
	 *
	 * @param errorCode   오류 코드
	 * @param messageArgs 메시지 치환 인자
	 * @param cause       원인 예외
	 */
	public FrameworkException(String errorCode, List<Object> messageArgs, Throwable cause) {
		super(null, cause, false, false);
		this.errorCode = errorCode;
		this.messageArgs = messageArgs;
	}

	@Override
	public String getMessage() {
		String errorCode = this.getErrorCode();
		String errorMessage = this.getErrorMessage();
		
		if (errorCode == null && errorMessage == null) {
			return null;
		}
		
		StringBuffer buf = new StringBuffer();
		if (errorCode != null) {
			buf.append("[").append(errorCode).append("]");
		}

		if (errorMessage != null) {
			List<Object> errorMessageArguments = this.getMessageArgs();
			if (errorMessageArguments != null && errorMessageArguments.size() > 0) {
				Object[] arguments = errorMessageArguments.toArray(new Object[errorMessageArguments.size()]);
				errorMessage = MessageFormat.format(errorMessage, arguments);
			}
			buf.append(errorMessage);
		}
		
		return buf.toString();
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void addNextPageParameter(String key, Object value) {
		if (value == null) {
			return;
		}
		
		if (this.nextPageParameters == null) {
			this.nextPageParameters = new LinkedHashMap<>();
		}
		
		this.nextPageParameters.put(key, value);
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void addErrorPageParameter(String key, Object value) {
		if (value == null) {
			return;
		}
		
		if (this.errorPageParameters == null) {
			this.errorPageParameters = new LinkedHashMap<>();
		}
		
		this.errorPageParameters.put(key, value);
	}
}
