package com.scbank.process.api.fw.dao.datasource;

import javax.sql.DataSource;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class DataSourceFactoryBean implements FactoryBean<DataSource>, EnvironmentAware {

	private Environment environment;

	private final String database;

	@Override
	public DataSource getObject() throws Exception {
		String prefix = "csl.dao.database." + this.database + ".datasource.";

		String url = require(prefix + "url");
		String username = require(prefix + "username");
		String credential = require(prefix + "credential");
		String driverClassName = require(prefix + "driver-class-name");

		HikariConfig cfg = new HikariConfig();
		cfg.setJdbcUrl(url);
		cfg.setUsername(username);
		cfg.setPassword(credential);
		cfg.setDriverClassName(driverClassName);

		Integer maximumPoolSize = getInt(prefix + "maximum-pool-size");
		Integer minimumIdle = getInt(prefix + "minimum-idle");
		Integer connectionTimeout = getInt(prefix + "connection-timeout-ms");
		Integer idleTimeout = getInt(prefix + "idle-timeout-ms");
		Long maxLifetime = getLong(prefix + "max-lifetime-ms");
		Boolean autoCommit = getBoolean(prefix + "auto-commit");
		Boolean readOnly = getBoolean(prefix + "read-only");
		String connectionTestQuery = environment.getProperty(prefix + "connection-test-query");

		if (maximumPoolSize != null) {
			cfg.setMaximumPoolSize(maximumPoolSize);
		}

		if (minimumIdle != null) {
			cfg.setMinimumIdle(minimumIdle);
		}

		if (connectionTimeout != null) {
			cfg.setConnectionTimeout(connectionTimeout);
		}

		if (idleTimeout != null) {
			cfg.setIdleTimeout(idleTimeout);
		}

		if (maxLifetime != null) {
			cfg.setMaxLifetime(maxLifetime);
		}

		if (autoCommit != null) {
			cfg.setAutoCommit(autoCommit);
		}

		if (readOnly != null) {
			cfg.setReadOnly(readOnly);
		}

		if (connectionTestQuery != null) {
			cfg.setConnectionTestQuery(connectionTestQuery);
		}

		log.info("# [DAO: {}] create HikariDataSource url={}", database, url);

		return new HikariDataSource(cfg);
	}

	@Override
	public Class<?> getObjectType() {
		return DataSource.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	private String require(String key) {
		String v = environment.getProperty(key);
		if (v == null || v.isBlank()) {
			throw new IllegalStateException("Missing required property: " + key);
		}
		return v;
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	private Integer getInt(String key) {
		String v = environment.getProperty(key);
		return (v == null || v.isBlank()) ? null : Integer.valueOf(v);
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	private Long getLong(String key) {
		String v = environment.getProperty(key);
		return (v == null || v.isBlank()) ? null : Long.valueOf(v);
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	private Boolean getBoolean(String key) {
		String v = environment.getProperty(key);
		return (v == null || v.isBlank()) ? null : Boolean.valueOf(v);
	}
}
