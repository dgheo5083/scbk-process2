package com.scbank.process.api.fw.message.converter.format.common;

import com.scbank.process.api.fw.message.context.MessageContext;
import com.scbank.process.api.fw.message.metadata.IMessageFieldMetadata;

public class VariableLengthFieldConverter extends AbstractByteArrayFieldConverter<String> {

	@Override
	public String read(byte[] source, IMessageFieldMetadata fieldMetadata, MessageContext messageContext) throws Exception {
		String encoding = this.getEncoding(fieldMetadata, messageContext.getDefaultEncoding());
		return new String(source, this.getEncoding(fieldMetadata, encoding));
	}

	@Override
	public byte[] write(String value, IMessageFieldMetadata fieldMetadata, MessageContext messageContext) throws Exception {
        String charset = this.getEncoding(fieldMetadata, messageContext.getDefaultEncoding());
        return value.getBytes(charset);
	}
}
