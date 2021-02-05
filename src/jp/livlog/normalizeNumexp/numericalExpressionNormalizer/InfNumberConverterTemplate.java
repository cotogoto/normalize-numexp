package jp.livlog.normalizeNumexp.numericalExpressionNormalizer;

import jp.livlog.normalizeNumexp.share.RefObject;

public interface InfNumberConverterTemplate {

    void convertNumber(String numberStringOrg, RefObject <Double> value, RefObject <Integer> numberType);


    void convertArabicKansujiMixedOf4digit(String numberString, RefObject <Integer> numberConverted);


    void deleteComma(String ustr, StringBuilder ret);


    void convertArabicNumerals(String numberString, RefObject <Double> value);


    void convertArabicKansujiKuraiManMixed(String numberString, RefObject <Double> value);
}
