package org.chiclepad.backend.business.session;

import org.chiclepad.backend.Dao.ChiclePadUserDao;
import org.chiclepad.backend.Dao.DaoFactory;
import org.chiclepad.backend.entity.ChiclePadUser;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.bcrypt.BCrypt;

public enum Authenticator {

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

        String hashedPassword = hashPassword(password);

        ChiclePadUser createdUser = userDao.create(email, hashedPassword);
        UserSession userSession = new UserSession(createdUser, createdUser.getId());
        UserSessionManager.INSTANCE.setCurrentUserSession(userSession);

        return userSession;
    }

    public String hashPassword(String plainText) {
        String salt = BCrypt.gensalt();
        return BCrypt.hashpw(plainText, salt);
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
