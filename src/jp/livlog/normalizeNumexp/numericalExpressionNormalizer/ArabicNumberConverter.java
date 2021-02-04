package jp.livlog.normalizeNumexp.numericalExpressionNormalizer;

import jp.livlog.normalizeNumexp.share.RefObject;

public abstract class ArabicNumberConverter extends NumberConverterTemplate {

    @Override
    public abstract void convertNumber(String numberStringOrg, RefObject <Double> value, RefObject <Integer> numberType);


    @Override
    protected abstract void convertArabicKansujiMixedOf4digit(String numberString, RefObject <Integer> numberConverted);
}
