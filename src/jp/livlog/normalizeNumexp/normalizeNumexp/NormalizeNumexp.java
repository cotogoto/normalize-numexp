package jp.livlog.normalizeNumexp.normalizeNumexp;

import java.util.List;

import jp.livlog.normalizeNumexp.abstimeExpressionNormalizer.AbstimeExpression;
import jp.livlog.normalizeNumexp.abstimeExpressionNormalizer.AbstimeExpressionNormalizer;
import jp.livlog.normalizeNumexp.abstimeExpressionNormalizer.impl.AbstimeExpressionNormalizerImpl;
import jp.livlog.normalizeNumexp.durationExpressionNormalizer.DurationExpression;
import jp.livlog.normalizeNumexp.durationExpressionNormalizer.DurationExpressionNormalizer;
import jp.livlog.normalizeNumexp.durationExpressionNormalizer.impl.DurationExpressionNormalizerImpl;
import jp.livlog.normalizeNumexp.inappropriateExpressionRemover.InappropriateExpressionRemover;
import jp.livlog.normalizeNumexp.inappropriateExpressionRemover.impl.InappropriateExpressionRemoverImpl;
import jp.livlog.normalizeNumexp.normalizerUtility.NormalizedExpressionTemplate;
import jp.livlog.normalizeNumexp.numericalExpressionNormalizer.NumericalExpression;
import jp.livlog.normalizeNumexp.numericalExpressionNormalizer.NumericalExpressionNormalizer;
import jp.livlog.normalizeNumexp.numericalExpressionNormalizer.impl.NumericalExpressionNormalizerImpl;
import jp.livlog.normalizeNumexp.reltimeExpressionNormalizer.ReltimeExpression;
import jp.livlog.normalizeNumexp.reltimeExpressionNormalizer.ReltimeExpressionNormalizer;
import jp.livlog.normalizeNumexp.reltimeExpressionNormalizer.impl.ReltimeExpressionNormalizerImpl;
import jp.livlog.normalizeNumexp.share.Symbol;

public abstract class NormalizeNumexp {

    public NormalizeNumexp(final String language) {

        this.NEN = new NumericalExpressionNormalizerImpl(language);
        this.AEN = new AbstimeExpressionNormalizerImpl(language);
        this.REN = new ReltimeExpressionNormalizerImpl(language);
        this.DEN = new DurationExpressionNormalizerImpl(language);
        this.IER = new InappropriateExpressionRemoverImpl(language);
    }


    // resultの生成
    protected void mergeNormalizeExpressionsIntoResult(
            List <NumericalExpression> numexps,
            List <AbstimeExpression> abstimeexps,
            List <ReltimeExpression> reltimeexps,
            List <DurationExpression> durationexps,
            List <String> result) {

        // TODO : それぞれの正規形に、toString関数をつける？逆に分かり辛い？ とりあえずここで処理
        final var kugiri = Symbol.ASTERISK;
        final var comma = Symbol.COMMA;
        final var ss = new StringBuilder();
        result.clear();

        for (var i = 0; i < numexps.size(); i++) {
            ss.setLength(0);
            ss.append("numerical");
            ss.append(kugiri);
            ss.append(numexps.get(i).originalExpression);
            ss.append(kugiri);
            ss.append(numexps.get(i).positionStart);
            ss.append(kugiri);
            ss.append(numexps.get(i).positionEnd);
            ss.append(kugiri);
            ss.append(numexps.get(i).counter);
            ss.append(kugiri);
            ss.append(numexps.get(i).valueLowerbound);
            ss.append(kugiri);
            ss.append(numexps.get(i).valueUpperbound);
            ss.append(kugiri);
            ss.append(this.showOptions(numexps.get(i)));
            result.add(ss.toString());
        }

        for (var i = 0; i < abstimeexps.size(); i++) {
            ss.setLength(0);
            ss.append("abstime");
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
            // TODO : 相対時間表現を、どう表示させるか？
            ss.append("reltime");
            ss.append(kugiri);
            ss.append(reltimeexps.get(i).originalExpression);
            ss.append(kugiri);
            ss.append(reltimeexps.get(i).positionStart);
            ss.append(kugiri);
            ss.append(reltimeexps.get(i).positionEnd);
            ss.append(kugiri);
            ss.append("none");
            ss.append(kugiri);
            ss.append(reltimeexps.get(i).valueLowerbound.toString(false));
            ss.append(comma);
            ss.append(reltimeexps.get(i).valueLowerbound.toDurationString(false));
            ss.append(kugiri);
            ss.append(abstimeexps.get(i).valueUpperbound.toString(true));
            ss.append(comma);
            ss.append(reltimeexps.get(i).valueLowerbound.toDurationString(true));
            ss.append(kugiri);
            ss.append(this.showOptions(reltimeexps.get(i)));
            result.add(ss.toString());
        }

        for (var i = 0; i < durationexps.size(); i++) {
            ss.setLength(0);
            ss.append("duration");
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
                ss.append(Symbol.COMMA);
            }
        }

        return ss.toString();
    }


    public abstract void normalize(final String text, List <String> result);

    protected NumericalExpressionNormalizer  NEN = null;

    protected AbstimeExpressionNormalizer    AEN = null;

    protected ReltimeExpressionNormalizer    REN = null;

    protected DurationExpressionNormalizer   DEN = null;

    protected InappropriateExpressionRemover IER = null;

}
