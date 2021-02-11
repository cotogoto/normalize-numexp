package jp.livlog.normalizeNumexp.reltimeExpressionNormalizer.impl;

import java.util.List;

import jp.livlog.normalizeNumexp.reltimeExpressionNormalizer.LimitedReltimeExpression;
import jp.livlog.normalizeNumexp.reltimeExpressionNormalizer.ReltimeExpression;
import jp.livlog.normalizeNumexp.reltimeExpressionNormalizer.ReltimeExpressionNormalizer;
import jp.livlog.normalizeNumexp.share.NNumber;
import jp.livlog.normalizeNumexp.share.NumberModifier;
import jp.livlog.normalizeNumexp.share.RefObject;

public class ReltimeExpressionNormalizerImpl extends ReltimeExpressionNormalizer {

    public ReltimeExpressionNormalizerImpl(String language) {

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
    public void reviseAnyTypeExpressionByMatchingLimitedExpression(List <ReltimeExpression> reltimeexps, RefObject <Integer> expressionId,
            LimitedReltimeExpression matchingLimitedReltimeExpression) {

        // TODO 自動生成されたメソッド・スタブ

    }


    @Override
    public void reviseAnyTypeExpressionByMatchingPrefixCounter(ReltimeExpression reltimeexp, LimitedReltimeExpression matchingLimitedExpression) {

        // TODO 自動生成されたメソッド・スタブ

    }


    @Override
    public void reviseAnyTypeExpressionByNumberModifier(ReltimeExpression reltimeexp, NumberModifier numberModifier) {

        // TODO 自動生成されたメソッド・スタブ

    }


    @Override
    public void deleteNotAnyTypeExpression(List <ReltimeExpression> reltimeexps) {

        // TODO 自動生成されたメソッド・スタブ

    }


    @Override
    public void fixByRangeExpression(StringBuilder uText, List <ReltimeExpression> reltimeexps) {

        // TODO 自動生成されたメソッド・スタブ

    }


    @Override
    public void loadFromDictionary1(String dictionaryPath, List <LimitedReltimeExpression> loadTarget) {

        // TODO 自動生成されたメソッド・スタブ

    }


    @Override
    public void convertNumbersToAnyTypeExpressions(List <NNumber> numbers, List <ReltimeExpression> anyTypeExpressions) {

        // TODO 自動生成されたメソッド・スタブ

    }

}
