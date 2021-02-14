package jp.livlog.normalizeNumexp.inappropriateExpressionRemover.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import jp.livlog.normalizeNumexp.abstimeExpressionNormalizer.AbstimeExpression;
import jp.livlog.normalizeNumexp.dictionaryDirpath.DictionaryDirpath;
import jp.livlog.normalizeNumexp.digitUtility.impl.DigitUtilityImpl;
import jp.livlog.normalizeNumexp.durationExpressionNormalizer.DurationExpression;
import jp.livlog.normalizeNumexp.inappropriateExpressionRemover.InappropriateExpressionRemover;
import jp.livlog.normalizeNumexp.inappropriateExpressionRemover.InappropriateStrings;
import jp.livlog.normalizeNumexp.normalizerUtility.NormalizedExpressionTemplate;
import jp.livlog.normalizeNumexp.numericalExpressionNormalizer.NumericalExpression;
import jp.livlog.normalizeNumexp.reltimeExpressionNormalizer.ReltimeExpression;
import jp.livlog.normalizeNumexp.share.BaseExpressionTemplate;
import jp.livlog.normalizeNumexp.share.NTime;
import jp.livlog.normalizeNumexp.share.RefObject;
import jp.livlog.normalizeNumexp.share.Symbol;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InappropriateExpressionRemoverImpl extends InappropriateExpressionRemover {

    public InappropriateExpressionRemoverImpl(String language) {

        super(language);
        this.initInappropriateStringss(language);
        this.initUrlStrings();
    }


    private <AnyTypeExpression1 extends NormalizedExpressionTemplate, AnyTypeExpression2 extends NormalizedExpressionTemplate> boolean isCoveredByOtherTypeExpression(
            AnyTypeExpression1 anyTypeExpression1, AnyTypeExpression2 anyTypeExpression2) {

        return anyTypeExpression2.positionStart <= anyTypeExpression1.positionStart
                && anyTypeExpression1.positionEnd <= anyTypeExpression2.positionEnd;
    }


    private <AnyTypeExpression1 extends NormalizedExpressionTemplate, AnyTypeExpression2 extends NormalizedExpressionTemplate> boolean isCoveredByOtherTypeExpressions(
            AnyTypeExpression1 anyTypeExpression1, List <AnyTypeExpression2> anyTypeExpressions2) {

        for (var i = 0; i < anyTypeExpressions2.size(); i++) {
            if (this.isCoveredByOtherTypeExpression(anyTypeExpression1, anyTypeExpressions2.get(i))) {
                return true;
            }
        }
        return false;
    }


    private void deleteDuplicateExtraction(
            List <NumericalExpression> numexps,
            List <AbstimeExpression> abstimeexps,
            List <ReltimeExpression> reltimeexps,
            List <DurationExpression> durationexps) {
        // 表現タイプ毎に重複があるので、これを削除する（例：「300年間」はabstimeexpsでも「300年」として規格化されている）
        // TODO : O(N^2)のアルゴリズム。対象となる表現と、その他すべての表現に対して重複をチェックしている。必要に応じて高速化する

        // TODO : inappropriate_abstime, 以下の削除順番 の最適な順番は？
        // 不適切な絶対時間を削除(1万年、など) -> durationを削除(3月3日)など -> abstime の流れで。

        // erase numexp
        // 時間表現と被っていた場合、時間表現を優先するため、一番先にnumexpを削除（例：「1分」）
        for (var i = 0; i < numexps.size(); i++) {
            if (this.isCoveredByOtherTypeExpressions(numexps.get(i), abstimeexps)
                    || this.isCoveredByOtherTypeExpressions(numexps.get(i), reltimeexps)
                    || this.isCoveredByOtherTypeExpressions(numexps.get(i), durationexps)) {
                numexps.remove(i);
                i--;
            }
        }

        // erase reltime
        for (var i = 0; i < reltimeexps.size(); i++) {
            if (this.isCoveredByOtherTypeExpressions(reltimeexps.get(i), abstimeexps)
                    || this.isCoveredByOtherTypeExpressions(reltimeexps.get(i), numexps)
                    || this.isCoveredByOtherTypeExpressions(reltimeexps.get(i), durationexps)) {
                reltimeexps.remove(i);
                i--;
            }
        }

        // erase duration
        for (var i = 0; i < durationexps.size(); i++) {
            if (this.isCoveredByOtherTypeExpressions(durationexps.get(i), abstimeexps)
                    || this.isCoveredByOtherTypeExpressions(durationexps.get(i), reltimeexps)
                    || this.isCoveredByOtherTypeExpressions(durationexps.get(i), numexps)) {
                durationexps.remove(i);
                i--;
            }
        }

        // erase abstime
        for (var i = 0; i < abstimeexps.size(); i++) {
            if (this.isCoveredByOtherTypeExpressions(abstimeexps.get(i), numexps)
                    || this.isCoveredByOtherTypeExpressions(abstimeexps.get(i), reltimeexps)
                    || this.isCoveredByOtherTypeExpressions(abstimeexps.get(i), durationexps)) {
                abstimeexps.remove(i);
                i--;
            }
        }
    }


    // 文字列の前後を参照して表現を削除
    private boolean isSuffixMatch(String text, int positionStart, String targ) {

        final var sz = targ.length();
        for (var i = 0; i < sz; i++) {
            if (positionStart - 1 - i < 0) {
                return false;
            }
            if (text.charAt(positionStart - 1 - i) != targ.charAt(sz - 1 - i)) {
                return false;
            }
        }
        return true;
    }


    private <AnyTypeExpression extends NormalizedExpressionTemplate> boolean isInappropriatePrefix(String text, AnyTypeExpression anyTypeExpression) {

        // TODO : 辞書知識として分離してない & 実装が超雑。
        // 現在はverのみを対象。
        final List <String> targs = new ArrayList <>();
        targs.add("ver");
        targs.add("ｖｅｒ");

        for (var i = 0; i < targs.size(); i++) {
            if (this.isSuffixMatch(text, anyTypeExpression.positionStart, targs.get(i))) {
                return true;
            }
        }
        return false;
    }


    // C++ TO JAVA CONVERTER WARNING: The original C++ template specifier was replaced with a Java generic specifier, which may not produce the same
    // behavior:
    // ORIGINAL LINE: template <class AnyTypeExpression>
    private <AnyTypeExpression extends NormalizedExpressionTemplate> void deleteInappropriateExtractionByPrefix(String text,
            List <AnyTypeExpression> anyTypeExpressions) {

        // 文字列全体でなく、特定のprefix（文字列のsuffix）を持つものを削除する
        for (var i = 0; i < anyTypeExpressions.size(); i++) {
            if (this.isInappropriatePrefix(text, anyTypeExpressions.get(i))) {
                anyTypeExpressions.remove(i);
                i--;
            }
        }
    }


    // 辞書に記述した表現の削除
    @Override
    protected <AnyTypeExpression extends BaseExpressionTemplate> void deleteInappropriateExtractionUsingDictionaryOneType(
            List <AnyTypeExpression> anyTypeExpressions) {

        for (var i = 0; i < anyTypeExpressions.size(); i++) {
            if (this.isInappropriateStringsToBool(anyTypeExpressions.get(i).originalExpression)) {
                anyTypeExpressions.remove(i);
                i--;
            }
        }
    }


    @Override
    protected <AnyTypeExpression extends BaseExpressionTemplate> boolean isUrlStrings(String text, AnyTypeExpression anyTypeExpression) {

        // 数量表現・時間表現の前後がurlに含まれる文字、かつ自分自身もurlに含まれる文字であればtrue
        final var uText = new StringBuilder(text);
        var a = ' ';
        var b = ' ';
        if (anyTypeExpression.positionStart >= 1) {
            a = uText.charAt(anyTypeExpression.positionStart - 1);
        }
        if (anyTypeExpression.positionEnd < uText.length()) {
            b = uText.charAt(anyTypeExpression.positionEnd);
        }
        if (this.isUrlStringsToBool(String.valueOf(a)) && this.isUrlStringsToBool(String.valueOf(b))) {
            for (var i = 0; i < anyTypeExpression.originalExpression.length(); i++) {
                if (!this.isUrlStringsToBool(String.valueOf(anyTypeExpression.originalExpression.charAt(i)))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }


    @Override
    protected <AnyTypeExpression extends BaseExpressionTemplate> void deleteUrlStrings(String text, List <AnyTypeExpression> anyTypeExpressions) {

        // url、その他英字記号の羅列と思われる文字列中の表現は、削除する 例：「http://www.iphone3g.com」など（3gで抽出してしまう）
        for (var i = 0; i < anyTypeExpressions.size(); i++) {
            if (this.isUrlStrings(text, anyTypeExpressions.get(i))) {
                anyTypeExpressions.remove(i);
                i--;
            }
        }
    }


    @Override
    protected void deleteInappropriateExtractionUsingDictionary(String text, List <NumericalExpression> numexps, List <AbstimeExpression> abstimeexps,
            List <ReltimeExpression> reltimeexps, List <DurationExpression> durationexps) {

        this.deleteInappropriateExtractionUsingDictionaryOneType(numexps);
        this.deleteInappropriateExtractionUsingDictionaryOneType(abstimeexps);
        this.deleteInappropriateExtractionUsingDictionaryOneType(reltimeexps);
        this.deleteInappropriateExtractionUsingDictionaryOneType(durationexps);

        this.deleteInappropriateExtractionByPrefix(text, numexps);
        this.deleteInappropriateExtractionByPrefix(text, abstimeexps);
        this.deleteInappropriateExtractionByPrefix(text, reltimeexps);
        this.deleteInappropriateExtractionByPrefix(text, durationexps);

        this.deleteUrlStrings(text, numexps);
        this.deleteUrlStrings(text, abstimeexps);
        this.deleteUrlStrings(text, reltimeexps);
        this.deleteUrlStrings(text, durationexps);
    }


    /*
     * 不適切な時間表現の削除
     */
    private void reviseYearDoRevise(RefObject <Double> year) {

        if (20 < year.argValue && year.argValue < 100) {
            year.argValue += 1900;
        } else if (0 <= year.argValue && year.argValue <= 20) {
            year.argValue += 2000;
        }
    }


    private boolean isPeriodEtc(char uc) {

        // TODO : とりあえず実装。。。
        // TODO : digit_utilityとの依存性の問題が解決できず、こんな事態に。。
        return uc == '.' || uc == '・' || uc == '．' || uc == '-' || uc == '−' || uc == 'ー' || uc == '―';
    }


    private void reviseYear(AbstimeExpression abstimeexp) {

        // 「98年7月21日」などの表記のとき（20 < year < 100のとき）は、1900を加算する
        // 「01年7月21日」などの表記のとき（0<= year <= 20のとき）は、2000を加算する
        // 「西暦」とついてたら処理を行わない
        if (abstimeexp.originalExpression.contains("西")) {
            return;
        }
        final var tempRef_year = new RefObject <>(abstimeexp.valueLowerbound.year);
        this.reviseYearDoRevise(tempRef_year);
        abstimeexp.valueLowerbound.year = tempRef_year.argValue;
        final var tempRef_year2 = new RefObject <>(abstimeexp.valueUpperbound.year);
        this.reviseYearDoRevise(tempRef_year2);
        abstimeexp.valueUpperbound.year = tempRef_year2.argValue;
    }


    private boolean isOutOfRange(double x, double a, double b) {

        if (x == Symbol.INFINITY || x == -Symbol.INFINITY) {
            return false;
        }
        return (x < a || b < x);
    }


    private boolean isInappropriateTimeValue(NTime t) {

        return (this.isOutOfRange(t.year, 1, 3000) || this.isOutOfRange(t.month, 1, 12) || this.isOutOfRange(t.day, 1, 31)
                || this.isOutOfRange(t.hour, 0, 30) || this.isOutOfRange(t.minute, 0, 59) || this.isOutOfRange(t.second, 0, 59));
    }


    private boolean isInappropriateAbstimeexp(AbstimeExpression abstimeexp) {

        // 「1.2.3」のような表現かどうか
        if (abstimeexp.originalExpression.length() > 1 && this.isPeriodEtc(abstimeexp.originalExpression.charAt(1))) {
            return true;
        }

        // 時間の範囲が明らかにおかしいかどうか
        return (this.isInappropriateTimeValue(abstimeexp.valueLowerbound) || this.isInappropriateTimeValue(abstimeexp.valueUpperbound));
    }


    private void reviseOrDeleteAbstimeexp(List <AbstimeExpression> abstimeexps, RefObject <Integer> i) {

        final int a = i.argValue;
        final var abstimeexp = abstimeexps.get(a);
        this.reviseYear(abstimeexp);
        if (this.isInappropriateAbstimeexp(abstimeexp)) {
            abstimeexps.remove(a);
            i.argValue--;
        }
    }


    private void deleteInappropriateAbstimeexps(List <AbstimeExpression> abstimeexps) {

        for (var i = 0; i < abstimeexps.size(); i++) {
            final var tempRefI = new RefObject <>(i);
            this.reviseOrDeleteAbstimeexp(abstimeexps, tempRefI);
            i = tempRefI.argValue;
        }
    }


    /*
     * 不適切な範囲表現の削除（TODO : 範囲表現の扱いが定まっておらず、normalizerの方の処理が雑だった。今後も変わることが考えられるので、（非効率だが）ここで一括で設定
     */
    private <AnyTypeExpression extends NormalizedExpressionTemplate> void reviseInappropriateRangeExpression(AnyTypeExpression anyTypeExpression) {

        if (anyTypeExpression.originalExpression.charAt(0) == 'か'
                && anyTypeExpression.originalExpression.charAt(1) == 'ら') {
            // 先頭一致
            anyTypeExpression.originalExpression = anyTypeExpression.originalExpression.substring(2, anyTypeExpression.originalExpression.length());
            anyTypeExpression.positionStart += 2;
        }
        if (anyTypeExpression.originalExpression.length() > 2
                && anyTypeExpression.originalExpression.charAt(anyTypeExpression.originalExpression.length() - 2) == 'か'
                && anyTypeExpression.originalExpression.charAt(anyTypeExpression.originalExpression.length() - 1) == 'ら') {
            anyTypeExpression.originalExpression = anyTypeExpression.originalExpression.substring(0,
                    anyTypeExpression.originalExpression.length() - 2);
            anyTypeExpression.positionEnd -= 2;
        }
    }


    private <AnyTypeExpression extends NormalizedExpressionTemplate> void deleteInappropriateRangeExpressionOneType(
            List <AnyTypeExpression> anyTypeExpressions) {

        for (var i = 0; i < anyTypeExpressions.size(); i++) {
            this.reviseInappropriateRangeExpression(anyTypeExpressions.get(i));
        }
    }


    private void deleteInappropriateRangeExpression(
            List <NumericalExpression> numexps,
            List <AbstimeExpression> abstimeexps,
            List <ReltimeExpression> reltimeexps,
            List <DurationExpression> durationexps) {

        this.deleteInappropriateRangeExpressionOneType(numexps);
        this.deleteInappropriateRangeExpressionOneType(abstimeexps);
        this.deleteInappropriateRangeExpressionOneType(reltimeexps);
        this.deleteInappropriateRangeExpressionOneType(durationexps);
    }


    // 不適切な表現の削除
    @Override
    public void removeInappropriateExtraction(
            String text,
            List <NumericalExpression> numexps,
            List <AbstimeExpression> abstimeexps,
            List <ReltimeExpression> reltimeexps,
            List <DurationExpression> durationexps) {

        // TODO : この部分を、コンポーネントとして書き出す。辞書で指定できるようにする。 規格化処理は辞書で指定できるが、不適切な表現の処理は辞書でできるようにほとんどなっていない。
        this.deleteInappropriateAbstimeexps(abstimeexps);
        this.deleteDuplicateExtraction(numexps, abstimeexps, reltimeexps, durationexps);

        this.deleteInappropriateExtractionUsingDictionary(text, numexps, abstimeexps, reltimeexps, durationexps);
        this.deleteInappropriateRangeExpression(numexps, abstimeexps, reltimeexps, durationexps);

    }


    /*
    初期化処理
    */
    @SuppressWarnings ("unchecked")
    private void loadFromDictionary(final String dictionaryPath, List <InappropriateStrings> loadTarget) {

        loadTarget.clear();

        final Reader reader = new InputStreamReader(
                DigitUtilityImpl.class.getResourceAsStream(dictionaryPath));

        final var gson = new Gson();
        final var listType = new TypeToken <HashMap <String, Object>>() {
        }.getType();
        InappropriateStrings expression = null;
        try (var br = new BufferedReader(reader)) {
            String line;
            while ((line = br.readLine()) != null) {
                final var map = (HashMap <String, Object>) gson.fromJson(line, listType);
                expression = new InappropriateStrings();
                expression.str = (String) map.get("str");
                loadTarget.add(expression);
            }
        } catch (final IOException e) {
            InappropriateExpressionRemoverImpl.log.error(e.getMessage(), e);
        }
    }


    @Override
    protected void initInappropriateStringss(final String language) {

        final var inappropriateStringss = new ArrayList <InappropriateStrings>();

        final var dictionaryPath = new StringBuilder(DictionaryDirpath.DICTIONARY_DIRPATH);
        dictionaryPath.append(language);
        dictionaryPath.append("/inappropriate_strings_json.txt");
        this.loadFromDictionary(dictionaryPath.toString(), inappropriateStringss);

        for (var i = 0; i < inappropriateStringss.size(); i++) {
            this.inappropriateStringsToBool.put(inappropriateStringss.get(i).str, true);
        }
    }


    @Override
    public void initUrlStrings() {

        final var url_strings = "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ１２３４５６７８９０ａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚＡＢＣＤＥＦＧＨＩＪＫＭＮＬＰＱＲＳＴＵＶＷＸＹＺ";
        for (var i = 0; i < url_strings.length(); i++) {
            this.urlStringsToBool.put(String.valueOf(url_strings.charAt(i)), true);
        }
    }

}
