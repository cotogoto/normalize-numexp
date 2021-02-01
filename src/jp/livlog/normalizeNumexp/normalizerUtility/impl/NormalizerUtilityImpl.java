package jp.livlog.normalizeNumexp.normalizerUtility.impl;

import java.util.List;

import jp.livlog.normalizeNumexp.digitUtility.DigitUtility;
import jp.livlog.normalizeNumexp.normalizerUtility.NormalizerUtility;
import jp.livlog.normalizeNumexp.share.Pair;
import jp.livlog.normalizeNumexp.share.Symbol;

public class NormalizerUtilityImpl extends NormalizerUtility {

    public class NormalizedExpressionTemplateImpl extends NormalizedExpressionTemplate {

        public NormalizedExpressionTemplateImpl(String originalExpression, int positionStart, int positionEnd) {

            super(originalExpression, positionStart, positionEnd);
        }


        @Override
        public void setOriginalExpressionFromPosition(String text) {

            this.originalExpression = text.substring(this.positionStart, this.positionEnd);
        }
    }

    public class LimitedExpressionTemplateImpl extends LimitedExpressionTemplate {

        @Override
        public void setTotalNumberOfPlaceHolder() {

            // patternが含むPLACE_HOLDERの数（ *月*日 -> 2個）
            this.totalNumberOfPlaceHolder = 0;

            for (final char c1 : this.pattern.toCharArray()) {
                if (NormalizerUtilityImpl.this.isPlaceHolder(c1)) {
                    this.totalNumberOfPlaceHolder++;
                }
            }
        }


        @Override
        public void setLengthOfStringsAfterFinalPlaceHolder() {

            // pattern中の最後のPLACE_HOLDERの後に続く文字列の長さ（*月*日 -> 1） positionの同定に必要
            this.lengthOfStringsAfterFinalPlaceHolder = 0;

            final var a = this.pattern.lastIndexOf(String.valueOf(NormalizerUtilityImpl.this.PLACE_HOLDER));
            final var str = this.pattern.substring(a);
            this.lengthOfStringsAfterFinalPlaceHolder = str.length();
        }
    }

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
    public void replaceNumbersInText(String uText, List <DigitUtility.Number> numbers, StringBuilder uTextReplaced) {

        uTextReplaced = new StringBuilder(uText);
        for (final DigitUtility.Number number : numbers) {
            uTextReplaced = uTextReplaced.replace(number.positionStart, number.positionEnd, String.valueOf(this.PLACE_HOLDER));
            uTextReplaced.replace(this.PLACE_HOLDER, this.PLACE_HOLDER, uText);
        }
    }


    @Override
    public void shortenPlaceHolderInText(String text, StringBuilder textShortened) {

        // 「****年*月」 -> 「*年*月」のように数の部分を縮約する（uxのprefixSearchで一致させるため）
        textShortened = new StringBuilder();
        var prevIsPlaceHolder = false;
        for (var i = 0; i < text.length(); i++) {
            final var chr = text.charAt(i);
            if (chr == this.PLACE_HOLDER) {
                if (!prevIsPlaceHolder) {
                    textShortened.append(this.PLACE_HOLDER);
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

        return this.PLACE_HOLDER == uc;
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
    public String identifyTimeDetail(Time time) {

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
