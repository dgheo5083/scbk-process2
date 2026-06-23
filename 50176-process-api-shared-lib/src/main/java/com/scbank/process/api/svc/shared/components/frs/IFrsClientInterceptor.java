package com.scbank.process.api.svc.shared.components.frs;

public interface IFrsClientInterceptor {
	void before(Object... logValues);
	void after(Object... logValues);
}
