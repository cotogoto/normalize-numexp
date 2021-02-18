package jp.livlog.numexp.numberNormalizer;

import java.util.List;

import jp.livlog.numexp.share.ENotationType;
import jp.livlog.numexp.share.RefObject;

public interface InfNumberConverterTemplate {

    void convertNumber(String numberStringOrg, RefObject <Double> value, List <ENotationType> notationType);


    void convertArabicKansujiMixedOf4digit(String numberString, RefObject <Integer> numberConverted);


    void deleteComma(String ustr, StringBuilder ret);


    void convertArabicNumerals(String numberString, RefObject <Double> value);


    void convertArabicKansujiKuraiManMixed(String numberString, RefObject <Double> value);
}
