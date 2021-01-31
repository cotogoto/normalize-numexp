package jp.livlog.normalizeNumexp.abstimeExpressionNormalizer;

import java.util.ArrayList;

import jp.livlog.normalizeNumexp.normalizerUtility.NormalizerUtility;
import jp.livlog.normalizeNumexp.share.Symbol;

public class AbstimeExpressionNormalizer {

    public class AbstimeExpression extends NormalizerUtility.NormalizedExpressionTemplate {

        public AbstimeExpression(jp.livlog.normalizeNumexp.digitUtility.DigitUtility.Number number) {

            super(number.originalExpression, number.positionStart, number.positionEnd);
            this.orgValueLowerbound = number.valueLowerbound;
            this.orgValueUpperbound = number.valueUpperbound;
            this.valueLowerbound = new NormalizerUtility.Time(Symbol.INFINITY);
            this.valueUpperbound = new NormalizerUtility.Time(-Symbol.INFINITY);
            this.ordinary = false;
        }

        public double                 orgValueLowerbound;

        public double                 orgValueUpperbound;

        public NormalizerUtility.Time valueLowerbound;

        public NormalizerUtility.Time valueUpperbound;

        public boolean                ordinary;
    }

    public abstract class LimitedAbstimeExpression extends NormalizerUtility.LimitedExpressionTemplate {

        public ArrayList <String> corresponding_time_position = new ArrayList <>();

        public ArrayList <String> process_type                = new ArrayList <>();
    }


//    public AbstimeExpressionNormalizer(final String language)
//    {
//        this.NN = language;
//        language_ = language;
//        init();
//    }

  //C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
  //  void init();
  //C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
  //  void normalize_number(final String text, java.util.ArrayList<digit_utility::Number> numbers);
  //C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
  //  void revise_any_type_expression_by_matching_limited_expression(java.util.ArrayList<AbstimeExpression> abstimeexps, tangible.RefObject<int> expression_id, LimitedAbstimeExpression matching_limited_abstime_expression);
  //C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
  //  void revise_any_type_expression_by_matching_prefix_counter(AbstimeExpression any_type_expression, final LimitedAbstimeExpression matching_limited_expression);
  //C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
  //  void revise_any_type_expression_by_number_modifier(AbstimeExpression abstimeexp, final normalizer_utility::NumberModifier number_modifier);
  //C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
  //  void delete_not_any_type_expression(java.util.ArrayList<AbstimeExpression> abstimeexps);
  //C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
  //  void fix_by_range_expression(final pfi::data::string::ustring utext, java.util.ArrayList<AbstimeExpression> abstimeexps);

//    private number_normalizer.NumberNormalizer NN = new number_normalizer.NumberNormalizer();

}
