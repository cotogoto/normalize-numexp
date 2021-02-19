package jp.livlog.numexp.share;

import lombok.ToString;

@ToString(callSuper=false)
public class NTime {

    public NTime(final double value) {

        this.year = this.month = this.day = this.hour = this.minute = this.second = value;
    }


    public NTime(final double year, final double month, final double day, final double hour, final double minute, final double second) {

        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }


    public boolean equalsTo(final NTime t) {

        return t.year == this.year && t.month == this.month && t.day == this.day && t.hour == this.hour && t.minute == this.minute
                && t.second == this.second;
    }


    public final boolean isNullTimeElement(double t, boolean isUpperbound) {

        if (isUpperbound) {
            return t == -NumexpSymbol.INFINITY;
        } else {
            return t == NumexpSymbol.INFINITY;
        }
    }


    public final boolean isInfinityTimeElement(double t, boolean isUpperbound) {

        if (isUpperbound) {
            return t == NumexpSymbol.INFINITY;
        } else {
            return t == -NumexpSymbol.INFINITY;
        }
    }


    public final String toStringFromTimeElement(double t, String nullString, String kugiri, boolean isUpperbound, int width) {

        final var ss = new StringBuilder();
        String ret = null;
        if (this.isNullTimeElement(t, isUpperbound)) {
            return nullString + kugiri;
        } else {
            ss.append(String.format("%0" + width + "d", (int) t));
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
            ss.append(this.format(t));
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
        if (this.isInfinityTimeElement(this.hour, isUpperbound)) {
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


    public String format(double d) {

        if (d == (int) d) {
            return String.format("%d", (int) d);
        } else {
            return String.format("%s", d);
        }
    }

    public double year;

    public double month;

    public double day;

    public double hour;

    public double minute;

    public double second;
}
