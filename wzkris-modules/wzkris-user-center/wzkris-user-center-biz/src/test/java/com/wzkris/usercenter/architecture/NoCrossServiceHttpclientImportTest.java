package com.wzkris.usercenter.architecture;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class NoCrossServiceHttpclientImportTest {

    private static final Path SRC = Path.of("src/main/java");

    private static final List<Pattern> FORBIDDEN = List.of(
            Pattern.compile("^\\s*import\\s+com\\.wzkris\\.system\\.remote\\..*;\\s*$"),
            Pattern.compile("^\\s*import\\s+com\\.wzkris\\.captcha\\.remote\\..*;\\s*$")
    );

    @Test
    void shouldNotImportHttpclientFromOtherServices() throws IOException {
        try (Stream<Path> files = Files.walk(SRC)) {
            List<String> violations = files
                    .filter(p -> p.toString().endsWith(".java"))
                    .flatMap(NoCrossServiceHttpclientImportTest::scanFile)
                    .collect(Collectors.toList());

            assertTrue(violations.isEmpty(), "Forbidden cross-service httpclient imports found:\n" + String.join("\n", violations));
        }
    }

    private static Stream<String> scanFile(Path path) {
        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            return lines.stream()
                    .filter(NoCrossServiceHttpclientImportTest::isForbiddenImport)
                    .map(line -> path + " :: " + line.trim());
        } catch (IOException ex) {
            throw new RuntimeException("Failed to read file: " + path, ex);
        }
    }

    private static boolean isForbiddenImport(String line) {
        return FORBIDDEN.stream().anyMatch(p -> p.matcher(line).matches());
    }

}
