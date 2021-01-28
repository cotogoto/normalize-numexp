package jp.livlog.normalizeNumexp.digitUtility;

import java.util.HashMap;

import jp.livlog.normalizeNumexp.share.Symbol;

public abstract class AbsDigitUtility {

    abstract void initKansuji(String language);


    abstract boolean isHankakusuji(String uc);


    abstract boolean isZenkakusuji(String uc);


    abstract boolean isArabic(String uc);


    abstract boolean isKansuji(String uc);


    abstract boolean isKansuji09(String uc);


    abstract boolean isKansujiKuraiSen(String uc);


    abstract boolean isKansujiKuraiMan(String uc);


    abstract boolean isKansujiKurai(String uc);


    abstract boolean isNumber(String uc);


    abstract boolean isComma(String uc);


    abstract boolean isDecimalPoint(String ustr);


    abstract boolean isRangeExpression(String ustr);


    abstract int convertKansuji09ToValue(String uc);


    abstract int convertKansujiKuraiToPowerValue(String uc);

    public enum ENotationType {

        NOT_NUMBER(0),
        KANSUJI_09(1),
        KANSUJI_KURAI_SEN(2),
        KANSUJI_KURAI_MAN(4),
        KANSUJI_KURAI(6),
        KANSUJI(7),
        ZENKAKU(8),
        HANKAKU(16);

        public static final int                                   SIZE = Integer.SIZE;

        private final int                                         intValue;

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

}
