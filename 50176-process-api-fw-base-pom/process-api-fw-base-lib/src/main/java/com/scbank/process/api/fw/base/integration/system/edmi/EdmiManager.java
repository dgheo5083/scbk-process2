package com.scbank.process.api.fw.base.integration.system.edmi;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.gateway.edmi.EDMIGatewayRegistry;
import com.scbank.process.api.fw.base.gateway.edmi.base.dto.EDMiRequestMessage;
import com.scbank.process.api.fw.base.gateway.edmi.base.dto.EDMiResponseMessage;
import com.scbank.process.api.fw.base.gateway.edmi.base.gateway.EDMIGateway;
import com.scbank.process.api.fw.base.integration.constant.IntegrationConstant;
import com.scbank.process.api.fw.base.integration.system.edmi.vo.EdmiError;
import com.scbank.process.api.fw.base.integration.system.edmi.vo.EdmiReqHeader;
import com.scbank.process.api.fw.base.integration.system.edmi.vo.EdmiResHeader;
import com.scbank.process.api.fw.core.utils.ByteBuffWrap;
import com.scbank.process.api.fw.integration.IntegrationManager;
import com.scbank.process.api.fw.integration.IntegrationProperties.IntegrationSystemConfig;
import com.scbank.process.api.fw.integration.client.options.FeignRequestOptionsContext;
import com.scbank.process.api.fw.integration.codec.IntegrationClientCodec;
import com.scbank.process.api.fw.integration.context.IntegrationContext;
import com.scbank.process.api.fw.integration.context.IntegrationContextHolder;
import com.scbank.process.api.fw.integration.exception.IntegrationException;
import com.scbank.process.api.fw.integration.interceptor.IntegrationInterceptorChain;
import com.scbank.process.api.fw.integration.request.IntegrationRequestHeaderBuilder;
import com.scbank.process.api.fw.integration.response.IntegrationResponseHandler;
import com.scbank.process.api.fw.integration.simulation.IntegrationSimulationRepository;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.context.MessageContext;
import com.scbank.process.api.fw.message.context.MessageContextHolder;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

/**
 * 호스트 외 연계 시스템을 처리하는 통합 매니저 클래스
 *
 * <p>
 * IntegrationManager를 상속하여 호스트 외 시스템 전문 통신의 전반적인 흐름을 구현합니다.
 * <ul>
 * <li>인터셉터 체인 적용</li>
 * <li>코덱 인코딩/디코딩</li>
 * <li>헤더/바디 병합 및 파싱</li>
 * <li>오류 분기 처리</li>
 * </ul>
 */
@Slf4j
public class EdmiManager extends IntegrationManager {

	/**
	 * 
	 */
	private final EDMIGatewayRegistry registry;
	
	/**
	 * 
	 * @param systemConfig
	 * @param requestHeaderBuilder
	 * @param responseHandler
	 */
	@SuppressWarnings("rawtypes")
	public EdmiManager(
			IntegrationSystemConfig systemConfig, 
			IntegrationRequestHeaderBuilder requestHeaderBuilder,
			IntegrationResponseHandler responseHandler,
			EDMIGatewayRegistry registry) {
		super(systemConfig, requestHeaderBuilder, responseHandler);
		this.registry = registry;
	}
	
	@PostConstruct
    public void init() {
        log.debug("# EDMI Manager 초기화 완료");
    }
	
	/**
     * 호스트 외 시스템 요청/응답을 처리하는 핵심 메서드
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
	@SuppressWarnings("unchecked")
	private <I extends IMessageObject, O extends IMessageObject> EdmiResponse<O> send0(
			IntegrationContext context,
            EdmiRequestOptions cfg,
            EdmiRequest<I> edmiRequest,
            Class<O> responseType
			) throws IntegrationException {
    	IntegrationInterceptorChain interceptorChain = this.getInterceptorChain();
        IntegrationClientCodec codec = this.getIntegrationClientCodec();
        MessageContext messageContext = this.createMessageContext(context);
        String interfaceId = context.getInterfaceId();
    	
    	EdmiResponse<O> edmiResponse = null;
    	
    	try {
        	//업무 커스텀 설정 저장 (연결/수신타임아웃 정보)
            FeignRequestOptionsContext.set(this.createRequestOptions(cfg));
            
            this.beginTrace(context);

            // 인터셉터: before
            interceptorChain.before(context, edmiRequest);

            EdmiReqHeader reqHeader = edmiRequest.getHeader();
            I input = edmiRequest.getRequestMessage();

            //codec.encode(messageContext, null);
            // 요청 헤더/바디 인코딩
            byte[] headerBytes = codec.encode(messageContext, reqHeader);
            byte[] requestBytes = codec.encode(messageContext, input);
            
            ByteBuffWrap mered = ByteBuffWrap.wrap(this.merged(headerBytes, requestBytes));
            
            if (this.isSimulationMode(cfg)) {
                IntegrationSimulationRepository simulationRepository = this.getIntegrationSimulationRepository();

                // 호스트 헤더부
                EdmiResHeader resHeader = simulationRepository.getHeader(this.getSystemId(), interfaceId,
                		EdmiResHeader.class);
                
                EdmiError errorResponse = null;
                O response = null;
                
                boolean isError = this.responseHandler.isError(resHeader);
                if (!isError) {
                	response = simulationRepository.getResponse(getSystemId(), interfaceId, responseType);
                }
                
             // 인터셉터: after
                interceptorChain.after(context, edmiRequest, edmiResponse);
                
                if (isError && cfg.isExceptionOnError()) {
                    responseHandler.checkErrorAndThrowable(context, reqHeader, resHeader, errorResponse);
                }
                
                // 대응답 응답 객체 구성
                edmiResponse = new EdmiResponse<>();
                edmiResponse.setHeader(resHeader);
                edmiResponse.setError(isError);
                edmiResponse.setErrorResponse(errorResponse);
                edmiResponse.setResponse(response);
            } else {
                EDMiRequestMessage edmiRequestMessage = EDMiRequestMessage.builder()
                        .interfaceId(interfaceId) //EDMI 인터페이스 ID
                        .systemId(this.getSystemId())
                        .trackingId(context.getRequestId()) //EDMI 트래킹ID
                        .requestMessage(mered.getByteArray()) //요청 메시지
                        .captureSystem(context.getCaptureSystem()) //EDMI 캡쳐시스템
                        .typeName(context.getTypeName())
                        .senderBody(context.getMessageSenderBody())
                        .senderDomainBody(context.getSenderDomainBody())
                        .build();

                EDMIGateway gateway = this.registry.resolve(context.getTypeName());
                // EDMI 전문 송/수신 처리
                EDMiResponseMessage edmiResponseMessage = gateway.send(edmiRequestMessage);

                log.debug("# edmiResponseMessage={}", edmiResponseMessage);
                //TODO
                
                byte[] responseBytes = (byte[])edmiResponseMessage.getResponseMessage();
                
                Map<String, byte[]> responseMap = this.convertResponse(responseBytes);
                byte[] responseHeaderBytes = responseMap.get("header");
                byte[] responseMessageBytes = responseMap.get("response");
                
                EdmiResHeader edmiResHeader = codec.decode(messageContext, responseHeaderBytes, EdmiResHeader.class);
                
                EdmiError errorResponse = null;
                O response = null;
                
				boolean isError = this.responseHandler.isError(edmiResHeader);
                if (!isError) {
                	response = codec.decode(messageContext, responseMessageBytes, responseType);
                }
                
                edmiResponse = new EdmiResponse<>();
                edmiResponse.setHeader(edmiResHeader);
                edmiResponse.setError(isError);
                edmiResponse.setErrorResponse(errorResponse);
                edmiResponse.setResponse(response);
                
                // 인터셉터: after
                interceptorChain.after(context, edmiRequest, edmiResponse);

                if (isError && cfg.isExceptionOnError()) {
                    responseHandler.checkErrorAndThrowable(context, reqHeader, edmiResHeader, errorResponse);
                }
                
            }
            return edmiResponse;
    	} catch (PRCServiceException e) {
    		//인터셉터 onError
        	interceptorChain.onError(context, edmiRequest, e);
        	
            throw e;
        } catch (Throwable e) {
            this.handleException(context, edmiRequest, interceptorChain, e);
            return null;
        } finally {
            this.endTrace();
            MessageContextHolder.clear();
            IntegrationContextHolder.clear();
            FeignRequestOptionsContext.clear();
        }
	}

	/**
     * 호스트 외 요청/응답을 처리하는 핵심 메서드
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
	public <I extends IMessageObject, O extends IMessageObject> EdmiResponse<O> send(
			EdmiRequestOptions  cfg,
			I input,
			Class<O> responseType) throws IntegrationException {
		
		IntegrationContext context = this.createContext(cfg, input);
		
		EdmiReqHeader header = (EdmiReqHeader)this.requestHeaderBuilder.build(this.systemConfig.defaultHeaders(), cfg);
		
		EdmiRequest<I> edmiRequest = (EdmiRequest<I>)EdmiRequest.builder()
				.header(header)
				.requestMessage(input)
				.build();
		return this.send0(context, cfg, edmiRequest, responseType);
	}

	@Override
	protected String getSystemId() {
		return IntegrationConstant.SYSTEM_ID_EDMI;
	}
	
	/**
	 * 
	 * @param headerBytes
	 * @param requestMessageBytes
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private byte[] merged(byte[] headerBytes, byte[] requestMessageBytes) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		
		Map<String, Object> headers = objectMapper.readValue(headerBytes, Map.class);
		Map<String, Object> tranData = objectMapper.readValue(requestMessageBytes, Map.class);
		
		Map<String, Object> merged = new LinkedHashMap<>();
		merged.putAll(headers);
		merged.put("TRANDATA", tranData);
		
		byte[] mergedBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(merged);
		return mergedBytes;
	}

	/**
	 * 응답 JSON 전문을 Map으로 변경 후 헤더/개별부를 나누어 응답데이터 역직렬화 처리를 할 수 있도록 한다.
	 * @param responseBytes 응답 전문
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private Map<String, byte[]> convertResponse(byte[] responseBytes) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> responseMap = objectMapper.readValue(responseBytes, Map.class);
		Map<String, Object> responseBody = (Map<String, Object>)responseMap.remove("TRANDATA");
		
		Map<String, byte[]> result = new HashMap<>();
		result.put("header", objectMapper.writeValueAsBytes(responseMap));
		result.put("response", objectMapper.writeValueAsBytes(responseBody));
		return result;
	}
}
