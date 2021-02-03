package jp.livlog.normalizeNumexp.share;

import java.util.HashMap;

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