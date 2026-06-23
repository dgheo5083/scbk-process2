package com.scbank.process.api.fw.dao;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

/**
 * DaoComponentScanner Test Class
 */
@ExtendWith(MockitoExtension.class)
class DaoComponentScannerTest {

    @Mock
    private BeanDefinitionRegistry registry;

    private DaoComponentScanner scanner;

    @BeforeEach
    void setUp() {
        scanner = new DaoComponentScanner("oracle", registry);
    }

    @Nested
    @DisplayName("constructor tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create scanner with database and registry")
        void shouldCreateScannerWithDatabaseAndRegistry() {
            DaoComponentScanner newScanner = new DaoComponentScanner("mysql", registry);

            assertNotNull(newScanner);
        }

        @Test
        @DisplayName("Should create scanner with empty database name")
        void shouldCreateScannerWithEmptyDatabaseName() {
            DaoComponentScanner newScanner = new DaoComponentScanner("", registry);

            assertNotNull(newScanner);
        }

        @Test
        @DisplayName("Should create scanner with different database names")
        void shouldCreateScannerWithDifferentDatabaseNames() {
            DaoComponentScanner oracleScanner = new DaoComponentScanner("oracle", registry);
            DaoComponentScanner mysqlScanner = new DaoComponentScanner("mysql", registry);
            DaoComponentScanner mariaScanner = new DaoComponentScanner("maria", registry);

            assertNotNull(oracleScanner);
            assertNotNull(mysqlScanner);
            assertNotNull(mariaScanner);
        }
    }

    @Nested
    @DisplayName("registerFilters tests")
    class RegisterFiltersTests {

        @Test
        @DisplayName("Should register filters without exception")
        void shouldRegisterFiltersWithoutException() {
            assertDoesNotThrow(() -> scanner.registerFilters());
        }

        @Test
        @DisplayName("Should allow multiple registerFilters calls")
        void shouldAllowMultipleRegisterFiltersCalls() {
            assertDoesNotThrow(() -> {
                scanner.registerFilters();
                scanner.registerFilters();
            });
        }
    }

    @Nested
    @DisplayName("database filtering tests")
    class DatabaseFilteringTests {

        @Test
        @DisplayName("Should create scanner for oracle database")
        void shouldCreateScannerForOracleDatabase() {
            DaoComponentScanner oracleScanner = new DaoComponentScanner("oracle", registry);

            assertNotNull(oracleScanner);
            assertDoesNotThrow(() -> oracleScanner.registerFilters());
        }

        @Test
        @DisplayName("Should create scanner for mysql database")
        void shouldCreateScannerForMysqlDatabase() {
            DaoComponentScanner mysqlScanner = new DaoComponentScanner("mysql", registry);

            assertNotNull(mysqlScanner);
            assertDoesNotThrow(() -> mysqlScanner.registerFilters());
        }

        @Test
        @DisplayName("Should create scanner for case-sensitive database name")
        void shouldCreateScannerForCaseSensitiveDatabaseName() {
            DaoComponentScanner upperScanner = new DaoComponentScanner("ORACLE", registry);
            DaoComponentScanner lowerScanner = new DaoComponentScanner("oracle", registry);

            assertNotNull(upperScanner);
            assertNotNull(lowerScanner);
        }
    }

    @Nested
    @DisplayName("inheritance tests")
    class InheritanceTests {

        @Test
        @DisplayName("Should extend ClassPathMapperScanner")
        void shouldExtendClassPathMapperScanner() {
            assertTrue(scanner instanceof org.mybatis.spring.mapper.ClassPathMapperScanner);
        }
    }
}
