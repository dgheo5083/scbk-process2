package com.scbank.process.api.fw.channel.constants;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * ChannelConstants Test Class
 */
class ChannelConstantsTest {

    @Nested
    @DisplayName("Constant values tests")
    class ConstantValuesTests {

        @Test
        @DisplayName("Should have correct FRAMEWORK_CHANNEL_PREFIX value")
        void shouldHaveCorrectFrameworkChannelPrefix() {
            assertEquals("csl.channel", ChannelConstants.FRAMEWORK_CHANNEL_PREFIX);
        }

        @Test
        @DisplayName("Should have correct FRAMEWORK_CHANNEL_ENABLED value")
        void shouldHaveCorrectFrameworkChannelEnabled() {
            assertEquals("csl.channel.enabled", ChannelConstants.FRAMEWORK_CHANNEL_ENABLED);
        }

        @Test
        @DisplayName("Should have correct FRAMEWORK_CHANNEL_VALIDATION_ENABLED value")
        void shouldHaveCorrectFrameworkChannelValidationEnabled() {
            assertEquals("csl.channel.validation.enabled", ChannelConstants.FRAMEWORK_CHANNEL_VALIDATION_ENABLED);
        }

        @Test
        @DisplayName("Should have correct FRAMEWORK_CHANNEL_MESSAGESOURCE_ENABLED value")
        void shouldHaveCorrectFrameworkChannelMessagesourceEnabled() {
            assertEquals("csl.channel.message-source.enabled", ChannelConstants.FRAMEWORK_CHANNEL_MESSAGESOURCE_ENABLED);
        }
    }

    @Nested
    @DisplayName("Constant relationships tests")
    class ConstantRelationshipsTests {

        @Test
        @DisplayName("FRAMEWORK_CHANNEL_ENABLED should be based on FRAMEWORK_CHANNEL_PREFIX")
        void frameworkChannelEnabledShouldBeBasedOnPrefix() {
            assertTrue(ChannelConstants.FRAMEWORK_CHANNEL_ENABLED.startsWith(ChannelConstants.FRAMEWORK_CHANNEL_PREFIX));
        }

        @Test
        @DisplayName("FRAMEWORK_CHANNEL_VALIDATION_ENABLED should be based on FRAMEWORK_CHANNEL_PREFIX")
        void frameworkChannelValidationEnabledShouldBeBasedOnPrefix() {
            assertTrue(ChannelConstants.FRAMEWORK_CHANNEL_VALIDATION_ENABLED.startsWith(ChannelConstants.FRAMEWORK_CHANNEL_PREFIX));
        }

        @Test
        @DisplayName("FRAMEWORK_CHANNEL_MESSAGESOURCE_ENABLED should be based on FRAMEWORK_CHANNEL_PREFIX")
        void frameworkChannelMessagesourceEnabledShouldBeBasedOnPrefix() {
            assertTrue(ChannelConstants.FRAMEWORK_CHANNEL_MESSAGESOURCE_ENABLED.startsWith(ChannelConstants.FRAMEWORK_CHANNEL_PREFIX));
        }
    }
}
