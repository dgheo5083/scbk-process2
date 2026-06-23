package com.scbank.process.api.fw.core.exception;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.core.error.FrameworkErrorCode;

class FrameworkRuntimeExceptionTest {

	@Test
    void getMessage_returnsNull_whenErrorCodeAndMessageAreNull() {
		FrameworkRuntimeException ex = new FrameworkRuntimeException(FrameworkErrorCode.INTERNAL_ERROR.getCode());
    	assertEquals("[MACF9999]", ex.getMessage());
    }
    
    @Test
    void getMessage_returnsFormattedMessage_whenMessageAreExit() {
    	FrameworkRuntimeException ex = new FrameworkRuntimeException("E001", Arrays.asList("SCB", 3), null);
    	
    	ex.setErrorMessage("Bank {0} error count {1}");
    	
    	String message = ex.getMessage();
    	
    	assertEquals("[E001]Bank SCB error count 3", message);
    }
    
    @Test
    void getMessage_returnsMessageWithoutArgs_whenArgsEmpty() {
    	FrameworkRuntimeException ex = new FrameworkRuntimeException("E002", Collections.emptyList(), null);
    	
    	ex.setErrorMessage("Simple error");
    	
    	assertEquals("[E002]Simple error", ex.getMessage());
    }
    
    @Test
    void getMessage_returnsMessage_whenOnlyMessageExists() {
    	FrameworkRuntimeException ex = new FrameworkRuntimeException(FrameworkErrorCode.INTERNAL_ERROR.getCode(), "Only message");
    	assertEquals("Only message", ex.getErrorMessage());
    }
    
    @Test
    void addNextPageParameter_doesNothing_whenValueIsNull() {
    	FrameworkRuntimeException ex = new FrameworkRuntimeException(FrameworkErrorCode.INTERNAL_ERROR.getCode(), "test");
    	
    	ex.addNextPageParameter("page", 1);
    	
    	assertNotNull(ex.getNextPageParameters());
    	assertEquals(1, ex.getNextPageParameters().get("page"));
    }
    
    @Test
    void addNextPageParameter_addsToExistingMap() {
    	FrameworkRuntimeException ex = new FrameworkRuntimeException(FrameworkErrorCode.INTERNAL_ERROR.getCode(), "test");
    
    	ex.addNextPageParameter("page", 1);
    	ex.addNextPageParameter("size", 10);
    	
    	assertEquals(2, ex.getNextPageParameters().size());
    }

}
