package org.chiclepad.backend.session;

import org.chiclepad.backend.Dao.ChiclePadUserDao;
import org.chiclepad.backend.Dao.DaoFactory;
import org.chiclepad.backend.entity.ChiclePadUser;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.time.LocalDateTime;

public class Authentificator {

   ChiclePadUserDao chiclePadUserDao = DaoFactory.INSTANCE.getChiclePadUserDao();

   private UserSession logIn(String email, String password) {

      ChiclePadUser user = chiclePadUserDao.get(email);
      int id = user.getId();

      // Check password
      String hashFromGivenPassword = BCrypt.hashpw(password, user.getSalt());
      if (!hashFromGivenPassword.equals(user.getPassword())) {
         throw new BadPasswordException();
      }
      UserSession userSession = new UserSession(user, user.getId());
      userSession.setLastLogInDate(LocalDateTime.now());
      return userSession;
   }

   private UserSession register(String email, String password) {

      // Check that user does not exist
      try {
         chiclePadUserDao.get(email);
         throw new UserAlreadyExistsException();
      } catch (EmptyResultDataAccessException e) {

      }

      // Generate salt for the user
      String salt = BCrypt.gensalt();

      // Hash password with SHA-256 algorithm
      String hash = BCrypt.hashpw(password, salt);

      // Save the user in the DB
      ChiclePadUser createdUser = chiclePadUserDao.create(email, hash, salt);

      UserSession userSession = new UserSession(createdUser, createdUser.getId());
      userSession.setRegistrationDate(LocalDateTime.now());

      return userSession;
   }
}
