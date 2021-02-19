package jp.livlog.numexp.digitUtility;

import jp.livlog.numexp.share.ENotationType;

public abstract class DigitUtility {

    public abstract void initKansuji(String language);


    public abstract boolean isHankakusuji(char uc);


    public abstract boolean isZenkakusuji(char uc);


    public abstract boolean isArabic(char uc);


    public abstract boolean isKansuji(char uc);


    public abstract boolean isKansuji09(char uc);


    public abstract boolean isKansujiKuraiSen(char uc);


    public abstract boolean isKansujiKuraiMan(char uc);


    public abstract boolean isKansujiKurai(char uc);


    public abstract boolean isNumber(char uc);


    public abstract boolean isComma(char uc);


    public abstract boolean isDecimalPoint(char uc);


    public abstract boolean isRangeExpression(String str);


    public abstract int convertKansuji09ToValue(char uc);


    public abstract int convertKansujiKuraiToPowerValue(char uc);


    public abstract ENotationType convertNotationType(char uc);

}
