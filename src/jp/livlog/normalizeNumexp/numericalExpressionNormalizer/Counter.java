package jp.livlog.normalizeNumexp.numericalExpressionNormalizer;

import jp.livlog.normalizeNumexp.normalizerUtility.impl.LimitedExpressionTemplateImpl;
import lombok.ToString;

@ToString
public class Counter extends LimitedExpressionTemplateImpl {

    public String counter;

    public int    SIprefix;             // 「キロ」「ミリ」などの表記に使用

    public int    optionalPowerOfTen; // 「トン」のような特殊な表記（数に10^4を乗算する必要がある）の時に使用
}
