package com.scbank.process.api.fw.batch.constants;

/**
 * 프레임워크 배치 상수 모음
 */
public class BatchConstants {

	public static final String BATCH_METADATA_JOB_KEY = "batch.metadata";
	public static final String BATCH_COMPONENT_ID = "batch.component.id";

	public static final String JOBDETAIL_NAME_SUFFIX = "-job";

	public static final String TRIGGER_NAME_SUFFIX = "-trigger";

	public static final String TRIGGER_PROP_START_DELAY = "startDelay";
	public static final String TRIGGER_PROP_REPEAT_INTERVAL = "repeatInterval";
	public static final String TRIGGER_PROP_REPEAT_COUNT = "repeatCount";
	public static final String TRIGGER_PROP_PRIORITY = "priority";
	public static final String TRIGGER_PROP_CRON_EXPRESSION = "cronExpression";
}
