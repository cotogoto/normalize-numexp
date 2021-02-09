package jp.livlog.normalizeNumexp.numericalExpressionNormalizer;

import jp.livlog.normalizeNumexp.normalizerTemplate.NormalizerTemplate;
import jp.livlog.normalizeNumexp.numberNormalizer.NumberNormalizer;
import jp.livlog.normalizeNumexp.numberNormalizer.impl.NumberNormalizerImpl;

public abstract class NumericalExpressionNormalizer extends NormalizerTemplate <NumericalExpression, Counter> {

    public NumericalExpressionNormalizer(final String language) {

        this.NN = new NumberNormalizerImpl(language);
        this.language = language;
        this.init();
    }

    // C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
    // void init();
    // C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
    // void normalize_number(final String text, java.util.ArrayList<digit_utility::Number> numbers);
    // C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
    // void revise_any_type_expression_by_matching_limited_expression(java.util.ArrayList<NumericalExpression> numexps, tangible.RefObject<int>
    // expression_id, Counter matching_limited_expression);
    // C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
    // void revise_any_type_expression_by_matching_prefix_counter(NumericalExpression numexps, final Counter matching_limited_expression);
    // C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
    // void revise_any_type_expression_by_number_modifier(NumericalExpression numexp, final normalizer_utility::NumberModifier number_modifier);
    // C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
    // void delete_not_any_type_expression(java.util.ArrayList<NumericalExpression> numexps);
    // C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
    // void fix_by_range_expression(final pfi::data::string::ustring utext, java.util.ArrayList<NumericalExpression> numexps);

    public NumberNormalizer NN;
}
