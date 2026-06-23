package com.scbank.process.api.fw.batch.listener;

import org.quartz.listeners.TriggerListenerSupport;

public class BatchTriggerListener extends TriggerListenerSupport {

	@Override
	public String getName() {
		return BatchTriggerListener.class.getSimpleName();
	}
}
