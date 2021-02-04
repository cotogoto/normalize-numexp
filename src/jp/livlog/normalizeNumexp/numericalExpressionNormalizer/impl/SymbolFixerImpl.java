package jp.livlog.normalizeNumexp.numericalExpressionNormalizer.impl;

import java.util.List;

import jp.livlog.normalizeNumexp.numericalExpressionNormalizer.SymbolFixer;
import jp.livlog.normalizeNumexp.share.NNumber;

public class SymbolFixerImpl extends SymbolFixer {

    @Override
    public void fixNumbersBySymbol(String text, List <NNumber> numbers) {

        // TODO 自動生成されたメソッド・スタブ

    }


    @Override
    public boolean isPlus(String uText, int i, String plusStrings) {

        // TODO 自動生成されたメソッド・スタブ
        return false;
    }


    @Override
    public boolean isMinus(String uText, int i, String plusStrings) {

        // TODO 自動生成されたメソッド・スタブ
        return false;
    }


    @Override
    public void fixPrefixSymbol(String uText, List <NNumber> numbers, int i) {

        // TODO 自動生成されたメソッド・スタブ

    }


    @Override
    public double createDecimalValue(NNumber number) {

        // TODO 自動生成されたメソッド・スタブ
        return 0;
    }


    @Override
    public void fixDecimalPoint(List <NNumber> numbers, int i, String decimalStrings) {

        // TODO 自動生成されたメソッド・スタブ

    }


    @Override
    public void fixRangeExpression(List <NNumber> numbers, int i, String rangeStrings) {

        // TODO 自動生成されたメソッド・スタブ

    }


    @Override
    public void fixIntermediateSymbol(String uText, List <NNumber> numbers, int i) {

        // TODO 自動生成されたメソッド・スタブ

    }


    @Override
    public void fixSuffixSymbol(String uText, List <NNumber> numbers, int i) {

        // TODO 自動生成されたメソッド・スタブ

    }

}