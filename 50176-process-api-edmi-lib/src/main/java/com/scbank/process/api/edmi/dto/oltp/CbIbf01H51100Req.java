package com.scbank.process.api.edmi.dto.oltp;

import java.math.BigDecimal;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "CbIbf01H51100Req", type = Type.REQUEST, captureSystem = "OLTP", description = "인터넷 환전 요청")
public class CbIbf01H51100Req implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "TransPassword", name = "이체비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "03")
    private String TransPassword;

    @MessageField(id = "YIOTONG", name = "출금계좌통화", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIOTONG;

    @MessageField(id = "YIOGJNO", name = "출금계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIOGJNO;

    @MessageField(id = "CgAcctPassword", name = "계좌비밀번호", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "03")
    private String CgAcctPassword;

    @MessageField(id = "YISTONG", name = "수수료계좌통화", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YISTONG;

    @MessageField(id = "YISGJNO", name = "수수료계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YISGJNO;

    @MessageField(id = "CgAcctPassword2", name = "수수료계좌비밀", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String CgAcctPassword2;

    @MessageField(id = "YIICJR", name = "이체종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIICJR;

    @MessageField(id = "YIJSNO", name = "이체접수번호", length = 5, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIJSNO;

    @MessageField(id = "YIGJUGB", name = "거주구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJUGB;

    @MessageField(id = "YIJONG", name = "환전종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIJONG;

    @MessageField(id = "YIYGNO", name = "여권번호", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIYGNO;

    @MessageField(id = "YITONG", name = "환전통화코드", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONG;

    @MessageField(id = "YIHJAK", name = "외화현찰금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YIHJAK;

    @MessageField(id = "YITCAK", name = "여행자수표금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YITCAK;

    @MessageField(id = "YISYBR", name = "수령영업점번호", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YISYBR;

    @MessageField(id = "YIOHGJ", name = "외화입금계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIOHGJ;

    @MessageField(id = "YISLIL", name = "수령희망일자", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YISLIL;

    @MessageField(id = "YIUDAE", name = "우대구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIUDAE;

    @MessageField(id = "YIINGUBUN", name = "보험구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIINGUBUN;

    @MessageField(id = "YIINILJA", name = "보험시작일", length = 9, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIINILJA;

    @MessageField(id = "YITEL1", name = "신청일전화번호1", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YITEL1;

    @MessageField(id = "YITEL2", name = "신청일전화번호2", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YITEL2;

    @MessageField(id = "YITEL3", name = "신청일전화번호3", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YITEL3;

    @MessageField(id = "YIGRNO", name = "관리번호", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIGRNO;

    @MessageField(id = "YIGDGB", name = "공동구매구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGDGB;

    @MessageField(id = "YIGDGM", name = "공동구매 판매완료시추가우대될 % 우대율", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIGDGM;

    @MessageField(id = "YIGDNO", name = "공동구매상품일련번호", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIGDNO;

    @MessageField(id = "YIIKWJON00", name = "권종", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIIKWJON00;

    @MessageField(id = "YIIMESU000", name = "매수", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIIMESU000;

    @MessageField(id = "YIIKWJON01", name = "권종", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIIKWJON01;

    @MessageField(id = "YIIMESU001", name = "매수", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIIMESU001;

    @MessageField(id = "YIIKWJON02", name = "권종", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIIKWJON02;

    @MessageField(id = "YIIMESU002", name = "매수", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIIMESU002;

    @MessageField(id = "YIIKWJON03", name = "권종", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIIKWJON03;

    @MessageField(id = "YIIMESU003", name = "매수", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIIMESU003;

    @MessageField(id = "YIIKWJON04", name = "권종", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIIKWJON04;

    @MessageField(id = "YIIMESU004", name = "매수", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIIMESU004;

    @MessageField(id = "YIIKWJON05", name = "권종", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIIKWJON05;

    @MessageField(id = "YIIMESU005", name = "매수", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIIMESU005;

    @MessageField(id = "YIIKWJON06", name = "권종", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIIKWJON06;

    @MessageField(id = "YIIMESU006", name = "매수", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIIMESU006;

    @MessageField(id = "YIIKWJON07", name = "권종", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIIKWJON07;

    @MessageField(id = "YIIMESU007", name = "매수", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIIMESU007;

    @MessageField(id = "YIIKWJON08", name = "권종", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIIKWJON08;

    @MessageField(id = "YIIMESU008", name = "매수", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIIMESU008;

    @MessageField(id = "YIIKWJON09", name = "권종", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIIKWJON09;

    @MessageField(id = "YIIMESU009", name = "매수", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIIMESU009;

    @MessageField(id = "YIIDNAME", name = "대리인성명", length = 20, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIIDNAME;

    @MessageField(id = "E2ERegNum", name = "대리인주민번호", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String E2ERegNum;

    @MessageField(id = "YIIDTEL1", name = "대리인전화(지역)", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIIDTEL1;

    @MessageField(id = "YIIDTEL2", name = "대리인전화(국번)", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIIDTEL2;

    @MessageField(id = "YIIDTEL3", name = "대리인전호(번호)", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIIDTEL3;

    @MessageField(id = "YIIKJNO", name = "국제학생증보유", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIIKJNO;

    @MessageField(id = "YIDUMY1", name = "Dummy", length = 160, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIDUMY1;

    @MessageField(id = "YIIPN", name = "IP", length = 40, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIIPN;

    @MessageField(id = "YIMAC", name = "MAC", length = 20, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIMAC;

}
