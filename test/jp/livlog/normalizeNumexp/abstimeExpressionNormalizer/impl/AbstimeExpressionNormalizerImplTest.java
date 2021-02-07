package jp.livlog.normalizeNumexp.abstimeExpressionNormalizer.impl;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jp.livlog.normalizeNumexp.abstimeExpressionNormalizer.AbstimeExpression;
import jp.livlog.normalizeNumexp.abstimeExpressionNormalizer.AbstimeExpressionNormalizer;


class AbstimeExpressionNormalizerImplTest {


    AbstimeExpressionNormalizer AEN           = null;


    @BeforeEach
    public void initialize() {

//        this.digitUtility = new DigitUtilityImpl();
//        this.digitUtility.initKansuji("ja");
//        this.ne = new NumberExtractorImpl(this.digitUtility);
    }

    @Test
    void test() {

        final var text = "あの人は1989年7月21日午前3時に生まれた";
        final List <AbstimeExpression> abstimeexps = new ArrayList <>();
        final var language = "ja";
        this.AEN = new AbstimeExpressionNormalizerImpl(language);
        this.AEN.process(text, abstimeexps);
        org.junit.Assert.assertEquals(1, abstimeexps.size());
    }

}
