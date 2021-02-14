package jp.livlog.normalizeNumexp.normalizerTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import jp.livlog.normalizeNumexp.dictionaryDirpath.DictionaryDirpath;
import jp.livlog.normalizeNumexp.digitUtility.impl.DigitUtilityImpl;
import jp.livlog.normalizeNumexp.normalizerUtility.LimitedExpressionTemplate;
import jp.livlog.normalizeNumexp.normalizerUtility.NormalizedExpressionTemplate;
import jp.livlog.normalizeNumexp.normalizerUtility.NormalizerUtility;
import jp.livlog.normalizeNumexp.normalizerUtility.impl.NormalizerUtilityImpl;
import jp.livlog.normalizeNumexp.share.BaseExpressionTemplate;
import jp.livlog.normalizeNumexp.share.NNumber;
import jp.livlog.normalizeNumexp.share.NumberModifier;
import jp.livlog.normalizeNumexp.share.Pair;
import jp.livlog.normalizeNumexp.share.PairKey0Comp;
import jp.livlog.normalizeNumexp.share.RefObject;
import lombok.extern.slf4j.Slf4j;

/*
 * 数量表現（「三人」「約1000円」などといった表現）や時間表現（「1989年3月」「3:30」「百年後」などといった表現）は以下のように構成される
 *
 * 【接頭辞 + 前置助数詞 + 数量表現or時間表現の基本パターン + 接尾辞】
 * 　・接頭辞：「約」「およそ」など
 * 　・前置助数詞：数量表現における「時速」「￥」や、絶対時間表現における年号など（本来はパターンに含めたいところだが、基本パターンをprefixSearchで探索している都合上、今回は別の構成要素として考える）
 * 　・基本パターン：「*人」「*円」「*年*月」「*:*」などの正規表現パターン。
 * 　・接尾辞：「以降」「くらい」など
 *
 * この構成性に着目し、この規格化モジュールでは、文中の数の周囲を正規表現でマッチングさせ、表現を認識させる。
 * （「数」 -> 「数」＋「助数詞」 -> 「前置助数詞」＋「数」＋「助数詞」 -> 「前置助数詞」＋「数」＋「助数詞」＋「接尾辞」 -> 「接頭辞」＋「前置助数詞」＋「数」＋「助数詞」　と認識範囲を増やしていく）
 * 認識した際には、認識したパターンに対応する処理を、辞書を参照して実行し、規格化表現を作成していく。
 *
 * この基底クラスでは、上のようにパターンを順番に認識していく処理を書いている。
 * 派生クラスとなるnumerical_expression_normalizer, abstime_expression_normalizer, reltime_expression_normalizer, duration_expression_normalizerでは、認識したパターンに対応する処理を書く。
 *
 */
@Slf4j
public abstract class NormalizerTemplate <AnyTypeExpression extends NormalizedExpressionTemplate, AnyTypeLimitedExpression extends LimitedExpressionTemplate> {

    protected NormalizerUtility normalizerUtility = new NormalizerUtilityImpl();

    public abstract void init();


    public abstract void normalizeNumber(StringBuilder uText, List <NNumber> numbers);


    public abstract void reviseAnyTypeExpressionByMatchingLimitedExpression(List <AnyTypeExpression> anyTypeExpressions,
            RefObject <Integer> expressionId, AnyTypeLimitedExpression matchingLimitedExpression);


    public abstract void reviseAnyTypeExpressionByMatchingPrefixCounter(AnyTypeExpression anyTypeExpressions,
            AnyTypeLimitedExpression matchingLimitedExpression);


    public abstract void reviseAnyTypeExpressionByNumberModifier(AnyTypeExpression anyTypeExpressions,
            NumberModifier numberModifier);


    public abstract void deleteNotAnyTypeExpression(List <AnyTypeExpression> anyTypeExpressions);


    public abstract void fixByRangeExpression(StringBuilder uText, List <AnyTypeExpression> anyTypeExpressions);


    public void process(String text, List <AnyTypeExpression> anyTypeExpressions) {

        anyTypeExpressions.clear();
        final var uText = new StringBuilder(text);

        // numbersの作成
        final List <NNumber> numbers = new ArrayList <>();
        this.normalizeNumber(uText, numbers);

        // numbersを変換して、ベースとなるany_type_expressionsを作成
        this.convertNumbersToAnyTypeExpressions(numbers, anyTypeExpressions);

        // searchするために、text中の数を*に置換しておく
        final var uTextReplaced = new StringBuilder();
        this.normalizerUtility.replaceNumbersInText(uText, numbers, uTextReplaced);

        // 単位の探索、規格化
        for (var i = 0; i < anyTypeExpressions.size(); i++) {
            final var no = new RefObject <>(i);
            if (!this.normalizeLimitedExpression(uTextReplaced, anyTypeExpressions, no)) {
                // TODO : 単位が存在しなかった場合の処理をどうするか、相談して決める
            }
            i = no.argValue;
            this.normalizePrefixCounter(uTextReplaced, anyTypeExpressions.get(i));
            if (this.normalizeSuffixNumberModifier(uTextReplaced, anyTypeExpressions.get(i))) {
                this.normalizeSuffixNumberModifier(uTextReplaced, anyTypeExpressions.get(i)); // TODO : 2回以上の繰り返しを本当に含めて良いのか？
            }
            if (this.normalizePrefixNumberModifier(uTextReplaced, anyTypeExpressions.get(i))) {
                this.normalizePrefixCounter(uTextReplaced, anyTypeExpressions.get(i));
            }
            anyTypeExpressions.get(i).setOriginalExpressionFromPosition(uText);
        }

        // TODO : 範囲表現の処理
        this.fixByRangeExpression(uText, anyTypeExpressions);

        // 規格化されなかったnumberを削除
        this.deleteNotAnyTypeExpression(anyTypeExpressions);
    }


    public void buildLimitedExpressionPatternsFromLimitedExpressions() {

        // limited_expressionのpatternでprefixSearchするために、patternをキーとするトライ木を生成する。
        final var limitedExpressionPatternTable = new ArrayList <Pair <String, Integer>>();
        for (var i = 0; i < this.limitedExpressions.size(); i++) {
            limitedExpressionPatternTable.add(new Pair <>(this.limitedExpressions.get(i).pattern, i));
        }
        this.limitedExpressionPatterns.addAll(limitedExpressionPatternTable);
    }


    public abstract void loadFromDictionary1(String dictionaryPath, List <AnyTypeLimitedExpression> loadTarget);

    // public abstract void loadFromDictionary2(String dictionaryPath, List <NumberModifier> loadTarget);


    public void loadFromDictionary2(String dictionaryPath, List <NumberModifier> loadTarget) {

        loadTarget.clear();

        final var reader = this.fileLoad(dictionaryPath);

        final var gson = new Gson();
        final var listType = new TypeToken <HashMap <String, String>>() {
        }.getType();
        NumberModifier numberModifier = null;
        try (var br = new BufferedReader(reader)) {
            String line;
            while ((line = br.readLine()) != null) {
                @SuppressWarnings ("unchecked")
                final var map = (HashMap <String, String>) gson.fromJson(line, listType);
                numberModifier = new NumberModifier(map.get("pattern"), map.get("process_type"));
                loadTarget.add(numberModifier);
            }
        } catch (final IOException e) {
            NormalizerTemplate.log.error(e.getMessage(), e);
        }
    }


    public <T extends BaseExpressionTemplate> void buildPatternsRev(List <T> originals, NavigableSet <Pair <String, Integer>> patterns) {

        // prefixSearchをつかってsuffixSearchを実現するため、uxに格納するパターンを予め前後逆にしておく
        final var kvs = new ArrayList <Pair <String, Integer>>();

        for (var i = 0; i < originals.size(); i++) {
            final var patternRev = this.normalizerUtility.reverseString(originals.get(i).pattern);
            kvs.add(new Pair <>(patternRev, i));
        }
        patterns.addAll(kvs);
    }


    public <T extends BaseExpressionTemplate> void buildPatterns(List <T> originals, NavigableSet <Pair <String, Integer>> patterns) {

        final var kvs = new ArrayList <Pair <String, Integer>>();
        for (var i = 0; i < originals.size(); i++) {
            kvs.add(new Pair <>(originals.get(i).pattern, i));
        }
        patterns.addAll(kvs);
    }


    public void loadFromDictionaries(
            String limitedExpressionDictionary,
            String prefixCounterDictionary,
            String prefixNumberModifierDictionary,
            String suffixNumberModifierDictionary) {

        final var dictionaryPath = DictionaryDirpath.DICTIONARY_DIRPATH;
        // dictionaryPath += this.language;
        // dictionaryPath += "/";
        this.loadFromDictionary1(dictionaryPath + this.language + "/" + limitedExpressionDictionary, this.limitedExpressions);
        this.loadFromDictionary1(dictionaryPath + this.language + "/" + prefixCounterDictionary, this.prefixCounters);
        this.loadFromDictionary2(dictionaryPath + this.language + "/" + suffixNumberModifierDictionary, this.suffixNumberModifier);
        this.loadFromDictionary2(dictionaryPath + this.language + "/" + prefixNumberModifierDictionary, this.prefixNumberModifier);

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


    public void searchMatchingLimitedExpression(StringBuilder uTextReplaced, AnyTypeExpression anyTypeExpression,
            RefObject <Integer> matchingPatternId) {

        final var stringAfterExpression = new StringBuilder();
        this.normalizerUtility.extractAfterString(uTextReplaced, anyTypeExpression.positionEnd, stringAfterExpression);
        this.normalizerUtility.prefixSearch(stringAfterExpression, this.limitedExpressionPatterns, matchingPatternId);
    }


    public void searchMatchingPrefixCounter(StringBuilder uTextReplaced, AnyTypeExpression anyTypeExpression,
            RefObject <Integer> matchingPatternId) {

        final var stringBeforeExpression = new StringBuilder();
        this.normalizerUtility.extractBeforeString(uTextReplaced, anyTypeExpression.positionStart, stringBeforeExpression);
        this.normalizerUtility.suffixSearch(stringBeforeExpression, this.prefixCounterPatterns, matchingPatternId);
    }


    public void reviseAnyTypeExpressionByMatchingPrefixNumberModifier(AnyTypeExpression anyTypeExpression,
            NumberModifier numberModifier) {

        anyTypeExpression.positionStart -= numberModifier.pattern.length();
        this.reviseAnyTypeExpressionByNumberModifier(anyTypeExpression, numberModifier);
    }


    public void reviseAnyTypeExpressionByMatchingSuffixNumberModifier(AnyTypeExpression anyTypeExpression,
            NumberModifier numberModifier) {

        anyTypeExpression.positionEnd += numberModifier.pattern.length();
        this.reviseAnyTypeExpressionByNumberModifier(anyTypeExpression, numberModifier);
    }


    public boolean normalizeLimitedExpression(StringBuilder uTextReplaced, List <AnyTypeExpression> anyTypeExpressions,
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


    public void normalizePrefixCounter(StringBuilder uTextReplaced, AnyTypeExpression anyTypeExpression) {

        var matchingPatternId = 0;
        final var tempRefMatchingPatternId = new RefObject <>(matchingPatternId);
        this.searchMatchingPrefixCounter(uTextReplaced, anyTypeExpression, tempRefMatchingPatternId);
        matchingPatternId = tempRefMatchingPatternId.argValue;
        if (matchingPatternId == -1) {
            return;
        }
        this.reviseAnyTypeExpressionByMatchingPrefixCounter(anyTypeExpression, this.prefixCounters.get(matchingPatternId));
    }


    public boolean normalizeSuffixNumberModifier(StringBuilder uTextReplaced, AnyTypeExpression anyTypeExpression) {

        var matchingPatternId = 0;
        final var tempRefMatchingPatternId = new RefObject <>(matchingPatternId);
        this.normalizerUtility.searchSuffixNumberModifier(uTextReplaced, anyTypeExpression.positionEnd, this.suffixNumberModifierPatterns,
                tempRefMatchingPatternId);
        matchingPatternId = tempRefMatchingPatternId.argValue;
        if (matchingPatternId == -1) {
            return false;
        }
        this.reviseAnyTypeExpressionByMatchingSuffixNumberModifier(anyTypeExpression, this.suffixNumberModifier.get(matchingPatternId));
        return true;
    }


    public boolean normalizePrefixNumberModifier(StringBuilder uTextReplaced, AnyTypeExpression anyTypeExpression) {

        var matchingPatternId = 0;
        final var tempRefMatchingPatternId = new RefObject <>(matchingPatternId);
        this.normalizerUtility.searchPrefixNumberModifier(uTextReplaced, anyTypeExpression.positionStart, this.prefixNumberModifierPatterns,
                tempRefMatchingPatternId);
        matchingPatternId = tempRefMatchingPatternId.argValue;
        if (matchingPatternId == -1) {
            return false;
        }
        this.reviseAnyTypeExpressionByMatchingPrefixNumberModifier(anyTypeExpression, this.prefixNumberModifier.get(matchingPatternId));
        return true;
    }


    public abstract void convertNumbersToAnyTypeExpressions(List <NNumber> numbers,
            List <AnyTypeExpression> anyTypeExpressions);


    public boolean haveKaraPrefix(List <String> options) {

        // return find(options.iterator(), options.end(), "kara_prefix") != options.end();
        return options.contains("kara_prefix");
    }


    public boolean haveKaraSuffix(List <String> options) {

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


    public Reader fileLoad(String dictionaryPath) {

        try {
            return new InputStreamReader(
                    DigitUtilityImpl.class.getResourceAsStream(dictionaryPath));
        } catch (final Exception e) {
            dictionaryPath = dictionaryPath.replace("/zh/", "/ja/");
            dictionaryPath = dictionaryPath.replace("/en/", "/ja/");
            return new InputStreamReader(
                    DigitUtilityImpl.class.getResourceAsStream(dictionaryPath));
        }
    }

    public NavigableSet <Pair <String, Integer>> limitedExpressionPatterns    = new TreeSet <>(new PairKey0Comp <String, Integer>());

    public NavigableSet <Pair <String, Integer>> prefixCounterPatterns        = new TreeSet <>(new PairKey0Comp <String, Integer>());

    public NavigableSet <Pair <String, Integer>> suffixNumberModifierPatterns = new TreeSet <>(new PairKey0Comp <String, Integer>());

    public NavigableSet <Pair <String, Integer>> prefixNumberModifierPatterns = new TreeSet <>(new PairKey0Comp <String, Integer>());

    public List <AnyTypeLimitedExpression>       limitedExpressions           = new ArrayList <>();

    public List <AnyTypeLimitedExpression>       prefixCounters               = new ArrayList <>();

    public List <NumberModifier>                 suffixNumberModifier         = new ArrayList <>();

    public List <NumberModifier>                 prefixNumberModifier         = new ArrayList <>();

    public String                                language;

}
