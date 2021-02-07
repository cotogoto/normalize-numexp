package jp.livlog.normalizeNumexp.numericalExpressionNormalizer.impl;

import java.util.ArrayList;
import java.util.List;

import jp.livlog.normalizeNumexp.digitUtility.DigitUtility;
import jp.livlog.normalizeNumexp.numericalExpressionNormalizer.NumberExtractor;
import jp.livlog.normalizeNumexp.share.ENotationType;
import jp.livlog.normalizeNumexp.share.NNumber;
import jp.livlog.normalizeNumexp.share.RefObject;

public class NumberExtractorImpl extends NumberExtractor {

    public NumberExtractorImpl(DigitUtility digitUtility) {

        super(digitUtility);
    }


    @Override
    public void extractNumber(String text, List <NNumber> numbers) {

        numbers.clear();
        final var utext = new StringBuilder(text);
        StringBuilder numstr = null;
        NNumber number = null;
        var numFlg = false;
        var kansujiFlg = false;
        for (var i = 0; i < utext.length(); i++) {
            final var uc = utext.charAt(i);
            if (this.digitUtility.isNumber(uc)) {
                if (!numFlg || this.digitUtility.isKansuji(uc) != kansujiFlg) {
                    if (this.digitUtility.isKansuji(uc)) {
                        kansujiFlg = true;
                    } else {
                        kansujiFlg = false;
                    }
                    numstr = new StringBuilder();
                    number = new NNumber();
                    number.positionStart = i;
                    number.notationType.add(this.digitUtility.convertNotationType(uc));
                    numFlg = true;
                    this.returnLongestNumberStrings(utext, new RefObject <>(i), numstr);
                    number.originalExpression = numstr.toString();
                    numbers.add(number);
                }
                number.positionEnd = i + 1;

            } else {
                numFlg = false;
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
        notationTypeB.add(ENotationType.HANKAKU);
        notationTypeB.add(ENotationType.KANSUJI_09);
        final List <ENotationType> notationTypeC = new ArrayList <>();
        notationTypeC.add(ENotationType.ZENKAKU);
        notationTypeC.add(ENotationType.KANSUJI_09);

        return this.includeMask(notationType, notationTypeA)
                || this.includeMask(notationType, notationTypeB)
                || this.includeMask(notationType, notationTypeC);
    }


    private boolean isInvalidKansujiKuraiOrder(final char uc, StringBuilder kansujiKuraiStringsForCheckInvalidNotation) {

        if (kansujiKuraiStringsForCheckInvalidNotation.length() == 0) {
            kansujiKuraiStringsForCheckInvalidNotation.append(uc);
            return false;
        }

        // final var a = this.digitUtility.getNumberStringCharacter(kansujiKuraiStringsForCheckInvalidNotation,
        // kansujiKuraiStringsForCheckInvalidNotation.length() - 1);
        final var a = kansujiKuraiStringsForCheckInvalidNotation.charAt(kansujiKuraiStringsForCheckInvalidNotation.length() - 1);
        final var prev = this.digitUtility.convertKansujiKuraiToPowerValue(a);
        final var cur = this.digitUtility.convertKansujiKuraiToPowerValue(uc);
        if (cur < prev) {
            kansujiKuraiStringsForCheckInvalidNotation.append(uc);
            return false;
        } else {
            return true;
        }
    }


    private boolean isInvalidNotation(final char uc, StringBuilder kansujiKuraiManStringsForCheckInvalidNotation,
            StringBuilder kansujiKuraiSenStringsForCheckInvalidNotation) {

        // 「百2千」「一万五千七百億」のような不適当な表記（百が千の前にある、万が億の前にある、など）を検出する。
        // リアルタイム検出するために、変数は呼び出し元と共有し変数をアップデートしていくアルゴリズムになっている
        // （ucは今回対象とする数）
        if (this.digitUtility.isKansujiKuraiMan(uc)) {
            kansujiKuraiSenStringsForCheckInvalidNotation.setLength(0);
            return this.isInvalidKansujiKuraiOrder(uc, kansujiKuraiManStringsForCheckInvalidNotation);
        } else if (this.digitUtility.isKansujiKuraiSen(uc)) {
            return this.isInvalidKansujiKuraiOrder(uc, kansujiKuraiSenStringsForCheckInvalidNotation);
        }
        return false;
    }


    @Override
    protected void returnLongestNumberStrings(StringBuilder uText, RefObject <Integer> i, StringBuilder numstr) {

        final List <ENotationType> notationType = new ArrayList <>();
        notationType.add(ENotationType.NOT_NUMBER);
        final var kansujiKuraiSenStringsForCheckInvalidNotation = new StringBuilder();
        final var kansujiKuraiManStringsForCheckInvalidNotation = new StringBuilder();
        numstr.setLength(0);
        int a = i.argValue;
        for (; a < uText.length(); a++) {
            final var uc = uText.charAt(a);
            if (!this.digitUtility.isNumber(uc)) {
                return;
            }
            this.updateNotationType(uc, notationType);
            if (this.isInvalidNotationType(notationType)
                    || this.isInvalidNotation(uc, kansujiKuraiManStringsForCheckInvalidNotation, kansujiKuraiSenStringsForCheckInvalidNotation)) {
                // if (this.isInvalidNotationType(notationType)) {
                a--;
                return;
            }
            numstr.append(uc);
        }

    }


    private void updateNotationType(final char uc, List <ENotationType> notationType) {

        final List <ENotationType> tempList = new ArrayList <>(notationType);
        if (this.digitUtility.isHankakusuji(uc)) {
            // notationType.argValue |= ENotationType.HANKAKU.getValue();
            tempList.add(ENotationType.HANKAKU);
        } else if (this.digitUtility.isZenkakusuji(uc)) {
            // notationType.argValue |= ENotationType.ZENKAKU.getValue();
            tempList.add(ENotationType.ZENKAKU);
        } else if (this.digitUtility.isKansuji09(uc)) {
            // notationType.argValue |= ENotationType.KANSUJI_09.getValue();
            tempList.add(ENotationType.KANSUJI_09);
        } else if (this.digitUtility.isKansujiKuraiSen(uc)) {
            // notationType.argValue |= ENotationType.KANSUJI_KURAI_SEN.getValue();
            tempList.add(ENotationType.KANSUJI_KURAI_SEN);
        } else if (this.digitUtility.isKansujiKuraiMan(uc)) {
            // notationType.argValue |= ENotationType.KANSUJI_KURAI_MAN.getValue();
            tempList.add(ENotationType.KANSUJI_KURAI_MAN);
        }
        notationType.clear();
        notationType.addAll(tempList.subList(tempList.size() - 2, tempList.size()));
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

}
