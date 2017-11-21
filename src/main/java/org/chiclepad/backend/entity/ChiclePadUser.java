package org.chiclepad.backend.entity;

import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Application user
 */
public class ChiclePadUser {

    /**
     * User id
     */
    private final int id;

    /**
     * Unique email used as login
     */
    private String email;

    /**
     * Hashed password with salt
     */
    private String password;

    /**
     * Salt added to password
     */
    private String salt;

    /**
     * Users entries
     */
    private final List<Entry> entries;

    /**
     * Name of the user
     */
    private Optional<String> name = Optional.empty();

    /**
     * Locale of the user
     */
    private Optional<Locale> locale = Optional.empty();

    /**
     * Basic constructor
     */
    public ChiclePadUser(int id, String email, String password, String salt, List<Entry> entries) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.salt = salt;
        this.entries = entries;
    }

    /**
     * Basic constructor
     */
    public ChiclePadUser(int id, String email, String password, String salt) {
        this(id, email, password, salt, new ArrayList<>());
    }

    /**
     * Constructor for user with locale
     */
    public ChiclePadUser(int id,
                         String email,
                         String password,
                         String salt,
                         List<Entry> entries,
                         @NonNull Locale locale) {
        this(id, email, password, salt, entries);

        if (locale == null) {
            throw new RuntimeException("Provided user's (" + id + " " + email + ") locale can't be null.");
        }

        this.locale = Optional.of(locale);
    }

    /**
     * Constructor for user with locale
     */
    public ChiclePadUser(int id, String email, String password, String salt, @NonNull Locale locale) {
        this(id, email, password, salt, new ArrayList<>(), locale);
    }

    /**
     * Constructor for user with name
     */
    public ChiclePadUser(int id,
                         String email,
                         String password,
                         String salt,
                         List<Entry> entries,
                         @NonNull String name) {
        this(id, email, password, salt, entries);

        if (name == null) {
            throw new RuntimeException("Provided username of user (" + id + " " + email + ") can't be null.");
        }

        this.name = Optional.of(name);
    }

    /**
     * Constructor for user with name
     */
    public ChiclePadUser(int id, String email, String password, String salt, @NonNull String name) {
        this(id, email, password, salt, new ArrayList<>(), name);
    }

    /**
     * Constructor for user with locale and name
     */
    public ChiclePadUser(int id,
                         String email,
                         String password,
                         String salt,
                         List<Entry> entries,
                         @NonNull Locale locale,
                         @NonNull String name) {
        this(id, email, password, salt, entries);

        if (locale == null) {
            throw new RuntimeException("Provided user's (" + id + " " + email + ") locale can't be null.");
        }

        if (name == null) {
            throw new RuntimeException("Provided username of user (" + id + " " + email + ") can't be null.");
        }

        this.locale = Optional.of(locale);
        this.name = Optional.of(name);
    }

    /**
     * Constructor for user with locale and name
     */
    public ChiclePadUser(int id, String email,
                         String password,
                         String salt,
                         @NonNull Locale locale,
                         @NonNull String name) {
        this(id, email, password, salt, new ArrayList<>(), locale, name);
    }

    /**
     * @return User id
     */
    public int getId() {
        return id;
    }

    /**
     * @return User login mail
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email New user mail
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return Hashed user password with salt
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password Hashed user password with salt
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return Salt to add to password for hashing
     */
    public String getSalt() {
        return salt;
    }

    /**
     * @param salt Salt used for hashing password
     */
    public void setSalt(String salt) {
        this.salt = salt;
    }

    /**
     * @return User entries
     */
    public List<Entry> getEntries() {
        return entries;
    }

    /**
     * @return User name
     */
    public Optional<String> getName() {
        return name;
    }

    /**
     * @param name Non null name of the user
     */
    public void setName(@NonNull String name) {
        if (name == null) {
            throw new RuntimeException("Provided username of user (" + id + " " + email + ") can't be null.");
        }

        this.name = Optional.of(name);
    }

    /**
     * @return User locale
     */
    public Optional<Locale> getLocale() {
        return locale;
    }

    /**
     * @param locale Non null user locale
     */
    public void setLocale(@NonNull Locale locale) {
        if (locale == null) {
            throw new RuntimeException("Provided user's (" + id + " " + email + ") locale can't be null.");
        }

        this.locale = Optional.of(locale);
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o != null &&
                getClass() == o.getClass() &&
                id == ((ChiclePadUser) o).id &&
                email.equals(((ChiclePadUser) o).email);
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
                ", name=" + name.orElse("None") +
                '}';
    }
}
