package com.scbank.process.api.fw.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.lang.NonNull;
import org.springframework.transaction.TransactionManager;

import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.fw.dao.DaoProperties.DatasourceConfig;
import com.scbank.process.api.fw.dao.DaoProperties.MybatisConfig;
import com.scbank.process.api.fw.dao.datasource.DataSourceFactoryBean;

import lombok.extern.slf4j.Slf4j;

/**
 * DAO 설정 클래스
 *
 * - JNDI 기반 DataSource Bean 등록
 * - SqlSessionFactory, SqlSessionTemplate, TransactionManager 등록
 * - 사용자 정의 @DaoComponent 애노테이션 기반 Mapper 자동 스캔
 * - Mapper XML 파일 위치 및 설정 자동 등록
 *
 * 구성 방식:
 * 1. application.yml → csl.dao.database.* 설정을 기반으로 동작
 * 2. BeanDefinitionRegistryPostProcessor 를 이용한 수동 Bean 등록
 * 3. DaoComponentScanner 를 통해 사용자 Mapper 등록
 *
 * @author sungdon.choi
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "csl.dao", name = "enabled", havingValue = "true")
public class DaoConfiguration implements EnvironmentAware,
        ApplicationContextAware,
        BeanDefinitionRegistryPostProcessor {

    /**
     * 
     */
    private ApplicationContext applicationContext;

    /**
     * 
     */
    private Environment environment;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void setEnvironment(@NonNull Environment environment) {
        this.environment = environment;
    }

    /**
     * application.yml 로부터 DaoProperties 로 바인딩 후 Bean 등록
     */
    @Override
    public void postProcessBeanDefinitionRegistry(@NonNull BeanDefinitionRegistry registry) throws BeansException {
        Binder binder = Binder.get(environment);
        DaoProperties properties = binder.bind("csl.dao", DaoProperties.class).get();

        if (log.isInfoEnabled()) {
            log.info("# 프레임워크 DAO Bean 등록 시작");
        }

        this.registerBean(registry, properties);
    }

    /**
     * 업무 데이터베이스 별 Mybatis 구성 시 필요한 Bean 구성 등록
     * 
     * @param registry   {@link BeanDefinitionRegistry}
     * @param properties DAO 프로퍼티
     */
    private void registerBean(BeanDefinitionRegistry registry, DaoProperties properties) {
        properties.getDatabase().forEach((key, database) -> {
            MybatisConfig mybatisConfig = database.getMybatisConfig();
            try {
                this.registerDataSourceBean(registry, key, database.getDatasource());
                this.registerSqlSessionFactoryBean(registry, key, mybatisConfig);
                // this.registerSqlSessionTemplate(registry, key);
                this.registerTransactionManagerBean(registry, key);
                this.daoComponentScanner(registry, key,
                        mybatisConfig.getMapperScanBasePackages().toArray(String[]::new));
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        });
    }

    /**
     * JNDI 기반 데이터소스 Bean 등록
     * 
     * @param registry {@link BeanDefinitionRegistry}
     * @param database 업무 데이터베이스명
     * @param jndiName 데이터소스 JNDI명
     */
    private void registerDataSourceBean(BeanDefinitionRegistry registry, String database,
            DatasourceConfig cfg) {
        if (log.isInfoEnabled()) {
            log.info("# 프레임워크 DAO [{}] 데이터소스 Bean 등록, config: {}", database, cfg);
        }

        GenericBeanDefinition def = new GenericBeanDefinition();
        def.setBeanClass(DataSourceFactoryBean.class);
        def.getConstructorArgumentValues().addIndexedArgumentValue(0, database);

        registry.registerBeanDefinition(this.getBeanName(database, DataSource.class), def);
    }

    /**
     * SqlSessionFactory 등록
     * 
     * @param registry {@link BeanDefinitionRegistry}
     * @param database 업무 데이터베이스명
     * @param config   마이바티스 설정
     */
    private void registerSqlSessionFactoryBean(BeanDefinitionRegistry registry, String database, MybatisConfig config) {
        if (log.isInfoEnabled()) {
            log.info("# 프레임워크 DAO [{}] SqlSessionFactory Bean 등록, configLocation: {}, mapperLocation: {}",
                    database, config.getConfigLocation(), config.getMapperLocation());
        }

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        Resource configLocation = applicationContext.getResource(config.getConfigLocation());
        List<Resource> mapperLocations = new ArrayList<>();
        config.getMapperLocation().forEach(location -> {
            try {
                Resource[] found = resolver.getResources(location);
                mapperLocations.addAll(Arrays.asList(found));
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        });

        GenericBeanDefinition def = new GenericBeanDefinition();
        def.setBeanClass(SqlSessionFactoryBean.class);

        MutablePropertyValues props = def.getPropertyValues();
        props.add("dataSource", new RuntimeBeanReference(this.getBeanName(database, DataSource.class)));
        props.add("configLocation", configLocation);
        props.add("mapperLocations", mapperLocations);

        registry.registerBeanDefinition(this.getBeanName(database, SqlSessionFactory.class), def);
    }

    /**
     * SqlSessionTemplate 등록
     * 
     * @param registry {@link BeanDefinitionRegistry}
     * @param database 업무 데이터베이스명
     */
    @SuppressWarnings("unused")
    private void registerSqlSessionTemplate(BeanDefinitionRegistry registry, String database) {
        if (log.isInfoEnabled()) {
            log.info("# 프레임워크 DAO [{}] SqlSessionTemplate Bean 등록", database);
        }

        GenericBeanDefinition def = new GenericBeanDefinition();
        def.setBeanClass(SqlSessionTemplate.class);

        ConstructorArgumentValues args = new ConstructorArgumentValues();
        args.addIndexedArgumentValue(0, new RuntimeBeanReference(this.getBeanName(database, SqlSessionFactory.class)));
        def.setConstructorArgumentValues(args);

        registry.registerBeanDefinition(this.getBeanName(database, SqlSessionTemplate.class), def);
    }

    /**
     * TransactionManager 등록
     * 
     * @param registry {@link BeanDefinitionRegistry}
     * @param database 업무 데이터베이스명
     */
    private void registerTransactionManagerBean(BeanDefinitionRegistry registry, String database) {
        if (log.isInfoEnabled()) {
            log.info("# 프레임워크 DAO [{}] TransactionManager Bean 등록", database);
        }

        GenericBeanDefinition def = new GenericBeanDefinition();
        def.setBeanClass(DataSourceTransactionManager.class);

        MutablePropertyValues props = new MutablePropertyValues();
        props.add("dataSource", new RuntimeBeanReference(this.getBeanName(database, DataSource.class)));
        def.setPropertyValues(props);

        registry.registerBeanDefinition(this.getBeanName(database, TransactionManager.class), def);
    }

    /**
     * DAO 컴포넌트(@DaoComponent) 스캔 처리
     * 
     * @param registry     {@link BeanDefinitionRegistry}
     * @param database     업무 데이터베이스명
     * @param basePackages 업무 데이터베이스 하위 스캔 대상 패키지 목록
     */
    private void daoComponentScanner(BeanDefinitionRegistry registry, String database, String... basePackages) {
        if (log.isInfoEnabled()) {
            log.info("# 프레임워크 DAO [{}] DAO 컴포넌트(@DaoComponent) Bean 등록, basePackages: [{}]", database, basePackages);
        }

        DaoComponentScanner scanner = new DaoComponentScanner(database, registry);
        scanner.setSqlSessionFactoryBeanName(this.getBeanName(database, SqlSessionFactory.class));
        // scanner.setSqlSessionTemplateBeanName(this.getBeanName(database,
        // SqlSessionTemplate.class));
        scanner.setMapperFactoryBeanClass(DaoComponentFactoryBean.class);
        scanner.setPrintWarnLogIfNotFoundMappers(true);
        scanner.setAnnotationClass(DaoComponent.class);
        scanner.setBeanNameGenerator(new DaoBeanNameGenerator(database));
        scanner.registerFilters();
        scanner.scan(basePackages);
    }

    /**
     * Bean 이름 규칙 생성기
     * 예: defaultDataSource, defaultSqlSessionFactory
     */
    private String getBeanName(String prefix, Class<?> clazz) {
        if (prefix == null || prefix.isEmpty()) {
            prefix = "default";
        }
        return prefix.toLowerCase() + clazz.getSimpleName();
    }
}
