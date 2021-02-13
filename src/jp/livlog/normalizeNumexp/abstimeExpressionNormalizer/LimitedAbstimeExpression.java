package jp.livlog.normalizeNumexp.abstimeExpressionNormalizer;

import java.util.ArrayList;
import java.util.List;

import jp.livlog.normalizeNumexp.normalizerUtility.impl.LimitedExpressionTemplateImpl;
import lombok.ToString;

@ToString(callSuper=true)
public class LimitedAbstimeExpression extends LimitedExpressionTemplateImpl {

    public List <String> correspondingTimePosition = new ArrayList <>();

    public List <String> processType               = new ArrayList <>();
}
