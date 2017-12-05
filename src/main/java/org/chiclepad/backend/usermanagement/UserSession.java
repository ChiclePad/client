package org.chiclepad.backend.usermanagement;

import org.chiclepad.backend.entity.ChiclePadUser;

import java.time.LocalDateTime;

public class UserSession {
   private ChiclePadUser loggedUser;
   private int userId;
   private LocalDateTime lastLogInDate;

   public UserSession(final ChiclePadUser loggedUser, final int userId) {
      this.loggedUser = loggedUser;
      this.userId = userId;
   }

   public LocalDateTime getLastLogInDate() {
      return lastLogInDate;
   }

   public void setLastLogInDate(final LocalDateTime lastLogInDate) {
      this.lastLogInDate = lastLogInDate;
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
}
