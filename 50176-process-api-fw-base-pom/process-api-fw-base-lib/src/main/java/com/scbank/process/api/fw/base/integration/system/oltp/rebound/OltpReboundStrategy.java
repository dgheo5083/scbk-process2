package com.scbank.process.api.fw.base.integration.system.oltp.rebound;

import com.scbank.process.api.fw.base.integration.constant.IntegrationConstant;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpRequest;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpResponse;
import com.scbank.process.api.fw.base.integration.system.oltp.vo.OltpCommon;
import com.scbank.process.api.fw.base.integration.system.oltp.vo.OltpReqHeader;
import com.scbank.process.api.fw.base.integration.system.oltp.vo.OltpResHeader;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.core.utils.ReflectionUtils;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.integration.context.IntegrationContext;
import com.scbank.process.api.fw.integration.rebound.AbstractIntegrationReboundStrategy;
import com.scbank.process.api.fw.message.IMessageObject;

/**
 * 
 */
public class OltpReboundStrategy extends
		AbstractIntegrationReboundStrategy<OltpRequest<? extends IMessageObject>, OltpResponse<? extends IMessageObject>> {

	/**
	 * 호스트 시스템 리바운드 거래 최대 허용 Loop 건수(기본값 : 10)
	 */
	private int maxLoopCnt;

	/**
	 * 호스트 시스템 리바운드 거래 응답 반복부 필드명(기본값: REC_01)
	 */
	private String listFieldName;

	public OltpReboundStrategy() {
		this.init();
	}

	protected void init() {
		this.maxLoopCnt = RuntimeContext.getProperty(IntegrationConstant.PROPS_HOST_MAX_LOOP_CNT,
				Integer.class, IntegrationConstant.DEFAULT_HOST_MAX_LOOP_CNT);
		this.listFieldName = RuntimeContext
				.getProperty(IntegrationConstant.PROPS_HOST_LIST_FIELD_NAME,
						IntegrationConstant.DEFAULT_HOST_LIST_FIELD_NAME);
	}

	@Override
	public boolean isContinue(IntegrationContext context, OltpResponse<? extends IMessageObject> response) {
		OltpResHeader hostHeader = response.getHeader();
		OltpCommon hostCommon = this.getHostCommon(hostHeader);
		String processTp = this.getProcessTp(hostCommon);

		if ("N".equals(processTp)) {
			return true;
		}

		if ("L".equals(processTp)) {
			return false;
		}
		return false;
	}

	@Override
	public OltpRequest<? extends IMessageObject> handleData(OltpRequest<? extends IMessageObject> request,
			OltpResponse<? extends IMessageObject> response) {

		OltpResHeader hostHeader = response.getHeader();
		IMessageObject responseBody = response.getResponse();
		OltpCommon hostCommon = this.getHostCommon(hostHeader);

		String processTp = this.getProcessTp(hostCommon); // 처리구분
		String continueInfo = this.getContinueInfo(responseBody); // 연속거래키

		OltpReqHeader requestHeader = request.getHeader();
		IMessageObject requestBody = request.getRequestMessage();
		OltpCommon hostReqCommon = requestHeader.getOltpCommon();

		hostReqCommon.setProcessTp(processTp);
		ReflectionUtils.setFieldValue(requestBody, "ContinueInfo", continueInfo);

		return request;
	}

	@Override
	public int getMaxLoopCnt() {
		return this.maxLoopCnt;
	}

	@Override
	public String getListFieldName() {
		return this.listFieldName;
	}

	/**
	 * 응답 전문 호스트 공통부를 획득한다.
	 * 
	 * @param response 응답전문 헤더부
	 * @return
	 */
	protected OltpCommon getHostCommon(OltpResHeader response) {
		if (response == null) {
			return new OltpCommon();
		}
		return response.getOltpCommon();
	}

	/**
	 * 응답전문 호스트 공통부 처리구분 (F:최초거래,N:연속,L:마지막) 문자열을 획득한다.
	 * 
	 * @param hostCommon 응답전문 호스트 공통부 처리구분 1자리
	 * @return
	 */
	protected String getProcessTp(OltpCommon hostCommon) {
		String processTp = StringUtils.defaultIfEmpty(hostCommon.getProcessTp(), StringUtils.EMPTY);
		return processTp;
	}

	/**
	 * 연속거래키를 가져온다.
	 * 
	 * @param responseBody 응답 데이터부
	 * @return 연속거래키 문자열
	 */
	protected String getContinueInfo(IMessageObject responseBody) {
		String continueInfo = StringUtils.defaultIfEmpty(
				(String) ReflectionUtils.getFieldValue(responseBody, "ContinueInfo"),
				StringUtils.EMPTY);
		return continueInfo;
	}
}
