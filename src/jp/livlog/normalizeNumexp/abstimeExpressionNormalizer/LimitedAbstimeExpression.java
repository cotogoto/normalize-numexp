package jp.livlog.normalizeNumexp.abstimeExpressionNormalizer;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import jp.livlog.normalizeNumexp.normalizerUtility.LimitedExpressionTemplate;

public abstract class LimitedAbstimeExpression extends LimitedExpressionTemplate {

    @SerializedName ("pattern")
    @Expose
    public String        pattern;

    @SerializedName ("corresponding_time_position")
    @Expose
    public List <String> correspondingTimePosition = null;

    @SerializedName ("process_type")
    @Expose
    public List <String> processType               = null;

    @SerializedName ("ordinary")
    @Expose
    public Boolean       ordinary;

    @SerializedName ("option")
    @Expose
    public String        option;
}
