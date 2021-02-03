package jp.livlog.normalizeNumexp.share;

public class NNumber extends BaseExpressionTemplate {

    public NNumber() {

        this.originalExpression = "";
        this.positionStart = -1;
        this.positionEnd = -1;
        this.valueLowerbound = Symbol.INFINITY;
        this.valueUpperbound = -Symbol.INFINITY;
        this.notationType = ENotationType.NOT_NUMBER.getValue();
    }


    public NNumber(String originalExpression, int positionStart, int positionEnd) {

        this.originalExpression = originalExpression;
        this.positionStart = positionStart;
        this.positionEnd = positionEnd;
        this.valueLowerbound = Symbol.INFINITY;
        this.valueUpperbound = -Symbol.INFINITY;
        this.notationType = ENotationType.NOT_NUMBER.getValue();
    }

    public String originalExpression = null;

    public int    positionStart;

    public int    positionEnd;

    public double valueLowerbound;

    public double valueUpperbound;

    public int    notationType;
}