package jp.livlog.normalizeNumexp.abstimeExpressionNormalizer.impl;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jp.livlog.normalizeNumexp.abstimeExpressionNormalizer.AbstimeExpressionNormalizer;
import jp.livlog.normalizeNumexp.share.NTime;
import jp.livlog.normalizeNumexp.share.Symbol;

class AbstimeExpressionNormalizerImplTest {

    AbstimeExpressionNormalizer AEN = null;

    private boolean isSameTime(final NTime a, final NTime b) {

        return a.year == b.year && a.month == b.month && a.day == b.day && a.hour == b.hour && a.minute == b.minute && a.second == b.second;
    }


    @BeforeEach
    public void initialize() {

        // this.digitUtility = new DigitUtilityImpl();
        // this.digitUtility.initKansuji("ja");
        // this.ne = new NumberExtractorImpl(this.digitUtility);
    }


    @Test
    void simple1() {

        final var text = "あの人は1989年7月21日午前3時に生まれた";
        final List <AbstimeExpressionImpl> abstimeexps = new ArrayList <>();
        final var language = "ja";
        this.AEN = new AbstimeExpressionNormalizerImpl(language);
        this.AEN.process(text, abstimeexps);
        System.out.println(abstimeexps.get(0).valueLowerbound.toDurationString(true));
        System.out.println(abstimeexps.get(1).valueLowerbound.toDurationString(true));
        org.junit.Assert.assertEquals(2, abstimeexps.size());
        final var ex1Lower = new NTime(1989,7,21, Symbol.INFINITY, Symbol.INFINITY, Symbol.INFINITY);
        final var ex1Upper = new NTime(1989,7,21, -Symbol.INFINITY, -Symbol.INFINITY, -Symbol.INFINITY);
//        org.junit.Assert.assertEquals("1989年7月21日",abstimeexps.get(0).originalExpression);
        org.junit.Assert.assertTrue(this.isSameTime(ex1Lower, abstimeexps.get(0).valueLowerbound));
        org.junit.Assert.assertTrue(this.isSameTime(ex1Upper, abstimeexps.get(0).valueUpperbound));
        final var ex2Lower = new NTime(Symbol.INFINITY,Symbol.INFINITY,Symbol.INFINITY, 3, Symbol.INFINITY, Symbol.INFINITY);
        final var ex2Upper = new NTime(-Symbol.INFINITY,-Symbol.INFINITY,-Symbol.INFINITY, 3, -Symbol.INFINITY, -Symbol.INFINITY);
//        org.junit.Assert.assertEquals("午前3時",abstimeexps.get(1).originalExpression);
        org.junit.Assert.assertTrue(this.isSameTime(ex2Lower, abstimeexps.get(1).valueLowerbound));
        org.junit.Assert.assertTrue(this.isSameTime(ex2Upper, abstimeexps.get(1).valueUpperbound));
    }


    @Test
    void simple2() {

        final var text = "1989-7-21　1989.7.21　1989/7/21 １９８９．７．２１";
        final List <AbstimeExpressionImpl> abstimeexps = new ArrayList <>();
        final var language = "ja";
        this.AEN = new AbstimeExpressionNormalizerImpl(language);
        this.AEN.process(text, abstimeexps);

        org.junit.Assert.assertEquals(4, abstimeexps.size());
        final var ex1Lower = new NTime(1989,7,21, Symbol.INFINITY, Symbol.INFINITY, Symbol.INFINITY);
        final var ex1Upper = new NTime(1989,7,21, -Symbol.INFINITY, -Symbol.INFINITY, -Symbol.INFINITY);
        System.out.println(abstimeexps.get(0).valueLowerbound.toDurationString(true));
        System.out.println(abstimeexps.get(1).valueLowerbound.toDurationString(true));
        System.out.println(abstimeexps.get(2).valueLowerbound.toDurationString(true));
        System.out.println(abstimeexps.get(3).valueLowerbound.toDurationString(true));
        org.junit.Assert.assertEquals("1989-7-21",abstimeexps.get(0).originalExpression);
        org.junit.Assert.assertTrue(this.isSameTime(ex1Lower, abstimeexps.get(0).valueLowerbound));
        org.junit.Assert.assertTrue(this.isSameTime(ex1Upper, abstimeexps.get(0).valueUpperbound));

        org.junit.Assert.assertEquals("1989.7.21",abstimeexps.get(1).originalExpression);
        org.junit.Assert.assertTrue(this.isSameTime(ex1Lower, abstimeexps.get(1).valueLowerbound));
        org.junit.Assert.assertTrue(this.isSameTime(ex1Upper, abstimeexps.get(1).valueUpperbound));

        org.junit.Assert.assertEquals("1989/7/21",abstimeexps.get(2).originalExpression);
        org.junit.Assert.assertTrue(this.isSameTime(ex1Lower, abstimeexps.get(2).valueLowerbound));
        org.junit.Assert.assertTrue(this.isSameTime(ex1Upper, abstimeexps.get(2).valueUpperbound));

        org.junit.Assert.assertEquals("１９８９．７．２１",abstimeexps.get(3).originalExpression);
        org.junit.Assert.assertTrue(this.isSameTime(ex1Lower, abstimeexps.get(3).valueLowerbound));
        org.junit.Assert.assertTrue(this.isSameTime(ex1Upper, abstimeexps.get(3).valueUpperbound));
    }
}
