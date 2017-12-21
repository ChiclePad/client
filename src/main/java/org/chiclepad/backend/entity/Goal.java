package org.chiclepad.backend.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * A goal the user set to himself for completing every day
 */
public class Goal extends Entry {

    /**
     * Unique id of the goal
     */
    private final int id;

    /**
     * Description of the goal to accomplish
     */
    private String description;

    /**
     * Goals that the user completed so far
     */
    private final List<CompletedGoal> completedGoals;

    /**
     * Basic constructor
     */
    public Goal(int entryId, List<Category> categories, int id, String description, List<CompletedGoal> completedGoals) {
        super(entryId, categories);
        this.id = id;
        this.description = description;
        this.completedGoals = completedGoals;
    }

    /**
     * Basic constructor
     */
    public Goal(int entryId, List<Category> categories, int id, String description) {
        this(entryId, categories, id, description, new ArrayList<>());
    }

    /**
     * Basic constructor
     */
    public Goal(int entryId, int id, String description, List<CompletedGoal> completedGoals) {
        super(entryId);
        this.id = id;
        this.description = description;
        this.completedGoals = completedGoals;
    }

    /**
     * Basic constructor
     */
    public Goal(int entryId, int id, String description) {
        this(entryId, id, description, new ArrayList<>());
    }

    /**
     * @return Unique id of the goal
     */
    public int getId() {
        return id;
    }

    /**
     * @return Description of the goal
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description New description of the goal to be accomplished every day
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * List of days when the user has successfully completed the goal
     */
    public List<CompletedGoal> getCompletedGoals() {
        return completedGoals;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o != null &&
                getClass() == o.getClass() &&
                id == ((Goal) o).id &&
                entryId == ((Goal) o).entryId;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "Goal{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", entryId=" + entryId +
                '}';
    }

}
