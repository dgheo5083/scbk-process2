package com.scbank.process.api.fw.base.integration.system.oltp.vo;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Data;

@Data
public class OltpCommon implements IMessageObject {

	private static final long serialVersionUID = 1L;

	// CAP HEADER (50)bytes
	@MessageField(id = "IMSTRANCD", name = "IMS TRAN CODE", length = 8)
	private String imsTranCd;

	@MessageField(id = "BLK", name = "BLANK", length = 1)
	private String blk;

	@MessageField(id = "INCHANNEL", name = "입력매체", length = 1, defaultValue = "G")
	private String inChannel;

	@MessageField(id = "BRANCH", name = "취급점", length = 4, defaultValue = "0996")
	private String branch;

	@MessageField(id = "LOTNO", name = "지번", length = 3, defaultValue = "000")
	private String lotNo;

	@MessageField(id = "TCNO", name = "T/C번호", length = 1, defaultValue = "6")
	private String tcNo;

	@MessageField(id = "INTERTYPE", length = 1, defaultValue = "0")
	private String interType;

	@MessageField(id = "INTERNO", length = 1, defaultValue = "0")
	private String interNo;

	@MessageField(id = "COUNTERINFO1", name = "COUNTER 정보부 1", length = 1)
	private String counterInfo1;

	@MessageField(id = "COUNTERINFO2", name = "COUNTER 정보부 2", length = 1)
	private String counterInfo2;

	@MessageField(id = "BRANCHNO", name = "대상점 번호", length = 3)
	private String branchNo;

	@MessageField(id = "CONVTERSCR", name = "convterscr", length = 4)
	private String convterscr;

	@MessageField(id = "RES", name = "res", length = 1)
	private String res;

	@MessageField(id = "MSGINFOBLK", name = "msgInfoBlk", length = 1)
	private String msgInfoBlk;

	@MessageField(id = "MODEINFO", name = "modeInfo", length = 1)
	private String modeInfo;

	@MessageField(id = "DESTP", name = "desTp", length = 1)
	private String desTp;

	@MessageField(id = "JOBTP", name = "업무 식별 코드", length = 2, defaultValue = "GP")
	private String jobTp;

	@MessageField(id = "INCLASSCD", name = "입력 식별 코드", length = 4)
	private String inClassCd;

	@MessageField(id = "CTRLCDNO", name = "조작자 식별 코드", length = 2)
	private String ctrlCdNo;

	@MessageField(id = "RESCDNO", name = "책임자 카드 번호", length = 2)
	private String resCdNo;

	@MessageField(id = "STFLAG1", name = "STFLAG1", length = 1, defaultValue = "0x80")
	private String stFlag1;

	@MessageField(id = "STFLAG2", name = "STFLAG2", length = 1, defaultValue = "0x80")
	private String stFlag2;

	@MessageField(id = "STFLAG3", name = "STFLAG3", length = 1, defaultValue = "0x80")
	private String stFlag3;

	@MessageField(id = "STFLAG4", name = "STFLAG4", length = 1, defaultValue = "0x80")
	private String stFlag4;

	@MessageField(id = "INLINE", name = "INLINE", length = 2)
	private String inline;

	@MessageField(id = "PFKEY", name = "pfKey", length = 1)
	private String pfKey;

	// 대외 공통부(50)bytes
	@MessageField(id = "RES1", name = "예비", length = 5)
	private String res1;

	@MessageField(id = "BRANCHSAV", name = "점통번 보관", length = 4)
	private String branchSav;

	@MessageField(id = "DEALSAV", name = "취급점 보관", length = 3)
	private String dealSav;

	@MessageField(id = "INTERSAV", name = "입력단말보관", length = 8)
	private String interSav;

	@MessageField(id = "NEWFORMATTP", name = "신FORMAT구분", length = 3, defaultValue = "NEW")
	private String newFormAttp;

	@MessageField(id = "OUTERRCD", name = "출력에러코드", length = 4)
	private String outErrCd;

	@MessageField(id = "OUTLU", name = "출력LU", length = 2, defaultValue = "E1")
	private String outLu;

	@MessageField(id = "SOURCEOUTLU", name = "원거래출력LU", length = 2)
	private String sourceOutLu;

	@MessageField(id = "INIJOBCLA", name = "초기 업무식별", length = 2)
	private String iniJobCla;

	@MessageField(id = "INIINCLA", name = "초기 입력식별", length = 4)
	private String iniInCla;

	@MessageField(id = "FBTPROCESSTRNSNO", name = "FBT처리통번", length = 1)
	private String fbtProcessTranNo;

	@MessageField(id = "RES2", name = "예비2", length = 12)
	private String res2;

	// 대외계 서버연락부(30)bytes
	@MessageField(id = "TRSTP", name = "전문구분 (0200요청, 0210:응답, 0201:미완료)", length = 4, defaultValue = "0200")
	private String txCd;

	@MessageField(id = "TRSNO", name = "전문번호 해당일SEQ (000001-999999)", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String trCd;

	@MessageField(id = "SVCCD", name = "서비스 코드", length = 3)
	private String svcCd;

	@MessageField(id = "PROCESSTP", name = "처리구분 F:최초거래,N:연속,L:마지막", length = 1, defaultValue = "F")
	private String processTp;

	@MessageField(id = "LOCALTP", name = "지역구분", length = 2, defaultValue = "P1")
	private String localTp;

	@MessageField(id = "VANTP", name = "sheft no/van구분", length = 2, defaultValue = "51")
	private String vanTp;

	@MessageField(id = "VAPCNLNO", name = "VAN체널번호", length = 3, defaultValue = "000")
	private String vapcnlNo;

	@MessageField(id = "FSPID", name = "FILE SERVER PROCESS ID", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO, defaultValue = "00000000")
	private String fspId;

	@MessageField(id = "INOUTCNLTP", name = "입출력매체구분", length = 1, defaultValue = "W")
	private String inOutCnlTp;

	// 업무연락부 (30)bytes - 구 대외 APPL공통부
	@MessageField(id = "SENDDATE", name = "전문전송일", length = 8)
	private String sendDate;

	@MessageField(id = "SENDTIME", name = "전문전송시간", length = 6)
	private String sendTime;

	@MessageField(id = "SENDSYSTEM", name = "전문전송처", length = 1, defaultValue = "P")
	private String sendSystem;

	@MessageField(id = "ERRORCODE", name = "응답코드", length = 4, defaultValue = "0000")
	private String errorCode;

	@MessageField(id = "RESPONSECODE", name = "RESPONSECODE", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String responseCode;

	@MessageField(id = "DISPLAYTYPE", name = "출력화면구분", length = 1)
	private String displayType;

	@MessageField(id = "ERRORLOCATION", name = "입력전문에러검증시 에러발생위치", length = 2, defaultValue = "00")
	private String errorLocation;

	@MessageField(id = "FILLER", name = "예비", length = 4)
	private String filler;
}
