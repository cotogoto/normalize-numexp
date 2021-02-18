package jp.livlog.numexp.abstimeExpressionNormalizer;

import jp.livlog.numexp.normalizerUtility.impl.NormalizedExpressionTemplateImpl;
import jp.livlog.numexp.share.NNumber;
import jp.livlog.numexp.share.NTime;
import jp.livlog.numexp.share.Symbol;
import lombok.ToString;

@ToString(callSuper=false)
public class AbstimeExpression extends NormalizedExpressionTemplateImpl {

    public AbstimeExpression(NNumber number) {

        super(number.originalExpression, number.positionStart, number.positionEnd);
        this.orgValueLowerbound = number.valueLowerbound;
        this.orgValueUpperbound = number.valueUpperbound;
        this.valueLowerbound = new NTime(Symbol.INFINITY);
        this.valueUpperbound = new NTime(-Symbol.INFINITY);
        this.ordinary = false;
    }

    public double  orgValueLowerbound;

    public double  orgValueUpperbound;

}
