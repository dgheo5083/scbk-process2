package com.scbank.process.api.edmi.dto.oltp;

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
@IntegrationMessage(id = "CbIbk01H92000Req", type = Type.REQUEST, captureSystem = "OLTP", description = "고객정보조회 요청 전문")
public class CbIbk01H92000Req implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, masking = true, maskingType = "03", encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "InJuMinNo", name = "주민등록번호", length = 13, masking = true, maskingType = "01", align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String InJuMinNo;

    @MessageField(id = "GjBunHo", name = "계좌번호", length = 11, masking = true, maskingType = "02")
    private String GjBunHo;

    /**
     * TOBE 암호화타입 어떻게?
     * <ext-ignore-condition> <!-- APP 이중암호화 대응 -->
     * <ext-start>_DNFE2E_</ext-start>
     * <ext-end>_/DNFE2E_</ext-end>
     * </ext-ignore-condition>
     * <ext-ignore-condition> <!-- WEB 이중암호화 대응 -->
     * <ext-start>_DVKEY_</ext-start>
     * <ext-end>_/DVKEY_</ext-end>
     * </ext-ignore-condition>
     */
    @MessageField(id = "GjPassword", name = "계좌비밀번호", length = 4, masking = true, maskingType = "03")
    private String GjPassword;

    @MessageField(id = "BKGuBun", name = "변경구분", length = 1)
    private String BKGuBun;

    @MessageField(id = "BeCustName", name = "변경전고객이름", length = 22, masking = true, maskingType = "04")
    private String BeCustName;

    @MessageField(id = "BeJuMinNo", name = "변경전주민등록번호(자택)", length = 13, masking = true, maskingType = "01", align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String BeJuMinNo;

    @MessageField(id = "BeBirthday", name = "변경전생년월일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String BeBirthday;

    @MessageField(id = "BeSolarLunarCalPart", name = "변경전양음력구분", length = 1)
    private String BeSolarLunarCalPart;

    @MessageField(id = "BeHomeHT", name = "변경전주거소유형태", length = 1)
    private String BeHomeHT;

    @MessageField(id = "BeHomeJL", name = "변경전주거종류", length = 1)
    private String BeHomeJL;

    @MessageField(id = "BeHomePostNum", name = "변경전우편번호", length = 6)
    private String BeHomePostNum;

    @MessageField(id = "BeHomeAddrHs", name = "변경전주소 시작", length = 1)
    private String BeHomeAddrHs;

    @MessageField(id = "BeHomeAddr", name = "변경전주소", length = 60, masking = true, maskingType = "06", multiBytes = true)
    private String BeHomeAddr;

    @MessageField(id = "BeHomeAddrHe", name = "변경전주소 종료", length = 1)
    private String BeHomeAddrHe;

    @MessageField(id = "BeHomeTele1", name = "변경전전화번호1", length = 4)
    private String BeHomeTele1;

    @MessageField(id = "BeHomeTele2", name = "변경전전화번호2", length = 4)
    private String BeHomeTele2;

    @MessageField(id = "BeHomeTele3", name = "변경전전화번호3", length = 4, masking = true, maskingType = "03")
    private String BeHomeTele3;

    @MessageField(id = "BeJobTele1", name = "변경전전화번호1", length = 4)
    private String BeJobTele1;

    @MessageField(id = "BeJobTele2", name = "변경전전화번호2", length = 4)
    private String BeJobTele2;

    @MessageField(id = "BeJobTele3", name = "변경전전화번호3", length = 4, masking = true, maskingType = "03")
    private String BeJobTele3;

    @MessageField(id = "BeJobPostNum", name = "변경전우편번호", length = 6)
    private String BeJobPostNum;

    @MessageField(id = "BeJobAddrHs", name = "변경전주소 시작", length = 1)
    private String BeJobAddrHs;

    @MessageField(id = "BeJobAddr", name = "변경전주소", length = 60, masking = true, maskingType = "06", multiBytes = true)
    private String BeJobAddr;

    @MessageField(id = "BeJobAddrHe", name = "변경전주소 종료", length = 1)
    private String BeJobAddrHe;

    @MessageField(id = "BeJobNameHs", name = "변경전직장명 시작", length = 1)
    private String BeJobNameHs;

    @MessageField(id = "BeJobName", name = "변경전직장명", length = 20)
    private String BeJobName;

    @MessageField(id = "BeJobNameHe", name = "변경전직장명 종료", length = 1)
    private String BeJobNameHe;

    @MessageField(id = "BeJobBSNameHs", name = "변경전부서명 시작", length = 1)
    private String BeJobBSNameHs;

    @MessageField(id = "BeJobBSName", name = "변경전부서명", length = 10)
    private String BeJobBSName;

    @MessageField(id = "BeJobBSNameHe", name = "변경전부서명 종료", length = 1)
    private String BeJobBSNameHe;

    @MessageField(id = "BeJobpositionHs", name = "변경전직장직위 시작", length = 1)
    private String BeJobpositionHs;

    @MessageField(id = "BeJobposition", name = "변경전직장직위", length = 10)
    private String BeJobposition;

    @MessageField(id = "BeJobpositionHe", name = "변경전직장직위 종료", length = 1)
    private String BeJobpositionHe;

    @MessageField(id = "BeJob", name = "변경전직업", length = 2)
    private String BeJob;

    @MessageField(id = "BeEmail", name = "변경전E-mail", length = 30, masking = true, maskingType = "07")
    private String BeEmail;

    @MessageField(id = "BeHandPhone1", name = "변경전이동전화번호1", length = 4)
    private String BeHandPhone1;

    @MessageField(id = "BeHandPhone2", name = "변경전이동전화번호2", length = 4)
    private String BeHandPhone2;

    @MessageField(id = "BeHandPhone3", name = "변경전이동전화번호3", length = 4, masking = true, maskingType = "03")
    private String BeHandPhone3;

    @MessageField(id = "BeWeddingday", name = "변경전결혼기념일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String BeWeddingday;

    @MessageField(id = "BeReligion", name = "변경전종교", length = 1)
    private String BeReligion;

    @MessageField(id = "BeHobby", name = "변경전취미", length = 50)
    private String BeHobby;

    @MessageField(id = "BeYearPay", name = "변경전연간소득", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String BeYearPay;

    @MessageField(id = "BeMarryYeubu", name = "변경전결혼여부", length = 1)
    private String BeMarryYeubu;

    @MessageField(id = "BeMailSend", name = "변경전우편물발송지", length = 1)
    private String BeMailSend;

    @MessageField(id = "Be1TimeDepLimt", name = "변경전1회이체한도", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String Be1TimeDepLimt;

    @MessageField(id = "Be1DayDepLimt", name = "변경전1일이체한도", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String Be1DayDepLimt;

    @MessageField(id = "BeInterest", name = "변경전관심분야", length = 20)
    private String BeInterest;

    @MessageField(id = "AfCustName", name = "변경후고객이름", length = 22, masking = true, maskingType = "04")
    private String AfCustName;

    @MessageField(id = "AfJuMinNo", name = "변경전우편물발송지", length = 13, masking = true, maskingType = "01", align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String AfJuMinNo;

    @MessageField(id = "AfBirthday", name = "변경후생년월일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String AfBirthday;

    @MessageField(id = "AfSolarLunarCalPart", name = "변경후양음력구분", length = 1)
    private String AfSolarLunarCalPart;

    @MessageField(id = "AfHomeHT", name = "변경후주거소유형태", length = 1)
    private String AfHomeHT;

    @MessageField(id = "AfHomeJL", name = "변경후주거종류", length = 1)
    private String AfHomeJL;

    @MessageField(id = "AfHomePostNum", name = "변경후우편번호", length = 6)
    private String AfHomePostNum;

    @MessageField(id = "AfHomeAddrHs", name = "변경후주소 시작", length = 1)
    private String AfHomeAddrHs;

    @MessageField(id = "AfHomeAddr", name = "변경후주소", length = 60, masking = true, maskingType = "06", multiBytes = true)
    private String AfHomeAddr;

    @MessageField(id = "AfHomeAddrHe", name = "변경후주소 종료", length = 1)
    private String AfHomeAddrHe;

    @MessageField(id = "AfHomeTele1", name = "변경후전화번호1", length = 4)
    private String AfHomeTele1;

    @MessageField(id = "AfHomeTele2", name = "변경후전화번호2", length = 4)
    private String AfHomeTele2;

    @MessageField(id = "AfHomeTele3", name = "변경후전화번호3", length = 4, masking = true, maskingType = "03")
    private String AfHomeTele3;

    @MessageField(id = "AfJobTele1", name = "변경후전화번호1", length = 4)
    private String AfJobTele1;

    @MessageField(id = "AfJobTele2", name = "변경후전화번호2", length = 4)
    private String AfJobTele2;

    @MessageField(id = "AfJobTele3", name = "변경후전화번호3", length = 4, masking = true, maskingType = "03")
    private String AfJobTele3;

    @MessageField(id = "AfJobPostNum", name = "변경후우편번호", length = 6)
    private String AfJobPostNum;

    @MessageField(id = "AfJobAddrHs", name = "변경후주소 시작", length = 1)
    private String AfJobAddrHs;

    @MessageField(id = "AfJobAddr", name = "변경후주소", length = 60, masking = true, maskingType = "06", multiBytes = true)
    private String AfJobAddr;

    @MessageField(id = "AfJobAddrHe", name = "변경후주소 종료", length = 1)
    private String AfJobAddrHe;

    @MessageField(id = "AfJobNameHs", name = "변경후직장명 시작", length = 1)
    private String AfJobNameHs;

    @MessageField(id = "AfJobName", name = "변경후직장명", length = 20)
    private String AfJobName;

    @MessageField(id = "AfJobNameHe", name = "변경후직장명 종료", length = 1)
    private String AfJobNameHe;

    @MessageField(id = "AfJobBSNameHs", name = "변경후부서명 시작", length = 1)
    private String AfJobBSNameHs;

    @MessageField(id = "AfJobBSName", name = "변경후부서명", length = 10)
    private String AfJobBSName;

    @MessageField(id = "AfJobBSNameHe", name = "변경후부서명 종료", length = 1)
    private String AfJobBSNameHe;

    @MessageField(id = "AfJobpositionHs", name = "변경후직장직위 시작", length = 1)
    private String AfJobpositionHs;

    @MessageField(id = "AfJobposition", name = "변경후직장직위", length = 10)
    private String AfJobposition;

    @MessageField(id = "AfJobpositionHe", name = "변경후직장직위 종료", length = 1)
    private String AfJobpositionHe;

    @MessageField(id = "AfJob", name = "변경후직업", length = 2)
    private String AfJob;

    @MessageField(id = "AfEmail", name = "변경후E-mail", length = 30, masking = true, maskingType = "07")
    private String AfEmail;

    @MessageField(id = "AfHandPhone1", name = "변경후이동전화번호1", length = 4)
    private String AfHandPhone1;

    @MessageField(id = "AfHandPhone2", name = "변경후이동전화번호2", length = 4)
    private String AfHandPhone2;

    @MessageField(id = "AfHandPhone3", name = "변경후이동전화번호3", length = 4, masking = true, maskingType = "03")
    private String AfHandPhone3;

    @MessageField(id = "AfWeddingday", name = "변경후결혼기념일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String AfWeddingday;

    @MessageField(id = "AfReligion", name = "변경후종교", length = 1)
    private String AfReligion;

    @MessageField(id = "AfHobby", name = "변경후취미", length = 50)
    private String AfHobby;

    @MessageField(id = "AfYearPay", name = "변경후연간소득", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String AfYearPay;

    @MessageField(id = "AfMarryYeubu", name = "변경후결혼여부", length = 1)
    private String AfMarryYeubu;

    @MessageField(id = "AfMailSend", name = "변경후우편물발송지", length = 1)
    private String AfMailSend;

    @MessageField(id = "Af1TimeDepLimt", name = "변경후1회이체한도", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String Af1TimeDepLimt;

    @MessageField(id = "Af1DayDepLimt", name = "변경후1일이체한도", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String Af1DayDepLimt;

    @MessageField(id = "AfInterest", name = "변경후관심분야", length = 20)
    private String AfInterest;

    @MessageField(id = "YISINCA", name = "신용카드신규여부", length = 1)
    private String YISINCA;

    @MessageField(id = "YIADRGB1", name = "자택_전_사용여부", length = 1)
    private String YIADRGB1;

    @MessageField(id = "YIZIPTXT1Hs", name = "구주소1(자택_전) 시작", length = 1)
    private String YIZIPTXT1Hs;

    @MessageField(id = "YIZIPTXT1", name = "구주소1(자택_전)", length = 40)
    private String YIZIPTXT1;

    @MessageField(id = "YIZIPTXT1He", name = "구주소1(자택_전) 종료", length = 1)
    private String YIZIPTXT1He;

    @MessageField(id = "YIZIPCD40", name = "신우편번호(자택_전)", length = 6)
    private String YIZIPCD40;

    @MessageField(id = "YIZIPCD41", name = "신도로명코드(자택_전)", length = 12)
    private String YIZIPCD41;

    @MessageField(id = "YIZIPCD42", name = "신일련번호(자택_전)", length = 2)
    private String YIZIPCD42;

    @MessageField(id = "YIZIPCD43", name = "신지하여부(자택_전)", length = 1)
    private String YIZIPCD43;

    @MessageField(id = "YIZIPCD44", name = "신건물번호(자택_전)", length = 10)
    private String YIZIPCD44;

    @MessageField(id = "YISJUSO4Hs", name = "신도로명(자택_전) 시작", length = 1)
    private String YISJUSO4Hs;

    @MessageField(id = "YISJUSO4", name = "신도로명(자택_전)", length = 70)
    private String YISJUSO4;

    @MessageField(id = "YISJUSO4He", name = "신도로명(자택_전) 종료", length = 1)
    private String YISJUSO4He;

    @MessageField(id = "YIUSO4Hs", name = "신상세주소(자택_전) 시작", length = 1)
    private String YIUSO4Hs;

    @MessageField(id = "YIUSO4", name = "상세주소(자택_전)", length = 100, masking = true, maskingType = "06", multiBytes = true)
    private String YIUSO4;

    @MessageField(id = "YIUSO4He", name = "신상세주소(자택_전) 종료", length = 1)
    private String YIUSO4He;

    @MessageField(id = "YIADRGB2", name = "자택_후_사용여부", length = 1)
    private String YIADRGB2;

    @MessageField(id = "YIZIPTXT2Hs", name = "구주소1(자택_후) 시작", length = 1)
    private String YIZIPTXT2Hs;

    @MessageField(id = "YIZIPTXT2", name = "구주소1(자택_후)", length = 40)
    private String YIZIPTXT2;

    @MessageField(id = "YIZIPTXT2He", name = "구주소1(자택_후) 종료", length = 1)
    private String YIZIPTXT2He;

    @MessageField(id = "YIZIPCD50", name = "신우편번호(자택_후)", length = 6)
    private String YIZIPCD50;

    @MessageField(id = "YIZIPCD51", name = "신도로명코드(자택_후)", length = 12)
    private String YIZIPCD51;

    @MessageField(id = "YIZIPCD52", name = "신일련번호(자택_후)", length = 2)
    private String YIZIPCD52;

    @MessageField(id = "YIZIPCD53", name = "신지하여부(자택_후)", length = 1)
    private String YIZIPCD53;

    @MessageField(id = "YIZIPCD54", name = "신건물번호(자택_후)", length = 10)
    private String YIZIPCD54;

    @MessageField(id = "YISJUSO5Hs", name = "신도로명(자택_후) 시작", length = 1)
    private String YISJUSO5Hs;

    @MessageField(id = "YISJUSO5", name = "신도로명(자택_후)", length = 70)
    private String YISJUSO5;

    @MessageField(id = "YISJUSO5He", name = "신도로명(자택_후) 종료", length = 1)
    private String YISJUSO5He;

    @MessageField(id = "YIUSO5Hs", name = "신상세주소(자택_후) 시작", length = 1)
    private String YIUSO5Hs;

    @MessageField(id = "YIUSO5", name = "신상세주소(자택_후", length = 100, masking = true, maskingType = "06", multiBytes = true)
    private String YIUSO5;

    @MessageField(id = "YIUSO5He", name = "신상세주소(자택_후) 종료", length = 1)
    private String YIUSO5He;

    @MessageField(id = "YIJIKGB1", name = "직장_전_사용여부", length = 1)
    private String YIJIKGB1;

    @MessageField(id = "YIJIKTXT1Hs", name = "구주소1(직장_전) 시작", length = 1)
    private String YIJIKTXT1Hs;

    @MessageField(id = "YIJIKTXT1", name = "구주소1(직장_전)", length = 40)
    private String YIJIKTXT1;

    @MessageField(id = "YIJIKTXT1He", name = "구주소1(직장_전) 종료", length = 1)
    private String YIJIKTXT1He;

    @MessageField(id = "YIJIKCD40", name = "신우편번호(직장_전)", length = 6)
    private String YIJIKCD40;

    @MessageField(id = "YIJIKCD41", name = "신도로명코드(직장_전)", length = 12)
    private String YIJIKCD41;

    @MessageField(id = "YIJIKCD42", name = "신일련번호(직장_전)", length = 2)
    private String YIJIKCD42;

    @MessageField(id = "YIJIKCD43", name = "신지하여부(직장_전)", length = 1)
    private String YIJIKCD43;

    @MessageField(id = "YIJIKCD44", name = "신건물번호(직장_전)", length = 10)
    private String YIJIKCD44;

    @MessageField(id = "YIJIKDORO1Hs", name = "신도로명(직장_전) 시작", length = 1)
    private String YIJIKDORO1Hs;

    @MessageField(id = "YIJIKDORO1", name = "신도로명(직장_전)", length = 70)
    private String YIJIKDORO1;

    @MessageField(id = "YIJIKDORO1He", name = "신도로명(직장_전) 종료", length = 1)
    private String YIJIKDORO1He;

    @MessageField(id = "YIJIKSANG1Hs", name = "신상세주소(직장_전) 시작", length = 1)
    private String YIJIKSANG1Hs;

    @MessageField(id = "YIJIKSANG1", name = "신상세주소(직장_전)", length = 100, masking = true, maskingType = "06", multiBytes = true)
    private String YIJIKSANG1;

    @MessageField(id = "YIJIKSANG1He", name = "신상세주소(직장_전) 종료", length = 1)
    private String YIJIKSANG1He;

    @MessageField(id = "YIJIKGB2", name = "직장_후_사용여부", length = 1)
    private String YIJIKGB2;

    @MessageField(id = "YIJIKTXT2Hs", name = "구주소1(직장_후) 시작", length = 1)
    private String YIJIKTXT2Hs;

    @MessageField(id = "YIJIKTXT2", name = "구주소1(직장_후)", length = 40)
    private String YIJIKTXT2;

    @MessageField(id = "YIJIKTXT2He", name = "구주소1(직장_후) 종료", length = 1)
    private String YIJIKTXT2He;

    @MessageField(id = "YIJIKCD50", name = "신우편번호(직장_후)", length = 6)
    private String YIJIKCD50;

    @MessageField(id = "YIJIKCD51", name = "신도로명코드(직장_후)", length = 12)
    private String YIJIKCD51;

    @MessageField(id = "YIJIKCD52", name = "신일련번호(직장_후)", length = 2)
    private String YIJIKCD52;

    @MessageField(id = "YIJIKCD53", name = "신지하여부(직장_후)", length = 1)
    private String YIJIKCD53;

    @MessageField(id = "YIJIKCD54", name = "신건물번호(직장_후)", length = 10)
    private String YIJIKCD54;

    @MessageField(id = "YIJIKDORO2Hs", name = "신도로명(직장_후) 시작", length = 1)
    private String YIJIKDORO2Hs;

    @MessageField(id = "YIJIKDORO2", name = "신도로명(직장_후)", length = 70)
    private String YIJIKDORO2;

    @MessageField(id = "YIJIKDORO2He", name = "신도로명(직장_후) 종료", length = 1)
    private String YIJIKDORO2He;

    @MessageField(id = "YIJIKSANG2Hs", name = "신상세주소(직장_후) 시작", length = 1)
    private String YIJIKSANG2Hs;

    @MessageField(id = "YIJIKSANG2", name = "신상세주소(직장_후)", length = 100, masking = true, maskingType = "06", multiBytes = true)
    private String YIJIKSANG2;

    @MessageField(id = "YIJIKSANG2He", name = "신상세주소(직장_후) 종료", length = 1)
    private String YIJIKSANG2He;

    @MessageField(id = "YIJUSOGB1", name = "자택주소일괄변경", length = 1)
    private String YIJUSOGB1;

    @MessageField(id = "YIJUSOGB2", name = "직장주소일괄변경", length = 1)
    private String YIJUSOGB2;

    @MessageField(id = "YIBANKJRP", name = "신청금융기관건수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIBANKJRP;

    @MessageField(id = "YIINFAGR", name = "제3자제공동의", length = 1)
    private String YIINFAGR;

    @MessageField(id = "YIBANKCD1", name = "신청금융기관코드1", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIBANKCD1;

    @MessageField(id = "YIBANKCD2", name = "신청금융기관코드2", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIBANKCD2;

    @MessageField(id = "YIBANKCD3", name = "신청금융기관코드3", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIBANKCD3;

    @MessageField(id = "YIBANKCD4", name = "신청금융기관코드4", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIBANKCD4;

    @MessageField(id = "YIBANKCD5", name = "신청금융기관코드5", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIBANKCD5;

    @MessageField(id = "YIBANKCD6", name = "신청금융기관코드6", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIBANKCD6;

    @MessageField(id = "YIBANKCD7", name = "신청금융기관코드7", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIBANKCD7;

    @MessageField(id = "YIBANKCD8", name = "신청금융기관코드8", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIBANKCD8;

    @MessageField(id = "YIBANKCD9", name = "신청금융기관코드9", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIBANKCD9;

    @MessageField(id = "YIBANKCD10", name = "신청금융기관코드10", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIBANKCD10;

    @MessageField(id = "YISALES", name = "비대면세일즈앱", length = 1)
    private String YISALES;

    @MessageField(id = "YIEADRGB", name = "외환고객여부", length = 1)
    private String YIEADRGB;

    @MessageField(id = "YIEADGB", name = "영문주소구분", length = 1)
    private String YIEADGB;

    @MessageField(id = "YIEJUSO1", name = "영문/외국주소1", length = 45)
    private String YIEJUSO1;

    @MessageField(id = "YIEJUSO2", name = "영문/외국주소2", length = 45)
    private String YIEJUSO2;

    @MessageField(id = "YIEBUNHO", name = "해외전화번호", length = 22)
    private String YIEBUNHO;

}
