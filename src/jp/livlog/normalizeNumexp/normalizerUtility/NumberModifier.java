package jp.livlog.normalizeNumexp.normalizerUtility;

import jp.livlog.normalizeNumexp.share.BaseExpressionTemplate;

public class NumberModifier extends BaseExpressionTemplate {

    public NumberModifier(String pattern, String processType) {

        this.pattern = pattern;
        this.processType = processType;
    }

    public String pattern;

    public String processType;
}
