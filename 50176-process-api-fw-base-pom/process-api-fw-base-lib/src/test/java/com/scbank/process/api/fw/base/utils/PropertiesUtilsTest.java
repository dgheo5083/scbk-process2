package com.scbank.process.api.fw.base.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.scbank.process.api.fw.common.property.IPropertyManager;

/**
 * Generated unit test for {@link PropertiesUtils}.
 */
class PropertiesUtilsTest {

    private IPropertyManager propertyManager;

    @BeforeEach
    void setUp() {
        propertyManager = mock(IPropertyManager.class);
        ReflectionTestUtils.setField(PropertiesUtils.class, "propertyManager", propertyManager);
        lenient().when(propertyManager.getString(eq("key"), eq(""))).thenReturn("value");
        lenient().when(propertyManager.getString(eq("key"), eq("def"))).thenReturn("value2");
        lenient().when(propertyManager.getInt(eq("num"), eq(0))).thenReturn(7);
        lenient().when(propertyManager.getInt(eq("num"), eq(5))).thenReturn(9);
    }

    @Test
    void getStringWithDefaultEmpty() {
        assertThat(PropertiesUtils.getString("key")).isEqualTo("value");
    }

    @Test
    void getStringWithDefaultValue() {
        assertThat(PropertiesUtils.getString("key", "def")).isEqualTo("value2");
    }

    @Test
    void getIntWithDefaultZero() {
        assertThat(PropertiesUtils.getInt("num")).isEqualTo(7);
    }

    @Test
    void getIntWithDefaultValue() {
        assertThat(PropertiesUtils.getInt("num", 5)).isEqualTo(9);
    }
}
