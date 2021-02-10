package jp.livlog.normalizeNumexp.abstimeExpressionNormalizer.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import jp.livlog.normalizeNumexp.abstimeExpressionNormalizer.AbstimeExpression;
import jp.livlog.normalizeNumexp.abstimeExpressionNormalizer.AbstimeExpressionNormalizer;
import jp.livlog.normalizeNumexp.abstimeExpressionNormalizer.LimitedAbstimeExpression;
import jp.livlog.normalizeNumexp.digitUtility.impl.DigitUtilityImpl;
import jp.livlog.normalizeNumexp.share.NNumber;
import jp.livlog.normalizeNumexp.share.NTime;
import jp.livlog.normalizeNumexp.share.NumberModifier;
import jp.livlog.normalizeNumexp.share.RefObject;
import jp.livlog.normalizeNumexp.share.Symbol;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AbstimeExpressionNormalizerImpl extends AbstimeExpressionNormalizer {

    public AbstimeExpressionNormalizerImpl(String language) {

        super(language);
    }


    @Override
    public void init() {

        this.loadFromDictionaries("abstime_expression_json.txt", "abstime_prefix_counter_json.txt", "abstime_prefix_json.txt",
                "abstime_suffix_json.txt");
    }


    @Override
    public void normalizeNumber(StringBuilder uText, List <NNumber> numbers) {

        this.NN.processDontFixBySymbol(uText.toString(), numbers);
    }


    @SuppressWarnings ("unchecked")
    @Override
    public void loadFromDictionary1(String dictionaryPath, List <LimitedAbstimeExpression> loadTarget) {

        loadTarget.clear();

        final Reader reader = new InputStreamReader(
                DigitUtilityImpl.class.getResourceAsStream(dictionaryPath));

        final var gson = new Gson();
        final var listType = new TypeToken <HashMap <String, Object>>() {
        }.getType();
        LimitedAbstimeExpression expression = null;
        try (var br = new BufferedReader(reader)) {
            String line;
            while ((line = br.readLine()) != null) {
                final var map = (HashMap <String, Object>) gson.fromJson(line, listType);
                expression = new LimitedAbstimeExpression();
                expression.pattern = (String) map.get("pattern");
                expression.correspondingTimePosition = (List <String>) map.get("corresponding_time_position");
                expression.processType = (List <String>) map.get("process_type");
                expression.ordinary = (Boolean) map.get("ordinary");
                expression.option = (String) map.get("option");
                loadTarget.add(expression);
            }
        } catch (final IOException e) {
            AbstimeExpressionNormalizerImpl.log.error(e.getMessage(), e);
        }
    }


    @Override
    public void reviseAnyTypeExpressionByMatchingLimitedExpression(
            List <AbstimeExpression> abstimeexps,
            RefObject <Integer> expressionId,
            LimitedAbstimeExpression matchingLimitedAbstimeExpressionImpl) {

        // 一致したパターンに応じて、規格化を行う
        final var finalIntegratedAbstimeexpId = expressionId.argValue + matchingLimitedAbstimeExpressionImpl.totalNumberOfPlaceHolder;
        abstimeexps.get(expressionId.argValue).positionEnd = abstimeexps.get(finalIntegratedAbstimeexpId).positionEnd
                + matchingLimitedAbstimeExpressionImpl.lengthOfStringsAfterFinalPlaceHolder;

        for (var i = 0; i < matchingLimitedAbstimeExpressionImpl.correspondingTimePosition.size(); i++) {
            this.setTime(abstimeexps.get(expressionId.argValue),
                    matchingLimitedAbstimeExpressionImpl.correspondingTimePosition.get(i),
                    abstimeexps.get(expressionId.argValue + i));
        }
        for (var i = 0; i < matchingLimitedAbstimeExpressionImpl.processType.size(); i++) {
            this.reviseAbstimeexpByProcessType(abstimeexps.get(expressionId.argValue), matchingLimitedAbstimeExpressionImpl.processType.get(i));
        }
        abstimeexps.get(expressionId.argValue).ordinary = matchingLimitedAbstimeExpressionImpl.ordinary;
        abstimeexps.get(expressionId.argValue).options.add(matchingLimitedAbstimeExpressionImpl.option);
        // for (var i = expressionId.argValue + 1; i < expressionId.argValue + 1 + matchingLimitedAbstimeExpressionImpl.totalNumberOfPlaceHolder; i++)
        // {
        // abstimeexps.remove(1);// 一致した部分のnumberを削除
        // }

        var i = 0;
        final var min = expressionId.argValue + 1;
        final var max = expressionId.argValue + matchingLimitedAbstimeExpressionImpl.totalNumberOfPlaceHolder;
        final var it = abstimeexps.iterator();
        while (it.hasNext()) {
            it.next();
            if (min <= i && i <= max) {
                it.remove();
            }
            i++;
        }
    }


    @Override
    public void reviseAnyTypeExpressionByMatchingPrefixCounter(
            AbstimeExpression anyTypeExpression,
            LimitedAbstimeExpression matchingLimitedExpression) {

        // 一致したパターンに応じて、規格化を行う（数字の前側に単位等が来る場合。絶対時間表現の場合「西暦」など）
        if (matchingLimitedExpression.option.equals("seireki")) {
            final int tmp = Integer.valueOf(matchingLimitedExpression.processType.get(0));
            anyTypeExpression.valueLowerbound.year += tmp;
            anyTypeExpression.valueUpperbound.year += tmp;
        } else if (matchingLimitedExpression.option.equals("gogo")) {
            anyTypeExpression.valueLowerbound.hour += 12;
            anyTypeExpression.valueUpperbound.hour += 12;
        } else if (matchingLimitedExpression.option.equals("gozen")) {

        } else {
            anyTypeExpression.options.add(matchingLimitedExpression.option);
        }
        anyTypeExpression.positionStart -= matchingLimitedExpression.pattern.length();
    }


    @Override
    public void reviseAnyTypeExpressionByNumberModifier(AbstimeExpression abstimeexp, NumberModifier numberModifier) {

        final var processType = numberModifier.processType;
        if (processType.equals("or_over")) {
            abstimeexp.valueUpperbound = new NTime(-Symbol.INFINITY);
        } else if (processType.equals("or_less")) {
            abstimeexp.valueLowerbound = new NTime(Symbol.INFINITY);
        } else if (processType.equals("over")) {
            abstimeexp.valueUpperbound = new NTime(-Symbol.INFINITY);
            abstimeexp.includeLowerbound = false;
        } else if (processType.equals("less")) {
            abstimeexp.valueLowerbound = new NTime(Symbol.INFINITY);
            abstimeexp.includeUpperbound = false;
        } else if (processType.equals("none")) {

        } else if (processType.equals("about")) {
            this.doTimeAbout(abstimeexp);
        } else if (processType.equals("zenhan")) {
            this.doTimeZenhan(abstimeexp);
        } else if (processType.equals("kouhan")) {
            this.doTimeKouhan(abstimeexp);
        } else if (processType.equals("nakaba")) {
            this.doTimeNakaba(abstimeexp);
        } else if (processType.equals("joujun")) {
            this.doTimeJoujun(abstimeexp);
        } else if (processType.equals("tyujun")) {
            this.doTimeTyujun(abstimeexp);
        } else if (processType.equals("gejun")) {
            this.doTimeGejun(abstimeexp);
        } else if (processType.equals("made")) {
            if (abstimeexp.valueUpperbound == abstimeexp.valueLowerbound) {
                // 「3時までに来て下さい」の場合
                abstimeexp.valueLowerbound = new NTime(Symbol.INFINITY);
            } else {
                // 「2時~3時までに来て下さい」の場合
            }
        } else {
            abstimeexp.options.add(processType);
        }
    }


    @Override
    public void deleteNotAnyTypeExpression(List <AbstimeExpression> abstimeexps) {

        for (var i = 0; i < abstimeexps.size(); i++) {
            if (this.normalizerUtility.isNullTime(abstimeexps.get(i).valueLowerbound)
                    && this.normalizerUtility.isNullTime(abstimeexps.get(i).valueUpperbound)) {
                abstimeexps.remove(i);
                i--;
            }
        }
    }


    @Override
    public void fixByRangeExpression(StringBuilder uText, List <AbstimeExpression> abstimeexps) {

        for (var i = 0; i < abstimeexps.size() - 1; i++) {
            if (this.haveKaraSuffix(abstimeexps.get(i).options)
                    && this.haveKaraPrefix(abstimeexps.get(i + 1).options)
                    && abstimeexps.get(i).positionEnd + 2 >= abstimeexps.get(i + 1).positionStart) {
                // 「4~12月」「4月3~4日」の場合、前者（後者）がそもそも時間表現として認識されてないので、時間表現として設定する。
                this.setAbstimeInformationToNullAbstime(abstimeexps.get(i), abstimeexps.get(i + 1));

                // 「2012/4/3~4/5」のような場合、どちらも時間表現として認識されているが、後者で情報が欠落しているので、これを埋める
                this.supplementAbstimeInformation(abstimeexps.get(i), abstimeexps.get(i + 1));

                // 範囲表現として設定する
                abstimeexps.get(i).valueUpperbound = abstimeexps.get(i + 1).valueUpperbound;
                abstimeexps.get(i).positionEnd = abstimeexps.get(i + 1).positionEnd;
                abstimeexps.get(i).setOriginalExpressionFromPosition(uText);
                this.mergeOptions(abstimeexps.get(i).options, abstimeexps.get(i + 1).options);
                abstimeexps.remove(i + 1);
            }
        }

    }


    private void setTime(AbstimeExpression abstimeexp, String correspondingTimePosition, AbstimeExpression integrateAbstimeexp) {

        if (correspondingTimePosition.equals("y")) {
            abstimeexp.valueLowerbound.year = integrateAbstimeexp.orgValueLowerbound;
            abstimeexp.valueUpperbound.year = integrateAbstimeexp.orgValueUpperbound;
        } else if (correspondingTimePosition.equals("m")) {
            abstimeexp.valueLowerbound.month = integrateAbstimeexp.orgValueLowerbound;
            abstimeexp.valueUpperbound.month = integrateAbstimeexp.orgValueUpperbound;
        } else if (correspondingTimePosition.equals("d")) {
            abstimeexp.valueLowerbound.day = integrateAbstimeexp.orgValueLowerbound;
            abstimeexp.valueUpperbound.day = integrateAbstimeexp.orgValueUpperbound;
        } else if (correspondingTimePosition.equals("h")) {
            abstimeexp.valueLowerbound.hour = integrateAbstimeexp.orgValueLowerbound;
            abstimeexp.valueUpperbound.hour = integrateAbstimeexp.orgValueUpperbound;
        } else if (correspondingTimePosition.equals("mn")) {
            abstimeexp.valueLowerbound.minute = integrateAbstimeexp.orgValueLowerbound;
            abstimeexp.valueUpperbound.minute = integrateAbstimeexp.orgValueUpperbound;
        } else if (correspondingTimePosition.equals("s")) {
            abstimeexp.valueLowerbound.second = integrateAbstimeexp.orgValueLowerbound;
            abstimeexp.valueUpperbound.second = integrateAbstimeexp.orgValueUpperbound;
        } else if (correspondingTimePosition.equals("seiki")) {
            abstimeexp.valueLowerbound.year = integrateAbstimeexp.orgValueLowerbound * 100 - 99;
            abstimeexp.valueUpperbound.year = integrateAbstimeexp.orgValueUpperbound * 100;
        }
    }


    private void reviseAbstimeexpByProcessType(AbstimeExpression abstimeexp, String processType) {

        // 修飾語でない、パターンに含まれるprocess_typeによる規格化表現の補正処理。
        if (processType.equals("gozen")) {
            if (abstimeexp.valueLowerbound.hour == Symbol.INFINITY) {
                abstimeexp.valueLowerbound.hour = 0;
                abstimeexp.valueUpperbound.hour = 12;
            } else {

            }
        } else if (processType.equals("gogo")) {
            if (abstimeexp.valueLowerbound.hour == Symbol.INFINITY) {
                abstimeexp.valueLowerbound.hour = 12;
                abstimeexp.valueUpperbound.hour = 24;
            } else {
                abstimeexp.valueLowerbound.hour += 12;
                abstimeexp.valueUpperbound.hour += 12;
            }
        } else if (processType.equals("han")) {
            abstimeexp.valueLowerbound.minute = 30;
            abstimeexp.valueUpperbound.minute = 30;
        } else if (processType.equals("unclear") && (1800 <= abstimeexp.valueLowerbound.month && abstimeexp.valueLowerbound.month <= 2100)) {
            // 「2012/3」「3/10」の曖昧性を解消するためのプロセス
            // 最初はmonth/dayとして認識している。monthの値として変で、yearとして考えられる場合、これを変更する
            abstimeexp.valueLowerbound.year = abstimeexp.valueLowerbound.month;
            abstimeexp.valueUpperbound.year = abstimeexp.valueUpperbound.month;
            abstimeexp.valueLowerbound.month = abstimeexp.valueLowerbound.day;
            abstimeexp.valueUpperbound.month = abstimeexp.valueUpperbound.day;
            abstimeexp.valueLowerbound.day = Symbol.INFINITY;
            abstimeexp.valueUpperbound.day = -Symbol.INFINITY;
        }
    }


    /*
     *修飾語による規格化表現の補正処理。
     */
    private void doTimeAbout(AbstimeExpression abstimeexp) {

        final var tvl = abstimeexp.valueLowerbound;
        final var tvu = abstimeexp.valueUpperbound;
        final var targetTimePosition = this.normalizerUtility.identifyTimeDetail(abstimeexp.valueLowerbound);
        if (targetTimePosition.equals("y")) {
            // TODO : 「1800年ごろ」という指定なら1790~1810くらいだと感じる 「1811年ごろ」という指定なら1810~1812くらいだと感じる 以下は雑な実装
            tvl.year -= 5;
            tvu.year += 5;
        } else if (targetTimePosition.equals("m")) {
            tvl.month -= 1;
            tvu.month += 1;
        } else if (targetTimePosition.equals("d")) {
            tvl.day -= 1;
            tvu.day += 1;
        } else if (targetTimePosition.equals("h")) {
            tvl.hour -= 1;
            tvu.hour += 1;
        } else if (targetTimePosition.equals("mn")) {
            tvl.minute -= 5;
            tvu.minute += 5;
        } else if (targetTimePosition.equals("s")) {
            tvl.second -= 5;
            tvu.second += 5;
        }
    }


    // ~~他の処理も同様
    private void doTimeZenhan(AbstimeExpression abstimeexp) {

        // 「18世紀前半」「1989年前半」「7月前半」「3日朝」など。
        // TODO : 「18世紀はじめ」などもzenhanに括ってしまっている。より細かく分類が行いたい場合は、hajime関数などを書いて処理
        final var tvl = abstimeexp.valueLowerbound;
        final var tvu = abstimeexp.valueUpperbound;
        final var targetTimePosition = this.normalizerUtility.identifyTimeDetail(abstimeexp.valueLowerbound);
        if (targetTimePosition.equals("y")) {
            if (tvl.year != tvu.year) {
                // 「18世紀前半」のとき
                tvu.year = (tvl.year + tvu.year) / 2 - 0.5;
            } else {
                // 「1989年前半」のとき
                tvl.month = 1;
                tvu.month = 6;
            }
        } else if (targetTimePosition.equals("m")) {
            // 「7月前半」のとき
            tvl.day = 1;
            tvl.day = 15;
        } else if (targetTimePosition.equals("d")) {
            // 「3日朝」のとき
            tvl.hour = 5;
            tvu.hour = 12;
        } else {
            // これ以外でzenhanになる場合はない？ 処理を行わない。
        }
    }


    private void doTimeKouhan(AbstimeExpression abstimeexp) {

        // 「18世紀後半」「1989年後半」「7月後半」など。
        // TODO : 「18世紀末」「3日夜」などもkouhanに括ってしまっている。
        final var tvl = abstimeexp.valueLowerbound;
        final var tvu = abstimeexp.valueUpperbound;
        final var targetTimePosition = this.normalizerUtility.identifyTimeDetail(abstimeexp.valueLowerbound);
        if (targetTimePosition.equals("y")) {
            if (tvl.year != tvu.year) {
                // 「18世紀後半」のとき
                tvl.year = (tvl.year + tvu.year) / 2 + 0.5;
            } else {
                // 「1989年後半」のとき
                tvl.month = 7;
                tvu.month = 12;
            }
        } else if (targetTimePosition.equals("m")) {
            // 「7月後半」のとき
            tvl.day = 16;
            tvl.day = 31;
        } else if (targetTimePosition.equals("d")) {
            // 「3日夜」のとき
            tvl.hour = 18;
            tvu.hour = 24;
        } else {
            // これ以外の場合はない？ 処理を行わない。
        }
    }


    private void doTimeNakaba(AbstimeExpression abstimeexp) {

        // 「18世紀中盤」「1989年中盤」「7月中盤」など。
        // TODO : 「18世紀なかば」「3日昼」などもnakabaに括ってしまっている。
        final var tvl = abstimeexp.valueLowerbound;
        final var tvu = abstimeexp.valueUpperbound;
        final var targetTimePosition = this.normalizerUtility.identifyTimeDetail(abstimeexp.valueLowerbound);
        if (targetTimePosition.equals("y")) {
            if (tvl.year != tvu.year) {
                // 「18世紀中盤」のとき
                final var tmp = (int) ((tvu.year - tvl.year) / 4);
                tvl.year += tmp;
                tvu.year -= tmp;
            } else {
                // 「1989年中盤」のとき
                tvl.month = 4;
                tvu.month = 9;
            }
        } else if (targetTimePosition.equals("m")) {
            // 「7月半ば」のとき
            tvl.day = 10;
            tvl.day = 20;
        } else if (targetTimePosition.equals("d")) {
            // 「3日昼」のとき
            tvl.hour = 10;
            tvu.hour = 15;
        } else {
            // これ以外の場合はない？ 処理を行わない。
        }
    }


    private void doTimeJoujun(AbstimeExpression abstimeexp) {

        final var tvl = abstimeexp.valueLowerbound;
        final var tvu = abstimeexp.valueUpperbound;
        final var targetTimePosition = this.normalizerUtility.identifyTimeDetail(abstimeexp.valueLowerbound);
        if (targetTimePosition.equals("m")) {
            tvl.day = 1;
            tvu.day = 10;
        }
    }


    private void doTimeTyujun(AbstimeExpression abstimeexp) {

        final var tvl = abstimeexp.valueLowerbound;
        final var tvu = abstimeexp.valueUpperbound;
        final var targetTimePosition = this.normalizerUtility.identifyTimeDetail(abstimeexp.valueLowerbound);
        if (targetTimePosition.equals("m")) {
            tvl.day = 11;
            tvu.day = 20;
        }
    }


    private void doTimeGejun(AbstimeExpression abstimeexp) {

        final var tvl = abstimeexp.valueLowerbound;
        final var tvu = abstimeexp.valueUpperbound;
        final var targetTimePosition = this.normalizerUtility.identifyTimeDetail(abstimeexp.valueLowerbound);
        if (targetTimePosition.equals("m")) {
            tvl.day = 21;
            tvu.day = 31;
        }
    }

    // private void supplementAbstimeInformationSpecificType(
    // double timeElement1Lowerbound,
    // double timeElement1Upperbound,
    // double timeElement2Lowerbound,
    // double timeElement2Upperbound) {
    //
    // if (timeElement1Lowerbound == Symbol.INFINITY
    // && timeElement1Upperbound == -Symbol.INFINITY) {
    // // 1がセットされていなければ、2に合わせる
    // timeElement1Lowerbound = timeElement2Lowerbound;
    // timeElement1Upperbound = timeElement2Upperbound;
    // } else {
    //
    // }
    // }


    private boolean isSupplementAbstimeInformationSpecificType(double timeElement1Lowerbound, double timeElement1Upperbound) {

        if (timeElement1Lowerbound == Symbol.INFINITY
                && timeElement1Upperbound == -Symbol.INFINITY) {
            return true;
        }
        return false;
    }


    private void supplementAbstimeInformation(AbstimeExpression abstime1, AbstimeExpression abstime2) {

        // this.supplementAbstimeInformationSpecificType(
        // abstime1.valueLowerbound.year,
        // abstime1.valueUpperbound.year,
        // abstime2.valueLowerbound.year,
        // abstime2.valueUpperbound.year);
        // this.supplementAbstimeInformationSpecificType(
        // abstime2.valueLowerbound.year,
        // abstime2.valueUpperbound.year,
        // abstime1.valueLowerbound.year,
        // abstime1.valueUpperbound.year);
        // this.supplementAbstimeInformationSpecificType(
        // abstime1.valueLowerbound.month,
        // abstime1.valueUpperbound.month,
        // abstime2.valueLowerbound.month,
        // abstime2.valueUpperbound.month);
        // this.supplementAbstimeInformationSpecificType(
        // abstime2.valueLowerbound.month,
        // abstime2.valueUpperbound.month,
        // abstime1.valueLowerbound.month,
        // abstime1.valueUpperbound.month);
        // this.supplementAbstimeInformationSpecificType(
        // abstime1.valueLowerbound.day,
        // abstime1.valueUpperbound.day,
        // abstime2.valueLowerbound.day,
        // abstime2.valueUpperbound.day);
        // this.supplementAbstimeInformationSpecificType(
        // abstime2.valueLowerbound.day,
        // abstime2.valueUpperbound.day,
        // abstime1.valueLowerbound.day,
        // abstime1.valueUpperbound.day);
        // this.supplementAbstimeInformationSpecificType(
        // abstime1.valueLowerbound.hour,
        // abstime1.valueUpperbound.hour,
        // abstime2.valueLowerbound.hour,
        // abstime2.valueUpperbound.hour);
        // this.supplementAbstimeInformationSpecificType(
        // abstime2.valueLowerbound.hour,
        // abstime2.valueUpperbound.hour,
        // abstime1.valueLowerbound.hour,
        // abstime1.valueUpperbound.hour);
        // this.supplementAbstimeInformationSpecificType(
        // abstime1.valueLowerbound.minute,
        // abstime1.valueUpperbound.minute,
        // abstime2.valueLowerbound.minute,
        // abstime2.valueUpperbound.minute);
        // this.supplementAbstimeInformationSpecificType(
        // abstime2.valueLowerbound.minute,
        // abstime2.valueUpperbound.minute,
        // abstime1.valueLowerbound.minute,
        // abstime1.valueUpperbound.minute);
        // this.supplementAbstimeInformationSpecificType(
        // abstime1.valueLowerbound.second,
        // abstime1.valueUpperbound.second,
        // abstime2.valueLowerbound.second,
        // abstime2.valueUpperbound.second);
        // this.supplementAbstimeInformationSpecificType(
        // abstime2.valueLowerbound.second,
        // abstime2.valueUpperbound.second,
        // abstime1.valueLowerbound.second,
        // abstime1.valueUpperbound.second);

        if (this.isSupplementAbstimeInformationSpecificType(abstime1.valueLowerbound.year, abstime1.valueUpperbound.year)) {
            abstime1.valueLowerbound.year = abstime2.valueLowerbound.year;
            abstime1.valueUpperbound.year = abstime2.valueUpperbound.year;
        }
        if (this.isSupplementAbstimeInformationSpecificType(abstime2.valueLowerbound.year, abstime2.valueUpperbound.year)) {
            abstime2.valueLowerbound.year = abstime1.valueLowerbound.year;
            abstime2.valueUpperbound.year = abstime1.valueUpperbound.year;
        }
        if (this.isSupplementAbstimeInformationSpecificType(abstime1.valueLowerbound.month, abstime1.valueUpperbound.month)) {
            abstime1.valueLowerbound.month = abstime2.valueLowerbound.month;
            abstime1.valueUpperbound.month = abstime2.valueUpperbound.month;
        }
        if (this.isSupplementAbstimeInformationSpecificType(abstime2.valueLowerbound.month, abstime2.valueUpperbound.month)) {
            abstime2.valueLowerbound.month = abstime1.valueLowerbound.month;
            abstime2.valueUpperbound.month = abstime1.valueUpperbound.month;
        }
        if (this.isSupplementAbstimeInformationSpecificType(abstime1.valueLowerbound.day, abstime1.valueUpperbound.day)) {
            abstime1.valueLowerbound.day = abstime2.valueLowerbound.day;
            abstime1.valueUpperbound.day = abstime2.valueUpperbound.day;
        }
        if (this.isSupplementAbstimeInformationSpecificType(abstime2.valueLowerbound.day, abstime2.valueUpperbound.day)) {
            abstime2.valueLowerbound.day = abstime1.valueLowerbound.day;
            abstime2.valueUpperbound.day = abstime1.valueUpperbound.day;
        }
        if (this.isSupplementAbstimeInformationSpecificType(abstime1.valueLowerbound.hour, abstime1.valueUpperbound.hour)) {
            abstime1.valueLowerbound.hour = abstime2.valueLowerbound.hour;
            abstime1.valueUpperbound.hour = abstime2.valueUpperbound.hour;
        }
        if (this.isSupplementAbstimeInformationSpecificType(abstime2.valueLowerbound.hour, abstime2.valueUpperbound.hour)) {
            abstime2.valueLowerbound.hour = abstime1.valueLowerbound.hour;
            abstime2.valueUpperbound.hour = abstime1.valueUpperbound.hour;
        }
        if (this.isSupplementAbstimeInformationSpecificType(abstime1.valueLowerbound.minute, abstime1.valueUpperbound.minute)) {
            abstime1.valueLowerbound.minute = abstime2.valueLowerbound.minute;
            abstime1.valueUpperbound.minute = abstime2.valueUpperbound.minute;
        }
        if (this.isSupplementAbstimeInformationSpecificType(abstime2.valueLowerbound.minute, abstime2.valueUpperbound.minute)) {
            abstime2.valueLowerbound.minute = abstime1.valueLowerbound.minute;
            abstime2.valueUpperbound.minute = abstime1.valueUpperbound.minute;
        }
        if (this.isSupplementAbstimeInformationSpecificType(abstime1.valueLowerbound.minute, abstime1.valueUpperbound.second)) {
            abstime1.valueLowerbound.second = abstime2.valueLowerbound.second;
            abstime1.valueUpperbound.second = abstime2.valueUpperbound.second;
        }
        if (this.isSupplementAbstimeInformationSpecificType(abstime2.valueLowerbound.minute, abstime2.valueUpperbound.second)) {
            abstime2.valueLowerbound.second = abstime1.valueLowerbound.second;
            abstime2.valueUpperbound.second = abstime1.valueUpperbound.second;
        }

    }


    private void setAbstimeInformationToNullAbstime(AbstimeExpression abstime1, AbstimeExpression abstime2) {

        if (abstime1.valueLowerbound.equalsTo(new NTime(Symbol.INFINITY))) {
            // lower_boundが空 = 時間として認識されていない場合（例：「4~12月」の「4~」）、lower_boundを設定
            // TODO :
            // 本当は、[i+1]の最上位時間単位を指定したいので、最下位時間単位を返すidentify_time_detailを用いるのは間違っている。
            // しかし、このパターンのとき、2つ以上の時間単位がでてくることは考えられないので、とりあえずこの実装でOK
            final var targetTimePosition = this.normalizerUtility.identifyTimeDetail(abstime2.valueUpperbound);
            final var tmpAbstimeexp = abstime1;
            this.setTime(abstime1, targetTimePosition, tmpAbstimeexp);
        } else if (abstime2.valueUpperbound.equalsTo(new NTime(-Symbol.INFINITY))) {
            // upper_boundが空 =
            // 時間として認識されていない場合（例：「2012/4/3~6」の「~6」）、upper_boundを設定
            abstime2.valueUpperbound = abstime1.valueUpperbound;
            final var targetTimePosition = this.normalizerUtility.identifyTimeDetail(abstime1.valueUpperbound);
            final var tmpAbstimeexp = abstime2;
            this.setTime(abstime2, targetTimePosition, tmpAbstimeexp);
        }
    }


    @Override
    public void convertNumbersToAnyTypeExpressions(List <NNumber> numbers, List <AbstimeExpression> anyTypeExpressions) {

        for (final NNumber number : numbers) {

            final var anyTypeExpression = new AbstimeExpression(number);
            anyTypeExpression.originalExpression = number.originalExpression;
            anyTypeExpression.positionStart = number.positionStart;
            anyTypeExpression.positionEnd = number.positionEnd;
            anyTypeExpressions.add(anyTypeExpression);
        }
    }
}
