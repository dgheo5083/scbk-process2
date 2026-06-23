package com.scbank.process.api.fw.base.integration.system.edmi.vo;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * 채널 입/출력 전문 거래공통부 VO
 * @author sungdon.choi
 * @version 1.0
 * @since 25. 4. 22.
 */
@Data
@IntegrationMessage(id = "tranCommonHeader", xmlRootName = "TRANCOMMONHEADER")
public class EdmiTranCommonHeader implements IMessageObject {

    private static final long serialVersionUID = 1L;
    
    @MessageField(id = "TMSG_MSG_TYP_CD", name = "전문메시지유형코드", length = 1, defaultValue = "1")
    private String tmsgMsgTypCd;

    @MessageField(id = "BLNG_GRCO_CD", name = "소속그룹사코드", length = 2, defaultValue = "01")
    private String blngGrcoCd;
    
    @MessageField(id = "BLNG_BR_NO", name = "소속점번호", length = 4)
    private String blngBrNo;
    
    @MessageField(id = "EMP_NO", name = "직원번호", length = 7)
    private String empNo;
    
    @MessageField(id = "OFLV_CD", name = "직급코드", length = 3)
    private String oflvCd;
    
    @MessageField(id = "OFDY_CD", name = "직무코드", length = 3)
    private String ofdyCd;
    
    @MessageField(id = "EMP_CD_NO", name = "조작자 카드번호", length = 3)
    private String empCdNo;

    @MessageField(id = "TXN_BR_NO", name = "계좌점번호", length = 4)
    private String txnBrNo;
    
    @MessageField(id = "APV_CD", name = "책임자승인코드", length = 15)
    private String apvCd;
    
    @MessageField(id = "APV_BRNCD_1", name = "1차책임자지점번호", length = 4)
    private String apvBrnCd1;
    
    @MessageField(id = "APV_EMP_NO_1", name = "1차책임자행번", length = 7)
    private String apvEmpNo1;
    
    @MessageField(id = "APV_PASSWD_1", name = "1차책임자비밀번호", length = 8)
    private String apvPasswd1;
    
    @MessageField(id = "APV_BRNCD_2", name = "2차책임자지점번호", length = 4)
    private String apvBrnCd2;
    
    @MessageField(id = "APV_EMP_NO_2", name = "2차책임자행번", length = 7)
    private String apvEmpNo2;
    
    @MessageField(id = "APV_PASSWD_2", name = "2차책임자비밀번호", length = 8)
    private String apvPasswd2;
    
    @MessageField(id = "APV_BRNCD_3", name = "3차책임자지점번호", length = 4)
    private String apvBrnCd3;
    
    @MessageField(id = "APV_EMP_NO_3", name = "3차책임자행번", length = 7)
    private String apvEmpNo3;
    
    @MessageField(id = "APV_PASSWD_3", name = "3차책임자비밀번호", length = 8)
    private String apvPasswd3;
    
    @MessageField(id = "SCRN_ID", name = "화면ID", length = 11)
    private String scrnId;
    
    @MessageField(id = "SUB_SCRN_ID", name = "하위화면ID", length = 10)
    private String subScrnId;
    
    @MessageField(id = "SIMUL_CD", name = "시뮬레이션 거래 여부", length = 1)
    private String simulCd;
    
    @MessageField(id = "PSBK_PRTR_CONN_DV_CD", name = "통장프린터접속구분코드", length = 1)
    private String psbkPrtrConnDvcd;
    
    @MessageField(id = "PSBK_DV_CD", name = "통장구분코드", length = 1, defaultValue = "0")
    private String psbkDvcd;
    
    @MessageField(id = "PSBK_MS_VAL", name = "통장MS값", length = 25)
    private String psbkMsVal;
    
    @MessageField(id = "PSBK_COVER_PAGE", name = "페이지이월", length = 1)
    private String psbkCoverPage;
    
    @MessageField(id = "OUTPUT_LINE_VAL", name = "출력라인", length = 2, defaultValue = "00")
    private String outputLineVal;
    
    @MessageField(id = "CRD_DV_CD", name = "카드구분코드", length = 1)
    private String crdDvcd;
    
    @MessageField(id = "CRD_MDCL_DV_CD", name = "카드사용구분코드", length = 1)
    private String crdMdclDvcd;
    
    @MessageField(id = "PAPER_DV_CD", name = "장표종류코드", length = 1)
    private String paperDvcd;
    
    @MessageField(id = "FUTURE_TRAN_KEY", name = "선일자Key입력", length = 1)
    private String futureTranKey;
    
    @MessageField(id = "CANCEL_KEY", name = "취소Key입력", length = 1)
    private String cancelKey;
    
    @MessageField(id = "PREV_DAY_KEY", name = "전일급Key입력", length = 1)
    private String prevDayKey;
    
    @MessageField(id = "PAST_DATE_KEY", name = "기산일Key입력", length = 1)
    private String pastDateKey;
    
    @MessageField(id = "PRINT_CONT_START", name = "장표계속시작", length = 1)
    private String printContStart;
    
    @MessageField(id = "PRINT_CONT", name = "장표계속", length = 1)
    private String printCont;
    
    @MessageField(id = "MPGB", name = "MP거래구분", length = 1)
    private String mpgb;
    
    @MessageField(id = "MP", name = "MP구분", length = 1)
    private String mp;
    
    @MessageField(id = "TRXCD", name = "OLTP Transaction Code", length = 20)
    private String trxCd;
    
    @MessageField(id = "BIZDISTCD", name = "OLTP 업무식별코드", length = 8)
    private String bizDistCd;
    
    @MessageField(id = "INPUTDISTCD", name = "OLTP 업무식별코드", length = 8)
    private String inputDistCd;
    
    @MessageField(id = "INPUTDISTCD_CANCEL", name = "OLTP 입력식별취소 코드", length = 8)
    private String inputDistCdCancel;
    
    @MessageField(id = "CHANNELID", name = "OLTP 채널 ID", length = 8)
    private String channelId;
    
    @MessageField(id = "OLDACCTCD", name = "OLTP 구과목코드", length = 8)
    private String oldAcctCd;
    
    @MessageField(id = "MACRO_AI", name = "입력 마크로 명", length = 8)
    private String macroAi;
    
    @MessageField(id = "MACRO_AO", name = "출력 마크로 명", length = 8)
    private String macroAo;
    
    @MessageField(id = "GJUNGB", name = "전문구분", length = 4, defaultValue = "0200")
    private String gjunGb;
    
    @MessageField(id = "GJMNO", name = "전문번호", length = 6)
    private String gjmno;
    
    @MessageField(id = "GSVCD", name = "서비스 코드", length = 3)
    private String gsvcd;
    
    @MessageField(id = "GCRGB", name = "처리구분", length = 1, defaultValue = "F")
    private String gcrGb;
    
    @MessageField(id = "GLOCAL", name = "지역구분", length = 2, defaultValue = "P1")
    private String glocal;
    
    @MessageField(id = "GSHELF", name = "SHELF NO/VAN 구분", length = 2, defaultValue = "51")
    private String gshelf;
    
    @MessageField(id = "GCHID", name = "ARS CHANNEL ID", length = 3, defaultValue = "000")
    private String gchid;
    
    @MessageField(id = "GFSID", name = "F/S PID", length = 8, defaultValue = "00000000")
    private String gfsid;
    
    @MessageField(id = "GTO", name = "서비스 코드", length = 1, defaultValue = "W")
    private String gto;
}
