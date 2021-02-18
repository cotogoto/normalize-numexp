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


    public abstract boolean isRangeExpression(char uc);


    public abstract int convertKansuji09ToValue(char uc);


    public abstract int convertKansujiKuraiToPowerValue(char uc);


    public abstract ENotationType convertNotationType(char uc);

//    public abstract char getNumberStringCharacter(StringBuilder numberString, int i);



    // public final class Pair <T1> {
    //
    // public T1 first;
    //
    // public Integer second;
    //
    // public Pair() {
    //
    // this.first = null;
    // this.second = null;
    // }
    //
    //
    // public Pair(T1 firstValue, Integer secondValue) {
    //
    // this.first = firstValue;
    // this.second = secondValue;
    // }
    //
    //
    // public Pair(Pair <T1> pairToCopy) {
    //
    // this.first = pairToCopy.first;
    // this.second = pairToCopy.second;
    // }
    //
    // }
    //
    // public class PrefixComp <T1> implements Comparator <Pair <T1>>, Serializable {
    //
    // /** シリアルバージョンUID. */
    // private static final long serialVersionUID = 1L;
    //
    // @Override
    // public int compare(final Pair <T1> arg0, final Pair <T1> arg1) {
    //
    // final var ret = arg0.second - arg1.second;
    // return ret;
    // }
    // }
    //
    // public class SuffixComp <T1> implements Comparator <Pair <T1>>, Serializable {
    //
    // /** シリアルバージョンUID. */
    // private static final long serialVersionUID = 1L;
    //
    // @Override
    // public int compare(final Pair <T1> arg0, final Pair <T1> arg1) {
    //
    // final var ret = arg1.second - arg0.second;
    //
    // return ret;
    // }
    // }

}
