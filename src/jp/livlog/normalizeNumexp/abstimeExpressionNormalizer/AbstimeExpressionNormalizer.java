package jp.livlog.normalizeNumexp.abstimeExpressionNormalizer;

import java.util.List;

import jp.livlog.normalizeNumexp.abstimeExpressionNormalizer.impl.AbstimeExpressionImpl;
import jp.livlog.normalizeNumexp.abstimeExpressionNormalizer.impl.LimitedAbstimeExpressionImpl;
import jp.livlog.normalizeNumexp.normalizerTemplate.NormalizerTemplate;
import jp.livlog.normalizeNumexp.numericalExpressionNormalizer.NumberNormalizer;
import jp.livlog.normalizeNumexp.numericalExpressionNormalizer.impl.NumberNormalizerImpl;
import jp.livlog.normalizeNumexp.share.NNumber;
import jp.livlog.normalizeNumexp.share.NumberModifier;
import jp.livlog.normalizeNumexp.share.RefObject;

public abstract class AbstimeExpressionNormalizer extends NormalizerTemplate <AbstimeExpressionImpl, LimitedAbstimeExpressionImpl> {

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
    public abstract void reviseAnyTypeExpressionByMatchingLimitedExpression(List <AbstimeExpressionImpl> abstimeexps,
            RefObject <Integer> expressionId, LimitedAbstimeExpressionImpl matchingLimitedAbstimeExpression);


    @Override
    public abstract void reviseAnyTypeExpressionByMatchingPrefixCounter(AbstimeExpressionImpl anyTypeExpression,
            LimitedAbstimeExpressionImpl matchingLimitedExpression);


    @Override
    public abstract void reviseAnyTypeExpressionByNumberModifier(AbstimeExpressionImpl abstimeexp, NumberModifier numberModifier);


    @Override
    public abstract void deleteNotAnyTypeExpression(List <AbstimeExpressionImpl> abstimeexps);


    @Override
    public abstract void fixByRangeExpression(StringBuilder uText, List <AbstimeExpressionImpl> abstimeexps);

    public NumberNormalizer NN;

}
