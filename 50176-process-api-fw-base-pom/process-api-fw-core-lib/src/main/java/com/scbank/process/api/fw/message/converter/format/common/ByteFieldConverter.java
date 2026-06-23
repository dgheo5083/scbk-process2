package com.scbank.process.api.fw.message.converter.format.common;

import com.scbank.process.api.fw.core.utils.ByteUtils;
import com.scbank.process.api.fw.message.context.MessageContext;
import com.scbank.process.api.fw.message.metadata.IMessageFieldMetadata;

/**
 * 바이트 필드 변환기 클래스
 * 
 * @author sungdon.choi
 */
public class ByteFieldConverter extends AbstractByteArrayFieldConverter<Byte> {

    @Override
    public Byte read(byte[] source, IMessageFieldMetadata fieldMetadata, MessageContext messageContext)
            throws Exception {

        String encoding = this.getEncoding(fieldMetadata, messageContext.getDefaultEncoding());
        byte[] paddingBytes = this.getPaddingBytes(fieldMetadata, encoding, ByteUtils.SPACE_PAD_BYTES);

        String raw = new String(source, encoding);
        return raw.isEmpty() ? paddingBytes[0] : raw.getBytes(encoding)[0];
    }

    @Override
    public byte[] write(Byte value, IMessageFieldMetadata fieldMetadata, MessageContext messageContext)
            throws Exception {
        String encoding = this.getEncoding(fieldMetadata, messageContext.getDefaultEncoding());
        String raw = new String(new byte[] { value }, encoding);
        byte[] encoded = raw.getBytes(encoding);
        return encoded;
    }

}
