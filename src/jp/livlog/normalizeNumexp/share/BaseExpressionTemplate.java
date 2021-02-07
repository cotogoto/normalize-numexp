package jp.livlog.normalizeNumexp.share;

import lombok.ToString;

@ToString
public class BaseExpressionTemplate {

    public String originalExpression;

    public int    positionStart;

    public int    positionEnd;

    public String pattern;

    public double valueLowerbound;

    public double valueUpperbound;
}
