package jp.livlog.normalizeNumexp.numericalExpressionNormalizer;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jp.livlog.normalizeNumexp.numericalExpressionNormalizer.impl.NumericalExpressionNormalizerImpl;

class NumericalExpressionNormalizerTest {

    NumericalExpressionNormalizer NEN = null;

    private boolean isSameNumexp(final NumericalExpression n1, final NumericalExpression n2) {

        return n1.originalExpression.equals(n2.originalExpression)  && n1.positionStart == n2.positionStart && n1.positionEnd == n2.positionEnd
                && n1.valueLowerbound == n2.valueLowerbound && n1.valueUpperbound == n2.valueUpperbound && n1.counter.equals(n2.counter);
    }


    @BeforeEach
    public void initialize() {

        // this.digitUtility = new DigitUtilityImpl();
        // this.digitUtility.initKansuji("ja");
        // this.ne = new NumberExtractorImpl(this.digitUtility);
    }


    @Test
    void simple1() {

        final var text = "その三人が死んだ";
        final List <NumericalExpression> numexps = new ArrayList <>();
        final var language = "ja";
        this.NEN = new NumericalExpressionNormalizerImpl(language);
        this.NEN.process(text, numexps);
        System.out.println(numexps.get(0));
        org.junit.Assert.assertEquals(1, numexps.size());
        final var ex = new NumericalExpression("三人", 2, 4, 3, 3);
        ex.counter = "人";
        org.junit.Assert.assertTrue(this.isSameNumexp(ex, numexps.get(0)));
    }

}
