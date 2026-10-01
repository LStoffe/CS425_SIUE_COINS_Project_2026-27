package edu.SIUE.config;

import edu.calpoly.its.mas.big.db.BIGDatabaseFacade;
import edu.calpoly.its.mas.big.db.BIGDatabaseFacadeInterface;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
 * LegacyConfig.Java
 * 
 * This configuration file provides Spring beans for legacy system support,
 * specifically exposing BIGDatabaseFacade as a bean implementing
 * BIGDatabaseFacadeInterface. Used for integrating legacy database
 * interactions within the modernized system context.
 *
 * Created for backward compatibility — avoid direct modification unless
 * necessary for migration or legacy integration.
 */

@Configuration
public class LegacyConfig {
    
    /**
     * Creates and exposes a BIGDatabaseFacade bean as BIGDatabaseFacadeInterface.
     * Handles and logs initialization exceptions.
     *
     * @return an instance of BIGDatabaseFacadeInterface
     * @throws RuntimeException if the database facade cannot be initialized
     */
    @Bean
    public BIGDatabaseFacadeInterface databaseFacade() {
        try {
            // Attempt to create the database facade
           
            BIGDatabaseFacade facade = new BIGDatabaseFacade();

            return facade;
        } catch (Exception e) {
            System.err.println("❌ Failed to create BIGDatabaseFacade: " + e.getMessage());
            e.printStackTrace();
            return null;  // MUST return null, not throw
        }
    }
}