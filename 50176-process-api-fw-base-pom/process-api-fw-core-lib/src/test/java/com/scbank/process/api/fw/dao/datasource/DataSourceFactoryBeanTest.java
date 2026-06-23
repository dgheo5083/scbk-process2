package com.scbank.process.api.fw.dao.datasource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.sql.DataSource;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.env.Environment;

/**
 * {@link DataSourceFactoryBean} 단위 테스트.
 *
 * <p>{@code getObject()} 의 마지막 단계는 실제 커넥션 풀을 생성하는 {@code new HikariDataSource(cfg)} 로,
 * 테스트 클래스패스에 JDBC 드라이버가 없으므로 풀 초기화에 도달하기 전 단계(필수 속성 검증)만 검증한다.</p>
 */
class DataSourceFactoryBeanTest {

    private static final String PREFIX = "csl.dao.database.oracle.datasource.";

    @Test
    @DisplayName("getObjectType 은 DataSource 타입을 반환")
    void getObjectType() {
        DataSourceFactoryBean factoryBean = new DataSourceFactoryBean("oracle");

        assertThat(factoryBean.getObjectType()).isEqualTo(DataSource.class);
    }

    @Test
    @DisplayName("isSingleton 은 true")
    void isSingleton() {
        DataSourceFactoryBean factoryBean = new DataSourceFactoryBean("oracle");

        assertThat(factoryBean.isSingleton()).isTrue();
    }

    @Test
    @DisplayName("url 속성이 없으면 IllegalStateException")
    void missingUrlThrows() {
        Environment env = mock(Environment.class);
        when(env.getProperty(PREFIX + "url")).thenReturn(null);

        DataSourceFactoryBean factoryBean = new DataSourceFactoryBean("oracle");
        factoryBean.setEnvironment(env);

        assertThatThrownBy(factoryBean::getObject)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(PREFIX + "url");
    }

    @Test
    @DisplayName("url 속성이 공백이면 IllegalStateException")
    void blankUrlThrows() {
        Environment env = mock(Environment.class);
        when(env.getProperty(PREFIX + "url")).thenReturn("   ");

        DataSourceFactoryBean factoryBean = new DataSourceFactoryBean("oracle");
        factoryBean.setEnvironment(env);

        assertThatThrownBy(factoryBean::getObject)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(PREFIX + "url");
    }

    @Test
    @DisplayName("username 속성이 없으면 IllegalStateException")
    void missingUsernameThrows() {
        Environment env = mock(Environment.class);
        when(env.getProperty(PREFIX + "url")).thenReturn("jdbc:oracle:thin:@host");
        when(env.getProperty(PREFIX + "username")).thenReturn(null);

        DataSourceFactoryBean factoryBean = new DataSourceFactoryBean("oracle");
        factoryBean.setEnvironment(env);

        assertThatThrownBy(factoryBean::getObject)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(PREFIX + "username");
    }

    @Test
    @DisplayName("credential 속성이 없으면 IllegalStateException")
    void missingCredentialThrows() {
        Environment env = mock(Environment.class);
        when(env.getProperty(PREFIX + "url")).thenReturn("jdbc:oracle:thin:@host");
        when(env.getProperty(PREFIX + "username")).thenReturn("user");
        when(env.getProperty(PREFIX + "credential")).thenReturn(null);

        DataSourceFactoryBean factoryBean = new DataSourceFactoryBean("oracle");
        factoryBean.setEnvironment(env);

        assertThatThrownBy(factoryBean::getObject)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(PREFIX + "credential");
    }

    @Test
    @DisplayName("driver-class-name 속성이 없으면 IllegalStateException")
    void missingDriverThrows() {
        Environment env = mock(Environment.class);
        lenient().when(env.getProperty(PREFIX + "url")).thenReturn("jdbc:oracle:thin:@host");
        lenient().when(env.getProperty(PREFIX + "username")).thenReturn("user");
        lenient().when(env.getProperty(PREFIX + "credential")).thenReturn("secret");
        when(env.getProperty(PREFIX + "driver-class-name")).thenReturn(null);

        DataSourceFactoryBean factoryBean = new DataSourceFactoryBean("oracle");
        factoryBean.setEnvironment(env);

        assertThatThrownBy(factoryBean::getObject)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(PREFIX + "driver-class-name");
    }
}
