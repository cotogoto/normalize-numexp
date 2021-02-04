package jp.livlog.normalizeNumexp.numericalExpressionNormalizer;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jp.livlog.normalizeNumexp.digitUtility.DigitUtility;
import jp.livlog.normalizeNumexp.digitUtility.impl.DigitUtilityImpl;
import jp.livlog.normalizeNumexp.numericalExpressionNormalizer.impl.NumberExtractorImpl;
import jp.livlog.normalizeNumexp.share.NNumber;

class NumberNormalizerTest {

    NumberExtractor ne = null;

    @BeforeEach
    public void initialize() {

        final DigitUtility digitUtility = new DigitUtilityImpl();
        digitUtility.initKansuji("ja");
        this.ne = new NumberExtractorImpl(digitUtility);
    }


    @Test
    void testExtractOneNumber() {

        final var input = "それは三十二年前の出来事";
        final List <NNumber> result = new ArrayList <>();
        this.ne.extractNumber(input, result);
        org.junit.Assert.assertEquals(1, result.size());
        org.junit.Assert.assertEquals("三十二", result.get(0).originalExpression);
    }



    @Test
    void testExtractSomeNumber() {
        final var  input="三年前に１２３４５円が奪われたのは123,456円の";
        final List <NNumber> result = new ArrayList <>();
        this.ne.extractNumber(input, result);
        org.junit.Assert.assertEquals(4, result.size());
        org.junit.Assert.assertEquals("三", result.get(0).originalExpression);
        org.junit.Assert.assertEquals("１２３４５", result.get(1).originalExpression);
        org.junit.Assert.assertEquals("123", result.get(2).originalExpression);
        org.junit.Assert.assertEquals("456", result.get(3).originalExpression);
    }
}
