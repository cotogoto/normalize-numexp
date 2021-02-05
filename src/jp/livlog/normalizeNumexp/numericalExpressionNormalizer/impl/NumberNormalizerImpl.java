package jp.livlog.normalizeNumexp.numericalExpressionNormalizer.impl;

import java.util.List;

import jp.livlog.normalizeNumexp.numericalExpressionNormalizer.NumberNormalizer;
import jp.livlog.normalizeNumexp.share.NNumber;

public class NumberNormalizerImpl extends NumberNormalizer {

    private boolean suffixIsArabic(final StringBuilder numberString) {

        return numberString.length() > 0
                && this.digitUtility.isArabic(this.digitUtility.getNumberStringCharacter(numberString, numberString.length() - 1));
    }


    private boolean prefix3digitIsArabic(final StringBuilder numberString) {

        return numberString.length() > 2
                && this.digitUtility.isArabic(this.digitUtility.getNumberStringCharacter(numberString, 0))
                && this.digitUtility.isArabic(this.digitUtility.getNumberStringCharacter(numberString, 1))
                && this.digitUtility.isArabic(this.digitUtility.getNumberStringCharacter(numberString, 2));
    }


    private boolean isValidCommaNotation(final StringBuilder numberString1, StringBuilder  numberString2) {

        return this.suffixIsArabic(numberString1)
                && this.prefix3digitIsArabic(numberString2)
                && (numberString2.length() == 3
                        || !this.digitUtility.isArabic(this.digitUtility.getNumberStringCharacter(numberString2, 3)));
    }


    private void joinNumbersByComma(final String text, List <NNumber> numbers) {

        // カンマ表記を統合する。カンマは「3,000,000」のように3桁ごとに区切っているカンマしか数のカンマ表記とは認めない（「29,30」のような表記は認めない）
        for (var i = numbers.size() - 1; i > 0; i--) {
            if (numbers.get(i - 1).positionEnd != numbers.get(i).positionStart - 1) {
                continue;
            }
            final var ucharIntermediate = text.toCharArray()[numbers.get(i - 1).positionEnd];
            if (!this.digitUtility.isComma(ucharIntermediate)) {
                continue;
            }
            if (!this.isValidCommaNotation(new StringBuilder(numbers.get(i - 1).originalExpression) ,
                    new StringBuilder(numbers.get(i).originalExpression))) {
                continue;
            }

            numbers.get(i - 1).positionEnd = numbers.get(i).positionEnd;
            numbers.get(i - 1).originalExpression += ucharIntermediate;
            numbers.get(i - 1).originalExpression += numbers.get(i).originalExpression;
            numbers.remove(i);
        }
    }





    public NumberNormalizerImpl(String language) {

        super(language);
    }


    @Override
    public void process(String input, List <NNumber> output) {

        // TODO 自動生成されたメソッド・スタブ

    }


    @Override
    public void processDontFixBySymbol(String input, List <NNumber> output) {

        // TODO 自動生成されたメソッド・スタブ

    }

}
