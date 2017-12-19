package org.chiclepad.frontend.jfx.model;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import org.chiclepad.backend.Dao.DaoFactory;
import org.chiclepad.backend.Dao.NoteDao;
import org.chiclepad.backend.Dao.TodoDao;
import org.chiclepad.backend.entity.Category;
import org.chiclepad.backend.entity.Todo;
import org.chiclepad.constants.ChiclePadColor;

import java.time.LocalDateTime;
import java.util.List;

public class TodoListModel implements ListModel {

    private TodoDao todoDao = DaoFactory.INSTANCE.getTodoDao();

    private ObservableList<TodoTreeItem> todoItems;

    private JFXTreeTableView<TodoTreeItem> layout;

    private TodoTreeItem selectedTodoItem;

    private Todo selectedTodo;

    private JFXTextField descriptionField;

    private JFXDatePicker deadlinePicker;

    private JFXDatePicker softDeadlinePicker;

    private JFXSlider prioritySlider;

    private NoteDao noteDao;

    private String filter = "";

    public TodoListModel(
            JFXTreeTableView<TodoTreeItem> todoList,
            JFXTextField descriptionField,
            JFXDatePicker deadlinePicker,
            JFXDatePicker softDeadlinePicker,
            JFXSlider prioritySlider
    ) {
        todoItems = FXCollections.observableArrayList();
        this.layout = todoList;
        this.descriptionField = descriptionField;
        this.deadlinePicker = deadlinePicker;
        this.softDeadlinePicker = softDeadlinePicker;
        this.prioritySlider = prioritySlider;

        JFXTreeTableColumn<TodoTreeItem, String> descriptionColumn = createDescriptionColumn();
        JFXTreeTableColumn<TodoTreeItem, String> deadlineColumn = createDeadlineColumn();
        JFXTreeTableColumn<TodoTreeItem, String> softDeadlineColumn = createSoftDeadlineColumn();
        JFXTreeTableColumn<TodoTreeItem, String> priorityColumn = createPriorityColumn();

        todoList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (selectedTodo != null) {
                todoDao.update(selectedTodo);
            }

            if (newValue == null) {
                selectedTodoItem = null;
                selectedTodo = null;

                descriptionField.setText("");
                deadlinePicker.setValue(null);
                softDeadlinePicker.setValue(null);
                prioritySlider.setValue(0);

                return;
            }

            selectedTodoItem = newValue.getValue();
            selectedTodo = newValue.getValue().todo;

            descriptionField.setText(selectedTodo.getDescription());
            deadlinePicker.setValue(selectedTodo.getDeadline().toLocalDate());
            softDeadlinePicker.setValue(selectedTodo.getSoftDeadline().map(LocalDateTime::toLocalDate).orElse(null));
            prioritySlider.setValue(selectedTodo.getPriority());
        });

        final TreeItem<TodoTreeItem> root = new RecursiveTreeItem<>(todoItems, RecursiveTreeObject::getChildren);
        todoList.setRoot(root);
        todoList.getColumns().setAll(descriptionColumn, deadlineColumn, softDeadlineColumn, priorityColumn);
    }

    private JFXTreeTableColumn<TodoTreeItem, String> createDescriptionColumn() {
        JFXTreeTableColumn<TodoTreeItem, String> descriptionColumn = new JFXTreeTableColumn<>("Description");
        descriptionColumn.setPrefWidth(475);
        descriptionColumn.setCellValueFactory(cellDataFeatures -> {
            return cellDataFeatures.getValue().getValue().getDescriptionProperty();
        });
        return descriptionColumn;
    }

    private JFXTreeTableColumn<TodoTreeItem, String> createDeadlineColumn() {
        JFXTreeTableColumn<TodoTreeItem, String> deadlineColumn = new JFXTreeTableColumn<>("Deadline");
        deadlineColumn.setPrefWidth(150);
        deadlineColumn.setCellValueFactory(cellDataFeatures -> {
            return cellDataFeatures.getValue().getValue().getDeadlineProperty();
        });
        return deadlineColumn;
    }

    private JFXTreeTableColumn<TodoTreeItem, String> createSoftDeadlineColumn() {
        JFXTreeTableColumn<TodoTreeItem, String> softDeadlineColumn = new JFXTreeTableColumn<>("SoftDeadline");
        softDeadlineColumn.setPrefWidth(150);
        softDeadlineColumn.setCellValueFactory(cellDataFeatures -> {
            return cellDataFeatures.getValue().getValue().getSoftDeadlineProperty();
        });
        return softDeadlineColumn;
    }

    private JFXTreeTableColumn<TodoTreeItem, String> createPriorityColumn() {
        JFXTreeTableColumn<TodoTreeItem, String> priorityColumn = new JFXTreeTableColumn<>("Priority");
        priorityColumn.setPrefWidth(100);
        priorityColumn.setCellValueFactory(cellDataFeatures -> {
            return cellDataFeatures.getValue().getValue().getPriorityProperty();
        });
        return priorityColumn;
    }

    public void add(Todo todo) {
        todoItems.add(new TodoTreeItem(todo));
    }

    public Todo deleteSelected() {
        todoItems.remove(selectedTodoItem);
        return selectedTodo;
    }

    public void setNewFilter(String filter) {
        layout.setPredicate(todo -> fitsFilter(todo.getValue().todo, filter));
    }

    private boolean fitsFilter(Todo todo, String filter) {
        return todo.getDescription().contains(filter) ||
                todo.getDeadline().toString().contains(filter) ||
                todo.getSoftDeadline().map(time -> time.toString().contains(filter)).orElse(false) ||
                Integer.toString(todo.getPriority()).contains(filter);
    }

    private String categoryColorOfTodo(Todo todo) {
        if (!todo.getCategories().isEmpty()) {
            return todo.getCategories().get(0).getColor();
        } else {
            return ChiclePadColor.toHex(ChiclePadColor.WHITE);
        }
    }

    @Override
    public void filterByCategory(List<Category> categories) {

    }

    @Override
    public void setCategoryToSelectedEntry(Category category) {

    }

}
