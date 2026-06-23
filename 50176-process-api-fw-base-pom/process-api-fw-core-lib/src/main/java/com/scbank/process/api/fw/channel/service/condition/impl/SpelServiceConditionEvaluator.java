package com.scbank.process.api.fw.channel.service.condition.impl;

import java.util.regex.Pattern;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelCompilerMode;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.SimpleEvaluationContext;

import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.service.condition.IServiceConditionEvaluator;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * Spring Expression Language(SpEL)를 이용하여
 * 서비스 실행 조건을 평가하는 {@link IServiceConditionEvaluator} 구현체입니다.
 *
 * 조건 예시:
 * - device == 'PC'
 * - device != 'MOBILE'
 * - #device == 'TABLET' or #device == 'PC'
 * </pre>
 *
 * 조건 문자열은 Dispatcher에서 XML 또는 설정 파일 기반으로 전달되며,
 * {@link IServiceContext}의 device 속성을 SpEL 변수로 바인딩합니다.
 *
 * 기본 바인딩 변수 목록:
 * <ul>
 * <li>{@code #device} → context.device().getId()</li>
 * </ul>
 *
 * @author sungdon.choi
 * @since 2025.04.15
 */
@Slf4j
public class SpelServiceConditionEvaluator implements IServiceConditionEvaluator {

    private static final Pattern SAFE_SPEL = Pattern.compile("^[a-zA-Z0-9_#().!=<>&|\\s]+S");

    /** SpEL 파서 */
    private static final ExpressionParser PARSER = new SpelExpressionParser(
            new SpelParserConfiguration(SpelCompilerMode.OFF, null));

    /**
     * SpEL 조건식을 평가합니다.
     *
     * @param context   현재 서비스 실행 컨텍스트
     * @param condition 평가할 조건 문자열 (SpEL)
     * @return true: 조건 만족 / false: 조건 미충족 또는 오류
     */
    @SuppressWarnings("deprecation")
	@Override
    public boolean evaluate(IServiceContext context, String condition) {
        if (condition == null || condition.isBlank()) {
            return true; // 조건이 없으면 항상 허용
        }

        if (!SAFE_SPEL.matcher(condition).matches()) {
            return false;
        }

        EvaluationContext evalCtx = SimpleEvaluationContext
                .forReadOnlyDataBinding()
                .build();

        // 바인딩 변수 등록 (추후 확장 가능)
        evalCtx.setVariable("device", context.device().getId());

        try {
            // SpEL 조건 평가
            boolean result = PARSER.parseExpression(condition)
                    .getValue(evalCtx, Boolean.class);
            return result;
        } catch (Exception e) {
            log.error("조건식 평가 실패: {}", condition, e);
            return false;
        }
    }
}
