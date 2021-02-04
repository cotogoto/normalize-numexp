package jp.livlog.normalizeNumexp.numericalExpressionNormalizer;

import java.util.List;

import jp.livlog.normalizeNumexp.share.NNumber;

public abstract class SymbolFixer {

    public abstract void fixNumbersBySymbol(String text, List <NNumber> numbers);


    public abstract boolean isPlus(String uText, int i, String plusStrings);


    public abstract boolean isMinus(String uText, int i, String plusStrings);


    public abstract void fixPrefixSymbol(String uText, List <NNumber> numbers, int i);


    public abstract double createDecimalValue(NNumber number);


    public abstract void fixDecimalPoint(List <NNumber> numbers, int i, String decimalStrings);


    public abstract void fixRangeExpression(List <NNumber> numbers, int i, String rangeStrings);


    public abstract void fixIntermediateSymbol(String uText, List <NNumber> numbers, int i);


    public abstract void fixSuffixSymbol(String uText, List <NNumber> numbers, int i);
}