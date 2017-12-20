package org.chiclepad.frontend.jfx.model;

import com.jfoenix.controls.JFXListView;
import org.chiclepad.backend.entity.Category;
import org.chiclepad.backend.entity.Goal;
import org.chiclepad.backend.entity.Note;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class NotificationsListModel implements ListModel {

    private List<Goal> goals;

    private List<Note> notes;

    private JFXListView layout;

    private DateTimeFormatter dateTimeFormatter;

    private String filter = "";

    public NotificationsListModel(JFXListView layout) {
        this.layout = layout;
        this.goals = new ArrayList<>();
        this.notes = new ArrayList<>();
        this.dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm dd-MM");
    }

    public void addNote(Note note) {
        notes.add(note);
        addNoteToLayout(note);
    }

    public void addGoal(Goal goal) {
        goals.add(goal);
        addGoalToLayout(goal);
    }

    private void addNoteToLayout(Note note) {
        String shownResult = "Note: " + note.getContent() + "  " + dateTimeFormatter.format(note.getReminderTime().get());

        layout.getItems().add(shownResult);
    }

    private void addGoalToLayout(Goal goal) {
        String shownResult = "Goal: " + goal.getDescription();

        layout.getItems().add(shownResult);
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
                .forEach(this::addNoteToLayout);
    }

    private boolean fitsFilter(Goal goal, String filter) {
        return goal.getDescription().contains(filter);
    }

    private boolean fitsFilter(Note note, String filter) {
        return note.getContent().contains(filter) ||
                note.getReminderTime().map(time -> time.toString().contains(filter)).orElse(false);
    }

    @Override
    public void filterByCategory(List<Category> categories) {

    }

    @Override
    public void setCategoryToSelectedEntry(Category category) {

    }

    @Override
    public void clearEntries() {

    }

    @Override
    public void deleteCategoriesForEntry() {

    }

}
