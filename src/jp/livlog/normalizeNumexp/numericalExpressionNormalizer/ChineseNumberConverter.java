package jp.livlog.normalizeNumexp.numericalExpressionNormalizer;

import jp.livlog.normalizeNumexp.share.RefObject;

public abstract class ChineseNumberConverter extends NumberConverterTemplate {

    @Override
    protected abstract void convertArabicKansujiMixedOf4digit(String numberString, RefObject <Integer> numberConverted);
}