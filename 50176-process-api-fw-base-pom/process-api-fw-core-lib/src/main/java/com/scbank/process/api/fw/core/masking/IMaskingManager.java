package com.scbank.process.api.fw.core.masking;

import com.scbank.process.api.fw.core.masking.IMaskingStrategy.MaskingType;

public interface IMaskingManager {

    String BEAN_ID = "csl.core.IMaskingManager";

    byte[] apply(MaskingType type, byte[] source, String charset);

    String apply(MaskingType type, String source, String charset);
}
