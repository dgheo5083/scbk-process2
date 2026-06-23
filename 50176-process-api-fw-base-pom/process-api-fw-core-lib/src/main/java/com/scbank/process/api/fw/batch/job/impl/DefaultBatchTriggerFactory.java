package com.scbank.process.api.fw.batch.job.impl;

import java.util.Date;

import org.quartz.CronScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

import com.scbank.process.api.fw.batch.constants.BatchConstants;
import com.scbank.process.api.fw.batch.exception.BatchJobException;
import com.scbank.process.api.fw.batch.job.IBatchTriggerFactory;
import com.scbank.process.api.fw.batch.metadata.IBatchMetadata;
import com.scbank.process.api.fw.batch.metadata.IBatchTriggerMetadata;
import com.scbank.process.api.fw.batch.metadata.IBatchTriggerMetadata.TriggerType;
import com.scbank.process.api.fw.core.utils.StringUtils;

/**
 * 프레임워크 배치 쿼츠 트리거 생성 팩토리 구현 클래스
 */
public class DefaultBatchTriggerFactory implements IBatchTriggerFactory {

	@Override
	public Trigger createTrigger(IBatchMetadata metadata) throws BatchJobException {

		IBatchTriggerMetadata triggerMetadata = metadata.getBatchTriggerMetadata();
		TriggerType triggerType = triggerMetadata.getTriggerType();
		if (triggerType == null) {
			throw new BatchJobException("", "지원하지 않는 배치 트리거 타입입니다.");
		}

		String triggerName = this.getTriggerName(metadata);

		Trigger trigger = null;
		switch (triggerType) {
			case SIMPLE:
				trigger = this.createSimpleTrigger(triggerName, triggerMetadata);
				break;
			case CRON:
				trigger = this.createCronTrigger(triggerName, triggerMetadata);
				break;
			default:
				throw new BatchJobException("", "지원하지 않는 배치 트리거 타입입니다. [" + triggerType + "]");
		}

		return trigger;
	}

	/**
	 * 배치 트리거명을 획득한다.
	 * 
	 * @param metadata 프레임워크 배치정보 메타데이터
	 * @return
	 */
	private String getTriggerName(IBatchMetadata metadata) {
		return metadata.getId() + "" + BatchConstants.TRIGGER_NAME_SUFFIX;
	}

	/**
	 * 
	 * @param triggerName
	 * @param triggerMetadata
	 * @return
	 * @throws BatchJobException
	 */
	private Trigger createSimpleTrigger(String triggerName, IBatchTriggerMetadata triggerMetadata)
			throws BatchJobException {
		String startDelayStr = StringUtils
				.defaultIfBlank(triggerMetadata.getProperty(BatchConstants.TRIGGER_PROP_START_DELAY), "0");
		long startDelay = Long.parseLong(startDelayStr);
		Date startTime = (startDelay > 0) ? new Date(System.currentTimeMillis() + startDelay)
				: new Date(System.currentTimeMillis());

		long repeatInterval = Long.parseLong(StringUtils
				.defaultIfEmpty(triggerMetadata.getProperty(BatchConstants.TRIGGER_PROP_REPEAT_INTERVAL), "-1"));
		if (repeatInterval <= 0) {
			throw new BatchJobException("", "올바르지 않는 배치 트리거 정보 [repeatInterval][" + repeatInterval + "] ");
		} // end if

		int repeatCount = Integer.parseInt(StringUtils
				.defaultIfEmpty(triggerMetadata.getProperty(BatchConstants.TRIGGER_PROP_REPEAT_COUNT), "-1"));
		if (repeatCount <= 0) {
			throw new BatchJobException("", "올바르지 않는 배치 트리거 정보 [repeatCount][" + repeatCount + "] ");
		} // end if

		return TriggerBuilder.newTrigger()
				.withIdentity(triggerName, Scheduler.DEFAULT_GROUP)
				.withDescription(triggerName)
				.startAt(startTime)
				.withSchedule(SimpleScheduleBuilder.simpleSchedule()
						.withIntervalInMilliseconds(repeatInterval)
						.withRepeatCount(repeatCount))
				.build();
	}

	/**
	 * CRON 트리거 객체 생성
	 * 
	 * @param triggerName     트리거명
	 * @param triggerMetadata 프레임워크 트리거정보 메타데이터
	 * @return
	 * @throws BatchJobException 트리거 객체 생성 도중 발생한 예외
	 */
	private Trigger createCronTrigger(String triggerName, IBatchTriggerMetadata triggerMetadata)
			throws BatchJobException {
		String startDelayStr = StringUtils
				.defaultIfBlank(triggerMetadata.getProperty(BatchConstants.TRIGGER_PROP_START_DELAY), "0");
		long startDelay = Long.parseLong(startDelayStr);
		Date startTime = (startDelay > 0) ? new Date(System.currentTimeMillis() + startDelay)
				: new Date(System.currentTimeMillis());

		String cronExpression = StringUtils
				.defaultIfEmpty(triggerMetadata.getProperty(BatchConstants.TRIGGER_PROP_CRON_EXPRESSION), "");
		if (StringUtils.isEmpty(cronExpression)) {
			throw new BatchJobException("", "올바르지 않는 배치 트리거 정보 [cronExpression][" + cronExpression + "] ");
		}

		return TriggerBuilder.newTrigger()
				.withIdentity(triggerName, Scheduler.DEFAULT_GROUP)
				.withDescription(triggerName)
				.startAt(startTime)
				.withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
				.build();
	}
}
