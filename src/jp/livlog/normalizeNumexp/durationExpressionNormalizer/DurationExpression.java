package jp.livlog.normalizeNumexp.durationExpressionNormalizer;

import jp.livlog.normalizeNumexp.normalizerUtility.impl.NormalizedExpressionTemplateImpl;
import jp.livlog.normalizeNumexp.share.NNumber;
import jp.livlog.normalizeNumexp.share.NTime;
import jp.livlog.normalizeNumexp.share.Symbol;

public class DurationExpression extends NormalizedExpressionTemplateImpl {

    public DurationExpression(NNumber number) {

        super(number.originalExpression, number.positionStart, number.positionEnd);
        this.orgValueLowerbound = number.valueLowerbound;
        this.orgValueUpperbound = number.valueUpperbound;
        this.valueLowerbound = new NTime(Symbol.INFINITY);
        this.valueUpperbound = new NTime(-Symbol.INFINITY);
        this.ordinary = false;
    }

    public double orgValueLowerbound;

    public double orgValueUpperbound;
}
