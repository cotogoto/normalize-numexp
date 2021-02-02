package jp.livlog.normalizeNumexp.numericalExpressionNormalizer;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import jp.livlog.normalizeNumexp.digitUtility.DigitUtility;
import jp.livlog.normalizeNumexp.numericalExpressionNormalizer.NumberNormalizer.NumberExtractor;
import jp.livlog.normalizeNumexp.numericalExpressionNormalizer.impl.NumberNormalizerImpl;

class NumberNormalizerTest {

    @Test
    void testNumberNormalizer() {

        final var language = "ja";

        final var input = "それは三十二年前の出来事";
        final NumberExtractor ne = new NumberNormalizerImpl(language).new NumberExtractorImpl();
        final List <DigitUtility.Number> result = new ArrayList<>();

        ne.extractNumber(input, result);
//        EXPECT_EQ(string_to_ustring("三十二"), result[0].original_expression);
    }

}
