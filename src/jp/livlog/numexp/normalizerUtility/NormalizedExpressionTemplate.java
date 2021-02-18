package jp.livlog.numexp.normalizerUtility;

import java.util.ArrayList;
import java.util.List;

import jp.livlog.numexp.share.BaseExpressionTemplate;
import jp.livlog.numexp.share.ENotationType;

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

    public ENotationType      numberNotationType;

    public boolean            includeLowerbound;

    public boolean            includeUpperbound;

    public boolean            isOver;

    public boolean            isLess;

    public boolean            ordinary;

    public List <String> options = new ArrayList <>();
}
