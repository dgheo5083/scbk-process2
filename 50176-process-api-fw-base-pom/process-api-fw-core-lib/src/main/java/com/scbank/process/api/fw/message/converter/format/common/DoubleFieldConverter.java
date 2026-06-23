package com.scbank.process.api.fw.message.converter.format.common;

import java.math.BigDecimal;

import com.scbank.process.api.fw.message.context.MessageContext;
import com.scbank.process.api.fw.message.metadata.IMessageFieldMetadata;

/**
 * 고정 길이 메시지 포맷에서 Double 타입 필드를 읽고 쓰기 위한 변환기 클래스.
 *
 * <p>
 * 이 클래스는 내부적으로 {@link BigDecimalFieldConverter}를 이용하여
 * 고정 길이 바이트 배열과 {@link Double} 값 간의 변환을 수행한다.
 * Double ↔ BigDecimal 간 변환은 자동으로 처리된다.
 * </p>
 *
 * 주요 특징:
 * <ul>
 * <li>읽기 시: 바이트 배열 → BigDecimal → Double 변환</li>
 * <li>쓰기 시: Double → BigDecimal → 바이트 배열 변환</li>
 * <li>BigDecimal 기반 고정 길이 포맷 처리 로직을 재활용</li>
 * </ul>
 *
 * 사용 예시:
 * <ul>
 * <li>금액, 비율 등의 실수형 데이터 고정 길이 변환</li>
 * </ul>
 *
 * @see BigDecimalFieldConverter
 */
public class DoubleFieldConverter extends AbstractByteArrayFieldConverter<Double> {

    /**
     * 내부 위임용 BigDecimal 변환기
     */
    private final BigDecimalFieldConverter bigDecimalFieldConverter = new BigDecimalFieldConverter();

    /**
     * 고정 길이 바이트 배열을 Double 타입으로 변환한다. (역직렬화)
     *
     * @param source         원본 바이트 배열
     * @param fieldMetadata  필드 메타데이터
     * @param messageContext 변환 컨텍스트
     * @return 변환된 Double 값
     * @throws Exception 변환 실패 시
     */
    @Override
    public Double read(byte[] source,
            IMessageFieldMetadata fieldMetadata,
            MessageContext messageContext) throws Exception {
        BigDecimal bigDecimal = this.bigDecimalFieldConverter.read(source, fieldMetadata, messageContext);
        return bigDecimal.doubleValue();
    }

    /**
     * Double 값을 고정 길이 바이트 배열로 변환한다. (직렬화)
     *
     * @param value          변환할 Double 값
     * @param fieldMetadata  필드 메타데이터
     * @param messageContext 변환 컨텍스트
     * @return 변환된 바이트 배열
     * @throws Exception 변환 실패 시
     */
    @Override
    public byte[] write(Double value,
            IMessageFieldMetadata fieldMetadata,
            MessageContext messageContext) throws Exception {
        if (value == null) {
            value = 0d;
        }
        return this.bigDecimalFieldConverter.write(BigDecimal.valueOf(value), fieldMetadata, messageContext);
    }
}
