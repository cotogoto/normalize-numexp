package jp.livlog.normalizeNumexp.share;

public class NumberModifier extends BaseExpressionTemplate {

    public NumberModifier(String pattern, String processType) {

        this.pattern = pattern;
        this.processType = processType;
    }

    public String processType;
}
