package com.puthi.commentapi;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

@RestController
public class DBHealthController {

    private final DataSource dataSource;

    public DBHealthController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @GetMapping(value = "/hello", produces = "application/json")
    public ResponseEntity<Map<String, String>> hello() {
        Map<String, String> body = new LinkedHashMap<>();
        body.put("name", "Puthi");
        body.put("age", "12");
        body.put("city", "Chennai");
        body.put("country", "India");
        body.put("message", "Hello, World!"); // last by insertion order

        return ResponseEntity.ok(body);
    }

    @GetMapping("/health/db")
    public ResponseEntity<Map<String, Object>> dbHealth() {
        Map<String, Object> body = new HashMap<>();
        body.put("component", "database");
        long t0 = System.nanoTime();
        try (Connection conn = dataSource.getConnection()) {
            boolean valid = conn.isValid(2);
            long ms = (System.nanoTime() - t0) / 1_000_000;
            body.put("status", valid ? "UP" : "DOWN");
            body.put("latencyMs", ms);
            if (valid) {
                body.put("databaseProduct", conn.getMetaData().getDatabaseProductName());
                body.put("databaseVersion", conn.getMetaData().getDatabaseProductVersion());
            }
            return new ResponseEntity<>(body, valid ? HttpStatus.OK : HttpStatus.SERVICE_UNAVAILABLE);
        } catch (Exception e) {
            body.put("status", "DOWN");
            body.put("error", e.getClass().getSimpleName() + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(body);
        }
    }
}