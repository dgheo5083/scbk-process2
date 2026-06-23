package com.scbank.process.api.fw.message.metadata.registry;

import java.util.List;

import com.scbank.process.api.fw.message.metadata.IIntegrationMessageMetadata;

/**
 * 프레임워크 전문 메타데이터 등록 및 관리 처리를 담당하는 인터페이스입니다.
 * 
 * <p>
 * 전문 메타데이터를 패키지 스캔을 통해 적재하거나,
 * 클래스 기반으로 조회하거나, 전체 메타데이터 목록을 반환하는 기능을 제공합니다.
 * </p>
 * 
 * @see IIntegrationMessageMetadata
 * @since 25. 4. 22.
 * @version 1.0
 *          작성자: sungdon.choi
 */
public interface IIntegrationMessageMetadataRegistrar {

    /**
     * 지정된 베이스 패키지들에서 메시지 메타데이터를 스캔하여 적재합니다.
     *
     * @param basePackages 스캔할 패키지 경로 배열
     */
    void scan(String... basePackages);

    /**
     * 지정한 최상위 전문 클래스에 매핑되는 메타데이터를 조회합니다.
     *
     * @param targetClass 최상위 전문 클래스
     * @return 해당 클래스에 매핑된 {@link IIntegrationMessageMetadata}, 없으면 {@code null}
     */
    IIntegrationMessageMetadata getMetadata(Class<?> targetClass);

    /**
     * 적재된 모든 전문 메타데이터 목록을 반환합니다.
     *
     * @return 전체 {@link IIntegrationMessageMetadata} 목록
     */
    List<IIntegrationMessageMetadata> getAllMetadata();
}
