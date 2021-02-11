package jp.livlog.normalizeNumexp.reltimeExpressionNormalizer;

import jp.livlog.normalizeNumexp.normalizerUtility.impl.NormalizedExpressionTemplateImpl;
import jp.livlog.normalizeNumexp.share.NNumber;
import jp.livlog.normalizeNumexp.share.NTime;
import jp.livlog.normalizeNumexp.share.Symbol;

public class ReltimeExpression extends NormalizedExpressionTemplateImpl {

    public ReltimeExpression(NNumber number) {

        super(number.originalExpression, number.positionStart, number.positionEnd);
        this.orgValueLowerbound = number.valueLowerbound;
        this.orgValueUpperbound = number.valueUpperbound;
        this.valueLowerboundAbs = new NTime(Symbol.INFINITY);
        this.valueUpperboundAbs = new NTime(-Symbol.INFINITY);
        this.valueLowerboundRel = new NTime(Symbol.INFINITY);
        this.valueUpperboundRel = new NTime(-Symbol.INFINITY);
        this.ordinary = false;
    }

    public double  orgValueLowerbound;

    public double  orgValueUpperbound;

    public NTime   valueLowerboundAbs;

    public NTime   valueUpperboundAbs;

    public NTime   valueLowerboundRel;

    public NTime   valueUpperboundRel;

    public boolean ordinary;
}