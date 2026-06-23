package com.scbank.process.api.fw.base.integration.log;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;

/**
 * Generated unit test for {@link IntegrationLogEventPublisher}.
 */
class IntegrationLogEventPublisherTest {

    private final ApplicationEventPublisher publisher = mock(ApplicationEventPublisher.class);
    private final IntegrationLogEventPublisher logPublisher = new IntegrationLogEventPublisher(publisher);

    @Test
    void publishesLogEvent() {
        IntegrationLogEvent event = IntegrationLogEvent.builder().build();
        logPublisher.publish(event);
        verify(publisher).publishEvent(event);
    }

    @Test
    void publishesCollectEvent() {
        IntegrationLogCollectEvent event = IntegrationLogCollectEvent.builder().build();
        logPublisher.publish(event);
        verify(publisher).publishEvent(event);
    }
}
