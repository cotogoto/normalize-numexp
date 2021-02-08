package jp.livlog.normalizeNumexp.normalizerUtility.impl;

import jp.livlog.normalizeNumexp.normalizerUtility.NormalizedExpressionTemplate;
import lombok.ToString;

@ToString
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
