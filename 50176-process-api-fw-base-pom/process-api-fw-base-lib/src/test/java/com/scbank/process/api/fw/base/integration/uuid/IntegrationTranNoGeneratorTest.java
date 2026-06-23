package com.scbank.process.api.fw.base.integration.uuid;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.core.uuid.sequence.ISequenceGenerator;

/**
 * Generated unit test for {@link IntegrationTranNoGenerator}.
 */
class IntegrationTranNoGeneratorTest {

    private final ISequenceGenerator sequenceGenerator = mock(ISequenceGenerator.class);

    @Test
    void generateIdUsesSequenceWithDefaultSize() {
        when(sequenceGenerator.next()).thenReturn(123L);
        IntegrationTranNoGenerator generator = new IntegrationTranNoGenerator(sequenceGenerator);

        String id = generator.generateId();

        assertThat(id).isNotNull();
        verify(sequenceGenerator).next();
    }

    @Test
    void generateIdUsesSequenceWithCustomSize() {
        when(sequenceGenerator.next()).thenReturn(7L);
        IntegrationTranNoGenerator generator = new IntegrationTranNoGenerator(sequenceGenerator, 8);

        String id = generator.generateId();

        assertThat(id).isNotNull();
    }
}
