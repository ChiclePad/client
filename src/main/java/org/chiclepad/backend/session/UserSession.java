package org.chiclepad.backend.session;

import org.chiclepad.backend.entity.ChiclePadUser;

import java.time.LocalDateTime;

public class UserSession {

   private ChiclePadUser loggedUser;
   private int userId;
   private LocalDateTime registrationDate;
   private LocalDateTime lastLogInDate;

   UserSession(final ChiclePadUser loggedUser, final int userId) {
      this.loggedUser = loggedUser;
      this.userId = userId;
   }

   public LocalDateTime getRegistrationDate() {
      return registrationDate;
   }

   public void setRegistrationDate(final LocalDateTime registrationDate) {
      this.registrationDate = registrationDate;
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

   public void setUserId(final int userId) {
      this.userId = userId;
   }
}
