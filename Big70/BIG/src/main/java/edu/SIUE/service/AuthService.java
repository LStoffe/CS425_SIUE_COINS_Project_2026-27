package edu.SIUE.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * AuthService.java
 *
 * Service responsible for authenticating company users within the SIUE BIG simulation platform.
 *
 * Features:
 *   - Authenticates company users by verifying provided credentials against records in the company database table.
 *   - Supports both BCrypt hashed passwords (recommended standard) and legacy plain text passwords for backward compatibility.
 *   - Automatically upgrades plain text passwords to BCrypt hash upon successful login.
 *   - Provides helper methods for password verification and migration to improved password security.
 *
 * Password Security:
 *   - All newly created or updated company passwords are stored as BCrypt hashes for security.
 *   - During login, the service gracefully supports legacy logins by accepting plain text passwords if the account has not yet migrated.
 *   - A successful login using a plain text password automatically upgrades the database record to a secure hash.
 *
 */

@Service
public class AuthService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    /**
     * Authenticate company against existing company table
     */
    public Map<String, Object> authenticatecompany(String username, String password) {
        try {
            // In the BIG schema, a company is identified by (companyid, gameid). Username is not unique.
            // We pick the most recent game row for that username.
            String sql = """
                    SELECT companyid, gameid, username, password
                    FROM public.company
                    WHERE username = ?
                    ORDER BY gameid DESC
                    LIMIT 1
                    """;

            List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, username);
            if (rows.isEmpty()) {
                return null;
            }

            Map<String, Object> company = rows.get(0);
            String storedPassword = (String) company.get("password");
            if (storedPassword == null) {
                return null;
            }

            // Verify: BCrypt hash or legacy plain text (for backward compatibility).
            boolean matches = isBcryptHash(storedPassword)
                ? passwordEncoder.matches(password, storedPassword)
                : storedPassword.equals(password);
            if (!matches) {
                return null;
            }

            // Upgrade plain-text password to BCrypt on successful login so next sign-in uses hash.
            if (!isBcryptHash(storedPassword)) {
                Integer companyid = (Integer) company.get("companyid");
                Integer gameid = (Integer) company.get("gameid");
                if (companyid != null && gameid != null) {
                    try {
                        String encoded = passwordEncoder.encode(password);
                        jdbcTemplate.update(
                            "UPDATE public.company SET password = ? WHERE companyid = ? AND gameid = ?",
                            encoded, companyid, gameid);
                    } catch (Exception ex) {
                        System.err.println("Failed to upgrade company password to hash: " + ex.getMessage());
                    }
                }
            }

            return company;
        } catch (Exception e) {
            System.err.println("Authentication error: " + e.getMessage());
        }

        return null;
    }

    /** Returns true if the stored value looks like a BCrypt hash (e.g. starts with $2a$ or $2b$). */
    private static boolean isBcryptHash(String stored) {
        return stored != null && stored.length() >= 60 && stored.startsWith("$2");
    }
}