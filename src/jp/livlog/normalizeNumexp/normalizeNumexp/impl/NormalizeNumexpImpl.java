package jp.livlog.normalizeNumexp.normalizeNumexp.impl;

import java.util.ArrayList;
import java.util.List;

import jp.livlog.normalizeNumexp.abstimeExpressionNormalizer.AbstimeExpression;
import jp.livlog.normalizeNumexp.durationExpressionNormalizer.DurationExpression;
import jp.livlog.normalizeNumexp.normalizeNumexp.NormalizeNumexp;
import jp.livlog.normalizeNumexp.numericalExpressionNormalizer.NumericalExpression;
import jp.livlog.normalizeNumexp.reltimeExpressionNormalizer.ReltimeExpression;

public class NormalizeNumexpImpl extends NormalizeNumexp {

    private void normalizeEachTypeExpressions(
            final String text,
            List <NumericalExpression> numexps,
            List <AbstimeExpression> abstimeexps,
            List <ReltimeExpression> reltimeexps,
            List <DurationExpression> durationexps) {

        this.NEN.process(text, numexps);
        this.AEN.process(text, abstimeexps);
        this.REN.process(text, reltimeexps);
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
        final var reltimeexps = new ArrayList <ReltimeExpression>();
        final var durationexps = new ArrayList <DurationExpression>();

        // 4つのnormalizerで処理を行う
        this.normalizeEachTypeExpressions(text, numexps, abstimeexps, reltimeexps, durationexps);

        // それぞれの結果より、不適当な抽出を削除
        this.IER.removeInappropriateExtraction(text, numexps, abstimeexps, reltimeexps, durationexps);

        // string型に変換し、resultにまとめる
        this.mergeNormalizeExpressionsIntoResult(
                new ArrayList <>(numexps),
                new ArrayList <>(abstimeexps),
                new ArrayList <>(reltimeexps),
                new ArrayList <>(durationexps),
                result);

    }

}