package jp.livlog.numexp.normalizeNumexp;

import java.util.List;

import jp.livlog.numexp.abstimeExpressionNormalizer.AbstimeExpressionNormalizer;
import jp.livlog.numexp.abstimeExpressionNormalizer.impl.AbstimeExpressionNormalizerImpl;
import jp.livlog.numexp.durationExpressionNormalizer.DurationExpressionNormalizer;
import jp.livlog.numexp.durationExpressionNormalizer.impl.DurationExpressionNormalizerImpl;
import jp.livlog.numexp.inappropriateExpressionRemover.InappropriateExpressionRemover;
import jp.livlog.numexp.inappropriateExpressionRemover.impl.InappropriateExpressionRemoverImpl;
import jp.livlog.numexp.numericalExpressionNormalizer.NumericalExpressionNormalizer;
import jp.livlog.numexp.numericalExpressionNormalizer.impl.NumericalExpressionNormalizerImpl;
import jp.livlog.numexp.reltimeExpressionNormalizer.ReltimeExpressionNormalizer;
import jp.livlog.numexp.reltimeExpressionNormalizer.impl.ReltimeExpressionNormalizerImpl;
import jp.livlog.numexp.share.Expression;

public abstract class NormalizeNumexp {

    protected NumericalExpressionNormalizer  NEN = null;

    protected AbstimeExpressionNormalizer    AEN = null;

    protected ReltimeExpressionNormalizer    REN = null;

    protected DurationExpressionNormalizer   DEN = null;

    protected InappropriateExpressionRemover IER = null;

    public NormalizeNumexp(String language) {

        this.NEN = new NumericalExpressionNormalizerImpl(language);
        this.AEN = new AbstimeExpressionNormalizerImpl(language);
        this.REN = new ReltimeExpressionNormalizerImpl(language);
        this.DEN = new DurationExpressionNormalizerImpl(language);
        this.IER = new InappropriateExpressionRemoverImpl(language);
    }


    public abstract List <String> normalize(String textt);


    public abstract List <Expression> normalizeData(String text);

}
