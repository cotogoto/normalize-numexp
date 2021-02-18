package jp.livlog.numexp.share;

import lombok.ToString;

@ToString(callSuper=false)
public final class Pair <T1, T2> {

    public T1 first;

    public T2 second;

    public Pair() {

        this.first = null;
        this.second = null;
    }


    public Pair(T1 firstValue, T2 secondValue) {

        this.first = firstValue;
        this.second = secondValue;
    }


    public Pair(Pair <T1, T2> pairToCopy) {

        this.first = pairToCopy.first;
        this.second = pairToCopy.second;
    }
}