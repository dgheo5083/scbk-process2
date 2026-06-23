package com.scbank.process.api.fw.message.evaluate;

import com.scbank.process.api.fw.message.context.MessageContext;

public class ContainsEvaluator implements IConditionEvaluator {

    @Override
    public boolean supports(String expression) {
        return expression.contains(" contains ");
    }

    @Override
    public boolean evaluate(String expression, MessageContext context) {
        String[] tokens = expression.split(" contains ");
        String path = tokens[0].trim();
        String keyword = tokens[1].trim().replaceAll("^['\"]|['\"]$", "");
        Object actual = context.getPathValue(path);
        return String.valueOf(actual).contains(keyword);
    }
}