package jp.livlog.normalizeNumexp.normalizerUtility;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;

import jp.livlog.normalizeNumexp.digitUtility.DigitUtility;
import jp.livlog.normalizeNumexp.digitUtility.impl.DigitUtilityImpl;
import jp.livlog.normalizeNumexp.share.BaseExpressionTemplate;
import jp.livlog.normalizeNumexp.share.Pair;
import jp.livlog.normalizeNumexp.share.Symbol;

public abstract class NormalizerUtility extends DigitUtilityImpl {

    public static class Time {

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


        public final String toStringFromTimeElement(double t, String nullString, String kugiri, boolean isUpperbound, int width) {

            final var ss = new StringBuilder();
            String ret = null;
            if (this.isNullTimeElement(t, isUpperbound)) {
                return nullString + kugiri;
            } else {
                ss.append(String.format("%04d", t));
                ss.append(kugiri);
                ret = ss.toString();
                return ret;
            }
        }


        public final String toIntervalStringFromTimeElement(double t, String timePosition, boolean isUpperbound) {

            final var ss = new StringBuilder();
            String ret = null;
            if (this.isNullTimeElement(t, isUpperbound)) {
                return "";
            } else {
                ss.append(t);
                ss.append(timePosition);
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

    public static abstract class NormalizedExpressionTemplate extends BaseExpressionTemplate {

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


        abstract public void setOriginalExpressionFromPosition(String uText);

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

    public static abstract class LimitedExpressionTemplate extends BaseExpressionTemplate {

        abstract public void setTotalNumberOfPlaceHolder();


        abstract public void setLengthOfStringsAfterFinalPlaceHolder();

        public String  pattern;

        public boolean ordinary;

        public String  option;

        public int     totalNumberOfPlaceHolder;             // patternが含むPLACE_HOLDERの数（ *月*日 -> 2個）

        public int     lengthOfStringsAfterFinalPlaceHolder; // pattern中の最後のPLACE_HOLDERの後に続く文字列の長さ（*月*日 -> 1） positionの同定に必要
    }

    public static class NumberModifier extends BaseExpressionTemplate {

        public NumberModifier(String pattern, String processType) {

            this.pattern = pattern;
            this.processType = processType;
        }

        public String pattern;

        public String processType;
    }

    abstract public void extractAfterString(StringBuilder uTextReplaced, int i, String afterString);


    abstract public void extractBeforeString(StringBuilder uTextReplaced, int i, String beforeString);


    abstract public void prefixSearch(String ustr, List <Pair <String, Integer>> patterns, int matchingPatternId);


    abstract public void suffixSearch(String ustr, List <Pair <String, Integer>> patternsRev, int matchingPatternId);


    abstract public void searchSuffixNumberModifier(StringBuilder uTextReplaced, int expPositionEnd, List <Pair <String, Integer>> suffixNumberModifierPatterns,
            int matchingPatternId);


    abstract public void searchPrefixNumberModifier(StringBuilder uTextReplaced, int expPositionStart, List <Pair <String, Integer>> prefixNumberModifierPatterns,
            int matchingPatternId);


    abstract public void replaceNumbersInText(String uText, List <DigitUtility.Number> numbers, StringBuilder uTextReplaced);


    abstract public void shortenPlaceHolderInText(String text, StringBuilder textShortened);


    abstract public boolean isPlaceHolder(char uc);


    abstract public boolean isFinite(double value);


    abstract public boolean isNullTime(double time);


    abstract public String identifyTimeDetail(Time time);


    abstract public String reverseString(String str);


    @SuppressWarnings ("unchecked")
    public <T1, T2> void cast(final T1 a, T2 b) {

        final var modelMapper = new ModelMapper();
        b = (T2) modelMapper.map(a, b.getClass());
    }

    // LATIN LETTER ALVEOLAR CLICK
    public final char PLACE_HOLDER = 'ǂ';

}
