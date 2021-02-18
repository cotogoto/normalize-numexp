
package jp.livlog.numexp.share;

/**
 * 定数クラス.
 *
 * @author H.Aoshima
 * @version 1.0
 */
public final class Symbol {

    /** 指定された浮動小数点型の正の無限大値を返します. */
    public static final double INFINITY  = Double.POSITIVE_INFINITY;

    /** アスタリスク. */
    public static final String ASTERISK  = "*";

    /** カンマ. */
    public static final String COMMA     = ",";

    /** 数量表現. */
    public static final String NUMERICAL = "numerical";

    /** 絶対時間表現. */
    public static final String ABSTIME   = "abstime";

    /** 相対時間表現. */
    public static final String RELTIME   = "reltime";

    /** 持続時間表現. */
    public static final String DURATION  = "duration";

    /**
     * コンストラクタ.
     */
    private Symbol() {

    }

}
