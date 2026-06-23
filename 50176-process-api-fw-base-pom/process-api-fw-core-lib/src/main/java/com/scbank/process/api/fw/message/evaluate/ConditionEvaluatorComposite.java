package com.scbank.process.api.fw.message.evaluate;

import java.util.ArrayList;
import java.util.List;

import com.scbank.process.api.fw.message.context.MessageContext;

/**
 * @author sungdon.choi
 */
public class ConditionEvaluatorComposite {

    private final List<IConditionEvaluator> operators = new ArrayList<>();

    public ConditionEvaluatorComposite() {
        operators.add(new EqualsEvaluator());
        operators.add(new GreaterThanEvaluator());
        operators.add(new ContainsEvaluator());
        operators.add(new InConditionEvaluator());
    }

    public boolean evaluate(String expression, MessageContext context) {
        return operators.stream()
                .filter(op -> op.supports(expression))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 조건식: " + expression))
                .evaluate(expression, context);
    }
}
