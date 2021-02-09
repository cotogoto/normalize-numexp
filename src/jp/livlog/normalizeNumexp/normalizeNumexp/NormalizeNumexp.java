package jp.livlog.normalizeNumexp.normalizeNumexp;

import java.util.List;

import jp.livlog.normalizeNumexp.abstimeExpressionNormalizer.AbstimeExpressionNormalizer;
import jp.livlog.normalizeNumexp.abstimeExpressionNormalizer.impl.AbstimeExpressionImpl;
import jp.livlog.normalizeNumexp.abstimeExpressionNormalizer.impl.AbstimeExpressionNormalizerImpl;

public abstract class NormalizeNumexp {

    public NormalizeNumexp(final String language) {

        this.AEN = new AbstimeExpressionNormalizerImpl(language);
    }


    public abstract void normalize(final String text, List <String> result);


    protected abstract void normalizeEachTypeExpressions(final String text,
            // List<numerical_expression_normalizer::NumericalExpression> numexps,
            List <AbstimeExpressionImpl> abstimeexps
    // List<reltime_expression_normalizer::ReltimeExpression> reltimeexps,
    // List<duration_expression_normalizer::DurationExpression> durationexps

    );

    // private final NumericalExpressionNormalizer NEN = new NumericalExpressionNormalizer();

    protected AbstimeExpressionNormalizer AEN = null;

    // private final ReltimeExpressionNormalizer REN = new ReltimeExpressionNormalizer();
    //
    // private final DurationExpressionNormalizer DEN = new DurationExpressionNormalizer();
    //
    // private final InappropriateExpressionRemover IER = new InappropriateExpressionRemover();
}
