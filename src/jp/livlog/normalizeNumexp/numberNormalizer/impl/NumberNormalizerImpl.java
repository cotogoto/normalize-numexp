package jp.livlog.normalizeNumexp.numberNormalizer.impl;

import java.util.ArrayList;
import java.util.List;

import jp.livlog.normalizeNumexp.numberNormalizer.InfNumberConverterTemplate;
import jp.livlog.normalizeNumexp.numberNormalizer.NumberNormalizer;
import jp.livlog.normalizeNumexp.share.NNumber;
import jp.livlog.normalizeNumexp.share.RefObject;

public class NumberNormalizerImpl extends NumberNormalizer {

    public NumberNormalizerImpl(String language) {

        super(language);
    }


    @Override
    public void process(String text, List <NNumber> numbers) {

        // 初期化
        numbers.clear();

        // 入力文inputに含まれる数の表記を抽出
        this.NE.extractNumber(text, numbers);

        // カンマの処理を行う
        this.joinNumbersByComma(text, numbers);

        // それぞれの数の表記を、数に変換
        if (this.language.equals("ja")) {
            this.NC = new JapaneseNumberConverterImpl(this.digitUtility);
            this.convertNumber(this.NC, numbers);
        } else if (this.language.equals("zh")) {
            this.NC = new ChineseNumberConverterImpl(this.digitUtility);
            this.convertNumber(this.NC, numbers);
        } else {
            this.NC = new ArabicNumberConverterImpl(this.digitUtility);
            this.convertNumber(this.NC, numbers);
        }

        // 「数万」などの処理を行う
        // fix_symbolとマージしたいが、下のような処理が必要なので、うまくいかない。なんとかマージしたい。
        this.fixNumbersBySu(text, numbers);

        // 「京」「万」など「万」以上の桁区切り文字しかないものを削除する。
        // 高速化のため最初から候補から除外したいが、「数万」などの処理のために必要。
        // NE側で「数」に関する判定を行えば解決するが、全体像が見えにくくなるので、ひとまず行わない。
        this.removeOnlyKansujiKuraiMan(numbers);

        // 記号の処理を行う
        this.SF.fixNumbersBySymbol(text, numbers);

        // 不要なデータ除外
        this.removeUnnecessaryData(text, numbers);
    }


    @Override
    public void processDontFixBySymbol(String text, List <NNumber> numbers) {

        // 初期化
        numbers.clear();

        // 入力文inputに含まれる数の表記を抽出
        this.NE.extractNumber(text, numbers);

        // それぞれの数の表記を、数に変換
        if (this.language.equals("ja")) {
            this.NC = new JapaneseNumberConverterImpl(this.digitUtility);
            this.convertNumber(this.NC, numbers);
        } else if (this.language.equals("zh")) {
            this.NC = new ChineseNumberConverterImpl(this.digitUtility);
            this.convertNumber(this.NC, numbers);
        } else {
            this.NC = new ArabicNumberConverterImpl(this.digitUtility);
            this.convertNumber(this.NC, numbers);
        }

        // 「京」「万」など単独のものを削除する（ここで処理を行わないと、「数万」などに対応できない）
        this.removeOnlyKansujiKuraiMan(numbers);

        // 不要なデータ除外
        this.removeUnnecessaryData(text, numbers);
    }


    private void convertNumber(InfNumberConverterTemplate NC, List <NNumber> numbers) {

        for (final NNumber number : numbers) {
            final var valueLowerbound = new RefObject <>(number.valueLowerbound);
            // final var numberType = new RefObject <>(number.notationType.getValue());

            NC.convertNumber(number.originalExpression, valueLowerbound, number.notationType);
            number.valueLowerbound = valueLowerbound.argValue;
            number.valueUpperbound = valueLowerbound.argValue;
        }
    }


    private boolean isOnlyKansujiKuraiMan(String originalExpression) {

        for (var i = 0; i < originalExpression.length(); i++) {
            if (!this.digitUtility.isKansujiKuraiMan(originalExpression.charAt(i))) {
                return false;
            }
        }
        return true;
    }


    private void removeOnlyKansujiKuraiMan(List <NNumber> numbers) {

        for (var i = numbers.size() - 1; i >= 0; i--) {
            if (this.isOnlyKansujiKuraiMan(numbers.get(i).originalExpression)) {
                numbers.remove(i);
            }
        }
    }


    private void removeUnnecessaryData(String text, List <NNumber> numbers) {

        final var temp = new StringBuilder(text);
        final List <NNumber> wkNumbers = new ArrayList <>();
        var positionEnd = 0;
        for (final NNumber number : numbers) {
            final var a = temp.indexOf(number.originalExpression);
            if (a > -1 && positionEnd <= number.positionStart) {
                final var b = a + number.originalExpression.length();
                temp.delete(a, b);
                wkNumbers.add(number);
            }
            positionEnd = number.positionEnd;
        }
        numbers.clear();
        numbers.addAll(wkNumbers);
    }


    private void fixPrefixSu(final StringBuilder uText, List <NNumber> numbers, int i) {

        // 「数十万円」「数万円」「数十円」といった表現の処理を行う。
        if (numbers.get(i).positionStart == 0) {
            return;
        }
        if (uText.charAt(numbers.get(i).positionStart - 1) != '数') {
            return;
        }

        // 数の範囲の操作
        numbers.get(i).valueUpperbound *= 9;

        // 統合処理
        numbers.get(i).positionStart--;
        numbers.get(i).originalExpression = "数" + numbers.get(i).originalExpression;
    }


    private void fixIntermediateSu(final StringBuilder uText, List <NNumber> numbers, int i) {

        // 「十数万円」といった表現の処理を行う
        if (numbers.size() - 1 <= i) {
            return;
        }
        if (numbers.get(i).positionEnd != numbers.get(i + 1).positionStart - 1) {
            return;
        }
        if (uText.charAt(numbers.get(i).positionEnd) != '数') {
            return;
        }

        // 数の範囲の操作
        // numbers[i].valueを、numbers[i+1].valueのスケールに合わせる
        while (true) {
            if (numbers.get(i + 1).valueLowerbound < numbers.get(i).valueLowerbound) {
                break;
            }
            numbers.get(i).valueLowerbound *= Math.pow(10, 4);
            if (numbers.get(i).valueLowerbound <= 0) {
                return; // 0数万とかのとき
            }
        }
        numbers.get(i).valueUpperbound = numbers.get(i).valueLowerbound;
        // numbers[i+1]に「数」の処理を行う
        numbers.get(i + 1).valueUpperbound *= 9;
        // 二つの数の範囲を統合
        numbers.get(i).valueLowerbound += numbers.get(i + 1).valueLowerbound;
        numbers.get(i).valueUpperbound += numbers.get(i + 1).valueUpperbound;

        // 統合処理
        numbers.get(i).positionEnd = numbers.get(i + 1).positionEnd;
        numbers.get(i).originalExpression += "数";
        numbers.get(i).originalExpression += numbers.get(i + 1).originalExpression;
        numbers.remove(i + 1);
        i--;
    }


    private void fixSuffixSu(final StringBuilder uText, List <NNumber> numbers, int i) {

        // 「十数円」の処理を行う（これ以外に、suffixに数がくるケースはない。
        if (numbers.get(i).positionEnd == uText.length()) {
            return;
        }
        if (uText.charAt(i) != '数') {
            return;
        }
        numbers.get(i).valueUpperbound += 9;
        numbers.get(i).valueLowerbound += 1;
        numbers.get(i).originalExpression += "数";
        numbers.get(i).positionEnd++;
    }


    private void fixNumbersBySu(final String text, List <NNumber> numbers) {

        final var uText = new StringBuilder(text);
        for (var i = 0; i < numbers.size(); i++) {
            this.fixPrefixSu(uText, numbers, i);
            this.fixIntermediateSu(uText, numbers, i);
            this.fixSuffixSu(uText, numbers, i);
        }
    }


    private boolean suffixIsArabic(final StringBuilder numberString) {

        return numberString.length() > 0
                && this.digitUtility.isArabic(numberString.charAt(numberString.length() - 1));
    }


    private boolean prefix3digitIsArabic(final StringBuilder numberString) {

        return numberString.length() > 2
                && this.digitUtility.isArabic(numberString.charAt(0))
                && this.digitUtility.isArabic(numberString.charAt(1))
                && this.digitUtility.isArabic(numberString.charAt(2));
    }


    private boolean isValidCommaNotation(final StringBuilder numberString1, StringBuilder numberString2) {

        return this.suffixIsArabic(numberString1)
                && this.prefix3digitIsArabic(numberString2)
                && (numberString2.length() == 3
                        || !this.digitUtility.isArabic(numberString2.charAt(3)));
    }


    private void joinNumbersByComma(final String text, List <NNumber> numbers) {

        // カンマ表記を統合する。カンマは「3,000,000」のように3桁ごとに区切っているカンマしか数のカンマ表記とは認めない（「29,30」のような表記は認めない）
        for (var i = numbers.size() - 1; i > 0; i--) {
            final var a = numbers.get(i - 1).positionEnd;
            final var b = numbers.get(i).positionStart - 1;
            if (a != b) {
                continue;
            }
            final var ucharIntermediate = text.toCharArray()[numbers.get(i - 1).positionEnd];
            if (!this.digitUtility.isComma(ucharIntermediate)) {
                continue;
            }
            if (!this.isValidCommaNotation(new StringBuilder(numbers.get(i - 1).originalExpression),
                    new StringBuilder(numbers.get(i).originalExpression))) {
                continue;
            }

            numbers.get(i - 1).positionEnd = numbers.get(i).positionEnd;
            numbers.get(i - 1).originalExpression += ucharIntermediate;
            numbers.get(i - 1).originalExpression += numbers.get(i).originalExpression;
            numbers.remove(i);
        }
    }

}
