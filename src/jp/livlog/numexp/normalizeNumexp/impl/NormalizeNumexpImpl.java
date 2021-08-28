package jp.livlog.numexp.normalizeNumexp.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jp.livlog.numexp.abstimeExpressionNormalizer.AbstimeExpression;
import jp.livlog.numexp.durationExpressionNormalizer.DurationExpression;
import jp.livlog.numexp.normalizeNumexp.NormalizeNumexp;
import jp.livlog.numexp.normalizerUtility.NormalizedExpressionTemplate;
import jp.livlog.numexp.numericalExpressionNormalizer.NumericalExpression;
import jp.livlog.numexp.reltimeExpressionNormalizer.ReltimeExpression;
import jp.livlog.numexp.share.Expression;
import jp.livlog.numexp.share.ExpressionKey0Comp;
import jp.livlog.numexp.share.NumexpSymbol;

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


    /**
     * resultの生成.
     * @param numexps
     * @param abstimeexps
     * @param reltimeexps
     * @param durationexps
     * @param result
     */
    protected List <String> mergeNormalizeExpressionsIntoResult(
            List <NumericalExpression> numexps,
            List <AbstimeExpression> abstimeexps,
            List <ReltimeExpression> reltimeexps,
            List <DurationExpression> durationexps) {

        final List <String> result = new ArrayList <>();

        final var kugiri = NumexpSymbol.ASTERISK;
        final var comma = NumexpSymbol.COMMA;
        final var ss = new StringBuilder();

        for (var i = 0; i < numexps.size(); i++) {
            ss.setLength(0);
            ss.append(NumexpSymbol.NUMERICAL);
            ss.append(kugiri);
            ss.append(numexps.get(i).originalExpression);
            ss.append(kugiri);
            ss.append(numexps.get(i).positionStart);
            ss.append(kugiri);
            ss.append(numexps.get(i).positionEnd);
            ss.append(kugiri);
            ss.append(numexps.get(i).counter);
            ss.append(kugiri);
            ss.append(this.format(numexps.get(i).valueLowerbound));
            ss.append(kugiri);
            ss.append(this.format(numexps.get(i).valueUpperbound));
            ss.append(kugiri);
            ss.append(this.showOptions(numexps.get(i)));
            result.add(ss.toString());
        }

        for (var i = 0; i < abstimeexps.size(); i++) {
            ss.setLength(0);
            ss.append(NumexpSymbol.ABSTIME);
            ss.append(kugiri);
            ss.append(abstimeexps.get(i).originalExpression);
            ss.append(kugiri);
            ss.append(abstimeexps.get(i).positionStart);
            ss.append(kugiri);
            ss.append(abstimeexps.get(i).positionEnd);
            ss.append(kugiri);
            ss.append("none");
            ss.append(kugiri);
            ss.append(abstimeexps.get(i).valueLowerbound.toString(false));
            ss.append(kugiri);
            ss.append(abstimeexps.get(i).valueUpperbound.toString(true));
            ss.append(kugiri);
            ss.append(this.showOptions(abstimeexps.get(i)));
            result.add(ss.toString());
        }

        for (var i = 0; i < reltimeexps.size(); i++) {
            ss.setLength(0);
            ss.append(NumexpSymbol.RELTIME);
            ss.append(kugiri);
            ss.append(reltimeexps.get(i).originalExpression);
            ss.append(kugiri);
            ss.append(reltimeexps.get(i).positionStart);
            ss.append(kugiri);
            ss.append(reltimeexps.get(i).positionEnd);
            ss.append(kugiri);
            ss.append("none");
            ss.append(kugiri);
            ss.append(reltimeexps.get(i).valueLowerboundAbs.toString(false));
            ss.append(comma);
            ss.append(reltimeexps.get(i).valueLowerboundRel.toDurationString(false));
            ss.append(kugiri);
            ss.append(reltimeexps.get(i).valueUpperboundAbs.toString(true));
            ss.append(comma);
            ss.append(reltimeexps.get(i).valueUpperboundRel.toDurationString(true));
            ss.append(kugiri);
            ss.append(this.showOptions(reltimeexps.get(i)));
            result.add(ss.toString());
        }

        for (var i = 0; i < durationexps.size(); i++) {
            ss.setLength(0);
            ss.append(NumexpSymbol.DURATION);
            ss.append(kugiri);
            ss.append(durationexps.get(i).originalExpression);
            ss.append(kugiri);
            ss.append(durationexps.get(i).positionStart);
            ss.append(kugiri);
            ss.append(durationexps.get(i).positionEnd);
            ss.append(kugiri);
            ss.append("none");
            ss.append(kugiri);
            ss.append(durationexps.get(i).valueLowerbound.toDurationString(false));
            ss.append(kugiri);
            ss.append(durationexps.get(i).valueUpperbound.toDurationString(true));
            ss.append(kugiri);
            ss.append(this.showOptions(durationexps.get(i)));
            result.add(ss.toString());
        }

        return result;
    }


    /**
     * データの生成.
     * @param numexps
     * @param abstimeexps
     * @param reltimeexps
     * @param durationexps
     */
    protected List <Expression> mergeNormalizeExpressionsIntoData(
            List <NumericalExpression> numexps,
            List <AbstimeExpression> abstimeexps,
            List <ReltimeExpression> reltimeexps,
            List <DurationExpression> durationexps) {

        final List <Expression> ret = new ArrayList <>();
        Expression expression = null;

        for (var i = 0; i < numexps.size(); i++) {
            expression = new Expression();
            expression.type = NumexpSymbol.NUMERICAL;
            expression.originalExpression = numexps.get(i).originalExpression;
            expression.positionStart = numexps.get(i).positionStart;
            expression.positionEnd = numexps.get(i).positionEnd;
            expression.counter = numexps.get(i).counter;
            expression.valueLowerbound = String.valueOf(numexps.get(i).valueLowerbound);
            expression.valueUpperbound = String.valueOf(numexps.get(i).valueUpperbound);
            expression.options = this.showOptions(numexps.get(i));
            ret.add(expression);
        }

        for (var i = 0; i < abstimeexps.size(); i++) {
            expression = new Expression();
            expression.type = NumexpSymbol.ABSTIME;
            expression.originalExpression = abstimeexps.get(i).originalExpression;
            expression.positionStart = abstimeexps.get(i).positionStart;
            expression.positionEnd = abstimeexps.get(i).positionEnd;
            expression.counter = "none";
            expression.valueLowerbound = abstimeexps.get(i).valueLowerbound.toSlashString(false);
            expression.valueUpperbound = abstimeexps.get(i).valueUpperbound.toSlashString(true);
            expression.options = this.showOptions(abstimeexps.get(i));
            ret.add(expression);
        }

        for (var i = 0; i < reltimeexps.size(); i++) {
            expression = new Expression();
            expression.type = NumexpSymbol.RELTIME;
            expression.originalExpression = reltimeexps.get(i).originalExpression;
            expression.positionStart = reltimeexps.get(i).positionStart;
            expression.positionEnd = reltimeexps.get(i).positionEnd;
            expression.counter = "none";
            expression.valueLowerboundAbs = reltimeexps.get(i).valueLowerboundAbs.toSlashString(false);
            expression.valueUpperboundAbs = reltimeexps.get(i).valueUpperboundAbs.toSlashString(true);
            expression.valueLowerboundRel = reltimeexps.get(i).valueLowerboundRel.toDurationString(false);
            expression.valueUpperboundRel = reltimeexps.get(i).valueUpperboundRel.toDurationString(true);
            expression.options = this.showOptions(reltimeexps.get(i));
            ret.add(expression);
        }

        for (var i = 0; i < durationexps.size(); i++) {
            expression = new Expression();
            expression.type = NumexpSymbol.DURATION;
            expression.originalExpression = durationexps.get(i).originalExpression;
            expression.positionStart = durationexps.get(i).positionStart;
            expression.positionEnd = durationexps.get(i).positionEnd;
            expression.counter = "none";
            expression.valueLowerbound = durationexps.get(i).valueLowerbound.toDurationString(false);
            expression.valueUpperbound = durationexps.get(i).valueUpperbound.toDurationString(true);
            expression.options = this.showOptions(durationexps.get(i));
            ret.add(expression);
        }

        // 単語の出現順にソート
        Collections.sort(ret, new ExpressionKey0Comp());

        return ret;
    }


    private <AnyTypeExpression extends NormalizedExpressionTemplate> String showOptions(AnyTypeExpression anyTypeExpression) {

        final var ss = new StringBuilder();
        if (anyTypeExpression.ordinary) {
            anyTypeExpression.options.add("ordinary");
        }
        final var sz = anyTypeExpression.options.size();
        for (var i = 0; i < sz; i++) {
            if (anyTypeExpression.options.get(i).equals("")) {
                continue;
            }
            ss.append(anyTypeExpression.options.get(i));
            if (i != sz - 1) {
                ss.append(NumexpSymbol.COMMA);
            }
        }

        return ss.toString();
    }


    public String format(double d) {

        if (d == (int) d) {
            return String.format("%d", (int) d);
        } else {
            return String.format("%s", d);
        }
    }


    public NormalizeNumexpImpl(final String language) {

        super(language);
    }


    @Override
    public List <String> normalize(final String text) {

        final var numexps = new ArrayList <NumericalExpression>();
        final var abstimeexps = new ArrayList <AbstimeExpression>();
        final var reltimeexps = new ArrayList <ReltimeExpression>();
        final var durationexps = new ArrayList <DurationExpression>();

        // 4つのnormalizerで処理を行う
        this.normalizeEachTypeExpressions(text, numexps, abstimeexps, reltimeexps, durationexps);

        // それぞれの結果より、不適当な抽出を削除
        this.IER.removeInappropriateExtraction(text, numexps, abstimeexps, reltimeexps, durationexps);

        // string型に変換し、resultにまとめる
        return this.mergeNormalizeExpressionsIntoResult(numexps, abstimeexps, reltimeexps, durationexps);
    }


    @Override
    public List <Expression> normalizeData(String text) {

        final var numexps = new ArrayList <NumericalExpression>();
        final var abstimeexps = new ArrayList <AbstimeExpression>();
        final var reltimeexps = new ArrayList <ReltimeExpression>();
        final var durationexps = new ArrayList <DurationExpression>();

        // 4つのnormalizerで処理を行う
        this.normalizeEachTypeExpressions(text, numexps, abstimeexps, reltimeexps, durationexps);

        // それぞれの結果より、不適当な抽出を削除
        this.IER.removeInappropriateExtraction(text, numexps, abstimeexps, reltimeexps, durationexps);

        return this.mergeNormalizeExpressionsIntoData(numexps, abstimeexps, reltimeexps, durationexps);
    }

}
