package jp.livlog.normalizeNumexp.numericalExpressionNormalizer;

import java.util.List;

import jp.livlog.normalizeNumexp.digitUtility.DigitUtility;
import jp.livlog.normalizeNumexp.digitUtility.impl.DigitUtilityImpl;
import jp.livlog.normalizeNumexp.numericalExpressionNormalizer.impl.NumberExtractorImpl;
import jp.livlog.normalizeNumexp.numericalExpressionNormalizer.impl.SymbolFixerImpl;
import jp.livlog.normalizeNumexp.share.NNumber;

public abstract class NumberNormalizer {

    protected DigitUtility               digitUtility = null;

    protected NumberExtractor            NE           = null;

    protected InfNumberConverterTemplate NC           = null;

    protected SymbolFixer                SF           = null;

    public NumberNormalizer(String language) {

        this.language = language;
        this.digitUtility = new DigitUtilityImpl();
        this.digitUtility.initKansuji(language);
        this.NE = new NumberExtractorImpl(this.digitUtility);
        this.SF = new SymbolFixerImpl();
    }


    public abstract void process(String input, List <NNumber> output);


    // 絶対時間表現の規格化の際に使用する（絶対時間表現では、前もって記号を処理させないため）
    public abstract void processDontFixBySymbol(String input, List <NNumber> output);

    public String language;
}
