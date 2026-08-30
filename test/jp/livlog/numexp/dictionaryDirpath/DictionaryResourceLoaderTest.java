package jp.livlog.numexp.dictionaryDirpath;

import java.io.BufferedReader;

import org.junit.jupiter.api.Test;

class DictionaryResourceLoaderTest {

    @Test
    void fallsBackToJapaneseDictionary() throws Exception {

        try (var reader = new BufferedReader(
                DictionaryResourceLoader.load("/dic/zh/duration_expression_json.txt"))) {
            org.junit.Assert.assertNotNull(reader.readLine());
        }
    }


    @Test
    void reportsMissingDictionaryPath() {

        final var exception = org.junit.jupiter.api.Assertions.assertThrows(
                IllegalStateException.class,
                () -> DictionaryResourceLoader.load("/dic/ja/not-found.txt"));
        org.junit.Assert.assertTrue(exception.getMessage().contains("/dic/ja/not-found.txt"));
    }
}
