package jp.livlog.normalizeNumexp.inappropriateExpressionRemover;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import jp.livlog.normalizeNumexp.abstimeExpressionNormalizer.AbstimeExpression;
import jp.livlog.normalizeNumexp.durationExpressionNormalizer.DurationExpression;
import jp.livlog.normalizeNumexp.numericalExpressionNormalizer.NumericalExpression;
import jp.livlog.normalizeNumexp.reltimeExpressionNormalizer.ReltimeExpression;
import jp.livlog.normalizeNumexp.share.BaseExpressionTemplate;

public abstract class InappropriateExpressionRemover {

    public InappropriateExpressionRemover(String language) {

        this.language = language;
    }


    public abstract void removeInappropriateExtraction(
            String text,
            List <NumericalExpression> numexps,
            List <AbstimeExpression> abstimeexps,
            List <ReltimeExpression> reltimeexps,
            List <DurationExpression> durationexps);


    protected abstract <AnyTypeExpression extends BaseExpressionTemplate> void deleteInappropriateExtractionUsingDictionaryOneType(
            List <AnyTypeExpression> any_type_expressions);


    protected abstract <AnyTypeExpression extends BaseExpressionTemplate> boolean isUrlStrings(String text, AnyTypeExpression anyTypeExpression);


    protected abstract <AnyTypeExpression extends BaseExpressionTemplate> void deleteUrlStrings(String text,
            List <AnyTypeExpression> anyTypeExpressions);


    protected abstract void deleteInappropriateExtractionUsingDictionary(
            String text,
            List <NumericalExpression> numexps,
            List <AbstimeExpression> abstimeexps,
            List <ReltimeExpression> reltimeexps,
            List <DurationExpression> durationexps);


    protected abstract void initInappropriateStringss(String language);


    protected abstract void initUrlStrings();


    protected boolean isInappropriateStringsToBool(String val) {

        final var ret = this.inappropriateStringsToBool.get(val);
        if (ret == null) {
            return false;
        }
        return ret;
    }


    protected boolean isUrlStringsToBool(String val) {

        final var ret = this.inappropriateStringsToBool.get(val);
        if (ret == null) {
            return false;
        }
        return ret;
    }

    protected Map <String, Boolean> inappropriateStringsToBool = new TreeMap <>();

    protected Map <String, Boolean> urlStringsToBool           = new TreeMap <>();

    protected String                language;
}
