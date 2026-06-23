package com.scbank.process.api.fw.integration.client.options;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import feign.Request;

class FeignRequestOptionsContextTest {

	@AfterEach
	void tearDown() throws Exception {
		FeignRequestOptionsContext.clear();
	}

	@Test
	void get_whenNoSet_thenReturnsNull() {
		Request.Options result = FeignRequestOptionsContext.get();
		
		assertNull(result);
	}
	
	@Test
	void set_thenGet_returnsSameInstance() {
		Request.Options original = new Request.Options(1000, TimeUnit.MILLISECONDS, 2000, TimeUnit.MILLISECONDS, true);
		FeignRequestOptionsContext.set(original);
		
		Request.Options result = FeignRequestOptionsContext.get();
		assertEquals(original, result);
	}
	
	@Test
	void clear_afterSet_thenGetReturnNull() {
		FeignRequestOptionsContext.set(new Request.Options(1000, TimeUnit.MILLISECONDS, 2000, TimeUnit.MILLISECONDS, true));
		
		FeignRequestOptionsContext.clear();
		
		assertNull(FeignRequestOptionsContext.get());
	}
	
	@Test
	void set_twice_thenLatestWins() {
		Request.Options first = new Request.Options(1000, TimeUnit.MILLISECONDS, 2000, TimeUnit.MILLISECONDS, true);
		Request.Options second = new Request.Options(5000, TimeUnit.MILLISECONDS, 5000, TimeUnit.MILLISECONDS, false);
		
		FeignRequestOptionsContext.set(first);
		FeignRequestOptionsContext.set(second);
		
		assertSame(second, FeignRequestOptionsContext.get());
	}
	
	@Test
	void threadLocal_isIsolatedBetweenThreads() throws Exception {
		Request.Options mainOptions = new Request.Options(1000, TimeUnit.MILLISECONDS, 2000, TimeUnit.MILLISECONDS, true);
		FeignRequestOptionsContext.set(mainOptions);
		
		ExecutorService executor = Executors.newFixedThreadPool(1);
		try {
			Future<Request.Options> otherThreadValue = executor.submit(() -> {
				return FeignRequestOptionsContext.get();
			});
			
			assertNull(otherThreadValue.get(2, TimeUnit.SECONDS));
			
			assertSame(mainOptions, FeignRequestOptionsContext.get());
		} finally {
			executor.shutdown();
		}
	}
	
	@Test
	void clear_onlyAffectsCurrentThread() throws Exception {
		ExecutorService executor = Executors.newFixedThreadPool(1);
		try {
			Future<Void> setInOtherThread = executor.submit(() -> {
				FeignRequestOptionsContext.set(new Request.Options(1000, TimeUnit.MILLISECONDS, 2000, TimeUnit.MILLISECONDS, true));
				return null;
			});
			
			setInOtherThread.get(2, TimeUnit.SECONDS);
			
			FeignRequestOptionsContext.clear();
			
			Future<Request.Options> stillThere = executor.submit(() -> {
				return FeignRequestOptionsContext.get();
			});
			assertNotNull(stillThere.get(2, TimeUnit.SECONDS));
			
			executor.submit(() -> {
				FeignRequestOptionsContext.clear();
				return null;
			}).get(2, TimeUnit.SECONDS);
			
			Future<Request.Options> nowNull = executor.submit(FeignRequestOptionsContext::get);
			assertNull(nowNull.get(2, TimeUnit.SECONDS));
		} finally {
			executor.shutdown();
		}
	}
}
