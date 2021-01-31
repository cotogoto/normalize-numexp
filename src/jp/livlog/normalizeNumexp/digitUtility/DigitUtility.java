package jp.livlog.normalizeNumexp.digitUtility;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;

import jp.livlog.normalizeNumexp.share.Symbol;

public abstract class DigitUtility {

    abstract public void initKansuji(String language);


    abstract public boolean isHankakusuji(String uc);


    abstract public boolean isZenkakusuji(String uc);


    abstract public boolean isArabic(String uc);


    abstract public boolean isKansuji(String uc);


    abstract public boolean isKansuji09(String uc);


    abstract public boolean isKansujiKuraiSen(String uc);


    abstract public boolean isKansujiKuraiMan(String uc);


    abstract public boolean isKansujiKurai(String uc);


    abstract public boolean isNumber(String uc);


    abstract public boolean isComma(String uc);


    abstract public boolean isDecimalPoint(String ustr);


    abstract public boolean isRangeExpression(String ustr);


    abstract public int convertKansuji09ToValue(String uc);


    abstract public int convertKansujiKuraiToPowerValue(String uc);

    public enum ENotationType {

        NOT_NUMBER(0),
        KANSUJI_09(1),
        KANSUJI_KURAI_SEN(2),
        KANSUJI_KURAI_MAN(4),
        KANSUJI_KURAI(6),
        KANSUJI(7),
        ZENKAKU(8),
        HANKAKU(16);

        public static final int                         SIZE = Integer.SIZE;

        private final int                               intValue;

        private static HashMap <Integer, ENotationType> mappings;

        private static HashMap <Integer, ENotationType> getMappings() {

            if (ENotationType.mappings == null) {
                synchronized (ENotationType.class) {
                    if (ENotationType.mappings == null) {
                        ENotationType.mappings = new HashMap <>();
                    }
                }
            }
            return ENotationType.mappings;
        }


        ENotationType(int value) {

            this.intValue = value;
            ENotationType.getMappings().put(value, this);
        }


        public int getValue() {

            return this.intValue;
        }


        public static ENotationType forValue(int value) {

            return ENotationType.getMappings().get(value);
        }
    }

    public class Number {

        public Number() {

            this.originalExpression = "";
            this.positionStart = -1;
            this.positionEnd = -1;
            this.valueLowerbound = Symbol.INFINITY;
            this.valueUpperbound = -Symbol.INFINITY;
            this.notationType = ENotationType.NOT_NUMBER.getValue();
        }


        public Number(String originalExpression, int positionStart, int positionEnd) {

            this.originalExpression = originalExpression;
            this.positionStart = positionStart;
            this.positionEnd = positionEnd;
            this.valueLowerbound = Symbol.INFINITY;
            this.valueUpperbound = -Symbol.INFINITY;
            this.notationType = ENotationType.NOT_NUMBER.getValue();
        }

        public String originalExpression = null;

        public int    positionStart;

        public int    positionEnd;

        public double valueLowerbound;

        public double valueUpperbound;

        public int    notationType;
    }

    public final class Pair <T1> {

        public T1      first;

        public Integer second;

        public Pair() {

            this.first = null;
            this.second = null;
        }


        public Pair(T1 firstValue, Integer secondValue) {

            this.first = firstValue;
            this.second = secondValue;
        }


        public Pair(Pair <T1> pairToCopy) {

            this.first = pairToCopy.first;
            this.second = pairToCopy.second;
        }

    }

    public class PrefixComp <T1> implements Comparator <Pair <T1>>, Serializable {

        /** シリアルバージョンUID. */
        private static final long serialVersionUID = 1L;

        @Override
        public int compare(final Pair <T1> arg0, final Pair <T1> arg1) {

            final var ret = arg0.second - arg1.second;
            return ret;
        }
    }

    public class SuffixComp <T1> implements Comparator <Pair <T1>>, Serializable {

        /** シリアルバージョンUID. */
        private static final long serialVersionUID = 1L;

        @Override
        public int compare(final Pair <T1> arg0, final Pair <T1> arg1) {

            final var ret = arg1.second - arg0.second;

            return ret;
        }
    }

}
