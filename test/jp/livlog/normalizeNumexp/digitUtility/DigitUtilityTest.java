package jp.livlog.normalizeNumexp.digitUtility;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import jp.livlog.normalizeNumexp.digitUtility.impl.DigitUtilityImpl;


class DigitUtilityTest {

    @Test
    void testInitKansuji() {

        final DigitUtility digitUtility = new DigitUtilityImpl();
        digitUtility.initKansuji("ja");
    }


    @Test
    void testIsHankakusuji() {

        Assertions.fail("まだ実装されていません");
    }


    @Test
    void testIsZenkakusuji() {

        Assertions.fail("まだ実装されていません");
    }


    @Test
    void testIsArabic() {

        Assertions.fail("まだ実装されていません");
    }


    @Test
    void testIsKansuji09() {

        Assertions.fail("まだ実装されていません");
    }


    @Test
    void testIsKansujiKuraiSen() {

        Assertions.fail("まだ実装されていません");
    }


    @Test
    void testIsKansujiKuraiMan() {

        Assertions.fail("まだ実装されていません");
    }


    @Test
    void testIsKansujiKurai() {

        Assertions.fail("まだ実装されていません");
    }


    @Test
    void testIsNumber() {

        Assertions.fail("まだ実装されていません");
    }


    @Test
    void testIsComma() {

        Assertions.fail("まだ実装されていません");
    }


    @Test
    void testIsDecimalPoint() {

        Assertions.fail("まだ実装されていません");
    }


    @Test
    void testIsRangeExpression() {

        Assertions.fail("まだ実装されていません");
    }


    @Test
    void testConvertKansuji09ToValue() {

        Assertions.fail("まだ実装されていません");
    }


    @Test
    void testConvertKansujiKuraiToPowerValue() {

        Assertions.fail("まだ実装されていません");
    }


    @Test
    void testGetNumberStringCharacter() {

        Assertions.fail("まだ実装されていません");
    }

}
