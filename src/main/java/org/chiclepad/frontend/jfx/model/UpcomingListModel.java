package org.chiclepad.frontend.jfx.model;

import com.jfoenix.controls.JFXListView;
import org.chiclepad.backend.entity.Category;
import org.chiclepad.backend.entity.Todo;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class UpcomingListModel implements ListModel {

    private List<Todo> todos;

    private JFXListView<String> layout;

    private String textFilter = "";

    private List<Category> categoriesFilter = new ArrayList<>();

    private boolean clearedScene;

    public UpcomingListModel(JFXListView<String> layout) {
        this.layout = layout;
        this.todos = new ArrayList<>();
    }

    public void add(Todo todo) {
        todos.add(todo);
        addTodoToLayout(todo);
    }

    private void addTodoToLayout(Todo todo) {
        long remainingHours = ChronoUnit.HOURS.between(LocalDateTime.now(), todo.getDeadline());
        long remainingMinutes = ChronoUnit.MINUTES.between(LocalDateTime.now(), todo.getDeadline()) - 60 * remainingHours;
        long remainingDays = ChronoUnit.DAYS.between(LocalDateTime.now(), todo.getDeadline());

        String shownResult = LocalDateTime.now().isAfter(todo.getDeadline()) ?
                "Overdue: " + todo.getDescription() :
                "In " + remainingHours + ":" + remainingMinutes + " hours and " + remainingDays + " days: " + todo.getDescription();

        layout.getItems().add(shownResult);
    }

    public void clearUpcoming() {
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

    private boolean fitsTextFilter(Todo todo) {
        return todo.getDescription().contains(textFilter) ||
                todo.getDeadline().toString().contains(textFilter) ||
                todo.getSoftDeadline().map(softDeadline -> softDeadline.toString().contains(textFilter))
                        .orElse(false) ||
                Integer.toString(todo.getPriority()).contains(textFilter);
    }

    private void filter() {
        if (clearedScene) {
            todos.stream()
                    .filter(this::fitsTextFilter)
                    .filter(diaryPage -> fitsCategoryFilter(diaryPage, this.categoriesFilter))
                    .forEach(this::addTodoToLayout);

            this.clearedScene = false;
        }
    }

    @Override
    public void setCategoryToSelectedEntry(Category category) {
        /* Can't select here */
    }

    @Override
    public void clearEntries() {
        clearUpcoming();
    }

    @Override
    public void deleteCategoriesForEntry() {

    }

}
