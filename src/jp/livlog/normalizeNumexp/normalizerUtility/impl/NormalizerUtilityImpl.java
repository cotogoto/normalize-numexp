package jp.livlog.normalizeNumexp.normalizerUtility.impl;

import java.util.List;

import jp.livlog.normalizeNumexp.normalizerUtility.NormalizerUtility;
import jp.livlog.normalizeNumexp.share.NNumber;
import jp.livlog.normalizeNumexp.share.NTime;
import jp.livlog.normalizeNumexp.share.Pair;
import jp.livlog.normalizeNumexp.share.Symbol;

public class NormalizerUtilityImpl extends NormalizerUtility {

    @Override
    public String reverseString(String str) {

        final var sb = new StringBuilder(str);
        str = sb.reverse().toString();

        return str;
    }


    @Override
    public void extractAfterString(StringBuilder text, int i, StringBuilder afterString) {

        final var value = text.substring(i);
        afterString = new StringBuilder(value);
    }


    @Override
    public void extractBeforeString(StringBuilder text, int i, StringBuilder beforeString) {

        final var value = text.substring(0, i);
        beforeString = new StringBuilder(value);
    }


    @Override
    public void prefixSearch(StringBuilder ustr, List <Pair <String, Integer>> patterns, int matchingPatternId) {

        /*
         * patternsの中から、ustrのprefixになっているものを探索（複数ある場合は最長のもの）
         */
        final var ustrShortened = new StringBuilder();
        this.shortenPlaceHolderInText(ustr, ustrShortened); // ustrは数字が一字一字、「*」に変換されているので、patternsの表記と食い違っている。*を縮約する操作を行う
        final var str = ustrShortened.toString();

        for (final Pair <String, Integer> pair : patterns) {
            if (pair.first.contains(str)) {
                matchingPatternId = pair.second;
                return;
            }
        }

        matchingPatternId = -1;
    }


    @Override
    public void suffixSearch(StringBuilder stringBeforeExpression, List <Pair <String, Integer>> patternsRev, int matchingPatternId) {

        /*
         * patternsの中から、ustrのsuffixになっているものを探索（複数ある場合は最長のもの）
         * あらかじめpatternsの文字列を逆にしたものを保管しておき（patterns_rev）、ustrも逆にしてしまい、その状態でprefixSearchを行った結果を返す
         */
        final var ustrShortened = new StringBuilder();
        this.shortenPlaceHolderInText(stringBeforeExpression, ustrShortened); // ustrは数字が一字一字、「*」に変換されているので、patternsの表記と食い違っている。*を縮約する操作を行う
        final var str = ustrShortened.toString();

        for (final Pair <String, Integer> pair : patternsRev) {
            if (pair.first.contains(str)) {
                matchingPatternId = pair.second;
                return;
            }
        }

        matchingPatternId = -1;
    }


    @Override
    public void searchSuffixNumberModifier(StringBuilder text, int expPositionEnd, List <Pair <String, Integer>> suffixNumberModifierPatterns,
            int matchingPatternId) {

        final var stringAfterExpression = new StringBuilder();
        this.extractAfterString(text, expPositionEnd, stringAfterExpression);
        this.prefixSearch(stringAfterExpression, suffixNumberModifierPatterns, matchingPatternId);
    }


    @Override
    public void searchPrefixNumberModifier(StringBuilder text, int expPositionStart, List <Pair <String, Integer>> prefixNumberModifierPatterns,
            int matchingPatternId) {

        final var stringBeforeExpression = new StringBuilder();
        this.extractBeforeString(text, expPositionStart, stringBeforeExpression);
        this.suffixSearch(stringBeforeExpression, prefixNumberModifierPatterns, matchingPatternId);
    }


    @Override
    public void replaceNumbersInText(StringBuilder uText, List <NNumber> numbers, StringBuilder uTextReplaced) {

        uTextReplaced = new StringBuilder(uText);
        for (final NNumber number : numbers) {
            uTextReplaced = uTextReplaced.replace(number.positionStart, number.positionEnd, String.valueOf(NormalizerUtility.PLACE_HOLDER));
        }
    }


    @Override
    public void shortenPlaceHolderInText(StringBuilder ustr, StringBuilder textShortened) {

        // 「****年*月」 -> 「*年*月」のように数の部分を縮約する（uxのprefixSearchで一致させるため）
        textShortened = new StringBuilder();
        var prevIsPlaceHolder = false;
        for (var i = 0; i < ustr.length(); i++) {
            final var chr = ustr.charAt(i);
            if (chr == NormalizerUtility.PLACE_HOLDER) {
                if (!prevIsPlaceHolder) {
                    textShortened.append(NormalizerUtility.PLACE_HOLDER);
                    prevIsPlaceHolder = true;
                }
            } else {
                textShortened.append(chr);
                prevIsPlaceHolder = false;
            }
        }
    }


    @Override
    public boolean isPlaceHolder(char uc) {

        return NormalizerUtility.PLACE_HOLDER == uc;
    }


    @Override
    public boolean isFinite(double value) {

        return value != Symbol.INFINITY && value != -Symbol.INFINITY;
    }


    @Override
    public boolean isNullTime(NTime t) {

        final var positive_inf = new NTime(Symbol.INFINITY);
        final var negative_inf = new NTime(-Symbol.INFINITY);
        return (positive_inf.equalsTo(t)) || (negative_inf.equalsTo(t));

    }


    @Override
    public String identifyTimeDetail(NTime time) {

        if (this.isFinite(time.second)) {
            return "s";
        } else if (this.isFinite(time.minute)) {
            return "mn";
        } else if (this.isFinite(time.hour)) {
            return "h";
        } else if (this.isFinite(time.day)) {
            return "d";
        } else if (this.isFinite(time.month)) {
            return "m";
        } else if (this.isFinite(time.year)) {
            return "y";
        }
        return "";
    }

}
