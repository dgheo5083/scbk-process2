package com.scbank.process.api.fw.channel.context;

import java.io.Serializable;
import java.util.Locale;

import com.scbank.process.api.fw.channel.device.IDevice;
import com.scbank.process.api.fw.channel.service.metadata.ServiceDefinitionMetadata;
import com.scbank.process.api.fw.session.ISessionContext;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 프레임워크의 서비스 실행 시점에 사용되는 컨텍스트 인터페이스
 *
 * <p>
 * 각 서비스 요청마다 생성되며, 요청 처리와 관련된 다양한 정보를 캡슐화하여
 * 핸들러, 필터, 인터셉터 등에서 공통적으로 참조할 수 있도록 합니다.
 *
 * <ul>
 * <li>요청 UUID</li>
 * <li>HTTP 요청/응답 객체</li>
 * <li>디바이스 정보</li>
 * <li>로케일 정보</li>
 * <li>서비스 라우팅 메타데이터</li>
 * <li>세션 또는 사용자 속성</li>
 * </ul>
 *
 * @author sungdon.choi
 */
public interface IServiceContext extends Serializable {

    /**
     * 요청 채널ID를 획득한다.
     * 
     * @return
     */
    String channelId();

    /**
     * 요청 고유 식별자(UUID)를 반환합니다.
     *
     * @return 요청 식별자 문자열
     */
    String requestUId();

    /**
     * 원본 HTTP 요청 객체를 반환합니다.
     *
     * @return {@link HttpServletRequest}
     */
    HttpServletRequest request();

    /**
     * 원본 HTTP 응답 객체를 반환합니다.
     *
     * @return {@link HttpServletResponse}
     */
    HttpServletResponse response();

    /**
     * 디바이스 정보 객체를 반환합니다.
     *
     * @return {@link IDevice} 구현체
     */
    @Deprecated
    IDevice device();

    /**
     * 요청으로부터 해석된 로케일 정보를 반환합니다.
     *
     * @return 요청 로케일
     */
    Locale locale();

    /**
     * 현재 요청의 라우팅 메타데이터를 반환합니다.
     *
     * @return {@link ServiceDefinitionMetadata}
     */
    ServiceDefinitionMetadata serviceDefinition();

    /**
     * 컨텍스트에 저장된 속성 값을 조회합니다.
     *
     * @param attrName 속성 키
     * @return 속성 값 객체 (null 가능)
     */
    Object attribute(String attrName);

    /**
     * 컨텍스트에 저장된 속성 값을 타입 캐스팅하여 조회합니다.
     *
     * @param attrName     속성 키
     * @param requiredType 반환받고자 하는 타입
     * @return 타입에 맞게 변환된 속성 값, 또는 null
     */
    <T> T attribute(String attrName, Class<T> requiredType);

    default void setAttribute(String attrName, Object value) {

    }

    /**
     * 서비스정의에 작성된 서비스 파라미터 값을 가져온다.
     * 
     * @param parameterName 서비스 파라미터명
     * @return
     */
    String parameter(String parameterName);

    ISessionContext session();
}
