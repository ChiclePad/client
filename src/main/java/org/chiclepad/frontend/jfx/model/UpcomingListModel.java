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

    private String filter = "";

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
        long remainingMinutes = ChronoUnit.MINUTES.between(LocalDateTime.now(), todo.getDeadline());
        long remainingDays = ChronoUnit.DAYS.between(LocalDateTime.now(), todo.getDeadline());

        String shownResult = LocalDateTime.now().isAfter(todo.getDeadline()) ?
                "Overdue: " + todo.getDescription() :
                "In " + remainingHours + ":" + remainingMinutes + " hours and " + remainingDays + " days: " + todo.getDescription();

        layout.getItems().add(shownResult);
    }

    public void clearUpcoming() {
        layout.getItems().clear();
    }

    public void setNewFilter(String filter) {
        this.filter = filter;

        todos.stream()
                .filter(todo -> fitsFilter(todo, filter))
                .forEach(this::addTodoToLayout);
    }

    private boolean fitsFilter(Todo todo, String filter) {
        return todo.getDescription().contains(filter) ||
                todo.getDeadline().toString().contains(filter) ||
                todo.getSoftDeadline().map(softDeadline -> softDeadline.toString().contains(filter)).orElse(false) ||
                Integer.toString(todo.getPriority()).contains(filter);
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
}
