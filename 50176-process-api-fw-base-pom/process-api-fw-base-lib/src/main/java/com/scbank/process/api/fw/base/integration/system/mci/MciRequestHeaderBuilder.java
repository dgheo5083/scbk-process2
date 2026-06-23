package com.scbank.process.api.fw.base.integration.system.mci;

import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.scbank.process.api.fw.base.integration.system.mci.vo.MciContTran;
import com.scbank.process.api.fw.base.integration.system.mci.vo.MciReqHeader;
import com.scbank.process.api.fw.base.integration.system.mci.vo.MciSystemHeader;
import com.scbank.process.api.fw.base.integration.system.mci.vo.MciTranCommHeader;
import com.scbank.process.api.fw.core.error.FrameworkErrorCode;
import com.scbank.process.api.fw.core.utils.DateUtils;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.integration.exception.IntegrationException;
import com.scbank.process.api.fw.integration.request.IntegrationRequestHeaderBuilder;

import lombok.extern.slf4j.Slf4j;

/**
 * MCI 요청 헤더 빌더 구현 클래스
 * 
 * @author sungdon.choi
 * @version 1.0
 * @since 25. 4. 18.
 */
@Slf4j
public class MciRequestHeaderBuilder implements IntegrationRequestHeaderBuilder<MciReqHeader, MciRequestOptions> {

	@Override
	public MciReqHeader build(Map<String, Object> defaultHeader, MciRequestOptions cfg) {
		if (cfg == null) {
			throw new IntegrationException(
					FrameworkErrorCode.INTERNAL_ERROR.getCode(), 
					"MCI 요청설정객체를 확인하세요.");
		}
		
		MciSystemHeader systemHeader = cfg.getMciSystemHeader();
		if (systemHeader == null) {
			systemHeader = new MciSystemHeader();
		}

		// 채널 입/출력 전문 시스템 헤더부 기본 설정
		if (defaultHeader != null) {
			try {
				BeanUtils.populate(systemHeader, defaultHeader);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}

		String tmsgWrtgDt = DateUtils.getCurrentDate("yyyyMMdd"); // 전문작성일자
		String tmsgWrtgTm = DateUtils.getCurrentDate("HHmmss"); // 전문작성시간
		String issSrlNo = String.valueOf(System.currentTimeMillis()).substring(10);

		// 업무에서 설정한 점번기번
		String tmsgCreSysNm = StringUtils.defaultIfEmpty(cfg.getTmsgCreSysNm(), StringUtils.EMPTY);
		String chnlTypCd = StringUtils.defaultIfEmpty(cfg.getChnlTypCd(), StringUtils.EMPTY);
		if (!StringUtils.hasLength(chnlTypCd)) {
			chnlTypCd = "HUB";
		}
		
		systemHeader.setTmsgWrtgDt(tmsgWrtgDt);
		systemHeader.setTmsgWrtgTm(tmsgWrtgTm);
		systemHeader.setIssSrlNo(issSrlNo);
		
		if (StringUtils.hasLength(tmsgCreSysNm)) {
			systemHeader.setTmsgCreSysNm(tmsgCreSysNm);
		}
		
		systemHeader.setChnlTypCd(chnlTypCd);
		systemHeader.setTranCd(cfg.getTranCd());
		
		MciTranCommHeader tranCommonHeader = cfg.getMciTranCommHeader();
		if (tranCommonHeader == null) {
			tranCommonHeader = new MciTranCommHeader();
		}
		
		String blngBrNo = StringUtils.defaultIfEmpty(cfg.getBlngBrNo(), StringUtils.EMPTY); //소속점번호
		String txnBrNo = StringUtils.defaultIfEmpty(cfg.getTxnBrNo(), StringUtils.EMPTY); //계좌점번호
		
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
		
		String empNo = StringUtils.defaultIfEmpty(cfg.getEmpNo(), StringUtils.EMPTY);
		tranCommonHeader.setEmpNo(empNo);
		
		MciContTran mciContTran = cfg.getMciContTran();
		if (mciContTran == null) {
			mciContTran = new MciContTran();
			mciContTran.setContData("");
			mciContTran.setContDataCd("");
			mciContTran.setContDataLen("");
		}

		return MciReqHeader.builder()
				.mciSystemHeader(systemHeader)
				.mciTranCommHeader(tranCommonHeader)
				.mciContTran(mciContTran)
				.build();
	}
}