package com.scbank.process.api.fw.base.integration.log;

import java.util.List;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.scbank.process.api.fw.base.integration.log.dao.MciLogDao;
import com.scbank.process.api.fw.base.integration.log.dao.dto.MciLogParameter;
import com.scbank.process.api.fw.core.utils.DateUtils;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.session.ISessionContextManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 전문로그 이벤트 DB 적재 리스너 
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class IntegrationLogEventListener {

	/**
	 * MCI 전문로그 적재 DAO 컴포넌트
	 */
	private final MciLogDao mciLogDao;
	
	/**
	 * 프레임워크 세션 컨텍스트 컴포넌트
	 */
	private final ISessionContextManager sessionContextManager;
	
	@Async("logExecutor")
	@EventListener
	public void on(IntegrationLogEvent event) {
		log.debug("# 전문로그: {}", event.toString());
		
		String systemId = event.getSystemId();
		String messageId = event.getMessageId();
		
		if (!List.of("mci", "edmi").contains(systemId)) {
			return;
		}
		
		String txCd = this.getTxCd(systemId, messageId, event.getTxCd());
		
		if (txCd.isEmpty()) {
			log.debug("# MCI/EDMI 전문로그 txCd is Empty");
			return;
		}
		
		String userId = sessionContextManager.getLoginValue("UserID", String.class); //
		String custNo = this.getCusNo(systemId, messageId);
		String tableNm = "NF_MCI_LOG_" + DateUtils.getCurrentDate("yyyyMM");
		
		MciLogParameter parameter = MciLogParameter.builder()
				.chnlGb("SB")
				.trCls(event.getTrCls())
				.custInfo(custNo)
				.txCd(event.getTxCd())
				.errCd(event.getResponseCode())
				.data(event.getData())
				.ipAddress(event.getIpAddress())
				.dynamic(tableNm)
				.userId(userId)
				.build();
		this.mciLogDao.insertMciLog(parameter);
	}
	
	/**
	 * 
	 * @param systemId 시스템ID
	 * @param messageId 전문ID
	 * @param txCd
	 * @return
	 */
	private String getTxCd(String systemId, String messageId, String txCd) {
		if (!"edmi".equals(systemId)) {
			return txCd;
		}
		
		txCd = messageId.replace("_", "");
		if (txCd.indexOf("EDMI") > -1) { 
			txCd = txCd.replace("EDMI", "E_"); 
		} else {
			txCd = "E_" + txCd;	 
		}
		
		if (txCd.length() > 12) {
			txCd = txCd.substring(0, 12);
		}
		
		return txCd;
	}
	
	/**
	 * 고객번호 획득
	 * @param systemId 시스템ID
	 * @param messageId 전문ID
	 * @return
	 */
	private String getCusNo(String systemId, String messageId) {
		String custNo = StringUtils.defaultIfEmpty(sessionContextManager.getLoginValue("CUST_NO", String.class), "");
		String juminNo = StringUtils.defaultIfEmpty(sessionContextManager.getGlobalValue("PerBusNo", String.class), sessionContextManager.getLoginValue("PerBusNo", String.class));
		
		if (StringUtils.isEmpty(juminNo) && "CB_IDENTIFY_MB".equals(messageId)) {
			juminNo = StringUtils.defaultIfEmpty(sessionContextManager.getGlobalValue("BF_DACOM_PERNO", String.class), "");
		}
		
		if (StringUtils.isNotEmpty(juminNo) && StringUtils.isEmpty(custNo)) {
			String perGb = juminNo.substring(6, 7);
			String birthDay = juminNo.substring(0, 6);
			
			if ("1".equals(perGb) || "2".equals(perGb)) {
				custNo = "19" + birthDay;
			} else if ("3".equals(perGb) || "4".equals(perGb)) {
				custNo = "20" + birthDay;
			} else if ("5".equals(perGb) || "6".equals(perGb)) {
				custNo = "19" + birthDay;
			} else if ("7".equals(perGb) || "8".equals(perGb)) {
				custNo = "20" + birthDay;
			} else {
				custNo = birthDay;
			}
		}
		
		return custNo;
	}
}
