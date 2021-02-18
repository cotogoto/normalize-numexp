package jp.livlog.numexp.durationExpressionNormalizer;

import java.util.List;

import jp.livlog.numexp.normalizerTemplate.NormalizerTemplate;
import jp.livlog.numexp.numberNormalizer.NumberNormalizer;
import jp.livlog.numexp.numberNormalizer.impl.NumberNormalizerImpl;
import jp.livlog.numexp.share.NNumber;
import jp.livlog.numexp.share.NumberModifier;
import jp.livlog.numexp.share.RefObject;

public abstract class DurationExpressionNormalizer extends NormalizerTemplate <DurationExpression, LimitedDurationExpression> {

    public DurationExpressionNormalizer(String language) {

        this.NN = new NumberNormalizerImpl(language);
        this.language = language;
        this.init();
    }


    @Override
    public abstract void init();


    @Override
    public abstract void normalizeNumber(StringBuilder uText, List <NNumber> numbers);


    @Override
    public abstract void reviseAnyTypeExpressionByMatchingLimitedExpression(List <DurationExpression> durationexps, RefObject <Integer> expressionId,
            LimitedDurationExpression matchingLimitedDurationExpression);


    @Override
    public abstract void reviseAnyTypeExpressionByMatchingPrefixCounter(DurationExpression anyTypeExpression,
            LimitedDurationExpression matchingLimitedExpression);


    @Override
    public abstract void reviseAnyTypeExpressionByNumberModifier(DurationExpression durationexp, NumberModifier numberModifier);


    @Override
    public abstract void deleteNotAnyTypeExpression(List <DurationExpression> durationexps);


    @Override
    public abstract void fixByRangeExpression(StringBuilder utext, List <DurationExpression> durationexps);

    public NumberNormalizer NN;
}
