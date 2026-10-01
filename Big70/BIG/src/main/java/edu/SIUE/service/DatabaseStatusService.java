package edu.SIUE.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * DatabaseStatusService.java
 *
 * Service for monitoring the health and status of the PostgreSQL database connection within the SIUE BIG simulation platform.
 *
 * Responsibilities:
 *   - Provides a simple connectivity check to determine if the PostgreSQL database is reachable.
 *   - Supplies basic status information, such as current company row count, for use in admin dashboards or main menus.
 *
 * Typical Usage:
 *   - Invoked by front-end components to display database connectivity and stats (e.g., "Database: Connected").
 *   - Used during health checks and status endpoints of the application.
 *
 * Developed for the SIUE BIG simulation web application.
 */

@Service
public class DatabaseStatusService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Returns true if the database is reachable.
     */
    public boolean isConnected() {
        try {
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Returns the number of rows in the company table, or -1 if table is missing or error.
     */
    public int getCompanyCount() {
        try {
            Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM company", Integer.class);
            return count != null ? count : -1;
        } catch (Exception e) {
            return -1;
        }
    }
}
