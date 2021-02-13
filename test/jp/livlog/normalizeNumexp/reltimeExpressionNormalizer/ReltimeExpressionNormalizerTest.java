package jp.livlog.normalizeNumexp.reltimeExpressionNormalizer;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import jp.livlog.normalizeNumexp.reltimeExpressionNormalizer.impl.ReltimeExpressionNormalizerImpl;
import jp.livlog.normalizeNumexp.share.NTime;
import jp.livlog.normalizeNumexp.share.Symbol;

class ReltimeExpressionNormalizerTest {

    ReltimeExpressionNormalizer REN = null;

    private boolean isSameTime(final NTime a, final NTime b) {

        if (a.year == b.year && a.month == b.month && a.day == b.day && a.hour == b.hour && a.minute == b.minute && a.second == b.second) {
            return true;
        }

        return false;
    }


    @Test
    void simple1() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "あの人は三時間前に生まれた";
        final List <ReltimeExpression> reltimeexps = new ArrayList <>();
        final var language = "ja";
        this.REN = new ReltimeExpressionNormalizerImpl(language);
        this.REN.process(text, reltimeexps);
        System.out.println(methodName + ":" + reltimeexps.get(0).valueLowerboundRel.toDurationString(false));
        org.junit.Assert.assertEquals(1, reltimeexps.size());
        final var ex1LowerAbs = new NTime(Symbol.INFINITY, Symbol.INFINITY, Symbol.INFINITY, Symbol.INFINITY, Symbol.INFINITY, Symbol.INFINITY);
        final var ex1UpperAbs = new NTime(-Symbol.INFINITY, -Symbol.INFINITY, -Symbol.INFINITY, -Symbol.INFINITY, -Symbol.INFINITY, -Symbol.INFINITY);
        final var ex1LowerRel = new NTime(Symbol.INFINITY, Symbol.INFINITY, Symbol.INFINITY, -3, Symbol.INFINITY, Symbol.INFINITY);
        final var ex1UpperRel = new NTime(-Symbol.INFINITY, -Symbol.INFINITY, -Symbol.INFINITY, -3, -Symbol.INFINITY, -Symbol.INFINITY);
        org.junit.Assert.assertEquals("三時間前", reltimeexps.get(0).originalExpression);
        org.junit.Assert.assertTrue(this.isSameTime(ex1LowerAbs, reltimeexps.get(0).valueLowerboundAbs));
        org.junit.Assert.assertTrue(this.isSameTime(ex1UpperAbs, reltimeexps.get(0).valueUpperboundAbs));
        org.junit.Assert.assertTrue(this.isSameTime(ex1LowerRel, reltimeexps.get(0).valueLowerboundRel));
        org.junit.Assert.assertTrue(this.isSameTime(ex1UpperRel, reltimeexps.get(0).valueUpperboundRel));
    }


    @Test
    void simple2() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "それは3年5ヶ月後の出来事";
        final List <ReltimeExpression> reltimeexps = new ArrayList <>();
        final var language = "ja";
        this.REN = new ReltimeExpressionNormalizerImpl(language);
        this.REN.process(text, reltimeexps);
        System.out.println(methodName + ":" + reltimeexps.get(0).valueLowerboundRel.toDurationString(false));
        org.junit.Assert.assertEquals(1, reltimeexps.size());
        final var ex1LowerAbs = new NTime(Symbol.INFINITY, Symbol.INFINITY, Symbol.INFINITY, Symbol.INFINITY, Symbol.INFINITY, Symbol.INFINITY);
        final var ex1UpperAbs = new NTime(-Symbol.INFINITY, -Symbol.INFINITY, -Symbol.INFINITY, -Symbol.INFINITY, -Symbol.INFINITY, -Symbol.INFINITY);
        final var ex1LowerRel = new NTime(3, 5, Symbol.INFINITY,  Symbol.INFINITY, Symbol.INFINITY, Symbol.INFINITY);
        final var ex1UpperRel = new NTime(3, 5, -Symbol.INFINITY, -Symbol.INFINITY, -Symbol.INFINITY, -Symbol.INFINITY);
        org.junit.Assert.assertEquals("3年5ヶ月後", reltimeexps.get(0).originalExpression);
        org.junit.Assert.assertTrue(this.isSameTime(ex1LowerAbs, reltimeexps.get(0).valueLowerboundAbs));
        org.junit.Assert.assertTrue(this.isSameTime(ex1UpperAbs, reltimeexps.get(0).valueUpperboundAbs));
        org.junit.Assert.assertTrue(this.isSameTime(ex1LowerRel, reltimeexps.get(0).valueLowerboundRel));
        org.junit.Assert.assertTrue(this.isSameTime(ex1UpperRel, reltimeexps.get(0).valueUpperboundRel));
    }



    @Test
    void plural1() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "話をしよう。あれは今から36万年前………いや、1万4000年前だったか。";
        final List <ReltimeExpression> reltimeexps = new ArrayList <>();
        final var language = "ja";
        this.REN = new ReltimeExpressionNormalizerImpl(language);
        this.REN.process(text, reltimeexps);

        org.junit.Assert.assertEquals(2, reltimeexps.size());
        final var ex1LowerAbs = new NTime(Symbol.INFINITY, Symbol.INFINITY, Symbol.INFINITY, Symbol.INFINITY, Symbol.INFINITY, Symbol.INFINITY);
        final var ex1UpperAbs = new NTime(-Symbol.INFINITY, -Symbol.INFINITY, -Symbol.INFINITY, -Symbol.INFINITY, -Symbol.INFINITY, -Symbol.INFINITY);
        final var ex1LowerRel = new NTime(-360000, Symbol.INFINITY, Symbol.INFINITY,  Symbol.INFINITY, Symbol.INFINITY, Symbol.INFINITY);
        final var ex1UpperRel = new NTime(-360000, -Symbol.INFINITY, -Symbol.INFINITY, -Symbol.INFINITY, -Symbol.INFINITY, -Symbol.INFINITY);
        final var ex2LowerAbs = new NTime(Symbol.INFINITY, Symbol.INFINITY, Symbol.INFINITY, Symbol.INFINITY, Symbol.INFINITY, Symbol.INFINITY);
        final var ex2UpperAbs = new NTime(-Symbol.INFINITY, -Symbol.INFINITY, -Symbol.INFINITY, -Symbol.INFINITY, -Symbol.INFINITY, -Symbol.INFINITY);
        final var ex2LowerRel = new NTime(-14000, Symbol.INFINITY, Symbol.INFINITY,  Symbol.INFINITY, Symbol.INFINITY, Symbol.INFINITY);
        final var ex2UpperRel = new NTime(-14000, -Symbol.INFINITY, -Symbol.INFINITY, -Symbol.INFINITY, -Symbol.INFINITY, -Symbol.INFINITY);
        System.out.println(methodName + ":" + reltimeexps.get(0).valueLowerboundRel.toDurationString(false));
        org.junit.Assert.assertEquals("から36万年前", reltimeexps.get(0).originalExpression);
        org.junit.Assert.assertTrue(this.isSameTime(ex1LowerAbs, reltimeexps.get(0).valueLowerboundAbs));
        org.junit.Assert.assertTrue(this.isSameTime(ex1UpperAbs, reltimeexps.get(0).valueUpperboundAbs));
        org.junit.Assert.assertTrue(this.isSameTime(ex1LowerRel, reltimeexps.get(0).valueLowerboundRel));
        org.junit.Assert.assertTrue(this.isSameTime(ex1UpperRel, reltimeexps.get(0).valueUpperboundRel));
        System.out.println(methodName + ":" + reltimeexps.get(1).valueLowerboundRel.toDurationString(false));
        org.junit.Assert.assertEquals("1万4000年前", reltimeexps.get(1).originalExpression);
        org.junit.Assert.assertTrue(this.isSameTime(ex2LowerAbs, reltimeexps.get(1).valueLowerboundAbs));
        org.junit.Assert.assertTrue(this.isSameTime(ex2UpperAbs, reltimeexps.get(1).valueUpperboundAbs));
        org.junit.Assert.assertTrue(this.isSameTime(ex2LowerRel, reltimeexps.get(1).valueLowerboundRel));
        org.junit.Assert.assertTrue(this.isSameTime(ex2UpperRel, reltimeexps.get(1).valueUpperboundRel));
    }
}
