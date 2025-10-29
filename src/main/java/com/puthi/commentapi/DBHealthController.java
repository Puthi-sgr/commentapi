package com.puthi.commentapi;

import com.puthi.commentapi.dto.health.DbHealthResponse;
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
    public ResponseEntity<DbHealthResponse> dbHealth() {
        long t0 = System.nanoTime();
        try (Connection conn = dataSource.getConnection()) {
            boolean valid = conn.isValid(2);
            long ms = (System.nanoTime() - t0) / 1_000_000;
            if (valid) {
                return new ResponseEntity<>(
                        new DbHealthResponse(
                                "database",
                                "UP",
                                ms,
                                conn.getMetaData().getDatabaseProductName(),
                                conn.getMetaData().getDatabaseProductVersion(),
                                null
                        ),
                        HttpStatus.OK
                );
            } else {
                return new ResponseEntity<>(
                        new DbHealthResponse("database", "DOWN", ms, null, null, null),
                        HttpStatus.SERVICE_UNAVAILABLE
                );
            }
        } catch (Exception e) {
            long ms = (System.nanoTime() - t0) / 1_000_000;
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(new DbHealthResponse("database", "DOWN", ms, null, null, e.getClass().getSimpleName() + ": " + e.getMessage()));
        }
    }
}