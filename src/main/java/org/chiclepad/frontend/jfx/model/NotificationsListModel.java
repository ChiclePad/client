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

    private String textFilter = "";

    private List<Category> categoriesFilter = new ArrayList<>();

    private boolean clearedScene;

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
        this.clearedScene = true;
    }

    @Override
    public void filterByCategory(List<Category> categories) {
        this.categoriesFilter = categories;
        this.filter();
    }

    public void filterByText(String textFilter) {
        this.textFilter = textFilter;
        this.filter();
    }

    private boolean fitsTextFilter(Goal goal) {
        return goal.getDescription().contains(textFilter);
    }

    private boolean fitsTextFilter(Note note) {
        return note.getContent().contains(textFilter) ||
                note.getReminderTime().map(time -> time.toString().contains(textFilter)).orElse(false);
    }

    private void filter() {
        if (clearedScene) {
            notes.stream()
                    .filter(this::fitsTextFilter)
                    .filter(diaryPage -> fitsCategoryFilter(diaryPage, this.categoriesFilter))
                    .forEach(this::addNoteToLayout);

            goals.stream()
                    .filter(this::fitsTextFilter)
                    .filter(diaryPage -> fitsCategoryFilter(diaryPage, this.categoriesFilter))
                    .forEach(this::addGoalToLayout);

            this.clearedScene = false;
        }
    }

    @Override
    public void setCategoryToSelectedEntry(Category category) {
        /* Can't select here */
    }

    @Override
    public void clearEntries() {
        clearNotifications();
    }

    @Override
    public void deleteCategoriesForEntry() {
        /* Can't select here */
    }

}
