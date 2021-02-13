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
}
