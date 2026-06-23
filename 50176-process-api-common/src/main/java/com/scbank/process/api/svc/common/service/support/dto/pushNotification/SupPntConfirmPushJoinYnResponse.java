package com.scbank.process.api.svc.common.service.support.dto.pushNotification;

import java.util.List;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;

import lombok.Data;

/**
 * CSL 서비스 응답 정보 클래스
 * 푸시 알림 서비스 설정화면 화면을 호출 및 푸시 가입여부를 조회
 */
@Data
@IntegrationMessage(id = "SupPntConfirmPushJoinYnResponse", type = Type.RESPONSE)
public class SupPntConfirmPushJoinYnResponse implements IMessageObject {

    @MessageField(id = "serno", name = "고객일련번호")
    private String serno;

    @MessageField(id = "iotranlistFlag", name = "입출금 내역")
    private String iotranlistFlag;

    @MessageField(id = "exrateFlg", name = "환율알림 설정")
    private String exrateFlg;

    @MessageField(id = "benefitFlag", name = "맞춤혜택 안내")
    private String benefitFlag;

    @MessageField(id = "financeFlag", name = "금융시장 정보")
    private String financeFlag;

    @MessageField(id = "financeVal", name = "금융시장 정보 값 - 1:전체, 2:주간정보, 3:월간정보")
    private String financeVal;

    @MessageField(id = "wmloungeFlag", name = "WMLOUNGE 정보")
    private String wmloungeFlag;

    @MessageField(id = "appGb", name = "")
    private String appGb;

    @MessageField(id = "okCount", name = "")
    private int okCount;

    @MessageField(id = "cnfrmNo", name = "")
    private String cnfrmNo;

    @MessageField(id = "cnfrmNoNew", name = "")
    private String cnfrmNoNew;

    @MessageField(id = "operType", name = "")
    private String operType;

    @MessageField(id = "pushSrvcApprvlFlg", name = "")
    private String pushSrvcApprvlFlg;

    @MessageField(id = "appInfo", name = "")
    private String appInfo;

    @MessageField(id = "deviceMd", name = "")
    private String deviceMd;

    @MessageField(id = "deviceVis", name = "")
    private String deviceVis;

    @MessageField(id = "agrmntMk", name = "")
    private String agrmntMk;

    @MessageField(id = "callPageType", name = "")
    private String callPageType;

    @MessageField(id = "eventNo", name = "")
    private String eventNo;

    @MessageField(id = "breezePushJoinYN", name = "")
    private String breezePushJoinYN;

    @MessageField(id = "pushServiceCdList", name = "")
    @RepeatedField
    private List<PushServiceCd> pushServiceCdList;

    @MessageField(id = "pushAgreeInfoList", name = "")
    @RepeatedField
    private List<PushAgreeInfo> pushAgreeInfoList;

    @Data
    public static class PushServiceCd implements IMessageObject {

        @MessageField(id = "cmmnCd", name = "공통 코드")
        private String cmmnCd;

        @MessageField(id = "subCd", name = "실제 동의 여부를 체크하는 코드")
        private String subCd;

        @MessageField(id = "cdNm", name = "서비스 알림동의 명")
        private String cdNm;

        @MessageField(id = "expln", name = "서비스 알림동의 툴팁 내용")
        private String expln;

        @MessageField(id = "useInd", name = "사용여부")
        private String useInd;
    }

    @Data
    public static class PushAgreeInfo implements IMessageObject {

        @MessageField(id = "msgTypeSubCd", name = "")
        private String msgTypeSubCd;

        @MessageField(id = "useYn", name = "")
        private String useYn;

    }

}
