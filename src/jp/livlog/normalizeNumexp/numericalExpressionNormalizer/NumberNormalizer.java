package jp.livlog.normalizeNumexp.numericalExpressionNormalizer;

import java.util.List;

import jp.livlog.normalizeNumexp.digitUtility.DigitUtility;
import jp.livlog.normalizeNumexp.digitUtility.impl.DigitUtilityImpl;
import jp.livlog.normalizeNumexp.share.NNumber;

public abstract class NumberNormalizer {

    protected DigitUtility digitUtility = new DigitUtilityImpl();

    public NumberNormalizer(String language) {

        this.language = language;
        this.digitUtility.initKansuji(language);
    }


    public abstract void process(String input, List <NNumber> output);


    // 絶対時間表現の規格化の際に使用する（絶対時間表現では、前もって記号を処理させないため）
    public abstract void processDontFixBySymbol(String input, List <NNumber> output);

    public String language;
}
