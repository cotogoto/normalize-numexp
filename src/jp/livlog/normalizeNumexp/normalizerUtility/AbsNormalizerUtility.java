package jp.livlog.normalizeNumexp.normalizerUtility;

import java.util.ArrayList;
import java.util.List;

import jp.livlog.normalizeNumexp.digitUtility.DigitUtility;
import jp.livlog.normalizeNumexp.share.Symbol;

public abstract class AbsNormalizerUtility extends DigitUtility {

    public class Time {

        public Time(final double value) {

            this.year = this.month = this.day = this.hour = this.minute = this.second = value;
        }


        public Time(final double year, final double month, final double day, final double hour, final double minute, final double second) {

            this.year = year;
            this.month = month;
            this.day = day;
            this.hour = hour;
            this.minute = minute;
            this.second = second;
        }


        public boolean equalsTo(final Time t) {

            return t.year == this.year && t.month == this.month && t.day == this.day && t.hour == this.hour && t.minute == this.minute
                    && t.second == this.second;
        }


        public final boolean isNullTimeElement(double t, boolean isUpperbound) {

            if (isUpperbound) {
                return t == -Symbol.INFINITY;
            } else {
                return t == Symbol.INFINITY;
            }
        }


        public final boolean isInfinityTimeElement(double t, boolean isUpperbound) {

            if (isUpperbound) {
                return t == Symbol.INFINITY;
            } else {
                return t == -Symbol.INFINITY;
            }
        }


        public final String toStringFromTimeElement(double t, String null_string, String kugiri, boolean isUpperbound, int width) {

            final var ss = new StringBuilder();
            String ret = null;
            if (this.isNullTimeElement(t, isUpperbound)) {
                return null_string + kugiri;
            } else {
                ss.append(String.format("%04d", t));
                ss.append(kugiri);
                ret = ss.toString();
                return ret;
            }
        }


        public final String toIntervalStringFromTimeElement(double t, String time_position, boolean isUpperbound) {

            final var ss = new StringBuilder();
            String ret = null;
            if (this.isNullTimeElement(t, isUpperbound)) {
                return "";
            } else {
                ss.append(t);
                ss.append(time_position);
                ret = ss.toString();
                return ret;
            }
        }


        public final String toString(boolean isUpperbound) {

            if (this.isNullTimeElement(this.year, isUpperbound) && this.isNullTimeElement(this.month, isUpperbound)
                    && this.isNullTimeElement(this.day, isUpperbound)) {
                return this.toTimeString(isUpperbound);
            } else {
                return this.toDateString(isUpperbound);
            }
        }


        public final String toDateString(boolean isUpperbound) {

            final var ss = new StringBuilder();
            String ret;
            if (this.isInfinityTimeElement(this.year, isUpperbound)) {
                if (isUpperbound) {
                    return "INF";
                } else {
                    return "-INF";
                }
            }
            ss.append(this.toStringFromTimeElement(this.year, "XXXX", "-", isUpperbound, 4));
            ss.append(this.toStringFromTimeElement(this.month, "XX", "-", isUpperbound, 2));
            ss.append(this.toStringFromTimeElement(this.day, "XX", "", isUpperbound, 2));
            ret = ss.toString();
            return ret;
        }


        public final String toTimeString(boolean isUpperbound) {

            final var ss = new StringBuilder();
            String ret;
            if (this.isInfinityTimeElement(this.year, isUpperbound)) {
                if (isUpperbound) {
                    return "INF";
                } else {
                    return "-INF";
                }
            }
            ss.append(this.toStringFromTimeElement(this.hour, "XX", ":", isUpperbound, 2));
            ss.append(this.toStringFromTimeElement(this.minute, "XX", ":", isUpperbound, 2));
            ss.append(this.toStringFromTimeElement(this.second, "XX", "", isUpperbound, 2));
            ret = ss.toString();
            return ret;
        }


        public final String toDurationString(boolean isUpperbound) {

            final var ss = new StringBuilder();
            String ret;
            if (this.isInfinityTimeElement(this.year, isUpperbound)) {
                if (isUpperbound) {
                    return "INF";
                } else {
                    return "-INF";
                }
            }
            ss.append("P");
            ss.append(this.toIntervalStringFromTimeElement(this.year, "Y", isUpperbound));
            ss.append(this.toIntervalStringFromTimeElement(this.month, "M", isUpperbound));
            ss.append(this.toIntervalStringFromTimeElement(this.day, "D", isUpperbound));
            ss.append(this.toIntervalStringFromTimeElement(this.hour, "h", isUpperbound));
            ss.append(this.toIntervalStringFromTimeElement(this.minute, "m", isUpperbound));
            ss.append(this.toIntervalStringFromTimeElement(this.second, "s", isUpperbound));
            ret = ss.toString();
            return ret;
        }

        public double year;

        public double month;

        public double day;

        public double hour;

        public double minute;

        public double second;
    }

    public class NormalizedExpressionTemplate {

        public NormalizedExpressionTemplate(final String originalExpression, final int positionStart, final int positionEnd) {

            this.originalExpression = originalExpression;
            this.positionStart = positionStart;
            this.positionEnd = positionEnd;
            this.numberNotationType = ENotationType.NOT_NUMBER;
            this.includeLowerbound = true;
            this.includeUpperbound = true;
            this.isOver = false;
            this.isLess = false;
            this.ordinary = false;
            this.options.clear();
        }

        public String             originalExpression;

        public int                positionStart;

        public int                positionEnd;

        public ENotationType      numberNotationType;

        public boolean            includeLowerbound;

        public boolean            includeUpperbound;

        public boolean            isOver;

        public boolean            isLess;

        public boolean            ordinary;

        public ArrayList <String> options = new ArrayList <>();
    }

    public abstract class LimitedExpressionTemplate {

        abstract void setTotalNumberOfPlaceHolder();


        abstract void setLengthOfStringsAfterFinalPlaceHolder();

        public String  pattern;

        public boolean ordinary;

        public String  option;

        public int     totalNumberOfPlaceHolder;             // patternが含むPLACE_HOLDERの数（ *月*日 -> 2個）

        public int     lengthOfStringsAfterFinalPlaceHolder; // pattern中の最後のPLACE_HOLDERの後に続く文字列の長さ（*月*日 -> 1） positionの同定に必要
    }

    public class NumberModifier {

        public NumberModifier(String pattern, String processType) {

            this.pattern = pattern;
            this.processType = processType;
        }

        public String pattern;

        public String processType;
    }

    abstract void extractAfterString(String text, int i, String afterString);


    abstract void extractBeforeString(String text, int i, String beforeString);


    abstract void prefixsearch(String ustr, List <Integer> patterns, int matchingPatternId);


    abstract void suffixsearch(String ustr, List <Integer> patternsRev, int matchingPatternId);


    abstract void searchSuffixNumberModifier(String text, int expPositionEnd, List <Integer> suffixNumberModifierPatterns, int matchingPatternId);


    abstract void searchPrefixNumberModifier(String text, int expPositionStart, List <Integer> prefixNumberModifierPatterns, int matchingPatternId);


    abstract void replaceNumbersInText(String utext, List <Integer> numbers, String utextReplaced);


    abstract void shortenPlaceHolderInText(String utext, String utextShortened);


    abstract boolean isPlaceHolder(String uc);


    abstract boolean isFinite(double value);


    abstract boolean isNullTime(Time time);


    abstract String identifyTimeDetail(Time time);


    abstract String reverseString(String str);
}
