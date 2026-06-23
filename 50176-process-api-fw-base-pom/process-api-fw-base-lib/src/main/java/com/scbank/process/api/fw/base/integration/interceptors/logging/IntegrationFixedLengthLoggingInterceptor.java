package com.scbank.process.api.fw.base.integration.interceptors.logging;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.scbank.process.api.fw.base.integration.log.IntegrationLogCollectEvent;
import com.scbank.process.api.fw.base.integration.log.IntegrationLogEvent;
import com.scbank.process.api.fw.base.integration.log.IntegrationLogEventPublisher;
import com.scbank.process.api.fw.base.integration.system.mci.MciResponse;
import com.scbank.process.api.fw.base.integration.system.mci.vo.MciMsgInfo;
import com.scbank.process.api.fw.base.integration.system.mci.vo.MciMsgInfo.MsgInfo;
import com.scbank.process.api.fw.base.integration.system.mci.vo.MciResHeader;
import com.scbank.process.api.fw.base.utils.PropertiesUtils;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.core.utils.ByteUtils;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.integration.codec.FixedLengthIntegrationClientCodec;
import com.scbank.process.api.fw.integration.context.IntegrationContext;
import com.scbank.process.api.fw.integration.request.IntegrationRequest;
import com.scbank.process.api.fw.integration.response.IntegrationResponse;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.context.MessageContext;
import com.scbank.process.api.fw.message.context.MessageContextFactory;
import com.scbank.process.api.fw.message.enums.MessageFormat;
import com.scbank.process.api.fw.session.ISessionContextManager;

import lombok.extern.slf4j.Slf4j;

/**
 * 고정길이방식 연계시스템 요청/응답 로깅 처리 인터셉터 구현 클래스
 * 
 * @author sungdon.choi
 */
@Slf4j
@Component("integrationFixedLengthLoggingInterceptor")
public class IntegrationFixedLengthLoggingInterceptor extends AbstractIntegrationLoggingInterceptor {

	/**
	 * 
	 */
    private final FixedLengthIntegrationClientCodec integrationClientCodec;
    
    /**
     * 전문로그 이벤트 발행자
     */
    private final IntegrationLogEventPublisher integrationLogEventPublisher;

    public IntegrationFixedLengthLoggingInterceptor(
    		MessageContextFactory messageContextFactory,
            FixedLengthIntegrationClientCodec integrationClientCodec, 
            IntegrationLogEventPublisher integrationLogEventPublisher) {
        super(messageContextFactory, Set.of(MessageFormat.FIXEDLENGTH));
        this.integrationClientCodec = integrationClientCodec;
        this.integrationLogEventPublisher = integrationLogEventPublisher;
    }

	@SuppressWarnings("unchecked")
	@Override
    public void before(IntegrationContext context, Object request) {
        try {
            MessageContext extentedCtx = this.getMessageContext(context);

            if (request instanceof IntegrationRequest req) {
                IMessageObject header = req.getHeader();
                IMessageObject requestBody = req.getRequestMessage();

                byte[] headerBytes = integrationClientCodec.encode(extentedCtx, (IMessageObject) header);
                byte[] requestBytes = integrationClientCodec.encode(extentedCtx, (IMessageObject) requestBody);
                byte[] mergedBytes = ByteUtils.merge(headerBytes, requestBytes);

                log.info("# [{}][{}] 전문 요청 로그: [{}]", context.getSystemId(), context.getInterfaceId(),
                        new String(mergedBytes, extentedCtx.getDefaultEncoding()));
                
                //****************************************
                // MCI 전문 로그 이벤트 발행
                //****************************************
                if (isActiveMciLog(context)) {
                	this.integrationLogEventPublisher.publish(IntegrationLogEvent.builder()
                			.systemId(context.getSystemId())
                			.trCls(IntegrationLogEvent.TRAN_IN)
                			.messageId(context.getInterfaceId())
                			.txCd(context.getAttribute("tranCd"))
                			.ipAddress(context.getAttribute("ipv6Adr"))
                			.data(new String(mergedBytes, extentedCtx.getDefaultEncoding()))
                			.build());
                }
                
                //****************************************
                //로그수집 이벤트 발행
                //****************************************
                ISessionContextManager sessionContextManager = RuntimeContext.getBean(ISessionContextManager.class);
                String userId = StringUtils.defaultIfEmpty(sessionContextManager.getLoginValue("UserID", String.class), StringUtils.EMPTY);
                String vFdsDeviceKey = StringUtils.defaultIfEmpty(sessionContextManager.getGlobalValue("VFDSDeviceKey", String.class), StringUtils.EMPTY);
                
                this.integrationLogEventPublisher.publish(IntegrationLogCollectEvent.builder()
                		.userId(userId)
                		.systemId(context.getSystemId())
                		.channelType("I")
                		.request(req)
                		.txDscd("S")
                		.deviceKey(vFdsDeviceKey)
                		.data(new String(mergedBytes, extentedCtx.getDefaultEncoding()))
                		.build());
            }
        } catch (Exception e) {
            log.error("요청 전문 로깅 처리 중 오류 발생", e);
        }
    }

    @SuppressWarnings("unchecked")
	@Override
    public void after(IntegrationContext context, Object request, Object response) {
        try {
            MessageContext extentedCtx = this.getMessageContext(context);

            if (response instanceof IntegrationResponse res) {
                IMessageObject header = res.getHeader();
                IMessageObject responseBody = res.getResponse();
                IMessageObject errorResponse = res.getErrorResponse();

                if ("mci".equalsIgnoreCase(context.getSystemId())) {
                	byte[] headerBytes = integrationClientCodec.encode(extentedCtx, header);
                    byte[] responseBytes = integrationClientCodec.encode(extentedCtx, responseBody);
                    byte[] mergedBytes = ByteUtils.merge(headerBytes, responseBytes);

                    log.info("# [{}][{}] 전문 응답 로그: [{}]", context.getSystemId(), context.getInterfaceId(),
                            new String(mergedBytes, extentedCtx.getDefaultEncoding()));
                    
                    if (this.isActiveMciLog(context)) {
                    	//****************************************
                        // MCI 전문 로그 이벤트 발행
                        //****************************************
                        this.integrationLogEventPublisher.publish(IntegrationLogEvent.builder()
                        		.systemId(context.getSystemId())
                    			.trCls(IntegrationLogEvent.TRAN_OUT)
                    			.messageId(context.getInterfaceId())
                    			.txCd(context.getAttribute("tranCd"))
                    			.ipAddress(context.getAttribute("ipv6Adr"))
                    			.responseCode(getMciResponseCode(res))
                    			.data(new String(mergedBytes, extentedCtx.getDefaultEncoding()))
                    			.build());
                    }
                    return;
                    
                } else {
                	byte[] headerBytes = integrationClientCodec.encode(extentedCtx, header);
                    byte[] responseBytes = integrationClientCodec.encode(extentedCtx,
                            res.isError() ? errorResponse : responseBody);
                    byte[] mergedBytes = ByteUtils.merge(headerBytes, responseBytes);

                    log.info("# [{}][{}] 전문 응답 로그: [{}]", context.getSystemId(), context.getInterfaceId(),
                            new String(mergedBytes, extentedCtx.getDefaultEncoding()));
                    
                    //****************************************
                    //로그수집 이벤트 발행
                    //****************************************
                    
                    ISessionContextManager sessionContextManager = RuntimeContext.getBean(ISessionContextManager.class);
                    String userId = StringUtils.defaultIfEmpty(sessionContextManager.getLoginValue("UserID", String.class), StringUtils.EMPTY);
                    String vFdsDeviceKey = StringUtils.defaultIfEmpty(sessionContextManager.getGlobalValue("VFDSDeviceKey", String.class), StringUtils.EMPTY);
                    
                    this.integrationLogEventPublisher.publish(IntegrationLogCollectEvent.builder()
                    		.userId(userId)
                    		.systemId(context.getSystemId())
                    		.channelType("I")
                    		.response(res)
                    		.txDscd("R")
                    		.deviceKey(vFdsDeviceKey)
                    		.data(new String(mergedBytes, extentedCtx.getDefaultEncoding()))
                    		.build());
                }
            }
        } catch (Exception e) {
            log.error("응답 전문 로깅 처리 중 오류 발생", e);
        }
    }
    
    private boolean isActiveMciLog(IntegrationContext context) {
    	return "mci".equalsIgnoreCase(context.getSystemId()) && "Y".equals(PropertiesUtils.getString("IS_ACTIVE_MCI_LOG"));
     }
    
    /**
     * MCI 전문 응답 객체에서 응답코드를 획득한다.
     * @param response MCI 전문 응답 객체
     * @return
     */
    @SuppressWarnings("rawtypes")
	private String getMciResponseCode(IntegrationResponse response) {
    	if (response instanceof MciResponse res) {
    		MciResHeader header = res.getHeader();
    		MciMsgInfo mciMsgInfo = header.getMciMsgInfo();
    		MsgInfo msgInfo = mciMsgInfo.getMsgInfo();
    		return msgInfo.getMsgCd();
    	}
    	return StringUtils.EMPTY;
    }
}
