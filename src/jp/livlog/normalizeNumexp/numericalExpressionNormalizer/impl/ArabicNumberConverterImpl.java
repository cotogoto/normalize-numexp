package jp.livlog.normalizeNumexp.numericalExpressionNormalizer.impl;

import jp.livlog.normalizeNumexp.digitUtility.DigitUtility;
import jp.livlog.normalizeNumexp.numericalExpressionNormalizer.NumberConverterTemplate;
import jp.livlog.normalizeNumexp.share.RefObject;

public class ArabicNumberConverterImpl extends NumberConverterTemplate {

    public ArabicNumberConverterImpl(DigitUtility digitUtility) {

        super(digitUtility);
    }


    @Override
    public void convertNumber(String numberStringOrg, RefObject <Double> value, RefObject <Integer> numberType) {

        // TODO 自動生成されたメソッド・スタブ

    }


    @Override
    public void convertArabicKansujiMixedOf4digit(String numberString, RefObject <Integer> numberConverted) {

        // TODO 自動生成されたメソッド・スタブ

    }

}
