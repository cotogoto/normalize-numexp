package jp.livlog.normalizeNumexp.normalizerUtility.impl;

import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;

import jp.livlog.normalizeNumexp.normalizerUtility.NormalizerUtility;
import jp.livlog.normalizeNumexp.share.NNumber;
import jp.livlog.normalizeNumexp.share.NTime;
import jp.livlog.normalizeNumexp.share.Pair;
import jp.livlog.normalizeNumexp.share.PairKey0Comp;
import jp.livlog.normalizeNumexp.share.RefObject;
import jp.livlog.normalizeNumexp.share.Symbol;

public class NormalizerUtilityImpl extends NormalizerUtility {

    @Override
    public String reverseString(String str) {

        if (str != null) {
            final var sb = new StringBuilder(str);
            str = sb.reverse().toString();
        }

        return str;
    }


    @Override
    public void extractAfterString(StringBuilder text, int i, StringBuilder afterString) {

        if (text.length() < i) {
            return;
        }

        final var value = text.substring(i);
        afterString.append(value);
    }


    @Override
    public void extractBeforeString(StringBuilder text, int i, StringBuilder beforeString) {

        if (i > -1) {
            return;
        }

        final var value = text.substring(0, i);
        beforeString.append(value);
    }


    @Override
    public void prefixSearch(StringBuilder ustr, NavigableSet <Pair <String, Integer>> patterns, RefObject <Integer> matchingPatternId) {

        /*
         * patternsの中から、ustrのprefixになっているものを探索（複数ある場合は最長のもの）
         */
        final var ustrShortened = new StringBuilder();
        this.shortenPlaceHolderInText(ustr, ustrShortened); // ustrは数字が一字一字、「*」に変換されているので、patternsの表記と食い違っている。*を縮約する操作を行う
        final var str = ustrShortened.toString();

        final NavigableSet <Pair <String, Integer>> temp = new TreeSet <>(new PairKey0Comp <String, Integer>());
        for (final Pair <String, Integer> pair : patterns) {
            if (str.startsWith(pair.first)) {
                temp.add(pair);
            }
        }

        if (!temp.isEmpty()) {
            var length = 0;
            Pair <String, Integer> fix = null;
            for (final Pair <String, Integer> pair : temp) {
                if (pair.first.length() > length) {
                    fix = pair;
                    length = pair.first.length();
                }
            }
            if (fix != null) {
                matchingPatternId.argValue = fix.second;
                return;
            }
        }

        matchingPatternId.argValue = -1;
    }


    @Override
    public void suffixSearch(StringBuilder stringBeforeExpression, NavigableSet <Pair <String, Integer>> patternsRev,
            RefObject <Integer> matchingPatternId) {

        /*
         * patternsの中から、ustrのsuffixになっているものを探索（複数ある場合は最長のもの）
         * あらかじめpatternsの文字列を逆にしたものを保管しておき（patterns_rev）、ustrも逆にしてしまい、その状態でprefixSearchを行った結果を返す
         */
        final var ustrShortened = new StringBuilder();
        this.shortenPlaceHolderInText(stringBeforeExpression, ustrShortened); // ustrは数字が一字一字、「*」に変換されているので、patternsの表記と食い違っている。*を縮約する操作を行う
        final var strRev = this.reverseString(ustrShortened.toString());

        final NavigableSet <Pair <String, Integer>> temp = new TreeSet <>(new PairKey0Comp <String, Integer>());
        for (final Pair <String, Integer> pair : patternsRev) {
            if (strRev.startsWith(pair.first)) {
                temp.add(pair);
            }
        }

        if (!temp.isEmpty()) {
            var length = 0;
            Pair <String, Integer> fix = null;
            for (final Pair <String, Integer> pair : temp) {
                if (pair.first.length() > length) {
                    fix = pair;
                    length = pair.first.length();
                }
            }
            if (fix != null) {
                matchingPatternId.argValue = fix.second;
                return;
            }
        }

        matchingPatternId.argValue = -1;
    }


    @Override
    public void searchSuffixNumberModifier(StringBuilder text, int expPositionEnd, NavigableSet <Pair <String, Integer>> suffixNumberModifierPatterns,
            RefObject <Integer> matchingPatternId) {

        final var stringAfterExpression = new StringBuilder();
        this.extractAfterString(text, expPositionEnd, stringAfterExpression);
        this.prefixSearch(stringAfterExpression, suffixNumberModifierPatterns, matchingPatternId);
    }


    @Override
    public void searchPrefixNumberModifier(StringBuilder text, int expPositionStart,
            NavigableSet <Pair <String, Integer>> prefixNumberModifierPatterns,
            RefObject <Integer> matchingPatternId) {

        final var stringBeforeExpression = new StringBuilder();
        this.extractBeforeString(text, expPositionStart, stringBeforeExpression);
        this.suffixSearch(stringBeforeExpression, prefixNumberModifierPatterns, matchingPatternId);
    }


    @Override
    public void replaceNumbersInText(StringBuilder uText, List <NNumber> numbers, StringBuilder uTextReplaced) {

        var temp = new StringBuilder(uText);
        for (final NNumber number : numbers) {
            temp = temp.replace(
                    number.positionStart,
                    number.positionEnd,
                    this.convPlaceHolder(NormalizerUtility.PLACE_HOLDER, number.positionEnd - number.positionStart));
        }
        uTextReplaced.append(temp);
    }


    private String convPlaceHolder(char chr, int cnt) {

        final var sb = new StringBuilder();
        for (var i = 0; i < cnt; i++) {
            sb.append(chr);
        }

        return sb.toString();
    }


    @Override
    public void shortenPlaceHolderInText(StringBuilder ustr, StringBuilder textShortened) {

        // 「****年*月」 -> 「*年*月」のように数の部分を縮約する（uxのprefixSearchで一致させるため）
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
