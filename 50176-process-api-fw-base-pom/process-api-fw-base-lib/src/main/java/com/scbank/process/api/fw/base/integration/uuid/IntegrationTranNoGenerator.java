package com.scbank.process.api.fw.base.integration.uuid;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.core.uuid.IIdentifyGenerator;
import com.scbank.process.api.fw.core.uuid.sequence.ISequenceGenerator;

/**
 * 연계시스템 전문 거래번호 생성기
 * 
 * @author sundon.choi
 */
public class IntegrationTranNoGenerator implements IIdentifyGenerator {

    /**
     * 전문 거래번호 기본 길이
     */
    private static final int DEFAULT_TRAN_NO_SIZE = 6;

    /**
     * 전문 거래번호 기본 패딩 character
     */
    private static final char PAD_CHAR = '0';

    /**
     * 시퀀스 생성기
     */
    private final ISequenceGenerator sequenceGenerator;

    /**
     * 전문 거래번호 길이
     */
    private final int size;

    /**
     * 생성자
     * 
     * @param sequenceGenerator 시퀀스 생성기
     */
    public IntegrationTranNoGenerator(ISequenceGenerator sequenceGenerator) {
        this(sequenceGenerator, DEFAULT_TRAN_NO_SIZE);
    }

    /**
     * 생성자
     * 
     * @param sequenceGenerator 시퀀스 생성기
     * @param size              전문 거래번호 길이
     */
    public IntegrationTranNoGenerator(ISequenceGenerator sequenceGenerator, int size) {
        this.sequenceGenerator = sequenceGenerator;
        this.size = size;
    }

    @Override
    public String generateId() {
        long sequence = this.sequenceGenerator.next();
        return StringUtils.lpad(sequence, size, PAD_CHAR);
    }
}
