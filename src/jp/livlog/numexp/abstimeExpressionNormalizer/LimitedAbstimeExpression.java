package jp.livlog.numexp.abstimeExpressionNormalizer;

import java.util.ArrayList;
import java.util.List;

import jp.livlog.numexp.normalizerUtility.impl.LimitedExpressionTemplateImpl;
import lombok.ToString;

@ToString(callSuper=false)
public class LimitedAbstimeExpression extends LimitedExpressionTemplateImpl {

    public List <String> correspondingTimePosition = new ArrayList <>();

    public List <String> processType               = new ArrayList <>();
}
