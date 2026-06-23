package com.scbank.process.api.fw.core.masking.impl.strategy;

import com.scbank.process.api.fw.core.masking.AbstractMaskingStrategy;

/**
 * 고유식별번호 마스킹 처리(주민번호/사업자번호/사용자ID) Strategy
 */
public class RegNoMaskingStrategy extends AbstractMaskingStrategy {

    @Override
    public byte[] mask(byte[] source, String charset) {
        if (source == null) {
            return null;
        }

        // 주민번호 뒤 7자리가 오는 경우는 full masking 처리
        if (source.length == 7) {
            return this.defaultMasking(source, charset);
        }

        try {
            int totLen = new String(source, charset).length();
            String trimStr = new String(source, charset).trim();
            byte[] trimBytes = trimStr.getBytes(charset);
            int maskOffset = 0;
            int mLen = source.length;
            // 주민사업자번호 or 사용자ID
            if (source.length == 10) {
                maskOffset = trimBytes.length - 3;
            }

            //
            if (source.length == 13) {
                maskOffset = trimBytes.length - 7;
            }
            byte[] rtnBytes = this.mask(source, maskOffset, charset);
            if (totLen - mLen > 0) {
                byte[] rtn = new byte[totLen];
                byte[] rtn2 = new byte[totLen - mLen];
                for (int i = 0; i < rtn2.length; i++) {
                    rtn2[i] = HOST_ENCODING.equals(charset) ? (byte) 0x40 : (byte) 0x20;
                }
                System.arraycopy(rtnBytes, 0, rtn, 0, rtnBytes.length);
                System.arraycopy(rtn2, 0, rtn, rtnBytes.length, rtn2.length);
                return rtn;
            }
            return rtnBytes;
        } catch (Exception e) {
            return this.defaultMasking(source, charset);
        }

    }
}
