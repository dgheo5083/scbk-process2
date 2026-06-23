package com.scbank.process.api.fw.integration;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.annotation.AnnotationUtils;

import com.scbank.process.api.fw.core.error.FrameworkErrorCode;
import com.scbank.process.api.fw.core.exception.FrameworkRuntimeException;
import com.scbank.process.api.fw.core.log.trace.TraceContextHolder;
import com.scbank.process.api.fw.core.log.trace.TraceSection;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.integration.IntegrationProperties.IntegrationSystemConfig;
import com.scbank.process.api.fw.integration.cfg.IntegrationRequestOptions;
import com.scbank.process.api.fw.integration.codec.IntegrationClientCodec;
import com.scbank.process.api.fw.integration.codec.IntegrationClientCodecRegistry;
import com.scbank.process.api.fw.integration.context.IntegrationContext;
import com.scbank.process.api.fw.integration.context.IntegrationContextHolder;
import com.scbank.process.api.fw.integration.exception.IntegrationException;
import com.scbank.process.api.fw.integration.exception.IntegrationSystemException;
import com.scbank.process.api.fw.integration.exception.IntegrationTimeoutException;
import com.scbank.process.api.fw.integration.interceptor.IntegrationInterceptorChain;
import com.scbank.process.api.fw.integration.interceptor.IntegrationInterceptorRegistry;
import com.scbank.process.api.fw.integration.request.IntegrationRequest;
import com.scbank.process.api.fw.integration.request.IntegrationRequestHeaderBuilder;
import com.scbank.process.api.fw.integration.response.IntegrationResponseHandler;
import com.scbank.process.api.fw.integration.simulation.IntegrationSimulationRepository;
import com.scbank.process.api.fw.integration.support.IntegrationMessageContextCreator;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.MessageFormatOptionConfig;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.context.MessageContext;
import com.scbank.process.api.fw.message.option.DeserializationOptions;
import com.scbank.process.api.fw.message.option.SerializationOptions;

import feign.FeignException.FeignClientException;
import feign.FeignException.FeignServerException;
import feign.Request;
import feign.codec.DecodeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 프레임워크 외부 시스템 연계 매니저 추상 클래스
 *
 * <p>
 * 외부 연계 시스템(MCI, FEP 등)과의 통신 처리를 위한 공통 기능을 제공합니다.
 * <ul>
 * <li>Codec, Client, Interceptor, Retry, HeaderBuilder 등 연동 구성 요소 주입</li>
 * <li>시스템 설정 기반으로 컨텍스트 및 컴포넌트 생성</li>
 * <li>하위 시스템별 구현체(MCIManager 등)에서 send() 구현</li>
 * </ul>
 *
 * <p>
 * 이 클래스를 상속하여 각 연계 시스템 전용 Manager를 구현합니다.
 *
 * @author sungdon.choi
 */
@Slf4j
@RequiredArgsConstructor
public abstract class IntegrationManager {

    /**
     * 연계 시스템 설정 정보 (YAML 기반)
     */
    protected final IntegrationSystemConfig systemConfig;

    /**
     * 요청 헤더 빌더 (시스템별로 동적 구성)
     */
    @SuppressWarnings("rawtypes")
    protected final IntegrationRequestHeaderBuilder requestHeaderBuilder;

    /**
     * 응답 핸들러 (에러 판별 및 예외 전환 처리)
     */
    @SuppressWarnings("rawtypes")
    protected final IntegrationResponseHandler responseHandler;

    /**
     * 메시지 포맷별 Codec 조회용 레지스트리
     */
    protected IntegrationClientCodecRegistry clientCodecRegistry;

    /**
     * 연계 인터셉터 레지스트리
     */
    protected IntegrationInterceptorRegistry interceptorRegistry;

    /**
     * 연계 시스템 전용 메시지 컨텍스트 생성기
     */
    private IntegrationMessageContextCreator messageContextCreator;

    /**
     * 
     * @return
     */
    protected IntegrationClientCodecRegistry getIntegrationClientCodecRegistry() {
        if (this.clientCodecRegistry == null) {
            this.clientCodecRegistry = RuntimeContext.getBean(IntegrationClientCodecRegistry.class);
        }
        return this.clientCodecRegistry;
    }

    /**
     * 
     * @return
     */
    protected IntegrationInterceptorRegistry getIntegrationInterceptorRegistry() {
        if (this.interceptorRegistry == null) {
            this.interceptorRegistry = RuntimeContext.getBean(IntegrationInterceptorRegistry.class);
        }
        return this.interceptorRegistry;
    }

    /**
     * 시스템 설정의 포맷에 해당하는 Codec을 조회합니다.
     *
     * @return 전문 직렬화/역직렬화용 Codec
     */
    protected IntegrationClientCodec getIntegrationClientCodec() {
        return this.getIntegrationClientCodecRegistry().getCodec(systemConfig.format());
    }

    /**
     * 시스템 설정에 정의된 인터셉터 목록으로 InterceptorChain을 구성합니다.
     *
     * @return 연동 요청/응답에 적용할 인터셉터 체인
     */
    protected final IntegrationInterceptorChain getInterceptorChain() {
        IntegrationInterceptorRegistry interceptorRegistry = this.getIntegrationInterceptorRegistry();
        if (interceptorRegistry == null) {
            return null;
        }
        return interceptorRegistry.resolve(systemConfig.interceptors());
    }

    /**
     * 연계시스템 메시지 컨텍스트 생성기를 가져온다.
     * 
     * @return 메시지 컨텍스트
     */
    protected final IntegrationMessageContextCreator getMessageContextCreator() {
        if (messageContextCreator != null) {
            return messageContextCreator;
        }

        messageContextCreator = RuntimeContext.getBean(IntegrationMessageContextCreator.class);
        return messageContextCreator;
    }

    /**
     * 연계 대상 시스템의 고유 ID를 반환합니다.
     * <p>
     * 하위 클래스에서 시스템별 고정값으로 구현
     * </p>
     *
     * @return 시스템 ID 문자열 (예: "MCI", "FEP")
     */
    protected abstract String getSystemId();

    /**
     * 전문 송수신 컨텍스트 객체를 생성합니다.
     * <p>
     * 시스템 ID, 인터페이스 ID, 사용자 설정 값을 포함
     * </p>
     *
     * @param cfg 업무 설정 객체
     * @return IntegrationContext
     */
    protected IntegrationContext createContext(IntegrationRequestOptions cfg) {
        Map<String, Object> attributes = new HashMap<>();
        attributes.putAll(this.systemConfig.properties());
        if (cfg.getAttributes() != null) {
            attributes.putAll(cfg.getAttributes());
        }

        IntegrationContext integrationContext = IntegrationContext.builder()
                .systemId(getSystemId())
                .charset(this.systemConfig.charset())
                .interfaceId(cfg.getInterfaceId())
                .captureSystem(cfg.getCaptureSystem())
                .locale(LocaleContextHolder.getLocale())
                .attributes(attributes)
                .serializationOptions(this.createSerializationOptions())
                .deserializationOptions(this.createDeserializationOptions())
                .build();

        IntegrationContextHolder.set(integrationContext);
        return integrationContext;
    }
    
    /**
     * 전문 송수신 컨텍스트 객체를 생성합니다.
     * <p>
     * 시스템 ID, 인터페이스 ID, 사용자 설정 값을 포함
     * </p>
     *
     * @param cfg 업무 설정 객체
     * @param input IMessageObject를 구현한 요청객체
     * @return IntegrationContext
     */
    protected <I extends IMessageObject> IntegrationContext createContext(IntegrationRequestOptions cfg, I input) {
        Map<String, Object> attributes = new HashMap<>();
        attributes.putAll(this.systemConfig.properties());
        if (cfg.getAttributes() != null) {
            attributes.putAll(cfg.getAttributes());
        }
        
        //EDMI 캡쳐시스템을 획득한다.
        String captureSystem = "";
        String typeName = "";
        String messageSenderBody = "";
        String senderDomainBody = "";
        if (input != null) {
        	Class<? extends IMessageObject> inputClass = input.getClass();
        	captureSystem = this.getCaptureSystem(inputClass);
        	typeName = this.getTypeName(inputClass);
        	messageSenderBody = this.getMessageSenderBody(inputClass);
        	senderDomainBody = this.getSenderDomainBody(inputClass);
        }
        
        if (StringUtils.hasLength(cfg.getCaptureSystem())) {
        	captureSystem = cfg.getCaptureSystem();
        }
        
        if (StringUtils.hasLength(cfg.getTypeName())) {
        	typeName = StringUtils.defaultIfEmpty(cfg.getTypeName(), "");
        }
        
        if (StringUtils.hasLength(cfg.getMessageSenderBody())) {
        	messageSenderBody = StringUtils.defaultIfEmpty(cfg.getMessageSenderBody(), "");
        }
        
        if (StringUtils.hasLength(cfg.getSenderDomainBody())) {
        	senderDomainBody = StringUtils.defaultIfEmpty(cfg.getSenderDomainBody(), "");
        }
        
        //캡쳐시스템이 미설정된 경우
        if (!StringUtils.hasLength(captureSystem)) {
        	throw new IntegrationException(
        			FrameworkErrorCode.INIT_CAPTURE_SYSTEM_NOT_FOUND.getCode(), 
        			"CaptureSystem 속성이 설정되지 않았습니다.");
        }
        
        log.debug("# createContext captureSystem={}, typeName={}, messageSenderBody={}, senderDomainBody={}", 
        		captureSystem, typeName, messageSenderBody, senderDomainBody);

        IntegrationContext integrationContext = IntegrationContext.builder()
                .systemId(getSystemId())
                .charset(this.systemConfig.charset())
                .interfaceId(cfg.getInterfaceId())
                .captureSystem(captureSystem)
                .typeName(typeName)
                .messageSenderBody(messageSenderBody)
                .senderDomainBody(senderDomainBody)
                .locale(LocaleContextHolder.getLocale())
                .attributes(attributes)
                .serializationOptions(this.createSerializationOptions())
                .deserializationOptions(this.createDeserializationOptions())
                .build();

        IntegrationContextHolder.set(integrationContext);
        return integrationContext;
    }

    /**
     * 전문 요청/응답 메시지 변환 컨텍스트 객체 생성
     * 
     * @param context {@link IntegrationContext}
     * @return 메시지 컨텍스트
     */
    protected MessageContext createMessageContext(IntegrationContext context) {
        MessageContext messageContext = this.getMessageContextCreator().create(systemConfig);
        context.setAttribute("_MESSAGE_CTX_", messageContext);
        return messageContext;
    }

    /**
     * 현재 시스템 설정(systemConfig)을 기반으로 직렬화(Serialization) 옵션을 생성합니다.
     *
     * <p>
     * 기본 메시지 포맷별 직렬화 옵션을 조회하고,<br>
     * 추가 사용자 설정이 존재할 경우 병합하여 최종 직렬화 옵션을 생성합니다.
     * </p>
     *
     * @return 최종 구성된 {@link SerializationOptions}
     */
    protected SerializationOptions createSerializationOptions() {
        SerializationOptions defaultOptions = SerializationOptions.getDefaultOptions(this.systemConfig.format())
                .orElseThrow(
                        () -> new IllegalArgumentException("Unsupported MessageFormat: " + this.systemConfig.format()));

        MessageFormatOptionConfig messageFormatOptionConfig = this.systemConfig.messageFormatOptions();
        if (messageFormatOptionConfig == null) {
            return defaultOptions;
        }

        Map<String, Object> extendedOptions = this.systemConfig.messageFormatOptions().getSerialization();
        if (extendedOptions != null) {
            return SerializationOptions.merge(defaultOptions, extendedOptions);
        }

        return defaultOptions;
    }

    /**
     * 현재 시스템 설정(systemConfig)을 기반으로 역직렬화(Deserialization) 옵션을 생성합니다.
     *
     * <p>
     * 기본 메시지 포맷별 역직렬화 옵션을 조회하고,<br>
     * 추가 사용자 설정이 존재할 경우 병합하여 최종 역직렬화 옵션을 생성합니다.
     * </p>
     *
     * @return 최종 구성된 {@link DeserializationOptions}
     */
    protected DeserializationOptions createDeserializationOptions() {
        DeserializationOptions defaultOptions = DeserializationOptions.getDefaultOptions(this.systemConfig.format())
                .orElseThrow(
                        () -> new IllegalArgumentException("Unsupported MessageFormat: " + this.systemConfig.format()));

        MessageFormatOptionConfig messageFormatOptionConfig = this.systemConfig.messageFormatOptions();
        if (messageFormatOptionConfig == null) {
            return defaultOptions;
        }

        Map<String, Object> extendedOptions = this.systemConfig.messageFormatOptions().getDeserialization();
        if (extendedOptions != null) {
            return DeserializationOptions.merge(defaultOptions, extendedOptions);
        }

        return defaultOptions;
    }

    /**
     * 
     * @return
     */
    protected IntegrationSimulationRepository getIntegrationSimulationRepository() {
        return RuntimeContext.getBean(IntegrationSimulationRepository.class);
    }

    /**
     * 연계시스템 전문 송/수신 시작 트레이스 로그 처리
     * 
     * @param ctx {@link IntegrationContext}
     */
    protected void beginTrace(IntegrationContext ctx) {
        String label = ctx.getSystemId() + ":" + ctx.getInterfaceId();
        TraceContextHolder.get()
                .ifPresent((traceContext) -> traceContext.begin(label, TraceSection.EXT_CALL));
    }

    /**
     * 연계시스템 전문 송/수신 완료 트레이스 로그 처리
     */
    protected void endTrace() {
        TraceContextHolder.get().ifPresent((traceContext) -> traceContext.end());
    }

    /**
     * 연계 시스템 전문 송/수신 실패 트레이스 로그 처리
     * 
     * @param ex 전문 송/수신 중 발생한 예외 객체
     */
    protected void failTrace(Throwable ex) {
        TraceContextHolder.get().ifPresent((traceContext) -> traceContext.getCurrent().fail(ex.getMessage()));
    }

    /**
     * 연계 시스템 대응답 모드 사용여부를 리턴한다.
     * 
     * @return 대응답 모드여부
     */
    protected boolean isSimulationMode(IntegrationRequestOptions cfg) {
        // 거래별 대응답 설정을 하는경우
        if (cfg.isSimulationMode()) {
            return true;
        }
        return systemConfig.simulation() != null && systemConfig.simulation().enabled();
    }

    /**
     * 연계시스템 예외처리를 일괄 처리 수행
     * 
     * @param <I>              연계시스템 별 IntegrationRequest 구현 클래스 타입
     * @param context          IntegrationContext
     * @param request          연계시스템 별 IntegrationRequest 구현 클래스
     * @param interceptorChain 연계 시스템 인터셉터 체인
     * @param e                연계 시스템 전문 송/수신 처리 중 발생한 예외 객체
     */
    protected <I extends IntegrationRequest<?, ?>> void handleException(IntegrationContext context, I request,
            IntegrationInterceptorChain interceptorChain, Throwable e) {
        if (e == null) {
            return;
        }

        // 인터셉터 onError call
        interceptorChain.onError(context, request, e);
        
        // 트레이스 실패 처리
        this.failTrace(e);

        if (e instanceof feign.RetryableException re) {
            Throwable cause = re.getCause();
            if (cause instanceof ConnectException ce) {
                throw new IntegrationSystemException(this.getSystemId(),
                        FrameworkErrorCode.INTEG_CONNECTION_FAILED.getCode(), ce);
            } else if (cause instanceof SocketTimeoutException ste) {
                throw new IntegrationTimeoutException(this.getSystemId(), ste);
            } else {
                throw new IntegrationSystemException(this.getSystemId(), cause);
            }
        } else if (e instanceof feign.FeignException fe) {
        	if (e instanceof DecodeException de) {
        		Throwable cause = de.getCause();
        		if (cause != null && cause instanceof IntegrationException ie) {
        			throw ie;
        		} else {
        			throw new IntegrationSystemException(this.getSystemId(), FrameworkErrorCode.INTEG_RECEIVED_FAILED.getCode(), de);
        		}
        	} else if (fe instanceof FeignClientException fce) {
                throw new IntegrationSystemException(this.getSystemId(), FrameworkErrorCode.INTEG_SEND_FAILED.getCode(),
                        fce);
            } else if (fe instanceof FeignServerException fse) {
                throw new IntegrationSystemException(this.getSystemId(),
                        FrameworkErrorCode.INTEG_RECEIVED_FAILED.getCode(), fse);
            } else {
                throw new IntegrationSystemException(this.getSystemId(), FrameworkErrorCode.INTEG_FAILED.getCode(), fe);
            }
        } else if (e instanceof IntegrationException ie) {
            throw ie;
        } else if (e instanceof FrameworkRuntimeException fe) {
        	throw fe;
        } else {
            throw new IntegrationSystemException(this.getSystemId(), FrameworkErrorCode.INTEG_FAILED.getCode(), e);
        }
    }
    
    /**
     * 업무에서 임의로 설정한 연결/수신 타임아웃을 feign.Request.Options 객체로 생성한다.
     * @param options
     * @return
     */
    protected Request.Options createRequestOptions(IntegrationRequestOptions options) {
    	return new Request.Options(
    			Duration.ofMillis(options.getConnectTimeout()), 
    			Duration.ofMillis(options.getReadTimeout()), 
    			false
    	);
    }
    
    /**
     * EDMI 전문 송/수신 Capture 시스템을 획득한다.
     * @param classType 전문 송/수신 DTO 클래스 타입
     * @return
     */
    protected String getCaptureSystem(Class<? extends IMessageObject> classType) {
    	IntegrationMessage integrationMessage = AnnotationUtils.findAnnotation(classType, IntegrationMessage.class);
    	if (integrationMessage == null) {
    		return StringUtils.EMPTY;
    	}
    	
    	return StringUtils.defaultIfEmpty(integrationMessage.captureSystem(), "");
    }
    
    /**
     * EDMI 전문 TypeName 문자열을 획득한다.
     * @param classType 전문 송/수신 DTO 클래스 타입
     * @return
     */
    protected String getTypeName(Class<? extends IMessageObject> classType) {
    	IntegrationMessage integrationMessage = AnnotationUtils.findAnnotation(classType, IntegrationMessage.class);
    	if (integrationMessage == null) {
    		return StringUtils.EMPTY;
    	}
    	
    	return StringUtils.defaultIfEmpty(integrationMessage.typeName(), "");
    }
    
    /**
     * EDMI 전문 MessageSenderBody 문자열을 획득한다.
     * @param classType 전문 송/수신 DTO 클래스 타입
     * @return
     */
    protected String getMessageSenderBody(Class<? extends IMessageObject> classType) {
    	IntegrationMessage integrationMessage = AnnotationUtils.findAnnotation(classType, IntegrationMessage.class);
    	if (integrationMessage == null) {
    		return StringUtils.EMPTY;
    	}
    	
    	return StringUtils.defaultIfEmpty(integrationMessage.messageSenderBody(), "");
    }
    
    /**
     * EDMI 전문 SenderDomainBody 문자열을 획득한다.
     * @param classType 전문 송/수신 DTO 클래스 타입
     * @return
     */
    protected String getSenderDomainBody(Class<? extends IMessageObject> classType) {
    	IntegrationMessage integrationMessage = AnnotationUtils.findAnnotation(classType, IntegrationMessage.class);
    	if (integrationMessage == null) {
    		return StringUtils.EMPTY;
    	}
    	
    	return StringUtils.defaultIfEmpty(integrationMessage.senderDomainBody(), "");
    }
}
