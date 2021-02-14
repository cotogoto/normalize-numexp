package jp.livlog.normalizeNumexp.normalizeNumexp;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import jp.livlog.normalizeNumexp.normalizeNumexp.impl.NormalizeNumexpImpl;

class NormalizeNumexpTest {

    NormalizeNumexp NN = null;

    @Test
    void simple1() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "1911年から2011年の間、その100年間において、9.3万人もの死傷者がでた。";
        final List <String> result = new ArrayList <>();
        final var language = "ja";
        this.NN = new NormalizeNumexpImpl(language);
        this.NN.normalize(text, result);
        for (final String line : result) {
            System.out.println(methodName + ":" + line);
        }
        org.junit.Assert.assertEquals(3, result.size());
        org.junit.Assert.assertEquals("numerical*9.3万人*27*32*人*93000*93000*", result.get(0));
        org.junit.Assert.assertEquals("abstime*1911年から2011年*0*12*none*1911-XX-XX*2011-XX-XX*", result.get(1));
        org.junit.Assert.assertEquals("duration*100年間*17*22*none*P100Y*P100Y*", result.get(2));
    }


    @Test
    void simple2() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "15年前、戦争があった";
        final List <String> result = new ArrayList <>();
        final var language = "ja";
        this.NN = new NormalizeNumexpImpl(language);
        this.NN.normalize(text, result);
        for (final String line : result) {
            System.out.println(methodName + ":" + line);
        }
        org.junit.Assert.assertEquals(1, result.size());
        org.junit.Assert.assertEquals("reltime*15年前*0*4*none*XX:XX:XX,P-15Y*XX:XX:XX,P-15Y*", result.get(0));
    }


    @Test
    void simple3() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "昨年3月、僕たち２人は結婚した";
        final List <String> result = new ArrayList <>();
        final var language = "ja";
        this.NN = new NormalizeNumexpImpl(language);
        this.NN.normalize(text, result);
        for (final String line : result) {
            System.out.println(methodName + ":" + line);
        }
        org.junit.Assert.assertEquals(2, result.size());
        org.junit.Assert.assertEquals("numerical*２人*8*10*人*2*2*", result.get(0));
        org.junit.Assert.assertEquals("reltime*昨年3月*0*4*none*XXXX-03-XX,P-1Y*XXXX-03-XX,P-1Y*", result.get(1));
    }


    @Test
    void simple4() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "131.1ポイントというスコアを叩き出した";
        final List <String> result = new ArrayList <>();
        final var language = "ja";
        this.NN = new NormalizeNumexpImpl(language);
        this.NN.normalize(text, result);
        for (final String line : result) {
            System.out.println(methodName + ":" + line);
        }
        org.junit.Assert.assertEquals(1, result.size());
        org.junit.Assert.assertEquals("numerical*131.1ポイント*0*9*ポイント*131.1*131.1*", result.get(0));
    }


    @Test
    void simple5() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "午後3時45分に待ち合わせ";
        final List <String> result = new ArrayList <>();
        final var language = "ja";
        this.NN = new NormalizeNumexpImpl(language);
        this.NN.normalize(text, result);
        for (final String line : result) {
            System.out.println(methodName + ":" + line);
        }
        org.junit.Assert.assertEquals(1, result.size());
        org.junit.Assert.assertEquals("abstime*午後3時45分*0*7*none*15:45:XX*15:45:XX*", result.get(0));
    }


    @Test
    void dayOfWeek1() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "5月3日(水)";
        final List <String> result = new ArrayList <>();
        final var language = "ja";
        this.NN = new NormalizeNumexpImpl(language);
        this.NN.normalize(text, result);
        for (final String line : result) {
            System.out.println(methodName + ":" + line);
        }
        org.junit.Assert.assertEquals(1, result.size());
        org.junit.Assert.assertEquals("abstime*5月3日(水)*0*7*none*XXXX-05-03*XXXX-05-03*Wed", result.get(0));
    }


    @Test
    void dayOfWeek2() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "2001/3/3 Sat";
        final List <String> result = new ArrayList <>();
        final var language = "ja";
        this.NN = new NormalizeNumexpImpl(language);
        this.NN.normalize(text, result);
        for (final String line : result) {
            System.out.println(methodName + ":" + line);
        }
        org.junit.Assert.assertEquals(1, result.size());
        org.junit.Assert.assertEquals("abstime*2001/3/3 Sat*0*12*none*2001-03-03*2001-03-03*Sat", result.get(0));
    }


    @Test
    void realExample1() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "【今日から開催】The Fruits of Adventures @ ZEIT-FOTO SALON(東京・京橋)  4/26(Tue)まで";
        final List <String> result = new ArrayList <>();
        final var language = "ja";
        this.NN = new NormalizeNumexpImpl(language);
        this.NN.normalize(text, result);
        for (final String line : result) {
            System.out.println(methodName + ":" + line);
        }
        org.junit.Assert.assertEquals(1, result.size());
        org.junit.Assert.assertEquals("abstime*4/26(Tue)まで*59*70*none*XXXX-04-26*XXXX-04-26*Tue", result.get(0));
    }


    @Test
    void inappropriateRange1() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "中国から30匹の鳥がきた";
        final List <String> result = new ArrayList <>();
        final var language = "ja";
        this.NN = new NormalizeNumexpImpl(language);
        this.NN.normalize(text, result);
        for (final String line : result) {
            System.out.println(methodName + ":" + line);
        }
        org.junit.Assert.assertEquals(1, result.size());
        org.junit.Assert.assertEquals("numerical*30匹*4*7*匹*30*30*kara_prefix", result.get(0));
    }


    @Test
    void inappropriateRange2() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "30匹からのプレゼント";
        final List <String> result = new ArrayList <>();
        final var language = "ja";
        this.NN = new NormalizeNumexpImpl(language);
        this.NN.normalize(text, result);
        for (final String line : result) {
            System.out.println(methodName + ":" + line);
        }
        org.junit.Assert.assertEquals(1, result.size());
        org.junit.Assert.assertEquals("numerical*30匹*0*3*匹*30*30*kara_suffix", result.get(0));
    }

    @Test
    void inappropriateRange3() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "一万年と二千年前から愛してる";
        final List <String> result = new ArrayList <>();
        final var language = "ja";
        this.NN = new NormalizeNumexpImpl(language);
        this.NN.normalize(text, result);
        for (final String line : result) {
            System.out.println(methodName + ":" + line);
        }
        org.junit.Assert.assertEquals(2, result.size());
        org.junit.Assert.assertEquals("reltime*二千年前*4*8*none*XX:XX:XX,P-2000Y*XX:XX:XX,P-2000Y*kara_suffix", result.get(0));
        org.junit.Assert.assertEquals("duration*一万年*0*3*none*P10000Y*P10000Y*", result.get(1));
    }


    @Test
    void inappropriateRange4() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "話をしよう。あれは今から36万年前………いや、1万4000年前だったか。";
        final List <String> result = new ArrayList <>();
        final var language = "ja";
        this.NN = new NormalizeNumexpImpl(language);
        this.NN.normalize(text, result);
        for (final String line : result) {
            System.out.println(methodName + ":" + line);
        }
        org.junit.Assert.assertEquals(2, result.size());
        org.junit.Assert.assertEquals("reltime*36万年前*12*17*none*XX:XX:XX,P-360000Y*XX:XX:XX,P-360000Y*kara_prefix", result.get(0));
        org.junit.Assert.assertEquals("reltime*1万4000年前*23*31*none*XX:XX:XX,P-14000Y*XX:XX:XX,P-14000Y*", result.get(1));
    }

    @Test
    void inappropriateStrings1() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "一体それがどうしたというのだね。九州。四国。";
        final List <String> result = new ArrayList <>();
        final var language = "ja";
        this.NN = new NormalizeNumexpImpl(language);
        this.NN.normalize(text, result);
        for (final String line : result) {
            System.out.println(methodName + ":" + line);
        }
        org.junit.Assert.assertEquals(0, result.size());
    }

    @Test
    void inappropriatePrefix1() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "ver2.3.4。ver２．３。";
        final List <String> result = new ArrayList <>();
        final var language = "ja";
        this.NN = new NormalizeNumexpImpl(language);
        this.NN.normalize(text, result);
        for (final String line : result) {
            System.out.println(methodName + ":" + line);
        }
        org.junit.Assert.assertEquals(0, result.size());
    }

    @Test
    void inappropriateAbstime1() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "080-6006-4451。ver2.0。";
        final List <String> result = new ArrayList <>();
        final var language = "ja";
        this.NN = new NormalizeNumexpImpl(language);
        this.NN.normalize(text, result);
        for (final String line : result) {
            System.out.println(methodName + ":" + line);
        }
        org.junit.Assert.assertEquals(0, result.size());
    }

    @Test
    void inappropriateAbstime2() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "198999年30月41日。";
        final List <String> result = new ArrayList <>();
        final var language = "ja";
        this.NN = new NormalizeNumexpImpl(language);
        this.NN.normalize(text, result);
        for (final String line : result) {
            System.out.println(methodName + ":" + line);
        }
        org.junit.Assert.assertEquals(3, result.size());
        org.junit.Assert.assertEquals("duration*198999年*0*7*none*P198999Y*P198999Y*", result.get(0));
        org.junit.Assert.assertEquals("duration*30月*7*10*none*P30M*P30M*", result.get(1));
        org.junit.Assert.assertEquals("duration*41日*10*13*none*P41D*P41D*", result.get(2));
    }

    @Test
    void url1() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "tttp3gl3molggg";
        final List <String> result = new ArrayList <>();
        final var language = "ja";
        this.NN = new NormalizeNumexpImpl(language);
        this.NN.normalize(text, result);
        for (final String line : result) {
            System.out.println(methodName + ":" + line);
        }
        org.junit.Assert.assertEquals(0, result.size());
    }


    @Test
    void reviseAbstime1() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "09年5月。99年5月";
        final List <String> result = new ArrayList <>();
        final var language = "ja";
        this.NN = new NormalizeNumexpImpl(language);
        this.NN.normalize(text, result);
        for (final String line : result) {
            System.out.println(methodName + ":" + line);
        }
        org.junit.Assert.assertEquals(2, result.size());
        org.junit.Assert.assertEquals("abstime*09年5月*0*5*none*2009-05-XX*2009-05-XX*", result.get(0));
        org.junit.Assert.assertEquals("abstime*99年5月*6*11*none*1999-05-XX*1999-05-XX*", result.get(1));
    }

    @Test
    void notAbstime1() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "1.2.2 2-2-2";
        final List <String> result = new ArrayList <>();
        final var language = "ja";
        this.NN = new NormalizeNumexpImpl(language);
        this.NN.normalize(text, result);
        for (final String line : result) {
            System.out.println(methodName + ":" + line);
        }
        org.junit.Assert.assertEquals(0, result.size());
    }

    @Test
    void reviseAbstime2() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "西暦99年5月";
        final List <String> result = new ArrayList <>();
        final var language = "ja";
        this.NN = new NormalizeNumexpImpl(language);
        this.NN.normalize(text, result);
        for (final String line : result) {
            System.out.println(methodName + ":" + line);
        }
        org.junit.Assert.assertEquals(1, result.size());
        org.junit.Assert.assertEquals("abstime*西暦99年5月*0*7*none*0099-05-XX*0099-05-XX*", result.get(0));
    }

    @Test
    void su1() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "数十人が十数人と喧嘩して、百数十円落とした";
        final List <String> result = new ArrayList <>();
        final var language = "ja";
        this.NN = new NormalizeNumexpImpl(language);
        this.NN.normalize(text, result);
        for (final String line : result) {
            System.out.println(methodName + ":" + line);
        }
        org.junit.Assert.assertEquals(3, result.size());
        org.junit.Assert.assertEquals("numerical*数十人*0*3*人*10*90*", result.get(0));
        org.junit.Assert.assertEquals("numerical*十数人*4*7*人*11*19*", result.get(1));
        org.junit.Assert.assertEquals("numerical*百数十円*13*17*円*110*190*", result.get(2));
    }

    @Test
    void range1() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "2012/4/3~6に行われる";
        final List <String> result = new ArrayList <>();
        final var language = "ja";
        this.NN = new NormalizeNumexpImpl(language);
        this.NN.normalize(text, result);
        for (final String line : result) {
            System.out.println(methodName + ":" + line);
        }
        org.junit.Assert.assertEquals(1, result.size());
        org.junit.Assert.assertEquals("abstime*2012/4/3~6*0*10*none*2012-04-03*2012-04-06*", result.get(0));
    }

    @Test
    void range2() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "2012/4/3~2012/4/6に行われる";
        final List <String> result = new ArrayList <>();
        final var language = "ja";
        this.NN = new NormalizeNumexpImpl(language);
        this.NN.normalize(text, result);
        for (final String line : result) {
            System.out.println(methodName + ":" + line);
        }
        org.junit.Assert.assertEquals(1, result.size());
        org.junit.Assert.assertEquals("abstime*2012/4/3~2012/4/6*0*17*none*2012-04-03*2012-04-06*", result.get(0));
    }

    @Test
    void wari1() {

        final var methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final var text = "彼の打率は3割4分5厘だ";
        final List <String> result = new ArrayList <>();
        final var language = "ja";
        this.NN = new NormalizeNumexpImpl(language);
        this.NN.normalize(text, result);
        for (final String line : result) {
            System.out.println(methodName + ":" + line);
        }
        org.junit.Assert.assertEquals(1, result.size());
        org.junit.Assert.assertEquals("numerical*3割4分5厘*5*11*%*34.5*34.5*", result.get(0));
    }
}
