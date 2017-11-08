package org.chiclepad.backend.entity;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Application user
 */
public class ChiclePadUser {

    private final int id;

    private String email;

    private String password;

    private String salt;

    private final List<Entry> entries;

    private Optional<String> name = Optional.empty();

    private Optional<Locale> locale = Optional.empty();

    public ChiclePadUser(int id, String email, String password, String salt, List<Entry> entries) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.salt = salt;
        this.entries = entries;
    }

    public ChiclePadUser(int id, String email, String password, List<Entry> entries, String salt, Locale locale) {
        this(id, email, password, salt, entries);
        this.locale = Optional.ofNullable(locale);
    }

    public ChiclePadUser(int id, String email, String password, List<Entry> entries, String salt, String name) {
        this(id, email, password, salt, entries);
        this.name = Optional.ofNullable(name);
    }

    public ChiclePadUser(int id, String email, String password, List<Entry> entries, String salt, Locale locale, String name) {
        this(id, email, password, salt, entries);
        this.locale = Optional.ofNullable(locale);
        this.name = Optional.ofNullable(name);
    }

    public boolean checkPassword(String submittedPassword) {

    }

    public void generatePassword(String submittedPassword) {

    }

    public String getEmail() {
        return email;
    }

    public List<Entry> getEntries() {
        return entries;
    }

    public Optional<String> getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Optional.ofNullable(name);
    }

    public Optional<Locale> getLocale() {
        return locale;
    }

    public void setName(Optional<String> name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public void setLocale(Optional<Locale> locale) {
        this.locale = locale;
    }

    public void setLocale(Locale locale) {
        this.locale = Optional.ofNullable(locale);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChiclePadUser that = (ChiclePadUser) o;
        return id == that.id && email.equals(that.email);
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "ChiclePadUser{" +
                "id=" + id +
                ", email='" + email + '\'' +
                '}';
    }
}
