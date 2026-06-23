package com.scbank.process.api.fw.message.metadata;

import java.util.ArrayList;
import java.util.List;

import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 통합 전문 메타데이터의 기본 구현체입니다.
 * 
 * <p>
 * 전문 ID, 전문 타입, 대상 매핑 클래스, 하위 필드 메타데이터 목록을 관리합니다.
 * </p>
 * 
 * <p>
 * {@link IIntegrationMessageMetadata} 인터페이스를 구현합니다.
 * </p>
 * 
 * @see IIntegrationMessageMetadata
 * @see Type
 * @since 25. 4. 22.
 * @version 1.0
 *          작성자: sungdon.choi
 */
@Data
@EqualsAndHashCode
@Builder
public class DefaultIntegrationMessageMetadata implements IIntegrationMessageMetadata {

    /** 통합 전문 ID (식별자) */
    private String id;

    /** 통합 전문 타입 ({@link Type}) */
    private Type type;

    /** 매핑 대상 클래스 */
    private Class<?> targetClass;

    /** 하위 필드 메타데이터 목록 */
    private List<IMessageMetadata> children;

    /**
     * 
     */
    private boolean xmlRootWrap;

    /**
     * 
     */
    private String xmlRootName;

    /**
     * EDMI 캡쳐시스템
     */
    private String captureSystem;

    /**
     * 하위 메타데이터를 추가합니다.
     *
     * @param child 추가할 하위 메타데이터
     */
    public void addChildren(IMessageMetadata... child) {
        if (this.children == null) {
            this.children = new ArrayList<>();
        }
        children.stream()
                .forEach(c -> this.children.add(c));
    }
}
