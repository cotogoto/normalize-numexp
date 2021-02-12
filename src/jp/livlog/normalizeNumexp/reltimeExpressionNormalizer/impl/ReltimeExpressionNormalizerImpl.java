package jp.livlog.normalizeNumexp.reltimeExpressionNormalizer.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import jp.livlog.normalizeNumexp.digitUtility.impl.DigitUtilityImpl;
import jp.livlog.normalizeNumexp.reltimeExpressionNormalizer.LimitedReltimeExpression;
import jp.livlog.normalizeNumexp.reltimeExpressionNormalizer.ReltimeExpression;
import jp.livlog.normalizeNumexp.reltimeExpressionNormalizer.ReltimeExpressionNormalizer;
import jp.livlog.normalizeNumexp.share.NNumber;
import jp.livlog.normalizeNumexp.share.NTime;
import jp.livlog.normalizeNumexp.share.NumberModifier;
import jp.livlog.normalizeNumexp.share.RefObject;
import jp.livlog.normalizeNumexp.share.Symbol;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReltimeExpressionNormalizerImpl extends ReltimeExpressionNormalizer {

    public ReltimeExpressionNormalizerImpl(String language) {

        super(language);
    }


    @Override
    public void init() {

        this.loadFromDictionaries("reltime_expression_json.txt", "reltime_prefix_counter_json.txt", "reltime_prefix_json.txt",
                "reltime_suffix_json.txt");
    }


    @Override
    public void normalizeNumber(StringBuilder uText, List <NNumber> numbers) {

        this.NN.process(uText.toString(), numbers);
    }


    @SuppressWarnings ("unchecked")
    @Override
    public void loadFromDictionary1(String dictionaryPath, List <LimitedReltimeExpression> loadTarget) {

        loadTarget.clear();

        final Reader reader = new InputStreamReader(
                DigitUtilityImpl.class.getResourceAsStream(dictionaryPath));

        final var gson = new Gson();
        final var listType = new TypeToken <HashMap <String, Object>>() {
        }.getType();
        LimitedReltimeExpression expression = null;
        try (var br = new BufferedReader(reader)) {
            String line;
            while ((line = br.readLine()) != null) {
                final var map = (HashMap <String, Object>) gson.fromJson(line, listType);
                expression = new LimitedReltimeExpression();
                expression.pattern = (String) map.get("pattern");
                expression.correspondingTimePosition = (List <String>) map.get("corresponding_time_position");
                expression.processType = (List <String>) map.get("process_type");
                expression.ordinary = (Boolean) map.get("ordinary");
                expression.option = (String) map.get("option");
                loadTarget.add(expression);
            }
        } catch (final IOException e) {
            ReltimeExpressionNormalizerImpl.log.error(e.getMessage(), e);
        }
    }


    private void setTime(ReltimeExpression reltimeexp, String correspondingTimePosition, ReltimeExpression integrateReltimeexp) {

        if (correspondingTimePosition.equals("y")) {
            reltimeexp.valueLowerboundAbs.year = integrateReltimeexp.orgValueLowerbound;
            reltimeexp.valueUpperboundAbs.year = integrateReltimeexp.orgValueUpperbound;
        } else if (correspondingTimePosition.equals("m")) {
            reltimeexp.valueLowerboundAbs.month = integrateReltimeexp.orgValueLowerbound;
            reltimeexp.valueUpperboundAbs.month = integrateReltimeexp.orgValueUpperbound;
        } else if (correspondingTimePosition.equals("d")) {
            reltimeexp.valueLowerboundAbs.day = integrateReltimeexp.orgValueLowerbound;
            reltimeexp.valueUpperboundAbs.day = integrateReltimeexp.orgValueUpperbound;
        } else if (correspondingTimePosition.equals("h")) {
            reltimeexp.valueLowerboundAbs.hour = integrateReltimeexp.orgValueLowerbound;
            reltimeexp.valueUpperboundAbs.hour = integrateReltimeexp.orgValueUpperbound;
        } else if (correspondingTimePosition.equals("mn")) {
            reltimeexp.valueLowerboundAbs.minute = integrateReltimeexp.orgValueLowerbound;
            reltimeexp.valueUpperboundAbs.minute = integrateReltimeexp.orgValueUpperbound;
        } else if (correspondingTimePosition.equals("s")) {
            reltimeexp.valueLowerboundAbs.second = integrateReltimeexp.orgValueLowerbound;
            reltimeexp.valueUpperboundAbs.second = integrateReltimeexp.orgValueUpperbound;
        } else if (correspondingTimePosition.equals("seiki")) {
            reltimeexp.valueLowerboundAbs.year = integrateReltimeexp.orgValueLowerbound * 100 - 99;
            reltimeexp.valueUpperboundAbs.year = integrateReltimeexp.orgValueUpperbound * 100;
        } else if (correspondingTimePosition.equals("w")) {
            reltimeexp.valueLowerboundAbs.day = integrateReltimeexp.orgValueLowerbound * 7;
            reltimeexp.valueUpperboundAbs.day = integrateReltimeexp.orgValueUpperbound * 7;
        } else if (correspondingTimePosition.equals("+y")) {
            reltimeexp.valueLowerboundRel.year = integrateReltimeexp.orgValueLowerbound;
            reltimeexp.valueUpperboundRel.year = integrateReltimeexp.orgValueUpperbound;
        } else if (correspondingTimePosition.equals("+m")) {
            reltimeexp.valueLowerboundRel.month = integrateReltimeexp.orgValueLowerbound;
            reltimeexp.valueUpperboundRel.month = integrateReltimeexp.orgValueUpperbound;
        } else if (correspondingTimePosition.equals("+d")) {
            reltimeexp.valueLowerboundRel.day = integrateReltimeexp.orgValueLowerbound;
            reltimeexp.valueUpperboundRel.day = integrateReltimeexp.orgValueUpperbound;
        } else if (correspondingTimePosition.equals("+h")) {
            reltimeexp.valueLowerboundRel.hour = integrateReltimeexp.orgValueLowerbound;
            reltimeexp.valueUpperboundRel.hour = integrateReltimeexp.orgValueUpperbound;
        } else if (correspondingTimePosition.equals("+mn")) {
            reltimeexp.valueLowerboundRel.minute = integrateReltimeexp.orgValueLowerbound;
            reltimeexp.valueUpperboundRel.minute = integrateReltimeexp.orgValueUpperbound;
        } else if (correspondingTimePosition.equals("+s")) {
            reltimeexp.valueLowerboundRel.second = integrateReltimeexp.orgValueLowerbound;
            reltimeexp.valueUpperboundRel.second = integrateReltimeexp.orgValueUpperbound;
        } else if (correspondingTimePosition.equals("+seiki")) {
            reltimeexp.valueLowerboundRel.year = integrateReltimeexp.orgValueLowerbound * 100;
            reltimeexp.valueUpperboundRel.year = integrateReltimeexp.orgValueUpperbound * 100;
        } else if (correspondingTimePosition.equals("+seiki")) {
            reltimeexp.valueLowerboundRel.day = integrateReltimeexp.orgValueLowerbound * 7;
            reltimeexp.valueUpperboundRel.day = integrateReltimeexp.orgValueUpperbound * 7;
        } else if (correspondingTimePosition.equals("-y")) {
            reltimeexp.valueLowerboundRel.year = -integrateReltimeexp.orgValueLowerbound;
            reltimeexp.valueUpperboundRel.year = -integrateReltimeexp.orgValueUpperbound;
        } else if (correspondingTimePosition.equals("-m")) {
            reltimeexp.valueLowerboundRel.month = -integrateReltimeexp.orgValueLowerbound;
            reltimeexp.valueUpperboundRel.month = -integrateReltimeexp.orgValueUpperbound;
        } else if (correspondingTimePosition.equals("-d")) {
            reltimeexp.valueLowerboundRel.day = -integrateReltimeexp.orgValueLowerbound;
            reltimeexp.valueUpperboundRel.day = -integrateReltimeexp.orgValueUpperbound;
        } else if (correspondingTimePosition.equals("-h")) {
            reltimeexp.valueLowerboundRel.hour = -integrateReltimeexp.orgValueLowerbound;
            reltimeexp.valueUpperboundRel.hour = -integrateReltimeexp.orgValueUpperbound;
        } else if (correspondingTimePosition.equals("-mn")) {
            reltimeexp.valueLowerboundRel.minute = -integrateReltimeexp.orgValueLowerbound;
            reltimeexp.valueUpperboundRel.minute = -integrateReltimeexp.orgValueUpperbound;
        } else if (correspondingTimePosition.equals("-s")) {
            reltimeexp.valueLowerboundRel.second = -integrateReltimeexp.orgValueLowerbound;
            reltimeexp.valueUpperboundRel.second = -integrateReltimeexp.orgValueUpperbound;
        } else if (correspondingTimePosition.equals("-seiki")) {
            reltimeexp.valueLowerboundRel.year = -integrateReltimeexp.orgValueLowerbound * 100;
            reltimeexp.valueUpperboundRel.year = -integrateReltimeexp.orgValueUpperbound * 100;
        } else if (correspondingTimePosition.equals("-w")) {
            reltimeexp.valueLowerboundRel.day = -integrateReltimeexp.orgValueLowerbound * 7;
            reltimeexp.valueUpperboundRel.day = -integrateReltimeexp.orgValueUpperbound * 7;
        }
    }


    private void doOptionHan(ReltimeExpression reltimeexp, String correspondingTimePosition) {

        // TODO : 「週」、「世紀」に対応していない部分がちらほらある。
        if (correspondingTimePosition.equals("+y")) {
            reltimeexp.valueLowerboundRel.year += 0.5;
            reltimeexp.valueUpperboundRel.year += 0.5;
        } else if (correspondingTimePosition.equals("+m")) {
            reltimeexp.valueLowerboundRel.month += 0.5;
            reltimeexp.valueUpperboundRel.month += 0.5;
        } else if (correspondingTimePosition.equals("+d")) {
            reltimeexp.valueLowerboundRel.day += 0.5;
            reltimeexp.valueUpperboundRel.day += 0.5;
        } else if (correspondingTimePosition.equals("+h")) {
            reltimeexp.valueLowerboundRel.hour += 0.5;
            reltimeexp.valueUpperboundRel.hour += 0.5;
        } else if (correspondingTimePosition.equals("+mn")) {
            reltimeexp.valueLowerboundRel.minute += 0.5;
            reltimeexp.valueUpperboundRel.minute += 0.5;
        } else if (correspondingTimePosition.equals("+s")) {
            reltimeexp.valueLowerboundRel.second += 0.5;
            reltimeexp.valueUpperboundRel.second += 0.5;
        } else if (correspondingTimePosition.equals("+seiki")) {
            reltimeexp.valueLowerboundRel.year += 50;
            reltimeexp.valueUpperboundRel.year += 50;
        } else if (correspondingTimePosition.equals("-y")) {
            reltimeexp.valueLowerboundRel.year -= 0.5;
            reltimeexp.valueUpperboundRel.year -= 0.5;
        } else if (correspondingTimePosition.equals("-m")) {
            reltimeexp.valueLowerboundRel.month -= 0.5;
            reltimeexp.valueUpperboundRel.month -= 0.5;
        } else if (correspondingTimePosition.equals("-d")) {
            reltimeexp.valueLowerboundRel.day -= 0.5;
            reltimeexp.valueUpperboundRel.day -= 0.5;
        } else if (correspondingTimePosition.equals("-h")) {
            reltimeexp.valueLowerboundRel.hour -= 0.5;
            reltimeexp.valueUpperboundRel.hour -= 0.5;
        } else if (correspondingTimePosition.equals("-mn")) {
            reltimeexp.valueLowerboundRel.minute -= 0.5;
            reltimeexp.valueUpperboundRel.minute -= 0.5;
        } else if (correspondingTimePosition.equals("-s")) {
            reltimeexp.valueLowerboundRel.second -= 0.5;
            reltimeexp.valueUpperboundRel.second -= 0.5;
        } else if (correspondingTimePosition.equals("-seiki")) {
            reltimeexp.valueLowerboundRel.year -= 50;
            reltimeexp.valueUpperboundRel.year -= 50;
        }
    }


    private void reviseReltimeexpByProcessType(ReltimeExpression reltimeexp, String processType,
            LimitedReltimeExpression matchingLimitedReltimeExpression) {

        if (processType.equals("han")) {
            if (matchingLimitedReltimeExpression.correspondingTimePosition.isEmpty()) {
                return;
            }
            final var correspondingTimePosition = matchingLimitedReltimeExpression.correspondingTimePosition
                    .get(matchingLimitedReltimeExpression.correspondingTimePosition.size() - 1);
            this.doOptionHan(reltimeexp, correspondingTimePosition);
        } else if (processType.equals("or_over")) {
            reltimeexp.valueUpperboundAbs = new NTime(-Symbol.INFINITY);
        } else if (processType.equals("or_less")) {
            reltimeexp.valueLowerboundAbs = new NTime(Symbol.INFINITY);
        } else if (processType.equals("over")) {
            reltimeexp.valueUpperboundAbs = new NTime(-Symbol.INFINITY);
            reltimeexp.includeLowerbound = false;
        } else if (processType.equals("less")) {
            reltimeexp.valueLowerboundAbs = new NTime(Symbol.INFINITY);
            reltimeexp.includeUpperbound = false;
        } else if (processType.equals("none")) {

        } else if (processType.equals("inai")) {
            reltimeexp.valueLowerboundRel = new NTime(0);
        }

    }


    @Override
    public void reviseAnyTypeExpressionByMatchingLimitedExpression(List <ReltimeExpression> reltimeexps, RefObject <Integer> expressionId,
            LimitedReltimeExpression matchingLimitedReltimeExpression) {

        final var final_integrated_reltimeexp_id = expressionId.argValue + matchingLimitedReltimeExpression.totalNumberOfPlaceHolder;
        reltimeexps.get(expressionId.argValue).positionEnd = reltimeexps.get(final_integrated_reltimeexp_id).positionEnd
                + matchingLimitedReltimeExpression.lengthOfStringsAfterFinalPlaceHolder;
        for (var i = 0; i < matchingLimitedReltimeExpression.correspondingTimePosition.size(); i++) {
            this.setTime(reltimeexps.get(expressionId.argValue), matchingLimitedReltimeExpression.correspondingTimePosition.get(i),
                    reltimeexps.get(expressionId.argValue + i));
        }
        for (var i = 0; i < matchingLimitedReltimeExpression.processType.size(); i++) {
            this.reviseReltimeexpByProcessType(reltimeexps.get(expressionId.argValue), matchingLimitedReltimeExpression.processType.get(i),
                    matchingLimitedReltimeExpression);
        }
        reltimeexps.get(expressionId.argValue).ordinary = matchingLimitedReltimeExpression.ordinary;

        var i = 0;
        final var min = expressionId.argValue + 1;
        final var max = expressionId.argValue + matchingLimitedReltimeExpression.totalNumberOfPlaceHolder;
        final var it = reltimeexps.iterator();
        while (it.hasNext()) {
            it.next();
            if (min <= i && i <= max) {
                it.remove();
            }
            i++;
        }

    }


    @Override
    public void reviseAnyTypeExpressionByMatchingPrefixCounter(ReltimeExpression reltimeexp, LimitedReltimeExpression matchingLimitedExpression) {

        if (matchingLimitedExpression.option.equals("add_relation")) {
            // 「去年3月」などの、「相対時間表現」＋「絶対時間表現」からなる処理
            if (this.normalizerUtility.isNullTime(reltimeexp.valueLowerboundAbs)
                    && this.normalizerUtility.isNullTime(reltimeexp.valueUpperboundAbs)) {
                return; // 絶対時間表現が抽出されていなければ、処理を行わない
            }
            final var relation_value = Integer.parseInt(matchingLimitedExpression.processType.get(0));
            if (matchingLimitedExpression.correspondingTimePosition.get(0).equals("y")) {
                reltimeexp.valueLowerboundRel.year = relation_value;
                reltimeexp.valueUpperboundRel.year = relation_value;
            } else if (matchingLimitedExpression.correspondingTimePosition.get(0).equals("m")) {
                reltimeexp.valueLowerboundRel.month = relation_value;
                reltimeexp.valueUpperboundRel.month = relation_value;
            } else if (matchingLimitedExpression.correspondingTimePosition.get(0).equals("d")) {
                reltimeexp.valueLowerboundRel.day = relation_value;
                reltimeexp.valueUpperboundRel.day = relation_value;
            } else if (matchingLimitedExpression.correspondingTimePosition.get(0).equals("h")) {
                reltimeexp.valueLowerboundRel.hour = relation_value;
                reltimeexp.valueUpperboundRel.hour = relation_value;
            } else if (matchingLimitedExpression.correspondingTimePosition.get(0).equals("mn")) {
                reltimeexp.valueLowerboundRel.minute = relation_value;
                reltimeexp.valueUpperboundRel.minute = relation_value;
            } else if (matchingLimitedExpression.correspondingTimePosition.get(0).equals("s")) {
                reltimeexp.valueLowerboundRel.second = relation_value;
                reltimeexp.valueUpperboundRel.second = relation_value;
            }
        }
        reltimeexp.positionStart -= matchingLimitedExpression.pattern.length();
    }

    /*
    　修飾語による規格化表現の補正処理。
      絶対表現と同じ処理を念のため書き加えたが、絶対表現と同じ修飾表現 + 相対時間表現は存在しないと考えられるので、恐らく必要ない。
    */


    private void doTimeAbout(ReltimeExpression reltimeexp) {

        // 「およそ1000年前」「2か月前頃」など
        final var tvl = reltimeexp.valueLowerboundRel;
        final var tvu = reltimeexp.valueUpperboundRel;
        final var targetTimePosition = this.normalizerUtility.identifyTimeDetail(reltimeexp.valueLowerboundRel);
        if (targetTimePosition.equals("y")) {
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
    private void doTimeZenhan(ReltimeExpression reltimeexp) {

        // 「18世紀前半」「1989年前半」「7月前半」「3日朝」など。
        // TODO : 「18世紀はじめ」などもzenhanに括ってしまっている。より細かく分類が行いたい場合は、hajime関数などを書いて処理
        final var tvl = reltimeexp.valueLowerboundAbs;
        final var tvu = reltimeexp.valueUpperboundAbs;
        final var targetTimePosition = this.normalizerUtility.identifyTimeDetail(reltimeexp.valueLowerboundAbs);
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


    private void doTimeKouhan(ReltimeExpression reltimeexp) {

        // 「18世紀後半」「1989年後半」「7月後半」など。
        // TODO : 「18世紀末」「3日夜」などもkouhanに括ってしまっている。
        final var tvl = reltimeexp.valueLowerboundAbs;
        final var tvu = reltimeexp.valueUpperboundAbs;
        final var targetTimePosition = this.normalizerUtility.identifyTimeDetail(reltimeexp.valueLowerboundAbs);
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


    private void doTimeNakaba(ReltimeExpression reltimeexp) {

        // 「18世紀中盤」「1989年中盤」「7月中盤」など。
        // TODO : 「18世紀なかば」「3日昼」などもnakabaに括ってしまっている。
        final var tvl = reltimeexp.valueLowerboundAbs;
        final var tvu = reltimeexp.valueUpperboundAbs;
        final var targetTimePosition = this.normalizerUtility.identifyTimeDetail(reltimeexp.valueLowerboundAbs);
        if (targetTimePosition.equals("y")) {
            if (tvl.year != tvu.year) {
                // 「18世紀中盤」のとき
                final var tmp = (tvu.year - tvl.year) / 4;
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


    private void doTimeJoujun(ReltimeExpression reltimeexp) {

        final var tvl = reltimeexp.valueLowerboundAbs;
        final var tvu = reltimeexp.valueUpperboundAbs;
        final var targetTimePosition = this.normalizerUtility.identifyTimeDetail(reltimeexp.valueLowerboundAbs);
        if (targetTimePosition.equals("m")) {
            tvl.day = 1;
            tvu.day = 10;
        }
    }


    private void doTimeTyujun(ReltimeExpression reltimeexp) {

        final var tvl = reltimeexp.valueLowerboundAbs;
        final var tvu = reltimeexp.valueUpperboundAbs;
        final var targetTimePosition = this.normalizerUtility.identifyTimeDetail(reltimeexp.valueLowerboundAbs);
        if (targetTimePosition.equals("m")) {
            tvl.day = 11;
            tvu.day = 20;
        }
    }


    private void doTimeGejun(ReltimeExpression reltimeexp) {

        final var tvl = reltimeexp.valueLowerboundAbs;
        final var tvu = reltimeexp.valueUpperboundAbs;
        final var targetTimePosition = this.normalizerUtility.identifyTimeDetail(reltimeexp.valueLowerboundAbs);
        if (targetTimePosition.equals("m")) {
            tvl.day = 21;
            tvu.day = 31;
        }
    }


    @Override
    public void reviseAnyTypeExpressionByNumberModifier(ReltimeExpression reltimeexp, NumberModifier numberModifier) {

        final var processType = numberModifier.processType;
        // or_overなどのタイプは、reltimeではexpression_jsonの方で記述されており、
        // これはrevise_reltimeexp_by_process_typeで処理されるので、ここでは処理しない。
        // （TODO : 辞書からありえない表現をのぞく）
        if (processType.equals("none")) {

        } else if (processType.equals("about")) {
            this.doTimeAbout(reltimeexp);
        } else if (processType.equals("zenhan")) {
            this.doTimeZenhan(reltimeexp);
        } else if (processType.equals("kouhan")) {
            this.doTimeKouhan(reltimeexp);
        } else if (processType.equals("nakaba")) {
            this.doTimeNakaba(reltimeexp);
        } else if (processType.equals("joujun")) {
            this.doTimeJoujun(reltimeexp);
        } else if (processType.equals("tyujun")) {
            this.doTimeTyujun(reltimeexp);
        } else if (processType.equals("gejun")) {
            this.doTimeGejun(reltimeexp);
        } else {
            reltimeexp.options.add(processType);
        }

    }


    @Override
    public void deleteNotAnyTypeExpression(List <ReltimeExpression> reltimeexps) {

        for (var i = 0; i < reltimeexps.size(); i++) {
            if (this.normalizerUtility.isNullTime(reltimeexps.get(i).valueLowerboundRel)
                    && this.normalizerUtility.isNullTime(reltimeexps.get(i).valueUpperboundRel)) {
                reltimeexps.remove(i);
                i--;
            }
        }
    }


    @Override
    public void fixByRangeExpression(StringBuilder uText, List <ReltimeExpression> reltimeexps) {

        for (var i = 0; i < reltimeexps.size() - 1; i++) {
            if (this.haveKaraSuffix(reltimeexps.get(i).options) && this.haveKaraPrefix(reltimeexps.get(i + 1).options)
                    && reltimeexps.get(i).positionEnd + 2 >= reltimeexps.get(i + 1).positionStart) {
                reltimeexps.get(i).valueUpperboundRel = reltimeexps.get(i + 1).valueUpperboundRel;
                reltimeexps.get(i).valueUpperboundAbs = reltimeexps.get(i + 1).valueUpperboundAbs;
                reltimeexps.get(i).positionEnd = reltimeexps.get(i + 1).positionEnd;
                reltimeexps.get(i).setOriginalExpressionFromPosition(uText);
                this.mergeOptions(reltimeexps.get(i).options, reltimeexps.get(i + 1).options);
                reltimeexps.remove(i + 1);
            }
        }
    }


    @Override
    public void convertNumbersToAnyTypeExpressions(List <NNumber> numbers, List <ReltimeExpression> anyTypeExpressions) {

        for (final NNumber number : numbers) {

            final var anyTypeExpression = new ReltimeExpression(number);
            anyTypeExpression.originalExpression = number.originalExpression;
            anyTypeExpression.positionStart = number.positionStart;
            anyTypeExpression.positionEnd = number.positionEnd;
            anyTypeExpressions.add(anyTypeExpression);
        }
    }

}
