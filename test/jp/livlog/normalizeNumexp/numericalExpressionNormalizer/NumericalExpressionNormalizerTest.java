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

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "その三人が死んだ";
        final List <NumericalExpression> numexps = new ArrayList <>();
        final var language = "ja";
        this.NEN = new NumericalExpressionNormalizerImpl(language);
        this.NEN.process(text, numexps);
        for (final NumericalExpression numexp : numexps) {
            System.out.println(methodName + ":" + numexp);
        }
        final var ex = new NumericalExpression("三人", 2, 4, 3, 3);
        ex.counter = "人";
        org.junit.Assert.assertTrue(this.isSameNumexp(ex, numexps.get(0)));
    }


    @Test
    void simple2() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "3kgのレッドブルと、2USドルのモンスター";
        final List <NumericalExpression> numexps = new ArrayList <>();
        final var language = "ja";
        this.NEN = new NumericalExpressionNormalizerImpl(language);
        this.NEN.process(text, numexps);
        for (final NumericalExpression numexp : numexps) {
            System.out.println(methodName + ":" + numexp);
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

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "その約十人がぼぼぼぼ";
        final List <NumericalExpression> numexps = new ArrayList <>();
        final var language = "ja";
        this.NEN = new NumericalExpressionNormalizerImpl(language);
        this.NEN.process(text, numexps);
        for (final NumericalExpression numexp : numexps) {
            System.out.println(methodName + ":" + numexp);
        }
        final var ex = new NumericalExpression("約十人", 2, 5, 7, 13.0);
        ex.counter = "人";
        org.junit.Assert.assertTrue(this.isSameNumexp(ex, numexps.get(0)));
    }


    @Test
    void about2() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "そのおよそ十人がぼぼぼぼ";
        final List <NumericalExpression> numexps = new ArrayList <>();
        final var language = "ja";
        this.NEN = new NumericalExpressionNormalizerImpl(language);
        this.NEN.process(text, numexps);
        for (final NumericalExpression numexp : numexps) {
            System.out.println(methodName + ":" + numexp);
        }
        final var ex = new NumericalExpression("およそ十人", 2, 7, 7, 13.0);
        ex.counter = "人";
        org.junit.Assert.assertTrue(this.isSameNumexp(ex, numexps.get(0)));
    }


    @Test
    void orOver() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "その三人以上がぼぼぼぼ";
        final List <NumericalExpression> numexps = new ArrayList <>();
        final var language = "ja";
        this.NEN = new NumericalExpressionNormalizerImpl(language);
        this.NEN.process(text, numexps);
        for (final NumericalExpression numexp : numexps) {
            System.out.println(methodName + ":" + numexp);
        }
        final var ex = new NumericalExpression("三人以上", 2, 6, 3.0, Symbol.INFINITY);
        ex.counter = "人";
        org.junit.Assert.assertTrue(this.isSameNumexp(ex, numexps.get(0)));
    }


    @Test
    void aboutAndOrOver() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "その約十人以上がぼぼぼぼ";
        final List <NumericalExpression> numexps = new ArrayList <>();
        final var language = "ja";
        this.NEN = new NumericalExpressionNormalizerImpl(language);
        this.NEN.process(text, numexps);
        for (final NumericalExpression numexp : numexps) {
            System.out.println(methodName + ":" + numexp);
        }
        final var ex = new NumericalExpression("約十人以上", 2, 7, 7.0, Symbol.INFINITY);
        ex.counter = "人";
        org.junit.Assert.assertTrue(this.isSameNumexp(ex, numexps.get(0)));
    }


    @Test
    void orLess() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "その三人以下がぼぼぼぼ";
        final List <NumericalExpression> numexps = new ArrayList <>();
        final var language = "ja";
        this.NEN = new NumericalExpressionNormalizerImpl(language);
        this.NEN.process(text, numexps);
        for (final NumericalExpression numexp : numexps) {
            System.out.println(methodName + ":" + numexp);
        }
        final var ex = new NumericalExpression("三人以下", 2, 6, -Symbol.INFINITY, 3);
        ex.counter = "人";
        org.junit.Assert.assertTrue(this.isSameNumexp(ex, numexps.get(0)));
    }


    @Test
    void kyou() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "レッドブルを10本強飲んだ";
        final List <NumericalExpression> numexps = new ArrayList <>();
        final var language = "ja";
        this.NEN = new NumericalExpressionNormalizerImpl(language);
        this.NEN.process(text, numexps);
        for (final NumericalExpression numexp : numexps) {
            System.out.println(methodName + ":" + numexp);
        }
        final var ex = new NumericalExpression("10本強", 6, 10, 10, 16);
        ex.counter = "本";
        org.junit.Assert.assertTrue(this.isSameNumexp(ex, numexps.get(0)));
    }


    @Test
    void jaku() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "レッドブルを10本弱飲んだ";
        final List <NumericalExpression> numexps = new ArrayList <>();
        final var language = "ja";
        this.NEN = new NumericalExpressionNormalizerImpl(language);
        this.NEN.process(text, numexps);
        for (final NumericalExpression numexp : numexps) {
            System.out.println(methodName + ":" + numexp);
        }
        final var ex = new NumericalExpression("10本弱", 6, 10, 5, 10);
        ex.counter = "本";
        org.junit.Assert.assertTrue(this.isSameNumexp(ex, numexps.get(0)));
    }


    @Test
    void ordinary() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "本日10本目のレッドブル";
        final List <NumericalExpression> numexps = new ArrayList <>();
        final var language = "ja";
        this.NEN = new NumericalExpressionNormalizerImpl(language);
        this.NEN.process(text, numexps);
        for (final NumericalExpression numexp : numexps) {
            System.out.println(methodName + ":" + numexp);
        }
        final var ex = new NumericalExpression("10本目", 2, 6, 10, 10);
        ex.counter = "本";
        ex.ordinary = true;
        org.junit.Assert.assertTrue(this.isSameNumexp(ex, numexps.get(0)));
    }


    @Test
    void han() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "レッドブルを1本半飲んだ";
        final List <NumericalExpression> numexps = new ArrayList <>();
        final var language = "ja";
        this.NEN = new NumericalExpressionNormalizerImpl(language);
        this.NEN.process(text, numexps);
        for (final NumericalExpression numexp : numexps) {
            System.out.println(methodName + ":" + numexp);
        }
        final var ex = new NumericalExpression("1本半", 6, 9, 1.5, 1.5);
        ex.counter = "本";
        ex.ordinary = true;
        org.junit.Assert.assertTrue(this.isSameNumexp(ex, numexps.get(0)));
    }


    @Test
    void per() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "１キロメートル／時";
        final List <NumericalExpression> numexps = new ArrayList <>();
        final var language = "ja";
        this.NEN = new NumericalExpressionNormalizerImpl(language);
        this.NEN.process(text, numexps);
        for (final NumericalExpression numexp : numexps) {
            System.out.println(methodName + ":" + numexp);
        }
        final var ex = new NumericalExpression("１キロメートル／時", 0, 9, 1000, 1000);
        ex.counter = "m/h";
        org.junit.Assert.assertTrue(this.isSameNumexp(ex, numexps.get(0)));
    }


    @Test
    void prefixCounter1() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "それは¥100だ";
        final List <NumericalExpression> numexps = new ArrayList <>();
        final var language = "ja";
        this.NEN = new NumericalExpressionNormalizerImpl(language);
        this.NEN.process(text, numexps);
        for (final NumericalExpression numexp : numexps) {
            System.out.println(methodName + ":" + numexp);
        }
        final var ex = new NumericalExpression("¥100", 3, 7, 100, 100);
        ex.counter = "円";
        ex.ordinary = true;
        org.junit.Assert.assertTrue(this.isSameNumexp(ex, numexps.get(0)));
    }


    @Test
    void prefixCounter2() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "それは時速40キロメートルだ";
        final List <NumericalExpression> numexps = new ArrayList <>();
        final var language = "ja";
        this.NEN = new NumericalExpressionNormalizerImpl(language);
        this.NEN.process(text, numexps);
        for (final NumericalExpression numexp : numexps) {
            System.out.println(methodName + ":" + numexp);
        }
        final var ex = new NumericalExpression("時速40キロメートル", 3, 13, 40000, 40000);
        ex.counter = "m/h";
        org.junit.Assert.assertTrue(this.isSameNumexp(ex, numexps.get(0)));
    }


    @Test
    void range1() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "このアトラクションは3人〜の運用になります";
        final List <NumericalExpression> numexps = new ArrayList <>();
        final var language = "ja";
        this.NEN = new NumericalExpressionNormalizerImpl(language);
        this.NEN.process(text, numexps);
        for (final NumericalExpression numexp : numexps) {
            System.out.println(methodName + ":" + numexp);
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

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "遊び方の欄には「〜8人」と書いてある";
        final List <NumericalExpression> numexps = new ArrayList <>();
        final var language = "ja";
        this.NEN = new NumericalExpressionNormalizerImpl(language);
        this.NEN.process(text, numexps);
        for (final NumericalExpression numexp : numexps) {
            System.out.println(methodName + ":" + numexp);
        }
        final var ex = new NumericalExpression("〜8人", 8, 11, 8, 8);
        ex.counter = "人";
        org.junit.Assert.assertEquals(1, numexps.size());
        org.junit.Assert.assertTrue(this.isSameNumexp(ex, numexps.get(0)));
        org.junit.Assert.assertEquals(1, numexps.get(0).options.size());
        org.junit.Assert.assertEquals("kara_prefix", numexps.get(0).options.get(0));
    }


    @Test
    void range3() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "遊び方の欄には「5〜8人」と書いてある";
        final List <NumericalExpression> numexps = new ArrayList <>();
        final var language = "ja";
        this.NEN = new NumericalExpressionNormalizerImpl(language);
        this.NEN.process(text, numexps);
        for (final NumericalExpression numexp : numexps) {
            System.out.println(methodName + ":" + numexp);
        }
        final var ex = new NumericalExpression("5〜8人", 8, 12, 5, 8);
        ex.counter = "人";
        org.junit.Assert.assertEquals(1, numexps.size());
        org.junit.Assert.assertTrue(this.isSameNumexp(ex, numexps.get(0)));
    }


    @Test
    void range4() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "遊び方の欄には「5人〜8人」と書いてある";
        final List <NumericalExpression> numexps = new ArrayList <>();
        final var language = "ja";
        this.NEN = new NumericalExpressionNormalizerImpl(language);
        this.NEN.process(text, numexps);
        for (final NumericalExpression numexp : numexps) {
            System.out.println(methodName + ":" + numexp);
        }
        final var ex = new NumericalExpression("5人〜8人", 8, 13, 5, 8);
        ex.counter = "人";
        org.junit.Assert.assertEquals(1, numexps.size());
        org.junit.Assert.assertTrue(this.isSameNumexp(ex, numexps.get(0)));
    }


    @Test
    void range5() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "時速50km〜60km";
        final List <NumericalExpression> numexps = new ArrayList <>();
        final var language = "ja";
        this.NEN = new NumericalExpressionNormalizerImpl(language);
        this.NEN.process(text, numexps);
        for (final NumericalExpression numexp : numexps) {
            System.out.println(methodName + ":" + numexp);
        }
        final var ex = new NumericalExpression("時速50km〜60km", 0, 11, 50000, 60000);
        ex.counter = "m/h";
        org.junit.Assert.assertEquals(1, numexps.size());
        org.junit.Assert.assertTrue(this.isSameNumexp(ex, numexps.get(0)));
    }


    @Test
    void range6() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "時速50kmから時速60km";
        final List <NumericalExpression> numexps = new ArrayList <>();
        final var language = "ja";
        this.NEN = new NumericalExpressionNormalizerImpl(language);
        this.NEN.process(text, numexps);
        for (final NumericalExpression numexp : numexps) {
            System.out.println(methodName + ":" + numexp);
        }
        final var ex = new NumericalExpression("時速50kmから時速60km", 0, 14, 50000, 60000);
        ex.counter = "m/h";
        org.junit.Assert.assertEquals(1, numexps.size());
        org.junit.Assert.assertTrue(this.isSameNumexp(ex, numexps.get(0)));
    }


    @Test
    void range7() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "時速50〜60km";
        final List <NumericalExpression> numexps = new ArrayList <>();
        final var language = "ja";
        this.NEN = new NumericalExpressionNormalizerImpl(language);
        this.NEN.process(text, numexps);
        for (final NumericalExpression numexp : numexps) {
            System.out.println(methodName + ":" + numexp);
        }
        final var ex = new NumericalExpression("時速50〜60km", 0, 9, 50000, 60000);
        ex.counter = "m/h";
        org.junit.Assert.assertEquals(1, numexps.size());
        org.junit.Assert.assertTrue(this.isSameNumexp(ex, numexps.get(0)));
    }


    @Test
    void range8() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "世界50カ国から3000人が出席予定だ";
        final List <NumericalExpression> numexps = new ArrayList <>();
        final var language = "ja";
        this.NEN = new NumericalExpressionNormalizerImpl(language);
        this.NEN.process(text, numexps);
        for (final NumericalExpression numexp : numexps) {
            System.out.println(methodName + ":" + numexp);
        }
        org.junit.Assert.assertEquals(2, numexps.size());
    }


    @Test
    void range9() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "およそ時速50km〜60kmくらい";
        final List <NumericalExpression> numexps = new ArrayList <>();
        final var language = "ja";
        this.NEN = new NumericalExpressionNormalizerImpl(language);
        this.NEN.process(text, numexps);
        for (final NumericalExpression numexp : numexps) {
            System.out.println(methodName + ":" + numexp);
        }
        final var ex = new NumericalExpression("およそ時速50km〜60kmくらい", 0, 17, 35000, 78000);
        ex.counter = "m/h";
        org.junit.Assert.assertEquals(1, numexps.size());
        org.junit.Assert.assertTrue(this.isSameNumexp(ex, numexps.get(0)));
    }


    @Test
    void chinese1() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "日本政府受清廷壓力，以千二百三元請孫中山離開日本。";
        final List <NumericalExpression> numexps = new ArrayList <>();
        final var language = "zh";
        this.NEN = new NumericalExpressionNormalizerImpl(language);
        this.NEN.process(text, numexps);
        for (final NumericalExpression numexp : numexps) {
            System.out.println(methodName + ":" + numexp);
        }
        final var ex = new NumericalExpression("千二百三元", 11, 16, 1230, 1230);
        ex.counter = "元";
        org.junit.Assert.assertEquals(1, numexps.size());
        org.junit.Assert.assertTrue(this.isSameNumexp(ex, numexps.get(0)));
    }



    @Test
    void test() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "中国から30匹の鳥がきた";
        final List <NumericalExpression> numexps = new ArrayList <>();
        final var language = "ja";
        this.NEN = new NumericalExpressionNormalizerImpl(language);
        this.NEN.process(text, numexps);
        for (final NumericalExpression numexp : numexps) {
            System.out.println(methodName + ":" + numexp);
        }
        final var ex = new NumericalExpression("から30匹", 2, 7, 30, 30);
        ex.counter = "匹";
        org.junit.Assert.assertEquals(1, numexps.size());
        org.junit.Assert.assertTrue(this.isSameNumexp(ex, numexps.get(0)));
    }

    @Test
    void test2() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "数十人が十数人と喧嘩して、百数十円落とした";
        final List <NumericalExpression> numexps = new ArrayList <>();
        final var language = "ja";
        this.NEN = new NumericalExpressionNormalizerImpl(language);
        this.NEN.process(text, numexps);
        for (final NumericalExpression numexp : numexps) {
            System.out.println(methodName + ":" + numexp);
        }
//        final var ex = new NumericalExpression("から30匹", 2, 7, 30, 30);
//        ex.counter = "匹";
//        org.junit.Assert.assertEquals(1, numexps.size());
//        org.junit.Assert.assertTrue(this.isSameNumexp(ex, numexps.get(0)));
    }
}
