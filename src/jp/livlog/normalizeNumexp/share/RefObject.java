package jp.livlog.normalizeNumexp.share;

public final class RefObject<T>
{
    public T argValue;
    public RefObject(T refArg)
    {
        this.argValue = refArg;
    }
}
