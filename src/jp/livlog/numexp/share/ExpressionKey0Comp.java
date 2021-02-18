package jp.livlog.numexp.share;

import java.io.Serializable;
import java.util.Comparator;

/**
 * ExpressionKey0Compクラス.
 *
 * @author H.Aoshima
 * @version 1.0
 */
public class ExpressionKey0Comp implements Comparator <Expression>, Serializable {

    /**
     * シリアルバージョンUID.
     */
    private static final long serialVersionUID = 1L;

    @Override
    public int compare(final Expression arg0, final Expression arg1) {

        final var ret = arg0.positionStart - arg1.positionStart;

        return ret;
    }
}