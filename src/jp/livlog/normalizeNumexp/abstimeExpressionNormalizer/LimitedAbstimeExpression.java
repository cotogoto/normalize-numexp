package jp.livlog.normalizeNumexp.abstimeExpressionNormalizer;

import java.util.ArrayList;
import java.util.List;

import jp.livlog.normalizeNumexp.normalizerUtility.LimitedExpressionTemplate;

public abstract class LimitedAbstimeExpression extends LimitedExpressionTemplate {

    public List <String> correspondingTimePosition = new ArrayList <>();

    public List <String> processType               = new ArrayList <>();
}
