package jp.livlog.normalizeNumexp.numericalExpressionNormalizer;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jp.livlog.normalizeNumexp.digitUtility.DigitUtility;
import jp.livlog.normalizeNumexp.digitUtility.impl.DigitUtilityImpl;
import jp.livlog.normalizeNumexp.numericalExpressionNormalizer.impl.JapaneseNumberConverterImpl;
import jp.livlog.normalizeNumexp.numericalExpressionNormalizer.impl.NumberExtractorImpl;
import jp.livlog.normalizeNumexp.share.NNumber;
import jp.livlog.normalizeNumexp.share.RefObject;

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

        final var input = "三年前に１２３４５円が奪われたのは123,456円の";
        final List <NNumber> result = new ArrayList <>();
        this.ne.extractNumber(input, result);
        org.junit.Assert.assertEquals(4, result.size());
        org.junit.Assert.assertEquals("三", result.get(0).originalExpression);
        org.junit.Assert.assertEquals("１２３４５", result.get(1).originalExpression);
        org.junit.Assert.assertEquals("123", result.get(2).originalExpression);
        org.junit.Assert.assertEquals("456", result.get(3).originalExpression);
    }


    @Test
    void testExtractTypeMixedNumber() {

        final var input = "1989三年前におきたその事件。";
        final List <NNumber> result = new ArrayList <>();
        this.ne.extractNumber(input, result);
        org.junit.Assert.assertEquals(2, result.size());
        org.junit.Assert.assertNotEquals("1989三", result.get(0).originalExpression);
        org.junit.Assert.assertEquals("1989", result.get(0).originalExpression);
        org.junit.Assert.assertEquals("三", result.get(1).originalExpression);
    }

    @Test
    void testConvert1() {

        final var input = "１，２３４";
        final NumberConverterTemplate nc = new JapaneseNumberConverterImpl();
        final var value = new RefObject <>(0d);
        final var numberType =  new RefObject <>(0);
        nc.convertNumber(input, value, numberType);
        org.junit.Assert.assertEquals(1234.0, value);
    }

    @Test
    void testConvert2() {

//        final var input = "1989三年前におきたその事件。";
//        final List <NNumber> result = new ArrayList <>();
//        this.ne.extractNumber(input, result);
//        org.junit.Assert.assertEquals(2, result.size());
//        org.junit.Assert.assertNotEquals("1989三", result.get(0).originalExpression);
//        org.junit.Assert.assertEquals("1989", result.get(0).originalExpression);
//        org.junit.Assert.assertEquals("三", result.get(1).originalExpression);
    }

    @Test
    void testConvert3() {

//        final var input = "1989三年前におきたその事件。";
//        final List <NNumber> result = new ArrayList <>();
//        this.ne.extractNumber(input, result);
//        org.junit.Assert.assertEquals(2, result.size());
//        org.junit.Assert.assertNotEquals("1989三", result.get(0).originalExpression);
//        org.junit.Assert.assertEquals("1989", result.get(0).originalExpression);
//        org.junit.Assert.assertEquals("三", result.get(1).originalExpression);
    }
}
