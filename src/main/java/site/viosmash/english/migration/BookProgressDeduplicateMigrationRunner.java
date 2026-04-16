package site.viosmash.english.migration;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Runs one-off migration to deduplicate book_progress rows and add unique key (user_id, book_id).
 * Enabled only under Spring profile "migration".
 */
@Component
@Profile("migration")
@RequiredArgsConstructor
public class BookProgressDeduplicateMigrationRunner implements ApplicationRunner {

    private final JdbcTemplate jdbcTemplate;
    private final ConfigurableApplicationContext applicationContext;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Path sqlPath = Path.of("sql", "book_progress_deduplicate.sql");
        String raw = Files.readString(sqlPath, StandardCharsets.UTF_8);

        List<String> statements = splitSqlStatements(stripLineComments(raw));
        for (String stmt : statements) {
            jdbcTemplate.execute(stmt);
        }

        int exitCode = SpringApplication.exit(applicationContext, () -> 0);
        System.exit(exitCode);
    }

    private static String stripLineComments(String sql) {
        StringBuilder out = new StringBuilder(sql.length());
        for (String line : sql.split("\\R", -1)) {
            String trimmed = line.trim();
            if (trimmed.startsWith("--")) {
                continue;
            }
            out.append(line).append('\n');
        }
        return out.toString();
    }

    private static List<String> splitSqlStatements(String sql) {
        List<String> out = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        for (char ch : sql.toCharArray()) {
            if (ch == ';') {
                String stmt = current.toString().trim();
                if (!stmt.isBlank()) {
                    out.add(stmt);
                }
                current.setLength(0);
            } else {
                current.append(ch);
            }
        }
        String last = current.toString().trim();
        if (!last.isBlank()) {
            out.add(last);
        }
        return out;
    }
}

