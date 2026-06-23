package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "CbIbk01H12800Res", type = Type.RESPONSE, description = "아이디찾기 선조회 응답")
public class CbIbk01H12800Res implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10)
    private String UserID;

    @MessageField(id = "YOJMNO", name = "주민번호", length = 13, masking = true, maskingType = "01")
    private String YOJMNO;

    @MessageField(id = "YOGJCHK", name = "계좌번호체크", length = 1)
    private String YOGJCHK;

    @MessageField(id = "YOCDCHK", name = "카드번호체크", length = 1)
    private String YOCDCHK;

    @MessageField(id = "YOTELCHK", name = "전화인증검증", length = 1)
    private String YOTELCHK;

    @MessageField(id = "YOHPHON1", name = "자택전화1", length = 4)
    private String YOHPHON1;

    @MessageField(id = "YOHPHON2", name = "자택전화2", length = 4)
    private String YOHPHON2;

    @MessageField(id = "YOHPHON3", name = "자택전화3", length = 4)
    private String YOHPHON3;

    @MessageField(id = "YOOPHON1", name = "직장전화1", length = 4)
    private String YOOPHON1;

    @MessageField(id = "YOOPHON2", name = "직장전화2", length = 4)
    private String YOOPHON2;

    @MessageField(id = "YOOPHON3", name = "직장전화3", length = 4)
    private String YOOPHON3;

    @MessageField(id = "YOHHP1", name = "휴대폰1", length = 4)
    private String YOHHP1;

    @MessageField(id = "YOHHP2", name = "휴대폰2", length = 4)
    private String YOHHP2;

    @MessageField(id = "YOHHP3", name = "휴대폰3", length = 4)
    private String YOHHP3;

    @MessageField(id = "YODUMMY", name = "더미", length = 58)
    private String YODUMMY;

}
