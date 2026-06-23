package com.scbank.process.api.fw.base.integration.system.oltp;

import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.gateway.edmi.MBOltpCommonRoute;
import com.scbank.process.api.fw.base.gateway.edmi.base.dto.EDMiRequestMessage;
import com.scbank.process.api.fw.base.gateway.edmi.base.dto.EDMiResponseMessage;
import com.scbank.process.api.fw.base.integration.constant.IntegrationConstant;
import com.scbank.process.api.fw.base.integration.system.oltp.rebound.OltpReboundStrategy;
import com.scbank.process.api.fw.base.integration.system.oltp.vo.OltpError;
import com.scbank.process.api.fw.base.integration.system.oltp.vo.OltpReqHeader;
import com.scbank.process.api.fw.base.integration.system.oltp.vo.OltpResHeader;
import com.scbank.process.api.fw.core.enums.CenterMode;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.core.utils.ByteBuffWrap;
import com.scbank.process.api.fw.core.utils.ByteUtils;
import com.scbank.process.api.fw.core.utils.ReflectionUtils;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.integration.IntegrationManager;
import com.scbank.process.api.fw.integration.IntegrationProperties.IntegrationSystemConfig;
import com.scbank.process.api.fw.integration.client.options.FeignRequestOptionsContext;
import com.scbank.process.api.fw.integration.codec.IntegrationClientCodec;
import com.scbank.process.api.fw.integration.context.IntegrationContext;
import com.scbank.process.api.fw.integration.context.IntegrationContextHolder;
import com.scbank.process.api.fw.integration.exception.IntegrationException;
import com.scbank.process.api.fw.integration.exception.IntegrationSystemException;
import com.scbank.process.api.fw.integration.interceptor.IntegrationInterceptorChain;
import com.scbank.process.api.fw.integration.request.IntegrationRequestHeaderBuilder;
import com.scbank.process.api.fw.integration.response.IntegrationResponseHandler;
import com.scbank.process.api.fw.integration.simulation.IntegrationSimulationRepository;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.context.MessageContext;
import com.scbank.process.api.fw.message.context.MessageContextHolder;
import com.scbank.process.api.fw.message.option.MessageFormatOptions.MessageFormatOption;
import com.scbank.process.api.fw.message.option.SerializationOptions;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

/**
 * OLTP 연계 시스템을 처리하는 통합 매니저 클래스
 *
 * <p>
 * IntegrationManager를 상속하여 호스트 전문 통신의 전반적인 흐름을 구현합니다.
 * <ul>
 * <li>인터셉터 체인 적용</li>
 * <li>코덱 인코딩/디코딩</li>
 * <li>헤더/바디 병합 및 파싱</li>
 * <li>오류 분기 처리</li>
 * </ul>
 */
@Slf4j
public class OltpManager extends IntegrationManager {

    private static final int HEADER_SIZE = 160;

    /**
     * 기본 호스트 리바운드 거래 전략 구현 클래스
     */
    private final OltpReboundStrategy defaultReboundStrategy;

    /**
     * 호스트 시스템 Gateway
     */
    private final MBOltpCommonRoute gateway;
    
    /**
     * 생성자
     * 
     * @param systemConfig             연계시스템별 설정정보
     * @param clientChannelInitializer 연계시스템 클라이언트 채널 초기화 처리자
     * @param requestHeaderBuilder     요청헤더 빌더
     * @param responseHandler          응답 핸들러
     */
    @SuppressWarnings("rawtypes")
    public OltpManager(
            IntegrationSystemConfig systemConfig,
            IntegrationRequestHeaderBuilder requestHeaderBuilder,
            IntegrationResponseHandler responseHandler,
            OltpReboundStrategy reboundStrategy,
            MBOltpCommonRoute gateway) {
        super(systemConfig, requestHeaderBuilder, responseHandler);
        this.defaultReboundStrategy = reboundStrategy;
        this.gateway = gateway;
    }

    @PostConstruct
    public void init() {
        log.debug("# Host Manager 초기화 완료");
    }

    /**
     * 호스트 요청/응답을 처리하는 핵심 메서드
     * 
     * @param context      연계시스템 컨텍스트
     * @param cfg          업무별 연계 설정
     * @param input        요청 메시지 (IMessageObject)
     * @param responseType 응답 메시지 타입
     * @param <I>          요청 메시지 타입
     * @param <O>          응답 메시지 타입
     * @return HostResponse<O>
     * @throws IntegrationException 연계 시스템 전문 송/수신 실패 시 발생
     */
    @SuppressWarnings({ "unchecked" })
    private <I extends IMessageObject, O extends IMessageObject> OltpResponse<O> send0(
            IntegrationContext context,
            OltpRequestOptions cfg,
            OltpRequest<I> hostRequest,
            Class<O> responseType) throws IntegrationException {
        IntegrationInterceptorChain interceptorChain = this.getInterceptorChain();
        IntegrationClientCodec codec = this.getIntegrationClientCodec();
        MessageContext messageContext = this.createMessageContext(context);
        String interfaceId = context.getInterfaceId();

        OltpResponse<O> hostResponse = null;

        try {
        	
        	//본거래/예비거래 여부를 설정한다.
        	context.setAttribute("isRealTran", cfg.isRealTran());
        	context.setAttribute("isPreTran", cfg.isPreTran());
        	
        	//업무 커스텀 설정 저장 (연결/수신타임아웃 정보)
            FeignRequestOptionsContext.set(this.createRequestOptions(cfg));
            
            this.beginTrace(context);

            // 인터셉터: before
            interceptorChain.before(context, hostRequest);

            OltpReqHeader reqHeader = hostRequest.getHeader();
            I input = hostRequest.getRequestMessage();

            // 요청 헤더/바디 인코딩
            byte[] headerBytes = codec.encode(messageContext, reqHeader);
            byte[] requestBytes = codec.encode(messageContext, input);
            byte[] endBytes = new byte[] { 0x1F }; //전문 종료부

            ByteBuffWrap merged = ByteBuffWrap.wrap(ByteUtils.merge(headerBytes, requestBytes, endBytes));

            // HEX 인코딩
            // String hexEncodedRequest = Hex.encodeHexString(merged.getByteArray());

            ByteBuffWrap responseBytes = null;
            if (this.isSimulationMode(cfg)) {
                IntegrationSimulationRepository simulationRepository = this.getIntegrationSimulationRepository();

                // 호스트 헤더부
                OltpResHeader resHeader = simulationRepository.getHeader(this.getSystemId(), interfaceId,
                        OltpResHeader.class);
                
                OltpError errorResponse = null;
                O response = null;
                
                boolean isError = this.responseHandler.isError(resHeader);
                if (isError) {
                    errorResponse = simulationRepository.getError(this.getSystemId(), interfaceId, OltpError.class);
                } else {
                	response = simulationRepository.getResponse(getSystemId(), interfaceId, responseType);
                }
                
                // 인터셉터: after
                interceptorChain.after(context, hostRequest, hostResponse);
                
                if (isError && cfg.isExceptionOnError()) {
                    responseHandler.checkErrorAndThrowable(context, reqHeader, resHeader, errorResponse);
                }
                
                // 대응답 응답 객체 구성
                hostResponse = new OltpResponse<>();
                hostResponse.setHeader(resHeader);
                hostResponse.setError(isError);
                hostResponse.setErrorResponse(errorResponse);
                hostResponse.setResponse(response);
            } else {
            	// ---------------------------------------
            	// HOST HOLIDAY 거래 테스트 처리
            	// captureSystem 을 HOLIDAY 로 설정한다.
            	// TODO 테스트 필요
            	// ---------------------------------------
            	Map<String, Object> properties = this.systemConfig.properties();
            	String holidayTestYn = (String)properties.get("holiday_test_yn");
            	String captureSystem = "Y".equals(holidayTestYn) ? (String)properties.get("holiday_target_system") : context.getCaptureSystem();
            	String typeName = context.getTypeName();
            	String messageSenderBody = context.getMessageSenderBody();
            	String senderDomainBody = context.getSenderDomainBody();
            	
            	if (!StringUtils.hasLength(typeName)) {
            		typeName = "CoreBanking:mbOLTPCommonRoute";
            	}
            	
            	if (!StringUtils.hasLength(messageSenderBody)) {
            		messageSenderBody = "MB";
            	}
            	
            	if (!StringUtils.hasLength(senderDomainBody)) {
            		senderDomainBody = "CoreBanking";
            	}
            	
                EDMiRequestMessage edmiRequest = EDMiRequestMessage.builder()
                		.systemId(this.getSystemId())
                        .interfaceId(interfaceId) //EDMI 인터페이스 ID
                        .trackingId(context.getRequestId()) //EDMI 트래킹ID
                        .requestMessage(merged.getByteArray()) //요청 메시지
                        .hexEncoding(true) //HEX 인코딩 여부
                        .captureSystem(captureSystem) //EDMI 캡쳐시스템
                        .typeName(typeName)
                        .senderBody(messageSenderBody)
                        .senderDomainBody(senderDomainBody)
                        .build();

                // EDMI 전문 송/수신 처리
                EDMiResponseMessage edmiResponse = this.gateway.send(edmiRequest);

                responseBytes = ByteBuffWrap.wrap((byte[]) edmiResponse.getResponseMessage());

                // ---------------------------------------
                // 응답 분해 및 디코딩
                // ---------------------------------------
                int headerSize = HEADER_SIZE;
                int offset = 0;

                byte[] responseHeaderBytes = responseBytes.getByteArray(offset, headerSize);
                offset += headerSize;

                OltpResHeader hostResHeader = codec.decode(messageContext, responseHeaderBytes, OltpResHeader.class);

                OltpError errorResponse = null;
                O response = null;

                byte[] remainBytes = responseBytes.getByteArray(offset, responseBytes.getLength() - offset);

                boolean isError = this.responseHandler.isError(hostResHeader);
                if (isError) {
                    errorResponse = codec.decode(messageContext, remainBytes, OltpError.class);
                } else {
                    response = codec.decode(messageContext, remainBytes, responseType);
                }
                
                // 응답 객체 구성
                hostResponse = new OltpResponse<>();
                hostResponse.setHeader(hostResHeader);
                hostResponse.setError(isError);
                hostResponse.setErrorResponse(errorResponse);
                hostResponse.setResponse(response);
                
                // 인터셉터: after
                interceptorChain.after(context, hostRequest, hostResponse);

                if (isError && cfg.isExceptionOnError()) {
                    responseHandler.checkErrorAndThrowable(context, reqHeader, hostResHeader, errorResponse);
                } else if (isError && !cfg.isExceptionOnError()) {
                	//CAP 에러여부만 체크한다.
                	((OltpResponseHandler)responseHandler).checkCapError(context, reqHeader, hostResHeader, errorResponse);
                }
            }
            return hostResponse;
        } catch (PRCServiceException e) {
        	//인터셉터 onError
        	interceptorChain.onError(context, hostRequest, e);
        	
        	throw e;
        } catch (Throwable e) {
            this.handleException(context, hostRequest, interceptorChain, e);
            return null;
        } finally {
            this.endTrace();
            MessageContextHolder.clear();
            //IntegrationContextHolder.clear();
            FeignRequestOptionsContext.clear();
        }
    }

    /**
     * 호스트 요청/응답을 처리하는 핵심 메서드
     *
     * @param cfg          업무별 연계 설정
     * @param input        요청 메시지 (IMessageObject)
     * @param responseType 응답 메시지 타입
     * @param <I>          요청 메시지 타입
     * @param <O>          응답 메시지 타입
     * @return HostResponse<O>
     * @throws IntegrationException 연계 실패 시 발생
     */
    @SuppressWarnings("unchecked")
    public <I extends IMessageObject, O extends IMessageObject> OltpResponse<O> send(
            OltpRequestOptions cfg,
            I input,
            Class<O> responseType) throws IntegrationException {

        IntegrationContext context = this.createContext(cfg, input);

        //[2026.04.22 최성돈] 전문 거래코드를 IntegrationContext 속성에 복사한다.
        //전문 거래코드 별 서비스 이용시간 체크 시 사용한다.
        String imsTranCd = cfg.getImsTranCd(); //IMS TRAN CODE
        String inClassCd = cfg.getInClassCd(); //입력식별코드
        context.setAttribute("tranCd", imsTranCd + inClassCd);
        
        // 요청 조립
        OltpReqHeader header = (OltpReqHeader) this.requestHeaderBuilder.build(this.systemConfig.defaultHeaders(), cfg);

        OltpRequest<I> hostRequest = (OltpRequest<I>) OltpRequest.builder()
                .header(header)
                .requestMessage(input)
                .build();
        try {
        	return this.send0(context, cfg, hostRequest, responseType);
        } finally {
        	IntegrationContextHolder.clear();
		}
    }

    /**
     * 
     * @param <I>
     * @param <O>
     * @param cfg
     * @param input
     * @param responseType
     * @return
     * @throws IntegrationException
     */
    public <I extends IMessageObject, O extends IMessageObject> OltpResponse<O> sendWithRebound(
            OltpRequestOptions cfg,
            I input,
            Class<O> responseType) throws IntegrationException {
        return this.sendWithRebound(cfg, input, responseType, this.defaultReboundStrategy);
    }

    /**
     * 
     * @param <I>
     * @param <O>
     * @param cfg
     * @param input
     * @param responseType
     * @return
     * @throws IntegrationException
     */
    @SuppressWarnings("unchecked")
    public <I extends IMessageObject, O extends IMessageObject> OltpResponse<O> sendWithRebound(
            OltpRequestOptions cfg,
            I input,
            Class<O> responseType, OltpReboundStrategy reboundStrategy) throws IntegrationException {

        IntegrationContext context = this.createContext(cfg, input);
        
        //[2026.04.22 최성돈] 전문 거래코드를 IntegrationContext 속성에 복사한다.
        //전문 거래코드 별 서비스 이용시간 체크 시 사용한다.
        String imsTranCd = cfg.getImsTranCd(); //IMS TRAN CODE
        String inClassCd = cfg.getInClassCd(); //입력식별코드
        context.setAttribute("tranCd", imsTranCd + inClassCd);
        
        // 요청 조립
        OltpReqHeader header = (OltpReqHeader) this.requestHeaderBuilder.build(this.systemConfig.defaultHeaders(), cfg);

        OltpRequest<I> hostRequest = (OltpRequest<I>) OltpRequest.builder()
                .header(header)
                .requestMessage(input)
                .build();

        OltpResponse<O> hostResponse = this.send0(context, cfg, hostRequest, responseType);
        if (hostResponse == null || hostResponse.isError()) {
            return hostResponse;
        }

        boolean nextPageYn = reboundStrategy.isContinue(context, hostResponse);
        if (!nextPageYn) {
            return hostResponse;
        }

        String listFieldName = StringUtils.defaultIfEmpty(reboundStrategy.getListFieldName(), StringUtils.EMPTY);
        List<Object> recordList = ReflectionUtils.getListField(hostResponse.getResponse(), listFieldName);

        try {
            int nCount = 2;
            int maxLoopCnt = reboundStrategy.getMaxLoopCnt();

            // 센터모드가 성능테스트모드인경우 처리
            CenterMode centerMode = CenterMode.of(RuntimeContext.getCenterMode());
            if (CenterMode.STRESS.equals(centerMode)) {
                maxLoopCnt = 2;
            }

            while (nextPageYn) {
                OltpRequest<? extends IMessageObject> nextHostRequest = reboundStrategy.handleData(hostRequest,
                        hostResponse);

                hostResponse = this.send0(context, cfg, nextHostRequest, responseType);
                nextPageYn = reboundStrategy.isContinue(context, hostResponse);

                // 최대 반복 회수를 넘으면 while 종료
                if (nCount >= maxLoopCnt) {
                    log.debug("# 최대 반복 회수를 넘으면 종료 ==> nCount:[{}], maxLoopCnt:[{}]", nCount, maxLoopCnt);
                    nextPageYn = false;
                }

                List<Object> nextRecordList = ReflectionUtils.getListField(hostResponse.getResponse(), listFieldName);
                if (!CollectionUtils.isEmpty(nextRecordList)) {
                    recordList.addAll(nextRecordList);
                }

                nCount++;
            }

            ReflectionUtils.setFieldValue(hostResponse.getResponse(), listFieldName, recordList);
        } catch (IntegrationException e) {
            throw e;
        } catch (Exception e) {
            throw new IntegrationSystemException(getSystemId(), e);
        } finally {
        	IntegrationContextHolder.clear();
        }
        return hostResponse;
    }
    
    /**
     * 로그처리용으로 요청데이터를 flat 데이터로 생성
     * @param <I>
     * @param cfg
     * @param input
     * @return
     */
    @SuppressWarnings("unchecked")
	public <I extends IMessageObject> Map<String, Object> createRequestBytes(OltpRequestOptions cfg, I input) {
    	try {
    		IntegrationContext context = this.createContext(cfg, input);
        	IntegrationClientCodec codec = this.getIntegrationClientCodec();
            MessageContext messageContext = this.createMessageContext(context);
            
            Map<String, Object> extendedOptions = Map.of(MessageFormatOption.FIELD_MASK.name(), true);
            SerializationOptions mergedOptions = SerializationOptions.merge(context.getSerializationOptions(),
                    extendedOptions);
            messageContext.setSerializationOptions(mergedOptions);
            // 요청 조립
            OltpReqHeader header = (OltpReqHeader) this.requestHeaderBuilder.build(this.systemConfig.defaultHeaders(), cfg);

            OltpRequest<I> hostRequest = (OltpRequest<I>) OltpRequest.builder()
                    .header(header)
                    .requestMessage(input)
                    .build();
            
            OltpReqHeader reqHeader = hostRequest.getHeader();

            // 요청 헤더/바디 인코딩
            byte[] headerBytes = codec.encode(messageContext, reqHeader);
            byte[] requestBytes = codec.encode(messageContext, input);
            byte[] endBytes = new byte[] { 0x1F }; //전문 종료부

            ByteBuffWrap merged = ByteBuffWrap.wrap(ByteUtils.merge(headerBytes, requestBytes, endBytes));
        	//return new String(merged.getByteArray(), context.getCharset());
        	return Map.of(
        			"request", hostRequest, 
        			"data", new String(merged.getByteArray(), context.getCharset()));
    	} catch (Exception e) {
    		log.error(e.getMessage(), e);
    		return null;
    	} finally {
    		IntegrationContextHolder.clear();
    	}
    }
    
    @Override
    protected String getSystemId() {
        return IntegrationConstant.SYSTEM_ID_HOST;
    }
}
