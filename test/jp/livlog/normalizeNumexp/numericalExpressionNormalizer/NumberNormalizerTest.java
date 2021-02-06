package jp.livlog.normalizeNumexp.numericalExpressionNormalizer;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jp.livlog.normalizeNumexp.digitUtility.DigitUtility;
import jp.livlog.normalizeNumexp.digitUtility.impl.DigitUtilityImpl;
import jp.livlog.normalizeNumexp.numericalExpressionNormalizer.impl.JapaneseNumberConverterImpl;
import jp.livlog.normalizeNumexp.numericalExpressionNormalizer.impl.NumberExtractorImpl;
import jp.livlog.normalizeNumexp.numericalExpressionNormalizer.impl.NumberNormalizerImpl;
import jp.livlog.normalizeNumexp.share.NNumber;
import jp.livlog.normalizeNumexp.share.RefObject;

class NumberNormalizerTest {

    DigitUtility     digitUtility = null;

    NumberExtractor  ne           = null;

    NumberNormalizer nn           = null;

    @BeforeEach
    public void initialize() {

        this.digitUtility = new DigitUtilityImpl();
        this.digitUtility.initKansuji("ja");
        this.ne = new NumberExtractorImpl(this.digitUtility);
    }


    @Test
    void extractOneNumberTest() {

        final var input = "それは三十二年前の出来事";
        final List <NNumber> result = new ArrayList <>();
        this.ne.extractNumber(input, result);
        org.junit.Assert.assertEquals(1, result.size());
        org.junit.Assert.assertEquals("三十二", result.get(0).originalExpression);
    }


    @Test
    void extractSomeNumberTest() {

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
    void extractTypeMixedNumber() {

        final var input = "1989三年前におきたその事件。";
        final List <NNumber> result = new ArrayList <>();
        this.ne.extractNumber(input, result);
        org.junit.Assert.assertEquals(2, result.size());
        org.junit.Assert.assertNotEquals("1989三", result.get(0).originalExpression);
        org.junit.Assert.assertEquals("1989", result.get(0).originalExpression);
        org.junit.Assert.assertEquals("三", result.get(1).originalExpression);
    }


    @Test
    void convertTest1() {

        final var input = "１，２３４";
        final NumberConverterTemplate nc = new JapaneseNumberConverterImpl(this.digitUtility);
        final var value = new RefObject <>(0d);
        final var numberType = new RefObject <>(0);
        nc.convertNumber(input, value, numberType);
        org.junit.Assert.assertTrue(1234.0 == value.argValue);
    }


    @Test
    void convertTest2() {

        final var input = "１，２３４，５６７";
        final NumberConverterTemplate nc = new JapaneseNumberConverterImpl(this.digitUtility);
        final var value = new RefObject <>(0d);
        final var numberType = new RefObject <>(0);
        nc.convertNumber(input, value, numberType);
        org.junit.Assert.assertTrue(1234567.0 == value.argValue);
    }


    @Test
    void convertTest3() {

        final var input = "一二三四五六七";
        final NumberConverterTemplate nc = new JapaneseNumberConverterImpl(this.digitUtility);
        final var value = new RefObject <>(0d);
        final var numberType = new RefObject <>(0);
        nc.convertNumber(input, value, numberType);
        org.junit.Assert.assertTrue(1234567.0 == value.argValue);
    }


    @Test
    void convertTest4() {

        final var input = "123万4567";
        final NumberConverterTemplate nc = new JapaneseNumberConverterImpl(this.digitUtility);
        final var value = new RefObject <>(0d);
        final var numberType = new RefObject <>(0);
        nc.convertNumber(input, value, numberType);
        org.junit.Assert.assertTrue(1234567.0 == value.argValue);
    }


    @Test
    void convertTest5() {

        final var input = "百二十三万四千五百六十七";
        final NumberConverterTemplate nc = new JapaneseNumberConverterImpl(this.digitUtility);
        final var value = new RefObject <>(0d);
        final var numberType = new RefObject <>(0);
        nc.convertNumber(input, value, numberType);
        org.junit.Assert.assertTrue(1234567.0 == value.argValue);
    }


    @Test
    void convertTest6() {

        final var input = "百2十3万4千5百6十7";
        final NumberConverterTemplate nc = new JapaneseNumberConverterImpl(this.digitUtility);
        final var value = new RefObject <>(0d);
        final var numberType = new RefObject <>(0);
        nc.convertNumber(input, value, numberType);
        org.junit.Assert.assertTrue(1234567.0 == value.argValue);
    }


    @Test
    void processTest1() {

        final var text = "その3,244人が３，４５６，７８９円で百二十三万四千五百六十七円";
        final List <NNumber> result = new ArrayList <>();
        final var language = "ja";
        this.nn = new NumberNormalizerImpl(language);
        this.nn.process(text, result);
        org.junit.Assert.assertEquals(3, result.size());
        org.junit.Assert.assertTrue(3244.0 == result.get(0).valueLowerbound);
        org.junit.Assert.assertTrue(3456789.0 == result.get(1).valueLowerbound);
        org.junit.Assert.assertTrue(1234567.0 == result.get(2).valueLowerbound);

    }
}
