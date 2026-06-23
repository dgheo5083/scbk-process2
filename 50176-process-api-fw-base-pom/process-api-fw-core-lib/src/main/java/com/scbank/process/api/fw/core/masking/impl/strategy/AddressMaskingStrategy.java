package com.scbank.process.api.fw.core.masking.impl.strategy;

import com.scbank.process.api.fw.core.masking.AbstractMaskingStrategy;
import com.scbank.process.api.fw.core.utils.ByteUtils;

/**
 * 주소 마스킹 처리 Strategy
 */
public class AddressMaskingStrategy extends AbstractMaskingStrategy {

    @Override
    public byte[] mask(byte[] source, String charset) {
        if (source == null) {
            return null;
        }

        int offset = 0;
        if (source[0] == (byte) 0x0E && source[source.length - 1] == (byte) 0x0F) {
            byte[] dest = new byte[source.length - 2];
            System.arraycopy(source, 1, dest, 0, dest.length - 1);
            offset = dest.length > 40 ? dest.length - 20 : dest.length - 10;
            for (int i = offset; i < dest.length; i++) {
                if (i % 2 == 0) {
                    dest[i] = (byte) 0x42;
                } else {
                    dest[i] = (byte) 0x5c;
                }
            }
            return ByteUtils.addSOSI(dest, 0);
        } else {
            offset = source.length > 20 ? source.length - 10 : source.length - 5;
            return this.mask(source, offset, charset);
        }

    }
}
