package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Data;

@Data
@IntegrationMessage(id = "CbTbs07H97600Req", type = Type.REQUEST, description = "ACCOUNT_INFO보유계좌내역일괄조회금융회사조회)")
public class CbTbs07H97600Req implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자 ID", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "YIGBKCD", name = "조회대상금융회사코드", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGBKCD;

    @MessageField(id = "YIWGOYU", name = "조회원거래고유번호", length = 12, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIWGOYU;

    @MessageField(id = "YICHANL", name = "정보제공요청채널", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YICHANL;

    @MessageField(id = "YIBUNHO", name = "지정번호", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIBUNHO;

    @MessageField(id = "YIDACNT", name = "데이터건수", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIDACNT;

    @MessageField(id = "YIUPGB", name = "업권구분 1: 은행 2: 제２금융권 4: 금융투자회사", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIUPGB;
}
