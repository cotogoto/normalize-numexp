package jp.livlog.numexp;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

class MainTest {

    @Test
    void returnsUsageErrorForMissingArguments() {

        final var out = new ByteArrayOutputStream();
        final var err = new ByteArrayOutputStream();
        final var exitCode = Main.run(
                new String[] {"ja"},
                new PrintStream(out, true, StandardCharsets.UTF_8),
                new PrintStream(err, true, StandardCharsets.UTF_8));

        org.junit.Assert.assertEquals(2, exitCode);
        org.junit.Assert.assertEquals("", out.toString(StandardCharsets.UTF_8));
        org.junit.Assert.assertTrue(err.toString(StandardCharsets.UTF_8).startsWith("Usage:"));
    }


    @Test
    void writesResultsAndReturnsSuccess() {

        final var out = new ByteArrayOutputStream();
        final var err = new ByteArrayOutputStream();
        final var exitCode = Main.run(
                new String[] {"ja", "3人"},
                new PrintStream(out, true, StandardCharsets.UTF_8),
                new PrintStream(err, true, StandardCharsets.UTF_8));

        org.junit.Assert.assertEquals(0, exitCode);
        org.junit.Assert.assertTrue(out.toString(StandardCharsets.UTF_8).contains("numerical*3人"));
        org.junit.Assert.assertEquals("", err.toString(StandardCharsets.UTF_8));
    }
}
