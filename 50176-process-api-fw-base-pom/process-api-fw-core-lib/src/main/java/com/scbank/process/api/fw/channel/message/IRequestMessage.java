package com.scbank.process.api.fw.channel.message;

import java.io.Serializable;

/**
 * @author sungdon.choi
 */
public interface IRequestMessage<H, T> extends Serializable {

	default H getHeader() {
		return null;
	}

	default T getBody() {
		return null;
	}
}
