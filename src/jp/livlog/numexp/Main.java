package jp.livlog.numexp;

import jp.livlog.numexp.normalizeNumexp.NormalizeNumexp;
import jp.livlog.numexp.normalizeNumexp.impl.NormalizeNumexpImpl;

public class Main {

    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: java jp.livlog.numexp.Main <language> <text>");
            return;
        }

        final var language = args[0];
        final var text = args[1];

        final NormalizeNumexp normalizeNumexp = new NormalizeNumexpImpl(language);
        final var result = normalizeNumexp.normalize(text);

        for (final String line : result) {
            System.out.println(line);
        }
    }

}
