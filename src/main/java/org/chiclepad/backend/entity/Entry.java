package org.chiclepad.backend.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    protected final LocalDateTime created;

    /**
     * Categories the entry belongs to
     */
    protected final List<Category> categories;

    /**
     * Basic constructor
     */
    public Entry(int entryId, LocalDateTime created, List<Category> categories) {
        this.entryId = entryId;
        this.created = created;
        this.categories = categories;
    }

    /**
     * Basic constructor
     */
    public Entry(int entryId, LocalDateTime created) {
        this(entryId, created, new ArrayList<>());
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
