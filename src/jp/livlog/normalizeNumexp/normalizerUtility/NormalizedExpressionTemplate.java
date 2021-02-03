package jp.livlog.normalizeNumexp.normalizerUtility;

import java.util.ArrayList;

import jp.livlog.normalizeNumexp.share.BaseExpressionTemplate;
import jp.livlog.normalizeNumexp.share.ENotationType;

public abstract class NormalizedExpressionTemplate extends BaseExpressionTemplate {

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


    abstract public void setOriginalExpressionFromPosition(StringBuilder uText);

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
