package com.scbank.process.api.fw.message.metadata.factory;

import java.lang.reflect.Field;

import com.scbank.process.api.fw.message.metadata.IMessageMetadata;

/**
 * 메시지 필드에 대한 메타데이터 생성 전략 인터페이스입니다.
 * <p>
 * 각 필드 타입에 따라 알맞은 {@link IMessageMetadata} 구현체를 생성하는 책임을 가집니다.
 * 이 인터페이스는 {@code IntegrationMessageMetadataRegistrar} 등에서
 * 필드 분석 시 조건에 맞는 팩토리를 선택하여 메타데이터를 위임 생성하는 데 사용됩니다.
 * </p>
 *
 * @author Min-jun
 */
public interface IMessageFieldMetadataFactory {

    /**
     * 해당 필드를 이 팩토리가 처리할 수 있는지 여부를 반환합니다.
     *
     * @param field 대상 필드
     * @return 처리 가능 여부
     */
    boolean supports(Field field);

    /**
     * 주어진 필드를 기반으로 메타데이터 객체를 생성합니다.
     *
     * @param field 분석 대상 필드
     * @param order 필드의 순서값
     * @param path  메타데이터 경로 (부모 필드 포함)
     * @return 생성된 메시지 필드 메타데이터
     */
    IMessageMetadata create(Field field, int order, String path);
}
