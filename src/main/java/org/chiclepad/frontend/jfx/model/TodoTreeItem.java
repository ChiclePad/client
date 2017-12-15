package org.chiclepad.frontend.jfx.model;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.chiclepad.backend.entity.Todo;

import java.time.LocalDate;

public class TodoTreeItem extends RecursiveTreeObject<TodoTreeItem> {

    public Todo todo;

    public StringProperty description;

    public JFXDatePicker deadline;

    public JFXDatePicker softDeadline;

    public IntegerProperty priority;


    public TodoTreeItem(Todo todo) {
        this.todo = todo;

        this.description = new SimpleStringProperty(todo.getDescription());
        this.deadline = new JFXDatePicker(todo.getDeadline().toLocalDate());
        if (todo.getSoftDeadline().isPresent()) {
            this.softDeadline = new JFXDatePicker(todo.getDeadline().toLocalDate());
        } else {
            this.softDeadline = new JFXDatePicker(LocalDate.now().plusDays(10));
        }

        this.priority = new SimpleIntegerProperty(todo.getPriority());
    }

}
