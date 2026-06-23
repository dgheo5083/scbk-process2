package com.scbank.process.api.fw.common.code;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class ICodeItemInfoTest {

	@BeforeEach
	void setUp() throws Exception {
	} 

	@Test
	void compareTo_should_compare_by_order() {
		ICodeItemInfo a = item(1);
		ICodeItemInfo b = item(2);
		
		assertTrue(a.compareTo(b) < 0);
		assertTrue(b.compareTo(a) > 0);
		assertEquals(0, a.compareTo(item(1)));
	}
	
	private ICodeItemInfo item(int order) {
		ICodeItemInfo a = new ICodeItemInfo() {
			
			@Override
			public String getValue() {
				return "a";
			}
			
			@Override
			public int getOrder() {
				return order;
			}
			
			@Override
			public String getKey() {
				return "b";
			}
		};
		return a;
	}
}
