package com.scbank.process.api.fw.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mybatis.spring.SqlSessionTemplate;

import com.scbank.process.api.fw.dao.paging.PagingRequestContext;

/**
 * DaoComponentFactoryBean Test Class
 */
@ExtendWith(MockitoExtension.class)
class DaoComponentFactoryBeanTest {

    @Mock
    private SqlSessionTemplate sqlSessionTemplate;

    @Nested
    @DisplayName("constructor tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create factory bean with mapper interface")
        void shouldCreateFactoryBeanWithMapperInterface() {
            DaoComponentFactoryBean<TestMapper> factoryBean = new DaoComponentFactoryBean<>(TestMapper.class);

            assertNotNull(factoryBean);
            assertEquals(TestMapper.class, factoryBean.getMapperInterface());
        }

        @Test
        @DisplayName("Should create factory bean with different mapper interface")
        void shouldCreateFactoryBeanWithDifferentMapperInterface() {
            DaoComponentFactoryBean<AnotherMapper> factoryBean = new DaoComponentFactoryBean<>(AnotherMapper.class);

            assertNotNull(factoryBean);
            assertEquals(AnotherMapper.class, factoryBean.getMapperInterface());
        }
    }

    @Nested
    @DisplayName("getMapperInterface tests")
    class GetMapperInterfaceTests {

        @Test
        @DisplayName("Should return correct mapper interface")
        void shouldReturnCorrectMapperInterface() {
            DaoComponentFactoryBean<TestMapper> factoryBean = new DaoComponentFactoryBean<>(TestMapper.class);

            assertEquals(TestMapper.class, factoryBean.getMapperInterface());
        }
    }

    @Nested
    @DisplayName("PagingRequestContext integration tests")
    class PagingRequestContextIntegrationTests {

        @BeforeEach
        void setUp() {
            PagingRequestContext.clear();
        }

        @Test
        @DisplayName("Should clear PagingRequestContext after initial state")
        void shouldClearPagingRequestContextAfterInitialState() {
            PagingRequestContext.set(10, 20);
            assertNotNull(PagingRequestContext.get());

            PagingRequestContext.clear();

            assertNull(PagingRequestContext.get());
        }
    }

    interface TestMapper {
        Object selectById(Long id);

        Object selectWithPaging(@org.apache.ibatis.annotations.Param("offset") Integer offset,
                                @org.apache.ibatis.annotations.Param("limit") Integer limit);
    }

    interface AnotherMapper {
        void insert(Object entity);
    }
}
