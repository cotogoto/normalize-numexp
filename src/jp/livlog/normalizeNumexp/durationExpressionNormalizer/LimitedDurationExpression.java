package jp.livlog.normalizeNumexp.durationExpressionNormalizer;

import java.util.ArrayList;
import java.util.List;

import jp.livlog.normalizeNumexp.normalizerUtility.impl.LimitedExpressionTemplateImpl;

public class LimitedDurationExpression extends LimitedExpressionTemplateImpl {

    public List <String> correspondingTimePosition = new ArrayList <>();

    public List <String> processType               = new ArrayList <>();
}