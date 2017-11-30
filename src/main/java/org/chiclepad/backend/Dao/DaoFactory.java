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

   // DAOs
   private CategoryDao categoryDao;
   private ChiclePadUserDao chiclePadUserDao;
   private DiaryPageDao diaryPageDao;
   private GoalDao goalDao;
   private NoteDao noteDao;
   private TodoDao todoDao;

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
      this.categoryDao = new CategoryDao(this.jdbcTemplate);
      this.chiclePadUserDao = new ChiclePadUserDao(this.jdbcTemplate);
      this.diaryPageDao = new DiaryPageDao(this.jdbcTemplate);
      this.goalDao = new GoalDao(this.jdbcTemplate);
      this.noteDao = new NoteDao(this.jdbcTemplate);
      this.todoDao = new TodoDao(this.jdbcTemplate);
   }

   public CategoryDao getCategoryDao() {
      return this.categoryDao;
   }

   public ChiclePadUserDao getChiclePadUserDao() {
      return this.chiclePadUserDao;
   }

   public DiaryPageDao getDiaryPageDao() {
      return this.diaryPageDao;
   }

   public GoalDao getGoalDao() {
      return this.goalDao;
   }

   public NoteDao getNoteDao() {
      return this.noteDao;
   }

   public TodoDao getTodoDao() {
      return this.todoDao;
   }

}
