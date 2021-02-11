package jp.livlog.normalizeNumexp.durationExpressionNormalizer.impl;

import java.util.List;

import jp.livlog.normalizeNumexp.durationExpressionNormalizer.DurationExpression;
import jp.livlog.normalizeNumexp.durationExpressionNormalizer.DurationExpressionNormalizer;
import jp.livlog.normalizeNumexp.durationExpressionNormalizer.LimitedDurationExpression;
import jp.livlog.normalizeNumexp.share.NNumber;
import jp.livlog.normalizeNumexp.share.NumberModifier;
import jp.livlog.normalizeNumexp.share.RefObject;

public class DurationExpressionNormalizerImpl extends DurationExpressionNormalizer {

    public DurationExpressionNormalizerImpl(String language) {

        super(language);
    }

    @Override
    public void init() {

        // TODO 自動生成されたメソッド・スタブ
        
    }

    @Override
    public void normalizeNumber(StringBuilder uText, List <NNumber> numbers) {

        // TODO 自動生成されたメソッド・スタブ
        
    }

    @Override
    public void reviseAnyTypeExpressionByMatchingLimitedExpression(List <DurationExpression> durationexps, RefObject <Integer> expressionId,
            LimitedDurationExpression matchingLimitedDurationExpression) {

        // TODO 自動生成されたメソッド・スタブ
        
    }

    @Override
    public void reviseAnyTypeExpressionByMatchingPrefixCounter(DurationExpression anyTypeExpression,
            LimitedDurationExpression matching_limited_expression) {

        // TODO 自動生成されたメソッド・スタブ
        
    }

    @Override
    public void reviseAnyTypeExpressionByNumberModifier(DurationExpression durationexp, NumberModifier numberModifier) {

        // TODO 自動生成されたメソッド・スタブ
        
    }

    @Override
    public void deleteNotAnyTypeExpression(List <DurationExpression> durationexps) {

        // TODO 自動生成されたメソッド・スタブ
        
    }

    @Override
    public void fixByRangeExpression(StringBuilder utext, List <DurationExpression> durationexps) {

        // TODO 自動生成されたメソッド・スタブ
        
    }

    @Override
    public void loadFromDictionary1(String dictionaryPath, List <LimitedDurationExpression> loadTarget) {

        // TODO 自動生成されたメソッド・スタブ
        
    }

    @Override
    public void convertNumbersToAnyTypeExpressions(List <NNumber> numbers, List <DurationExpression> anyTypeExpressions) {

        // TODO 自動生成されたメソッド・スタブ
        
    }

}
