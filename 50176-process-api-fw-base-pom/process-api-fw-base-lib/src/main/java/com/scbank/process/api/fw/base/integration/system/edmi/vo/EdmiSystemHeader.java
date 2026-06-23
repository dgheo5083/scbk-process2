package com.scbank.process.api.fw.base.integration.system.edmi.vo;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * 시스템헤더 VO
 * @author sungdon.choi
 * @version 1.0
 * @since 25. 4. 22.
 */
@Data
public class EdmiSystemHeader implements IMessageObject {

    private static final long serialVersionUID = 1L;

    /**
     * 암호화 구분
     */
	@MessageField(id = "CRYP_DV_CD", length = 1, defaultValue = "0")
    private String crypDvCd;

	/**
	 * 전문작성일자
	 */
    @MessageField(id = "TMSG_WRTG_DT", length = 8)
    private String tmsgWrtgDt;

    /**
     * 전문작성시간
     */
    @MessageField(id = "TMSG_WRTG_TM", length = 6)
    private String tmsgWrtgTm;

    /**
     * 거래그룹사코드(통합단말에서 거래를 하기 위해 부여된 거래그룹사코드)
     */
    @MessageField(id = "TRSC_GRCO_CD", length = 2, defaultValue = "01")
    private String trscGrcoCd;

    /**
     * 점번기번
     */
    @MessageField(id = "TMSG_CRE_SYS_NM", length = 8)
    private String tmsgCreSysNm;

    /**
     * 발권통번 (전문발생시마다 채번)
     */
    @MessageField(id = "ISS_SRL_NO", length = 3)
    private String issSrlNo;

    /**
     * IPV6주소
     */
    @MessageField(id = "IPV6_ADR", length = 16)
    private String ipv6Adr;

    /**
     * 입력매체코드
     */
    @MessageField(id = "INPT_DLV_CD", length = 1)
    private String inptDlvCd;

    /**
     * 환경정보구분코드
     */
    @MessageField(id = "ENVR_INFO_DV_CD", length = 1)
    private String envrInfoDvCd;

    /**
     * 요청응답구분코드
     */
    @MessageField(id = "RQST_RSPS_DV_CD", length = 1, defaultValue = "Q")
    private String rqstRspsDvCd;

    /**
     * 거래동기화구분코드
     */
    @MessageField(id = "TRSC_SYNC_DV_CD", length = 1, defaultValue = "S")
    private String trscSyncDvCd;

    /**
     * 입력거래코드
     */
    @MessageField(id = "TRAN_CD", length = 12)
    private String tranCd;

    /**
     * 전문응답일시
     */
    @MessageField(id = "TMSG_RSPS_DTM", length = 16)
    private String tmsgRspsDtm;

    /**
     * 처리결과구분코드
     */
    @MessageField(id = "PROC_RSLT_DV_CD", length = 1)
    private String procRsltDvcd;

    /**
     * 채널유형코드
     */
    @MessageField(id = "CHNL_TYP_CD", length = 3)
    private String chnlTypCd;

    /**
     * MCI노드번호
     */
    @MessageField(id = "MCI_ND_NO", length = 2, defaultValue = "0")
    private String mciNdNo;

    /**
     * MCI세션ID번호
     */
    @MessageField(id = "MCI_SESS_ID_NO", length = 8)
    private String mciSessIdNo;
}
