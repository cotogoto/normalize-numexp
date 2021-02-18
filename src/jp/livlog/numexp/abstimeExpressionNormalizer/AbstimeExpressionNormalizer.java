package jp.livlog.numexp.abstimeExpressionNormalizer;

import java.util.List;

import jp.livlog.numexp.normalizerTemplate.NormalizerTemplate;
import jp.livlog.numexp.numberNormalizer.NumberNormalizer;
import jp.livlog.numexp.numberNormalizer.impl.NumberNormalizerImpl;
import jp.livlog.numexp.share.NNumber;
import jp.livlog.numexp.share.NumberModifier;
import jp.livlog.numexp.share.RefObject;

public abstract class AbstimeExpressionNormalizer extends NormalizerTemplate <AbstimeExpression, LimitedAbstimeExpression> {

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
