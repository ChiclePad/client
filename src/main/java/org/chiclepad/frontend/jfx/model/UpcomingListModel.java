package org.chiclepad.frontend.jfx.model;

import com.jfoenix.controls.JFXListView;
import org.chiclepad.backend.entity.Todo;

import java.util.ArrayList;
import java.util.List;

public class UpcomingListModel {

    private List<Todo> todos;

    private JFXListView layout;

    private String filter = "";

    public UpcomingListModel(JFXListView layout) {
        this.layout = layout;
        this.todos = new ArrayList<>();
    }

    public void add(Todo todo) {
        todos.add(todo);
        addTodoToLayout(todo);
    }

    private void addTodoToLayout(Todo todo) {
        layout.getItems().add(todo);
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

}
