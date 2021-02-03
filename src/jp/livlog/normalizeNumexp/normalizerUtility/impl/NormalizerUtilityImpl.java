package jp.livlog.normalizeNumexp.normalizerUtility.impl;

import java.util.List;

import jp.livlog.normalizeNumexp.normalizerUtility.NormalizerUtility;
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
    public void extractAfterString(StringBuilder text, int i, String afterString) {

        afterString = text.substring(i);
    }


    @Override
    public void extractBeforeString(StringBuilder text, int i, String beforeString) {

        beforeString = text.substring(0, i);
    }


    @Override
    public void prefixSearch(String ustr, List <Pair <String, Integer>> patterns, int matchingPatternId) {

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
    public void suffixSearch(String ustr, List <Pair <String, Integer>> patternsRev, int matchingPatternId) {

        /*
         * patternsの中から、ustrのsuffixになっているものを探索（複数ある場合は最長のもの）
         * あらかじめpatternsの文字列を逆にしたものを保管しておき（patterns_rev）、ustrも逆にしてしまい、その状態でprefixSearchを行った結果を返す
         */
        final var ustrShortened = new StringBuilder();
        this.shortenPlaceHolderInText(ustr, ustrShortened); // ustrは数字が一字一字、「*」に変換されているので、patternsの表記と食い違っている。*を縮約する操作を行う
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

        final String stringAfterExpression = null;
        this.extractAfterString(text, expPositionEnd, stringAfterExpression);
        this.prefixSearch(stringAfterExpression, suffixNumberModifierPatterns, matchingPatternId);
    }


    @Override
    public void searchPrefixNumberModifier(StringBuilder text, int expPositionStart, List <Pair <String, Integer>> prefixNumberModifierPatterns,
            int matchingPatternId) {

        final String stringBeforeExpression = null;
        this.extractBeforeString(text, expPositionStart, stringBeforeExpression);
        this.suffixSearch(stringBeforeExpression, prefixNumberModifierPatterns, matchingPatternId);
    }


    @Override
    public void replaceNumbersInText(String uText, List <jp.livlog.normalizeNumexp.share.Number> numbers, StringBuilder uTextReplaced) {

        uTextReplaced = new StringBuilder(uText);
        for (final jp.livlog.normalizeNumexp.share.Number number : numbers) {
            uTextReplaced = uTextReplaced.replace(number.positionStart, number.positionEnd, String.valueOf(NormalizerUtility.PLACE_HOLDER));
            uTextReplaced.replace(NormalizerUtility.PLACE_HOLDER, NormalizerUtility.PLACE_HOLDER, uText);
        }
    }


    @Override
    public void shortenPlaceHolderInText(String text, StringBuilder textShortened) {

        // 「****年*月」 -> 「*年*月」のように数の部分を縮約する（uxのprefixSearchで一致させるため）
        textShortened = new StringBuilder();
        var prevIsPlaceHolder = false;
        for (var i = 0; i < text.length(); i++) {
            final var chr = text.charAt(i);
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
    public boolean isNullTime(double t) {

        final var positive_inf = Symbol.INFINITY;
        final var negative_inf = -Symbol.INFINITY;
        return (positive_inf == t) || (negative_inf == t);

    }


    @Override
    public String identifyTimeDetail(jp.livlog.normalizeNumexp.share.Time time) {

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
