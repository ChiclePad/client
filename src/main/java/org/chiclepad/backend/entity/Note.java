package org.chiclepad.backend.entity;

import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Note in form of a post-it on a board, used for simple reminders without a deadline
 */
public class Note extends Entry {

    /**
     * Unique id of the note
     */
    private final int id;

    /**
     * Text written on the note
     */
    private String content;

    /**
     * The time the user wants to be reminded of current note
     */
    private Optional<LocalDateTime> reminderTime = Optional.empty();

    /**
     * Basic constructor
     */
    public Note(int entryId, List<Category> categories, int id, String content) {
        super(entryId, categories);
        this.id = id;
        this.content = content;
    }

    /**
     * Constructor for note with reminder
     */
    public Note(int entryId, List<Category> categories, int id, String content, @NonNull LocalDateTime reminderTime) {
        this(entryId, categories, id, content);

        if (reminderTime == null) {
            throw new RuntimeException("Provided reminder time to a note " + id + " can't be null");
        }

        this.reminderTime = Optional.of(reminderTime);
    }

    /**
     * Basic constructor
     */
    public Note(int entryId, int id, String content) {
        super(entryId);
        this.id = id;
        this.content = content;
    }

    /**
     * Constructor for note with reminder
     */
    public Note(int entryId, int id, String content, @NonNull LocalDateTime reminderTime) {
        this(entryId, id, content);

        if (reminderTime == null) {
            throw new RuntimeException("Provided reminder time to a note " + id + " can't be null");
        }

        this.reminderTime = Optional.of(reminderTime);
    }

    /**
     * @return Get unique note id
     */
    public int getId() {
        return id;
    }

    /**
     * @return Get what's written on the note
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content New content of the note
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return Time the user needs to be reminded of the note
     */
    public Optional<LocalDateTime> getReminderTime() {
        return reminderTime;
    }

    /**
     * @param reminderTime Time to remind the user of the note
     */
    public void setReminderTime(@NonNull LocalDateTime reminderTime) {
        if (reminderTime == null) {
            throw new RuntimeException("Provided reminder time to a note " + id + " can't be null");
        }

        this.reminderTime = Optional.of(reminderTime);
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o != null &&
                getClass() == o.getClass() &&
                id == ((Note) o).id &&
                entryId == ((Note) o).entryId;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", entryId=" + entryId +
                '}';
    }

}
