package com.scbank.process.api.fw.base.integration.simulation;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.scbank.process.api.fw.base.integration.constant.IntegrationConstant;
import com.scbank.process.api.fw.base.integration.system.mci.vo.MciMsgInfo;
import com.scbank.process.api.fw.base.integration.system.mci.vo.MciMsgInfo.AdtnMsgInfo;
import com.scbank.process.api.fw.base.integration.system.mci.vo.MciMsgInfo.MsgInfo;
import com.scbank.process.api.fw.base.integration.system.mci.vo.MciMsgInfo.NewErrMsgInfo;
import com.scbank.process.api.fw.base.integration.system.mci.vo.MciContTran;
import com.scbank.process.api.fw.base.integration.system.mci.vo.MciResHeader;
import com.scbank.process.api.fw.base.integration.system.mci.vo.MciSystemHeader;
import com.scbank.process.api.fw.base.integration.system.mci.vo.MciTranCommHeader;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.integration.simulation.IntegrationSimulationHeaderStrategy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCI 시스템 전문 대응답 헤더 생성 전략 구현 클래스
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class MciSimulationHeaderStrategy implements IntegrationSimulationHeaderStrategy<MciResHeader, Void> {

	/**
     * 
     */
    private final XmlMapper objectMapper;
    
    @Override
    public boolean supported(String systemId) {
        return IntegrationConstant.SYSTEM_ID_MCI.equalsIgnoreCase(systemId);
    }

    @Override
    public MciResHeader getHeader(String response) {
    	try {
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode mciCommonNode = rootNode.path("MCICommon");
            JsonNode mciMsgInfoNode = rootNode.path("MCIMsgInfo");
            JsonNode mciContTranNode = rootNode.path("MCIContTran");
            
            MciSystemHeader mciSystemHeader = this.createMCISystemHeader(mciCommonNode);
            MciTranCommHeader mciTranCommHeader = this.createMciTranCommHeader(mciCommonNode);
            MciMsgInfo mciMsgInfo = this.createMCIMsgInfo(mciMsgInfoNode);
            MciContTran mciContTran = this.createMCIContTran(mciContTranNode);
            
            MciResHeader header = new MciResHeader();
            header.setMciSystemHeader(mciSystemHeader);
            header.setMciTranCommHeader(mciTranCommHeader);
            header.setMciMsgInfo(mciMsgInfo);
            header.setMciContTran(mciContTran);
            
            return header;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            MciResHeader header = new MciResHeader();
            header.setMciSystemHeader(new MciSystemHeader());
            header.setMciTranCommHeader(new MciTranCommHeader());
            header.setMciMsgInfo(new MciMsgInfo());
            header.setMciContTran(new MciContTran());
            return header;
        }
    }
    
    private MciSystemHeader createMCISystemHeader(JsonNode node) {
    	MciSystemHeader mciSystemHeader = new MciSystemHeader();
    	mciSystemHeader.setCrypDvCd(node.path("CRYP_DV_CD").asText());
    	mciSystemHeader.setTmsgWrtgDt(node.path("TMSG_WRTG_DT").asText());
    	mciSystemHeader.setTmsgWrtgTm(node.path("TMSG_WRTG_TM").asText());
    	mciSystemHeader.setTrscGrcoCd(node.path("TRSC_GRCO_CD").asText());
    	mciSystemHeader.setTmsgCreSysNm(node.path("TMSG_CRE_SYS_NM").asText());
    	mciSystemHeader.setIssSrlNo(node.path("ISS_SRL_NO").asText());
    	mciSystemHeader.setIpv6Adr(node.path("IPV6_ADR").asText());
    	mciSystemHeader.setInptDlvCd(node.path("INPT_DLV_CD").asText());
    	mciSystemHeader.setEnvrInfoDvCd(node.path("ENVR_INFO_DV_CD").asText());
    	mciSystemHeader.setRqstRspsDvCd(node.path("RQST_RSPS_DV_CD").asText());
    	mciSystemHeader.setTrscSyncDvCd(node.path("TRSC_SYNC_DV_CD").asText());
    	mciSystemHeader.setTranCd(node.path("TRAN_CD").asText());
    	mciSystemHeader.setTmsgRspsDtm(node.path("TMSG_RSPS_DTM").asText());
    	mciSystemHeader.setProcRsltDvcd(node.path("PROC_RSLT_DV_CD").asText());
    	mciSystemHeader.setChnlTypCd(node.path("CHNL_TYP_CD").asText());
    	mciSystemHeader.setMciNdNo(Integer.parseInt(StringUtils.defaultIfEmpty(node.path("MCI_ND_NO").asText(), "0")));
    	mciSystemHeader.setMciSessIdNo(node.path("MCI_SESS_ID_NO").asText());
    	return mciSystemHeader;
    }
    
    private MciTranCommHeader createMciTranCommHeader(JsonNode node) {
    	MciTranCommHeader tranCommHeader = new MciTranCommHeader();
    	tranCommHeader.setTmsgMsgTypCd(node.path("TMSG_MSG_TYP_CD").asText());
    	tranCommHeader.setBlngBrNo(node.path("BLNG_BR_NO").asText());
    	tranCommHeader.setEmpNo(node.path("EMP_NO").asText());
    	tranCommHeader.setTxnBrNo(node.path("TXN_BR_NO").asText());
    	tranCommHeader.setServerId(node.path("SERVER_ID").asText());
    	tranCommHeader.setTermId(node.path("TERM_ID").asText());
    	tranCommHeader.setCancelKey(node.path("CANCEL_KEY").asText());
    	tranCommHeader.setMciSvrDummy01(node.path("MCI_SVR_DUMMY01").asText());
    	
    	return tranCommHeader;
    }
    
    private MciMsgInfo createMCIMsgInfo(JsonNode node) {
    	MciMsgInfo mciMsgInfo = new MciMsgInfo();
    	
    	MsgInfo msgInfo = new MsgInfo();
    	msgInfo.setMsgCd(node.path("MSG_CD").asText());
    	msgInfo.setMsgCtt(node.path("MSG_CTT").asText());
    	
    	AdtnMsgInfo adtnMsgInfo = new AdtnMsgInfo();
    	adtnMsgInfo.setAdtnMsgCd(node.path("ADTN_MSG_CD").asText());
    	adtnMsgInfo.setAdtnMsg(node.path("ADTN_MSG").asText());
    	
    	NewErrMsgInfo newErrMsgInfo = new NewErrMsgInfo();
    	newErrMsgInfo.setNewErrMsgCd(node.path("NEW_ERR_MSG_CD").asText());
    	newErrMsgInfo.setNewErrMsgCtt(node.path("NEW_ERR_MSG_CTT").asText());
    	
    	mciMsgInfo.setMsgInfo(msgInfo);
    	mciMsgInfo.setAdtnMsgInfo(adtnMsgInfo);
    	mciMsgInfo.setNewErrMsgInfo(newErrMsgInfo);
    	return mciMsgInfo;
    }
    
    private MciContTran createMCIContTran(JsonNode node) {
    	MciContTran mciContTran = new MciContTran();
    	mciContTran.setContDataCd(node.path("CONT_DATA_CD").asText());
    	mciContTran.setContDataLen(node.path("CONT_DATA_LEN").asText());
    	mciContTran.setContData(node.path("CONT_DATA").asText());
    	
    	return mciContTran;
    }

    @Override
    public Void getErrorMsg(String response) {
        return null;
    }
}
