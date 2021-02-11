package jp.livlog.normalizeNumexp.normalizeNumexp;

import java.util.List;

import jp.livlog.normalizeNumexp.abstimeExpressionNormalizer.AbstimeExpressionNormalizer;
import jp.livlog.normalizeNumexp.abstimeExpressionNormalizer.impl.AbstimeExpressionNormalizerImpl;
import jp.livlog.normalizeNumexp.durationExpressionNormalizer.DurationExpressionNormalizer;
import jp.livlog.normalizeNumexp.durationExpressionNormalizer.impl.DurationExpressionNormalizerImpl;
import jp.livlog.normalizeNumexp.numericalExpressionNormalizer.NumericalExpressionNormalizer;
import jp.livlog.normalizeNumexp.numericalExpressionNormalizer.impl.NumericalExpressionNormalizerImpl;

public abstract class NormalizeNumexp {

    public NormalizeNumexp(final String language) {

        this.NEN = new NumericalExpressionNormalizerImpl(language);
        this.AEN = new AbstimeExpressionNormalizerImpl(language);

        this.DEN = new DurationExpressionNormalizerImpl(language);
    }


    public abstract void normalize(final String text, List <String> result);




    protected NumericalExpressionNormalizer NEN = null;

    protected AbstimeExpressionNormalizer   AEN = null;

    // private final ReltimeExpressionNormalizer REN = new ReltimeExpressionNormalizer();
    //
    protected DurationExpressionNormalizer  DEN = null;
    //
    // private final InappropriateExpressionRemover IER = new InappropriateExpressionRemover();


}
