package jp.livlog.normalizeNumexp.abstimeExpressionNormalizer.impl;

import java.util.ArrayList;
import java.util.List;

import jp.livlog.normalizeNumexp.normalizerUtility.impl.LimitedExpressionTemplateImpl;
import lombok.ToString;

@ToString
public class LimitedAbstimeExpressionImpl extends LimitedExpressionTemplateImpl {

    public List <String> correspondingTimePosition = new ArrayList <>();

    public List <String> processType               = new ArrayList <>();
}
