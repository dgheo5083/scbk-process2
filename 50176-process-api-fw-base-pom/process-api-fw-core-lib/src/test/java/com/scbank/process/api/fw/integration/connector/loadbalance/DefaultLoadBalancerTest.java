package com.scbank.process.api.fw.integration.connector.loadbalance;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.integration.IntegrationProperties.Endpoint;

class DefaultLoadBalancerTest {

	@Test
	void next_delegatesToStrategyAndReturnsSelectEndpoint() {
		Endpoint ep1 = mock(Endpoint.class);
		Endpoint ep2 = mock(Endpoint.class);
		
		List<Endpoint> targets = List.of(ep1, ep2);
		
		LoadBalanceStrategy strategy = mock(LoadBalanceStrategy.class);
		when(strategy.select(targets)).thenReturn(ep2);
		
		DefaultLoadBalancer lb = new DefaultLoadBalancer(targets, strategy);
		
		Endpoint selected = lb.next();
		
		assertSame(ep2, selected);
		verify(strategy, times(1)).select(targets);
		verifyNoMoreInteractions(strategy);
	}
	
	@Test
	void next_passesEmptyTargetsToStrategy() {
		List<Endpoint> targets = List.of();
		
		LoadBalanceStrategy strategy = mock(LoadBalanceStrategy.class);
		Endpoint expected = mock(Endpoint.class);
		when(strategy.select(targets)).thenReturn(expected);
		
		DefaultLoadBalancer lb = new DefaultLoadBalancer(targets, strategy);
		
		Endpoint selected = lb.next();
		
		assertSame(expected, selected);
		verify(strategy).select(targets);
	}
	
	@Test
	void next_propagatesStrategyException() {
		Endpoint ep1 = mock(Endpoint.class);
		List<Endpoint> targets = List.of(ep1);
		
		LoadBalanceStrategy strategy = mock(LoadBalanceStrategy.class);
		RuntimeException ex = new RuntimeException("boom");
		when(strategy.select(targets)).thenThrow(ex);
		
		DefaultLoadBalancer lb = new DefaultLoadBalancer(targets, strategy);
		
		RuntimeException thrown = assertThrows(RuntimeException.class, lb::next);
		assertSame(ex, thrown);
		verify(strategy).select(targets);
	}
}
