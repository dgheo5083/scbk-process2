package com.scbank.process.api.fw.core.masking;

public interface IMaskingStrategy {

    /**
     * 
     * @param source
     * @return
     */
    byte[] mask(byte[] source, String charset);

    default String mask(String source, String charset) {
        try {
            return new String(this.mask(source.getBytes(charset), charset));
        } catch (Exception e) {
            return source;
        }
    }

    public static enum MaskingType {
        REGNO("01"), ACCOUNT("02"), PASSWORD("03"), NAME("04"), PHONE("05"), ADDRES("06"), EMAIL("07"), CARD("08"),
        DEFAULT("99");

        private String code;

        MaskingType(String code) {
            this.code = code;
        }

        public String getCode() {
            return this.code;
        }

        public static MaskingType from(String code) {
            for (MaskingType t : values()) {
                if (t.code.equals(code)) {
                    return t;
                }
            }
            return DEFAULT;
        }
    }
}
