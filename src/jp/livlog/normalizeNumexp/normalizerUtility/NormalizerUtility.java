package jp.livlog.normalizeNumexp.normalizerUtility;

import java.util.List;
import java.util.NavigableSet;

import jp.livlog.normalizeNumexp.share.NNumber;
import jp.livlog.normalizeNumexp.share.NTime;
import jp.livlog.normalizeNumexp.share.Pair;
import jp.livlog.normalizeNumexp.share.RefObject;

public abstract class NormalizerUtility {

    abstract public void extractAfterString(StringBuilder uTextReplaced, int i, StringBuilder afterString);


    abstract public void extractBeforeString(StringBuilder uTextReplaced, int i, StringBuilder beforeString);


    abstract public Pair <String, Integer> prefixSearch(StringBuilder uTextReplaced, NavigableSet <Pair <String, Integer>> patterns,
            RefObject <Integer> matchingPatternId);


    abstract public Pair <String, Integer> suffixSearch(StringBuilder uTextReplaced, NavigableSet <Pair <String, Integer>> patternsRev,
            RefObject <Integer> matchingPatternId);


    abstract public void searchSuffixNumberModifier(StringBuilder uTextReplaced, int expPositionEnd,
            NavigableSet <Pair <String, Integer>> suffixNumberModifierPatterns,
            RefObject <Integer> matchingPatternId);


    abstract public void searchPrefixNumberModifier(StringBuilder uTextReplaced, int expPositionStart,
            NavigableSet <Pair <String, Integer>> prefixNumberModifierPatterns,
            RefObject <Integer> matchingPatternId);


    abstract public void replaceNumbersInText(StringBuilder uText, List <NNumber> numbers,
            StringBuilder uTextReplaced);


    abstract public void shortenPlaceHolderInText(StringBuilder text, StringBuilder textShortened);


    abstract public boolean isPlaceHolder(char uc);


    abstract public boolean isFinite(double value);


    abstract public boolean isNullTime(NTime time);


    abstract public String identifyTimeDetail(NTime time);


    abstract public String reverseString(String str);

    // @SuppressWarnings ("unchecked")
    // public <T1, T2> void cast(final T1 a, T2 b) {
    //
    // final var modelMapper = new ModelMapper();
    // b = (T2) modelMapper.map(a, b.getClass());
    // }

    // LATIN LETTER ALVEOLAR CLICK
    public static final char PLACE_HOLDER = 'ǂ';

}
