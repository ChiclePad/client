package org.chiclepad.backend.business.session;

import org.chiclepad.backend.entity.ChiclePadUser;

import java.time.LocalDateTime;

public class UserSession {

    private int userId;
    private ChiclePadUser loggedUser;
    private LocalDateTime logInDate;

    UserSession(final ChiclePadUser loggedUser, final int userId) {
        this.loggedUser = loggedUser;
        this.userId = userId;
        this.logInDate = LocalDateTime.now();
    }

    public LocalDateTime getLogInDate() {
        return logInDate;
    }

    public void setLogInDate(final LocalDateTime logInDate) {
        this.logInDate = logInDate;
    }

    public ChiclePadUser getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(final ChiclePadUser loggedUser) {
        this.loggedUser = loggedUser;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(final int userId) {
        this.userId = userId;
    }

}
