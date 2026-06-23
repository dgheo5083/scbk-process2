package com.scbank.process.api.fw.message.evaluate;

import com.scbank.process.api.fw.message.context.MessageContext;

public class GreaterThanEvaluator implements IConditionEvaluator {

    @Override
    public boolean supports(String expression) {
        return expression.contains(">");
    }

    @Override
    public boolean evaluate(String expression, MessageContext context) {
        String[] tokens = expression.split(">");
        double actual = Double.parseDouble(String.valueOf(context.getPathValue(tokens[0].trim())));
        double expected = Double.parseDouble(tokens[1].trim());
        return actual > expected;
    }
}