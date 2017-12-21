package org.chiclepad.backend.entity;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Recorded day when a user completed his goal
 */
public class CompletedGoal {

    /**
     * Unique id of a recorded goal competition
     */
    private final int id;

    /**
     * Day a user completed his goal
     */
    private final LocalDate date;

    /**
     * Time at which the goal was completed
     */
    private final LocalTime time;

    /**
     * Basic constructor
     */
    public CompletedGoal(int id, LocalDate date, LocalTime time) {
        this.id = id;
        this.date = date;
        this.time = time;
    }

    /**
     * @return Unique record id
     */
    public int getId() {
        return id;
    }

    /**
     * @return Date the goal was successfully completed
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * @return Time the user completed his goal this day
     */
    public LocalTime getTime() {
        return time;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o != null &&
                getClass() == o.getClass() &&
                id == ((CompletedGoal) o).id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "CompletedGoal{" +
                "id=" + id +
                ", date=" + date +
                ", time=" + time +
                '}';
    }

}
