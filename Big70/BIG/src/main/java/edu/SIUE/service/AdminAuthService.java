package edu.SIUE.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * AdminAuthService.java
 *
 * Service responsible for administrator authentication and management within the SIUE BIG simulation platform.
 *
 * Features:
 *   - Authenticates admin users by verifying credentials against the database.
 *   - Supports both BCrypt hashed passwords (current standard) and legacy plain text passwords for backward compatibility.
 *   - Provides methods related to admin login and profile retrieval.
 *
 * Password Security:
 *   - All newly created or updated admin passwords are stored as BCrypt hashes.
 *   - When an admin attempts to log in, legacy plain text passwords are supported for accounts that have not yet upgraded;
 *     successful login may be used to trigger an upgrade to a hashed password outside this service.
 *
 */

@Service
public class AdminAuthService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Authenticates an admin user by verifying the username and password.
     * 
     * @param username The username of the admin attempting to log in.
     * @param password The password provided for authentication.
     * @return A map representing the admin's information if authentication is successful, or null otherwise.
     */
    public Map<String, Object> authenticateAdmin(String username, String password) {
        try {
            // Prepare SQL query to fetch admin details for the provided username.
            String sql = """
                    SELECT adminid, username, password, is_head_admin, email
                    FROM public.admin
                    WHERE username = ?
                    ORDER BY adminid DESC
                    LIMIT 1
                    """;

            // Execute query to retrieve matching admin records from the database.
            List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, username);

            // If no admin with the given username exists, return null.
            if (rows.isEmpty()) {
                return null;
            }

            // Get the first (most recently created) admin record.
            Map<String, Object> admin = rows.get(0);
            String storedPassword = (String) admin.get("password");

            // If no password is stored for this admin (should not occur), fail authentication.
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

            // Upgrade legacy plain-text password to BCrypt after successful sign-in.
            if (!isBcryptHash(storedPassword)) {
                Number adminIdNumber = (Number) admin.get("adminid");
                if (adminIdNumber != null) {
                    try {
                        jdbcTemplate.update(
                            "UPDATE public.admin SET password = ? WHERE adminid = ?",
                            passwordEncoder.encode(password),
                            adminIdNumber.intValue()
                        );
                    } catch (Exception ex) {
                        System.err.println("Failed to upgrade admin password to hash: " + ex.getMessage());
                    }
                }
            }

           
            return admin;
        } catch (Exception e) {
            // Print error details and return null to indicate authentication failure.
            System.err.println("Admin authentication error: " + e.getMessage());
            return null;
        }
    }

    /**
     * Registers a new admin in the database.
     * 
     * @param username The username of the new admin.
     * @param password The password of the new admin.
     * @param email    The email address of the new admin.
     * @return true if the admin was registered successfully, false if the username already exists or an error occurs.
     */

    public boolean registerAdmin(String username, String password, String email) {
        try {
            // Check if the username already exists
            String checkSql = "SELECT COUNT(*) FROM public.admin WHERE username = ?";
            int count = jdbcTemplate.queryForObject(checkSql, Integer.class, username);
            if (count > 0) return false; // Username already exists, registration failed

            // Insert the new admin into the database
            String insertSql = """
                INSERT INTO public.admin (adminid, username, password, email, is_head_admin)
                VALUES (
                    (SELECT COALESCE(MAX(adminid), 0) + 1 FROM public.admin),
                    ?, ?, ?, false
                )
                """;
            String encodedPassword = passwordEncoder.encode(password);
            jdbcTemplate.update(insertSql, username, encodedPassword, email);
            return true; // Registration succeeded

        } catch (Exception e) {
            // Log error and return failure
            System.err.println("registerAdmin error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Updates the email address for the specified admin.
     * 
     * @param adminId The ID of the admin whose email is to be updated.
     * @param email   The new email address to set. May be blank or null to clear the address.
     * @return true if the email was updated successfully, false if an error occurs.
     */

    public boolean updateEmail(int adminId, String email) {
        try {
            // Update the email address for the given admin ID
            String sql = "UPDATE public.admin SET email = ? WHERE adminid = ?";
            jdbcTemplate.update(sql, email, adminId);
            return true;
        } catch (Exception e) {
            // Log error and return failure
            System.err.println("updateEmail error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Updates the password for the specified admin, after verifying the old password.
     *
     * @param adminId     The ID of the admin whose password is being changed.
     * @param oldPassword The current password, to verify identity.
     * @param newPassword The desired new password.
     * @return true if password was updated successfully; false if old password does not match or error occurs.
     */
    public boolean updatePassword(int adminId, String oldPassword, String newPassword) {
        try {
            // Query for the current password for this admin from the database
            String checkSql = "SELECT password FROM public.admin WHERE adminid = ?";
            String storedPassword = jdbcTemplate.queryForObject(checkSql, String.class, adminId);

            // Verify old password (BCrypt or legacy plain text).
            boolean oldMatches = storedPassword == null ? false
                : isBcryptHash(storedPassword)
                    ? passwordEncoder.matches(oldPassword, storedPassword)
                    : storedPassword.equals(oldPassword);
            if (!oldMatches) {
                return false;
            }

            // Store new password as BCrypt hash.
            String updateSql = "UPDATE public.admin SET password = ? WHERE adminid = ?";
            jdbcTemplate.update(updateSql, passwordEncoder.encode(newPassword), adminId);
            return true;

        } catch (Exception e) {
            // Log the error and indicate failure
            System.err.println("updatePassword error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves all games owned by a given admin, sorted by most recent first.
     *
     * @param adminId The ID of the admin whose games to return.
     * @return A list of maps, each containing details of a game. Returns empty list if error occurs.
     */
    public List<Map<String, Object>> getGamesByAdmin(int adminId) {
        try {
            // Query for all games owned by this admin, ordered by gameid descending (most recent first)
            String sql = """
                SELECT gameid, gamename, gametype, start_year, current_period, is_active
                FROM public.game
                WHERE adminid = ?
                ORDER BY gameid DESC
                """;
            return jdbcTemplate.queryForList(sql, adminId);
        } catch (Exception e) {
            // Log the error and return an empty list if an exception occurs
            System.err.println("getGamesByAdmin error: " + e.getMessage());
            return new java.util.ArrayList<>();
        }
    }

    /** Returns true if the stored value looks like a BCrypt hash (e.g. starts with $2a$ or $2b$). */
    private static boolean isBcryptHash(String stored) {
        return stored != null && stored.length() >= 60 && stored.startsWith("$2");
    }

}

