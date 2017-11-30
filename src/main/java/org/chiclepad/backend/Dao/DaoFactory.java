package org.chiclepad.backend.Dao;

import org.chiclepad.backend.DatabaseManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.File;

/**
 * Factory providing data access objects for manipulation with entities
 */
public enum DaoFactory {

   /**
    * Singleton instance of the factory
    */
   INSTANCE;

   private JdbcTemplate jdbcTemplate;

   /**
    * Logger for reporting errors, and important events
    */
   private final Logger logger = LoggerFactory.getLogger(DaoFactory.class);

   /**
    *
    */
   private DaoFactory() {
      if (!DatabaseManager.INSTANCE.isConnected()) {
         DatabaseManager.INSTANCE.connect(new File("postgress/connection.properties"));
      }

      if (DatabaseManager.INSTANCE.isConnected()) {
         this.jdbcTemplate = DatabaseManager.INSTANCE.getConnection();
      }

      // TODO get dao instances.. or use Singletons
   }

   public ChiclePadUserDao getChiclePadUserDao() {
      return new ChiclePadUserDao(this.jdbcTemplate);
   }

}
