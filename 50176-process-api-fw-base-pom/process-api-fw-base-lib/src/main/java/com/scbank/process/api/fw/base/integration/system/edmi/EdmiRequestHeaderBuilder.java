package com.scbank.process.api.fw.base.integration.system.edmi;

import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.scbank.process.api.fw.base.integration.system.edmi.vo.EdmiContTran;
import com.scbank.process.api.fw.base.integration.system.edmi.vo.EdmiCustomInfo;
import com.scbank.process.api.fw.base.integration.system.edmi.vo.EdmiReqHeader;
import com.scbank.process.api.fw.base.integration.system.edmi.vo.EdmiSystemHeader;
import com.scbank.process.api.fw.base.integration.system.edmi.vo.EdmiTranCommonHeader;
import com.scbank.process.api.fw.base.integration.uuid.IntegrationTranNoGenerator;
import com.scbank.process.api.fw.core.utils.DateUtils;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.integration.request.IntegrationRequestHeaderBuilder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class EdmiRequestHeaderBuilder implements IntegrationRequestHeaderBuilder<EdmiReqHeader, EdmiRequestOptions>  {

	/**
	 * 전문 거래번호 생성기
	 */
	private final IntegrationTranNoGenerator integrationTranNoGenerator;
	
	@Override
	public EdmiReqHeader build(Map<String, Object> defaultHeader, EdmiRequestOptions cfg) {
		
		//채널 입/출력 전문 시스템 헤더부 처리
		EdmiSystemHeader systemHeader = cfg.getSystemHeader();
		if (systemHeader == null) {
			systemHeader = new EdmiSystemHeader();
		}
		
		// 채널 입/출력 전문 시스템 헤더부 기본 설정
		try {
			BeanUtils.populate(systemHeader, defaultHeader);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		
		String tmsgWrtgDt = DateUtils.getCurrentDate("yyyyMMdd"); //전문작성일자
		String tmsgWrtgTm = DateUtils.getCurrentDate("HHmmss"); //전문작성시간
		String issSrlNo = String.valueOf(System.currentTimeMillis()).substring(10);
		
		//업무에서 설정한 점번기번
		String tmsgCreSysNm = StringUtils.defaultIfEmpty(cfg.getTmsgCreSysNm(), StringUtils.EMPTY);
		String chnlTypCd = StringUtils.defaultIfEmpty(cfg.getChnlTypCd(), StringUtils.EMPTY);
		if (!StringUtils.hasLength(chnlTypCd)) {
			chnlTypCd = "API";
		}
				
		systemHeader.setTmsgWrtgDt(tmsgWrtgDt);
		systemHeader.setTmsgWrtgTm(tmsgWrtgTm);
		systemHeader.setIssSrlNo(issSrlNo);
		
		if (StringUtils.hasLength(tmsgCreSysNm)) {
			systemHeader.setTmsgCreSysNm(tmsgCreSysNm);
		}
		
		systemHeader.setChnlTypCd(chnlTypCd);
		systemHeader.setTranCd(cfg.getInterfaceId());
		
		//채널 입/출력 전문 거래공통부 처리
		EdmiTranCommonHeader tranCommonHeader = cfg.getTranCommonHeader();
		if (tranCommonHeader == null) {
			tranCommonHeader = new EdmiTranCommonHeader();
		}
		
		String blngBrNo = StringUtils.defaultIfEmpty(tranCommonHeader.getBlngBrNo(), StringUtils.EMPTY); //소속점번호
		String txnBrNo = StringUtils.defaultIfEmpty(tranCommonHeader.getTxnBrNo(), StringUtils.EMPTY); //계좌점번호
		
		// 채널 입/출력 전문 거래공통부 기본 설정
		try {
			BeanUtils.populate(tranCommonHeader, defaultHeader);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		
		if (StringUtils.hasLength(blngBrNo)) {
			tranCommonHeader.setBlngBrNo(blngBrNo);
		}
		
		if (StringUtils.hasLength(txnBrNo)) {
			tranCommonHeader.setTxnBrNo(txnBrNo);
		}
		
		String inputDistCd = StringUtils.defaultIfEmpty(cfg.getInputDistCd(), StringUtils.EMPTY);
		String inputDistCdCancel = StringUtils.defaultIfEmpty(cfg.getInputDistCdCancel(), StringUtils.EMPTY);
		String bizDistCd = StringUtils.defaultIfEmpty(cfg.getBizDistCd(), StringUtils.EMPTY);
		String channelId = StringUtils.defaultIfEmpty(cfg.getChannelId(), StringUtils.EMPTY);
		String macroAi = StringUtils.defaultIfEmpty(cfg.getMacroAi(), StringUtils.EMPTY);
		String macroAo = StringUtils.defaultIfEmpty(cfg.getMacroAo(), StringUtils.EMPTY);
		String oldAcctCd = StringUtils.defaultIfEmpty(cfg.getOldAcctCd(), StringUtils.EMPTY);
		String trxCd = StringUtils.defaultIfEmpty(cfg.getTrxCd(), StringUtils.EMPTY);
		String gsvcd = StringUtils.defaultIfEmpty(cfg.getGsvcd(), StringUtils.EMPTY);
		String gshelf = StringUtils.defaultIfEmpty(cfg.getGshelf(), StringUtils.EMPTY);
		String empNo = StringUtils.defaultIfEmpty(cfg.getEmpNo(), StringUtils.EMPTY);
		String scrnId = StringUtils.defaultIfEmpty(cfg.getScrnId(), StringUtils.EMPTY);
		
		tranCommonHeader.setInputDistCd(inputDistCd);
		tranCommonHeader.setInputDistCdCancel(inputDistCdCancel);
		tranCommonHeader.setBizDistCd(bizDistCd);
		tranCommonHeader.setChannelId(channelId);
		tranCommonHeader.setMacroAi(macroAi);
		tranCommonHeader.setMacroAo(macroAo);
		tranCommonHeader.setOldAcctCd(oldAcctCd);
		tranCommonHeader.setTrxCd(trxCd);
		tranCommonHeader.setGsvcd(gsvcd);
		tranCommonHeader.setGshelf(gshelf);
		tranCommonHeader.setEmpNo(empNo);
		tranCommonHeader.setScrnId(scrnId);
		
		// 전문번호 설정
		String gjmno = this.integrationTranNoGenerator.generateId();
		tranCommonHeader.setGjmno(gjmno);
		
		return EdmiReqHeader.builder()
				.systemHeader(systemHeader)
				.tranCommonHeader(tranCommonHeader)
				.customInfo(new EdmiCustomInfo())
				.contTran(new EdmiContTran())
				.build();
	}
}
