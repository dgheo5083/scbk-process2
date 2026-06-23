package com.scbank.process.api.fw.base.integration.system.edmi;

import java.util.List;

import org.springframework.util.CollectionUtils;

import com.scbank.process.api.fw.base.integration.system.edmi.exception.EdmiSystemException;
import com.scbank.process.api.fw.base.integration.system.edmi.vo.EdmiError;
import com.scbank.process.api.fw.base.integration.system.edmi.vo.EdmiMsgInfo;
import com.scbank.process.api.fw.base.integration.system.edmi.vo.EdmiMsgInfo.HostMsg;
import com.scbank.process.api.fw.base.integration.system.edmi.vo.EdmiReqHeader;
import com.scbank.process.api.fw.base.integration.system.edmi.vo.EdmiResHeader;
import com.scbank.process.api.fw.base.integration.system.edmi.vo.EdmiSystemHeader;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.integration.context.IntegrationContext;
import com.scbank.process.api.fw.integration.response.IntegrationResponseHandler;

/**
 * EDMI 매핑 전문 응답 핸들러 구현 클래스
 */
public class EdmiResponseHandler implements IntegrationResponseHandler<EdmiReqHeader, EdmiResHeader, EdmiError> {

	@Override
	public boolean isError(EdmiResHeader header) {
		EdmiSystemHeader systemHeader = header.getSystemHeader();
		String procRsltDvCd = systemHeader.getProcRsltDvcd();
		return !"0".equals(procRsltDvCd);
	}
	
	@Override
	public String getResponseCode(EdmiResHeader header) {
		return this.getErrorCode(header);
	}

	@Override
	public void checkErrorAndThrowable(IntegrationContext context, EdmiReqHeader requestHeader,
			EdmiResHeader responseHeader, EdmiError error) {
		if (!this.isError(responseHeader)) {
			return;
		}
		
		String errorCode = this.getResponseCode(responseHeader);
		String errorMessage = this.getErrorMessage(responseHeader);
		
		EdmiSystemException exception = new EdmiSystemException(responseHeader, errorCode, errorMessage);
		exception.setErrorLocation(context.getCaptureSystem());
		
		throw exception;
	}
	
	/**
	 * 메시지부 객체를 획득한다.
	 * @param header 전문 수신 헤더 객체 {@link EdmiResHeader}
	 * @return
	 */
	private EdmiMsgInfo getEdmiMsgInfo(EdmiResHeader header) {
		return header.getMsgInfo();
	}
	
	/**
	 * <pre>
	 * 오류코드를 가져온다.
	 * MSGINFO -> HOST_MSG -> MSG_CD 필드값으로 오류코드 획득
	 * </pre>
	 * @param header 전문 수신 헤더객체 {@link EdmiResHeader}
	 * @return 오류 메시지
	 */
	private String getErrorCode(EdmiResHeader header) {
		EdmiMsgInfo msgInfo = this.getEdmiMsgInfo(header);
		List<HostMsg> hostMsg = msgInfo.getHostMsg();
		if (!CollectionUtils.isEmpty(hostMsg)) {
			HostMsg msg = hostMsg.get(0);
			String msgCd = msg.getMsgCd();
			return msgCd;
		}
		return StringUtils.EMPTY;
	}
	
	/**
	 * <pre>
	 * 오류 메시지를 가져온다.
	 * MSGINFO -> HOST_MSG -> MSG_CTT 필드값으로 오류 메시지 획득
	 * </pre>
	 * @param header 전문 수신 헤더객체 {@link IDataObject}
	 * @return 오류 메시지
	 */
	private String getErrorMessage(EdmiResHeader header) {
		EdmiMsgInfo msgInfo = this.getEdmiMsgInfo(header);
		List<HostMsg> hostMsg = msgInfo.getHostMsg();
		if (!CollectionUtils.isEmpty(hostMsg)) {
			HostMsg msg = hostMsg.get(0);
			String msgCtt = msg.getMsgCtt();
			return msgCtt;
		}
		return StringUtils.EMPTY;
	}
}
