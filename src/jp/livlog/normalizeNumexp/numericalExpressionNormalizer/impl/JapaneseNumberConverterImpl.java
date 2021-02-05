package jp.livlog.normalizeNumexp.numericalExpressionNormalizer.impl;

import jp.livlog.normalizeNumexp.digitUtility.DigitUtility;
import jp.livlog.normalizeNumexp.numericalExpressionNormalizer.NumberConverterTemplate;
import jp.livlog.normalizeNumexp.share.RefObject;

public class JapaneseNumberConverterImpl extends NumberConverterTemplate {

    public JapaneseNumberConverterImpl(DigitUtility digitUtility) {

        super(digitUtility);
    }


    @Override
    public void convertArabicKansujiMixedOf4digit(String numberString, RefObject <Integer> numberConverted) {

        numberConverted.argValue = 0;
        var tmpnum = 0; // 直前の数字を格納
        for (var i = 0; i < numberString.length(); i++) {
            final var uc = numberString.toCharArray()[i];
            if (this.digitUtility.isKansujiKuraiSen(uc)) {
                if (tmpnum == 0) {
                    tmpnum = 1;
                } // 直前に数字がでてこなかったら、そのままその位を適応（「十」＝１０）
                numberConverted.argValue += (int) (tmpnum * Math.pow(10, this.digitUtility.convertKansujiKuraiToPowerValue(uc)));
                tmpnum = 0;
            } else if (this.digitUtility.isKansuji09(uc)) {
                tmpnum = tmpnum * 10 + this.digitUtility.convertKansuji09ToValue(uc);
            } else if (this.digitUtility.isHankakusuji(uc)) {
                final var castTmp = Integer.parseInt(String.valueOf(uc));
                tmpnum = tmpnum * 10 + castTmp;
            }
        }
        if (tmpnum != 0) {
            numberConverted.argValue += tmpnum;
        }
    }

}