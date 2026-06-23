package com.scbank.process.api.fw.core.masking.impl.strategy;

import com.scbank.process.api.fw.core.masking.AbstractMaskingStrategy;

/**
 * 카드번호 마스킹 처리 Strategy
 */
public class CardNumberMaskingStrategy extends AbstractMaskingStrategy {

    @Override
    public byte[] mask(byte[] source, String charset) {
        if (source == null) {
            return null;
        }

        try {
            byte[] masked = new byte[source.length];
            if (source.length == 16) {
                int maskingOffset = source.length - 8;
                return this.mask(source, maskingOffset, charset);
            }
            return masked;
        } catch (Exception e) {
            return this.defaultMasking(source, charset);
        }
    }
}
