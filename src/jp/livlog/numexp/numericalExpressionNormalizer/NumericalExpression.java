package jp.livlog.numexp.numericalExpressionNormalizer;

import jp.livlog.numexp.normalizerUtility.impl.NormalizedExpressionTemplateImpl;
import jp.livlog.numexp.share.NNumber;
import lombok.ToString;

@ToString(callSuper=false)
public class NumericalExpression extends NormalizedExpressionTemplateImpl {

    public NumericalExpression(final String originalExpression, final int positionStart, final int positionEnd, final double valueLowerbound,
            final double valueUpperbound) {

        super(originalExpression, positionStart, positionEnd);
        this.valueLowerbound = valueLowerbound;
        this.valueUpperbound = valueUpperbound;
        this.counter = "";
        this.ordinary = false;
    }


    public NumericalExpression(NNumber number) {

        super(number.originalExpression, number.positionStart, number.positionEnd);
        this.valueLowerbound = number.valueLowerbound;
        this.valueUpperbound = number.valueUpperbound;
        this.counter = "";
        this.ordinary = false;
    }

    public double  valueLowerbound;

    public double  valueUpperbound;

    public String  counter;
}
