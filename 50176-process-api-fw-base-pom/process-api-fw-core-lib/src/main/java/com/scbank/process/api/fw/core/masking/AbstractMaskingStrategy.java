package com.scbank.process.api.fw.core.masking;

import com.scbank.process.api.fw.core.utils.StringUtils;

public abstract class AbstractMaskingStrategy implements IMaskingStrategy {

    protected static final String HOST_ENCODING = "cp933";

    /**
     * 
     * @param source
     * @param startIndex
     * @param endIndex
     * @return
     */
    protected byte[] mask(byte[] source, int startIndex, int endIndex, String charset) {
        byte[] masked = new byte[source.length];
        for (int i = 0; i < source.length; i++) {
            if (i >= startIndex && i < endIndex) {
                if (HOST_ENCODING.equals(charset)) {
                    if (source[i] == 0x0E || source[i] == 0x0F) {
                        masked[i] = source[i];
                    } else if (source[i] != 0x40 && source[i] != 0x00) {
                        masked[i] = (byte) 0x5c;
                    } else {
                        masked[i] = source[i];
                    }
                } else {
                    if (source[i] != 0x20 && source[i] != 0x00) {
                        masked[i] = (byte) '*';
                    } else {
                        masked[i] = 0x20;
                    }
                }
            } else {
                masked[i] = source[i];
            }
        }
        return masked;
    }

    /**
     * 
     * @param source
     * @param maskOffset
     * @return
     */
    protected byte[] mask(byte[] source, int maskOffset, String charset) {
        try {
            String trimStr = new String(source, charset);
            byte[] tempBytes = new byte[source.length];

            byte[] trimBytes = trimStr.getBytes(charset);
            byte[] masked = this.mask(trimBytes, maskOffset, trimBytes.length, charset);
            System.arraycopy(masked, 0, tempBytes, 0, masked.length);
            return tempBytes;
        } catch (Exception e) {
            return source;
        }
    }

    /**
     * 타입이 없는 경우 full masking 처리를 한다.
     * 
     * @param source
     * @return
     */
    protected byte[] defaultMasking(byte[] source, String charset) {
        if (source == null) {
            return null;
        }

        byte spaceByte = "cp933".equals(charset) ? (byte) 0x40 : (byte) 0x20;
        try {
            byte[] masked = new byte[source.length];
            for (int i = 0; i < source.length; i++) {
                if (source[i] != spaceByte && source[i] != 0x00) {
                    masked[i] = (byte) "*".getBytes(charset)[0];
                } else {
                    masked[i] = StringUtils.SPACE.getBytes(charset)[0];
                }
            }
            return masked;
        } catch (Exception e) {
            return source;
        }
    }

}
