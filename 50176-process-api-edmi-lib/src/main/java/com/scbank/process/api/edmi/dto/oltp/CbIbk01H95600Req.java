package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@IntegrationMessage(id = "CbIbk01H95600Req", type = Type.REQUEST, description = "계좌별명관리 요청 전문", captureSystem = "OLTP")
public class CbIbk01H95600Req implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "InBrnchNum", name = "계좌번호", length = 11, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String InBrnchNum;

    @MessageField(id = "Gubun", name = "구분(1:등록 2:수정 3:삭제)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String Gubun;

    // @MessageField(id = "NickNameHs", name = "계좌별명한글시작", length = 1, align =
    // AlignType.LEFT)
    // private String NickNameHs; // 0x0E

    @MessageField(id = "NickName", name = "계좌별명", length = 22, align = AlignType.LEFT, sosi = true)
    private String NickName; // 0x20

    // @MessageField(id = "NickNameHe", name = "계좌별명한글끝", length = 1, align =
    // AlignType.LEFT)
    // private String NickNameHe; // 0x0F
}
