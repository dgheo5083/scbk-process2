package com.scbank.process.api.svc.common.service.support.dto.pushNotification;

import java.math.BigDecimal;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 응답 정보 클래스
 * 상세 조회 및 상세설정 화면
 */
@Data
@IntegrationMessage(id = "SupPntSetDetailSettingPageResponse", type = Type.RESPONSE)
public class SupPntSetDetailSettingPageResponse implements IMessageObject {

    @MessageField(id = "serno", name = "고객일련번호")
    private String serno;

    @MessageField(id = "drawAccNum", name = "통장번호")
    private String drawAccNum;

    @MessageField(id = "drawAccName", name = "통장명")
    private String drawAccName;

    @MessageField(id = "workType", name = "업무구분값 (1:신규, 2:추가, 3:변경, 4:삭제)")
    private String workType;

    @MessageField(id = "tongjiMoney", name = "통지거래금액 - 금액")
    private BigDecimal tongjiMoney;

    @MessageField(id = "mutongGB", name = "")
    private String mutongGB;

    @MessageField(id = "transferStartTime", name = "거래명세시작시간 - 알림수신 제외시간 시작")
    private String transferStartTime;

    @MessageField(id = "transferOutTime", name = "거래명세종료시간 - 알림수신 제외시간 종료")
    private String transferOutTime;

    @MessageField(id = "centerProcessGB", name = "센터처리통보구분 - 시간 설정")
    private String centerProcessGB;

    @MessageField(id = "yiJANMU", name = "통지범위 - 잔액범위")
    private String yiJANMU;

    @MessageField(id = "benefitFlag", name = "")
    private String benefitFlag;

    @MessageField(id = "financeFlag", name = "")
    private String financeFlag;

    @MessageField(id = "financeVal", name = "")
    private String financeVal;

    @MessageField(id = "iotranlistFlag", name = "")
    private String iotranlistFlag;

    @MessageField(id = "notyExrateFlg", name = "")
    private String notyExrateFlg;

    @MessageField(id = "wmloungeFlag", name = "")
    private String wmloungeFlag;

}
