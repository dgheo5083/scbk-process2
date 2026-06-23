package com.scbank.process.api.fw.core.error;

/**
 * 프레임워크 오류코드 열거형 상수.
 * <p>
 * 각 오류 항목은 코드 (예: MACF0001)와 사용자 메시지를 포함하며,
 * 전역 예외 처리기 및 응답 메시지 생성 시 활용됩니다.
 * <p>
 * 도메인별로 접두어를 구분하여 그룹핑됩니다:
 * <ul>
 * <li>COMMON - 공통 오류</li>
 * <li>MSG - 메시지 처리 오류</li>
 * <li>INTEG - 연계 시스템 오류</li>
 * <li>DAO - 데이터 접근 오류</li>
 * <li>VALID - 유효성 검증 오류</li>
 * <li>SVC - 서비스 컴포넌트 오류</li>
 * <li>BIZTIME - 영업시간/거래 가능 시간 오류</li>
 * <li>INIT - 초기화 오류</li>
 * <li>FILE - 파일 처리 오류</li>
 * </ul>
 */
public enum FrameworkErrorCode implements IErrorCode {

    // COMMON 오류 (MACF0001~0099): 전역적이고 범용적인 입력 및 시스템 오류
    /** 잘못된 입력값 */
    INVALID_INPUT("MACF0001", "잘못된 입력입니다."),
    /** 지원하지 않는 Content-Type */
    UNSUPPORTED_CONTENT_TYPE("MACF0002", "지원하지 않는 Content-Type입니다."),
    /** 알 수 없는 내부 시스템 오류 */
    INTERNAL_ERROR("MACF9999", "내부 시스템 오류입니다."),

    // MESSAGE 오류 (MACF0100~0199)
    /** 메시지 메타데이터 없음 */
    MSG_METADATA_NOT_FOUND("MACF0101", "메시지 메타데이터를 찾을 수 없습니다."),
    /** 직렬화 실패 */
    MSG_SERIALIZATION_FAILED("MACF0102", "메시지 직렬화에 실패했습니다."),
    /** 역직렬화 실패 */
    MSG_DESERIALIZATION_FAILED("MACF0103", "메시지 역직렬화에 실패했습니다."),
    MSG_PROCESS_FAILED("MACF0104", "메시지 처리에 실패했습니다."),

    // INTEGRATION 오류 (MACF0200~0299)
    /** 연계 시스템 연결 실패 */
    INTEG_CONNECTION_FAILED("MACF0201", "연계 시스템 연결에 실패했습니다."),
    /** 연계 시스템 응답 지연 (타임아웃) */
    INTEG_TIMEOUT("MACF0202", "연계 시스템 응답 지연입니다."),
    /** 연계 시스템 응답 오류 */
    INTEG_INVALID_RESPONSE("MACF0203", "연계 시스템 응답이 유효하지 않습니다."),
    /** 연계시스템 전문 송/수신 실패 오류 */
    INTEG_FAILED("MACF0204", "연계시스템 전문 송/수신에 실패했습니다."),
    /** 연계시스템 전문 송신 실패 오류 */
    INTEG_SEND_FAILED("MACF0205", "연계시스템 전문 송신에 실패했습니다."),
    /** 연계시스템 전문 수신 실패 오류 */
    INTEG_RECEIVED_FAILED("MACF0206", "연계시스템 전문 수신에 실패했습니다."),

    // DAO 오류 (MACF0300~0399)
    /** 데이터 없음 */
    DAO_DATA_NOT_FOUND("MACF0301", "요청한 데이터를 찾을 수 없습니다."),
    /** 데이터 접근 중 오류 */
    DAO_DATA_ACCESS_ERROR("MACF0302", "데이터 접근 중 오류가 발생했습니다."),
    /** 중복된 키 */
    DAO_DUPLICATE_KEY("MACF0303", "중복된 키로 인해 저장에 실패했습니다."),

    // VALIDATION 오류 (MACF0400~0499)
    /** 필수 항목 누락 */
    VALID_REQUIRED("MACF0401", "필수 입력값이 누락되었습니다."),
    /** 포맷이 올바르지 않음 */
    VALID_INVALID_FORMAT("MACF0402", "입력 형식이 올바르지 않습니다."),
    /** 길이 제한 위반 */
    VALID_LENGTH_EXCEEDED("MACF0403", "입력 길이가 허용 범위를 초과했습니다."),
    /** 허용되지 않는 값 */
    VALID_INVALID_VALUE("MACF0404", "허용되지 않는 값입니다."),
    /** 범위 초과 또는 미달 */
    VALID_OUT_OF_RANGE("MACF0405", "입력값이 허용 범위를 벗어났습니다."),

    // SERVICE 오류 (MACF0500~0599)
    /** 등록되지 않은 서비스 */
    SVC_NOT_FOUND("MACF0501", "요청한 서비스가 존재하지 않습니다."),
    /** 서비스 실행 실패 */
    SVC_EXECUTION_FAILED("MACF0502", "서비스 실행 중 오류가 발생했습니다."),
    /** 서비스 접근 거부 */
    SVC_ACCESS_DENIED("MACF0503", "해당 서비스에 접근할 수 없습니다."),
    /** 서비스 결과가 유효하지 않음 */
    SVC_INVALID_RESULT("MACF0504", "서비스 실행 결과가 유효하지 않습니다."),
    /** 서비스 파라미터 오류 */
    SVC_INVALID_PARAMETER("MACF0505", "서비스 요청 파라미터가 유효하지 않습니다."),

    // BIZTIME 오류 (MACF0700~0799)
    /** 영업시간 외 요청 */
    BIZTIME_OUT_OF_BUSINESS_HOURS("MACF0701", "현재 시간은 서비스 이용이 불가능한 시간입니다."),
    /** 거래 가능 시간 아님 */
    BIZTIME_NOT_ALLOWED("MACF0702", "해당 시간에는 거래를 처리할 수 없습니다."),
    /** 영업일 아님 */
    BIZTIME_HOLIDAY("MACF0703", "오늘은 영업일이 아닙니다."),

    // INIT 오류 (MACF0800~0899)
    /** 구성 로딩 실패 */
    INIT_CONFIG_LOAD_FAILED("MACF0801", "설정 파일 로딩에 실패했습니다."),
    /** 메타데이터 초기화 실패 */
    INIT_METADATA_FAILED("MACF0802", "메타데이터 초기화 중 오류가 발생했습니다."),
    /** 컴포넌트 등록 실패 */
    INIT_COMPONENT_REGISTRATION_FAILED("MACF0803", "컴포넌트 등록에 실패했습니다."),
    
    /** 캡쳐시스템 설정하지 않는경우*/
    INIT_CAPTURE_SYSTEM_NOT_FOUND("MACF0804", "CaptureSystem 속성이 설정되지 않았습니다."),

    // FILE 오류 (MACF0900~0999)
    /** 파일 업로드 실패 */
    FILE_UPLOAD_FAILED("MACF0901", "파일 업로드에 실패했습니다."),
    /** 파일 다운로드 실패 */
    FILE_DOWNLOAD_FAILED("MACF0902", "파일 다운로드에 실패했습니다."),
    /** 지원하지 않는 파일 형식 */
    FILE_UNSUPPORTED_TYPE("MACF0903", "지원하지 않는 파일 형식입니다."),
    /** 허용된 파일 크기 초과 */
    FILE_SIZE_EXCEEDED("MACF0904", "허용된 파일 크기를 초과했습니다."),
    /** 파일 접근 권한 없음 */
    FILE_ACCESS_DENIED("MACF0905", "파일에 접근할 권한이 없습니다."),

    // SESSION 오류 (MACF1000~1099)
    SESSION_EXPIRED("MACF1001", "세션이 만료되었습니다."),
    SESSION_NOT_FOUND("MACF1002", "세션 정보를 찾을 수 없습니다."),
    SESSION_KEY_INVALID("MACF1003", "허용하지 않은 세션키입니다."),

    // ENCRYPT 오류 (MACF1100~1199)
    ENCRYPT_ENCRYPTION_FAILED("MACF1101", "데이터 암호화에 실패했습니다."),
    ENCRYPT_DECRYPTION_FAILED("MACF1102", "데이터 복호화에 실패했습니다."),
    ENCRYPT_KEY_NOT_FOUND("MACF1103", "암호화 키를 찾을 수 없습니다."),
    ENCRYPT_INVALID_ALGORITHM("MACF1104", "지원하지 않는 암호화 알고리즘입니다."),
    ENCRYPT_MALFORMED_INPUT("MACF1105", "암호화 입력값이 올바르지 않습니다.");

    private final String code;
    private final String message;

    FrameworkErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /** 오류 코드 문자열 반환 */
    public String getCode() {
        return code;
    }

    /** 사용자 메시지 반환 */
    public String getMessage() {
        return message;
    }
}
