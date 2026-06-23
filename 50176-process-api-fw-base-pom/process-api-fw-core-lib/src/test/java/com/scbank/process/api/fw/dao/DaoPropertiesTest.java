package com.scbank.process.api.fw.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.dao.DaoProperties.Database;
import com.scbank.process.api.fw.dao.DaoProperties.DatasourceConfig;
import com.scbank.process.api.fw.dao.DaoProperties.MybatisConfig;

/**
 * {@link DaoProperties} 및 중첩 설정 클래스 단위 테스트.
 */
class DaoPropertiesTest {

    @Test
    @DisplayName("DaoProperties enabled / database 맵 getter/setter")
    void daoPropertiesGetterSetter() {
        DaoProperties properties = new DaoProperties();
        Database database = new Database();
        properties.setEnabled(true);
        properties.setDatabase(Map.of("oracle", database));

        assertThat(properties.isEnabled()).isTrue();
        assertThat(properties.getDatabase()).containsEntry("oracle", database);
    }

    @Test
    @DisplayName("Database 전체 인자 생성자 및 getter")
    void databaseAllArgs() {
        DatasourceConfig ds = new DatasourceConfig();
        MybatisConfig mybatis = new MybatisConfig();
        Database database = new Database("oracle", ds, mybatis);

        assertThat(database.getVendor()).isEqualTo("oracle");
        assertThat(database.getDatasource()).isSameAs(ds);
        assertThat(database.getMybatisConfig()).isSameAs(mybatis);
    }

    @Test
    @DisplayName("Database setter 동작")
    void databaseSetters() {
        Database database = new Database();
        database.setVendor("maria");

        assertThat(database.getVendor()).isEqualTo("maria");
    }

    @Test
    @DisplayName("MybatisConfig 전체 인자 생성자 및 getter")
    void mybatisConfigAllArgs() {
        MybatisConfig config = new MybatisConfig(
                "classpath:mybatis-config.xml",
                List.of("classpath:mapper/*.xml"),
                List.of("com.scbank.mapper"));

        assertThat(config.getConfigLocation()).isEqualTo("classpath:mybatis-config.xml");
        assertThat(config.getMapperLocation()).containsExactly("classpath:mapper/*.xml");
        assertThat(config.getMapperScanBasePackages()).containsExactly("com.scbank.mapper");
    }

    @Test
    @DisplayName("DatasourceConfig 전체 인자 생성자 및 getter")
    void datasourceConfigAllArgs() {
        DatasourceConfig config = new DatasourceConfig(
                "jdbc:oracle:thin:@host",
                "oracle.jdbc.OracleDriver",
                "user",
                "secret",
                10, 2, 30000L, 60000L, 1800000L,
                true, false, "SELECT 1 FROM DUAL");

        assertThat(config.getUrl()).isEqualTo("jdbc:oracle:thin:@host");
        assertThat(config.getDriverClassName()).isEqualTo("oracle.jdbc.OracleDriver");
        assertThat(config.getUsername()).isEqualTo("user");
        assertThat(config.getPassword()).isEqualTo("secret");
        assertThat(config.getMaximumPoolSize()).isEqualTo(10);
        assertThat(config.getMinimumIdle()).isEqualTo(2);
        assertThat(config.getConnectionTimeoutMs()).isEqualTo(30000L);
        assertThat(config.getIdleTimeoutMs()).isEqualTo(60000L);
        assertThat(config.getMaxLifetimeMs()).isEqualTo(1800000L);
        assertThat(config.getAutoCommit()).isTrue();
        assertThat(config.getReadOnly()).isFalse();
        assertThat(config.getConnectionTestQuery()).isEqualTo("SELECT 1 FROM DUAL");
    }

    @Test
    @DisplayName("DatasourceConfig 기본 생성자 및 setter")
    void datasourceConfigNoArgs() {
        DatasourceConfig config = new DatasourceConfig();
        config.setUrl("jdbc:h2:mem:test");

        assertThat(config.getUrl()).isEqualTo("jdbc:h2:mem:test");
    }

    @Test
    @DisplayName("Lombok equals/hashCode/toString 동작")
    void equalsHashCodeToString() {
        MybatisConfig a = new MybatisConfig("c", List.of("m"), List.of("p"));
        MybatisConfig b = new MybatisConfig("c", List.of("m"), List.of("p"));

        assertThat(a).isEqualTo(b);
        assertThat(a).hasSameHashCodeAs(b);
        assertThat(a.toString()).contains("MybatisConfig");
    }
}
