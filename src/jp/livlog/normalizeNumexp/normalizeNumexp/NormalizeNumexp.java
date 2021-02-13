package jp.livlog.normalizeNumexp.normalizeNumexp;

import java.util.List;

import jp.livlog.normalizeNumexp.abstimeExpressionNormalizer.AbstimeExpressionNormalizer;
import jp.livlog.normalizeNumexp.abstimeExpressionNormalizer.impl.AbstimeExpressionNormalizerImpl;
import jp.livlog.normalizeNumexp.durationExpressionNormalizer.DurationExpressionNormalizer;
import jp.livlog.normalizeNumexp.durationExpressionNormalizer.impl.DurationExpressionNormalizerImpl;
import jp.livlog.normalizeNumexp.inappropriateExpressionRemover.InappropriateExpressionRemover;
import jp.livlog.normalizeNumexp.inappropriateExpressionRemover.impl.InappropriateExpressionRemoverImpl;
import jp.livlog.normalizeNumexp.numericalExpressionNormalizer.NumericalExpressionNormalizer;
import jp.livlog.normalizeNumexp.numericalExpressionNormalizer.impl.NumericalExpressionNormalizerImpl;
import jp.livlog.normalizeNumexp.reltimeExpressionNormalizer.ReltimeExpressionNormalizer;
import jp.livlog.normalizeNumexp.reltimeExpressionNormalizer.impl.ReltimeExpressionNormalizerImpl;

public abstract class NormalizeNumexp {

    public NormalizeNumexp(final String language) {

        this.NEN = new NumericalExpressionNormalizerImpl(language);
        this.AEN = new AbstimeExpressionNormalizerImpl(language);
        this.REN = new ReltimeExpressionNormalizerImpl(language);
        this.DEN = new DurationExpressionNormalizerImpl(language);
        this.IER = new InappropriateExpressionRemoverImpl(language);
    }


    public abstract void normalize(final String text, List <String> result);

    protected NumericalExpressionNormalizer  NEN = null;

    protected AbstimeExpressionNormalizer    AEN = null;

    protected ReltimeExpressionNormalizer    REN = null;

    protected DurationExpressionNormalizer   DEN = null;

    protected InappropriateExpressionRemover IER = null;

}
