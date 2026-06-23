package com.scbank.process.api.fw.core.masking.impl.strategy;

import com.scbank.process.api.fw.core.masking.AbstractMaskingStrategy;

/**
 * 연락처 마스킹 처리(전화번호, 휴대전화번호) Strategy
 */
public class PhoneMaskingStrategy extends AbstractMaskingStrategy {

    @Override
    public byte[] mask(byte[] source, String charset) {
        if (source == null) {
            return null;
        }

        try {
            if (source.length > 4) {
                int maskingOffset = source.length - 4;
                return this.mask(source, maskingOffset, charset);
            } else if (source.length == 4 || source.length < 4) {
                return defaultMasking(source, charset);
            } else {
                return defaultMasking(source, charset);
            }
        } catch (Exception e) {
            return this.defaultMasking(source, charset);
        }

    }
}
