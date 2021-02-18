package jp.livlog.numexp.durationExpressionNormalizer;

import java.util.ArrayList;
import java.util.List;

import jp.livlog.numexp.normalizerUtility.impl.LimitedExpressionTemplateImpl;

public class LimitedDurationExpression extends LimitedExpressionTemplateImpl {

    public List <String> correspondingTimePosition = new ArrayList <>();

    public List <String> processType               = new ArrayList <>();
}