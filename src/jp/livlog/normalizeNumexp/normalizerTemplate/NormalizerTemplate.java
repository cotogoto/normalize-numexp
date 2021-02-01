package jp.livlog.normalizeNumexp.normalizerTemplate;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import jp.livlog.normalizeNumexp.dictionaryDirpath.DictionaryDirpath;
import jp.livlog.normalizeNumexp.digitUtility.DigitUtility;
import jp.livlog.normalizeNumexp.digitUtility.impl.DigitUtilityImpl;
import jp.livlog.normalizeNumexp.normalizerUtility.NormalizerUtility;
import jp.livlog.normalizeNumexp.normalizerUtility.NormalizerUtility.LimitedExpressionTemplate;
import jp.livlog.normalizeNumexp.normalizerUtility.NormalizerUtility.NormalizedExpressionTemplate;
import jp.livlog.normalizeNumexp.normalizerUtility.NormalizerUtility.NumberModifier;
import jp.livlog.normalizeNumexp.normalizerUtility.impl.NormalizerUtilityImpl;
import jp.livlog.normalizeNumexp.share.BaseExpressionTemplate;
import jp.livlog.normalizeNumexp.share.Pair;
import jp.livlog.normalizeNumexp.share.RefObject;

public abstract class NormalizerTemplate <AnyTypeExpression extends NormalizedExpressionTemplate, AnyTypeLimitedExpression extends LimitedExpressionTemplate> {

    protected NormalizerUtility normalizerUtility = new NormalizerUtilityImpl();

    public abstract void init();


    public abstract void normalizeNumber(final String text, List <DigitUtility.Number> numbers);


    public abstract void reviseAnyTypeExpressionByMatchingLimitedExpression(List <AnyTypeExpression> anyTypeExpressions,
            RefObject <Integer> expressionId, AnyTypeLimitedExpression matchingLimitedExpression);


    public abstract void reviseAnyTypeExpressionByMatchingPrefixCounter(AnyTypeExpression anyTypeExpressions,
            final AnyTypeLimitedExpression matchingLimitedExpression);


    public abstract void reviseAnyTypeExpressionByNumberModifier(AnyTypeExpression anyTypeExpressions,
            final NormalizerUtility.NumberModifier numberModifier);


    public abstract void deleteNotAnyTypeExpression(List <AnyTypeExpression> anyTypeExpressions);


    public abstract void fixByRangeExpression(final String uText, List <AnyTypeExpression> anyTypeExpressions);


    public final void buildLimitedExpressionPatternsFromLimitedExpressions() {

        // limited_expressionのpatternでprefixSearchするために、patternをキーとするトライ木を生成する。
        final var limitedExpressionPatternTable = new ArrayList <Pair <String, Integer>>();
        for (var i = 0; i < this.limitedExpressions.size(); i++) {
            limitedExpressionPatternTable.add(new Pair <>(this.limitedExpressions.get(i).pattern, i));
        }
        this.limitedExpressionPatterns.addAll(limitedExpressionPatternTable);
    }


    public <T extends BaseExpressionTemplate> void loadJsonFromFile(final String filepath, List <T> list) {

        final Reader reader = new InputStreamReader(
                DigitUtilityImpl.class.getResourceAsStream(filepath));

        final var gson = new Gson();
        final var collectionType = new TypeToken <Collection <T>>() {
        }.getType();
        list = gson.fromJson(reader, collectionType);
    }


    public <T extends BaseExpressionTemplate> void loadFromDictionary(final String dictionaryPath, List <T> loadTarget) {

        loadTarget.clear();

        this.loadJsonFromFile(dictionaryPath, loadTarget);
    }


    public <T extends BaseExpressionTemplate> void buildPatternsRev(final List <T> originals, List <Pair <String, Integer>> patterns) {

        // prefixSearchをつかってsuffixSearchを実現するため、uxに格納するパターンを予め前後逆にしておく
        final var kvs = new ArrayList <Pair <String, Integer>>();

        for (var i = 0; i < originals.size(); i++) {
            final var patternRev = this.normalizerUtility.reverseString(originals.get(i).pattern);
            kvs.add(new Pair <>(patternRev, i));
        }
        patterns.addAll(kvs);
    }


    public <T extends BaseExpressionTemplate> void buildPatterns(final List <T> originals, List <Pair <String, Integer>> patterns) {

        final var kvs = new ArrayList <Pair <String, Integer>>();
        for (var i = 0; i < originals.size(); i++) {
            kvs.add(new Pair <>(originals.get(i).pattern, i));
        }
        patterns.addAll(kvs);
    }


    public void load_from_dictionaries(final String limited_expression_dictionary, final String prefix_counter_dictionary,
            final String prefix_number_modifier_dictionary, final String suffix_number_modifier_dictionary) {

        var dictionaryPath = DictionaryDirpath.DICTIONARY_DIRPATH;
        dictionaryPath += this.language;
        dictionaryPath += "/";
        this.loadFromDictionary(dictionaryPath + limited_expression_dictionary, this.limitedExpressions);
        this.loadFromDictionary(dictionaryPath + prefix_counter_dictionary, this.prefixCounters);
        this.loadFromDictionary(dictionaryPath + suffix_number_modifier_dictionary, this.suffixNumberModifier);
        this.loadFromDictionary(dictionaryPath + prefix_number_modifier_dictionary, this.prefixNumberModifier);

        this.buildPatterns(this.limitedExpressions, this.limitedExpressionPatterns);
        this.buildPatternsRev(this.prefixCounters, this.prefixCounterPatterns);
        this.buildPatterns(this.suffixNumberModifier, this.suffixNumberModifierPatterns);
        this.buildPatternsRev(this.prefixNumberModifier, this.prefixNumberModifierPatterns);

        for (var i = 0; i < this.limitedExpressions.size(); i++) {
            if (this.limitedExpressions.get(i) instanceof LimitedExpressionTemplate) {
                final var limitedExpression = (LimitedExpressionTemplate) this.limitedExpressions.get(i);
                limitedExpression.setTotalNumberOfPlaceHolder();
                limitedExpression.setLengthOfStringsAfterFinalPlaceHolder();

            }
        }
    }


    public void searchMatchingLimitedExpression(final StringBuilder uTextReplaced, final AnyTypeExpression anyTypeExpression,
            RefObject <Integer> matchingPatternId) {

        final String stringAfterExpression = null;

        this.normalizerUtility.extractAfterString(uTextReplaced, anyTypeExpression.positionEnd, stringAfterExpression);
        this.normalizerUtility.prefixSearch(stringAfterExpression, this.limitedExpressionPatterns, matchingPatternId.argValue);
    }


    public void searchMatchingPrefixCounter(final StringBuilder uTextReplaced, final AnyTypeExpression anyTypeExpression,
            RefObject <Integer> matchingPatternId) {

        final String stringBeforeExpression = null;
        this.normalizerUtility.extractBeforeString(uTextReplaced, anyTypeExpression.positionStart, stringBeforeExpression);
        this.normalizerUtility.suffixSearch(stringBeforeExpression, this.prefixCounterPatterns, matchingPatternId.argValue);
    }


    public void reviseAnyTypeExpressionByMatchingPrefixNumberModifier(AnyTypeExpression anyTypeExpression,
            final NumberModifier numberModifier) {

        anyTypeExpression.positionStart -= numberModifier.pattern.length();
        this.reviseAnyTypeExpressionByNumberModifier(anyTypeExpression, numberModifier);
    }


    public void reviseAnyTypeExpressionByMatchingSuffixNumberModifier(AnyTypeExpression anyTypeExpression,
            final NumberModifier numberModifier) {

        anyTypeExpression.positionEnd += numberModifier.pattern.length();
        this.reviseAnyTypeExpressionByNumberModifier(anyTypeExpression, numberModifier);
    }


    public boolean normalizeLimitedExpression(final StringBuilder uTextReplaced, List <AnyTypeExpression> anyTypeExpressions,
            RefObject <Integer> i) {

        var matchingPatternId = 0;
        final var temprefMatchingPatternId = new RefObject <>(matchingPatternId);
        this.searchMatchingLimitedExpression(uTextReplaced, anyTypeExpressions.get(i.argValue), temprefMatchingPatternId);
        matchingPatternId = temprefMatchingPatternId.argValue;
        if (matchingPatternId == -1) {
            return false;
        }
        this.reviseAnyTypeExpressionByMatchingLimitedExpression(anyTypeExpressions, i, this.limitedExpressions.get(matchingPatternId));
        return true;
    }


    public void normalizePrefixCounter(final StringBuilder uTextReplaced, AnyTypeExpression anyTypeExpression) {

        var matchingPatternId = 0;
        final var tempRef_matchingPatternId = new RefObject <>(matchingPatternId);
        this.searchMatchingPrefixCounter(uTextReplaced, anyTypeExpression, tempRef_matchingPatternId);
        matchingPatternId = tempRef_matchingPatternId.argValue;
        if (matchingPatternId == -1) {
            return;
        }
        this.reviseAnyTypeExpressionByMatchingPrefixCounter(anyTypeExpression, this.prefixCounters.get(matchingPatternId));
    }


    @SuppressWarnings ("unused")
    public boolean normalizeSuffixNumberModifier(final StringBuilder uTextReplaced, AnyTypeExpression anyTypeExpression) {

        final var matchingPatternId = 0;
        this.normalizerUtility.searchSuffixNumberModifier(uTextReplaced, anyTypeExpression.positionEnd, this.suffixNumberModifierPatterns,
                matchingPatternId);
        if (matchingPatternId == -1) {
            return false;
        }
        this.reviseAnyTypeExpressionByMatchingSuffixNumberModifier(anyTypeExpression, this.suffixNumberModifier.get(matchingPatternId));
        return true;
    }


    @SuppressWarnings ("unused")
    public boolean normalize_prefix_number_modifier(final StringBuilder uTextReplaced, AnyTypeExpression anyTypeExpression) {

        final var matchingPatternId = 0;
        this.normalizerUtility.searchPrefixNumberModifier(uTextReplaced, anyTypeExpression.positionStart, this.prefixNumberModifierPatterns,
                matchingPatternId);
        if (matchingPatternId == -1) {
            return false;
        }
        this.reviseAnyTypeExpressionByMatchingPrefixNumberModifier(anyTypeExpression, this.prefixNumberModifier.get(matchingPatternId));
        return true;
    }


    @SuppressWarnings ("unchecked")
    public void convertNumbersToAnyTypeExpressions(final List <DigitUtility.Number> numbers,
            List <AnyTypeExpression> anyTypeExpressions) {

        for (final DigitUtility.Number number : numbers) {

            final var baseExpressionTemplate = new BaseExpressionTemplate();
            baseExpressionTemplate.originalExpression = number.originalExpression;
            baseExpressionTemplate.positionStart = number.positionStart;
            baseExpressionTemplate.positionEnd = number.positionEnd;
            anyTypeExpressions.add((AnyTypeExpression) baseExpressionTemplate);
        }
    }


    public boolean haveKaraPrefix(final List <String> options) {

        // return find(options.iterator(), options.end(), "kara_prefix") != options.end();
        return options.contains("kara_prefix");
    }


    public boolean haveKaraSuffix(final List <String> options) {

        // return find(options.iterator(), options.end(), "kara_suffix") != options.end();
        return options.contains("kara_suffix");
    }


    public void mergeOptions(List <String> options1, List <String> options2) {

        // 範囲表現の統合の際に使われる。kara_suffix, kara_prefixはここで削除する
        // TODO : 削除するというのが非常に分かり辛い。どうにかする。
        for (var i = 0; i < options1.size(); i++) {
            if (options1.get(i).equals("kara_suffix")) {
                options1.remove(i);
                break;
            }
        }
        for (var i = 0; i < options2.size(); i++) {
            if (options2.get(i).equals("kara_prefix")) {
                continue;
            }
            options1.add(options2.get(i));
        }
    }


    public void process(final String text, List <AnyTypeExpression> anyTypeExpressions) {

        anyTypeExpressions.clear();
        final var uText = text;

        // numbersの作成
        final List <DigitUtility.Number> numbers = new ArrayList <>();
        this.normalizeNumber(text, numbers);

        // numbersを変換して、ベースとなるany_type_expressionsを作成
        this.convertNumbersToAnyTypeExpressions(numbers, anyTypeExpressions);

        // searchするために、text中の数を*に置換しておく
        final var uTextReplaced = new StringBuilder();
        this.normalizerUtility.replaceNumbersInText(uText, numbers, uTextReplaced);

        // 単位の探索、規格化
        for (var i = 0; i < anyTypeExpressions.size(); i++) {
            if (!this.normalizeLimitedExpression(uTextReplaced, anyTypeExpressions, new RefObject <>(i))) {
                // TODO : 単位が存在しなかった場合の処理をどうするか、相談して決める
            }
            this.normalizePrefixCounter(uTextReplaced, anyTypeExpressions.get(i));
            if (this.normalizeSuffixNumberModifier(uTextReplaced, anyTypeExpressions.get(i))) {
                this.normalizeSuffixNumberModifier(uTextReplaced, anyTypeExpressions.get(i)); // TODO : 2回以上の繰り返しを本当に含めて良いのか？
            }
            if (this.normalize_prefix_number_modifier(uTextReplaced, anyTypeExpressions.get(i))) {
                this.normalizePrefixCounter(uTextReplaced, anyTypeExpressions.get(i));
            }
            anyTypeExpressions.get(i).setOriginalExpressionFromPosition(uText);
        }

        // TODO : 範囲表現の処理
        this.fixByRangeExpression(uText, anyTypeExpressions);

        // 規格化されなかったnumberを削除
        this.deleteNotAnyTypeExpression(anyTypeExpressions);
    }

    public List <Pair <String, Integer>>           limitedExpressionPatterns    = new ArrayList <>();

    public List <Pair <String, Integer>>           prefixCounterPatterns        = new ArrayList <>();

    public List <Pair <String, Integer>>           suffixNumberModifierPatterns = new ArrayList <>();

    public List <Pair <String, Integer>>           prefixNumberModifierPatterns = new ArrayList <>();

    public List <AnyTypeLimitedExpression>         limitedExpressions           = new ArrayList <>();

    public List <AnyTypeLimitedExpression>         prefixCounters               = new ArrayList <>();

    public List <NormalizerUtility.NumberModifier> suffixNumberModifier         = new ArrayList <>();

    public List <NormalizerUtility.NumberModifier> prefixNumberModifier         = new ArrayList <>();

    public String                                  language;

}
