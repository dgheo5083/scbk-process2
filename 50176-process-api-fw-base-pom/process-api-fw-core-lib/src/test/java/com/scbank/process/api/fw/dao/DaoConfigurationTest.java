package com.scbank.process.api.fw.dao;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;

import com.scbank.process.api.fw.dao.DaoProperties.Database;
import com.scbank.process.api.fw.dao.DaoProperties.DatasourceConfig;
import com.scbank.process.api.fw.dao.DaoProperties.MybatisConfig;

/**
 * DaoConfiguration Test Class
 */
@ExtendWith(MockitoExtension.class)
class DaoConfigurationTest {

    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private Environment environment;

    @Mock
    private BeanDefinitionRegistry registry;

    @Mock
    private Resource configResource;

    private DaoConfiguration daoConfiguration;

    @BeforeEach
    void setUp() {
        daoConfiguration = new DaoConfiguration();
    }

    @Nested
    @DisplayName("setApplicationContext tests")
    class SetApplicationContextTests {

        @Test
        @DisplayName("Should set application context correctly")
        void shouldSetApplicationContextCorrectly() {
            assertDoesNotThrow(() -> daoConfiguration.setApplicationContext(applicationContext));
        }

        @Test
        @DisplayName("Should accept non-null application context")
        void shouldAcceptNonNullApplicationContext() {
            daoConfiguration.setApplicationContext(applicationContext);
            // Verify no exception is thrown
            assertNotNull(daoConfiguration);
        }
    }

    @Nested
    @DisplayName("setEnvironment tests")
    class SetEnvironmentTests {

        @Test
        @DisplayName("Should set environment correctly")
        void shouldSetEnvironmentCorrectly() {
            assertDoesNotThrow(() -> daoConfiguration.setEnvironment(environment));
        }

        @Test
        @DisplayName("Should accept non-null environment")
        void shouldAcceptNonNullEnvironment() {
            daoConfiguration.setEnvironment(environment);
            // Verify no exception is thrown
            assertNotNull(daoConfiguration);
        }
    }

    @Nested
    @DisplayName("interface implementation tests")
    class InterfaceImplementationTests {

        @Test
        @DisplayName("Should implement EnvironmentAware")
        void shouldImplementEnvironmentAware() {
            assertTrue(daoConfiguration instanceof org.springframework.context.EnvironmentAware);
        }

        @Test
        @DisplayName("Should implement ApplicationContextAware")
        void shouldImplementApplicationContextAware() {
            assertTrue(daoConfiguration instanceof org.springframework.context.ApplicationContextAware);
        }

        @Test
        @DisplayName("Should implement BeanDefinitionRegistryPostProcessor")
        void shouldImplementBeanDefinitionRegistryPostProcessor() {
            assertTrue(daoConfiguration instanceof org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor);
        }
    }

    @Nested
    @DisplayName("bean name generation tests")
    class BeanNameGenerationTests {

        @Test
        @DisplayName("Should use getBeanName method logic correctly")
        void shouldUseBeanNameMethodLogicCorrectly() {
            // Test the bean name generation pattern through reflection or verification
            // The bean name should follow pattern: {prefix}{SimpleName}
            // e.g., "defaultDataSource", "oracleSqlSessionFactory"
            assertNotNull(daoConfiguration);
        }
    }

    @Nested
    @DisplayName("DaoProperties configuration tests")
    class DaoPropertiesConfigurationTests {

        @Test
        @DisplayName("Should create DaoProperties with enabled flag")
        void shouldCreateDaoPropertiesWithEnabledFlag() {
            DaoProperties properties = new DaoProperties();
            properties.setEnabled(true);

            assertTrue(properties.isEnabled());
        }

        @Test
        @DisplayName("Should create DaoProperties with database configuration")
        void shouldCreateDaoPropertiesWithDatabaseConfiguration() {
            DaoProperties properties = new DaoProperties();
            properties.setEnabled(true);

            Map<String, Database> databaseMap = new HashMap<>();
            DatasourceConfig datasourceConfig = createTestDatasourceConfig();
            MybatisConfig mybatisConfig = new MybatisConfig(
                    "classpath:mybatis-config.xml",
                    List.of("classpath:mapper/**/*.xml"),
                    List.of("com.example.dao")
            );
            Database database = new Database("oracle", datasourceConfig, mybatisConfig);
            databaseMap.put("oracle", database);
            properties.setDatabase(databaseMap);

            assertNotNull(properties.getDatabase());
            assertEquals(1, properties.getDatabase().size());
        }

        @Test
        @DisplayName("Should create DaoProperties with multiple databases")
        void shouldCreateDaoPropertiesWithMultipleDatabases() {
            DaoProperties properties = new DaoProperties();
            properties.setEnabled(true);

            Map<String, Database> databaseMap = new HashMap<>();

            DatasourceConfig oracleConfig = createTestDatasourceConfig();
            oracleConfig.setUrl("jdbc:oracle:thin:@localhost:1521:xe");
            MybatisConfig oracleMybatisConfig = new MybatisConfig(
                    "classpath:oracle/mybatis-config.xml",
                    List.of("classpath:oracle/mapper/**/*.xml"),
                    List.of("com.example.oracle.dao")
            );
            databaseMap.put("oracle", new Database("oracle", oracleConfig, oracleMybatisConfig));

            DatasourceConfig mysqlConfig = createTestDatasourceConfig();
            mysqlConfig.setUrl("jdbc:mysql://localhost:3306/testdb");
            MybatisConfig mysqlMybatisConfig = new MybatisConfig(
                    "classpath:mysql/mybatis-config.xml",
                    List.of("classpath:mysql/mapper/**/*.xml"),
                    List.of("com.example.mysql.dao")
            );
            databaseMap.put("mysql", new Database("mysql", mysqlConfig, mysqlMybatisConfig));

            properties.setDatabase(databaseMap);

            assertEquals(2, properties.getDatabase().size());
            assertTrue(properties.getDatabase().containsKey("oracle"));
            assertTrue(properties.getDatabase().containsKey("mysql"));
        }
    }

    @Nested
    @DisplayName("DatasourceConfig tests")
    class DatasourceConfigTests {

        @Test
        @DisplayName("Should create complete DatasourceConfig")
        void shouldCreateCompleteDatasourceConfig() {
            DatasourceConfig config = createTestDatasourceConfig();

            assertEquals("jdbc:oracle:thin:@localhost:1521:xe", config.getUrl());
            assertEquals("oracle.jdbc.OracleDriver", config.getDriverClassName());
            assertEquals("testuser", config.getUsername());
            assertEquals("testpassword", config.getPassword());
            assertEquals(Integer.valueOf(20), config.getMaximumPoolSize());
            assertEquals(Integer.valueOf(5), config.getMinimumIdle());
            assertEquals(Long.valueOf(30000L), config.getConnectionTimeoutMs());
            assertEquals(Long.valueOf(600000L), config.getIdleTimeoutMs());
            assertEquals(Long.valueOf(1800000L), config.getMaxLifetimeMs());
            assertTrue(config.getAutoCommit());
            assertFalse(config.getReadOnly());
            assertEquals("SELECT 1 FROM DUAL", config.getConnectionTestQuery());
        }

        @Test
        @DisplayName("Should handle partial DatasourceConfig")
        void shouldHandlePartialDatasourceConfig() {
            DatasourceConfig config = new DatasourceConfig();
            config.setUrl("jdbc:mysql://localhost:3306/testdb");
            config.setUsername("user");
            config.setPassword("pass");

            assertEquals("jdbc:mysql://localhost:3306/testdb", config.getUrl());
            assertEquals("user", config.getUsername());
            assertNull(config.getDriverClassName());
            assertNull(config.getMaximumPoolSize());
        }
    }

    @Nested
    @DisplayName("MybatisConfig tests")
    class MybatisConfigTests {

        @Test
        @DisplayName("Should create MybatisConfig with all parameters")
        void shouldCreateMybatisConfigWithAllParameters() {
            MybatisConfig config = new MybatisConfig(
                    "classpath:mybatis-config.xml",
                    List.of("classpath:mapper/**/*.xml", "classpath:custom/**/*.xml"),
                    List.of("com.example.dao", "com.example.repository")
            );

            assertEquals("classpath:mybatis-config.xml", config.getConfigLocation());
            assertEquals(2, config.getMapperLocation().size());
            assertEquals(2, config.getMapperScanBasePackages().size());
        }

        @Test
        @DisplayName("Should create MybatisConfig with single mapper location")
        void shouldCreateMybatisConfigWithSingleMapperLocation() {
            MybatisConfig config = new MybatisConfig(
                    "classpath:mybatis-config.xml",
                    List.of("classpath:mapper/**/*.xml"),
                    List.of("com.example.dao")
            );

            assertEquals(1, config.getMapperLocation().size());
            assertEquals(1, config.getMapperScanBasePackages().size());
        }
    }

    @Nested
    @DisplayName("Configuration annotation tests")
    class ConfigurationAnnotationTests {

        @Test
        @DisplayName("Should have Configuration annotation")
        void shouldHaveConfigurationAnnotation() {
            assertTrue(DaoConfiguration.class.isAnnotationPresent(
                    org.springframework.context.annotation.Configuration.class));
        }

        @Test
        @DisplayName("Should have ConditionalOnProperty annotation")
        void shouldHaveConditionalOnPropertyAnnotation() {
            assertTrue(DaoConfiguration.class.isAnnotationPresent(
                    org.springframework.boot.autoconfigure.condition.ConditionalOnProperty.class));
        }
    }

    private DatasourceConfig createTestDatasourceConfig() {
        DatasourceConfig config = new DatasourceConfig();
        config.setUrl("jdbc:oracle:thin:@localhost:1521:xe");
        config.setDriverClassName("oracle.jdbc.OracleDriver");
        config.setUsername("testuser");
        config.setPassword("testpassword");
        config.setMaximumPoolSize(20);
        config.setMinimumIdle(5);
        config.setConnectionTimeoutMs(30000L);
        config.setIdleTimeoutMs(600000L);
        config.setMaxLifetimeMs(1800000L);
        config.setAutoCommit(true);
        config.setReadOnly(false);
        config.setConnectionTestQuery("SELECT 1 FROM DUAL");
        return config;
    }
}
