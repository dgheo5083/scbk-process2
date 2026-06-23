package com.scbank.process.api.fw.core.log.trace;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TraceContextTest {

	@Test
	void constructor_should_create_root() {
		
		TraceContext context = new TraceContext("ROOT");
		
		assertNotNull(context.getCurrent());
	}
	
	@Test
	void begin_should_create_child_node() {
		
		TraceContext context = new TraceContext("ROOT");
		TraceNode root = context.getCurrent();
		
		context.begin("SVC", TraceSection.SVC);
		
		TraceNode child = context.getCurrent();
		
		assertNotNull(child);
		assertNotSame(root, child);
		assertEquals(root, child.getParent());
	}
	
	@Test
	void end_should_move_to_parent() {
		TraceContext context = new TraceContext("ROOT");
		
		TraceNode root = context.getCurrent();
		
		context.begin("SVC", TraceSection.SVC);
		
		context.end();
		
		assertSame(root, context.getCurrent());
	}
	
	@Test
	void fail_should_mark_current_node() {
		TraceContext context = new TraceContext("ROOT");
		
		context.fail(new RuntimeException("TEST"));
		
		assertNotNull(context.getCurrent());
	}
}
