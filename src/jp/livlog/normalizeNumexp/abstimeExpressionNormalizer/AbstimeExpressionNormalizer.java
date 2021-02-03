package jp.livlog.normalizeNumexp.abstimeExpressionNormalizer;

import java.util.ArrayList;
import java.util.List;

import jp.livlog.normalizeNumexp.abstimeExpressionNormalizer.AbstimeExpressionNormalizer.AbstimeExpression;
import jp.livlog.normalizeNumexp.abstimeExpressionNormalizer.AbstimeExpressionNormalizer.LimitedAbstimeExpression;
import jp.livlog.normalizeNumexp.normalizerTemplate.NormalizerTemplate;
import jp.livlog.normalizeNumexp.normalizerUtility.LimitedExpressionTemplate;
import jp.livlog.normalizeNumexp.normalizerUtility.NormalizedExpressionTemplate;
import jp.livlog.normalizeNumexp.numericalExpressionNormalizer.NumberNormalizer;
import jp.livlog.normalizeNumexp.numericalExpressionNormalizer.impl.NumberNormalizerImpl;
import jp.livlog.normalizeNumexp.share.NNumber;
import jp.livlog.normalizeNumexp.share.NTime;
import jp.livlog.normalizeNumexp.share.NumberModifier;
import jp.livlog.normalizeNumexp.share.RefObject;
import jp.livlog.normalizeNumexp.share.Symbol;

public abstract class AbstimeExpressionNormalizer extends NormalizerTemplate <AbstimeExpression, LimitedAbstimeExpression> {

    public abstract static class AbstimeExpression extends NormalizedExpressionTemplate {

        public AbstimeExpression(NNumber number) {

            super(number.originalExpression, number.positionStart, number.positionEnd);
            this.orgValueLowerbound = number.valueLowerbound;
            this.orgValueUpperbound = number.valueUpperbound;
            this.valueLowerbound = new NTime(Symbol.INFINITY);
            this.valueUpperbound = new NTime(-Symbol.INFINITY);
            this.ordinary = false;
        }

        public double                 orgValueLowerbound;

        public double                 orgValueUpperbound;

        public NTime valueLowerbound;

        public NTime valueUpperbound;

        public boolean                ordinary;
    }

    public abstract static class LimitedAbstimeExpression extends LimitedExpressionTemplate {

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
    public abstract void normalizeNumber(StringBuilder uText, List <NNumber> numbers);


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
    public abstract void fixByRangeExpression(StringBuilder uText, List <AbstimeExpression> abstimeexps);

    public NumberNormalizer NN;

}
