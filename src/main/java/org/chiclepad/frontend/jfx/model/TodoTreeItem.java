package org.chiclepad.frontend.jfx.model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.chiclepad.backend.entity.Todo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TodoTreeItem extends RecursiveTreeObject<TodoTreeItem> {

    public Todo todo;

    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private StringProperty descriptionProperty;
    private StringProperty deadlineProperty;
    private StringProperty softDeadlineProperty;
    private StringProperty priorityProperty;

    TodoTreeItem(Todo todo) {
        this.todo = todo;

        this.descriptionProperty = new SimpleStringProperty();
        this.deadlineProperty = new SimpleStringProperty();
        this.softDeadlineProperty = new SimpleStringProperty();
        this.priorityProperty = new SimpleStringProperty(Integer.toString(todo.getPriority()));

        setDescription(todo.getDescription());
        setDeadline(todo.getDeadline().toLocalDate());
        todo.getSoftDeadline().ifPresent(time -> setSoftDeadline(time.toLocalDate()));
        setPriority(todo.getPriority());
    }

    public static DateTimeFormatter getDateFormatter() {
        return dateFormatter;
    }

    public StringProperty priorityPropertyProperty() {
        return priorityProperty;
    }

    public void setDescription(String newDescription) {
        this.descriptionProperty.setValue(newDescription);
        this.todo.setDescription(newDescription);
    }

    public void setDeadline(LocalDate newDeadline) {
        this.deadlineProperty.setValue(dateFormatter.format(newDeadline));
        this.todo.setDeadline(LocalDateTime.of(newDeadline, LocalTime.now()));
    }

    public void setSoftDeadline(LocalDate newSoftDeadline) {
        this.softDeadlineProperty.setValue(dateFormatter.format(newSoftDeadline));
        this.todo.setSoftDeadline(LocalDateTime.of(newSoftDeadline, LocalTime.now()));
    }

    public void setPriority(int newPriority) {
        this.priorityProperty.setValue(Integer.toString(newPriority));
        this.todo.setPriority(newPriority);
    }

    public StringProperty getDescriptionProperty() {
        return descriptionProperty;
    }

    public StringProperty getDeadlineProperty() {
        return deadlineProperty;
    }

    public StringProperty getSoftDeadlineProperty() {
        return softDeadlineProperty;
    }

    public StringProperty getPriorityProperty() {
        return priorityProperty;
    }

}
