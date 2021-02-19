package jp.livlog.numexp.abstimeExpressionNormalizer;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jp.livlog.numexp.abstimeExpressionNormalizer.impl.AbstimeExpressionNormalizerImpl;
import jp.livlog.numexp.share.NTime;
import jp.livlog.numexp.share.NumexpSymbol;

class AbstimeExpressionNormalizerTest {

    AbstimeExpressionNormalizer AEN = null;

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
    void simple0() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "午後3時";
        final List <AbstimeExpression> abstimeexps = new ArrayList <>();
        final var language = "ja";
        this.AEN = new AbstimeExpressionNormalizerImpl(language);
        this.AEN.process(text, abstimeexps);
        System.out.println(methodName + ":" + abstimeexps.get(0).valueLowerbound.toDurationString(false));
        org.junit.Assert.assertEquals(1, abstimeexps.size());
        final var ex2Lower = new NTime(NumexpSymbol.INFINITY, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY, 15, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY);
        final var ex2Upper = new NTime(-NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, 15, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY);
        org.junit.Assert.assertEquals("午後3時", abstimeexps.get(0).originalExpression);
        org.junit.Assert.assertTrue(this.isSameTime(ex2Lower, abstimeexps.get(0).valueLowerbound));
        org.junit.Assert.assertTrue(this.isSameTime(ex2Upper, abstimeexps.get(0).valueUpperbound));
    }


    @Test
    void simple1() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "あの人は1989年7月21日午前3時に生まれた";
        final List <AbstimeExpression> abstimeexps = new ArrayList <>();
        final var language = "ja";
        this.AEN = new AbstimeExpressionNormalizerImpl(language);
        this.AEN.process(text, abstimeexps);
        System.out.println(methodName + ":" + abstimeexps.get(0).valueLowerbound.toDurationString(false));
        System.out.println(methodName + ":" + abstimeexps.get(1).valueLowerbound.toDurationString(false));
        org.junit.Assert.assertEquals(2, abstimeexps.size());
        final var ex1Lower = new NTime(1989, 7, 21, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY);
        final var ex1Upper = new NTime(1989, 7, 21, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY);
        org.junit.Assert.assertEquals("1989年7月21日", abstimeexps.get(0).originalExpression);
        org.junit.Assert.assertTrue(this.isSameTime(ex1Lower, abstimeexps.get(0).valueLowerbound));
        org.junit.Assert.assertTrue(this.isSameTime(ex1Upper, abstimeexps.get(0).valueUpperbound));
        final var ex2Lower = new NTime(NumexpSymbol.INFINITY, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY, 3, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY);
        final var ex2Upper = new NTime(-NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, 3, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY);
        org.junit.Assert.assertEquals("午前3時", abstimeexps.get(1).originalExpression);
        org.junit.Assert.assertTrue(this.isSameTime(ex2Lower, abstimeexps.get(1).valueLowerbound));
        org.junit.Assert.assertTrue(this.isSameTime(ex2Upper, abstimeexps.get(1).valueUpperbound));
    }


    @Test
    void simple2() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "1989-7-21　1989.7.21　1989/7/21 １９８９．７．２１";
        final List <AbstimeExpression> abstimeexps = new ArrayList <>();
        final var language = "ja";
        this.AEN = new AbstimeExpressionNormalizerImpl(language);
        this.AEN.process(text, abstimeexps);

        org.junit.Assert.assertEquals(4, abstimeexps.size());
        final var ex1Lower = new NTime(1989, 7, 21, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY);
        final var ex1Upper = new NTime(1989, 7, 21, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY);
        System.out.println(methodName + ":" + abstimeexps.get(0).valueLowerbound.toDurationString(false));
        System.out.println(methodName + ":" + abstimeexps.get(1).valueLowerbound.toDurationString(false));
        System.out.println(methodName + ":" + abstimeexps.get(2).valueLowerbound.toDurationString(false));
        System.out.println(methodName + ":" + abstimeexps.get(3).valueLowerbound.toDurationString(false));
        org.junit.Assert.assertEquals("1989-7-21", abstimeexps.get(0).originalExpression);
        org.junit.Assert.assertTrue(this.isSameTime(ex1Lower, abstimeexps.get(0).valueLowerbound));
        org.junit.Assert.assertTrue(this.isSameTime(ex1Upper, abstimeexps.get(0).valueUpperbound));

        org.junit.Assert.assertEquals("1989.7.21", abstimeexps.get(1).originalExpression);
        org.junit.Assert.assertTrue(this.isSameTime(ex1Lower, abstimeexps.get(1).valueLowerbound));
        org.junit.Assert.assertTrue(this.isSameTime(ex1Upper, abstimeexps.get(1).valueUpperbound));

        org.junit.Assert.assertEquals("1989/7/21", abstimeexps.get(2).originalExpression);
        org.junit.Assert.assertTrue(this.isSameTime(ex1Lower, abstimeexps.get(2).valueLowerbound));
        org.junit.Assert.assertTrue(this.isSameTime(ex1Upper, abstimeexps.get(2).valueUpperbound));

        org.junit.Assert.assertEquals("１９８９．７．２１", abstimeexps.get(3).originalExpression);
        org.junit.Assert.assertTrue(this.isSameTime(ex1Lower, abstimeexps.get(3).valueLowerbound));
        org.junit.Assert.assertTrue(this.isSameTime(ex1Upper, abstimeexps.get(3).valueUpperbound));
    }


    @Test
    void gogo() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "あの人は1989年7月21日午後3時に生まれた。";
        final List <AbstimeExpression> abstimeexps = new ArrayList <>();
        final var language = "ja";
        this.AEN = new AbstimeExpressionNormalizerImpl(language);
        this.AEN.process(text, abstimeexps);
        System.out.println(methodName + ":" + abstimeexps.get(0).valueLowerbound.toDurationString(false));
        System.out.println(methodName + ":" + abstimeexps.get(1).valueLowerbound.toDurationString(false));
        org.junit.Assert.assertEquals(2, abstimeexps.size());
        final var ex1Lower = new NTime(1989, 7, 21, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY);
        final var ex1Upper = new NTime(1989, 7, 21, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY);
        org.junit.Assert.assertEquals("1989年7月21日", abstimeexps.get(0).originalExpression);
        org.junit.Assert.assertTrue(this.isSameTime(ex1Lower, abstimeexps.get(0).valueLowerbound));
        org.junit.Assert.assertTrue(this.isSameTime(ex1Upper, abstimeexps.get(0).valueUpperbound));
        final var ex2Lower = new NTime(NumexpSymbol.INFINITY, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY, 15, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY);
        final var ex2Upper = new NTime(-NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, 15, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY);
        org.junit.Assert.assertEquals("午後3時", abstimeexps.get(1).originalExpression);
        org.junit.Assert.assertTrue(this.isSameTime(ex2Lower, abstimeexps.get(1).valueLowerbound));
        org.junit.Assert.assertTrue(this.isSameTime(ex2Upper, abstimeexps.get(1).valueUpperbound));
    }


    @Test
    void gogoHan() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "あの人は午後3時半に生まれた。";
        final List <AbstimeExpression> abstimeexps = new ArrayList <>();
        final var language = "ja";
        this.AEN = new AbstimeExpressionNormalizerImpl(language);
        this.AEN.process(text, abstimeexps);
        System.out.println(methodName + ":" + abstimeexps.get(0).valueLowerbound.toDurationString(false));
        org.junit.Assert.assertEquals(1, abstimeexps.size());
        final var ex2Lower = new NTime(NumexpSymbol.INFINITY, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY, 15, 30, NumexpSymbol.INFINITY);
        final var ex2Upper = new NTime(-NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, 15, 30, -NumexpSymbol.INFINITY);
        org.junit.Assert.assertEquals("午後3時半", abstimeexps.get(0).originalExpression);
        org.junit.Assert.assertTrue(this.isSameTime(ex2Lower, abstimeexps.get(0).valueLowerbound));
        org.junit.Assert.assertTrue(this.isSameTime(ex2Upper, abstimeexps.get(0).valueUpperbound));
    }


    @Test
    void seiki() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "あの人は18世紀に生まれた。";
        final List <AbstimeExpression> abstimeexps = new ArrayList <>();
        final var language = "ja";
        this.AEN = new AbstimeExpressionNormalizerImpl(language);
        this.AEN.process(text, abstimeexps);
        System.out.println(methodName + ":" + abstimeexps.get(0).valueLowerbound.toDurationString(false));
        org.junit.Assert.assertEquals(1, abstimeexps.size());
        final var ex2Lower = new NTime(1701, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY);
        final var ex2Upper = new NTime(1800, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY);
        org.junit.Assert.assertEquals("18世紀", abstimeexps.get(0).originalExpression);
        org.junit.Assert.assertTrue(this.isSameTime(ex2Lower, abstimeexps.get(0).valueLowerbound));
        org.junit.Assert.assertTrue(this.isSameTime(ex2Upper, abstimeexps.get(0).valueUpperbound));
    }


    @Test
    void about1() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "あの人は1989年7月21日ごろに生まれた";
        final List <AbstimeExpression> abstimeexps = new ArrayList <>();
        final var language = "ja";
        this.AEN = new AbstimeExpressionNormalizerImpl(language);
        this.AEN.process(text, abstimeexps);
        System.out.println(methodName + ":" + abstimeexps.get(0).valueLowerbound.toDurationString(false));
        final var ex1Lower = new NTime(1989, 7, 20, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY);
        final var ex1Upper = new NTime(1989, 7, 22, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY);
        org.junit.Assert.assertEquals("1989年7月21日ごろ", abstimeexps.get(0).originalExpression);
        org.junit.Assert.assertTrue(this.isSameTime(ex1Lower, abstimeexps.get(0).valueLowerbound));
        org.junit.Assert.assertTrue(this.isSameTime(ex1Upper, abstimeexps.get(0).valueUpperbound));
    }


    @Test
    void prefixCounter1() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "平成1年7月21日、私は生まれた";
        final List <AbstimeExpression> abstimeexps = new ArrayList <>();
        final var language = "ja";
        this.AEN = new AbstimeExpressionNormalizerImpl(language);
        this.AEN.process(text, abstimeexps);
        System.out.println(methodName + ":" + abstimeexps.get(0).valueLowerbound.toDurationString(false));
        final var ex1Lower = new NTime(1989, 7, 21, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY);
        final var ex1Upper = new NTime(1989, 7, 21, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY);
        org.junit.Assert.assertTrue(this.isSameTime(ex1Lower, abstimeexps.get(0).valueLowerbound));
        org.junit.Assert.assertTrue(this.isSameTime(ex1Upper, abstimeexps.get(0).valueUpperbound));
    }


    @Test
    void zenhan1() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "あの人は18世紀前半に生まれた。";
        final List <AbstimeExpression> abstimeexps = new ArrayList <>();
        final var language = "ja";
        this.AEN = new AbstimeExpressionNormalizerImpl(language);
        this.AEN.process(text, abstimeexps);
        System.out.println(methodName + ":" + abstimeexps.get(0).valueLowerbound.toDurationString(false));
        org.junit.Assert.assertEquals(1, abstimeexps.size());
        final var ex2Lower = new NTime(1701, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY);
        final var ex2Upper = new NTime(1750, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY);
        org.junit.Assert.assertEquals("18世紀前半", abstimeexps.get(0).originalExpression);
        org.junit.Assert.assertTrue(this.isSameTime(ex2Lower, abstimeexps.get(0).valueLowerbound));
        org.junit.Assert.assertTrue(this.isSameTime(ex2Upper, abstimeexps.get(0).valueUpperbound));
    }


    @Test
    void kouhan1() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "あの人は18世紀後半に生まれた。";
        final List <AbstimeExpression> abstimeexps = new ArrayList <>();
        final var language = "ja";
        this.AEN = new AbstimeExpressionNormalizerImpl(language);
        this.AEN.process(text, abstimeexps);
        System.out.println(methodName + ":" + abstimeexps.get(0).valueLowerbound.toDurationString(false));
        org.junit.Assert.assertEquals(1, abstimeexps.size());
        final var ex2Lower = new NTime(1751, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY);
        final var ex2Upper = new NTime(1800, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY);
        org.junit.Assert.assertEquals("18世紀後半", abstimeexps.get(0).originalExpression);
        org.junit.Assert.assertTrue(this.isSameTime(ex2Lower, abstimeexps.get(0).valueLowerbound));
        org.junit.Assert.assertTrue(this.isSameTime(ex2Upper, abstimeexps.get(0).valueUpperbound));
    }


    @Test
    void nakaba1() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "あの人は18世紀半ばに生まれた。";
        final List <AbstimeExpression> abstimeexps = new ArrayList <>();
        final var language = "ja";
        this.AEN = new AbstimeExpressionNormalizerImpl(language);
        this.AEN.process(text, abstimeexps);
        System.out.println(methodName + ":" + abstimeexps.get(0).valueLowerbound.toDurationString(false));
        org.junit.Assert.assertEquals(1, abstimeexps.size());
        final var ex2Lower = new NTime(1725, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY);
        final var ex2Upper = new NTime(1776, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY);
        org.junit.Assert.assertEquals("18世紀半ば", abstimeexps.get(0).originalExpression);
        org.junit.Assert.assertTrue(this.isSameTime(ex2Lower, abstimeexps.get(0).valueLowerbound));
        org.junit.Assert.assertTrue(this.isSameTime(ex2Upper, abstimeexps.get(0).valueUpperbound));
    }


    @Test
    void zenhan2() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "あの人は7月3日朝に生まれた。";
        final List <AbstimeExpression> abstimeexps = new ArrayList <>();
        final var language = "ja";
        this.AEN = new AbstimeExpressionNormalizerImpl(language);
        this.AEN.process(text, abstimeexps);
        System.out.println(methodName + ":" + abstimeexps.get(0).valueLowerbound.toDurationString(false));
        final var ex1Lower = new NTime(NumexpSymbol.INFINITY, 7, 3, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY);
        final var ex1Upper = new NTime(-NumexpSymbol.INFINITY, 7, 3, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY);
        org.junit.Assert.assertEquals("7月3日", abstimeexps.get(0).originalExpression);
        org.junit.Assert.assertTrue(this.isSameTime(ex1Lower, abstimeexps.get(0).valueLowerbound));
        org.junit.Assert.assertTrue(this.isSameTime(ex1Upper, abstimeexps.get(0).valueUpperbound));
    }


    @Test
    void joujun() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "あの人は７月上旬に生まれた。";
        final List <AbstimeExpression> abstimeexps = new ArrayList <>();
        final var language = "ja";
        this.AEN = new AbstimeExpressionNormalizerImpl(language);
        this.AEN.process(text, abstimeexps);
        System.out.println(methodName + ":" + abstimeexps.get(0).valueLowerbound.toDurationString(false));
        final var ex1Lower = new NTime(NumexpSymbol.INFINITY, 7, 1, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY);
        final var ex1Upper = new NTime(-NumexpSymbol.INFINITY, 7, 10, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY);
        org.junit.Assert.assertEquals("７月上旬", abstimeexps.get(0).originalExpression);
        org.junit.Assert.assertTrue(this.isSameTime(ex1Lower, abstimeexps.get(0).valueLowerbound));
        org.junit.Assert.assertTrue(this.isSameTime(ex1Upper, abstimeexps.get(0).valueUpperbound));
    }


    @Test
    void orLess() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "3月11日以前に、";
        final List <AbstimeExpression> abstimeexps = new ArrayList <>();
        final var language = "ja";
        this.AEN = new AbstimeExpressionNormalizerImpl(language);
        this.AEN.process(text, abstimeexps);
        System.out.println(methodName + ":" + abstimeexps.get(0).valueLowerbound.toDurationString(false));
        final var ex1Lower = new NTime(NumexpSymbol.INFINITY, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY);
        final var ex1Upper = new NTime(-NumexpSymbol.INFINITY, 3, 11, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY);
        org.junit.Assert.assertEquals("3月11日以前", abstimeexps.get(0).originalExpression);
        org.junit.Assert.assertTrue(this.isSameTime(ex1Lower, abstimeexps.get(0).valueLowerbound));
        org.junit.Assert.assertTrue(this.isSameTime(ex1Upper, abstimeexps.get(0).valueUpperbound));
    }


    @Test
    void orOver() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "3月11日以降に、";
        final List <AbstimeExpression> abstimeexps = new ArrayList <>();
        final var language = "ja";
        this.AEN = new AbstimeExpressionNormalizerImpl(language);
        this.AEN.process(text, abstimeexps);
        System.out.println(methodName + ":" + abstimeexps.get(0).valueLowerbound.toDurationString(false));
        final var ex1Lower = new NTime(NumexpSymbol.INFINITY, 3, 11, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY);
        final var ex1Upper = new NTime(-NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY);
        org.junit.Assert.assertEquals("3月11日以降", abstimeexps.get(0).originalExpression);
        org.junit.Assert.assertTrue(this.isSameTime(ex1Lower, abstimeexps.get(0).valueLowerbound));
        org.junit.Assert.assertTrue(this.isSameTime(ex1Upper, abstimeexps.get(0).valueUpperbound));
    }


    @Test
    void range1() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "15時〜18時の間に、";
        final List <AbstimeExpression> abstimeexps = new ArrayList <>();
        final var language = "ja";
        this.AEN = new AbstimeExpressionNormalizerImpl(language);
        this.AEN.process(text, abstimeexps);
        System.out.println(methodName + ":" + abstimeexps.get(0).valueLowerbound.toDurationString(false));
        final var ex1Lower = new NTime(NumexpSymbol.INFINITY, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY, 15, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY);
        final var ex1Upper = new NTime(-NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, 18, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY);
        org.junit.Assert.assertEquals("15時〜18時", abstimeexps.get(0).originalExpression);
        org.junit.Assert.assertTrue(this.isSameTime(ex1Lower, abstimeexps.get(0).valueLowerbound));
        org.junit.Assert.assertTrue(this.isSameTime(ex1Upper, abstimeexps.get(0).valueUpperbound));
    }


    @Test
    void range2() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "15:00から18:00の間に、";
        final List <AbstimeExpression> abstimeexps = new ArrayList <>();
        final var language = "ja";
        this.AEN = new AbstimeExpressionNormalizerImpl(language);
        this.AEN.process(text, abstimeexps);
        System.out.println(methodName + ":" + abstimeexps.get(0).valueLowerbound.toDurationString(false));
        final var ex1Lower = new NTime(NumexpSymbol.INFINITY, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY, 15, 0, NumexpSymbol.INFINITY);
        final var ex1Upper = new NTime(-NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, 18, 0, -NumexpSymbol.INFINITY);
        org.junit.Assert.assertEquals("15:00から18:00", abstimeexps.get(0).originalExpression);
        org.junit.Assert.assertTrue(this.isSameTime(ex1Lower, abstimeexps.get(0).valueLowerbound));
        org.junit.Assert.assertTrue(this.isSameTime(ex1Upper, abstimeexps.get(0).valueUpperbound));
    }


    @Test
    void range3() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "15~18時の間に、";
        final List <AbstimeExpression> abstimeexps = new ArrayList <>();
        final var language = "ja";
        this.AEN = new AbstimeExpressionNormalizerImpl(language);
        this.AEN.process(text, abstimeexps);
        System.out.println(methodName + ":" + abstimeexps.get(0).valueLowerbound.toDurationString(false));
        final var ex1Lower = new NTime(NumexpSymbol.INFINITY, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY, 15, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY);
        final var ex1Upper = new NTime(-NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, 18, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY);
        org.junit.Assert.assertEquals("15~18時", abstimeexps.get(0).originalExpression);
        org.junit.Assert.assertTrue(this.isSameTime(ex1Lower, abstimeexps.get(0).valueLowerbound));
        org.junit.Assert.assertTrue(this.isSameTime(ex1Upper, abstimeexps.get(0).valueUpperbound));
    }


    @Test
    void range4() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "2012/3/8~3/10の間に、";
        final List <AbstimeExpression> abstimeexps = new ArrayList <>();
        final var language = "ja";
        this.AEN = new AbstimeExpressionNormalizerImpl(language);
        this.AEN.process(text, abstimeexps);
        System.out.println(methodName + ":" + abstimeexps.get(0).valueLowerbound.toDurationString(false));
        final var ex1Lower = new NTime(2012, 3, 8, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY);
        final var ex1Upper = new NTime(2012, 3, 10, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY);
        org.junit.Assert.assertEquals("2012/3/8~3/10", abstimeexps.get(0).originalExpression);
        org.junit.Assert.assertTrue(this.isSameTime(ex1Lower, abstimeexps.get(0).valueLowerbound));
        org.junit.Assert.assertTrue(this.isSameTime(ex1Upper, abstimeexps.get(0).valueUpperbound));
    }


    @Test
    void ambiguous1() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "2011.3";
        final List <AbstimeExpression> abstimeexps = new ArrayList <>();
        final var language = "ja";
        this.AEN = new AbstimeExpressionNormalizerImpl(language);
        this.AEN.process(text, abstimeexps);
        System.out.println(methodName + ":" + abstimeexps.get(0).valueLowerbound.toDurationString(false));
        final var ex1Lower = new NTime(2011, 3, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY);
        final var ex1Upper = new NTime(2011, 3, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY);
        org.junit.Assert.assertEquals("2011.3", abstimeexps.get(0).originalExpression);
        org.junit.Assert.assertTrue(this.isSameTime(ex1Lower, abstimeexps.get(0).valueLowerbound));
        org.junit.Assert.assertTrue(this.isSameTime(ex1Upper, abstimeexps.get(0).valueUpperbound));
    }


    @Test
    void ambiguous2() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "3.11";
        final List <AbstimeExpression> abstimeexps = new ArrayList <>();
        final var language = "ja";
        this.AEN = new AbstimeExpressionNormalizerImpl(language);
        this.AEN.process(text, abstimeexps);
        System.out.println(methodName + ":" + abstimeexps.get(0).valueLowerbound.toDurationString(false));
        final var ex1Lower = new NTime(NumexpSymbol.INFINITY, 3, 11, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY);
        final var ex1Upper = new NTime(-NumexpSymbol.INFINITY, 3, 11, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY);
        org.junit.Assert.assertEquals("3.11", abstimeexps.get(0).originalExpression);
        org.junit.Assert.assertTrue(this.isSameTime(ex1Lower, abstimeexps.get(0).valueLowerbound));
        org.junit.Assert.assertTrue(this.isSameTime(ex1Upper, abstimeexps.get(0).valueUpperbound));
    }


    @Test
    void chinese1() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "我生于1989年7月21日";
        final List <AbstimeExpression> abstimeexps = new ArrayList <>();
        final var language = "zh";
        this.AEN = new AbstimeExpressionNormalizerImpl(language);
        this.AEN.process(text, abstimeexps);
        System.out.println(methodName + ":" + abstimeexps.get(0).valueLowerbound.toDurationString(false));
        final var ex1Lower = new NTime(1989, 7, 21, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY, NumexpSymbol.INFINITY);
        final var ex1Upper = new NTime(1989, 7, 21, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY, -NumexpSymbol.INFINITY);
        org.junit.Assert.assertEquals("1989年7月21日", abstimeexps.get(0).originalExpression);
        org.junit.Assert.assertTrue(this.isSameTime(ex1Lower, abstimeexps.get(0).valueLowerbound));
        org.junit.Assert.assertTrue(this.isSameTime(ex1Upper, abstimeexps.get(0).valueUpperbound));
    }
}
