package org.chiclepad.backend.entity;

import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * One of the users added organization entries
 */
public abstract class Entry {

    /**
     * Id of the entry
     */
    protected final int entryId;

    /**
     * Date the entry was created
     */
    protected LocalDateTime created;

    /**
     * Categories the entry belongs to
     */
    protected final List<Category> categories;

    /**
     * Time the entry was deleted
     */
    protected Optional<LocalDateTime> deletionTime = Optional.empty();

    /**
     * Basic constructor
     */
    Entry(int entryId, LocalDateTime created, List<Category> categories) {
        this.entryId = entryId;
        this.created = created;
        this.categories = categories;
    }

    /**
     * Basic constructor
     */
    Entry(int entryId, List<Category> categories) {
        this.entryId = entryId;
        this.categories = categories;
    }

    /**
     * Basic constructor
     */
    Entry(int entryId, LocalDateTime created) {
        this(entryId, created, new ArrayList<>());
    }

    /**
     * Basic constructor
     */
    Entry(int entryId) {
        this(entryId, new ArrayList<>());
    }

    /**
     * Constructor for deleted entries
     */
    Entry(int entryId, LocalDateTime created, List<Category> categories, @NonNull LocalDateTime deletionTime) {
        this(entryId, created, categories);

        if (deletionTime == null) {
            throw new RuntimeException("Provided deletion time to a entry " + entryId + " can't be null");
        }

        this.deletionTime = Optional.of(deletionTime);
    }

    /**
     * Constructor for deleted entries
     */
    public Entry(int entryId, LocalDateTime created, @NonNull LocalDateTime deletionTime) {
        this(entryId, created, new ArrayList<>());

        if (deletionTime == null) {
            throw new RuntimeException("Provided deletion time to a entry " + entryId + " can't be null");
        }

        this.deletionTime = Optional.of(deletionTime);
    }

    /**
     * @return The date the entry was created
     */
    public LocalDateTime getCreated() {
        return created;
    }

    /**
     * @return Categories the entry belongs to
     */
    public List<Category> getCategories() {
        return categories;
    }

    /**
     * @return Get id of the entry
     */
    public int getEntryId() {
        return entryId;
    }

    /**
     * @return the time the entry was deleted
     */
    public Optional<LocalDateTime> getDeletionTime() {
        return deletionTime;
    }

    /**
     * @param deletionTime Set the time the entry was deleted
     */
    public void setDeletionTime(@NonNull LocalDateTime deletionTime) {
        if (deletionTime == null) {
            throw new RuntimeException("Provided deletion time to a entry " + entryId + " can't be null");
        }

        this.deletionTime = Optional.of(deletionTime);
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o != null &&
                getClass() == o.getClass() &&
                entryId == ((Entry) o).entryId;
    }

    @Override
    public int hashCode() {
        return entryId;
    }

    @Override
    public String toString() {
        return "Entry{" +
                "entryId=" + entryId +
                ", created=" + created +
                '}';
    }

}
