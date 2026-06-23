/**
 * 
 */
package com.scbank.process.api.fw.common.code;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * 
 */
class ICodeInfoTest {

	@Test
	void getCodeItemList_should_return_empty() {
		ICodeInfo info = new ICodeInfo() {
			
			@Override
			public String getLocale() {
				return "ko_KR";
			}
			
			@Override
			public String getKey() {
				return "TEST";
			}
		};
		
		List<ICodeItemInfo> result = info.getCodeItemList();
		
		assertNotNull(result);
		assertTrue(result.isEmpty());
	}

}
