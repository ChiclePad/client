package org.chiclepad.backend.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Entry on the users To-do list
 */
public class Todo extends Entry {

    /**
     * Id of the To-do entry
     */
    private final int id;

    /**
     * Description of what needs to be done
     */
    private String description;

    /**
     * Deadline when the task needs to be done
     */
    private LocalDateTime deadline;

    /**
     * Personal deadline that the user set himself
     */
    private Optional<LocalDateTime> softDeadline = Optional.empty();

    /**
     * Priority of the task
     */
    private int priority;

    /**
     * Basic constructor
     */
    public Todo(int entryId, List<Category> categories, int id, String description, LocalDateTime deadline, int priority) {
        super(entryId, categories);
        this.id = id;
        this.description = description;
        this.deadline = deadline;
        this.priority = priority;
    }

    /**
     * Constructor with soft deadline user set himself for task completion
     */
    public Todo(int entryId,
                List<Category> categories,
                int id,
                String description,
                LocalDateTime deadline,
                LocalDateTime softDeadline,
                int priority) {
        this(entryId, categories, id, description, deadline, priority);
        this.softDeadline = Optional.ofNullable(softDeadline);
    }

    /**
     * Basic constructor
     */
    public Todo(int entryId, int id, String description, LocalDateTime deadline, int priority) {
        super(entryId);
        this.id = id;
        this.description = description;
        this.deadline = deadline;
        this.priority = priority;
    }

    /**
     * Constructor with soft deadline user set himself for task completion
     */
    public Todo(int entryId,
                int id,
                String description,
                LocalDateTime deadline,
                LocalDateTime softDeadline,
                int priority) {
        this(entryId, id, description, deadline, priority);
        this.softDeadline = Optional.ofNullable(softDeadline);
    }

    /**
     * @return Unique To-do entry id
     */
    public int getId() {
        return id;
    }

    /**
     * @return Description of the task
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description New description of the task
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return The time the task needs to be done
     */
    public LocalDateTime getDeadline() {
        return deadline;
    }

    /**
     * @param deadline New time the task needs to be done (This is gonna be hot function)
     */
    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    /**
     * @return Deadline user set himself for task completion
     */
    public Optional<LocalDateTime> getSoftDeadline() {
        return softDeadline;
    }

    /**
     * @param softDeadline New deadline user set for himself
     */
    public void setSoftDeadline(LocalDateTime softDeadline) {
        this.softDeadline = Optional.ofNullable(softDeadline);
    }

    /**
     * @return Priority of the task
     */
    public int getPriority() {
        return priority;
    }

    /**
     * @param priority New priority of the task
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o != null &&
                getClass() == o.getClass() &&
                id == ((Todo) o).id &&
                entryId == ((Todo) o).entryId;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "Todo{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", deadline=" + deadline +
                ", softDeadline=" + softDeadline +
                ", priority=" + priority +
                ", entryId=" + entryId +
                '}';
    }

}
