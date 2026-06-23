package com.scbank.process.api.fw.base.gateway.prc.base.invocation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.scbank.process.api.fw.core.log.trace.TraceSection;

import feign.Target;

/**
 * Generated unit test for {@link BaseInvocationHandlerFactory}.
 */
class BaseInvocationHandlerFactoryTest {

    /** Minimal concrete subclass exposing the protected helpers for testing. */
    static class TestFactory extends BaseInvocationHandlerFactory {
        @Override
        @SuppressWarnings("rawtypes")
        public InvocationHandler create(Target target, Map<Method, MethodHandler> dispatch) {
            return null;
        }

        String callPath(Method m) {
            return path(m);
        }

        void callTraces(Throwable ex) {
            beginTrace(TraceSection.PRC_CALL, "http://localhost/x");
            failTrace(ex);
            endTrace();
        }
    }

    interface Api {
        @GetMapping("/g")
        void get();

        @PostMapping(path = "/p")
        void post();

        @PutMapping
        void put();

        @DeleteMapping("/d")
        void delete();

        @PatchMapping("/pa")
        void patch();

        void none();
    }

    private final TestFactory factory = new TestFactory();

    @Test
    void pathResolvesFromMappingAnnotations() throws Exception {
        assertThat(factory.callPath(Api.class.getMethod("get"))).isEqualTo("/g");
        assertThat(factory.callPath(Api.class.getMethod("post"))).isEqualTo("/p");
        assertThat(factory.callPath(Api.class.getMethod("put"))).isEqualTo("/");
        assertThat(factory.callPath(Api.class.getMethod("delete"))).isEqualTo("/d");
        assertThat(factory.callPath(Api.class.getMethod("patch"))).isEqualTo("/pa");
        assertThat(factory.callPath(Api.class.getMethod("none"))).isEqualTo("/");
    }

    @Test
    void traceHelpersAreSafeWithoutTraceContext() {
        assertThatCode(() -> factory.callTraces(new RuntimeException("boom")))
                .doesNotThrowAnyException();
    }
}
