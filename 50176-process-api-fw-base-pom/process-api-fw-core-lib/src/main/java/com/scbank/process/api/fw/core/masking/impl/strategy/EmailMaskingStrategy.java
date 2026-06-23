package com.scbank.process.api.fw.core.masking.impl.strategy;

import com.scbank.process.api.fw.core.masking.AbstractMaskingStrategy;

public class EmailMaskingStrategy extends AbstractMaskingStrategy {

    @Override
    public byte[] mask(byte[] source, String charset) {
        if (source == null) {
            return null;
        }

        try {
            if (source.length > 4) {
                String tmp = new String(source, charset);
                String[] arr = tmp.split("\\@");
                if (arr.length == 2) {
                    String arr1 = arr[0];
                    String arr2 = arr[1];

                    int startIndex = 0;
                    byte[] idBytes = arr1.getBytes(charset);
                    int endIndex = (idBytes.length > 4 ? 4 : idBytes.length - 1);
                    byte[] bytes1 = this.mask(idBytes, startIndex, endIndex, charset);
                    byte[] bytes2 = "@".getBytes(charset);
                    byte[] bytes3 = arr2.getBytes(charset);

                    byte[] result = new byte[bytes1.length + bytes2.length + bytes3.length];
                    int offset = 0;
                    System.arraycopy(bytes1, 0, result, offset, bytes1.length);
                    offset += bytes1.length;

                    System.arraycopy(bytes2, 0, result, offset, bytes2.length);
                    offset += bytes2.length;

                    System.arraycopy(bytes3, 0, result, offset, bytes3.length);

                    return result;
                } else {
                    return this.defaultMasking(source, charset);
                }
            } else {
                return this.defaultMasking(source, charset);
            }
        } catch (Exception e) {
            return this.defaultMasking(source, charset);
        }

    }
}
