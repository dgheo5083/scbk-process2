package com.scbank.process.api.svc.shared.constants;

public class SyncAtConstants {

    public static final int BIND = 0;

    public static final int BIND_ACK = 1;

    public static final int DELIVER = 2;

    public static final int DELIVER_ACK = 3;

    public static final int REPORT = 4;

    public static final int REPORT_ACK = 5;

    public static final int LDELIVER = 6;

    public static final int LDELIVER_ACK = 7;

    public static final int BIND_BODYLENGTH = 32;

    public static final int BIND_ACK_BODYLENGTH = 20;

    public static final int DELIVER_BODYLENGTH = 284;

    public static final int DELIVER_GENERAL_BODYLENGTH = 264;

    public static final int DELIVER_ACK_BODYLENGTH = 72;

    public static final int LDELIVER_BODYLENGTH = 204;

    public static final int LDELIVER_ACK_BODYLENGTH = 72;

    public static final int REPORT_BODYLENGTH = 92;

    public static final int REPORT_ACK_BODYLENGTH = 4;

    public static final int E_OK = 0;

    public static final int E_SYSFAIL = 1;

    public static final int E_AUTH_FAIL = 2;

    public static final int E_FORMAT_ERR = 3;

    public static final int E_NOT_BOUND = 4;

    public static final int E_NO_DESTIN = 5;

    public static final int E_SENT = 6;

    public static final int E_EXPIRED = 7;

    public static final int E_INVALID_TERM = 11;

    public static final int E_OVERFLOW = 12;

    public static final int E_EXPIRED_PWD = 13;

    public static final int E_NOSERVICE = 7;

    public static final int E_POWEROFF = 8;

    public static final int E_SHADE = 9;

    public static final int E_MSGFULL = 10;

    public static final int E_ETC = 11;

    public static final int E_PORTED_OUT = 13;

    public static final int E_EXRIRED = 21;

    /** 수신 번호 PREFIX에러 */
    public static final int E_PREFIX = 101;

    /** 수신번호 길이 에러 */
    public static final int E_DSTADDR_LEGNTH = 102;

    /** 메시지가 없습니다. */
    public static final int E_NULL_TEXT = 103;

    /** 메시지 길이 에러 (최대 80bytes) */
    public static final int E_TEXT_LENGTH = 104;

    /** WEBPUSH 길이 에러 (최대 232bytes) */
    public static final int E_WEBURL_LENGTH = 105;

    public static final int SMS_ID_LENGTH = 16;
    public static final int SMS_PWD_LENGTH = 16;
    public static final int SMS_RESULT_LENGTH = 16;
    public static final int SMS_PREFIX_LENGTH = 16;
    public static final int SMS_TID_LENGTH = 4;
    public static final int SMS_ORGADDR_LENGTH = 32;
    public static final int SMS_DSTADDR_LENGTH = 32;
    public static final int SMS_SN_LENGTH = 4;
    public static final int SMS_SENDTIME_LENGTH = 20;
    public static final int SMS_CALLBACK_LENGTH = 32;
    public static final int SMS_DELIVERTIME_LENGTH = 20;

    public static final int SMS_TEXT_LENGTH = 80;
    public static final int SMS_URL_LENGTH = 80;

    public static final int SMS_GENERAL_TEXT_LENGTH = 160;

    public static final int CLIENT_RESPONSE_LENGTH = 4;
    public static final int CLIENT_BODY_LENTH = 540;
    public static final int CLIENT_ORGADDR_LEGNTH = 32;
    public static final int CLIENT_DSTADDR_LENGTH = 32;

    /** SNS Gateway등 이 모듈을 쓰는 클라이언트에서 보낼 수 있는 최대 WEBPUSH text 크기 */
    public static final int CLIENT_TEXT_LENGTH = 230;

    /** SNS Application ID */
    public static final int SNS_APPLICATION_ID_LENGTH = 8;

    /** SNS Gateway등 이 모듈을 쓰는 클라이언트에서 보낼 수 있는 최대 WEBPUSH URL 크기 */
    public static final int CLIENT_URL_LENGTH = 230;

    public static final int WEB_PUSH_LSM_USER_TEXT_LENGTH = 78;

    /** 일반 SMS TID */
    public static final int GENERAL_SMS_TID = 4098;

    /** WAP PUSH TID */
    public static final int WAPPUSH_TID = 49167;

    /** WEBPUSH용 SMS TID */
    public static final int WEBPUSH_SMS_TID = 49765;

    /** WEBPUSH용 LMS TID */
    public static final int WEBPUSH_LMS_TID = 49766;

    /** SNS용 SMS TID */
    public static final int SNS_SMS_TID = 49162;

    /** SNS용 LMS TID */
    public static final int SNS_LMS_TID = 49163;

    /** SNS SMS 각 필드 구분자 */
    public static final byte SMS_FIELD_SEPARATOR = 0x1D;

    /** SNS SMS 끝을 나타내는 구분자 */
    public static final byte SMS_END_SEPARATOR = 0x03;

    /** 공통부 길이 */
    public static final int HEADER_LENGTH = 4;

    /** 전체 길이를 나타내는 필드의 길이 */
    public static final int LENGTH_LENGTH = 4;

    /** PACKET TYPE 필드의 길이 */
    public static final int PACKET_TYPE_LENGTH = 2;

    /** TARGET_SERVICE 필드의 길이 */
    public static final int TARGET_SERVICE_LENGTH = 20;

    /** CQ PACKET TYPE */
    public static final String CQ_PACKET_TYPE = "CQ";

    /** CR PACKET TYPE */
    public static final String CR_PACKET_TYPE = "CR";

    /** TQ PACKET TYPE */
    public static final String TQ_PACKET_TYPE = "TQ";

    /** TR PACKET TYPE */
    public static final String TR_PACKET_TYPE = "TR";

    /** FIELD TYPE NUMBER */
    public static final String FIELD_TYPE_NUMBER = "N";

    /** FIELD TYPE CHARACTER */
    public static final String FIELD_TYPE_CHARACTER = "C";

    /** SYNCAT 서버 정상 */
    public static final String SERVER_STAT_NORMAL = "0";

    /** SYNCAT 서버 장애 */
    public static final int SERVER_STAT_ERROR = 1;

    /** 서버 상태 필드 명 */
    public static final String SERVER_STAT_FIELD_NAME = "ServerStat";

    /** Transaction Id 필드 명 */
    public static final String TranId_FIELD_NAME = "TranId";

    /** Account 필드 명 */
    public static final String Account_FIELD_NAME = "Account";

    /** AuthTelNo 필드 명 */
    public static final String AuthTelNo_FIELD_NAME = "AuthTelNo";

    /** TargetService 필드 명 */
    public static final String TargetService_FIELD_NAME = "TargetService";

    /** 공통 WorkCd 필드 명 */
    public static final String WorkCd_FIELD_NAME = "WorkCd";

    /** SvcManChange 필드 명 */
    public static final String SvcManChange_FIELD_NAME = "SvcManChange";

    /** InBankName 필드 명 */
    public static final String InBankName_FIELD_NAME = "InBankName";

    /** InClientName 필드 명 */
    public static final String ClientName_FIELD_NAME = "ClientName";

    /** InClientName 필드 명 */
    public static final String InClientName_FIELD_NAME = "InClientName";

    /** InAmount 필드 명 */
    public static final String InAmount_FIELD_NAME = "InAmount";

    /** TotalCnt 필드 명 */
    public static final String TotalCnt_FIELD_NAME = "TotalCnt";

    /** TotalAmount 필드 명 */
    public static final String TotalAmount_FIELD_NAME = "TotalAmount";

    /** ResTfDateTime 필드 명 */
    public static final String ResTfDateTime_FIELD_NAME = "ResTfDateTime";

    /** AutoTfDateTime 필드 명 */
    public static final String AutoTfDateTime_FIELD_NAME = "AutoTfDateTime";

    /** ArrayCnt 필드 명 */
    public static final String ArrayCnt_FIELD_NAME = "ArrayCnt";

    /** 전화승인결과 필드 명 */
    public static final String ResultCode_FIELD_NAME = "ResultCode";

    /** 은행으로 보내는 CR 패킷의 길이 */
    public static final int BANK_CR_PACKET_LENGTH = 28;

    /** TargetService의 길이 20 */
    public static final int TargetService_LENGTH = 20;

    /** Account의 길이 20 */
    public static final int Account_LENGTH = 20;

    /** 거래번호 길이 20 */
    public static final int TranId_LENGTH = 20;

    /** 승인요청번호 길이 20 */
    public static final int AuthTelNo_LENGTH = 20;

    /** 보내는사람 길이 40 */
    public static final int ClientName_LENGTH = 40;

    /** 인증번호 13 */
    public static final int Ssn_LENGTH = 13;

    /** 영문체크 길이 02 */
    public static final int IdManWorkCd_LENGTH = 2;

    /** 공통 WorkCd 길이 0 2 */
    public static final int WorkCd_LENGTH = 2;

    /** 입금 은행 길이 40 */
    public static final int InBankName_LENGTH = 40;

    /** 입금자 이름 길이 40 */
    public static final int InClientName_LENGTH = 40;

    /** 금액 길이 20 */
    public static final int InAmount_LENGTH = 20;

    /** 건수 길이 3 */
    public static final int TotalCnt_LENGTH = 3;

    /** 총 합계 금액 길이 20 */
    public static final int TotalAmount_LENGTH = 20;

    /** 예약이체시간 길이 12 */
    public static final int ResTfDateTime_LENGTH = 12;

    /** 자동이체시간 길이 12 */
    public static final int AutoTfDateTime_LENGTH = 12;

    /** 배열 갯수 길이 3 */
    public static final int ArrayCnt_LENGTH = 3;

    /** 반복_입금은행 길이 40 */
    public static final int ArBankName_LENGTH = 40;

    /** 반복_받는사람 길이 40 */
    public static final int ArClientName_LENGTH = 40;

    /** 반복_입금 금액 길이 20 */
    public static final int ArAmount_LENGTH = 20;

    /** 서비스 길이 2 */
    public static final int SvcManChange_LENGTH = 2;

    /** 결과 코드 길이 4 */
    public static final int ResultCode_LENGTH = 4;

}
