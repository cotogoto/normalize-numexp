package jp.livlog.normalizeNumexp.share;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NumberModifier extends BaseExpressionTemplate {

    public NumberModifier(String pattern, String processType) {

        this.pattern = pattern;
        this.processType = processType;
    }

    @SerializedName ("pattern")
    @Expose
    public String pattern;

    @SerializedName ("process_type")
    @Expose
    public String processType;
}
