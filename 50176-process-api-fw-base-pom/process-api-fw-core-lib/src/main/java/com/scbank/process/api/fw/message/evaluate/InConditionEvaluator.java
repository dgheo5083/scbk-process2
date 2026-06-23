package com.scbank.process.api.fw.message.evaluate;

import com.scbank.process.api.fw.message.context.MessageContext;

public class InConditionEvaluator implements IConditionEvaluator {

    @Override
    public boolean supports(String expression) {
        return expression.contains(" in ");
    }

    @Override
    public boolean evaluate(String expression, MessageContext context) {
        String[] parts = expression.split(" in ");
        String path = parts[0].trim();
        Object actual = context.getPathValue(path);

        String[] values = parts[1]
                .replaceAll("[\\[\\]'\" ]", "")
                .split(",");

        for (String val : values) {
            if (String.valueOf(actual).equals(val)) {
                return true;
            }
        }
        return false;
    }
}