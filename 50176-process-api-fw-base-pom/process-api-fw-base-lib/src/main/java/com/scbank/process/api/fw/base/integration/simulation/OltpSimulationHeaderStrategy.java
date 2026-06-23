package com.scbank.process.api.fw.base.integration.simulation;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.scbank.process.api.fw.base.integration.constant.IntegrationConstant;
import com.scbank.process.api.fw.base.integration.system.oltp.vo.OltpCommon;
import com.scbank.process.api.fw.base.integration.system.oltp.vo.OltpError;
import com.scbank.process.api.fw.base.integration.system.oltp.vo.OltpResHeader;
import com.scbank.process.api.fw.integration.simulation.IntegrationSimulationHeaderStrategy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 호스트 시스템 대응답 헤더 생성 구현 클래스
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class OltpSimulationHeaderStrategy implements IntegrationSimulationHeaderStrategy<OltpResHeader, OltpError> {

    /**
     * 
     */
    private final XmlMapper objectMapper;

    @Override
    public boolean supported(String systemId) {
        return IntegrationConstant.SYSTEM_ID_HOST.equalsIgnoreCase(systemId);
    }

    @Override
    public OltpResHeader getHeader(String response) {
        try {
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode responseNode = rootNode.path("ScfbHeader");
            OltpCommon hostCommon = this.createHostCommon(responseNode);
            
            OltpResHeader header = new OltpResHeader();
            header.setOltpCommon(hostCommon);
            
            return header;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            OltpResHeader header = new OltpResHeader();
            header.setOltpCommon(new OltpCommon());
            return header;
        }
    }
    
    private OltpCommon createHostCommon(JsonNode node) {
    	OltpCommon hostCommon = new OltpCommon();
    	hostCommon.setImsTranCd(node.path("IMSTRANCD").asText());
    	hostCommon.setBlk(node.path("BLK").asText());
    	hostCommon.setInChannel(node.path("INCHANNEL").asText());
    	hostCommon.setBranch(node.path("BRANCH").asText());
    	hostCommon.setLotNo(node.path("LOTNO").asText());
    	hostCommon.setTcNo(node.path("TCNO").asText());
    	hostCommon.setInterType(node.path("INTERTYPE").asText());
    	hostCommon.setInterNo(node.path("INTERNO").asText());
    	hostCommon.setCounterInfo1(node.path("COUNTERINFO1").asText());
    	hostCommon.setCounterInfo2(node.path("COUNTERINFO2").asText());
    	hostCommon.setBranchNo(node.path("BRANCHNO").asText());
    	hostCommon.setConvterscr(node.path("CONVTERSCR").asText());
    	hostCommon.setRes(node.path("RES").asText());
    	hostCommon.setMsgInfoBlk(node.path("MSGINFOBLK").asText());
    	hostCommon.setModeInfo(node.path("MODEINFO").asText());
    	hostCommon.setDesTp(node.path("DESTP").asText());
    	hostCommon.setJobTp(node.path("JOBTP").asText());
    	hostCommon.setInClassCd(node.path("INCLASSCD").asText());
    	hostCommon.setCtrlCdNo(node.path("CTRLCDNO").asText());
    	hostCommon.setResCdNo(node.path("RESCDNO").asText());
    	hostCommon.setStFlag1(node.path("STFLAG1").asText());
    	hostCommon.setStFlag2(node.path("STFLAG2").asText());
    	hostCommon.setStFlag3(node.path("STFLAG3").asText());
    	hostCommon.setStFlag4(node.path("STFLAG4").asText());
    	hostCommon.setInline(node.path("INLINE").asText());
    	hostCommon.setPfKey(node.path("PFKEY").asText());
    	
    	hostCommon.setRes1(node.path("RES1").asText());
    	hostCommon.setBranchSav(node.path("BRANCHSAV").asText());
    	hostCommon.setDealSav(node.path("DEALSAV").asText());
    	hostCommon.setInterSav(node.path("INTERSAV").asText());
    	hostCommon.setNewFormAttp(node.path("NEWFORMATTP").asText());
    	hostCommon.setOutErrCd(node.path("OUTERRCD").asText());
    	hostCommon.setOutLu(node.path("OUTLU").asText());
    	hostCommon.setSourceOutLu(node.path("SOURCEOUTLU").asText());
    	hostCommon.setIniJobCla(node.path("INIJOBCLA").asText());
    	hostCommon.setFbtProcessTranNo(node.path("FBTPROCESSTRNSNO").asText());
    	hostCommon.setRes2(node.path("RES2").asText());
    	
    	hostCommon.setTxCd(node.path("TRSTP").asText());
    	hostCommon.setTrCd(node.path("TRSNO").asText());
    	hostCommon.setSvcCd(node.path("SVCCD").asText());
    	hostCommon.setProcessTp(node.path("PROCESSTP").asText());
    	hostCommon.setLocalTp(node.path("LOCALTP").asText());
    	hostCommon.setVanTp(node.path("VANTP").asText());
    	hostCommon.setVapcnlNo(node.path("VAPCNLNO").asText());
    	hostCommon.setFspId(node.path("FSPID").asText());
    	hostCommon.setInOutCnlTp(node.path("INOUTCNLTP").asText());
    	
    	hostCommon.setSendTime(node.path("SENDTIME").asText());
    	hostCommon.setSendSystem(node.path("SENDSYSTEM").asText());
    	hostCommon.setErrorCode(node.path("ERRORCODE").asText());
    	hostCommon.setResponseCode(node.path("RESPONSECODE").asText());
    	hostCommon.setDisplayType(node.path("DISPLAYTYPE").asText());
    	hostCommon.setErrorLocation(node.path("ERRORLOCATION").asText());
    	hostCommon.setFiller(node.path("FILLER").asText());
    	
    	return hostCommon;
    }

	@Override
	public OltpError getErrorMsg(String response) {
		try {
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode responseNode = rootNode.path("HostErrorMsg");
            OltpError hostError = objectMapper.treeToValue(responseNode, OltpError.class);
            return hostError;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
	}
}
