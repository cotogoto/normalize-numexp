package jp.livlog.numexp;

import java.io.PrintStream;

import jp.livlog.numexp.normalizeNumexp.NormalizeNumexp;
import jp.livlog.numexp.normalizeNumexp.impl.NormalizeNumexpImpl;

public class Main {

    public static void main(String[] args) {

        final var exitCode = Main.run(args, System.out, System.err);
        if (exitCode != 0) {
            System.exit(exitCode);
        }
    }


    static int run(String[] args, PrintStream out, PrintStream err) {

        if (args.length < 2) {
            err.println("Usage: java jp.livlog.numexp.Main <language> <text>");
            return 2;
        }

        final var language = args[0];
        final var text = args[1];

        final NormalizeNumexp normalizeNumexp = new NormalizeNumexpImpl(language);
        final var result = normalizeNumexp.normalize(text);

        for (final String line : result) {
            out.println(line);
        }
        return 0;
    }

}
