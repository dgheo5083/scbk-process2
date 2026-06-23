package com.scbank.process.api.svc.common.service.settings.dto.app;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "SetAppGetInitInfoResponse", description = "앱 초기화 정보조회 응답 DTO", type = Type.RESPONSE)
public class SetAppGetInitInfoResponse implements IMessageObject {

    @MessageField(id = "sessionTime", name = "서버세션시간")
    private String sessionTime;

    @MessageField(id = "hostUrl", name = "Host Url")
    private String hostUrl;

    @MessageField(id = "mainPageUrl", name = "Main Page Url")
    private String mainPageUrl;

    @MessageField(id = "authUrl", name = "Auth Url")
    private String authUrl;

    @MessageField(id = "timeUrl", name = "PKI Time Url")
    private String timeUrl;

    @MessageField(id = "relayUrl", name = "Relay Url (인증서 복사)")
    private String relayUrl;

    @MessageField(id = "vpcgAgentUrl", name = "VPCG WIZVERA 게이트웨이?")
    private String vpcgAgentUrl;

    @MessageField(id = "vpcgAppUrl", name = "VPCG WIZVERA G10 랜딩페이지")
    private String vpcgAppUrl;

    @MessageField(id = "nfilterPublicKey", name = "nfilter 공개키(e2e)")
    private String nfilterPublicKey;

    @MessageField(id = "noticeEmergency", name = "긴급공지(json)")
    private String noticeEmergency;

    @MessageField(id = "createTableQuery", name = "테이블 생성 쿼리")
    private String createTableQuery;

    @MessageField(id = "gatewayUrl", name = "게이트웨이 Url")
    private String gatewayUrl;

    @MessageField(id = "loginMenuUrl", name = "로그인 페이지")
    private String loginMenuUrl;

    @MessageField(id = "loginTypeMenuUrl", name = "로그인방식 페이지")
    private String loginTypeMenuUrl;

    @MessageField(id = "joinMembershipMenuUrl", name = "회원가입 페이지")
    private String joinMembershipMenuUrl;

    @MessageField(id = "financialProductMainUrl", name = "금융상품몰 메인페이지")
    private String financialProductMainUrl;

    @MessageField(id = "authcenterMainUrl", name = "인증센터 메인페이지")
    private String authcenterMainUrl;

    @MessageField(id = "pushUrl", name = "Push Url")
    private String pushUrl;

    @MessageField(id = "pushFcmUrl", name = "Push Fcm Url")
    private String pushFcmUrl;

    @MessageField(id = "pushMotpUrl", name = "Push MOTP Url")
    private String pushMotpUrl;

    @MessageField(id = "pushFcmTk", name = "Push Fcm TK (native 취약조치)")
    private String pushFcmTk;

    @MessageField(id = "ntcShowYn", name = "긴급공지 노출여부", defaultValue = "N")
    private String ntcShowYn;

    @MessageField(id = "ntcTitle", name = "긴급공지 제목")
    private String ntcTitle;

    @MessageField(id = "ntcContents", name = "긴급공지 내용")
    private String ntcContents;

    @MessageField(id = "ntcBtnFlg", name = "긴급공지 버튼플래그 (C:기존처럼 공지닫은 후 앱실행, L:(close or link) close일때는 공지보고 앱종료, close가아니면 링크이동)")
    private String ntcBtnFlg;

    @MessageField(id = "ntcBtnUrl", name = "긴급공지 링크 Url (close일때는 공지보고 앱종료, close가아니면 링크이동)")
    private String ntcBtnUrl;

    @MessageField(id = "ntcBtnName", name = "버튼명")
    private String ntcBtnName;

    @MessageField(id = "svrIosVersion", name = "IOS 앱버전")
    private String svrIosVersion;

    @MessageField(id = "svrAndroidVersion", name = "안드로이드 앱버전")
    private String svrAndroidVersion;

    @MessageField(id = "v3MobileRealtimeAI", name = "V3 AI")
    private String v3MobileRealtimeAI;

    @MessageField(id = "v3MobileRealtimeRC", name = "V3 RC")
    private String v3MobileRealtimeRC;

    @MessageField(id = "v3MobileDetectNum", name = "V3 탐지레벨")
    private String v3MobileDetectNum;

    @MessageField(id = "homeMenuInfo", name = "홈메뉴 탭정보")
    private HomeMenuInfo homeMenuInfo;
}
