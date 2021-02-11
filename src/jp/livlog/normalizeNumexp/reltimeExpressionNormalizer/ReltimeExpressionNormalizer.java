package jp.livlog.normalizeNumexp.reltimeExpressionNormalizer;

import java.util.List;

import jp.livlog.normalizeNumexp.normalizerTemplate.NormalizerTemplate;
import jp.livlog.normalizeNumexp.numberNormalizer.NumberNormalizer;
import jp.livlog.normalizeNumexp.numberNormalizer.impl.NumberNormalizerImpl;
import jp.livlog.normalizeNumexp.share.NNumber;
import jp.livlog.normalizeNumexp.share.NumberModifier;
import jp.livlog.normalizeNumexp.share.RefObject;

public abstract class ReltimeExpressionNormalizer extends NormalizerTemplate <ReltimeExpression, LimitedReltimeExpression> {

    public ReltimeExpressionNormalizer(final String language) {

        this.NN = new NumberNormalizerImpl(language);
        this.language = language;
        this.init();
    }


    @Override
    public abstract void init();


    @Override
    public abstract void normalizeNumber(StringBuilder uText, List <NNumber> numbers);


    @Override
    public abstract void reviseAnyTypeExpressionByMatchingLimitedExpression(List <ReltimeExpression> reltimeexps, RefObject <Integer> expressionId,
            LimitedReltimeExpression matchingLimitedReltimeExpression);


    @Override
    public abstract void reviseAnyTypeExpressionByMatchingPrefixCounter(ReltimeExpression reltimeexp,
            final LimitedReltimeExpression matchingLimitedExpression);


    @Override
    public abstract void reviseAnyTypeExpressionByNumberModifier(ReltimeExpression reltimeexp, final NumberModifier numberModifier);


    @Override
    public abstract void deleteNotAnyTypeExpression(List <ReltimeExpression> reltimeexps);


    @Override
    public abstract void fixByRangeExpression(final StringBuilder uText, List <ReltimeExpression> reltimeexps);

    public final NumberNormalizer NN;
}
