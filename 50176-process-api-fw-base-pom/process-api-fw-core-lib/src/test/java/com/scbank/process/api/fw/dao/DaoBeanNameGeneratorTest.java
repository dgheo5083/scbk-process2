package com.scbank.process.api.fw.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import com.scbank.process.api.fw.core.component.DaoComponent;

/**
 * {@link DaoBeanNameGenerator} 단위 테스트.
 */
class DaoBeanNameGeneratorTest {

    private final BeanDefinitionRegistry registry = mock(BeanDefinitionRegistry.class);

    @DaoComponent(database = "oracle", id = "USER_DAO")
    static class AnnotatedWithId {
    }

    @DaoComponent(database = "oracle")
    static class AnnotatedWithoutId {
    }

    static class NotAnnotated {
    }

    private BeanDefinition definitionFor(String className) {
        BeanDefinition definition = mock(BeanDefinition.class);
        when(definition.getBeanClassName()).thenReturn(className);
        return definition;
    }

    @Test
    @DisplayName("@DaoComponent.id 가 있으면 'dbName#id' 형식으로 생성")
    void withAnnotationId() {
        DaoBeanNameGenerator generator = new DaoBeanNameGenerator("oracle");
        BeanDefinition definition = definitionFor(AnnotatedWithId.class.getName());

        String beanName = generator.generateBeanName(definition, registry);

        assertThat(beanName).isEqualTo("oracle#USER_DAO");
    }

    @Test
    @DisplayName("@DaoComponent 가 있으나 id 가 비어있으면 'dbName#className' 형식으로 생성")
    void withAnnotationEmptyId() {
        DaoBeanNameGenerator generator = new DaoBeanNameGenerator("oracle");
        BeanDefinition definition = definitionFor(AnnotatedWithoutId.class.getName());

        String beanName = generator.generateBeanName(definition, registry);

        assertThat(beanName).isEqualTo("oracle#" + AnnotatedWithoutId.class.getName());
    }

    @Test
    @DisplayName("@DaoComponent 가 없으면 'dbName#className' 형식으로 생성")
    void withoutAnnotation() {
        DaoBeanNameGenerator generator = new DaoBeanNameGenerator("maria");
        BeanDefinition definition = definitionFor(NotAnnotated.class.getName());

        String beanName = generator.generateBeanName(definition, registry);

        assertThat(beanName).isEqualTo("maria#" + NotAnnotated.class.getName());
    }

    @Test
    @DisplayName("클래스를 찾을 수 없으면 애노테이션 없음으로 간주하여 'dbName#className' 형식으로 생성")
    void classNotFoundFallsBack() {
        DaoBeanNameGenerator generator = new DaoBeanNameGenerator("mysql");
        BeanDefinition definition = definitionFor("com.scbank.process.api.fw.dao.DoesNotExist");

        String beanName = generator.generateBeanName(definition, registry);

        assertThat(beanName).isEqualTo("mysql#com.scbank.process.api.fw.dao.DoesNotExist");
    }
}
