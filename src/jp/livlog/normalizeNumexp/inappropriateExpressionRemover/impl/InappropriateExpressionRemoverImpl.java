package jp.livlog.normalizeNumexp.inappropriateExpressionRemover.impl;

import java.util.ArrayList;
import java.util.List;

import jp.livlog.normalizeNumexp.abstimeExpressionNormalizer.AbstimeExpression;
import jp.livlog.normalizeNumexp.durationExpressionNormalizer.DurationExpression;
import jp.livlog.normalizeNumexp.inappropriateExpressionRemover.InappropriateExpressionRemover;
import jp.livlog.normalizeNumexp.numericalExpressionNormalizer.NumericalExpression;
import jp.livlog.normalizeNumexp.reltimeExpressionNormalizer.ReltimeExpression;

public class InappropriateExpressionRemoverImpl extends InappropriateExpressionRemover {

    public InappropriateExpressionRemoverImpl(String language) {

        super(language);
        // TODO 自動生成されたコンストラクター・スタブ
    }


    @Override
    public void removeInappropriateExtraction(String text, List <NumericalExpression> numexps, List <AbstimeExpression> abstimeexps,
            List <ReltimeExpression> reltimeexps, List <DurationExpression> durationexps) {

        // TODO 自動生成されたメソッド・スタブ

    }


    @Override
    protected <AnyTypeExpression> void deleteInappropriateExtractionUsingDictionaryOneType(ArrayList <AnyTypeExpression> any_type_expressions) {

        // TODO 自動生成されたメソッド・スタブ

    }


    @Override
    protected <AnyTypeExpression> boolean isUrlStrings(String text, AnyTypeExpression anyTypeExpression) {

        // TODO 自動生成されたメソッド・スタブ
        return false;
    }


    @Override
    protected <AnyTypeExpression> void deleteUrlStrings(String text, ArrayList <AnyTypeExpression> anyTypeExpressions) {

        // TODO 自動生成されたメソッド・スタブ

    }


    @Override
    protected void deleteInappropriateExtractionUsingDictionary(String text, List <NumericalExpression> numexps, List <AbstimeExpression> abstimeexps,
            List <ReltimeExpression> reltimeexps, List <DurationExpression> durationexps) {

        // TODO 自動生成されたメソッド・スタブ

    }


    @Override
    protected void initInappropriateStringss(String language) {

        // TODO 自動生成されたメソッド・スタブ

    }


    @Override
    protected void initUrlStrings() {

        // TODO 自動生成されたメソッド・スタブ

    }

}
