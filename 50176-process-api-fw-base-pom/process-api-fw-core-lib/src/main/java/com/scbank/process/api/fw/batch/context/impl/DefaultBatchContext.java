package com.scbank.process.api.fw.batch.context.impl;

import java.util.Map;

import com.scbank.process.api.fw.batch.context.IBatchContext;
import com.scbank.process.api.fw.core.utils.StringUtils;

import lombok.Data;

/**
 * 프레임워크 배치 컨텍스트 구현 클래스
 */
@Data
public class DefaultBatchContext implements IBatchContext {

	private static final long serialVersionUID = 1L;

	private Map<String, String> initParameters;

	private Throwable cause;

	private boolean error;

	private String jobName;

	private String triggerName;

	private String instanceId;

	/**
	 * 생성자
	 * 
	 * @param jobName
	 * @param triggerName
	 * @param instanceId
	 */
	public DefaultBatchContext(String jobName, String triggerName, String instanceId,
			Map<String, String> initParameter) {
		this.jobName = jobName;
		this.triggerName = triggerName;
		this.instanceId = instanceId;
		this.initParameters = initParameter;
	}

	@Override
	public String getInitParameter(String name) {
		return this.getString(name, StringUtils.EMPTY);
	}

	@Override
	public String getString(String key, String defaultValue) {
		return this.initParameters.getOrDefault(key, defaultValue);
	}

	@Override
	public int getInt(String key, int defaultValue) {
		String value = getString(key, String.valueOf(defaultValue));
		return Integer.parseInt(value);
	}

	@Override
	public boolean getBoolean(String key, boolean defaultValue) {
		String value = getString(key, String.valueOf(defaultValue));
		return Boolean.parseBoolean(value);
	}

	@Override
	public String getJobName() {
		return this.jobName;
	}

	@Override
	public String getTriggerName() {
		return this.triggerName;
	}

	@Override
	public String getInstanceId() {
		return this.instanceId;
	}

	@Override
	public void setError(Throwable t) {
		this.cause = t;
		this.markAsFailed();
	}

	@Override
	public Throwable getError() {
		return this.cause;
	}

	@Override
	public void markAsFailed() {
		this.error = true;
	}

	@Override
	public boolean isFailed() {
		return this.error;
	}
}
