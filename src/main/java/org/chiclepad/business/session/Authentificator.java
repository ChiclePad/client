package org.chiclepad.business.session;

import org.chiclepad.backend.Dao.ChiclePadUserDao;
import org.chiclepad.backend.Dao.DaoFactory;
import org.chiclepad.backend.entity.ChiclePadUser;
import org.chiclepad.business.UserSessionManager;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.bcrypt.BCrypt;

public enum Authentificator {

   INSTANCE;

   private ChiclePadUserDao userDao = DaoFactory.INSTANCE.getChiclePadUserDao();

   public UserSession logIn(String email, String password) throws BadPasswordException, EmptyResultDataAccessException {
      ChiclePadUser user = userDao.get(email);

      if (!BCrypt.checkpw(password, user.getPassword())) {
         throw new BadPasswordException();
      }

      UserSession userSession = new UserSession(user, user.getId());
      UserSessionManager.INSTANCE.setCurrentUserSession(userSession);

      return userSession;
   }

   public UserSession register(String email, String password)
         throws UserAlreadyExistsException {
      if (userExists(email)) {
         throw new UserAlreadyExistsException();
      }

      String salt = BCrypt.gensalt();
      String hashedPassword = BCrypt.hashpw(password, salt);

      ChiclePadUser createdUser = userDao.create(email, hashedPassword);
      UserSession userSession = new UserSession(createdUser, createdUser.getId());
      UserSessionManager.INSTANCE.setCurrentUserSession(userSession);

      return userSession;
   }

   private boolean userExists(String email) {
      try {
         userDao.get(email);
         return true;

      } catch (EmptyResultDataAccessException ignored) {
         return false;
      }
   }

}
