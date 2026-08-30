package jp.livlog.numexp.dictionaryDirpath;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

public final class DictionaryResourceLoader {

    private DictionaryResourceLoader() {
    }


    public static Reader load(String dictionaryPath) {

        var resolvedPath = dictionaryPath;
        var inputStream = DictionaryResourceLoader.class.getResourceAsStream(resolvedPath);
        if (inputStream == null) {
            resolvedPath = resolvedPath.replace("/zh/", "/ja/");
            resolvedPath = resolvedPath.replace("/en/", "/ja/");
            inputStream = DictionaryResourceLoader.class.getResourceAsStream(resolvedPath);
        }
        if (inputStream == null) {
            throw new IllegalStateException("Dictionary resource not found: " + resolvedPath);
        }
        return new InputStreamReader(inputStream, StandardCharsets.UTF_8);
    }
}
