package com.scbank.process.api.fw.integration.connector.loadbalance.strategy;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.core.enums.CenterMode;
import com.scbank.process.api.fw.integration.IntegrationProperties.Endpoint;

class RoundRobinStrategyTest {

	@Test
	void empty() {
		RoundRobinStrategy stra = new RoundRobinStrategy();

		assertThrows(IllegalArgumentException.class, () -> stra.select(List.of()));
	}

	@Test
	void selectOrder() {
		RoundRobinStrategy stra = new RoundRobinStrategy();

		Endpoint e1 = new Endpoint(CenterMode.MAIN, "10.0.0.1", 8080, null);
		Endpoint e2 = new Endpoint(CenterMode.MAIN, "10.0.0.1", 8080, null);
		Endpoint e3 = new Endpoint(CenterMode.MAIN, "10.0.0.1", 8080, null);

		List<Endpoint> points = List.of(e1, e2, e3);

		assertSame(e1, stra.select(points));
		assertSame(e2, stra.select(points));
		assertSame(e3, stra.select(points));
		assertSame(e1, stra.select(points));
		assertSame(e2, stra.select(points));
	}

	@Test
	void alwaysSame() {
		RoundRobinStrategy stra = new RoundRobinStrategy();

		Endpoint e1 = new Endpoint(CenterMode.MAIN, "127.0.0.1", 8080, null);

		List<Endpoint> points = List.of(e1);

		assertSame(e1, stra.select(points));
		assertSame(e1, stra.select(points));
		assertSame(e1, stra.select(points));
	}

}
