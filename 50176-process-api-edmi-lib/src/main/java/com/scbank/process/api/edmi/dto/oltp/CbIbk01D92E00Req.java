package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Data;

@Data
@IntegrationMessage(id = "CbIbk01D92E00Req", type = Type.REQUEST, description = "퍼스트알리미 가입조회변경 요청부")
public class CbIbk01D92E00Req implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "TranServiceGB", name = "거래구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TranServiceGB;

    @MessageField(id = "JHTranServiceGB", name = "조회거래구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String JHTranServiceGB;

    @MessageField(id = "JuminNoPart", name = "주민번호구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String JuminNoPart;

    @MessageField(id = "PerBusNo", name = "주민등록번호", length = 13, masking = true, maskingType = "01", align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String PerBusNo;

    @MessageField(id = "CgAcctNum", name = "수수료출금계좌", length = 11, masking = true, maskingType = "02", align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String CgAcctNum;

    @MessageField(id = "CgAcctNum", name = "수수료출금계좌비밀번호", length = 4, masking = true, maskingType = "03", align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String CgAcctPassword;

    @MessageField(id = "Email", name = "이메일", length = 30, masking = true, maskingType = "07", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String Email;

    @MessageField(id = "Phone1", name = "전화번호1", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String Phone1;

    @MessageField(id = "Phone2", name = "전화번호2", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String Phone2;

    @MessageField(id = "Phone3", name = "전화번호3", length = 4, masking = true, maskingType = "03", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String Phone3;

    @MessageField(id = "SmsServiceGB", name = "휴대폰거래명세통지구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SmsServiceGB;

    @MessageField(id = "MoneyTongjiVanGB", name = "환율통지매체구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String MoneyTongjiVanGB;

    @MessageField(id = "MoneyTongjiChgb", name = "환율통지서비스변경구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String MoneyTongjiChgb;

    @MessageField(id = "MoneyTongGB", name = "환율통지구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String MoneyTongGB;

    @MessageField(id = "TongCodeA1", name = "통화코드", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TongCodeA1;

    @MessageField(id = "MoneyTongjiMode", name = "환율통지방법구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String MoneyTongjiMode;

    @MessageField(id = "MoneyTongjiHour1", name = "환율통지시각1", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String MoneyTongjiHour1;

    @MessageField(id = "MoneyTongjiHour2", name = "환율통지시각2", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String MoneyTongjiHour2;

    @MessageField(id = "EjukumTongjiGB", name = "예적금만기일통지구분 1서비스이용 0서비스이용안함", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String EjukumTongjiGB;

    @MessageField(id = "LoanTongjiGB", name = "대출만기일통지구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String LoanTongjiGB;

    @MessageField(id = "EBankingTongjiGB", name = "전자금융통지구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String EBankingTongjiGB;

    @MessageField(id = "AutotransferGB", name = "자동이체불능통지구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AutotransferGB;

    @MessageField(id = "ServiceJoinGB", name = "조회신청구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String ServiceJoinGB;

    @MessageField(id = "SusuroHab", name = "서비스이용수수료합계", length = 9, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String SusuroHab;

    @MessageField(id = "TongjiAccutNum1", name = "통지계좌번호", length = 11, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String TongjiAccutNum1;

    @MessageField(id = "AcctPasswd", name = "통지계좌비밀번호", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO, masking = true, maskingType = "03")
    private String AcctPasswd;

    @MessageField(id = "TongjiMoney", name = "통지거래금액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String TongjiMoney;

    @MessageField(id = "MutongInGB1", name = "무통입금통지구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String MutongInGB1;

    @MessageField(id = "MutongOutGB1", name = "무통출금통지구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String MutongOutGB1;

    @MessageField(id = "TransferStartTime", name = "거래명세시작시간", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TransferStartTime;

    @MessageField(id = "TransferOutTime", name = "거래명세종료시간", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TransferOutTime;

    @MessageField(id = "CenterProcessGB", name = "센터처리통보구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String CenterProcessGB;

    @MessageField(id = "YIJANMU", name = "통지범위", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIJANMU;

    @MessageField(id = "CenterStartTime", name = "센터처리통지시작시간", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String CenterStartTime;

    @MessageField(id = "CenterOutTime", name = "센터처리통지종료시간", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String CenterOutTime;

    @MessageField(id = "SusuroGB", name = "수수료형태구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SusuroGB;

    @MessageField(id = "MengseTongGB", name = "거래명세통지신규변경", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String MengseTongGB;

    @MessageField(id = "MengseGB1", name = "거래명세통지구분1", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String MengseGB1;

    @MessageField(id = "DepositZong1", name = "예금종별", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String DepositZong1;

    @MessageField(id = "InOutGB1", name = "촐금통지구분1", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String InOutGB1;

    @MessageField(id = "Susuro1", name = "이용수수료1", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String Susuro1;

    @MessageField(id = "RegistDay1", name = "등록일자1", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String RegistDay1;

    @MessageField(id = "TongjiPhoneA1", name = "거래명세휴대전화번호1", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TongjiPhoneA1;

    @MessageField(id = "TongjiPhoneB1", name = "거래명세휴대전화번호1", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TongjiPhoneB1;

    @MessageField(id = "TongjiPhoneC1", name = "거래명세휴대전화번호1", length = 4, masking = true, maskingType = "03", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TongjiPhoneC1;

    @MessageField(id = "Overdue1", name = "연체통지비트", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String Overdue1;

    @MessageField(id = "FeeDate1", name = "수수료납부년월", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String FeeDate1;

    @MessageField(id = "MoneyPhoneNumA1", name = "환율통지휴대전화번호1", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String MoneyPhoneNumA1;

    @MessageField(id = "MoneyPhoneNumB1", name = "환율통지휴대전화번호1", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String MoneyPhoneNumB1;

    @MessageField(id = "MoneyPhoneNumC1", name = "환율통지휴대전화번호1", length = 4, masking = true, maskingType = "03", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String MoneyPhoneNumC1;

    @MessageField(id = "MengseGB2", name = "거래명세통지구분2", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String MengseGB2;

    @MessageField(id = "DepositZong2", name = "예금종별", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String DepositZong2;

    @MessageField(id = "InOutGB2", name = "출금통지구분2", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String InOutGB2;

    @MessageField(id = "Susuro2", name = "이용수수료", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String Susuro2;

    @MessageField(id = "RegistDay2", name = "등록일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String RegistDay2;

    @MessageField(id = "TongjiPhoneA2", name = "거래명세휴대전화번호2", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TongjiPhoneA2;

    @MessageField(id = "TongjiPhoneB2", name = "거래명세휴대전화번호2", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TongjiPhoneB2;

    @MessageField(id = "TongjiPhoneC2", name = "거래명세휴대전화번호2", length = 4, masking = true, maskingType = "03", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TongjiPhoneC2;

    @MessageField(id = "Overdue2", name = "연체통지비트2", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String Overdue2;

    @MessageField(id = "FeeDate2", name = "수수료납부년월2", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String FeeDate2;

    @MessageField(id = "MoneyPhoneNumA2", name = "환율통지휴대전화번호2", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String MoneyPhoneNumA2;

    @MessageField(id = "MoneyPhoneNumB2", name = "환율통지휴대전화번호2", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String MoneyPhoneNumB2;

    @MessageField(id = "MoneyPhoneNumC2", name = "환율통지휴대전화번호2", length = 4, masking = true, maskingType = "03", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String MoneyPhoneNumC2;

    @MessageField(id = "MengseGB3", name = "거래명세통지구분3", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String MengseGB3;

    @MessageField(id = "DepositZong3", name = "예금종별", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String DepositZong3;

    @MessageField(id = "InOutGB3", name = "출금통지구분3", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String InOutGB3;

    @MessageField(id = "Susuro3", name = "이용수수료", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String Susuro3;

    @MessageField(id = "RegistDay3", name = "등록일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String RegistDay3;

    @MessageField(id = "TongjiPhoneA3", name = "거래명세휴대전화번호3", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TongjiPhoneA3;

    @MessageField(id = "TongjiPhoneB3", name = "거래명세휴대전화번호3", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TongjiPhoneB3;

    @MessageField(id = "TongjiPhoneC3", name = "거래명세휴대전화번호3", length = 4, masking = true, maskingType = "03", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TongjiPhoneC3;

    @MessageField(id = "Overdue3", name = "연체통지비트3", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String Overdue3;

    @MessageField(id = "FeeDate3", name = "수수료납부년월3", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String FeeDate3;

    @MessageField(id = "MoneyPhoneNumA3", name = "환율통지휴대전화번호3", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String MoneyPhoneNumA3;

    @MessageField(id = "MoneyPhoneNumB3", name = "환율통지휴대전화번호3", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String MoneyPhoneNumB3;

    @MessageField(id = "MoneyPhoneNumC3", name = "환율통지휴대전화번호3", length = 4, masking = true, maskingType = "03", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String MoneyPhoneNumC3;

    @MessageField(id = "MengseGB4", name = "거래명세통지구분4", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String MengseGB4;

    @MessageField(id = "DepositZong4", name = "예금종별", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String DepositZong4;

    @MessageField(id = "InOutGB4", name = "출금통지구분4", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String InOutGB4;

    @MessageField(id = "Susuro4", name = "이용수수료", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String Susuro4;

    @MessageField(id = "RegistDay4", name = "등록일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String RegistDay4;

    @MessageField(id = "TongjiPhoneA4", name = "거래명세휴대전화번호4", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TongjiPhoneA4;

    @MessageField(id = "TongjiPhoneB4", name = "거래명세휴대전화번호4", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TongjiPhoneB4;

    @MessageField(id = "TongjiPhoneC4", name = "거래명세휴대전화번호4", length = 4, masking = true, maskingType = "03", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TongjiPhoneC4;

    @MessageField(id = "Overdue4", name = "연체통지비트4", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String Overdue4;

    @MessageField(id = "FeeDate4", name = "수수료납부년월", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String FeeDate4;

    @MessageField(id = "MoneyPhoneNumA4", name = "환율통지휴대전화번호4", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String MoneyPhoneNumA4;

    @MessageField(id = "MoneyPhoneNumB4", name = "환율통지휴대전화번호4", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String MoneyPhoneNumB4;

    @MessageField(id = "MoneyPhoneNumC4", name = "환율통지휴대전화번호4", length = 4, masking = true, maskingType = "03", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String MoneyPhoneNumC4;

    @MessageField(id = "MengseGB5", name = "거래명세통지구분5", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String MengseGB5;

    @MessageField(id = "DepositZong5", name = "예금종별", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String DepositZong5;

    @MessageField(id = "InOutGB5", name = "출금통지구분5", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String InOutGB5;

    @MessageField(id = "Susuro5", name = "이용수수료", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String Susuro5;

    @MessageField(id = "RegistDay5", name = "등록일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String RegistDay5;

    @MessageField(id = "TongjiPhoneA5", name = "거래명세휴대전화번호5", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TongjiPhoneA5;

    @MessageField(id = "TongjiPhoneB5", name = "거래명세휴대전화번호5", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TongjiPhoneB5;

    @MessageField(id = "TongjiPhoneC5", name = "거래명세휴대전화번호5", length = 4, masking = true, maskingType = "03", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TongjiPhoneC5;

    @MessageField(id = "Overdue5", name = "연체통지비트5", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String Overdue5;

    @MessageField(id = "FeeDate5", name = "수수료납부년월5", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String FeeDate5;

    @MessageField(id = "MoneyPhoneNumA5", name = "환율통지휴대전화번호5", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String MoneyPhoneNumA5;

    @MessageField(id = "MoneyPhoneNumB5", name = "환율통지휴대전화번호5", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String MoneyPhoneNumB5;

    @MessageField(id = "MoneyPhoneNumC5", name = "환율통지휴대전화번호5", length = 4, masking = true, maskingType = "03", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String MoneyPhoneNumC5;

    @MessageField(id = "MengseGB6", name = "거래명세통지구분6", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String MengseGB6;

    @MessageField(id = "DepositZong6", name = "예금종별", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String DepositZong6;

    @MessageField(id = "InOutGB6", name = "출금통지구분6", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String InOutGB6;

    @MessageField(id = "Susuro6", name = "이용수수료", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String Susuro6;

    @MessageField(id = "RegistDay6", name = "등록일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String RegistDay6;

    @MessageField(id = "TongjiPhoneA6", name = "거래명세휴대전화번호6", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TongjiPhoneA6;

    @MessageField(id = "TongjiPhoneB6", name = "거래명세휴대전화번호6", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TongjiPhoneB6;

    @MessageField(id = "TongjiPhoneC6", name = "거래명세휴대전화번호6", length = 4, masking = true, maskingType = "03", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TongjiPhoneC6;

    @MessageField(id = "Overdue6", name = "연체통지비트6", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String Overdue6;

    @MessageField(id = "FeeDate6", name = "수수료납부년월6", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String FeeDate6;

    @MessageField(id = "MoneyPhoneNumA6", name = "환율통지휴대전화번호6", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String MoneyPhoneNumA6;

    @MessageField(id = "MoneyPhoneNumB6", name = "환율통지휴대전화번호6", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String MoneyPhoneNumB6;

    @MessageField(id = "MoneyPhoneNumC6", name = "환율통지휴대전화번호6", length = 4, masking = true, maskingType = "03", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String MoneyPhoneNumC6;

    @MessageField(id = "MengseGB7", name = "거래명세통지구분7", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String MengseGB7;

    @MessageField(id = "DepositZong7", name = "예금종별", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String DepositZong7;

    @MessageField(id = "InOutGB7", name = "출금통지구분7", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String InOutGB7;

    @MessageField(id = "Susuro7", name = "이용수수료", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String Susuro7;

    @MessageField(id = "RegistDay7", name = "등록일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String RegistDay7;

    @MessageField(id = "TongjiPhoneA7", name = "거래명세휴대전화번호7", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TongjiPhoneA7;

    @MessageField(id = "TongjiPhoneB7", name = "거래명세휴대전화번호7", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TongjiPhoneB7;

    @MessageField(id = "TongjiPhoneC7", name = "거래명세휴대전화번호7", length = 4, masking = true, maskingType = "03", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TongjiPhoneC7;

    @MessageField(id = "Overdue7", name = "연체통지비트7", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String Overdue7;

    @MessageField(id = "FeeDate7", name = "수수료납부년월7", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String FeeDate7;

    @MessageField(id = "MoneyPhoneNumA7", name = "환율통지휴대전화번호7", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String MoneyPhoneNumA7;

    @MessageField(id = "MoneyPhoneNumB7", name = "환율통지휴대전화번호7", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String MoneyPhoneNumB7;

    @MessageField(id = "MoneyPhoneNumC7", name = "환율통지휴대전화번호7", length = 4, masking = true, maskingType = "03", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String MoneyPhoneNumC7;

}
