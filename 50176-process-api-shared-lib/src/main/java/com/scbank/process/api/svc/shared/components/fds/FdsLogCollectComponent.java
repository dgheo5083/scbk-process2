package com.scbank.process.api.svc.shared.components.fds;

import java.util.Map;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

import com.scbank.process.api.fw.base.integration.log.IntegrationLogCollectEvent;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpManager;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpRequest;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpRequestOptions;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpResponse;
import com.scbank.process.api.fw.base.integration.system.oltp.vo.OltpCommon;
import com.scbank.process.api.fw.base.integration.system.oltp.vo.OltpReqHeader;
import com.scbank.process.api.fw.base.integration.system.oltp.vo.OltpResHeader;
import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.SharedComponent;
import com.scbank.process.api.fw.core.utils.DateUtils;
import com.scbank.process.api.fw.core.utils.IpUtils;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.shared.components.fds.dto.FdsLogData;
import com.scbank.process.api.svc.shared.components.ipinside.IpinsideComponent;
import com.scbank.process.api.svc.shared.utils.PRCSharedUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * FDS SR 로그 수집 컴포넌트
 */
@Slf4j
@RequiredArgsConstructor
@SharedComponent(name = "FDS SR 로그 수집 컴포넌트")
public class FdsLogCollectComponent {

	/**
	 * Ipinside 컴포넌트
	 */
	private final IpinsideComponent ipinsideComponent;
	
	/**
	 * OLTP 전문 송/수신 매니저
	 */
    private final OltpManager oltpManager;
    
    /**
     * 세션 매니저 컴포넌트
     */
    private final ISessionContextManager sessionContextManager;
    
    /**
     * 전문로그 수집 이벤트 리스너
     * @param event
     */
    @SuppressWarnings("rawtypes")
	@Async("logExecutor")
	@EventListener
    public void onLogCollectEvent(IntegrationLogCollectEvent event) {
    	if (event == null) {
    		return;
    	}
    	
    	String systemId = event.getSystemId();
    	if (!"host".equals(systemId)) {
    		return;
    	}
    	
    	String userId = event.getUserId();
    	String txDscd = event.getTxDscd(); //송수신 구분코드
    	String deviceKey = event.getDeviceKey(); //FDS 디바이스키
    	//************************************
    	//요청
    	//************************************
    	if ("S".equals(txDscd)) {
    		Object request = event.getRequest();
        	if (request == null) {
        		return;
        	}
        	
        	//호스트 전문 요청만 처리한다.
        	if (!(request instanceof OltpRequest)) {
        		return;
        	}
        	
        	OltpRequest req = (OltpRequest)request;
        	String fdsLogData = this.createFdsRequestLogData(event.getChannelType(), req, event.getData(), userId, deviceKey);
    		
    		log.debug("# onLogCollectEvent vFdsDeviceKey={}, data={}", event.getDeviceKey(), fdsLogData);
    		
    		ipinsideComponent.sendFdsLog(event.getDeviceKey(), fdsLogData); 
    	//************************************
    	//응답
    	//************************************
    	} else {
    		Object response = event.getResponse();
        	if (response == null) {
        		return;
        	}
        	
        	if (!(response instanceof OltpResponse)) {
        		return;
        	}
        	
        	OltpResponse res = (OltpResponse)response;
        	String vFdsDeviceKey = sessionContextManager.getGlobalValue("VFDSDeviceKey", String.class);
    		String fdsLogData = this.createFdsResponseLogData(event.getChannelType(), res, event.getData(), userId, deviceKey);
    		
    		log.debug("# onLogCollectEvent vFdsDeviceKey={}, data={}", vFdsDeviceKey, fdsLogData);
    		
    		ipinsideComponent.sendFdsLog(vFdsDeviceKey, fdsLogData); 
    	}
    }
	
	/**
	 * 오픈뱅킹용 FDS 로그 수집 처리
	 * @param <I>
	 * @param requestOptions
	 * @param input
	 */
	@ComponentOperation(name = "오픈뱅킹용 OLTP FDS SR 로그 수집 처리", author = "sungdon.choi")
	public <I extends IMessageObject> void sendOpenBankingFdsLog(OltpRequestOptions requestOptions, I input) {
		this.sendOpenBankingFdsLog("Z", requestOptions, input);
	}
	
	/**
	 * 오픈뱅킹용 FDS 로그 수집 처리
	 * @param <I>
	 * @param channelType
	 * @param requestOptions
	 * @param input
	 */
	@SuppressWarnings("unchecked")
	@ComponentOperation(name = "오픈뱅킹용 OLTP FDS SR 로그 수집 처리", author = "sungdon.choi")
	public <I extends IMessageObject> void sendOpenBankingFdsLog(String channelType, OltpRequestOptions requestOptions, I input) {
		Map<String, Object> result = oltpManager.createRequestBytes(requestOptions, input);
		
		OltpRequest<I> request = (OltpRequest<I>) result.get("request");
		String data = (String)result.get("data");
		
		String vFdsDeviceKey = sessionContextManager.getGlobalValue("VFDSDeviceKey", String.class);
		String userId = this.getUserId();
		String fdsLogData = this.createFdsRequestLogData(channelType, request, data, userId, vFdsDeviceKey);
		
		log.debug("# sendOpenBankingFdsLog vFdsDeviceKey={}, data={}", vFdsDeviceKey, fdsLogData);
		//TODO FDS 로그 송/수신 처리
		ipinsideComponent.sendFdsLog(vFdsDeviceKey, fdsLogData);
	}
	
	/**
	 * FDS 전문 로그 수집 데이터를 생성한다.
	 * @param channelType 채널타입
	 * @param req 요청객체
	 * @param data 요청전문 고정길이로 직렬화한 문자열 데이터
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private String createFdsRequestLogData(String channelType, OltpRequest req, String data, String userId, String deviceKey) {
		OltpReqHeader header = req.getHeader();
		OltpCommon oltpCommon = header.getOltpCommon();
		
		String curDate = DateUtils.getCurrentDate("yyyyMMdd");
		String curTime = DateUtils.getCurrentDate("HHmmss");
		String wasNum = "1";
		String txDscd = "RCV"; //메시지허브와 동일하게 RCV로 설정
		String trCd = StringUtils.defaultIfEmpty(oltpCommon.getTrCd(), StringUtils.ZERO);
		String egmsTranNo = "0";
		String userIp = IpUtils.getClientIp();
		String imsTranCd = oltpCommon.getImsTranCd();
		String inClassCd = oltpCommon.getInClassCd();
		String jobCd = oltpCommon.getJobTp();
		String svcCd = oltpCommon.getSvcCd();
		String appVersion = PRCSharedUtils.getAppVersion();
		
		FdsLogData logData = FdsLogData.builder()
			.curDate(curDate)
			.curTime(curTime)
			.channelType(channelType)
			.wasNum(wasNum)
			.txDscd(txDscd)
			.wasTrnaNo(Integer.parseInt(trCd))
			.egmsTranNo(Integer.parseInt(egmsTranNo))
			.userId(userId)
			.userIp(userIp)
			.imsTranCd(imsTranCd)
			.inClassCd(inClassCd)
			.jobCd(jobCd)
			.svcCd(svcCd)
			.data(data)
			.deviceKey(deviceKey)
			.appVersion(appVersion)
			.build();
		return logData.toString();
	}
	
	/**
	 * FDS 전문 로그 수집 데이터를 생성한다.
	 * @param channelType 채널타입
	 * @param req 요청객체
	 * @param data 요청전문 고정길이로 직렬화한 문자열 데이터
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private String createFdsResponseLogData(String channelType, OltpResponse res, String data, String userId, String deviceKey) {
		OltpResHeader header = res.getHeader();
		OltpCommon oltpCommon = header.getOltpCommon();
		
		String curDate = DateUtils.getCurrentDate("yyyyMMdd");
		String curTime = DateUtils.getCurrentDate("HHmmss");
		String wasNum = "1";
		String txDscd = "SND";
		String trCd = StringUtils.defaultIfEmpty(oltpCommon.getTrCd(), StringUtils.ZERO);
		String egmsTranNo = "0";
		String userIp = IpUtils.getClientIp();
		String imsTranCd = oltpCommon.getImsTranCd();
		String inClassCd = oltpCommon.getInClassCd();
		String jobCd = oltpCommon.getJobTp();
		String svcCd = oltpCommon.getSvcCd();
		String appVersion = PRCSharedUtils.getAppVersion();
		
		FdsLogData logData = FdsLogData.builder()
			.curDate(curDate)
			.curTime(curTime)
			.channelType(channelType)
			.wasNum(wasNum)
			.txDscd(txDscd)
			.wasTrnaNo(Integer.parseInt(trCd))
			.egmsTranNo(Integer.parseInt(egmsTranNo))
			.userId(userId)
			.userIp(userIp)
			.imsTranCd(imsTranCd)
			.inClassCd(inClassCd)
			.jobCd(jobCd)
			.svcCd(svcCd)
			.data(data)
			.deviceKey(deviceKey)
			.appVersion(appVersion)
			.build();
		return logData.toString();
	}
	
	/**
	 * 사용자ID를 획득한다.
	 * @return
	 */
	private String getUserId() {
		String userId = StringUtils.EMPTY;
		if (this.sessionContextManager.isLogin()) {
			userId = this.sessionContextManager.getLoginValue("UserID", String.class);
		}
		return userId;
	}
}
