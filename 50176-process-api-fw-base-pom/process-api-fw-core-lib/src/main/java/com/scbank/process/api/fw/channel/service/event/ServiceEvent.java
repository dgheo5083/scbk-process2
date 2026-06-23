package com.scbank.process.api.fw.channel.service.event;

import org.springframework.context.ApplicationEvent;

public class ServiceEvent extends ApplicationEvent {

	private static final long serialVersionUID = 1L;

	public ServiceEvent(Object source) {
		super(source);
	}

}
