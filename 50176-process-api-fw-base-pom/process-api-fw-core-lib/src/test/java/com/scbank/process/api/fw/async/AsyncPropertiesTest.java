package com.scbank.process.api.fw.async;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.async.AsyncProperties.ThreadPoolProperties;
import com.scbank.process.api.fw.async.AsyncProperties.ThreadPoolProperties.Pool;
import com.scbank.process.api.fw.async.AsyncProperties.ThreadPoolProperties.RejectionPolicy;
import com.scbank.process.api.fw.async.AsyncProperties.ThreadPoolProperties.Shutdown;

/**
 * {@link AsyncProperties} 및 중첩 클래스 단위 테스트.
 */
class AsyncPropertiesTest {

    @Test
    @DisplayName("AsyncProperties 기본값 및 getter/setter")
    void asyncPropertiesGetterSetter() {
        AsyncProperties properties = new AsyncProperties();
        assertThat(properties.isEnabled()).isFalse();
        assertThat(properties.getExecutors()).isEmpty();

        ThreadPoolProperties pool = new ThreadPoolProperties();
        properties.setEnabled(true);
        properties.setExecutors(Map.of("default", pool));

        assertThat(properties.isEnabled()).isTrue();
        assertThat(properties.getExecutors()).containsEntry("default", pool);
    }

    @Test
    @DisplayName("ThreadPoolProperties 기본값 검증")
    void threadPoolPropertiesDefaults() {
        ThreadPoolProperties props = new ThreadPoolProperties();

        assertThat(props.getPool()).isNotNull();
        assertThat(props.getShutdown()).isNotNull();
        assertThat(props.getRejectionPolicy()).isEqualTo(RejectionPolicy.DISCARD);
    }

    @Test
    @DisplayName("ThreadPoolProperties 전체 인자 생성자")
    void threadPoolPropertiesAllArgs() {
        Pool pool = new Pool();
        Shutdown shutdown = new Shutdown();
        ThreadPoolProperties props = new ThreadPoolProperties(pool, shutdown, RejectionPolicy.ABORT);

        assertThat(props.getPool()).isSameAs(pool);
        assertThat(props.getShutdown()).isSameAs(shutdown);
        assertThat(props.getRejectionPolicy()).isEqualTo(RejectionPolicy.ABORT);
    }

    @Test
    @DisplayName("Pool 기본값 검증")
    void poolDefaults() {
        Pool pool = new Pool();

        assertThat(pool.getCoreSize()).isEqualTo(2);
        assertThat(pool.getMaxSize()).isEqualTo(8);
        assertThat(pool.getQueueCapacity()).isEqualTo(5000);
        assertThat(pool.getKeepAliveSeconds()).isEqualTo(60);
        assertThat(pool.isAllowCoreThreadTimeOut()).isTrue();
        assertThat(pool.getThreadNamePrefix()).isEqualTo("async-");
    }

    @Test
    @DisplayName("Pool 전체 인자 생성자 및 setter")
    void poolAllArgsAndSetters() {
        Pool pool = new Pool(4, 16, 100, 30, false, "worker-");

        assertThat(pool.getCoreSize()).isEqualTo(4);
        assertThat(pool.getMaxSize()).isEqualTo(16);
        assertThat(pool.getQueueCapacity()).isEqualTo(100);
        assertThat(pool.getKeepAliveSeconds()).isEqualTo(30);
        assertThat(pool.isAllowCoreThreadTimeOut()).isFalse();
        assertThat(pool.getThreadNamePrefix()).isEqualTo("worker-");

        pool.setCoreSize(5);
        assertThat(pool.getCoreSize()).isEqualTo(5);
    }

    @Test
    @DisplayName("Shutdown 기본값 및 전체 인자 생성자")
    void shutdownDefaultsAndAllArgs() {
        Shutdown defaults = new Shutdown();
        assertThat(defaults.isWaitForTasksCompleteOnShutdown()).isTrue();
        assertThat(defaults.getAwaitTerminationSeconds()).isEqualTo(10);

        Shutdown custom = new Shutdown(false, 30);
        assertThat(custom.isWaitForTasksCompleteOnShutdown()).isFalse();
        assertThat(custom.getAwaitTerminationSeconds()).isEqualTo(30);
    }

    @Test
    @DisplayName("RejectionPolicy enum 값 검증")
    void rejectionPolicyValues() {
        assertThat(RejectionPolicy.values())
                .containsExactly(RejectionPolicy.DISCARD, RejectionPolicy.DISCARD_OLDEST,
                        RejectionPolicy.CALLER_RUNS, RejectionPolicy.ABORT);
        assertThat(RejectionPolicy.valueOf("CALLER_RUNS")).isEqualTo(RejectionPolicy.CALLER_RUNS);
    }
}
