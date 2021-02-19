package jp.livlog.numexp.share;

import java.util.ArrayList;
import java.util.List;

import lombok.ToString;

@ToString(callSuper=false)
public class NNumber extends BaseExpressionTemplate {

    public NNumber() {

        this.originalExpression = "";
        this.positionStart = -1;
        this.positionEnd = -1;
        this.valueLowerbound = NumexpSymbol.INFINITY;
        this.valueUpperbound = -NumexpSymbol.INFINITY;
        this.notationType = new ArrayList <>();
    }


    public NNumber(String originalExpression, int positionStart, int positionEnd) {

        this.originalExpression = originalExpression;
        this.positionStart = positionStart;
        this.positionEnd = positionEnd;
        this.valueLowerbound = NumexpSymbol.INFINITY;
        this.valueUpperbound = -NumexpSymbol.INFINITY;
        this.notationType = new ArrayList <>();
    }

    public String               originalExpression = null;

    public double               valueLowerbound;

    public double               valueUpperbound;

    public List <ENotationType> notationType;
}
