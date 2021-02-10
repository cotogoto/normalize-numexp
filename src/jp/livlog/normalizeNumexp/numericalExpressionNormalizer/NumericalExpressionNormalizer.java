package jp.livlog.normalizeNumexp.numericalExpressionNormalizer;

import java.util.List;

import jp.livlog.normalizeNumexp.normalizerTemplate.NormalizerTemplate;
import jp.livlog.normalizeNumexp.numberNormalizer.NumberNormalizer;
import jp.livlog.normalizeNumexp.numberNormalizer.impl.NumberNormalizerImpl;
import jp.livlog.normalizeNumexp.share.NNumber;
import jp.livlog.normalizeNumexp.share.NumberModifier;
import jp.livlog.normalizeNumexp.share.RefObject;

public abstract class NumericalExpressionNormalizer extends NormalizerTemplate <NumericalExpression, Counter> {

    public NumericalExpressionNormalizer(String language) {

        this.NN = new NumberNormalizerImpl(language);
        this.language = language;
        this.init();
    }


    @Override
    public abstract void init();


    @Override
    public abstract void normalizeNumber(StringBuilder uText, List <NNumber> numbers);


    @Override
    public abstract void reviseAnyTypeExpressionByMatchingLimitedExpression(List <NumericalExpression> numexps, RefObject <Integer> expressionId,
            Counter matchingLimitedExpression);


    @Override
    public abstract void reviseAnyTypeExpressionByMatchingPrefixCounter(NumericalExpression numexps, Counter matchingLimitedExpression);


    @Override
    public abstract void reviseAnyTypeExpressionByNumberModifier(NumericalExpression numexp, NumberModifier numberModifier);


    @Override
    public abstract void deleteNotAnyTypeExpression(List <NumericalExpression> numexps);


    @Override
    public abstract void fixByRangeExpression(StringBuilder uText, List <NumericalExpression> numexps);

    public NumberNormalizer NN;
}
