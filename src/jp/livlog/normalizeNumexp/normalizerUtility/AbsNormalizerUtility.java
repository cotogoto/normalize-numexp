package jp.livlog.normalizeNumexp.normalizerUtility;

import jp.livlog.normalizeNumexp.digitUtility.DigitUtility;
import jp.livlog.normalizeNumexp.share.Symbol;

public class AbsNormalizerUtility extends DigitUtility {

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


        public final boolean is_null_time_element(double t, boolean is_upperbound) {

            if (is_upperbound) {
                return t == -Symbol.INFINITY;
            } else {
                return t == Symbol.INFINITY;
            }
        }


        public final boolean is_infinity_time_element(double t, boolean is_upperbound) {

            if (is_upperbound) {
                return t == Symbol.INFINITY;
            } else {
                return t == -Symbol.INFINITY;
            }
        }


        public final String to_string_from_time_element(double t, String null_string, String kugiri, boolean is_upperbound, int width) {

            final var ss = new StringBuilder();
            String ret = null;
            if (this.is_null_time_element(t, is_upperbound)) {
                return null_string + kugiri;
            } else {
                ss.append(String.format("%04d", t));
                ss.append(kugiri);
                ret = ss.toString();
                return ret;
            }
        }


        public final String to_interval_string_from_time_element(double t, String time_position, boolean is_upperbound) {

            final var ss = new StringBuilder();
            String ret = null;
            if (this.is_null_time_element(t, is_upperbound)) {
                return "";
            } else {
                ss.append(t);
                ss.append(time_position);
                ret = ss.toString();
                return ret;
            }
        }


        public final String to_string(boolean is_upperbound) {

            if (this.is_null_time_element(this.year, is_upperbound) && this.is_null_time_element(this.month, is_upperbound)
                    && this.is_null_time_element(this.day, is_upperbound)) {
                return this.to_time_string(is_upperbound);
            } else {
                return this.to_date_string(is_upperbound);
            }
        }


        public final String to_date_string(boolean is_upperbound) {

            // std::stringstream ss = new std::stringstream();
            final String ret = null;
            // if (this.is_infinity_time_element(this.year, is_upperbound))
            // {
            // if (is_upperbound)
            // {
            // return "INF";
            // }
            // else
            // {
            // return "-INF";
            // }
            // }
            // ss << this.to_string_from_time_element(this.year, "XXXX", "-", is_upperbound, 4);
            // ss << this.to_string_from_time_element(this.month, "XX", "-", is_upperbound, 2);
            // ss << this.to_string_from_time_element(this.day, "XX", "", is_upperbound, 2);
            // //C++ TO JAVA CONVERTER WARNING: The right shift operator was not replaced by Java's logical right shift operator since the left
            // operand was not confirmed to be of an unsigned type, but you should review whether the logical right shift operator (>>>) is more
            // appropriate:
            // ss >> ret;
            return ret;
        }


        public final String to_time_string(boolean is_upperbound) {

            // std::stringstream ss = new std::stringstream();
            final String ret = null;
            // if (this.is_infinity_time_element(this.year, is_upperbound))
            // {
            // if (is_upperbound)
            // {
            // return "INF";
            // }
            // else
            // {
            // return "-INF";
            // }
            // }
            // ss << this.to_string_from_time_element(this.hour, "XX", ":", is_upperbound, 2);
            // ss << this.to_string_from_time_element(this.minute, "XX", ":", is_upperbound, 2);
            // ss << this.to_string_from_time_element(this.second, "XX", "", is_upperbound, 2);
            // //C++ TO JAVA CONVERTER WARNING: The right shift operator was not replaced by Java's logical right shift operator since the left
            // operand was not confirmed to be of an unsigned type, but you should review whether the logical right shift operator (>>>) is more
            // appropriate:
            // ss >> ret;
            return ret;
        }


        public final String to_duration_string(boolean is_upperbound) {

            // std::stringstream ss = new std::stringstream();
            final String ret = null;
            // if (this.is_infinity_time_element(this.year, is_upperbound))
            // {
            // if (is_upperbound)
            // {
            // return "INF";
            // }
            // else
            // {
            // return "-INF";
            // }
            // }
            // ss << "P";
            // ss << this.to_interval_string_from_time_element(this.year, "Y", is_upperbound);
            // ss << this.to_interval_string_from_time_element(this.month, "M", is_upperbound);
            // ss << this.to_interval_string_from_time_element(this.day, "D", is_upperbound);
            // ss << this.to_interval_string_from_time_element(this.hour, "h", is_upperbound);
            // ss << this.to_interval_string_from_time_element(this.minute, "m", is_upperbound);
            // ss << this.to_interval_string_from_time_element(this.second, "s", is_upperbound);
            // //C++ TO JAVA CONVERTER WARNING: The right shift operator was not replaced by Java's logical right shift operator since the left
            // operand was not confirmed to be of an unsigned type, but you should review whether the logical right shift operator (>>>) is more
            // appropriate:
            // ss >> ret;
            return ret;
        }

        public double year;

        public double month;

        public double day;

        public double hour;

        public double minute;

        public double second;
    }

}
