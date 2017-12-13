package org.chiclepad.business;

import org.chiclepad.business.session.UserSession;

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
