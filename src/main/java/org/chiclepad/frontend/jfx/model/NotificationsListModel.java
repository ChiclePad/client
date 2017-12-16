package org.chiclepad.frontend.jfx.model;

import com.jfoenix.controls.JFXListView;
import org.chiclepad.backend.entity.Goal;
import org.chiclepad.backend.entity.Note;

import java.util.ArrayList;
import java.util.List;

public class NotificationsListModel {

    private List<Goal> goals;

    private List<Note> notes;

    private JFXListView layout;

    private String filter = "";

    public NotificationsListModel(JFXListView layout) {
        this.layout = layout;
        this.goals = new ArrayList<>();
        this.notes = new ArrayList<>();
    }


    public void addNote(Note note) {
        notes.add(note);
        addGoalToLayout(note);
    }

    public void addGoal(Goal goal) {
        goals.add(goal);
        addGoalToLayout(goal);
    }

    private void addGoalToLayout(Object line) {
        layout.getItems().add(line);
    }


    public void clearNotifications() {
        layout.getItems().clear();
    }

    public void setNewFilter(String filter) {
        this.filter = filter;

        goals.stream()
                .filter(goal -> fitsFilter(goal, filter))
                .forEach(this::addGoalToLayout);
        notes.stream()
                .filter(note -> fitsFilter(note, filter))
                .forEach(this::addGoalToLayout);
    }

    private boolean fitsFilter(Goal goal, String filter) {
        return goal.getDescription().contains(filter);
    }

    private boolean fitsFilter(Note note, String filter) {
        return note.getContent().contains(filter) ||
                note.getReminderTime().map(time -> time.toString().contains(filter)).orElse(false);
    }

}
