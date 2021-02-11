package jp.livlog.normalizeNumexp.normalizeNumexp.impl;

import java.util.ArrayList;
import java.util.List;

import jp.livlog.normalizeNumexp.abstimeExpressionNormalizer.AbstimeExpression;
import jp.livlog.normalizeNumexp.durationExpressionNormalizer.DurationExpression;
import jp.livlog.normalizeNumexp.normalizeNumexp.NormalizeNumexp;
import jp.livlog.normalizeNumexp.numericalExpressionNormalizer.NumericalExpression;

public class NormalizeNumexpImpl extends NormalizeNumexp {

    public void normalizeEachTypeExpressions(
            final String text,
            List <NumericalExpression> numexps,
            List <AbstimeExpression> abstimeexps,
            // ArrayList<reltime_expression_normalizer.ReltimeExpression> reltimeexps,
            List <DurationExpression> durationexps) {

        this.NEN.process(text, numexps);
        this.AEN.process(text, abstimeexps);
        // REN.process(text, reltimeexps);
        this.DEN.process(text, durationexps);
    }


    public NormalizeNumexpImpl(final String language) {

        super(language);
    }


    @Override
    public void normalize(final String text, List <String> result) {

        result.clear();
        final var numexps = new ArrayList <NumericalExpression>();
        final var abstimeexps = new ArrayList <AbstimeExpression>();
        // final ArrayList<reltime_expression_normalizer.ReltimeExpression> reltimeexps = new
        // ArrayList<reltime_expression_normalizer.ReltimeExpression>();
        final var durationexps = new ArrayList <DurationExpression>();

        // 4つのnormalizerで処理を行う
        this.normalizeEachTypeExpressions(text, numexps, abstimeexps, durationexps);
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