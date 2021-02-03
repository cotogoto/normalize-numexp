package jp.livlog.normalizeNumexp.normalizerUtility;

import java.util.List;

import org.modelmapper.ModelMapper;

import jp.livlog.normalizeNumexp.share.Pair;
import jp.livlog.normalizeNumexp.share.Time;

public abstract class NormalizerUtility {

    abstract public void extractAfterString(StringBuilder uTextReplaced, int i, String afterString);


    abstract public void extractBeforeString(StringBuilder uTextReplaced, int i, String beforeString);


    abstract public void prefixSearch(String ustr, List <Pair <String, Integer>> patterns, int matchingPatternId);


    abstract public void suffixSearch(String ustr, List <Pair <String, Integer>> patternsRev, int matchingPatternId);


    abstract public void searchSuffixNumberModifier(StringBuilder uTextReplaced, int expPositionEnd,
            List <Pair <String, Integer>> suffixNumberModifierPatterns,
            int matchingPatternId);


    abstract public void searchPrefixNumberModifier(StringBuilder uTextReplaced, int expPositionStart,
            List <Pair <String, Integer>> prefixNumberModifierPatterns,
            int matchingPatternId);


    abstract public void replaceNumbersInText(String uText, List <jp.livlog.normalizeNumexp.share.Number> numbers,
            StringBuilder uTextReplaced);


    abstract public void shortenPlaceHolderInText(String text, StringBuilder textShortened);


    abstract public boolean isPlaceHolder(char uc);


    abstract public boolean isFinite(double value);


    abstract public boolean isNullTime(double time);


    abstract public String identifyTimeDetail(Time time);


    abstract public String reverseString(String str);


    @SuppressWarnings ("unchecked")
    public <T1, T2> void cast(final T1 a, T2 b) {

        final var modelMapper = new ModelMapper();
        b = (T2) modelMapper.map(a, b.getClass());
    }

    // LATIN LETTER ALVEOLAR CLICK
    public static final char PLACE_HOLDER = 'ǂ';

}
