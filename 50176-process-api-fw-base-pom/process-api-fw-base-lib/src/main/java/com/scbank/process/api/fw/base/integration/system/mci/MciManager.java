package com.scbank.process.api.fw.base.integration.system.mci;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.integration.constant.IntegrationConstant;
import com.scbank.process.api.fw.base.integration.system.mci.rebound.MciReboundStrategy;
import com.scbank.process.api.fw.base.integration.system.mci.vo.MciError;
import com.scbank.process.api.fw.base.integration.system.mci.vo.MciReqHeader;
import com.scbank.process.api.fw.base.integration.system.mci.vo.MciResHeader;
import com.scbank.process.api.fw.core.enums.CenterMode;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.core.utils.ByteBuffWrap;
import com.scbank.process.api.fw.core.utils.ByteUtils;
import com.scbank.process.api.fw.core.utils.ReflectionUtils;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.integration.IntegrationManager;
import com.scbank.process.api.fw.integration.IntegrationProperties.IntegrationSystemConfig;
import com.scbank.process.api.fw.integration.codec.IntegrationClientCodec;
import com.scbank.process.api.fw.integration.connector.IntegrationConnector;
import com.scbank.process.api.fw.integration.connector.factory.SimpleTcpConnectorFactory;
import com.scbank.process.api.fw.integration.connector.initializer.IntegrationConnectorChannelInitializer;
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

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

/**
 * MCI 연계 시스템을 처리하는 통합 매니저 클래스
 *
 * <p>
 * IntegrationManager를 상속하여 MCI 전문 통신의 전반적인 흐름을 구현합니다.
 * <ul>
 * <li>인터셉터 체인 적용</li>
 * <li>코덱 인코딩/디코딩</li>
 * <li>헤더/바디 병합 및 파싱</li>
 * <li>오류 분기 처리</li>
 * </ul>
 */
@Slf4j
public final class MciManager extends IntegrationManager {

    /**
     * MCI 연속거래 리바운드 처리 전략 구현 클래스
     */
    private final MciReboundStrategy defaultReboundStrategy;

    /**
     * 
     */
    private final IntegrationConnectorChannelInitializer connectorChannelInitializer;

    // 생성자: MCI 연계를 위한 주요 컴포넌트 주입
    @SuppressWarnings("rawtypes")
    public MciManager(
            IntegrationSystemConfig systemConfig,
            IntegrationConnectorChannelInitializer connectorChannelInitializer,
            IntegrationRequestHeaderBuilder integrationRequestHeaderBuilder,
            IntegrationResponseHandler integrationResponseHandler,
            MciReboundStrategy reboundStrategy) {
        super(systemConfig, integrationRequestHeaderBuilder, integrationResponseHandler);
        this.defaultReboundStrategy = reboundStrategy;
        this.connectorChannelInitializer = connectorChannelInitializer;
    }

    @PostConstruct
    public void init() {
        log.debug("# MCI Manager 초기화 완료");
    }

    /**
     * MCI 요청/응답을 처리하는 핵심 메서드
     * 
     * @param context      연계시스템 컨텍스트
     * @param cfg          업무별 연계 설정
     * @param input        요청 메시지 (IMessageObject)
     * @param responseType 응답 메시지 타입
     * @param <I>          요청 메시지 타입
     * @param <O>          응답 메시지 타입
     * @return MCIResponse<O>
     * @throws IntegrationException 연계 실패 시 발생
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private <I extends IMessageObject, O extends IMessageObject> MciResponse<O> send0(
            IntegrationContext context,
            MciRequestOptions cfg,
            MciRequest<I> mciRequest,
            Class<O> responseType) throws IntegrationException {
        IntegrationConnector connector = this.getIntegrationConnector();
        IntegrationInterceptorChain interceptorChain = this.getInterceptorChain();
        IntegrationClientCodec codec = this.getIntegrationClientCodec();
        MessageContext messageContext = this.createMessageContext(context);
        String interfaceId = context.getInterfaceId();

        MciResponse<O> mciResponse = null;
        try {
            this.beginTrace(context);

            // 인터셉터: before
            interceptorChain.before(context, mciRequest);

            MciReqHeader reqHeader = mciRequest.getHeader();
            I input = mciRequest.getRequestMessage();

            // 요청 헤더/바디 인코딩
            byte[] headerBytes = codec.encode(messageContext, reqHeader);
            byte[] requestBytes = codec.encode(messageContext, input);
            byte[] delimiterBytes = new byte[] { (byte) 0x1F, (byte) 0x1E, (byte) 0xFF, (byte) 0xFF };
            byte[] mergedBytes = ByteUtils.merge(headerBytes, requestBytes, delimiterBytes);

            // ---------------------------------------
            // 전문길이 처리
            // ---------------------------------------
            int msgLL = context.getAttribute("msg_length_field_size", Integer.class);
            int totalLL = (headerBytes.length + requestBytes.length + delimiterBytes.length) - msgLL;
            byte[] msgLLBytes = new byte[msgLL];
            byte[] truncatedLenBytes = Integer.toString(totalLL).getBytes();

            int start = msgLL - truncatedLenBytes.length;
            Arrays.fill(msgLLBytes, 0, start, (byte) 0x30);
            System.arraycopy(truncatedLenBytes, 0, msgLLBytes, start, truncatedLenBytes.length);
            System.arraycopy(msgLLBytes, 0, mergedBytes, 0, msgLL);

            ByteBuffWrap merged = ByteBuffWrap.wrap(mergedBytes);

            if (this.isSimulationMode(cfg)) {
                IntegrationSimulationRepository simulationRepository = this.getIntegrationSimulationRepository();
                // MCI 헤더
                MciResHeader mciResHeader = simulationRepository.getHeader(this.getSystemId(), interfaceId,
                        MciResHeader.class);
                // MCI 개별부
                O response = simulationRepository.getResponse(this.getSystemId(), interfaceId, responseType);

                MciError errorResponse = null;
                boolean isError = this.responseHandler.isError(mciResHeader);

                mciResponse = new MciResponse<>();
                mciResponse.setHeader(mciResHeader);
                mciResponse.setError(isError);
                mciResponse.setErrorResponse(null);
                mciResponse.setResponse(response);

                // 인터셉터: after
                interceptorChain.after(context, mciRequest, mciResponse);

                if (isError && cfg.isExceptionOnError()) {
                    responseHandler.checkErrorAndThrowable(context, reqHeader, mciResHeader, errorResponse); // 에러 응답 예외
                }
            } else {
                // MCI 전문 송/수신 처리
                ByteBuffWrap responseBuffer = (ByteBuffWrap) connector.send(context, merged);

                // ---------------------------------------
                // 응답 분해 및 디코딩
                // ---------------------------------------
                byte[] responseBytes = responseBuffer.getByteArray();
                
                // 헤더 디코딩
                MciResHeader mciResHeader = codec.decode(messageContext, responseBytes, MciResHeader.class);
                // 응답 개별부 디코딩
                byte[] remainBytes = messageContext.getRemainBytes();
                O response = codec.decode(messageContext, remainBytes, responseType);
                // 메시지부가 응답헤더에 있으니 에러응답 처리는 따로 수행하지 않는다.
                MciError errorResponse = null;
                boolean isError = this.responseHandler.isError(mciResHeader);

                // 응답 객체 구성
                mciResponse = new MciResponse<>();
                mciResponse.setHeader(mciResHeader);
                mciResponse.setError(isError);
                mciResponse.setErrorResponse(errorResponse);
                mciResponse.setResponse(response);

                // 인터셉터: after
                interceptorChain.after(context, mciRequest, mciResponse);

                if (isError && cfg.isExceptionOnError()) {
                    responseHandler.checkErrorAndThrowable(context, reqHeader, mciResHeader, errorResponse); // 에러 응답 예외
                }
            }

            return mciResponse;
        } catch (PRCServiceException e) {
        	//인터셉터 onError
        	interceptorChain.onError(context, mciRequest, e);
        	
        	throw e;
        } catch (Throwable e) {
            this.handleException(context, mciRequest, interceptorChain, e);
            return null;
        } finally {
            this.endTrace();
            MessageContextHolder.clear();
            IntegrationContextHolder.clear();
        }
    }

    /**
     * MCI 요청/응답을 처리하는 핵심 메서드
     *
     * @param cfg          업무별 연계 설정
     * @param input        요청 메시지 (IMessageObject)
     * @param responseType 응답 메시지 타입
     * @param <I>          요청 메시지 타입
     * @param <O>          응답 메시지 타입
     * @return MCIResponse<O>
     * @throws IntegrationException MCI 전문 송/수신 실패 시 발생
     */
    @SuppressWarnings("unchecked")
    public <I extends IMessageObject, O extends IMessageObject> MciResponse<O> send(
            MciRequestOptions cfg,
            I input,
            Class<O> responseType) throws IntegrationException {

        Map<String, Object> defaultHeaders = this.systemConfig.defaultHeaders();
        IntegrationContext context = this.createContext(cfg);

        // 거래코드를 IntegrationContext 속성에 복사한다.
        context.setAttribute("tranCd", cfg.getTranCd());
        context.setAttribute("ipv6Addr", defaultHeaders.get("ipv6Adr"));

        MciReqHeader header = (MciReqHeader) this.requestHeaderBuilder.build(defaultHeaders, cfg);

        MciRequest<I> mciRequest = new MciRequest<>();
        mciRequest.setHeader(header);
        mciRequest.setRequestMessage(input);

        return this.send0(context, cfg, mciRequest, responseType);
    }

    /**
     * MCI 리바운드 거래 처리
     * 
     * @param <I>          요청 메시지 타입
     * @param <O>          응답 메시지 타입
     * @param cfg          업무별 연계 설정
     * @param input        요청 메시지 (IMessageObject)
     * @param responseType 응답 메시지 타입
     * @return MCIResponse<O>
     * @throws IntegrationException MCI 전문 송/수신 실패 시 발생
     */
    public <I extends IMessageObject, O extends IMessageObject> MciResponse<O> sendWithRebound(
            MciRequestOptions cfg,
            I input,
            Class<O> responseType) throws IntegrationException {
        return this.sendWithRebound(cfg, input, responseType, this.defaultReboundStrategy);
    }

    /**
     * MCI 리바운드 거래 처리
     * 
     * @param <I>             요청 메시지 타입
     * @param <O>             응답 메시지 타입
     * @param cfg             업무별 연계 설정
     * @param input           요청 메시지 (IMessageObject)
     * @param responseType    응답 메시지 타입
     * @param reboundStrategy 업무 커스터마이징 MCI 리바운드거래 전략
     * @return MCIResponse<O>
     * @throws IntegrationException MCI 전문 송/수신 실패 시 발생
     * @history
     *          - 2026.04.15 sungdon.choi 기본 헤더 정보 처리 누락 오류 수정
     */
    @SuppressWarnings("unchecked")
    public <I extends IMessageObject, O extends IMessageObject> MciResponse<O> sendWithRebound(
            MciRequestOptions cfg,
            I input,
            Class<O> responseType,
            MciReboundStrategy reboundStrategy) throws IntegrationException {

        if (reboundStrategy == null) {
            reboundStrategy = new MciReboundStrategy();
        }

        Map<String, Object> defaultHeaders = this.systemConfig.defaultHeaders();
        IntegrationContext context = this.createContext(cfg);

        // 거래코드를 IntegrationContext 속성에 복사한다.
        context.setAttribute("tranCd", cfg.getTranCd());
        context.setAttribute("ipv6Addr", defaultHeaders.get("ipv6Adr"));

        MciReqHeader header = (MciReqHeader) this.requestHeaderBuilder.build(defaultHeaders, cfg);

        MciRequest<I> mciRequest = new MciRequest<>();
        mciRequest.setHeader(header);
        mciRequest.setRequestMessage(input);

        MciResponse<O> mciResponse = this.send0(context, cfg, mciRequest, responseType);

        if (mciResponse == null || mciResponse.isError()) {
            return mciResponse;
        }

        boolean nextPageYn = reboundStrategy.isContinue(context, mciResponse);
        if (!nextPageYn) {
            return mciResponse;
        }

        String listFieldName = StringUtils.defaultIfEmpty(reboundStrategy.getListFieldName(), "");
        List<Object> recordList = ReflectionUtils.getListField(mciResponse.getResponse(), listFieldName);

        try {

            int nCount = 2;
            int maxLoopCnt = reboundStrategy.getMaxLoopCnt();

            // 센터모드가 성능테스트모드인경우 처리
            CenterMode centerMode = CenterMode.of(RuntimeContext.getCenterMode());
            if (CenterMode.STRESS.equals(centerMode)) {
                maxLoopCnt = 2;
            }

            while (nextPageYn) {
                MciRequest<? extends IMessageObject> nextMciRequest = reboundStrategy.handleData(mciRequest,
                        mciResponse);

                mciResponse = this.send0(context, cfg, nextMciRequest, responseType);
                nextPageYn = reboundStrategy.isContinue(context, mciResponse);

                // 최대 반복 회수를 넘으면 while 종료
                if (nCount >= maxLoopCnt) {
                    log.debug("# 최대 반복 회수를 넘으면 종료 ==> nCount:[{}], maxLoopCnt:[{}]", nCount, maxLoopCnt);
                    nextPageYn = false;
                }

                List<Object> nextRecordList = ReflectionUtils.getListField(mciResponse.getResponse(), listFieldName);
                if (!CollectionUtils.isEmpty(nextRecordList)) {
                    recordList.addAll(nextRecordList);
                }

                nCount++;
            }

            ReflectionUtils.setFieldValue(mciResponse.getResponse(), listFieldName, recordList);
        } catch (IntegrationException e) {
            throw e;
        } catch (Exception e) {
            throw new IntegrationSystemException(getSystemId(), e);
        }
        return mciResponse;
    }

    /**
     * 시스템 ID를 "mci"로 고정 반환
     */
    @Override
    protected String getSystemId() {
        return IntegrationConstant.SYSTEM_ID_MCI;
    }

    /**
     * 시스템 설정에 정의된 프로토콜에 따라 IntegrationConnector를 생성합니다.
     * <p>
     * 현재는 TCP 대해서만 기본 클라이언트를 생성합니다.
     * </p>
     *
     * @return IntegrationClient {@link IntegrationConnector}
     */
    @SuppressWarnings("rawtypes")
    protected IntegrationConnector getIntegrationConnector() {
        IntegrationConnector client = new SimpleTcpConnectorFactory().create(
                getSystemId(),
                this.systemConfig,
                this.connectorChannelInitializer);

        return client;
    }
}
