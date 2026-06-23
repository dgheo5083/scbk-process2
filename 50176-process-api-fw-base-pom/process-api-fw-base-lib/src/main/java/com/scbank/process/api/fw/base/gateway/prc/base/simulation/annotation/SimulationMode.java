package com.scbank.process.api.fw.base.gateway.prc.base.simulation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * CSL 게이트웨이 시뮬레이션 모드 어노테이션
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SimulationMode {

	/**
	 * 시뮬레이션 시나리오
	 * @return
	 */
	String senario() default "success";
}
