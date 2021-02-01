package jp.livlog.normalizeNumexp.numericalExpressionNormalizer;

import java.util.List;

import jp.livlog.normalizeNumexp.digitUtility.DigitUtility;
import jp.livlog.normalizeNumexp.digitUtility.impl.DigitUtilityImpl;
import jp.livlog.normalizeNumexp.share.RefObject;

public abstract class NumberNormalizer {

    public DigitUtility digitUtility = new DigitUtilityImpl();

    public abstract class NumberExtractor {

        public abstract void extractNumber(String input, List <DigitUtility.Number> output);


        public abstract boolean isInvalidNotationType(int notationType);


        public abstract void returnLongestNumberStrings(String uText, RefObject <Integer> i, String numstr);
    }

    public abstract class NumberConverterTemplate {

        public abstract void convertNumber(String numberStringOrg, RefObject <Double> value, RefObject <Integer> numberType);


        public abstract void convertArabicKansujiMixedOf4digit(String numberString, RefObject <Integer> numberConverted);


        public abstract void deleteComma(String ustr, String ret);


        public abstract void convertArabicNumerals(String numberString, RefObject <Double> value);


        public abstract void convertArabicKansujiKuraiManMixed(String numberString, RefObject <Double> value);


        public abstract void convertArabicKansujiMixed(String numberString, RefObject <Double> value);
    }

    public abstract class JapaneseNumberConverter extends NumberConverterTemplate {

        @Override
        public abstract void convertArabicKansujiMixedOf4digit(String numberString, RefObject <Integer> numberConverted);
    }

    public abstract class ChineseNumberConverter extends NumberConverterTemplate {

        @Override
        public abstract void convertArabicKansujiMixedOf4digit(String numberString, RefObject <Integer> numberConverted);
    }

    public abstract class ArabicNumberConverter extends NumberConverterTemplate {

        @Override
        public abstract void convertNumber(String numberStringOrg, RefObject <Double> value, RefObject <Integer> numberType);


        @Override
        public abstract void convertArabicKansujiMixedOf4digit(String numberString, RefObject <Integer> numberConverted);
    }

    public abstract class SymbolFixer {

        public abstract void fixNumbersBySymbol(String text, List <DigitUtility.Number> numbers);


        public abstract boolean isPlus(String uText, int i, String plusStrings);


        public abstract boolean isMinus(String uText, int i, String plusStrings);


        public abstract void fixPrefixSymbol(String uText, List <DigitUtility.Number> numbers, int i);


        public abstract double createDecimalValue(DigitUtility.Number number);


        public abstract void fixDecimalPoint(List <DigitUtility.Number> numbers, int i, String decimalStrings);


        public abstract void fixRangeExpression(List <DigitUtility.Number> numbers, int i, String rangeStrings);


        public abstract void fixIntermediateSymbol(String uText, List <DigitUtility.Number> numbers, int i);


        public abstract void fixSuffixSymbol(String uText, List <DigitUtility.Number> numbers, int i);
    }

    public NumberNormalizer(String language) {

        this.language = language;
        this.digitUtility.initKansuji(language);
    }


    public abstract void process(String input, List <DigitUtility.Number> output);


    // 絶対時間表現の規格化の際に使用する（絶対時間表現では、前もって記号を処理させないため）
    public abstract void processDontFixBySymbol(String input, List <DigitUtility.Number> output);

    public String language;
}
