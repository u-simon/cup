package com.ca.ms.cup.common.task.dao.impl;

/**
 *
 */
public class WhereClause {
    private String expression;
    private Object value;

    public String getExpression() {
        return expression;
    }

    public Object getValue() {
        return value;
    }

    public WhereClause(String expression, Object value) {
        this.expression = expression;
        this.value = value;
    }
}
