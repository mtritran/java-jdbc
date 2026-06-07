package org.example.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;
import java.util.stream.Collectors;

public class DDLRunner {

    public static void runSchemaScript() {
        System.out.println("Initializing database schema from schema.sql...");
        try (InputStream is = DDLRunner.class.getClassLoader().getResourceAsStream("db/schema.sql")) {
            if (is == null) {
                System.err.println("schema.sql not found in resources!");
                return;
            }

            String sql;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                sql = reader.lines().collect(Collectors.joining("\n"));
            }

            String[] queries = sql.split(";");

            try (Connection conn = DBConnection.getConnection();
                 Statement stmt = conn.createStatement()) {
                for (String query : queries) {
                    String trimmedQuery = query.trim();
                    if (!trimmedQuery.isEmpty()) {
                        stmt.execute(trimmedQuery);
                    }
                }
                System.out.println("Database schema initialized successfully!");
            }
        } catch (Exception e) {
            System.err.println("Failed to run schema.sql DDL script: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
