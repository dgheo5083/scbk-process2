package com.scbank.process.api.fw.core.uuid.sequence;

import java.time.LocalDate;

public interface IDailySequenceGenerator extends ISequenceGenerator {

    long next(LocalDate day);
}
