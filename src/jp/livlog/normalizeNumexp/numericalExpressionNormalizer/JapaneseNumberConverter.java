package jp.livlog.normalizeNumexp.numericalExpressionNormalizer;

import jp.livlog.normalizeNumexp.share.RefObject;

public abstract class JapaneseNumberConverter extends NumberConverterTemplate {

    @Override
    protected abstract void convertArabicKansujiMixedOf4digit(String numberString, RefObject <Integer> numberConverted);
}
