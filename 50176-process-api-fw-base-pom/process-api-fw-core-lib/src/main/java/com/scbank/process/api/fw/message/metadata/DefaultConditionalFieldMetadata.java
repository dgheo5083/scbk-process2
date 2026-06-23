package com.scbank.process.api.fw.message.metadata;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class DefaultConditionalFieldMetadata extends DefaultMessageFieldMetadata implements IConditionalFieldMetadata {

    private static final long serialVersionUID = 1L;

    /**
     * 조건부 spEL 문자열
     */
    private String condition;
}
