package jp.livlog.normalizeNumexp.share;

import lombok.ToString;

@ToString (callSuper = false)
public class Expression {

    /* 表現タイプ. */
    public String type;

    /* 表現. */
    public String originalExpression;

    /* 開始位置. */
    public int positionStart;

    /* 終了位置. */
    public int positionEnd;

    /* 単位. */
    public String counter;

    /* 数量(時間)の下限. */
    public String valueLowerbound;

    /* 数量(時間)の上限. */
    public String valueUpperbound;

    /* オプション. */
    public String options;
}
