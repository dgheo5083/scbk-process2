package com.scbank.process.api.fw.base.gateway.prc.base.simulation;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Generated unit test for {@link SimulationKeyResolver}.
 */
class SimulationKeyResolverTest {

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

        @RequestMapping(method = RequestMethod.GET, path = "/rm")
        void requestMappingWithMethod();

        @RequestMapping(path = "/rm2")
        void requestMappingWithoutMethod();

        void none();
    }

    private java.lang.reflect.Method method(String name) throws Exception {
        return Api.class.getMethod(name);
    }

    @Test
    void httpMethodResolvesFromAnnotations() throws Exception {
        assertThat(SimulationKeyResolver.httpMethod(method("get"))).isEqualTo("GET");
        assertThat(SimulationKeyResolver.httpMethod(method("post"))).isEqualTo("POST");
        assertThat(SimulationKeyResolver.httpMethod(method("put"))).isEqualTo("PUT");
        assertThat(SimulationKeyResolver.httpMethod(method("delete"))).isEqualTo("DELETE");
        assertThat(SimulationKeyResolver.httpMethod(method("patch"))).isEqualTo("PATCH");
        assertThat(SimulationKeyResolver.httpMethod(method("requestMappingWithMethod"))).isEqualTo("GET");
        assertThat(SimulationKeyResolver.httpMethod(method("requestMappingWithoutMethod"))).isEqualTo("POST");
        assertThat(SimulationKeyResolver.httpMethod(method("none"))).isEqualTo("POST");
    }

    @Test
    void pathResolvesFromAnnotations() throws Exception {
        assertThat(SimulationKeyResolver.path(method("get"))).isEqualTo("/g");
        assertThat(SimulationKeyResolver.path(method("post"))).isEqualTo("/p");
        assertThat(SimulationKeyResolver.path(method("put"))).isEqualTo("/");
        assertThat(SimulationKeyResolver.path(method("delete"))).isEqualTo("/d");
        assertThat(SimulationKeyResolver.path(method("patch"))).isEqualTo("/pa");
        assertThat(SimulationKeyResolver.path(method("none"))).isEqualTo("/");
    }

    @Test
    void toSafePathTokenNormalisesPaths() {
        assertThat(SimulationKeyResolver.toSafePathToken(null)).isEqualTo("_root_");
        assertThat(SimulationKeyResolver.toSafePathToken("/")).isEqualTo("_root_");
        assertThat(SimulationKeyResolver.toSafePathToken("/a/b/")).isEqualTo("a_b");
        assertThat(SimulationKeyResolver.toSafePathToken("a/b?x=1")).isEqualTo("a_b");
    }

    @Test
    void fileNameCombinesMethodAndToken() throws Exception {
        assertThat(SimulationKeyResolver.fileName(method("get"))).isEqualTo("get_g.json");
        assertThat(SimulationKeyResolver.fileName(method("none"))).isEqualTo("post__root_.json");
    }
}
