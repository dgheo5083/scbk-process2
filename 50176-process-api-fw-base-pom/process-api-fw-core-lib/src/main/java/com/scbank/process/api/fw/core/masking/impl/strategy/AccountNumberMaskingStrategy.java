package com.scbank.process.api.fw.core.masking.impl.strategy;

import com.scbank.process.api.fw.core.masking.AbstractMaskingStrategy;

/**
 * 계좌번호(02) 마스킹 처리 Strategy
 */
public class AccountNumberMaskingStrategy extends AbstractMaskingStrategy {

    @Override
    public byte[] mask(byte[] source, String charset) {
        if (source == null) {
            return null;
        }

        int maskOffset = source.length - 6;
        return this.mask(source, maskOffset, charset);

    }
}
