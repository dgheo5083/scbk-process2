package com.scbank.process.api.fw.core.uuid.sequence;

import java.util.concurrent.atomic.AtomicLong;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AbstractSequenceGenerator {

    protected static final long DEFAULT_MAX_VALUE = 999_9L;
    protected static final long INIT_VALUE = -1L;

    /**
     * 증가할수 있는 최대 시퀀스 값
     */
    protected final long maxValue;
    protected final AtomicLong curValue = new AtomicLong(0L);

    protected boolean isValid(long next) {
        return next <= maxValue;
    }

    protected void setCurValue(long value) {
        this.curValue.set(value);
    }

    protected void init() {
        this.curValue.set(INIT_VALUE);
    }
}
