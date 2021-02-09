package jp.livlog.normalizeNumexp.numericalExpressionNormalizer;

import jp.livlog.normalizeNumexp.normalizerUtility.impl.NormalizedExpressionTemplateImpl;
import jp.livlog.normalizeNumexp.share.NNumber;
import lombok.ToString;

@ToString
public class NumericalExpression extends NormalizedExpressionTemplateImpl {

    public NumericalExpression(final String original_expression, final int position_start, final int position_end, final double value_lowerbound,
            final double value_upperbound) {

        super(original_expression, position_start, position_end);
        this.value_lowerbound = value_lowerbound;
        this.value_upperbound = value_upperbound;
        this.counter = "";
        this.ordinary = false;
    }


    public NumericalExpression(NNumber number) {

        super(number.originalExpression, number.positionStart, number.positionEnd);
        this.value_lowerbound = number.valueLowerbound;
        this.value_upperbound = number.valueUpperbound;
        this.counter = "";
        this.ordinary = false;
    }

    public double  value_lowerbound;

    public double  value_upperbound;

    public String  counter = new String();

    public boolean ordinary;
}
