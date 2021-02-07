package jp.livlog.normalizeNumexp.numericalExpressionNormalizer;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import jp.livlog.normalizeNumexp.digitUtility.DigitUtility;
import jp.livlog.normalizeNumexp.share.ENotationType;
import jp.livlog.normalizeNumexp.share.Pair;
import jp.livlog.normalizeNumexp.share.RefObject;

public abstract class NumberConverterTemplate implements InfNumberConverterTemplate {

    protected DigitUtility digitUtility = null;

    public NumberConverterTemplate(DigitUtility digitUtility) {

        this.digitUtility = digitUtility;
    }


    @Override
    public void convertNumber(String numberStringOrg, RefObject <Double> value, List <ENotationType> notationType) {

        var numberString = new StringBuilder();
        this.deleteComma(numberStringOrg, numberString);
        numberString = new StringBuilder(Normalizer.normalize(numberString.toString(), Normalizer.Form.NFKC));

        value.argValue = 0d;
        final var numberStringSplited = new ArrayList <Pair <String, Character>>();
        this.splitByKansujiKurai(numberString, numberStringSplited); // 「億」「万」などの単位で区切る
        final var numberConverted = new RefObject <>(0);
        for (var i = 0; i < numberStringSplited.size(); i++) {
            this.convertArabicKansujiMixedOf4digit(numberStringSplited.get(i).first, numberConverted);
            if (numberConverted.argValue == 0 && numberStringSplited.get(i).second != '　') {
                if (value.argValue == 0d) {
                    // 「万」「億」など単体で出てくるとき。「数万」などの処理のため、これも規格化しておく
                    numberConverted.argValue = 1;
                } else {
                    // numberConverted.argValue = (int) value.argValue;
                    // 「一億万」など、「億」「万」で区切ったときに前に数字がない場合、とりあえず前までの値を参照する
                    // TODO : invalidとして扱わないことにした。必要なケースはあるか？
                }
            }

            value.argValue += numberConverted.argValue
                    * Math.pow(10, this.digitUtility.convertKansujiKuraiToPowerValue(numberStringSplited.get(i).second));
            numberConverted.argValue = 0;
        }

    }

    // @Override
    // public void convertArabicKansujiMixedOf4digit(StringBuilder numberString, RefObject <Integer> numberConverted) {
    //
    // }


    @Override
    public void deleteComma(String ustr, StringBuilder ret) {

        ret.setLength(0);
        for (var i = 0; i < ustr.length(); i++) {
            final var uc = ustr.charAt(i);
            if (!this.digitUtility.isComma(uc)) {
                ret.append(uc);
            }
        }
    }


    @Override
    public void convertArabicNumerals(String numberString, RefObject <Double> value) {

        value.argValue = Double.valueOf(numberString);
    }


    @Override
    public void convertArabicKansujiKuraiManMixed(String numberString, RefObject <Double> value) {

    }


    private void splitByKansujiKurai(StringBuilder numberString, List <Pair <String, Character>> numberstringSplited) {

        numberstringSplited.clear();
        var ustr = new StringBuilder();
        for (var i = 0; i < numberString.length(); i++) {
            final var uc = numberString.charAt(i);
            if (this.digitUtility.isKansujiKuraiMan(uc)) {
                numberstringSplited.add(new Pair <>(ustr.toString(), uc));
                ustr = new StringBuilder();
            } else {
                ustr.append(uc);
            }
        }
        numberstringSplited.add(new Pair <>(ustr.toString(), '　'));
    }

}