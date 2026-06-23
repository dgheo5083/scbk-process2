package com.scbank.process.api.fw.channel.context;

import java.util.Locale;
import java.util.Map;

import org.springframework.lang.Nullable;

import com.scbank.process.api.fw.channel.device.IDevice;
import com.scbank.process.api.fw.channel.service.metadata.ServiceDefinitionMetadata;
import com.scbank.process.api.fw.session.ISessionContext;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 프레임워크 기본 서비스 컨텍스트 구현체
 *
 * <p>
 * {@link IServiceContext}의 표준 구현으로, HTTP 요청 단위의 서비스 실행에 필요한
 * 모든 정보를 포함합니다. Spring의 요청 처리 구조와 통합되며, 트랜잭션 처리, 로깅, 라우팅,
 * 세션 등 다양한 공통 기능에서 참조됩니다.
 *
 * <p>
 * record 기반으로 생성되며, 생성자 파라미터가 그대로 필드로 사용됩니다.
 *
 * @param requestUId        요청 UUID
 * @param request           HTTP 요청 객체
 * @param response          HTTP 응답 객체
 * @param device            디바이스 정보
 * @param locale            요청 로케일
 * @param serviceDefinition 서비스 정의 메타데이터
 * @param session           세션 컨텍스트
 * @param attribute         확장 속성 맵
 * @param serviceParameter  서비스정의 메타데이터 파라미터
 * @author sungdon.choi
 */
public record DefaultServiceContext(
        String channelId,
        String requestUId,
        HttpServletRequest request,
        HttpServletResponse response,
        IDevice device,
        Locale locale,
        ServiceDefinitionMetadata serviceDefinition,
        ISessionContext session,
        Map<String, Object> attribute,
        Map<String, String> serviceParameter) implements IServiceContext {

    /**
     * 속성 맵에서 해당 이름의 속성을 조회합니다.
     *
     * @param attrName 속성 키
     * @return 등록된 속성 객체, 존재하지 않으면 null
     */
    @Override
    public Object attribute(@Nullable String attrName) {
        return attribute.get(attrName);
    }

    /**
     * 속성 맵에서 해당 이름의 속성을 타입 캐스팅하여 반환합니다.
     *
     * @param attrName     속성 키
     * @param requiredType 기대하는 반환 타입
     * @return 타입에 맞춰 변환된 객체, 존재하지 않으면 null
     * @param <T> 반환 타입
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T attribute(@Nullable String attrName, @Nullable Class<T> requiredType) {
        return (T) attribute.get(attrName);
    }

    @Override
    public void setAttribute(String attrName, Object value) {
        attribute.put(attrName, value);
    }

    @Override
    public String parameter(String parameterName) {
        return serviceParameter.get(parameterName);
    }
}
