package com.scbank.process.api.fw.base.integration.system.mci.rebound;

import com.scbank.process.api.fw.base.integration.constant.IntegrationConstant;
import com.scbank.process.api.fw.base.integration.system.mci.MciRequest;
import com.scbank.process.api.fw.base.integration.system.mci.MciResponse;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.core.utils.ReflectionUtils;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.integration.context.IntegrationContext;
import com.scbank.process.api.fw.integration.rebound.AbstractIntegrationReboundStrategy;
import com.scbank.process.api.fw.message.IMessageObject;

/**
 * MCI 리바운드 거래 전략 구현 클래스
 * 
 * @author sungdon.choi
 * @version 1.0
 * @since 25. 4. 21.
 * @history
 *          - 2026.04.15 sungdon.choi MCI 리바운드 거래 처리 오류 수정
 */
public class MciReboundStrategy extends
		AbstractIntegrationReboundStrategy<MciRequest<? extends IMessageObject>, MciResponse<? extends IMessageObject>> {

	/**
	 * MCI 시스템 리바운드 거래 최대 허용 Loop 건수
	 */
	private int maxLoopCnt;

	/**
	 * MCI 시스템 리바운드 거래 응답 반복부 필드명
	 */
	private String listFieldName;

	public MciReboundStrategy() {
		this.init();
	}

	protected void init() {
		this.maxLoopCnt = RuntimeContext.getProperty(IntegrationConstant.PROPS_MCI_MAX_LOOP_CNT,
				Integer.class, IntegrationConstant.DEFAULT_MCI_MAX_LOOP_CNT);
		this.listFieldName = RuntimeContext
				.getProperty(IntegrationConstant.PROPS_MCI_LIST_FIELD_NAME,
						IntegrationConstant.DEFAULT_MCI_LIST_FIELD_NAME);
	}

	@Override
	public boolean isContinue(IntegrationContext context, MciResponse<? extends IMessageObject> response) {
		IMessageObject responseBody = response.getResponse();
		String errCd = getFoErrCd(responseBody);
		String continuous = getFoContinuous(responseBody);
		return ("".equals(errCd) && "Y".equals(continuous));
	}

	@Override
	public MciRequest<? extends IMessageObject> handleData(MciRequest<? extends IMessageObject> request,
			MciResponse<? extends IMessageObject> response) {

		IMessageObject responseBody = response.getResponse();
		IMessageObject requestBody = request.getRequestMessage();

		String continuous = getFoContinuous(responseBody); // 연속거래여부
		int pageNo = getFoPageNo(responseBody); // 페이지번호

		ReflectionUtils.setFieldValue(requestBody, "FI_CONTINUOUS", continuous);
		ReflectionUtils.setFieldValue(requestBody, "FI_PAGENO", pageNo);

		return request;
	}

	@Override
	public String getListFieldName() {
		return this.listFieldName;
	}

	@Override
	public int getMaxLoopCnt() {
		return maxLoopCnt;
	}

	/**
	 * 
	 * @param responseBody
	 * @return
	 */
	protected String getFoErrCd(IMessageObject responseBody) {
		String errCd = StringUtils.defaultIfEmpty((String) ReflectionUtils.getFieldValue(responseBody, "FO_ERRCD"), "");
		return errCd;
	}

	/**
	 * 
	 * @param responseBody
	 * @return
	 */
	protected String getFoContinuous(IMessageObject responseBody) {
		String continuous = StringUtils
				.defaultIfEmpty((String) ReflectionUtils.getFieldValue(responseBody, "FO_CONTINUOUS"), "");
		return continuous;
	}

	/**
	 * 
	 * @param responseBody
	 * @return
	 */
	protected Integer getFoPageNo(IMessageObject responseBody) {
		if (responseBody == null) {
			return 0;
		}
		Integer foPageNo = ReflectionUtils.getFieldValue(responseBody, "FO_PAGENO");
		return foPageNo;
	}
}
