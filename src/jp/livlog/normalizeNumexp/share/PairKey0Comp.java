package jp.livlog.normalizeNumexp.share;

import java.io.Serializable;
import java.util.Comparator;

/**
 * PairKey0Compクラス.
 *
 * @author H.Aoshima
 * @version 1.0
 */
public class PairKey0Comp <T1, T2> implements Comparator <Pair <T1, T2>>, Serializable {

    /**
     * シリアルバージョンUID.
     */
    private static final long serialVersionUID = 1L;

    @Override
    public int compare(final Pair <T1, T2> arg0, final Pair <T1, T2> arg1) {

        final var ret = arg0.toString().compareTo(arg1.toString());

        return ret;
    }
}