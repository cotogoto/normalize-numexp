package jp.livlog.numexp.numberNormalizer.impl;

import java.util.List;

import jp.livlog.numexp.digitUtility.DigitUtility;
import jp.livlog.numexp.numberNormalizer.SymbolFixer;
import jp.livlog.numexp.share.NNumber;
import jp.livlog.numexp.share.Symbol;

public class SymbolFixerImpl extends SymbolFixer {

    public SymbolFixerImpl(DigitUtility digitUtility) {

        super(digitUtility);
    }


    @Override
    public void fixNumbersBySymbol(String text, List <NNumber> numbers) {

        final var uText = new StringBuilder(text);
        for (var i = 0; i < numbers.size(); i++) {
            this.fixPrefixSymbol(uText, numbers, i);
            this.fixIntermediateSymbol(uText, numbers, i);
            this.fixSuffixSymbol(uText, numbers, i);
        }
    }


    @Override
    protected void fixPrefixSymbol(StringBuilder uText, List <NNumber> numbers, int i) {

        final var plusStrings = new StringBuilder();
        final var minusStrings = new StringBuilder();

        if (this.isPlus(uText, numbers.get(i).positionStart - 1, plusStrings)) {
            numbers.get(i).originalExpression = plusStrings + numbers.get(i).originalExpression;
            numbers.get(i).positionStart--;
        } else if (this.isMinus(uText, numbers.get(i).positionStart - 1, minusStrings)) {
            numbers.get(i).valueLowerbound *= -1;
            numbers.get(i).valueUpperbound *= -1;
            numbers.get(i).originalExpression = minusStrings + numbers.get(i).originalExpression;
            numbers.get(i).positionStart--;
        }
    }


    @Override
    protected void fixIntermediateSymbol(StringBuilder uText, List <NNumber> numbers, int i) {

        if (numbers.size() - 1 <= i) {
            return;
        }

        final var a = numbers.get(i).positionEnd;
        final var b = numbers.get(i + 1).positionStart; // - numbers.get(i).positionEnd;

        if (a > b) {
            return;
        }

        final var intermediate = uText.substring(a, b);
        if (intermediate.length() == 0) {
            return;
        }

        // final String decimal_strings;
        if (numbers.get(i).valueLowerbound == Symbol.INFINITY || numbers.get(i + 1).valueLowerbound == Symbol.INFINITY) {
            return;
        }

        if (this.digitUtility.isDecimalPoint(intermediate.charAt(0))) {
            this.fixDecimalPoint(numbers, i, intermediate);
        }

        if (this.digitUtility.isRangeExpression(intermediate)
                || (this.digitUtility.isComma(intermediate.charAt(0))
                        && intermediate.length() == 1
                        && (numbers.get(i).valueLowerbound == numbers.get(i + 1).valueUpperbound - 1))) { // 範囲表現か、コンマの並列表現のとき
            this.fixRangeExpression(numbers, i, intermediate);
        }
    }


    @Override
    protected void fixSuffixSymbol(StringBuilder uText, List <NNumber> numbers, int i) {

        // suffixの処理は特にない
    }


    @Override
    protected boolean isPlus(StringBuilder uText, int i, StringBuilder plusStrings) {

        plusStrings.setLength(0);

        if (i < 0) {
            return false;
        }

        var str = String.valueOf(uText.charAt(i));
        if ("+".equals(str) || "＋".equals(str)) {
            plusStrings.append(str);
            return true;
        }

        if (i < 2) {
            return false;
        }
        str = uText.substring(i - 2, i + 1);
        if ("プラス".equals(str)) {
            plusStrings.append(str);
            return true;
        }
        return false;
    }


    @Override
    protected boolean isMinus(StringBuilder uText, int i, StringBuilder minusStrings) {

        minusStrings.setLength(0);

        if (i < 0) {
            return false;
        }
        var str = String.valueOf(uText.charAt(i));
        if ("-".equals(str) || "ー".equals(str) || "－".equals(str)) {
            minusStrings.append(str);
            return true;
        }

        if (i < 3) {
            return false;
        }
        str = uText.substring(i - 3, i + 1);
        if ("マイナス".equals(str)) {
            minusStrings.append(str);
            return true;
        }
        return false;
    }


    @Override
    protected double createDecimalValue(NNumber number) {

        double decimal;
        decimal = number.valueLowerbound;

        // 1より小さくなるまで0.1を乗算する
        while (decimal >= 1) {
            decimal *= 0.1;
        }

        // 「1.001」のような0が含まれる表記のため、先頭のゼロの分、0.1を乗算する
        var pos = 0;
        final var len = number.originalExpression.length();
        while (true) {
            if (len <= pos) {
                break;
            }
            final var str = String.valueOf(number.originalExpression.charAt(pos));
            if (!"0".equals(str) && !"０".equals(str) && !"零".equals(str) && !"〇".equals(str)) {
                break;
            }
            decimal *= 0.1;
            pos++;
        }

        return decimal;
    }


    @Override
    protected void fixDecimalPoint(List <NNumber> numbers, int i, String decimalStrings) {

        // 小数点の処理を行う。「3.14」「9.3万」など。
        // 「3.14」の場合、小数点以下を10^(-n)乗してから、小数点以上の値に付け加える
        // 「9.3万」の場合、先に「万」を無視して上と同じ処理を行ってから、最後に「万」分の処理を行う
        final var decimal = this.createDecimalValue(numbers.get(i + 1));
        numbers.get(i).valueLowerbound += decimal;
        final var uc = numbers.get(i + 1).originalExpression.charAt(numbers.get(i + 1).originalExpression.length() - 1);
        if (this.digitUtility.isKansujiKuraiMan(uc)) {
            final var powerValue = this.digitUtility.convertKansujiKuraiToPowerValue(uc);
            numbers.get(i).valueLowerbound *= Math.pow(10, powerValue);
        }

        numbers.get(i).valueUpperbound = numbers.get(i).valueLowerbound;
        numbers.get(i).originalExpression += decimalStrings;
        numbers.get(i).originalExpression += numbers.get(i + 1).originalExpression;
        numbers.get(i).positionEnd = numbers.get(i + 1).positionEnd;
        numbers.remove(i + 1);
    }


    @Override
    protected void fixRangeExpression(List <NNumber> numbers, int i, String rangeStrings) {

        numbers.get(i).valueUpperbound = numbers.get(i + 1).valueLowerbound;
        // numbers.get(i).positionEnd = numbers.get(i + 1).positionEnd;
        numbers.get(i).originalExpression += rangeStrings;
        numbers.get(i).originalExpression += numbers.get(i + 1).originalExpression;
        numbers.get(i).positionEnd = numbers.get(i + 1).positionEnd;
        numbers.remove(i + 1);
    }

}