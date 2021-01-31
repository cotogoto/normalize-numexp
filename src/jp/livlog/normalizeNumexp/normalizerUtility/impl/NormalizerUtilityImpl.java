package jp.livlog.normalizeNumexp.normalizerUtility.impl;

import java.util.List;

import jp.livlog.normalizeNumexp.normalizerUtility.NormalizerUtility;

public class NormalizerUtilityImpl extends NormalizerUtility {

    public class NormalizedExpressionTemplateImpl extends NormalizedExpressionTemplate {

        public NormalizedExpressionTemplateImpl(String originalExpression, int positionStart, int positionEnd) {

            super(originalExpression, positionStart, positionEnd);
        }


        public void setOriginalExpressionFromPosition(String text) {

            this.originalExpression = text.substring(this.positionStart, this.positionEnd);
        }
    }

    @Override
    public String reverseString(String str) {

        final var sb = new StringBuilder(str);
        str = sb.reverse().toString();

        return str;
    }


    @Override
    public void extractAfterString(String text, int i, String afterString) {

        afterString = text.substring(i);
    }


    @Override
    public void extractBeforeString(String text, int i, String beforeString) {

        beforeString = text.substring(0, i);
    }


    @Override
    public void prefixSearch(String ustr, List <Pair <String>> patterns, int matchingPatternId) {

        /*
         * patternsの中から、ustrのprefixになっているものを探索（複数ある場合は最長のもの）
         */
        final var ustrShortened = new StringBuilder();
        this.shortenPlaceHolderInText(ustr, ustrShortened); // ustrは数字が一字一字、「*」に変換されているので、patternsの表記と食い違っている。*を縮約する操作を行う
        final var str = ustrShortened.toString();

        for (final Pair <String> pair : patterns) {
            if (pair.first.contains(str)) {
                matchingPatternId = pair.second;
                return;
            }
        }

        matchingPatternId = -1;
    }


    @Override
    public void suffixSearch(String ustr, List <Pair <String>> patternsRev, int matchingPatternId) {

        /*
         * patternsの中から、ustrのsuffixになっているものを探索（複数ある場合は最長のもの）
         * あらかじめpatternsの文字列を逆にしたものを保管しておき（patterns_rev）、ustrも逆にしてしまい、その状態でprefixSearchを行った結果を返す
         */
        final var ustrShortened = new StringBuilder();
        this.shortenPlaceHolderInText(ustr, ustrShortened); // ustrは数字が一字一字、「*」に変換されているので、patternsの表記と食い違っている。*を縮約する操作を行う
        final var str = ustrShortened.toString();

        for (final Pair <String> pair : patternsRev) {
            if (pair.first.contains(str)) {
                matchingPatternId = pair.second;
                return;
            }
        }

        matchingPatternId = -1;
    }


    @Override
    public void searchSuffixNumberModifier(String text, int expPositionEnd, List <Pair <String>> suffixNumberModifierPatterns,
            int matchingPatternId) {

        final String stringAfterExpression = null;
        this.extractAfterString(text, expPositionEnd, stringAfterExpression);
        this.prefixSearch(stringAfterExpression, suffixNumberModifierPatterns, matchingPatternId);
    }


    @Override
    public void searchPrefixNumberModifier(String text, int expPositionStart, List <Pair <String>> prefixNumberModifierPatterns,
            int matchingPatternId) {

        final String stringBeforeExpression = null;
        this.extractBeforeString(text, expPositionStart, stringBeforeExpression);
        this.suffixSearch(stringBeforeExpression, prefixNumberModifierPatterns, matchingPatternId);
    }


    @Override
    public void replaceNumbersInText(String utext, List <Integer> numbers, String utextReplaced) {

        utextReplaced = utext;
        for (final Integer number : numbers) {
            utextReplaced = utextReplaced.replace(number.toString(), String.valueOf(this.PLACE_HOLDER));
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
    public boolean isPlaceHolder(String uc) {

        // TODO 自動生成されたメソッド・スタブ
        return false;
    }


    @Override
    public boolean isFinite(double value) {

        // TODO 自動生成されたメソッド・スタブ
        return false;
    }


    @Override
    public boolean isNullTime(Time time) {

        // TODO 自動生成されたメソッド・スタブ
        return false;
    }


    @Override
    public String identifyTimeDetail(Time time) {

        // TODO 自動生成されたメソッド・スタブ
        return null;
    }

}
