package com.scbank.process.api.svc.shared.components.fido;

import org.springframework.stereotype.Component;

import com.scbank.process.api.fw.core.uuid.sequence.AbstractSequenceGenerator;
import com.scbank.process.api.fw.core.uuid.sequence.ISequenceGenerator;

/**
 * FIDO svcTrID 생성용 (MAX_VALUE = 9999)
 */
@Component
class FidoSequenceGenerator extends AbstractSequenceGenerator implements ISequenceGenerator {

	public FidoSequenceGenerator() {
		super(9999);
	}

	@Override
	public long next() {
		long nextSeq = curValue.incrementAndGet();
		if (isValid(nextSeq)) {
			return nextSeq;
		} else {
			init();
			return next();
		}
	}
}