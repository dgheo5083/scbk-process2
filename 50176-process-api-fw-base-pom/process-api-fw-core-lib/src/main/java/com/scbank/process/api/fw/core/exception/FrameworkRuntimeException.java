package com.scbank.process.api.fw.core.exception;

import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.lang.Nullable;

import com.scbank.process.api.fw.core.error.FrameworkErrorCode;
import com.scbank.process.api.fw.core.utils.StringUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 프레임워크 런타임 예외 클래스입니다.
 * <p>
 * {@link FrameworkException}과 구조는 같으며, 체크 예외가 아닌 비검사 예외(RuntimeException)를 기반으로
 * 런타임 전파가 필요한 경우 사용합니다.
 * </p>
 *
 * @see IExceptionInfo
 * @author sungdon.choi
 */
@Getter
@Setter
@ToString
public class FrameworkRuntimeException extends RuntimeException implements IExceptionInfo {

    private static final long serialVersionUID = 1L;

    /** 오류 코드 (예: BFE0501) */
    private String errorCode;

    /** 오류 발생 위치 (예: CORE, MCI, HOST 등 선택적 사용) */
    private String errorLocation;

    /**
     * 에러 모듈
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

    /** 메시지 템플릿에 바인딩할 인자 목록 */
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
	

    public FrameworkRuntimeException() {
        super();
    }
    
    /**
     * 
     * @param errorCode 프레임워크 에러코드 열거형 상수
     */
    public FrameworkRuntimeException(FrameworkErrorCode errorCode) {
        this(errorCode.getCode(), errorCode.getMessage());
    }

    /**
     * 
     * @param errorCode 프레임워크 에러코드 열거형 상수
     * @param cause     예외
     */
    public FrameworkRuntimeException(FrameworkErrorCode errorCode, Throwable cause) {
        this(errorCode.getCode(), cause);
    }

    /**
     * 
     * @param errorCode    프레임워크 에러코드 열거형 상수
     * @param errorMessage 에러 메시지
     * @param cause        예외
     */
    public FrameworkRuntimeException(FrameworkErrorCode errorCode, String errorMessage, Throwable cause) {
        this(errorCode.getCode(), cause);
        this.errorMessage = errorMessage;
    }

    /**
     * 
     * @param errorCode    업무에서 정의한 에러코드 문자열
     * @param errorMessage 에러 메시지
     * @param cause        예외
     */
    public FrameworkRuntimeException(String errorCode, String errorMessage, Throwable cause) {
        this(errorCode, cause);
        this.errorMessage = errorMessage;
    }

    /**
     * 
     * @param cause 예외
     */
    public FrameworkRuntimeException(Throwable cause) {
        super(cause);
        this.errorCode = FrameworkErrorCode.INTERNAL_ERROR.getCode();
        this.errorCode = FrameworkErrorCode.INTERNAL_ERROR.getMessage();
    }

    /**
     * 오류 코드만 전달하는 기본 생성자
     * 
     * @param errorCode 오류 코드
     */
    public FrameworkRuntimeException(String errorCode) {
        this(errorCode, "");
    }

    /**
     * 오류 코드와 사용자 정의 메시지 전달
     * 
     * @param errorCode 오류 코드
     * @param message   메시지 (로그 확인용)
     */
    public FrameworkRuntimeException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.errorMessage = message;
    }

    /**
     * 오류 코드 + cause 전달
     * 
     * @param errorCode 오류 코드
     * @param cause     원인 예외
     */
    public FrameworkRuntimeException(String errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
        this.errorMessage = cause.getMessage();
    }

    /**
     * 오류 코드 + 메시지 치환 인자 전달
     * 
     * @param errorCode   오류 코드
     * @param messageArgs 메시지 치환 인자
     */
    public FrameworkRuntimeException(String errorCode, List<Object> messageArgs) {
        this(errorCode, messageArgs, null);
    }

    /**
     * 오류 코드 + 메시지 인자 + cause 전달
     * 
     * @param errorCode   오류 코드
     * @param messageArgs 메시지 치환 인자
     * @param cause       원인 예외
     */
    public FrameworkRuntimeException(String errorCode, List<Object> messageArgs, @Nullable Throwable cause) {
        super(cause != null ? cause.getMessage() : StringUtils.EMPTY, cause, false, false);
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
