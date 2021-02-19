package jp.livlog.numexp.numericalExpressionNormalizer.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import jp.livlog.numexp.numericalExpressionNormalizer.Counter;
import jp.livlog.numexp.numericalExpressionNormalizer.NumericalExpression;
import jp.livlog.numexp.numericalExpressionNormalizer.NumericalExpressionNormalizer;
import jp.livlog.numexp.share.NNumber;
import jp.livlog.numexp.share.NumberModifier;
import jp.livlog.numexp.share.RefObject;
import jp.livlog.numexp.share.NumexpSymbol;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NumericalExpressionNormalizerImpl extends NumericalExpressionNormalizer {

    public NumericalExpressionNormalizerImpl(String language) {

        super(language);
    }


    @Override
    public void init() {

        this.loadFromDictionaries("num_counter_json.txt", "num_prefix_counter_json.txt", "num_prefix_json.txt", "num_suffix_json.txt");
    }


    @Override
    public void normalizeNumber(StringBuilder uText, List <NNumber> numbers) {

        this.NN.process(uText.toString(), numbers);
    }


    @SuppressWarnings ("unchecked")
    @Override
    public void loadFromDictionary1(String dictionaryPath, List <Counter> loadTarget) {

        loadTarget.clear();

        final var reader = this.fileLoad(dictionaryPath);

        final var gson = new Gson();
        final var listType = new TypeToken <HashMap <String, Object>>() {
        }.getType();
        Counter expression = null;
        try (var br = new BufferedReader(reader)) {
            String line;
            while ((line = br.readLine()) != null) {
                // {"pattern":"￥", "counter":"円", "SI_prefix":0, "optional_power_of_ten":0, "ordinary":false, "option":"counter"}
                final var map = (HashMap <String, Object>) gson.fromJson(line, listType);
                expression = new Counter();
                expression.pattern = (String) map.get("pattern");
                expression.counter = (String) map.get("counter");
                expression.SIprefix = new BigDecimal((double) map.get("SI_prefix")).intValue();
                expression.optionalPowerOfTen = new BigDecimal((double) map.get("optional_power_of_ten")).intValue();
                expression.ordinary = (Boolean) map.get("ordinary");
                expression.option = (String) map.get("option");
                loadTarget.add(expression);
            }
        } catch (final IOException e) {
            NumericalExpressionNormalizerImpl.log.error(e.getMessage(), e);
        }
    }


    private void multiplyNumexpValue(NumericalExpression numexp, double x) {

        numexp.valueLowerbound *= x;
        numexp.valueUpperbound *= x;
    }


    private void doOptionWari(List <NumericalExpression> numexps, int expressionId, final Counter matchingLimitedExpression) {

        final var uPattern = new StringBuilder(matchingLimitedExpression.pattern);
        numexps.get(expressionId).positionEnd += uPattern.length();
        numexps.get(expressionId).counter = "%";
        numexps.get(expressionId).ordinary = false;

        // set_value
        var value = 0D;
        for (var i = 0; i < uPattern.length(); i += 2) {
            final var chr = uPattern.charAt(i);
            switch (chr) {
                case '割':
                    value += numexps.get(expressionId + i / 2).valueLowerbound * 10;
                    break;
                case '分':
                    value += numexps.get(expressionId + i / 2).valueLowerbound * 1;
                    break;
                case '厘':
                    value += numexps.get(expressionId + i / 2).valueLowerbound * 0.1;
                    break;
                default:
                    break;
            }
        }
        numexps.get(expressionId).valueLowerbound = value;
        numexps.get(expressionId).valueUpperbound = value;

        // erase merged numexps
        for (var i = 2; i < uPattern.length(); i += 2) {
            numexps.remove(expressionId + 1);
        }
    }


    @Override
    public void reviseAnyTypeExpressionByMatchingLimitedExpression(List <NumericalExpression> numexps, RefObject <Integer> expressionId,
            Counter matchingLimitedExpression) {

        // 特殊なタイプをここで例外処理
        if (matchingLimitedExpression.option.equals("wari")) {
            this.doOptionWari(numexps, expressionId.argValue, matchingLimitedExpression);
            return;
        }
        // TODO : 今のところ特殊なタイプは分数しかないので、とりあえず保留

        numexps.get(expressionId.argValue).positionEnd += matchingLimitedExpression.pattern.length();
        numexps.get(expressionId.argValue).counter = matchingLimitedExpression.counter;
        this.multiplyNumexpValue(numexps.get(expressionId.argValue), Math.pow(10, matchingLimitedExpression.SIprefix));
        this.multiplyNumexpValue(numexps.get(expressionId.argValue), Math.pow(10, matchingLimitedExpression.optionalPowerOfTen));
        numexps.get(expressionId.argValue).ordinary = matchingLimitedExpression.ordinary;

    }


    @Override
    public void reviseAnyTypeExpressionByMatchingPrefixCounter(NumericalExpression numexp, Counter matchingLimitedExpression) {

        if (matchingLimitedExpression.option.equals("counter")) {
            numexp.positionStart -= matchingLimitedExpression.pattern.length();
            numexp.counter = matchingLimitedExpression.counter;
            this.multiplyNumexpValue(numexp, Math.pow(10, matchingLimitedExpression.SIprefix));
            this.multiplyNumexpValue(numexp, Math.pow(10, matchingLimitedExpression.optionalPowerOfTen));
            numexp.ordinary = matchingLimitedExpression.ordinary;
        } else if (matchingLimitedExpression.option.equals("add_suffix_counter")) {
            if (numexp.counter.isEmpty()) {
                return; // TODO : 単位が空の場合、追加は行わない？
            }
            numexp.positionStart -= matchingLimitedExpression.pattern.length();
            numexp.counter += matchingLimitedExpression.counter;
        }

    }


    @Override
    public void reviseAnyTypeExpressionByNumberModifier(NumericalExpression numexp, NumberModifier numberModifier) {

        final var processType = numberModifier.processType;
        /*
         * 「約」などのNumberModifierの処理を行う。
         */
        if (processType.equals("or_over")) {
            numexp.valueUpperbound = NumexpSymbol.INFINITY;
        } else if (processType.equals("or_less")) {
            numexp.valueLowerbound = -NumexpSymbol.INFINITY;
        } else if (processType.equals("over")) {
            numexp.valueUpperbound = NumexpSymbol.INFINITY;
            numexp.includeLowerbound = false;
        } else if (processType.equals("less")) {
            numexp.valueLowerbound = -NumexpSymbol.INFINITY;
            numexp.includeUpperbound = false;
        } else if (processType.equals("dai")) {
            // TODO : どんな処理をするか未定。。 該当する事例は「30代」「9秒台」のみ？
        } else if (processType.equals("ordinary")) {
            numexp.ordinary = true;
        } else if (processType.equals("han")) {
            numexp.valueLowerbound += 0.5;
            numexp.valueUpperbound += 0.5;
        } else if (processType.charAt(0) == '/') { // /hour, /minなど
            numexp.counter += processType;
        } else if (processType.equals("none")) {

        } else if (processType.equals("per")) {
            // TODO : どんな処理をするか未定。 該当する事例は「1ページ毎」など。
        } else if (processType.equals("about")) {
            numexp.valueLowerbound *= 0.7;
            numexp.valueUpperbound *= 1.3;
        } else if (processType.equals("kyou")) {
            numexp.valueUpperbound *= 1.6;
        } else if (processType.equals("jaku")) {
            numexp.valueLowerbound *= 0.5;
        } else if (processType.equals("made")) {
            if (numexp.valueLowerbound == numexp.valueUpperbound) {
                numexp.valueLowerbound = -NumexpSymbol.INFINITY;
            } else {

            }
        } else {
            numexp.options.add(processType);
        }
    }


    @Override
    public void deleteNotAnyTypeExpression(List <NumericalExpression> numexps) {

        for (var i = 0; i < numexps.size(); i++) {
            if (numexps.get(i).counter.isEmpty()) {
                numexps.remove(i);
                i--;
            }
        }
    }


    private void deleteAfterSlash(RefObject <String> ustr) {

        if (ustr.argValue.indexOf("/") == -1) {
            return;
        }
        ustr.argValue = ustr.argValue.substring(0, ustr.argValue.indexOf("/"));
    }


    private boolean suffixMatchCounter(String counter1, String counter2) {

        // 単位が一致しているかどうかを判断する。
        // 「時速50km〜60km」のような事例に対応する（前者は[50km/h], 後者は[60km]と規格化されており、完全一致ではマッチしない）ために、スラッシュより前の単位が一致するかどうかで判断する
        final var tempRefCounter1 = new RefObject <>(counter1);
        this.deleteAfterSlash(tempRefCounter1);
        counter1 = tempRefCounter1.argValue;
        final var tempRefCounter2 = new RefObject <>(counter2);
        this.deleteAfterSlash(tempRefCounter2);
        counter2 = tempRefCounter2.argValue;
        return counter1.equals(counter2);
    }


    @Override
    public void fixByRangeExpression(StringBuilder uText, List <NumericalExpression> numexps) {

        for (var i = 0; i < numexps.size() - 1; i++) {
            if (this.haveKaraSuffix(numexps.get(i).options) && this.haveKaraPrefix(numexps.get(i + 1).options)
                    && numexps.get(i).positionEnd + 2 >= numexps.get(i + 1).positionStart) {
                if (!this.suffixMatchCounter(numexps.get(i).counter, numexps.get(i + 1).counter)) {
                    continue;
                }
                numexps.get(i).valueUpperbound = numexps.get(i + 1).valueUpperbound;
                numexps.get(i).positionEnd = numexps.get(i + 1).positionEnd;
                numexps.get(i).setOriginalExpressionFromPosition(uText);
                // memo :単位のマージは、必ずiの方がi+1よりも長いので、する必要なし
                this.mergeOptions(numexps.get(i).options, numexps.get(i + 1).options);
                numexps.remove(i + 1);
            }
        }
    }


    @Override
    public void convertNumbersToAnyTypeExpressions(List <NNumber> numbers, List <NumericalExpression> anyTypeExpressions) {

        for (final NNumber number : numbers) {

            final var anyTypeExpression = new NumericalExpression(number);
            anyTypeExpression.originalExpression = number.originalExpression;
            anyTypeExpression.positionStart = number.positionStart;
            anyTypeExpression.positionEnd = number.positionEnd;
            anyTypeExpressions.add(anyTypeExpression);
        }
    }
}
