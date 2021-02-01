package jp.livlog.normalizeNumexp.abstimeExpressionNormalizer;

import java.util.ArrayList;
import java.util.List;

import jp.livlog.normalizeNumexp.abstimeExpressionNormalizer.AbstimeExpressionNormalizer.AbstimeExpression;
import jp.livlog.normalizeNumexp.abstimeExpressionNormalizer.AbstimeExpressionNormalizer.LimitedAbstimeExpression;
import jp.livlog.normalizeNumexp.digitUtility.DigitUtility;
import jp.livlog.normalizeNumexp.normalizerTemplate.NormalizerTemplate;
import jp.livlog.normalizeNumexp.normalizerUtility.NormalizerUtility;
import jp.livlog.normalizeNumexp.normalizerUtility.NormalizerUtility.NumberModifier;
import jp.livlog.normalizeNumexp.numericalExpressionNormalizer.NumberNormalizer;
import jp.livlog.normalizeNumexp.numericalExpressionNormalizer.impl.NumberNormalizerImpl;
import jp.livlog.normalizeNumexp.share.RefObject;
import jp.livlog.normalizeNumexp.share.Symbol;

public abstract class AbstimeExpressionNormalizer extends NormalizerTemplate <AbstimeExpression, LimitedAbstimeExpression> {

    public abstract static class AbstimeExpression extends NormalizerUtility.NormalizedExpressionTemplate {

        public AbstimeExpression(DigitUtility.Number number) {

            super(number.originalExpression, number.positionStart, number.positionEnd);
            this.orgValueLowerbound = number.valueLowerbound;
            this.orgValueUpperbound = number.valueUpperbound;
            this.valueLowerbound = new NormalizerUtility.Time(Symbol.INFINITY);
            this.valueUpperbound = new NormalizerUtility.Time(-Symbol.INFINITY);
            this.ordinary = false;
        }

        public double                 orgValueLowerbound;

        public double                 orgValueUpperbound;

        public NormalizerUtility.Time valueLowerbound;

        public NormalizerUtility.Time valueUpperbound;

        public boolean                ordinary;
    }

    public abstract static class LimitedAbstimeExpression extends NormalizerUtility.LimitedExpressionTemplate {

        public List <String> corresponding_time_position = new ArrayList <>();

        public List <String> process_type                = new ArrayList <>();
    }

    public AbstimeExpressionNormalizer(String language) {

        this.NN = new NumberNormalizerImpl(language);
        this.language = language;
        this.init();
    }


    @Override
    public abstract void init();


    @Override
    public abstract void normalizeNumber(String text, List <DigitUtility.Number> numbers);


    @Override
    public abstract void reviseAnyTypeExpressionByMatchingLimitedExpression(List <AbstimeExpression> abstimeexps,
            RefObject <Integer> expressionId, LimitedAbstimeExpression matchingLimitedAbstimeExpression);


    @Override
    public abstract void reviseAnyTypeExpressionByMatchingPrefixCounter(AbstimeExpression anyTypeExpression,
            LimitedAbstimeExpression matchingLimitedExpression);


    @Override
    public abstract void reviseAnyTypeExpressionByNumberModifier(AbstimeExpression abstimeexp, NumberModifier numberModifier);


    @Override
    public abstract void deleteNotAnyTypeExpression(List <AbstimeExpression> abstimeexps);


    @Override
    public abstract void fixByRangeExpression(String uText, List <AbstimeExpression> abstimeexps);

    public NumberNormalizer NN;

}
