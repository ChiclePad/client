package org.chiclepad.frontend.jfx.model;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.cells.editors.TextFieldEditorBuilder;
import com.jfoenix.controls.cells.editors.base.GenericEditableTreeTableCell;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import org.chiclepad.backend.entity.Category;
import org.chiclepad.backend.entity.Todo;

import java.util.List;

public class TodoListModel implements ListModel {

    ObservableList<TodoTreeItem> items;

    private JFXTreeTableView<TodoTreeItem> todoList;

    public TodoListModel(JFXTreeTableView<TodoTreeItem> todoList) {
        items = FXCollections.observableArrayList();
        this.todoList = todoList;

        JFXTreeTableColumn<TodoTreeItem, String> descriptionColumn = new JFXTreeTableColumn<>("Description");
        descriptionColumn.setPrefWidth(400);
        descriptionColumn.setCellValueFactory(cellDataFeatures -> {
            return cellDataFeatures.getValue().getValue().description;
        });

        JFXTreeTableColumn<TodoTreeItem, String> deadlineColumn = new JFXTreeTableColumn<>("Deadline");
        deadlineColumn.setCellValueFactory(cellDataFeatures -> {
            return new SimpleStringProperty(cellDataFeatures.getValue().getValue().deadline.getValue().toString());
        });

        JFXTreeTableColumn<TodoTreeItem, String> softDeadlineColumn = new JFXTreeTableColumn<>("SoftDeadline");
        softDeadlineColumn.setCellValueFactory(cellDataFeatures -> {
            return new SimpleStringProperty(cellDataFeatures.getValue().getValue().softDeadline.getValue().toString());
        });

        JFXTreeTableColumn<TodoTreeItem, String> priorityColumn = new JFXTreeTableColumn<>("Priority");
        priorityColumn.setCellValueFactory(cellDataFeatures -> {
            return new SimpleStringProperty(Integer.toString(cellDataFeatures.getValue().getValue().todo.getPriority()));
        });

        deadlineColumn.setCellFactory(param -> new GenericEditableTreeTableCell<>(new TextFieldEditorBuilder()));
        deadlineColumn.setOnEditCommit(t -> {
            t.getTreeTableView().getTreeItem(t.getTreeTablePosition().getRow()).getValue().todo
                    .setDescription(t.getNewValue());
        });

        descriptionColumn.setCellFactory(param -> new GenericEditableTreeTableCell<>(new TextFieldEditorBuilder()));
        descriptionColumn.setOnEditCommit(t -> {
            t.getTreeTableView().getTreeItem(t.getTreeTablePosition().getRow()).getValue().todo
                    .setPriority(Integer.parseInt(t.getNewValue()));
        });

        final TreeItem<TodoTreeItem> root = new RecursiveTreeItem<TodoTreeItem>(items, RecursiveTreeObject::getChildren);
        todoList.setRoot(root);
        todoList.getColumns().setAll(descriptionColumn, deadlineColumn, softDeadlineColumn, priorityColumn);
    }

    public void add(Todo todo) {
        items.add(new TodoTreeItem(todo));
    }

    public void setNewFilter(String filter) {
//        todoList.setPredicate(todo -> todo.getValue().);
    }

    public Todo deleteSelected() {
        return null;
    }

    @Override
    public void filterByCategory(List<Category> categories) {

    }

    @Override
    public void setCategoryToSelectedEntry(Category category) {

    }
}
