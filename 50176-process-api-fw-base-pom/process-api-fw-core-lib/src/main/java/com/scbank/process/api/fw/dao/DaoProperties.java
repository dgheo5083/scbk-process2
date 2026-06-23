package com.scbank.process.api.fw.dao;

import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sungdon.choi
 */
@Data
@ConfigurationProperties(prefix = "csl.dao")
public class DaoProperties {

    /**
     * 활성화 여부
     */
    private boolean enabled;

    /**
     * 데이터베이스별 설정
     */
    private Map<String, Database> database;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Database {
    	private String vendor;
    	private DatasourceConfig datasource; 
		private MybatisConfig mybatisConfig;        
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MybatisConfig {

    	private String configLocation;
    	private List<String> mapperLocation;
    	private List<String> mapperScanBasePackages;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DatasourceConfig {
        String url;
        String driverClassName;
        String username;
        String password;
        Integer maximumPoolSize;
        Integer minimumIdle;
        Long connectionTimeoutMs;
        Long idleTimeoutMs;
        Long maxLifetimeMs;
        Boolean autoCommit;
        Boolean readOnly;
        String connectionTestQuery;
    }
}
