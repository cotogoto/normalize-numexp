package jp.livlog.numexp.numberNormalizer.impl;

import jp.livlog.numexp.digitUtility.DigitUtility;
import jp.livlog.numexp.numberNormalizer.NumberConverterTemplate;
import jp.livlog.numexp.share.RefObject;

public class ChineseNumberConverterImpl extends NumberConverterTemplate {

    public ChineseNumberConverterImpl(DigitUtility digitUtility) {

        super(digitUtility);
    }


    @Override
    public void convertArabicKansujiMixedOf4digit(String numberString, RefObject <Integer> numberConverted) {

        /* 基本的に日本語と同じだが、「０」の扱いが異なる。「千二百三」は「1230」となり、「千二百零三」が「1203」となる。
         * よって、直前に零が出てきたかどうか、また出てきた場合のために前の桁を覚えておく必要がある。
         */
        numberConverted.argValue = 0;
        var tmpnum = 0;
        var powerValue = 1;
        var prevIsZero = false; // chinese specific
        var currentIsZero = false;
        for (var i = 0; i < numberString.length(); i++) {
            final var uc = numberString.charAt(i);
            if (this.digitUtility.isKansujiKuraiSen(uc)) {
                if (tmpnum == 0) {
                    tmpnum = 1;
                }
                powerValue = this.digitUtility.convertKansujiKuraiToPowerValue(uc);
                numberConverted.argValue += (int) (tmpnum * Math.pow(10, powerValue));
                tmpnum = 0;
            } else if (this.digitUtility.isKansuji09(uc)) {
                tmpnum = tmpnum * 10 + this.digitUtility.convertKansuji09ToValue(uc);
                prevIsZero = currentIsZero;
                if (tmpnum == 0) {
                    currentIsZero = true;
                } else {
                    currentIsZero = false;
                }
            } else if (this.digitUtility.isHankakusuji(uc)) {
                final var castTmp = Integer.parseInt(String.valueOf(uc));
                tmpnum = tmpnum * 10 + castTmp;
                prevIsZero = currentIsZero;
                if (tmpnum == 0) {
                    currentIsZero = true;
                } else {
                    currentIsZero = false;
                }
            }
        }
        if (tmpnum != 0) {
            if (prevIsZero) {
                numberConverted.argValue += tmpnum;
            } else {
                numberConverted.argValue += (int) (tmpnum * Math.pow(10, powerValue - 1));
            }
        }
    }
}
