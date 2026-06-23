package com.scbank.process.api.fw.message.evaluate;

import com.scbank.process.api.fw.message.context.MessageContext;

public class EqualsEvaluator implements IConditionEvaluator {

    @Override
    public boolean supports(String expression) {
        return expression.contains("==");
    }

    @Override
    public boolean evaluate(String expression, MessageContext context) {
        String[] tokens = expression.split("==");
        String path = tokens[0].trim();
        String expected = tokens[1].trim().replaceAll("^['\"]|['\"]$", "");
        Object actual = context.getPathValue(path);
        return expected.equals(String.valueOf(actual));
    }
}