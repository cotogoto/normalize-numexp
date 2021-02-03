package jp.livlog.normalizeNumexp.numericalExpressionNormalizer.impl;

import java.util.ArrayList;
import java.util.List;

import jp.livlog.normalizeNumexp.numericalExpressionNormalizer.NumberNormalizer;
import jp.livlog.normalizeNumexp.share.ENotationType;
import jp.livlog.normalizeNumexp.share.NNumber;
import jp.livlog.normalizeNumexp.share.Pair;
import jp.livlog.normalizeNumexp.share.RefObject;

public class NumberNormalizerImpl extends NumberNormalizer {

    private void updateNotationType(final char uc, List <ENotationType> notationType) {

        if (NumberNormalizer.digitUtility.isHankakusuji(uc)) {
            // notationType.argValue |= ENotationType.HANKAKU.getValue();
            notationType.add(ENotationType.HANKAKU);
        } else if (NumberNormalizer.digitUtility.isZenkakusuji(uc)) {
            // notationType.argValue |= ENotationType.ZENKAKU.getValue();
            notationType.add(ENotationType.ZENKAKU);
        } else if (NumberNormalizer.digitUtility.isKansuji09(uc)) {
            // notationType.argValue |= ENotationType.KANSUJI_09.getValue();
            notationType.add(ENotationType.KANSUJI_09);
        } else if (NumberNormalizer.digitUtility.isKansujiKuraiSen(uc)) {
            // notationType.argValue |= ENotationType.KANSUJI_KURAI_SEN.getValue();
            notationType.add(ENotationType.KANSUJI_KURAI_SEN);
        } else if (NumberNormalizer.digitUtility.isKansujiKuraiMan(uc)) {
            // notationType.argValue |= ENotationType.KANSUJI_KURAI_MAN.getValue();
            notationType.add(ENotationType.KANSUJI_KURAI_MAN);
        }
    }


    private boolean includeMask(final List <ENotationType> mask, final List <ENotationType> submask) {

        if (mask.size() > 1) {
            if (mask.get(0).getValue() == submask.get(0).getValue() && mask.get(1).getValue() == submask.get(1).getValue()) {
                return true;
            } else if (mask.get(0).getValue() == submask.get(1).getValue() && mask.get(1).getValue() == submask.get(0).getValue()) {
                return true;
            }
        }

        return false;
    }


    private boolean suffixIsArabic(final String numberString) {

        return numberString.length() > 0
                && NumberNormalizer.digitUtility
                        .isArabic(NumberNormalizer.digitUtility.getNumberStringCharacter(numberString, numberString.length() - 1));
    }


    private boolean prefix3digitIsArabic(final String numberString) {

        return numberString.length() > 2
                && NumberNormalizer.digitUtility.isArabic(NumberNormalizer.digitUtility.getNumberStringCharacter(numberString, 0))
                && NumberNormalizer.digitUtility.isArabic(NumberNormalizer.digitUtility.getNumberStringCharacter(numberString, 1))
                && NumberNormalizer.digitUtility.isArabic(NumberNormalizer.digitUtility.getNumberStringCharacter(numberString, 2));
    }


    private boolean isValidCommaNotation(final String numberString1, final String numberString2) {

        return this.suffixIsArabic(numberString1)
                && this.prefix3digitIsArabic(numberString2)
                && (numberString2.length() == 3
                        || !NumberNormalizer.digitUtility.isArabic(NumberNormalizer.digitUtility.getNumberStringCharacter(numberString2, 3)));
    }


    private void joinNumbersByComma(final String text, List <NNumber> numbers) {

        // カンマ表記を統合する。カンマは「3,000,000」のように3桁ごとに区切っているカンマしか数のカンマ表記とは認めない（「29,30」のような表記は認めない）
        for (var i = numbers.size() - 1; i > 0; i--) {
            if (numbers.get(i - 1).positionEnd != numbers.get(i).positionStart - 1) {
                continue;
            }
            final var ucharIntermediate = text.toCharArray()[numbers.get(i - 1).positionEnd];
            if (!NumberNormalizer.digitUtility.isComma(ucharIntermediate)) {
                continue;
            }
            if (!this.isValidCommaNotation(numbers.get(i - 1).originalExpression,
                    numbers.get(i).originalExpression)) {
                continue;
            }

            numbers.get(i - 1).positionEnd = numbers.get(i).positionEnd;
            numbers.get(i - 1).originalExpression += ucharIntermediate;
            numbers.get(i - 1).originalExpression += numbers.get(i).originalExpression;
            numbers.remove(i);
        }
    }


    private void splitByKansujiKurai(final String numberString, ArrayList <Pair <String, Character>> numberstringSplited) {

        numberstringSplited.clear();
        var ustr = new StringBuilder();
        for (var i = 0; i < numberString.length(); i++) {
            final var uc = numberString.toCharArray()[i];
            if (NumberNormalizer.digitUtility.isKansujiKuraiMan(uc)) {
                numberstringSplited.add(new Pair <>(ustr.toString(), uc));
                ustr = new StringBuilder();
            } else {
                ustr.append(uc);
            }
        }
        numberstringSplited.add(new Pair <>(ustr.toString(), '　'));
    }

    public class NumberExtractorImpl extends NumberExtractor {

        @Override
        public void extractNumber(String text, List <NNumber> numbers) {

            numbers.clear();
            final var utext = text;
            final String numstr = null;
            for (var i = 0; i < utext.length(); i++) {
                final var uc = utext.toCharArray()[i];
                if (NumberNormalizer.digitUtility.isNumber(uc)) {
                    final var number = new NNumber();
                    number.positionStart = i;
                    this.returnLongestNumberStrings(utext, new RefObject <>(i), numstr);
                    number.originalExpression = numstr;
                    number.positionEnd = i;
                    numbers.add(number);
                }
            }
        }


        @Override
        protected boolean isInvalidNotationType(List <ENotationType> notationType) {

            // 不適当な数字の表記を、表記タイプから判断する。（「２０００30」や「2000三十」などの、数字の表記が入り乱れているもの）
            final List <ENotationType> notationTypeA = new ArrayList <>();
            notationTypeA.add(ENotationType.HANKAKU);
            notationTypeA.add(ENotationType.ZENKAKU);
            final List <ENotationType> notationTypeB = new ArrayList <>();
            notationTypeA.add(ENotationType.HANKAKU);
            notationTypeA.add(ENotationType.KANSUJI_09);
            final List <ENotationType> notationTypeC = new ArrayList <>();
            notationTypeA.add(ENotationType.ZENKAKU);
            notationTypeA.add(ENotationType.KANSUJI_09);

            return NumberNormalizerImpl.this.includeMask(notationType, notationTypeA)
                    || NumberNormalizerImpl.this.includeMask(notationType, notationTypeB)
                    || NumberNormalizerImpl.this.includeMask(notationType, notationTypeC);
        }


        private boolean isInvalidKansujiKuraiOrder(final char uc, String kansujiKuraiStringsForCheckInvalidNotation) {

            if (kansujiKuraiStringsForCheckInvalidNotation.length() == 0) {
                kansujiKuraiStringsForCheckInvalidNotation += uc;
                return false;
            }

            final var a = NumberNormalizer.digitUtility.getNumberStringCharacter(kansujiKuraiStringsForCheckInvalidNotation,
                    kansujiKuraiStringsForCheckInvalidNotation.length() - 1);
            final var prev = NumberNormalizer.digitUtility.convertKansujiKuraiToPowerValue(a);
            final var cur = NumberNormalizer.digitUtility.convertKansujiKuraiToPowerValue(uc);
            if (cur < prev) {
                kansujiKuraiStringsForCheckInvalidNotation += uc;
                return false;
            } else {
                return true;
            }
        }


        private boolean isInvalidNotation(final char uc, String kansujiKuraiManStringsForCheckInvalidNotation,
                String kansujiKuraiSenStringsForCheckInvalidNotation) {

            // 「百2千」「一万五千七百億」のような不適当な表記（百が千の前にある、万が億の前にある、など）を検出する。
            // リアルタイム検出するために、変数は呼び出し元と共有し変数をアップデートしていくアルゴリズムになっている
            // （ucは今回対象とする数）
            if (NumberNormalizer.digitUtility.isKansujiKuraiMan(uc)) {
                return this.isInvalidKansujiKuraiOrder(uc, kansujiKuraiManStringsForCheckInvalidNotation);
            } else if (NumberNormalizer.digitUtility.isKansujiKuraiSen(uc)) {
                return this.isInvalidKansujiKuraiOrder(uc, kansujiKuraiSenStringsForCheckInvalidNotation);
            }
            return false;
        }


        @Override
        protected void returnLongestNumberStrings(String uText, RefObject <Integer> i, String numstr) {

            final List <ENotationType> notationType = new ArrayList <>();
            notationType.add(ENotationType.NOT_NUMBER);
            final String kansujiKuraiSenStringsForCheckInvalidNotation = null; // TODO : 変数名これで良い??…。
            final String kansujiKuraiManStringsForCheckInvalidNotation = null;
            // numstr.clear();
            int a = i.argValue;
            for (; a < uText.length(); a++) {
                final var uc = uText.toCharArray()[a];
                if (!NumberNormalizer.digitUtility.isNumber(uc)) {
                    return;
                }
                NumberNormalizerImpl.this.updateNotationType(uc, notationType);
                if (this.isInvalidNotationType(notationType)
                        || this.isInvalidNotation(uc, kansujiKuraiManStringsForCheckInvalidNotation, kansujiKuraiSenStringsForCheckInvalidNotation)) {
                    a--;
                    return;
                }
                numstr += uc;
            }

        }

    }

    public class NumberConverterTemplateImpl extends NumberConverterTemplate {

        @Override
        public void convertNumber(String numberStringOrg, RefObject <Double> value, RefObject <Integer> numberType) {

            // TODO 自動生成されたメソッド・スタブ

        }


        @Override
        protected void convertArabicKansujiMixedOf4digit(String numberString, RefObject <Integer> numberConverted) {

            // TODO 自動生成されたメソッド・スタブ

        }


        @Override
        protected void deleteComma(String ustr, String ret) {

            // TODO 自動生成されたメソッド・スタブ

        }


        @Override
        protected void convertArabicNumerals(String numberString, RefObject <Double> value) {

            // TODO 自動生成されたメソッド・スタブ

        }


        @Override
        protected void convertArabicKansujiKuraiManMixed(String numberString, RefObject <Double> value) {

            // TODO 自動生成されたメソッド・スタブ

        }


        @Override
        protected void convertArabicKansujiMixed(String numberString, RefObject <Double> value) {

            // TODO 自動生成されたメソッド・スタブ

        }

    }

    public class JapaneseNumberConverterImpl extends NumberConverterTemplateImpl {

        @Override
        protected void convertArabicKansujiMixedOf4digit(String numberString, RefObject <Integer> numberConverted) {

            // TODO 自動生成されたメソッド・スタブ

        }

    }

    public class ChineseNumberConverterImpl extends NumberConverterTemplateImpl {

        @Override
        protected void convertArabicKansujiMixedOf4digit(String numberString, RefObject <Integer> numberConverted) {

            // TODO 自動生成されたメソッド・スタブ

        }

    }

    public class ArabicNumberConverterImpl extends NumberConverterTemplateImpl {

        @Override
        public void convertNumber(String numberStringOrg, RefObject <Double> value, RefObject <Integer> numberType) {

            // TODO 自動生成されたメソッド・スタブ

        }


        @Override
        protected void convertArabicKansujiMixedOf4digit(String numberString, RefObject <Integer> numberConverted) {

            // TODO 自動生成されたメソッド・スタブ

        }

    }

    public class SymbolFixerImpl extends SymbolFixer {

        @Override
        public void fixNumbersBySymbol(String text, List <NNumber> numbers) {

            // TODO 自動生成されたメソッド・スタブ

        }


        @Override
        public boolean isPlus(String uText, int i, String plusStrings) {

            // TODO 自動生成されたメソッド・スタブ
            return false;
        }


        @Override
        public boolean isMinus(String uText, int i, String plusStrings) {

            // TODO 自動生成されたメソッド・スタブ
            return false;
        }


        @Override
        public void fixPrefixSymbol(String uText, List <NNumber> numbers, int i) {

            // TODO 自動生成されたメソッド・スタブ

        }


        @Override
        public double createDecimalValue(NNumber number) {

            // TODO 自動生成されたメソッド・スタブ
            return 0;
        }


        @Override
        public void fixDecimalPoint(List <NNumber> numbers, int i, String decimalStrings) {

            // TODO 自動生成されたメソッド・スタブ

        }


        @Override
        public void fixRangeExpression(List <NNumber> numbers, int i, String rangeStrings) {

            // TODO 自動生成されたメソッド・スタブ

        }


        @Override
        public void fixIntermediateSymbol(String uText, List <NNumber> numbers, int i) {

            // TODO 自動生成されたメソッド・スタブ

        }


        @Override
        public void fixSuffixSymbol(String uText, List <NNumber> numbers, int i) {

            // TODO 自動生成されたメソッド・スタブ

        }

    }

    public NumberNormalizerImpl(String language) {

        super(language);
    }


    @Override
    public void process(String input, List <NNumber> output) {

        // TODO 自動生成されたメソッド・スタブ

    }


    @Override
    public void processDontFixBySymbol(String input, List <NNumber> output) {

        // TODO 自動生成されたメソッド・スタブ

    }

}
