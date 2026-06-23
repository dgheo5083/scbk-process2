package com.scbank.process.api.fw.channel.service.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.scbank.process.api.fw.core.utils.StringUtils;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ServiceEndpoint {

	/**
	 * 서비스 endpoint 경로
	 * 
	 * @return
	 */
	String url();

	String name();

	/**
	 * 서비스 endpoint 설명
	 * 
	 * @return
	 */
	String description() default StringUtils.EMPTY;

	/**
	 * 작성자
	 * 
	 * @return
	 */
	String author() default StringUtils.EMPTY;

	/**
	 * 서비스 파라미터 배열
	 * 
	 * @return
	 */
	Parameter[] parameters() default {};

	/**
	 * 접근제어
	 * 
	 * @return
	 */
	AccessControl accessControl() default @AccessControl;

	/**
	 * 서비스이용시간
	 * 
	 * @return
	 */
	ServiceTime serviceTime() default @ServiceTime;

	/**
	 * 서비스 인터셉터 목록
	 * 
	 * @return
	 */
	Interceptor[] interceptors() default {};

	@Target({})
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface Parameter {
		/**
		 * 파라미터명
		 * 
		 * @return
		 */
		String name();

		/**
		 * 파라미터값
		 * 
		 * @return
		 */
		String value();
	}

	@Target({})
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface AccessControl {

		/**
		 * 로그인 여부
		 * 
		 * @return
		 */
		boolean requiredLogin() default false;

		/**
		 * 허용가능한 채널 목록
		 * 
		 * @return
		 */
		String[] channels() default {};
	}

	@Target({})
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface ServiceTime {

		boolean enabled() default false;

		TimeRange businessDay() default @TimeRange;

		TimeRange holiday() default @TimeRange;

		@Target({})
		@Retention(RetentionPolicy.RUNTIME)
		public static @interface TimeRange {

			String startTime() default "";

			String endTime() default "";
		}
	}

	@Target({})
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface Interceptor {
		String value();
	}
}
