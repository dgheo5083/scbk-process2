package com.scbank.process.api.fw.dao;

import java.util.Map;

import org.mybatis.spring.mapper.ClassPathMapperScanner;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.type.AnnotationMetadata;

import com.scbank.process.api.fw.core.component.DaoComponent;

import lombok.extern.slf4j.Slf4j;

/**
 * DaoComponent 전용 스캐너 - database 속성 필터링 포함
 *
 * @author sungdon.choi
 */
@Slf4j
public class DaoComponentScanner extends ClassPathMapperScanner {

    /**
     * 데이터베이스명
     */
    private final String database;

    /**
     * 
     * @param database 데이터베이스명
     * @param registry {@link BeanDefinitionRegistry}
     */
    public DaoComponentScanner(String database, BeanDefinitionRegistry registry) {
        super(registry);
        this.database = database;
    }

    /**
     * DaoComponent(database) 조건에 맞는 클래스만 스캔 대상으로 포함
     */
    @Override
    public void registerFilters() {
        this.addIncludeFilter((metadataReader, metadataReaderFactory) -> {
            AnnotationMetadata metadata = metadataReader.getAnnotationMetadata();

            if (!metadata.hasAnnotation(DaoComponent.class.getName())) {
                return false;
            }

            Map<String, Object> attributes = metadata.getAnnotationAttributes(DaoComponent.class.getName());
            if (attributes == null)
                return false;

            String databaseAttr = (String) attributes.get("database");
            boolean enabledAttr = (boolean) attributes.getOrDefault("enabled", true);

            boolean match = this.database.equalsIgnoreCase(databaseAttr);
            if (!match) {
                log.trace("# 프레임워크 DAO [{}] registerFilters - ✗ DB mismatch - Skipped: {} → expected={}, found={}",
                        this.database, metadata.getClassName(), this.database, databaseAttr);
            }

            return match && enabledAttr;
        });
    }
}
