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
//        System.out.println(methodName + ":" + abstimeexps.get(0).valueLowerbound.toDurationString(false));
//        org.junit.Assert.assertEquals(1, abstimeexps.size());
//        final var ex2Lower = new NTime(Symbol.INFINITY, Symbol.INFINITY, Symbol.INFINITY, 15, Symbol.INFINITY, Symbol.INFINITY);
//        final var ex2Upper = new NTime(-Symbol.INFINITY, -Symbol.INFINITY, -Symbol.INFINITY, 15, -Symbol.INFINITY, -Symbol.INFINITY);
//        org.junit.Assert.assertEquals("午後3時", abstimeexps.get(0).originalExpression);
//        org.junit.Assert.assertTrue(this.isSameTime(ex2Lower, abstimeexps.get(0).valueLowerbound));
//        org.junit.Assert.assertTrue(this.isSameTime(ex2Upper, abstimeexps.get(0).valueUpperbound));
    }

}
