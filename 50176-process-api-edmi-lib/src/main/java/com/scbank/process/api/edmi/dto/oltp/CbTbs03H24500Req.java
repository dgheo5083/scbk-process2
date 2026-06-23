package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "CbTbs03H24500Req", type = Type.REQUEST, captureSystem = "OLTP", description = "오픈뱅킹 자동이체 정보 조회 요청 전문")
public class CbTbs03H24500Req implements IMessageObject {

    @MessageField(id = "YIUSID", name = "이용자번호", length = 10)
    private String YIUSID;

    @MessageField(id = "YIPASS", name = "통신비밀번호", length = 8)
    private String YIPASS;

    @MessageField(id = "YIJMNO", name = "주민번호", length = 13)
    private String YIJMNO;

    /* - YIJHGB (조회구분) */
    /* 1 :단건조회（PK입력) */
    /* 2 :입금계좌별조회 */
    /* 3 :출금계좌별조회 */
    /* 4 :이용자번호별조회(USID) */
    /* 5 : CIF번호별조회 */
    /* C :충전입금가능계좌여부조회 */
    /* H :입금계좌해약가능여부조회 */
    @MessageField(id = "YIJHGB", name = "조회구분", length = 1)
    private String YIJHGB;

    @MessageField(id = "YIIGJNO", name = "입금계좌번호", length = 11)
    private String YIIGJNO;

    @MessageField(id = "YICGJNO", name = "출금계좌번호", length = 20)
    private String YICGJNO;

    @MessageField(id = "YIICJR", name = "이체종류", length = 1)
    private String YIICJR;

    @MessageField(id = "YICIFNO", name = "CIF번호", length = 13)
    private String YICIFNO;

}
