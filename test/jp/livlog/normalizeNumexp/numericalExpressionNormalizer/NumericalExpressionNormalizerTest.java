package jp.livlog.normalizeNumexp.numericalExpressionNormalizer;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jp.livlog.normalizeNumexp.numericalExpressionNormalizer.impl.NumericalExpressionNormalizerImpl;
import jp.livlog.normalizeNumexp.share.Symbol;

class NumericalExpressionNormalizerTest {

    NumericalExpressionNormalizer NEN = null;

    private boolean isSameNumexp(final NumericalExpression n1, final NumericalExpression n2) {

        return n1.originalExpression.equals(n2.originalExpression) && n1.positionStart == n2.positionStart && n1.positionEnd == n2.positionEnd
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
        for (final NumericalExpression numexp : numexps) {
            System.out.println(numexp);
        }
        final var ex = new NumericalExpression("三人", 2, 4, 3, 3);
        ex.counter = "人";
        org.junit.Assert.assertTrue(this.isSameNumexp(ex, numexps.get(0)));
    }


    @Test
    void simple2() {

        final var text = "3kgのレッドブルと、2USドルのモンスター";
        final List <NumericalExpression> numexps = new ArrayList <>();
        final var language = "ja";
        this.NEN = new NumericalExpressionNormalizerImpl(language);
        this.NEN.process(text, numexps);
        for (final NumericalExpression numexp : numexps) {
            System.out.println(numexp);
        }
        final var ex1 = new NumericalExpression("3kg", 0, 3, 3000, 3000);
        ex1.counter = "g";
        final var ex2 = new NumericalExpression("2USドル", 11, 16, 2, 2);
        ex2.counter = "ドル";
        org.junit.Assert.assertTrue(this.isSameNumexp(ex1, numexps.get(0)));
        org.junit.Assert.assertTrue(this.isSameNumexp(ex2, numexps.get(1)));
    }


    @Test
    void about1() {

        final var text = "その約十人がぼぼぼぼ";
        final List <NumericalExpression> numexps = new ArrayList <>();
        final var language = "ja";
        this.NEN = new NumericalExpressionNormalizerImpl(language);
        this.NEN.process(text, numexps);
        for (final NumericalExpression numexp : numexps) {
            System.out.println(numexp);
        }
        final var ex = new NumericalExpression("約十人", 2, 5, 7, 13.0);
        ex.counter = "人";
        org.junit.Assert.assertTrue(this.isSameNumexp(ex, numexps.get(0)));
    }


    @Test
    void about2() {

        final var text = "そのおよそ十人がぼぼぼぼ";
        final List <NumericalExpression> numexps = new ArrayList <>();
        final var language = "ja";
        this.NEN = new NumericalExpressionNormalizerImpl(language);
        this.NEN.process(text, numexps);
        for (final NumericalExpression numexp : numexps) {
            System.out.println(numexp);
        }
        final var ex = new NumericalExpression("およそ十人", 2, 7, 7, 13.0);
        ex.counter = "人";
        org.junit.Assert.assertTrue(this.isSameNumexp(ex, numexps.get(0)));
    }


    @Test
    void orOver() {

        final var text = "その三人以上がぼぼぼぼ";
        final List <NumericalExpression> numexps = new ArrayList <>();
        final var language = "ja";
        this.NEN = new NumericalExpressionNormalizerImpl(language);
        this.NEN.process(text, numexps);
        for (final NumericalExpression numexp : numexps) {
            System.out.println(numexp);
        }
        final var ex = new NumericalExpression("三人以上", 2, 6, 3.0, Symbol.INFINITY);
        ex.counter = "人";
        org.junit.Assert.assertTrue(this.isSameNumexp(ex, numexps.get(0)));
    }


    @Test
    void aboutAndOrOver() {

        final var text = "その約十人以上がぼぼぼぼ";
        final List <NumericalExpression> numexps = new ArrayList <>();
        final var language = "ja";
        this.NEN = new NumericalExpressionNormalizerImpl(language);
        this.NEN.process(text, numexps);
        for (final NumericalExpression numexp : numexps) {
            System.out.println(numexp);
        }
        final var ex = new NumericalExpression("約十人以上", 2, 7, 7.0, Symbol.INFINITY);
        ex.counter = "人";
        org.junit.Assert.assertTrue(this.isSameNumexp(ex, numexps.get(0)));
    }


    @Test
    void orLess() {

        final var text = "その三人以下がぼぼぼぼ";
        final List <NumericalExpression> numexps = new ArrayList <>();
        final var language = "ja";
        this.NEN = new NumericalExpressionNormalizerImpl(language);
        this.NEN.process(text, numexps);
        for (final NumericalExpression numexp : numexps) {
            System.out.println(numexp);
        }
        final var ex = new NumericalExpression("三人以下", 2, 6, -Symbol.INFINITY, 3);
        ex.counter = "人";
        org.junit.Assert.assertTrue(this.isSameNumexp(ex, numexps.get(0)));
    }


    @Test
    void kyou() {

        final var text = "レッドブルを10本強飲んだ";
        final List <NumericalExpression> numexps = new ArrayList <>();
        final var language = "ja";
        this.NEN = new NumericalExpressionNormalizerImpl(language);
        this.NEN.process(text, numexps);
        for (final NumericalExpression numexp : numexps) {
            System.out.println(numexp);
        }
        final var ex = new NumericalExpression("10本強", 6, 10, 10, 16);
        ex.counter = "本";
        org.junit.Assert.assertTrue(this.isSameNumexp(ex, numexps.get(0)));
    }


    @Test
    void jaku() {

        final var text = "レッドブルを10本弱飲んだ";
        final List <NumericalExpression> numexps = new ArrayList <>();
        final var language = "ja";
        this.NEN = new NumericalExpressionNormalizerImpl(language);
        this.NEN.process(text, numexps);
        for (final NumericalExpression numexp : numexps) {
            System.out.println(numexp);
        }
        final var ex = new NumericalExpression("10本弱", 6, 10, 5, 10);
        ex.counter = "本";
        org.junit.Assert.assertTrue(this.isSameNumexp(ex, numexps.get(0)));
    }


    @Test
    void ordinary() {

        final var text = "本日10本目のレッドブル";
        final List <NumericalExpression> numexps = new ArrayList <>();
        final var language = "ja";
        this.NEN = new NumericalExpressionNormalizerImpl(language);
        this.NEN.process(text, numexps);
        for (final NumericalExpression numexp : numexps) {
            System.out.println(numexp);
        }
        final var ex = new NumericalExpression("10本目", 2, 6, 10, 10);
        ex.counter = "本";
        ex.ordinary = true;
        org.junit.Assert.assertTrue(this.isSameNumexp(ex, numexps.get(0)));
    }


    @Test
    void han() {

        final var text = "レッドブルを1本半飲んだ";
        final List <NumericalExpression> numexps = new ArrayList <>();
        final var language = "ja";
        this.NEN = new NumericalExpressionNormalizerImpl(language);
        this.NEN.process(text, numexps);
        for (final NumericalExpression numexp : numexps) {
            System.out.println(numexp);
        }
        final var ex = new NumericalExpression("1本半", 6, 9, 1.5, 1.5);
        ex.counter = "本";
        ex.ordinary = true;
        org.junit.Assert.assertTrue(this.isSameNumexp(ex, numexps.get(0)));
    }


    @Test
    void per() {

        final var text = "１キロメートル／時";
        final List <NumericalExpression> numexps = new ArrayList <>();
        final var language = "ja";
        this.NEN = new NumericalExpressionNormalizerImpl(language);
        this.NEN.process(text, numexps);
        for (final NumericalExpression numexp : numexps) {
            System.out.println(numexp);
        }
        final var ex = new NumericalExpression("１キロメートル／時", 0, 9, 1000, 1000);
        ex.counter = "m/h";
        org.junit.Assert.assertTrue(this.isSameNumexp(ex, numexps.get(0)));
    }


    @Test
    void prefixCounter1() {

        final var text = "それは¥100だ";
        final List <NumericalExpression> numexps = new ArrayList <>();
        final var language = "ja";
        this.NEN = new NumericalExpressionNormalizerImpl(language);
        this.NEN.process(text, numexps);
        for (final NumericalExpression numexp : numexps) {
            System.out.println(numexp);
        }
        final var ex = new NumericalExpression("¥100", 3, 7, 100, 100);
        ex.counter = "円";
        ex.ordinary = true;
        org.junit.Assert.assertTrue(this.isSameNumexp(ex, numexps.get(0)));
    }


    @Test
    void prefixCounter2() {

        final var text = "それは時速40キロメートルだ";
        final List <NumericalExpression> numexps = new ArrayList <>();
        final var language = "ja";
        this.NEN = new NumericalExpressionNormalizerImpl(language);
        this.NEN.process(text, numexps);
        for (final NumericalExpression numexp : numexps) {
            System.out.println(numexp);
        }
        final var ex = new NumericalExpression("時速40キロメートル", 3, 13, 40000, 40000);
        ex.counter = "m/h";
        org.junit.Assert.assertTrue(this.isSameNumexp(ex, numexps.get(0)));
    }


    @Test
    void range1() {

        final var text = "このアトラクションは3人〜の運用になります";
        final List <NumericalExpression> numexps = new ArrayList <>();
        final var language = "ja";
        this.NEN = new NumericalExpressionNormalizerImpl(language);
        this.NEN.process(text, numexps);
        for (final NumericalExpression numexp : numexps) {
            System.out.println(numexp);
        }
        final var ex = new NumericalExpression("3人〜", 10, 13, 3, 3);
        ex.counter = "人";
        org.junit.Assert.assertEquals(1, numexps.size());
        org.junit.Assert.assertTrue(this.isSameNumexp(ex, numexps.get(0)));
        org.junit.Assert.assertEquals(1, numexps.get(0).options.size());
        org.junit.Assert.assertEquals("kara_suffix", numexps.get(0).options.get(0));
    }


    @Test
    void range2() {

        final var text = "遊び方の欄には「〜8人」と書いてある";
        final List <NumericalExpression> numexps = new ArrayList <>();
        final var language = "ja";
        this.NEN = new NumericalExpressionNormalizerImpl(language);
        this.NEN.process(text, numexps);
        for (final NumericalExpression numexp : numexps) {
            System.out.println(numexp);
        }
        final var ex = new NumericalExpression("〜8人", 8, 11, 8, 8);
        ex.counter = "人";
        org.junit.Assert.assertEquals(1, numexps.size());
        org.junit.Assert.assertTrue(this.isSameNumexp(ex, numexps.get(0)));
        org.junit.Assert.assertEquals(1, numexps.get(0).options.size());
        org.junit.Assert.assertEquals("kara_prefix", numexps.get(0).options.get(0));
    }

    // TEST_F(NumexpNormalizerTest, range3) {
    // NumericalExpressionNormalizer NEN("ja");
    // std::string text("遊び方の欄には「5〜8人」と書いてある");
    // std::vector<NumericalExpression> numexps;
    // NEN.process(text, numexps);
    // NumericalExpression ex(string_to_ustring("5〜8人"), 8, 12, 5, 8);
    // ex.counter = string_to_ustring("人");
    // ASSERT_EQ(1u, numexps.size());
    // EXPECT_TRUE(is_same_numexp(ex, numexps[0]));
    // }
    //
    // TEST_F(NumexpNormalizerTest, range4) {
    // NumericalExpressionNormalizer NEN("ja");
    // std::string text("遊び方の欄には「5人〜8人」と書いてある");
    // std::vector<NumericalExpression> numexps;
    // NEN.process(text, numexps);
    // NumericalExpression ex(string_to_ustring("5人〜8人"), 8, 13, 5, 8);
    // ex.counter = string_to_ustring("人");
    // ASSERT_EQ(1u, numexps.size());
    //
    // EXPECT_TRUE(is_same_numexp(ex, numexps[0]));
    // }
    //
    // TEST_F(NumexpNormalizerTest, range5) {
    // NumericalExpressionNormalizer NEN("ja");
    // std::string text("時速50km〜60km");
    // std::vector<NumericalExpression> numexps;
    // NEN.process(text, numexps);
    // NumericalExpression ex(string_to_ustring("時速50km〜60km"), 0, 11, 50000, 60000);
    // ex.counter = string_to_ustring("m/h");
    // ASSERT_EQ(1u, numexps.size());
    //
    // EXPECT_TRUE(is_same_numexp(ex, numexps[0]));
    // }
    //
    // TEST_F(NumexpNormalizerTest, range6) {
    // NumericalExpressionNormalizer NEN("ja");
    // std::string text("時速50kmから時速60km");
    // std::vector<NumericalExpression> numexps;
    // NEN.process(text, numexps);
    // NumericalExpression ex(string_to_ustring("時速50kmから時速60km"), 0, 14, 50000, 60000);
    // ex.counter = string_to_ustring("m/h");
    // ASSERT_EQ(1u, numexps.size());
    //
    // EXPECT_TRUE(is_same_numexp(ex, numexps[0]));
    // }
    //
    // TEST_F(NumexpNormalizerTest, range7) {
    // NumericalExpressionNormalizer NEN("ja");
    // std::string text("時速50〜60km");
    // std::vector<NumericalExpression> numexps;
    // NEN.process(text, numexps);
    // NumericalExpression ex(string_to_ustring("時速50〜60km"), 0, 9, 50000, 60000);
    // ex.counter = string_to_ustring("m/h");
    // ASSERT_EQ(1u, numexps.size());
    //
    // EXPECT_TRUE(is_same_numexp(ex, numexps[0]));
    // }
    //
    // TEST_F(NumexpNormalizerTest, range8) {
    // NumericalExpressionNormalizer NEN("ja");
    // std::string text("世界50カ国から3000人が出席予定だ");
    // std::vector<NumericalExpression> numexps;
    // NEN.process(text, numexps);
    // ASSERT_EQ(2u, numexps.size()); //単位が違うので、マージされない
    // }
    //
    // TEST_F(NumexpNormalizerTest, range9) {
    // NumericalExpressionNormalizer NEN("ja");
    // std::string text("およそ時速50km〜60kmくらい");
    // std::vector<NumericalExpression> numexps;
    // NEN.process(text, numexps);
    // NumericalExpression ex(string_to_ustring("およそ時速50km〜60kmくらい"), 0, 17, 35000, 78000);
    // ex.counter = string_to_ustring("m/h");
    // ASSERT_EQ(1u, numexps.size());
    // EXPECT_TRUE(is_same_numexp(ex, numexps[0]));
    // }
    //
    // TEST_F(NumexpNormalizerTest, chinese1) {
    // NumericalExpressionNormalizer NEN("zh");
    // std::string text("日本政府受清廷壓力，以千二百三元請孫中山離開日本。");
    // std::vector<NumericalExpression> numexps;
    // NEN.process(text, numexps);
    // ASSERT_EQ(1u, numexps.size());
    // NumericalExpression ex(string_to_ustring("千二百三元"), 11, 16, 1230, 1230);
    // ex.counter = string_to_ustring("元");
    // EXPECT_TRUE(is_same_numexp(ex, numexps[0]));
    //
    // }
}
