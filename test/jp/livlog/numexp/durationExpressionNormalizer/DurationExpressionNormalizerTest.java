package jp.livlog.numexp.durationExpressionNormalizer;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jp.livlog.numexp.durationExpressionNormalizer.impl.DurationExpressionNormalizerImpl;
import jp.livlog.numexp.share.NTime;
import jp.livlog.numexp.share.NumexpSymbol;

class DurationExpressionNormalizerTest {

    DurationExpressionNormalizer DEN = null;

    private boolean isSameTime(final NTime a, final NTime b) {

        if (a.year == b.year && a.month == b.month && a.day == b.day && a.hour == b.hour && a.minute == b.minute && a.second == b.second) {
            return true;
        }

        return false;
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
        final var text = "あの人は三時間も耐えた";
        final List <DurationExpression> durationexps = new ArrayList <>();
        final var language = "ja";
        this.DEN = new DurationExpressionNormalizerImpl(language);
        this.DEN.process(text, durationexps);

        for (final DurationExpression durationexp : durationexps) {
            System.out.println(methodName + ":" + durationexp.valueLowerbound.toDurationString(false));
        }
        org.junit.Assert.assertEquals(1, durationexps.size());
        final var ex1Lower = new NTime(NumexpSymbol.INFINITY, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY, 3, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY);
        final var ex1Upper = new NTime(-NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, 3, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY);
        org.junit.Assert.assertEquals("三時間", durationexps.get(0).originalExpression);
        org.junit.Assert.assertTrue(this.isSameTime(ex1Lower, durationexps.get(0).valueLowerbound));
        org.junit.Assert.assertTrue(this.isSameTime(ex1Upper, durationexps.get(0).valueUpperbound));
    }


    @Test
    void simple2() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "それは3年5ヶ月の間にも";
        final List <DurationExpression> durationexps = new ArrayList <>();
        final var language = "ja";
        this.DEN = new DurationExpressionNormalizerImpl(language);
        this.DEN.process(text, durationexps);
        for (final DurationExpression durationexp : durationexps) {
            System.out.println(methodName + ":" + durationexp.valueLowerbound.toDurationString(false));
        }
        org.junit.Assert.assertEquals(1, durationexps.size());
        final var ex1Lower = new NTime(3, 5, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY);
        final var ex1Upper = new NTime(3, 5, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY);
        org.junit.Assert.assertEquals("3年5ヶ月", durationexps.get(0).originalExpression);
        org.junit.Assert.assertTrue(this.isSameTime(ex1Lower, durationexps.get(0).valueLowerbound));
        org.junit.Assert.assertTrue(this.isSameTime(ex1Upper, durationexps.get(0).valueUpperbound));
    }


    @Test
    void seiki1() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "あの人は三世紀も耐えた";
        final List <DurationExpression> durationexps = new ArrayList <>();
        final var language = "ja";
        this.DEN = new DurationExpressionNormalizerImpl(language);
        this.DEN.process(text, durationexps);
        for (final DurationExpression durationexp : durationexps) {
            System.out.println(methodName + ":" + durationexp.valueLowerbound.toDurationString(false));
        }
        org.junit.Assert.assertEquals(1, durationexps.size());
        final var ex1Lower = new NTime(300, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY);
        final var ex1Upper = new NTime(300, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY);
        org.junit.Assert.assertEquals("三世紀", durationexps.get(0).originalExpression);
        org.junit.Assert.assertTrue(this.isSameTime(ex1Lower, durationexps.get(0).valueLowerbound));
        org.junit.Assert.assertTrue(this.isSameTime(ex1Upper, durationexps.get(0).valueUpperbound));
    }


    @Test
    void han1() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "あの人は三世紀半も耐えた";
        final List <DurationExpression> durationexps = new ArrayList <>();
        final var language = "ja";
        this.DEN = new DurationExpressionNormalizerImpl(language);
        this.DEN.process(text, durationexps);
        for (final DurationExpression durationexp : durationexps) {
            System.out.println(methodName + ":" + durationexp.valueLowerbound.toDurationString(false));
        }
        org.junit.Assert.assertEquals(1, durationexps.size());
        final var ex1Lower = new NTime(350, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY);
        final var ex1Upper = new NTime(350, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY);
        org.junit.Assert.assertEquals("三世紀半", durationexps.get(0).originalExpression);
        org.junit.Assert.assertTrue(this.isSameTime(ex1Lower, durationexps.get(0).valueLowerbound));
        org.junit.Assert.assertTrue(this.isSameTime(ex1Upper, durationexps.get(0).valueUpperbound));
    }


    @Test
    void han2() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "あの人は三時間半も耐えた";
        final List <DurationExpression> durationexps = new ArrayList <>();
        final var language = "ja";
        this.DEN = new DurationExpressionNormalizerImpl(language);
        this.DEN.process(text, durationexps);
        for (final DurationExpression durationexp : durationexps) {
            System.out.println(methodName + ":" + durationexp.valueLowerbound.toDurationString(false));
        }
        org.junit.Assert.assertEquals(1, durationexps.size());
        final var ex1Lower = new NTime(NumexpSymbol.INFINITY, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY, 3.5, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY);
        final var ex1Upper = new NTime(-NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, 3.5, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY);
        org.junit.Assert.assertEquals("三時間半", durationexps.get(0).originalExpression);
        org.junit.Assert.assertTrue(this.isSameTime(ex1Lower, durationexps.get(0).valueLowerbound));
        org.junit.Assert.assertTrue(this.isSameTime(ex1Upper, durationexps.get(0).valueUpperbound));
    }


    @Test
    void plural1() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "三年間と五ヶ月の間";
        final List <DurationExpression> durationexps = new ArrayList <>();
        final var language = "ja";
        this.DEN = new DurationExpressionNormalizerImpl(language);
        this.DEN.process(text, durationexps);
        for (final DurationExpression durationexp : durationexps) {
            System.out.println(methodName + ":" + durationexp.valueLowerbound.toDurationString(false));
        }
        org.junit.Assert.assertEquals(2, durationexps.size());
        final var ex1Lower = new NTime(3, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY);
        final var ex1Upper = new NTime(3, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY);
        org.junit.Assert.assertEquals("三年間", durationexps.get(0).originalExpression);
        org.junit.Assert.assertTrue(this.isSameTime(ex1Lower, durationexps.get(0).valueLowerbound));
        org.junit.Assert.assertTrue(this.isSameTime(ex1Upper, durationexps.get(0).valueUpperbound));
        final var ex2Lower = new NTime(NumexpSymbol.INFINITY, 5, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY);
        final var ex2Upper = new NTime(-NumexpSymbol.INFINITY, 5, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY);
        org.junit.Assert.assertEquals("五ヶ月", durationexps.get(1).originalExpression);
        org.junit.Assert.assertTrue(this.isSameTime(ex2Lower, durationexps.get(1).valueLowerbound));
        org.junit.Assert.assertTrue(this.isSameTime(ex2Upper, durationexps.get(1).valueUpperbound));
    }


    @Test
    void orOver1() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "あの人は三時間以上も耐えた";
        final List <DurationExpression> durationexps = new ArrayList <>();
        final var language = "ja";
        this.DEN = new DurationExpressionNormalizerImpl(language);
        this.DEN.process(text, durationexps);
        for (final DurationExpression durationexp : durationexps) {
            System.out.println(methodName + ":" + durationexp.valueLowerbound.toDurationString(false));
        }
        org.junit.Assert.assertEquals(1, durationexps.size());
        final var ex1Lower = new NTime(NumexpSymbol.INFINITY, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY, 3, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY);
        final var ex1Upper = new NTime(-NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY);
        org.junit.Assert.assertEquals("三時間以上", durationexps.get(0).originalExpression);
        org.junit.Assert.assertTrue(this.isSameTime(ex1Lower, durationexps.get(0).valueLowerbound));
        org.junit.Assert.assertTrue(this.isSameTime(ex1Upper, durationexps.get(0).valueUpperbound));
    }

    @Test
    void aboutSuffix() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "あの人は三時間くらいは耐えた";
        final List <DurationExpression> durationexps = new ArrayList <>();
        final var language = "ja";
        this.DEN = new DurationExpressionNormalizerImpl(language);
        this.DEN.process(text, durationexps);
        for (final DurationExpression durationexp : durationexps) {
            System.out.println(methodName + ":" + durationexp.valueLowerbound.toDurationString(false));
        }
        org.junit.Assert.assertEquals(1, durationexps.size());
        final var ex1Lower = new NTime(NumexpSymbol.INFINITY, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY, 2, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY);
        final var ex1Upper = new NTime(-NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, 4, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY);
        org.junit.Assert.assertEquals("三時間くらい", durationexps.get(0).originalExpression);
        org.junit.Assert.assertTrue(this.isSameTime(ex1Lower, durationexps.get(0).valueLowerbound));
        org.junit.Assert.assertTrue(this.isSameTime(ex1Upper, durationexps.get(0).valueUpperbound));
    }

    @Test
    void aboutPrefix() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "あの人はほぼ三時間は耐えた";
        final List <DurationExpression> durationexps = new ArrayList <>();
        final var language = "ja";
        this.DEN = new DurationExpressionNormalizerImpl(language);
        this.DEN.process(text, durationexps);
        for (final DurationExpression durationexp : durationexps) {
            System.out.println(methodName + ":" + durationexp.valueLowerbound.toDurationString(false));
        }
        org.junit.Assert.assertEquals(1, durationexps.size());
        final var ex1Lower = new NTime(NumexpSymbol.INFINITY, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY, 2, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY);
        final var ex1Upper = new NTime(-NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, 4, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY);
        org.junit.Assert.assertEquals("ほぼ三時間", durationexps.get(0).originalExpression);
        org.junit.Assert.assertTrue(this.isSameTime(ex1Lower, durationexps.get(0).valueLowerbound));
        org.junit.Assert.assertTrue(this.isSameTime(ex1Upper, durationexps.get(0).valueUpperbound));
    }


    @Test
    void kyou() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "あの人は三時間強は耐えた";
        final List <DurationExpression> durationexps = new ArrayList <>();
        final var language = "ja";
        this.DEN = new DurationExpressionNormalizerImpl(language);
        this.DEN.process(text, durationexps);

        for (final DurationExpression durationexp : durationexps) {
            System.out.println(methodName + ":" + durationexp.valueLowerbound.toDurationString(false));
        }
        org.junit.Assert.assertEquals(1, durationexps.size());
        final var ex1Lower = new NTime(NumexpSymbol.INFINITY, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY, 3, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY);
        final var ex1Upper = new NTime(-NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, 4, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY);
        org.junit.Assert.assertEquals("三時間強", durationexps.get(0).originalExpression);
        org.junit.Assert.assertTrue(this.isSameTime(ex1Lower, durationexps.get(0).valueLowerbound));
        org.junit.Assert.assertTrue(this.isSameTime(ex1Upper, durationexps.get(0).valueUpperbound));
    }


    @Test
    void jaku() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "あの人は三時間弱は耐えた";
        final List <DurationExpression> durationexps = new ArrayList <>();
        final var language = "ja";
        this.DEN = new DurationExpressionNormalizerImpl(language);
        this.DEN.process(text, durationexps);

        for (final DurationExpression durationexp : durationexps) {
            System.out.println(methodName + ":" + durationexp.valueLowerbound.toDurationString(false));
        }
        org.junit.Assert.assertEquals(1, durationexps.size());
        final var ex1Lower = new NTime(NumexpSymbol.INFINITY, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY, 2, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY);
        final var ex1Upper = new NTime(-NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, 3, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY);
        org.junit.Assert.assertEquals("三時間弱", durationexps.get(0).originalExpression);
        org.junit.Assert.assertTrue(this.isSameTime(ex1Lower, durationexps.get(0).valueLowerbound));
        org.junit.Assert.assertTrue(this.isSameTime(ex1Upper, durationexps.get(0).valueUpperbound));
    }

}
