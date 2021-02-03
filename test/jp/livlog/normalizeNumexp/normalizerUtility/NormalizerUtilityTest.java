package jp.livlog.normalizeNumexp.normalizerUtility;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jp.livlog.normalizeNumexp.normalizerUtility.impl.NormalizerUtilityImpl;
import jp.livlog.normalizeNumexp.share.NNumber;
import jp.livlog.normalizeNumexp.share.NTime;
import jp.livlog.normalizeNumexp.share.Pair;
import jp.livlog.normalizeNumexp.share.Symbol;

class NormalizerUtilityTest {

    NormalizerUtility                   normalizerUtility = new NormalizerUtilityImpl();

    final List <Pair <String, Integer>> uxm               = new ArrayList <>();

    final List <Pair <String, Integer>> uxmRev            = new ArrayList <>();

    @BeforeEach
    public void initialize() {

        this.uxm.add(new Pair <>("あ", 1));
        this.uxm.add(new Pair <>("あい", 2));
        this.uxm.add(new Pair <>("あいう", 3));
        this.uxm.add(new Pair <>("いう", 4));
        this.uxm.add(new Pair <>("うえ", 5));
        this.uxm.add(new Pair <>("うえお", 6));
        this.uxm.add(new Pair <>("えお", 7));
        this.uxm.add(new Pair <>("いうえおあ", 8));

        for (var i = 0; i < this.uxm.size(); i++) {
            final var strRev = this.normalizerUtility.reverseString(this.uxm.get(i).first);
            this.uxmRev.add(new Pair <>(strRev, this.uxm.get(i).second));
        }
    }


    @Test
    void testExtractAfterString() {

        final var text = new StringBuilder("それは秒速5センチメートルくらいで進む");
        final var str = new StringBuilder();
        this.normalizerUtility.extractAfterString(text, 6, str);
        org.junit.Assert.assertEquals("センチメートルくらいで進む", str);
    }


    @Test
    void testExtractBeforeString() {

        final var text = new StringBuilder("それは秒速5センチメートルくらいで進む");
        final var str = new StringBuilder();
        this.normalizerUtility.extractBeforeString(text, 5, str);
        org.junit.Assert.assertEquals("それは秒速", str);
    }


    @Test
    void testPrefixSearch() {

        var ustr = new StringBuilder("あいうえお");
        final var matchingPatternId = 0;
        this.normalizerUtility.prefixSearch(ustr, this.uxm, matchingPatternId);
        org.junit.Assert.assertEquals(3, matchingPatternId); // ("あいう", 3)

        ustr = new StringBuilder("いうえおあいうえお");
        this.normalizerUtility.prefixSearch(ustr, this.uxm, matchingPatternId);
        org.junit.Assert.assertEquals(8, matchingPatternId); // ("いうえおあ", 8)
    }


    @Test
    void testSuffixSearch() {

        var ustr = new StringBuilder("あいうえお");
        final var matchingPatternId = 0;
        this.normalizerUtility.suffixSearch(ustr, this.uxmRev, matchingPatternId);
        org.junit.Assert.assertEquals(6, matchingPatternId); // ("うえお", 6)

        ustr = new StringBuilder("あいうえおあ");
        this.normalizerUtility.suffixSearch(ustr, this.uxmRev, matchingPatternId);
        org.junit.Assert.assertEquals(8, matchingPatternId); // ("いうえおあ", 8)
    }


    @Test
    void testSearchSuffixNumberModifier() {

        final var text = new StringBuilder("あいうえおあ5あいうえおごごごごご");
        final var matchingPatternId = 0;
        this.normalizerUtility.searchSuffixNumberModifier(text, 7, this.uxm, matchingPatternId);
        org.junit.Assert.assertEquals(3, matchingPatternId);
    }


    @Test
    void testSearchPrefixNumberModifier() {

        final var text = new StringBuilder("あいうえおあ5あいうえおごごごごご");
        final var matchingPatternId = 0;
        this.normalizerUtility.searchPrefixNumberModifier(text, 6, this.uxmRev, matchingPatternId);
        org.junit.Assert.assertEquals(8, matchingPatternId);
    }


    @Test
    void testReplaceNumbersInText() {

        final var text = new StringBuilder("その30人がそれは三十五人でボボボ");
        final var textReplaced = new StringBuilder();
        final var numbers = new ArrayList <NNumber>();
        final var exp1 = "30人";
        final var exp2 = "三十五人";
        numbers.add(new NNumber(exp1, 2, 4));
        numbers.add(new NNumber(exp2, 9, 12));
        this.normalizerUtility.replaceNumbersInText(text, numbers, textReplaced);
        org.junit.Assert.assertEquals("そのǂǂ人がそれはǂǂǂ人でボボボ", textReplaced.toString());
    }


    @Test
    void testShortenPlaceHolderInText() {

        final var text = new StringBuilder("そのǂǂ人がそれはǂǂǂǂǂǂ人でボボボǂǂǂ");
        final var textShortened = new StringBuilder();
        this.normalizerUtility.shortenPlaceHolderInText(text, textShortened);
        org.junit.Assert.assertEquals("そのǂ人がそれはǂ人でボボボǂ", textShortened.toString());
    }


    @Test
    void testIsPlaceHolder() {

        org.junit.Assert.assertTrue(this.normalizerUtility.isPlaceHolder('ǂ'));
        org.junit.Assert.assertFalse(this.normalizerUtility.isPlaceHolder('あ'));
    }


    @Test
    void testIsFinite() {

        org.junit.Assert.assertTrue(this.normalizerUtility.isFinite(99999.0));
        org.junit.Assert.assertFalse(this.normalizerUtility.isFinite(Symbol.INFINITY));
    }


    @Test
    void testIsNullTime() {

        var t = new NTime(Symbol.INFINITY);
        org.junit.Assert.assertTrue(this.normalizerUtility.isNullTime(t));
        t = new NTime(1);
        org.junit.Assert.assertFalse(this.normalizerUtility.isNullTime(t));
    }


    @Test
    void testIdentifyTimeDetail() {

        final var t = new NTime(1, 1, 1, 1, 1, Symbol.INFINITY);
        org.junit.Assert.assertEquals(this.normalizerUtility.identifyTimeDetail(t), "mn");
    }


    @Test
    void testReverseString() {

        final var str = "aiueo";
        org.junit.Assert.assertEquals(this.normalizerUtility.reverseString(str), "oeuia");
    }

    // @Test
    // void testCast() {
    //
    // fail("まだ実装されていません");
    // }

}
