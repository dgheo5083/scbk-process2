package com.scbank.process.api.fw.core.masking.impl.strategy;

import com.scbank.process.api.fw.core.masking.AbstractMaskingStrategy;

/**
 * 이름 마스킹 처리 Strategy
 */
public class NameMaskingStrategy extends AbstractMaskingStrategy {

    @Override
    public byte[] mask(byte[] source, String charset) {
        return this.defaultMasking(source, charset);
    }
}
