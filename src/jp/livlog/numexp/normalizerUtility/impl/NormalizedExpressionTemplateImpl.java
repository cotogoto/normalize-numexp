package jp.livlog.numexp.normalizerUtility.impl;

import jp.livlog.numexp.normalizerUtility.NormalizedExpressionTemplate;
import lombok.ToString;

@ToString(callSuper=false)
public class NormalizedExpressionTemplateImpl extends NormalizedExpressionTemplate {

    public NormalizedExpressionTemplateImpl(String originalExpression, int positionStart, int positionEnd) {

        super(originalExpression, positionStart, positionEnd);
    }


    @Override
    public void setOriginalExpressionFromPosition(StringBuilder text) {

        if (text.length() < this.positionEnd) {
            return;
        }

        this.originalExpression = text.substring(this.positionStart, this.positionEnd);
    }
}
