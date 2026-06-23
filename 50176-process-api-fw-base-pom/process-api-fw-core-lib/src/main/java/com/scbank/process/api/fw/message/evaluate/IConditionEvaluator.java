package com.scbank.process.api.fw.message.evaluate;

import com.scbank.process.api.fw.message.context.MessageContext;

/**
 * @author sungdon.choi
 */
public interface IConditionEvaluator {

    /**
     * 
     * @param expression
     * @return
     */
    boolean supports(String expression);

    /**
     * 
     * @param expression
     * @param ctx
     * @return
     */
    boolean evaluate(String expression, MessageContext ctx);
}
