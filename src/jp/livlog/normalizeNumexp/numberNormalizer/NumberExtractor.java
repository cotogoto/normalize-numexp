package jp.livlog.normalizeNumexp.numberNormalizer;

import java.util.List;

import jp.livlog.normalizeNumexp.digitUtility.DigitUtility;
import jp.livlog.normalizeNumexp.share.ENotationType;
import jp.livlog.normalizeNumexp.share.NNumber;
import jp.livlog.normalizeNumexp.share.RefObject;

public abstract class NumberExtractor {

    protected DigitUtility digitUtility = null;

    public NumberExtractor(DigitUtility digitUtility) {

        this.digitUtility = digitUtility;
    }


    public abstract void extractNumber(String input, List <NNumber> output);


    protected abstract boolean isInvalidNotationType(List <ENotationType> notationType);


    protected abstract void returnLongestNumberStrings(StringBuilder uText, RefObject <Integer> i, StringBuilder numstr);
}
