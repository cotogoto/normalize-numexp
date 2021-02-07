package jp.livlog.normalizeNumexp.numericalExpressionNormalizer.impl;

import java.text.Normalizer;
import java.util.List;

import jp.livlog.normalizeNumexp.digitUtility.DigitUtility;
import jp.livlog.normalizeNumexp.numericalExpressionNormalizer.NumberConverterTemplate;
import jp.livlog.normalizeNumexp.share.ENotationType;
import jp.livlog.normalizeNumexp.share.RefObject;

public class ArabicNumberConverterImpl extends NumberConverterTemplate {

    public ArabicNumberConverterImpl(DigitUtility digitUtility) {

        super(digitUtility);
    }


    @Override
    public void convertNumber(String numberStringOrg, RefObject <Double> value, List <ENotationType> notationType) {


        //日本語、中国語以外では漢数字がでてこないので、アラビア数字を値として認識する処理のみを行う
        //もし漢数字以外にも、アラビア数字以外の表記を数字として認識させたい場合は、ExtractorとConverterの2つに処理を追記する
        var numberString = new StringBuilder();
        this.deleteComma(numberStringOrg, numberString);
        numberString = new StringBuilder(Normalizer.normalize(numberString.toString(), Normalizer.Form.NFKC));

        this.convertArabicNumerals(numberString.toString(), value);
    }


    @Override
    public void convertArabicKansujiMixedOf4digit(String numberString, RefObject <Integer> numberConverted) {

    }

}
