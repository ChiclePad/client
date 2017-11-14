package org.chiclepad.backend.entity;

import org.springframework.lang.NonNull;

import java.awt.*;
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
     * Horizontal and vertival position on the office board
     */
    private Point position;

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
    public Note(int entryId,
                LocalDateTime created,
                List<Category> categories,
                int id,
                int positionX,
                int positionY,
                String content) {
        super(entryId, created, categories);
        this.id = id;
        this.position = new Point(positionX, positionY);
        this.content = content;
    }

    /**
     * Constructor for note with reminder
     */
    public Note(int entryId,
                LocalDateTime created,
                List<Category> categories,
                int id,
                int positionX,
                int positionY,
                String content,
                @NonNull LocalDateTime reminderTime) {
        this(entryId, created, categories, id, positionX, positionY, content);

        if (reminderTime == null) {
            throw new RuntimeException("Provided reminder time to a note " + id + " can't be null");
        }

        this.reminderTime = Optional.of(reminderTime);
    }

    /**
     * Basic constructor
     */
    public Note(int entryId,
                LocalDateTime created,
                int id,
                int positionX,
                int positionY,
                String content) {
        super(entryId, created);
        this.id = id;
        this.position = new Point(positionX, positionY);
        this.content = content;
    }

    /**
     * Constructor for note with reminder
     */
    public Note(int entryId,
                LocalDateTime created,
                int id,
                int positionX,
                int positionY,
                String content,
                @NonNull LocalDateTime reminderTime) {
        this(entryId, created, id, positionX, positionY, content);

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
     * @return Position of the note on an office board
     */
    public Point getPosition() {
        return position;
    }

    /**
     * @param position New position of the note on an office board
     */
    public void setPosition(Point position) {
        this.position = position;
    }

    /**
     * @return Get horizontal position of a note on an office board
     */
    public int getPositionX() {
        return position.x;
    }

    /**
     * @param positionX New horizontal position of the note
     */
    public void setPositionX(int positionX) {
        this.position.x = positionX;
    }

    /**
     * @return Get vertival position of the note on an office board
     */
    public int getPositionY() {
        return position.y;
    }

    /**
     * @param positionY New vertical position of the note
     */
    public void setPositionY(int positionY) {
        this.position.y = positionY;
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
                ", position=" + position +
                ", content='" + content + '\'' +
                ", entryId=" + entryId +
                '}';
    }

}
