package com.scbank.process.api.fw.dao;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.NonNull;

import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.fw.core.utils.StringUtils;

import lombok.RequiredArgsConstructor;

/**
 * Dao 컴포넌트 빈이름 생성 클래스
 * 
 * @author sungdon.choi
 */
@RequiredArgsConstructor
public class DaoBeanNameGenerator implements BeanNameGenerator {

    /**
     * 데이터베이스명
     */
    private final String dbName;

    @Override
    public @NonNull String generateBeanName(@NonNull BeanDefinition definition,
            @NonNull BeanDefinitionRegistry registry) {

        String beanClassName = definition.getBeanClassName();
        String beanName = beanClassName;
        // log.debug("# definition = {}", definition.getBeanClassName());
        DaoComponent annotation = this.getDaoComponentAnnotation(beanClassName);
        if (annotation == null) {
            beanName = dbName + "#" + beanClassName;
        } else {
            String id = annotation.id();
            if (StringUtils.isEmpty(id)) {
                beanName = dbName + "#" + beanClassName;
            } else {
                beanName = dbName + "#" + id;
            }
        }

        // log.debug("# 확정된 Dao 컴포넌트 bean name : {}", beanName);
        return beanName;
    }

    /**
     * 
     * @param beanClassName
     * @return
     */
    private DaoComponent getDaoComponentAnnotation(String beanClassName) {
        try {
            Class<?> clazz = Class.forName(beanClassName);
            DaoComponent annotation = AnnotationUtils.findAnnotation(clazz, DaoComponent.class);
            return annotation;
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
}
