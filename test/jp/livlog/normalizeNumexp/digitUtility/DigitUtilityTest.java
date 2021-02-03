package jp.livlog.normalizeNumexp.digitUtility;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jp.livlog.normalizeNumexp.digitUtility.impl.DigitUtilityImpl;

class DigitUtilityTest {

    final DigitUtility digitUtility = new DigitUtilityImpl();

    @BeforeEach
    public void initialize() {

        this.digitUtility.initKansuji("ja");
    }


//    @Test
//    void testInitKansuji() {
//        this.digitUtility.initKansuji("ja");
//    }


    @Test
    void testIsHankakusuji() {

        org.junit.Assert.assertFalse(this.digitUtility.isZenkakusuji('1'));
        org.junit.Assert.assertTrue(this.digitUtility.isZenkakusuji('１'));
        org.junit.Assert.assertFalse(this.digitUtility.isZenkakusuji('一'));
        org.junit.Assert.assertFalse(this.digitUtility.isZenkakusuji('あ'));
    }


    @Test
    void testIsZenkakusuji() {

    }


    @Test
    void testIsArabic() {

        org.junit.Assert.assertTrue(this.digitUtility.isArabic('1'));
        org.junit.Assert.assertTrue(this.digitUtility.isArabic('１'));
        org.junit.Assert.assertFalse(this.digitUtility.isArabic('一'));
        org.junit.Assert.assertFalse(this.digitUtility.isArabic('あ'));
    }


    @Test
    void testIsKansuji() {

        org.junit.Assert.assertFalse(this.digitUtility.isKansuji('1'));
        org.junit.Assert.assertFalse(this.digitUtility.isKansuji('１'));
        org.junit.Assert.assertTrue(this.digitUtility.isKansuji('一'));
        org.junit.Assert.assertFalse(this.digitUtility.isKansuji('あ'));
    }


    @Test
    void testIsKansuji09() {

        org.junit.Assert.assertFalse(this.digitUtility.isKansuji09('1'));
        org.junit.Assert.assertFalse(this.digitUtility.isKansuji09('１'));
        org.junit.Assert.assertTrue(this.digitUtility.isKansuji09('一'));
        org.junit.Assert.assertFalse(this.digitUtility.isKansuji09('十'));
        org.junit.Assert.assertFalse(this.digitUtility.isKansuji09('万'));
        org.junit.Assert.assertFalse(this.digitUtility.isKansuji09('あ'));
    }


    @Test
    void testIsKansujiKuraiSen() {

        org.junit.Assert.assertFalse(this.digitUtility.isKansujiKuraiSen('1'));
        org.junit.Assert.assertFalse(this.digitUtility.isKansujiKuraiSen('１'));
        org.junit.Assert.assertFalse(this.digitUtility.isKansujiKuraiSen('一'));
        org.junit.Assert.assertTrue(this.digitUtility.isKansujiKuraiSen('十'));
        org.junit.Assert.assertTrue(this.digitUtility.isKansujiKuraiSen('百'));
        org.junit.Assert.assertTrue(this.digitUtility.isKansujiKuraiSen('千'));
        org.junit.Assert.assertFalse(this.digitUtility.isKansujiKuraiSen('万'));
        org.junit.Assert.assertFalse(this.digitUtility.isKansujiKuraiSen('あ'));
    }


    @Test
    void testIsKansujiKuraiMan() {

        org.junit.Assert.assertFalse(this.digitUtility.isKansujiKuraiMan('1'));
        org.junit.Assert.assertFalse(this.digitUtility.isKansujiKuraiMan('１'));
        org.junit.Assert.assertFalse(this.digitUtility.isKansujiKuraiMan('一'));
        org.junit.Assert.assertFalse(this.digitUtility.isKansujiKuraiMan('十'));
        org.junit.Assert.assertTrue(this.digitUtility.isKansujiKuraiMan('万'));
        org.junit.Assert.assertTrue(this.digitUtility.isKansujiKuraiMan('億'));
        org.junit.Assert.assertTrue(this.digitUtility.isKansujiKuraiMan('兆'));
        org.junit.Assert.assertFalse(this.digitUtility.isKansujiKuraiMan('あ'));
    }


    @Test
    void testIsKansujiKurai() {

        org.junit.Assert.assertFalse(this.digitUtility.isKansujiKurai('1'));
        org.junit.Assert.assertFalse(this.digitUtility.isKansujiKurai('１'));
        org.junit.Assert.assertFalse(this.digitUtility.isKansujiKurai('一'));
        org.junit.Assert.assertTrue(this.digitUtility.isKansujiKurai('十'));
        org.junit.Assert.assertTrue(this.digitUtility.isKansujiKurai('万'));
        org.junit.Assert.assertFalse(this.digitUtility.isKansujiKurai('あ'));
    }


    @Test
    void testIsNumber() {

        org.junit.Assert.assertTrue(this.digitUtility.isNumber('1'));
        org.junit.Assert.assertTrue(this.digitUtility.isNumber('１'));
        org.junit.Assert.assertTrue(this.digitUtility.isNumber('一'));
        org.junit.Assert.assertTrue(this.digitUtility.isNumber('十'));
        org.junit.Assert.assertTrue(this.digitUtility.isNumber('万'));
        org.junit.Assert.assertFalse(this.digitUtility.isNumber('あ'));
    }


//    @Test
//    void testIsComma() {
//
//        // Assertions.fail('まだ実装されていません');
//    }
//
//
//    @Test
//    void testIsDecimalPoint() {
//
//        // Assertions.fail('まだ実装されていません');
//    }
//
//
//    @Test
//    void testIsRangeExpression() {
//
//        // Assertions.fail('まだ実装されていません');
//    }
//
//
//    @Test
//    void testConvertKansuji09ToValue() {
//
//        // Assertions.fail('まだ実装されていません');
//    }
//
//
//    @Test
//    void testConvertKansujiKuraiToPowerValue() {
//
//        // Assertions.fail('まだ実装されていません');
//    }
//
//
//    @Test
//    void testGetNumberStringCharacter() {
//
//        // Assertions.fail('まだ実装されていません');
//    }

}
