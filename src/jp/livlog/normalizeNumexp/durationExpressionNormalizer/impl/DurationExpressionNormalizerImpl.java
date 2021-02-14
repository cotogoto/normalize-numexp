package jp.livlog.normalizeNumexp.durationExpressionNormalizer.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import jp.livlog.normalizeNumexp.durationExpressionNormalizer.DurationExpression;
import jp.livlog.normalizeNumexp.durationExpressionNormalizer.DurationExpressionNormalizer;
import jp.livlog.normalizeNumexp.durationExpressionNormalizer.LimitedDurationExpression;
import jp.livlog.normalizeNumexp.share.NNumber;
import jp.livlog.normalizeNumexp.share.NTime;
import jp.livlog.normalizeNumexp.share.NumberModifier;
import jp.livlog.normalizeNumexp.share.RefObject;
import jp.livlog.normalizeNumexp.share.Symbol;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DurationExpressionNormalizerImpl extends DurationExpressionNormalizer {

    public DurationExpressionNormalizerImpl(String language) {

        super(language);
    }


    @Override
    public void init() {

        this.loadFromDictionaries("duration_expression_json.txt", "duration_prefix_counter_json.txt", "duration_prefix_json.txt",
                "duration_suffix_json.txt");
    }


    @Override
    public void normalizeNumber(StringBuilder uText, List <NNumber> numbers) {

        this.NN.process(uText.toString(), numbers);
    }


    @SuppressWarnings ("unchecked")
    @Override
    public void loadFromDictionary1(String dictionaryPath, List <LimitedDurationExpression> loadTarget) {

        loadTarget.clear();

        final var reader = this.fileLoad(dictionaryPath);

        final var gson = new Gson();
        final var listType = new TypeToken <HashMap <String, Object>>() {
        }.getType();
        LimitedDurationExpression expression = null;
        try (var br = new BufferedReader(reader)) {
            String line;
            while ((line = br.readLine()) != null) {
                final var map = (HashMap <String, Object>) gson.fromJson(line, listType);
                expression = new LimitedDurationExpression();
                expression.pattern = (String) map.get("pattern");
                expression.correspondingTimePosition = (List <String>) map.get("corresponding_time_position");
                expression.processType = (List <String>) map.get("process_type");
                expression.ordinary = (Boolean) map.get("ordinary");
                expression.option = (String) map.get("option");
                loadTarget.add(expression);
            }
        } catch (final IOException e) {
            DurationExpressionNormalizerImpl.log.error(e.getMessage(), e);
        }
    }


    private void setTime(DurationExpression durationexp, String corresponding_time_position, DurationExpression integrate_durationexp) {

        if (corresponding_time_position.equals("y")) {
            durationexp.valueLowerbound.year = integrate_durationexp.orgValueLowerbound;
            durationexp.valueUpperbound.year = integrate_durationexp.orgValueUpperbound;
        } else if (corresponding_time_position.equals("m")) {
            durationexp.valueLowerbound.month = integrate_durationexp.orgValueLowerbound;
            durationexp.valueUpperbound.month = integrate_durationexp.orgValueUpperbound;
        } else if (corresponding_time_position.equals("d")) {
            durationexp.valueLowerbound.day = integrate_durationexp.orgValueLowerbound;
            durationexp.valueUpperbound.day = integrate_durationexp.orgValueUpperbound;
        } else if (corresponding_time_position.equals("h")) {
            durationexp.valueLowerbound.hour = integrate_durationexp.orgValueLowerbound;
            durationexp.valueUpperbound.hour = integrate_durationexp.orgValueUpperbound;
        } else if (corresponding_time_position.equals("mn")) {
            durationexp.valueLowerbound.minute = integrate_durationexp.orgValueLowerbound;
            durationexp.valueUpperbound.minute = integrate_durationexp.orgValueUpperbound;
        } else if (corresponding_time_position.equals("s")) {
            durationexp.valueLowerbound.second = integrate_durationexp.orgValueLowerbound;
            durationexp.valueUpperbound.second = integrate_durationexp.orgValueUpperbound;
        } else if (corresponding_time_position.equals("seiki")) {
            durationexp.valueLowerbound.year = integrate_durationexp.orgValueLowerbound * 100;
            durationexp.valueUpperbound.year = integrate_durationexp.orgValueUpperbound * 100;
        } else if (corresponding_time_position.equals("w")) {
            durationexp.valueLowerbound.day = integrate_durationexp.orgValueLowerbound * 7;
            durationexp.valueUpperbound.day = integrate_durationexp.orgValueUpperbound * 7;
        }
    }


    private void doOptionHan(DurationExpression durationexp, String correspondingTimePosition) {

        if (correspondingTimePosition.equals("y")) {
            durationexp.valueLowerbound.year += 0.5;
            durationexp.valueUpperbound.year += 0.5;
        } else if (correspondingTimePosition.equals("m")) {
            durationexp.valueLowerbound.month += 0.5;
            durationexp.valueUpperbound.month += 0.5;
        } else if (correspondingTimePosition.equals("d")) {
            durationexp.valueLowerbound.day += 0.5;
            durationexp.valueUpperbound.day += 0.5;
        } else if (correspondingTimePosition.equals("h")) {
            durationexp.valueLowerbound.hour += 0.5;
            durationexp.valueUpperbound.hour += 0.5;
        } else if (correspondingTimePosition.equals("mn")) {
            durationexp.valueLowerbound.minute += 0.5;
            durationexp.valueUpperbound.minute += 0.5;
        } else if (correspondingTimePosition.equals("s")) {
            durationexp.valueLowerbound.second += 0.5;
            durationexp.valueUpperbound.second += 0.5;
        } else if (correspondingTimePosition.equals("seiki")) {
            durationexp.valueLowerbound.year += 50;
            durationexp.valueUpperbound.year += 50;
        }
    }


    private void reviseDurationexpByProcessType(DurationExpression durationexp, String processType,
            LimitedDurationExpression matchingLimitedDurationExpression) {

        if (processType.equals("han")) {
            if (matchingLimitedDurationExpression.correspondingTimePosition.isEmpty()) {
                return;
            }

            final var correspondingTimePosition = matchingLimitedDurationExpression.correspondingTimePosition
                    .get(matchingLimitedDurationExpression.correspondingTimePosition.size() - 1);
            this.doOptionHan(durationexp, correspondingTimePosition);
        }
    }


    @Override
    public void reviseAnyTypeExpressionByMatchingLimitedExpression(List <DurationExpression> durationexps, RefObject <Integer> expressionId,
            LimitedDurationExpression matchingLimitedDurationExpression) {

        final var integratedDurationexpId = expressionId.argValue + matchingLimitedDurationExpression.totalNumberOfPlaceHolder;
        durationexps.get(expressionId.argValue).positionEnd = durationexps.get(integratedDurationexpId).positionEnd
                + matchingLimitedDurationExpression.lengthOfStringsAfterFinalPlaceHolder;
        for (var i = 0; i < matchingLimitedDurationExpression.correspondingTimePosition.size(); i++) {
            this.setTime(durationexps.get(expressionId.argValue), matchingLimitedDurationExpression.correspondingTimePosition.get(i),
                    durationexps.get(expressionId.argValue + i));
        }
        for (var i = 0; i < matchingLimitedDurationExpression.processType.size(); i++) {
            this.reviseDurationexpByProcessType(durationexps.get(expressionId.argValue), matchingLimitedDurationExpression.processType.get(i),
                    matchingLimitedDurationExpression);
        }
        durationexps.get(expressionId.argValue).ordinary = matchingLimitedDurationExpression.ordinary;

        // durationexps.erase(durationexps.iterator() + expressionId.argValue + 1,
        // durationexps.iterator() + expressionId.argValue + 1 + matchingLimitedDurationExpression.total_number_of_place_holder);

        var i = 0;
        final var min = expressionId.argValue + 1;
        final var max = expressionId.argValue + matchingLimitedDurationExpression.totalNumberOfPlaceHolder;
        final var it = durationexps.iterator();
        while (it.hasNext()) {
            it.next();
            if (min <= i && i <= max) {
                it.remove();
            }
            i++;
        }
    }


    @Override
    public void reviseAnyTypeExpressionByMatchingPrefixCounter(DurationExpression anyTypeExpression,
            LimitedDurationExpression matching_limited_expression) {

        // 持続時間にprefix_counterは存在しない（今のところ）
    }

    /*
     * 修飾語による規格化表現の補正処理。
     */


    private void doTimeAbout(DurationExpression durationexp) {

        final var tvl = durationexp.valueLowerbound;
        final var tvu = durationexp.valueUpperbound;
        final var targetTimePosition = this.normalizerUtility.identifyTimeDetail(durationexp.valueLowerbound);
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


    private void doTimeKyou(DurationExpression durationexp) {

        final var tvu = durationexp.valueUpperbound;
        final var targetTimePosition = this.normalizerUtility.identifyTimeDetail(durationexp.valueLowerbound);
        if (targetTimePosition.equals("y")) {
            tvu.year += 5;
        } else if (targetTimePosition.equals("m")) {
            tvu.month += 1;
        } else if (targetTimePosition.equals("d")) {
            tvu.day += 1;
        } else if (targetTimePosition.equals("h")) {
            tvu.hour += 1;
        } else if (targetTimePosition.equals("mn")) {
            tvu.minute += 5;
        } else if (targetTimePosition.equals("s")) {
            tvu.second += 5;
        }
    }


    private void doTimeJaku(DurationExpression durationexp) {

        final var tvl = durationexp.valueLowerbound;
        final var targetTimePosition = this.normalizerUtility.identifyTimeDetail(durationexp.valueLowerbound);
        if (targetTimePosition.equals("y")) {
            tvl.year -= 5;
        } else if (targetTimePosition.equals("m")) {
            tvl.month -= 1;
        } else if (targetTimePosition.equals("d")) {
            tvl.day -= 1;
        } else if (targetTimePosition.equals("h")) {
            tvl.hour -= 1;
        } else if (targetTimePosition.equals("mn")) {
            tvl.minute -= 5;
        } else if (targetTimePosition.equals("s")) {
            tvl.second -= 5;
        }
    }


    @Override
    public void reviseAnyTypeExpressionByNumberModifier(DurationExpression durationexp, NumberModifier numberModifier) {

        final var processType = numberModifier.processType;
        if (processType.equals("or_over")) {
            durationexp.valueUpperbound = new NTime(-Symbol.INFINITY);
        } else if (processType.equals("or_less")) {
            durationexp.valueLowerbound = new NTime(Symbol.INFINITY);
        } else if (processType.equals("over")) {
            durationexp.valueUpperbound = new NTime(-Symbol.INFINITY);
            durationexp.includeLowerbound = false;
        } else if (processType.equals("less")) {
            durationexp.valueLowerbound = new NTime(Symbol.INFINITY);
            durationexp.includeUpperbound = false;
        } else if (processType.equals("ordinary")) {
            // TODO : 序数は絶対時間として扱う？持続時間として扱う？ 未定
            durationexp.ordinary = true;
        } else if (processType.equals("none")) {

        } else if (processType.equals("per")) {
            // TODO : 「1日毎」など? どんな処理をするか未定。
        } else if (processType.equals("dai")) {
            // TODO : 「1秒台」など。 どんな処理をするか未定。 これは持続時間？（ではなさそう）
        } else if (processType.equals("about")) {
            this.doTimeAbout(durationexp);
        } else if (processType.equals("kyou")) {
            this.doTimeKyou(durationexp);
        } else if (processType.equals("jaku")) {
            this.doTimeJaku(durationexp);
        } else if (processType.equals("made")) {
            if (durationexp.valueLowerbound == durationexp.valueUpperbound) {
                durationexp.valueLowerbound = new NTime(Symbol.INFINITY);
            } else {

            }
        } else {
            durationexp.options.add(processType);
        }
    }


    @Override
    public void deleteNotAnyTypeExpression(List <DurationExpression> durationexps) {

        for (var i = 0; i < durationexps.size(); i++) {
            if (this.normalizerUtility.isNullTime(durationexps.get(i).valueLowerbound)
                    && this.normalizerUtility.isNullTime(durationexps.get(i).valueUpperbound)) {
                durationexps.remove(i);
                i--;
            }
        }
    }


    @Override
    public void fixByRangeExpression(StringBuilder utext, List <DurationExpression> durationexps) {

        for (var i = 0; i < durationexps.size() - 1; i++) {
            if (this.haveKaraSuffix(durationexps.get(i).options) && this.haveKaraPrefix(durationexps.get(i + 1).options)
                    && durationexps.get(i).positionEnd + 2 >= durationexps.get(i + 1).positionStart) {
                durationexps.get(i).valueUpperbound = durationexps.get(i + 1).valueUpperbound;
                durationexps.get(i).positionEnd = durationexps.get(i + 1).positionEnd;
                durationexps.get(i).setOriginalExpressionFromPosition(utext);
                this.mergeOptions(durationexps.get(i).options, durationexps.get(i + 1).options);
                durationexps.remove(i + 1);
            }
        }
    }


    @Override
    public void convertNumbersToAnyTypeExpressions(List <NNumber> numbers, List <DurationExpression> anyTypeExpressions) {

        for (final NNumber number : numbers) {

            final var anyTypeExpression = new DurationExpression(number);
            anyTypeExpression.originalExpression = number.originalExpression;
            anyTypeExpression.positionStart = number.positionStart;
            anyTypeExpression.positionEnd = number.positionEnd;
            anyTypeExpressions.add(anyTypeExpression);
        }
    }

}
