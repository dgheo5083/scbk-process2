package com.scbank.process.api.fw.async;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 프레임워크 비동기 설정
 */
@Data
@ConfigurationProperties(prefix = "csl.async")
public class AsyncProperties {
	
	/**
	 * 활성화여부
	 */
	private boolean enabled;

	/**
	 * 스레드풀 설정
	 */
	private Map<String, ThreadPoolProperties> executors = new HashMap<>();

	@Data
	@NoArgsConstructor
    @AllArgsConstructor
	public static class ThreadPoolProperties {

		private Pool pool = new Pool();
		private Shutdown shutdown = new Shutdown();
		private RejectionPolicy rejectionPolicy = RejectionPolicy.DISCARD;

		public static enum RejectionPolicy {
			DISCARD, DISCARD_OLDEST, CALLER_RUNS, ABORT;
		}

		@Data
		@NoArgsConstructor
	    @AllArgsConstructor
		public static class Pool {

			private int coreSize = 2;
			private int maxSize = 8;
			private int queueCapacity = 5000;
			private int keepAliveSeconds = 60;
			private boolean allowCoreThreadTimeOut = true;
			private String threadNamePrefix = "async-";
		}

		@Data
		@NoArgsConstructor
	    @AllArgsConstructor
		public static class Shutdown {
			private boolean waitForTasksCompleteOnShutdown = true;
			private int awaitTerminationSeconds = 10;
		}
	}
}
