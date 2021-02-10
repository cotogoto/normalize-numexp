package jp.livlog.normalizeNumexp.normalizeNumexp;

import java.util.List;

import jp.livlog.normalizeNumexp.abstimeExpressionNormalizer.AbstimeExpression;
import jp.livlog.normalizeNumexp.abstimeExpressionNormalizer.AbstimeExpressionNormalizer;
import jp.livlog.normalizeNumexp.abstimeExpressionNormalizer.impl.AbstimeExpressionNormalizerImpl;
import jp.livlog.normalizeNumexp.numericalExpressionNormalizer.NumericalExpression;
import jp.livlog.normalizeNumexp.numericalExpressionNormalizer.NumericalExpressionNormalizer;
import jp.livlog.normalizeNumexp.numericalExpressionNormalizer.impl.NumericalExpressionNormalizerImpl;

public abstract class NormalizeNumexp {

    public NormalizeNumexp(final String language) {

        this.NEN = new NumericalExpressionNormalizerImpl(language);
        this.AEN = new AbstimeExpressionNormalizerImpl(language);
    }


    public abstract void normalize(final String text, List <String> result);


    protected abstract void normalizeEachTypeExpressions(final String text,
            List <NumericalExpression> numexps,
            List <AbstimeExpression> abstimeexps
    // List<reltime_expression_normalizer::ReltimeExpression> reltimeexps,
    // List<duration_expression_normalizer::DurationExpression> durationexps

    );

    protected NumericalExpressionNormalizer NEN = null;

    protected AbstimeExpressionNormalizer   AEN = null;

    // private final ReltimeExpressionNormalizer REN = new ReltimeExpressionNormalizer();
    //
    // private final DurationExpressionNormalizer DEN = new DurationExpressionNormalizer();
    //
    // private final InappropriateExpressionRemover IER = new InappropriateExpressionRemover();
}
