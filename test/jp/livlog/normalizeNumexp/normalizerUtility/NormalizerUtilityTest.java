package jp.livlog.normalizeNumexp.normalizerUtility;

import java.util.ArrayList;
import java.util.NavigableSet;
import java.util.TreeSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jp.livlog.normalizeNumexp.normalizerUtility.impl.NormalizerUtilityImpl;
import jp.livlog.normalizeNumexp.share.NNumber;
import jp.livlog.normalizeNumexp.share.NTime;
import jp.livlog.normalizeNumexp.share.Pair;
import jp.livlog.normalizeNumexp.share.PairKey0Comp;
import jp.livlog.normalizeNumexp.share.RefObject;
import jp.livlog.normalizeNumexp.share.Symbol;

class NormalizerUtilityTest {

    NormalizerUtility                     normalizerUtility = new NormalizerUtilityImpl();

    NavigableSet <Pair <String, Integer>> uxm               = new TreeSet <>(new PairKey0Comp<String, Integer>());

    NavigableSet <Pair <String, Integer>> uxmRev            = new TreeSet <>(new PairKey0Comp<String, Integer>());

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

        for (final Pair <String, Integer> pair : this.uxm) {
            final var strRev = this.normalizerUtility.reverseString(pair.first);
            this.uxmRev.add(new Pair <>(strRev, pair.second));
        }
    }


    @Test
    void testExtractAfterString() {

        final var text = new StringBuilder("それは秒速5センチメートルくらいで進む");
        final var str = new StringBuilder();
        this.normalizerUtility.extractAfterString(text, 6, str);
        org.junit.Assert.assertEquals("センチメートルくらいで進む", str.toString());
    }


    @Test
    void testExtractBeforeString() {

        final var text = new StringBuilder("それは秒速5センチメートルくらいで進む");
        final var str = new StringBuilder();
        this.normalizerUtility.extractBeforeString(text, 5, str);
        org.junit.Assert.assertEquals("それは秒速", str.toString());
    }


    @Test
    void testPrefixSearch() {

        var ustr = new StringBuilder("あいうえお");
        var matchingPatternId = new RefObject <>(0);
        this.normalizerUtility.prefixSearch(ustr, this.uxm, matchingPatternId);
        org.junit.Assert.assertEquals(3, matchingPatternId.argValue.intValue()); // ("あいう", 3)

        ustr = new StringBuilder("いうえおあいうえお");
        matchingPatternId = new RefObject <>(0);
        this.normalizerUtility.prefixSearch(ustr, this.uxm, matchingPatternId);
        org.junit.Assert.assertEquals(8, matchingPatternId.argValue.intValue()); // ("いうえおあ", 8)
    }


    @Test
    void testSuffixSearch() {

        var ustr = new StringBuilder("あいうえお");
        var matchingPatternId = new RefObject <>(0);
        this.normalizerUtility.suffixSearch(ustr, this.uxmRev, matchingPatternId);
        org.junit.Assert.assertEquals(6, matchingPatternId.argValue.intValue()); // ("うえお", 6)

        ustr = new StringBuilder("あいうえおあ");
        matchingPatternId = new RefObject <>(0);
        this.normalizerUtility.suffixSearch(ustr, this.uxmRev, matchingPatternId);
        org.junit.Assert.assertEquals(8, matchingPatternId.argValue.intValue()); // ("いうえおあ", 8)
    }


    @Test
    void testSearchSuffixNumberModifier() {

        final var text = new StringBuilder("あいうえおあ5あいうえおごごごごご");
        final var matchingPatternId = new RefObject <>(0);
        this.normalizerUtility.searchSuffixNumberModifier(text, 7, this.uxm, matchingPatternId);
        org.junit.Assert.assertEquals(3, matchingPatternId.argValue.intValue());
    }


    @Test
    void testSearchPrefixNumberModifier() {

        final var text = new StringBuilder("あいうえおあ5あいうえおごごごごご");
        final var matchingPatternId = new RefObject <>(0);
        this.normalizerUtility.searchPrefixNumberModifier(text, 6, this.uxmRev, matchingPatternId);
        org.junit.Assert.assertEquals(8, matchingPatternId.argValue.intValue());
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
