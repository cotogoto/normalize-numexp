package jp.livlog.normalizeNumexp.numericalExpressionNormalizer;

import java.util.List;

import jp.livlog.normalizeNumexp.digitUtility.DigitUtility;
import jp.livlog.normalizeNumexp.share.NNumber;

public abstract class SymbolFixer {

    protected DigitUtility digitUtility = null;

    public SymbolFixer(DigitUtility digitUtility) {

        this.digitUtility = digitUtility;
    }


    public abstract void fixNumbersBySymbol(String text, List <NNumber> numbers);


    protected abstract void fixPrefixSymbol(StringBuilder uText, List <NNumber> numbers, int i);


    protected abstract void fixIntermediateSymbol(StringBuilder uText, List <NNumber> numbers, int i);


    protected abstract void fixSuffixSymbol(StringBuilder uText, List <NNumber> numbers, int i);


    protected abstract boolean isPlus(StringBuilder uText, int i, StringBuilder plusStrings);


    protected abstract boolean isMinus(StringBuilder uText, int i, StringBuilder plusStrings);


    protected abstract double createDecimalValue(NNumber number);


    protected abstract void fixDecimalPoint(List <NNumber> numbers, int i, String decimalStrings);


    protected abstract void fixRangeExpression(List <NNumber> numbers, int i, String rangeStrings);

}