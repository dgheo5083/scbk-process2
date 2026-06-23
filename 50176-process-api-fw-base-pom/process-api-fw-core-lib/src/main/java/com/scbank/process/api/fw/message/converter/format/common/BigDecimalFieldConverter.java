package com.scbank.process.api.fw.message.converter.format.common;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;

import org.springframework.lang.NonNull;

import com.scbank.process.api.fw.core.utils.ByteUtils;
import com.scbank.process.api.fw.message.context.MessageContext;
import com.scbank.process.api.fw.message.enums.AlignType;
import com.scbank.process.api.fw.message.exception.MessageFieldConvertException;
import com.scbank.process.api.fw.message.metadata.IMessageFieldMetadata;
import com.scbank.process.api.fw.message.option.DeserializationOptions;
import com.scbank.process.api.fw.message.option.MessageFormatOptions;
import com.scbank.process.api.fw.message.option.MessageFormatOptions.MessageFormatOption;

import lombok.extern.slf4j.Slf4j;

/**
 * 고정 길이 메시지 포맷에서 BigDecimal 타입 필드를 읽고 쓰기 위한 변환기 클래스.
 *
 * 주요 지원 기능:
 * - 고정 길이 바이트 배열을 BigDecimal로 변환 (역직렬화)
 * - BigDecimal을 고정 길이 바이트 배열로 변환 (직렬화)
 * - 패딩(PADDING)/언패딩(UNPADDING) 옵션 적용
 * - 소수점 포함 여부(INCLUDE_DECIMAL_POINT) 제어
 * - 스케일 고정(FORCE_SCALE_ON_READ) 및 반올림(DECIMAL_ROUNDING_MODE) 처리
 * - 길이 초과 검증(LENGTH_CHECK)
 */
@Slf4j
public class BigDecimalFieldConverter extends AbstractByteArrayFieldConverter<BigDecimal> {

    /**
     * 고정 길이 바이트 배열을 BigDecimal 타입으로 변환한다. (역직렬화)
     */
    @Override
    public BigDecimal read(@NonNull byte[] source,
            @NonNull IMessageFieldMetadata fieldMetadata,
            @NonNull MessageContext messageContext) throws Exception {
        try {
            DeserializationOptions options = messageContext.getDeserializationOptions();
            Charset charset = Charset.forName(this.getEncoding(fieldMetadata, messageContext.getDefaultEncoding()));
            int fieldLength = fieldMetadata.getLength();
            int scale = fieldMetadata.getScale();

            // LENGTH_CHECK: 읽기 길이 검증
            if (options.enabled(MessageFormatOption.LENGTH_CHECK) && fieldLength > 0 && source.length > fieldLength) {
                throw new MessageFieldConvertException(fieldMetadata.getId(),
                        String.format("[%s] 읽기 길이 초과: 기대 %d바이트, 실제 %d바이트",
                                fieldMetadata.getId(), fieldLength, source.length));
            }

            byte[] paddingBytes = this.getPaddingBytes(fieldMetadata, charset.name(), ByteUtils.ZERO_PAD_BYTES);
            AlignType align = fieldMetadata.getAlign() == AlignType.NONE ? AlignType.RIGHT : fieldMetadata.getAlign();

            // UNPADDING 옵션에 따라 패딩 제거 또는 원본 사용
            String raw;
            if (options.enabled(MessageFormatOption.UNPADDING)) {
                raw = ByteUtils.removePadding(source, paddingBytes, align, charset.name());
            } else {
                raw = new String(source, charset);
            }

            // FIELD_TRIM 적용 (양쪽 공백 제거)
            if (options.enabled(MessageFormatOption.FIELD_TRIM)) {
                raw = raw.trim();
            }

            // 빈 문자열이면 BigDecimal.ZERO 반환
            if (raw.isEmpty()) {
                return BigDecimal.ZERO;
            }

            boolean includeDecimalPoint = options.enabled(MessageFormatOption.INCLUDE_DECIMAL_POINT);
            boolean forceScale = options.enabled(MessageFormatOption.FORCE_SCALE_ON_READ);
            RoundingMode roundingMode = (RoundingMode) options.get(MessageFormatOption.DECIMAL_ROUNDING_MODE)
                    .orElse(RoundingMode.UNNECESSARY);

            // 소수점 포함 여부에 따라 변환 방식 분기
            BigDecimal value;
            if (includeDecimalPoint) {
                value = new BigDecimal(raw);
            } else {
                int len = raw.length();
                if (scale == 0) {
                    value = new BigDecimal(raw);
                } else if (len <= scale) {
                    // "00012"처럼 scale보다 짧은 경우 보정
                    StringBuilder sb = new StringBuilder("0.");
                    for (int i = len; i < scale; i++) {
                        sb.append('0');
                    }
                    sb.append(raw);
                    value = new BigDecimal(sb.toString());
                } else {
                    // 정수부/소수부 구분
                    if (raw.contains(".")) {
                        value = new BigDecimal(raw);
                    } else {
                        String intPart = raw.substring(0, len - scale);
                        String fracPart = raw.substring(len - scale);
                        value = new BigDecimal(intPart + "." + fracPart);
                    }
                }
            }

            // FORCE_SCALE_ON_READ 활성화 시 스케일 강제 고정
            if (forceScale) {
                value = value.setScale(scale, roundingMode);
            }

            if (messageContext.isUseDebugLog()) {
                log.info("### {} read {}({}): [{}]", fieldMetadata.getFieldType().getSimpleName(),
                        fieldMetadata.getId(),
                        fieldLength,
                        value);
            }

            return value;
        } catch (Exception e) {
            throw new MessageFieldConvertException(e);
        }
    }

    /**
     * BigDecimal 값을 고정 길이 바이트 배열로 변환한다. (직렬화)
     */
    @Override
    public byte[] write(@NonNull BigDecimal value,
            @NonNull IMessageFieldMetadata fieldMetadata,
            @NonNull MessageContext messageContext) throws Exception {
        try {
            if (value == null) {
                value = BigDecimal.ZERO;
            }

            MessageFormatOptions options = messageContext.getSerializationOptions();
            Charset charset = Charset.forName(this.getEncoding(fieldMetadata, messageContext.getDefaultEncoding()));
            int fieldLength = fieldMetadata.getLength();
            int scale = fieldMetadata.getScale();
            byte[] paddingBytes = this.getPaddingBytes(fieldMetadata, charset.name(), ByteUtils.ZERO_PAD_BYTES);
            AlignType align = fieldMetadata.getAlign() == AlignType.NONE ? AlignType.RIGHT : fieldMetadata.getAlign();

            boolean includeDecimal = options.enabled(MessageFormatOption.INCLUDE_DECIMAL_POINT);
            RoundingMode roundingMode = (RoundingMode) options.get(MessageFormatOption.DECIMAL_ROUNDING_MODE)
                    .orElse(RoundingMode.UNNECESSARY);

            // 소수점 포함 여부에 따라 문자열 변환
            String stringValue;
            if (includeDecimal) {
                stringValue = value.setScale(scale, roundingMode).stripTrailingZeros().toPlainString();
            } else {
                BigDecimal scaled = value.setScale(scale, roundingMode).movePointRight(scale);
                stringValue = scaled.toPlainString();
            }

            byte[] result;

            // PADDING 옵션에 따라 패딩 추가 여부 결정
            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                if (options.enabled(MessageFormatOption.PADDING)) {
                    ByteUtils.writeWithPadding(
                            out,
                            stringValue,
                            fieldLength,
                            paddingBytes,
                            AlignType.LEFT.equals(align),
                            charset.name());
                } else {
                    out.write(stringValue.getBytes(charset));
                }
                result = out.toByteArray();
            }

            // LENGTH_CHECK: 쓰기 길이 검증
            if (options.enabled(MessageFormatOption.LENGTH_CHECK) && fieldLength > 0 && result.length > fieldLength) {
                throw new MessageFieldConvertException(fieldMetadata.getId(),
                        String.format("[%s] 쓰기 길이 초과: 최대 %d바이트, 현재 %d바이트",
                                fieldMetadata.getId(), fieldLength, result.length));
            }

            if (messageContext.isUseDebugLog()) {
                log.info("### {} write {}({}): [{}]", fieldMetadata.getFieldType().getSimpleName(),
                        fieldMetadata.getId(),
                        fieldLength,
                        new String(result, charset));
            }

            return result;
        } catch (Exception e) {
            throw new MessageFieldConvertException(e);
        }
    }
}