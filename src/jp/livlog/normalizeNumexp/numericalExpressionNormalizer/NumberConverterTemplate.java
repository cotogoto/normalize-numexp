package jp.livlog.normalizeNumexp.numericalExpressionNormalizer;

import jp.livlog.normalizeNumexp.share.RefObject;

public abstract class NumberConverterTemplate {

    public abstract void convertNumber(String numberStringOrg, RefObject <Double> value, RefObject <Integer> numberType);


    protected abstract void convertArabicKansujiMixedOf4digit(String numberString, RefObject <Integer> numberConverted);


    protected abstract void deleteComma(String ustr, String ret);


    protected abstract void convertArabicNumerals(String numberString, RefObject <Double> value);


    protected abstract void convertArabicKansujiKuraiManMixed(String numberString, RefObject <Double> value);


    protected abstract void convertArabicKansujiMixed(String numberString, RefObject <Double> value);
}
