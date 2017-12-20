package org.chiclepad.frontend.jfx.model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.chiclepad.backend.entity.Category;
import org.chiclepad.backend.entity.Todo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TodoTreeItem extends RecursiveTreeObject<TodoTreeItem> {

    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public Todo todo;

    private StringProperty descriptionProperty;

    private StringProperty deadlineProperty;

    private StringProperty softDeadlineProperty;

    private StringProperty priorityProperty;

    private StringProperty categoryProperty;

    TodoTreeItem(Todo todo) {
        this.todo = todo;

        this.descriptionProperty = new SimpleStringProperty();
        this.deadlineProperty = new SimpleStringProperty();
        this.softDeadlineProperty = new SimpleStringProperty();
        this.priorityProperty = new SimpleStringProperty();
        this.categoryProperty = new SimpleStringProperty();

        setDescription(todo.getDescription());
        setDeadline(todo.getDeadline().toLocalDate());
        todo.getSoftDeadline().ifPresent(time -> setSoftDeadline(time.toLocalDate()));
        setPriority(todo.getPriority());
        setCategory(todo.getCategories().size() == 0 ? null : todo.getCategories().get(0));
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
        this.todo.setDeadline(newDeadline.atStartOfDay());
    }

    public void setSoftDeadline(LocalDate newSoftDeadline) {
        if (newSoftDeadline == null) {
            this.softDeadlineProperty.setValue(null);
            this.todo.setSoftDeadline(null);

            return;
        }

        this.softDeadlineProperty.setValue(dateFormatter.format(newSoftDeadline));
        this.todo.setSoftDeadline(newSoftDeadline.atStartOfDay());
    }

    public void setPriority(int newPriority) {
        this.priorityProperty.setValue(Integer.toString(newPriority));
        this.todo.setPriority(newPriority);
    }

    public void setCategory(Category category) {
        this.categoryProperty.setValue(category == null ? "" : category.getName());
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

    public StringProperty getCategoryProperty() {
        return categoryProperty;
    }

}
