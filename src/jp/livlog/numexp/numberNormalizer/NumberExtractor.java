package jp.livlog.numexp.numberNormalizer;

import java.util.List;

import jp.livlog.numexp.digitUtility.DigitUtility;
import jp.livlog.numexp.share.ENotationType;
import jp.livlog.numexp.share.NNumber;
import jp.livlog.numexp.share.RefObject;

public abstract class NumberExtractor {

    protected DigitUtility digitUtility = null;

    public NumberExtractor(DigitUtility digitUtility) {

        this.digitUtility = digitUtility;
    }


    public abstract void extractNumber(String input, List <NNumber> output);


    protected abstract boolean isInvalidNotationType(List <ENotationType> notationType);


    protected abstract void returnLongestNumberStrings(StringBuilder uText, RefObject <Integer> i, StringBuilder numstr);
}
