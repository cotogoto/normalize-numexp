package jp.livlog.normalizeNumexp.normalizeNumexp.impl;

import java.util.ArrayList;
import java.util.List;

import jp.livlog.normalizeNumexp.abstimeExpressionNormalizer.impl.AbstimeExpressionImpl;
import jp.livlog.normalizeNumexp.normalizeNumexp.NormalizeNumexp;

public class NormalizeNumexpImpl extends NormalizeNumexp {

    @Override
    public void normalizeEachTypeExpressions(
            final String text,
            // ArrayList<numerical_expression_normalizer.NumericalExpression> numexps,
            List <AbstimeExpressionImpl> abstimeexps
    // ArrayList<reltime_expression_normalizer.ReltimeExpression> reltimeexps,
    // ArrayList<duration_expression_normalizer.DurationExpression> durationexps
    ) {

        // NEN.process(text, numexps);
        this.AEN.process(text, abstimeexps);
        // REN.process(text, reltimeexps);
        // DEN.process(text, durationexps);
    }


    public NormalizeNumexpImpl(final String language) {

        super(language);
        // this.NEN = language;
        // this.AEN = language;
        // this.REN = language;
        // this.DEN = language;
        // this.IER = language;
    }


    @Override
    public void normalize(final String text, List <String> result) {

        result.clear();
        // final ArrayList<numerical_expression_normalizer.NumericalExpression> numexps = new
        // ArrayList<numerical_expression_normalizer.NumericalExpression>();
        final List <AbstimeExpressionImpl> abstimeexps = new ArrayList <>();
        // final ArrayList<reltime_expression_normalizer.ReltimeExpression> reltimeexps = new
        // ArrayList<reltime_expression_normalizer.ReltimeExpression>();
        // final ArrayList<duration_expression_normalizer.DurationExpression> durationexps = new
        // ArrayList<duration_expression_normalizer.DurationExpression>();
        //
        // 4つのnormalizerで処理を行う
        this.normalizeEachTypeExpressions(text, abstimeexps);
        //
        // //それぞれの結果より、不適当な抽出を削除
        // IER.remove_inappropriate_extraction(text, numexps, abstimeexps, reltimeexps, durationexps);
        //
        // //string型に変換し、resultにまとめる
        // merge_normalize_expressions_into_result(new ArrayList<numerical_expression_normalizer.NumericalExpression>(numexps), new
        // ArrayList<abstime_expression_normalizer.AbstimeExpression>(abstimeexps), new
        // ArrayList<reltime_expression_normalizer.ReltimeExpression>(reltimeexps), new
        // ArrayList<duration_expression_normalizer.DurationExpression>(durationexps), result);
    }

}