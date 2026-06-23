package com.scbank.process.api.svc.common.service.support.dto.pushNotification;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 요청 정보 클래스
 * 입출금 내역 푸시 등록/수정/삭제
 */
@Data
@IntegrationMessage(id = "SupPntUpdateTransferPushRequest", type = Type.REQUEST)
public class SupPntUpdateTransferPushRequest implements IMessageObject {

    @MessageField(id = "workType", name = "업무구분값 (1:신규, 2:추가, 3:변경, 4:삭제)")
    private String workType;

    @MessageField(id = "iotranlistFlag", name = "입출금 푸쉬알림 활성화 설정상태 Y/N")
    private String iotranlistFlag;

    @MessageField(id = "drawAccName", name = "계좌명")
    private String drawAccName;

    @MessageField(id = "mutongGB", name = "통지구분")
    private String mutongGB;

    @MessageField(id = "tongjiMoney", name = "통지금액")
    private String tongjiMoney;

    @MessageField(id = "centerProcessGB", name = "시간설정")
    private String centerProcessGB;

    @MessageField(id = "transferStartTime", name = "시간설정-시작")
    private String transferStartTime;

    @MessageField(id = "transferOutTime", name = "시간설정-종료")
    private String transferOutTime;

    @MessageField(id = "yiJANMU", name = "잔액표시")
    private String yiJANMU;

    @MessageField(id = "tongjiAccutNum1", name = "계좌번호")
    private String tongjiAccutNum1;

    @MessageField(id = "acctPasswd", name = "계좌비밀번호")
    private String acctPasswd;

    @MessageField(id = "serno", name = "고객일련번호")
    private String serno;

    @MessageField(id = "benefitFlag", name = "")
    private String benefitFlag;

    @MessageField(id = "financeFlag", name = "")
    private String financeFlag;

    @MessageField(id = "financeVal", name = "")
    private String financeVal;

    @MessageField(id = "wmloungeFlag", name = "")
    private String wmloungeFlag;

    @MessageField(id = "notyExrateFlg", name = "")
    private String notyExrateFlg;

    @MessageField(id = "acctSum", name = "")
    private String acctSum;

}
