package jp.livlog.numexp.normalizerUtility;

import jp.livlog.numexp.share.BaseExpressionTemplate;

public abstract class LimitedExpressionTemplate extends BaseExpressionTemplate {

    abstract public void setTotalNumberOfPlaceHolder();


    abstract public void setLengthOfStringsAfterFinalPlaceHolder();

    public boolean ordinary;

    public String  option;

    public int     totalNumberOfPlaceHolder;             // patternが含むPLACE_HOLDERの数（ *月*日 -> 2個）

    public int     lengthOfStringsAfterFinalPlaceHolder; // pattern中の最後のPLACE_HOLDERの後に続く文字列の長さ（*月*日 -> 1） positionの同定に必要
}