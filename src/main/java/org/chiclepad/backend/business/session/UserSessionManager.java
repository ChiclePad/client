package org.chiclepad.backend.business.session;

public enum UserSessionManager {
    INSTANCE;

    private UserSession currentUserSession;

    public UserSession getCurrentUserSession() {
        return currentUserSession;
    }

    public void setCurrentUserSession(final UserSession currentUserSession) {
        this.currentUserSession = currentUserSession;
    }
}
