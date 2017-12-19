package org.chiclepad.frontend.jfx.model;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import org.chiclepad.backend.Dao.DaoFactory;
import org.chiclepad.backend.Dao.TodoDao;
import org.chiclepad.backend.Dao.DaoFactory;
import org.chiclepad.backend.Dao.NoteDao;
import org.chiclepad.backend.Dao.TodoDao;
import org.chiclepad.backend.entity.Category;
import org.chiclepad.backend.entity.Todo;
import org.chiclepad.constants.ChiclePadColor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.chiclepad.constants.ChiclePadColor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TodoListModel implements ListModel {

    private TodoDao todoDao = DaoFactory.INSTANCE.getTodoDao();

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

    private String textFilter = "";
    private List<Category> categoriesFilter = new ArrayList<>();
    private ObservableList<TodoTreeItem> todoItems;

    private JFXTreeTableView<TodoTreeItem> layout;

    private TodoTreeItem selectedTodoItem;

    private Todo selectedTodo;

    private JFXTextField descriptionField;

    private JFXDatePicker deadlinePicker;

    private JFXDatePicker softDeadlinePicker;

    private JFXSlider prioritySlider;

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

        initializeTable();
    }

    private void initializeTable() {
        setDescriptionCallback();
        setDeadlineCallback();
        setSoftDeadlineCallback();
        setPrioritySliderCallback();

        setSelectionCallback();

        addColumnsAndDisplay();
    }
    private boolean clearedScene;
        JFXTreeTableColumn<TodoTreeItem, String> descriptionColumn = createDescriptionColumn();
        JFXTreeTableColumn<TodoTreeItem, String> deadlineColumn = createDeadlineColumn();
        JFXTreeTableColumn<TodoTreeItem, String> softDeadlineColumn = createSoftDeadlineColumn();
        JFXTreeTableColumn<TodoTreeItem, String> priorityColumn = createPriorityColumn();

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
        JFXTreeTableColumn<TodoTreeItem, String> descriptionColumn = createDescriptionColumn();
        JFXTreeTableColumn<TodoTreeItem, String> deadlineColumn = createDeadlineColumn();
        JFXTreeTableColumn<TodoTreeItem, String> softDeadlineColumn = createSoftDeadlineColumn();
        JFXTreeTableColumn<TodoTreeItem, String> priorityColumn = createPriorityColumn();

    private void setSelectionCallback() {
        layout.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            updatePreviousTodo();
            setNewSelection(newValue);
            setAddSelectedDataToFields();
        });
    }

    private void updatePreviousTodo() {
        if (selectedTodo != null) {
            todoDao.update(selectedTodo);
        }
    }

    private void setNewSelection(TreeItem<TodoTreeItem> newValue) {
        if (newValue == null || isGroupedCell(newValue)) {
            selectedTodoItem = null;
            selectedTodo = null;
        } else {
            selectedTodoItem = newValue.getValue();
            selectedTodo = newValue.getValue().todo;
        }
    }

    private boolean isGroupedCell(TreeItem<TodoTreeItem> newValue) {
        return !(newValue.getValue() instanceof TodoTreeItem);
    }

    private void setAddSelectedDataToFields() {
        String description = selectedTodo != null ? selectedTodo.getDescription() : null;
        LocalDate deadline = selectedTodo != null ? selectedTodo.getDeadline().toLocalDate() : null;
        LocalDate softDeadline = selectedTodo != null ? selectedTodo.getSoftDeadline()
                .map(LocalDateTime::toLocalDate)
                .orElse(null) : null;
        int priority = selectedTodo != null ? selectedTodo.getPriority() : 0;

        descriptionField.setText(description);
        deadlinePicker.setValue(deadline);
        softDeadlinePicker.setValue(softDeadline);
        prioritySlider.setValue(priority);
    }

    private void addColumnsAndDisplay() {
        final TreeItem<TodoTreeItem> root = new RecursiveTreeItem<>(todoItems, RecursiveTreeObject::getChildren);
        this.layout.setRoot(root);
        this.layout.getColumns().setAll(
                createDescriptionColumn(),
                createDeadlineColumn(),
                createSoftDeadlineColumn(),
                createPriorityColumn()
        );
    }

    private JFXTreeTableColumn<TodoTreeItem, String> createDescriptionColumn() {
        JFXTreeTableColumn<TodoTreeItem, String> descriptionColumn = new JFXTreeTableColumn<>("Description");
        descriptionColumn.setPrefWidth(500);
        descriptionColumn.setCellValueFactory(cellDataFeatures -> {
            if (descriptionColumn.validateValue(cellDataFeatures)) {
                return cellDataFeatures.getValue().getValue().getDescriptionProperty();
            } else {
                return descriptionColumn.getComputedValue(cellDataFeatures);
            }
        });
        return descriptionColumn;
    }

    private JFXTreeTableColumn<TodoTreeItem, String> createDeadlineColumn() {
        JFXTreeTableColumn<TodoTreeItem, String> deadlineColumn = new JFXTreeTableColumn<>("Deadline");
        deadlineColumn.setPrefWidth(150);
        deadlineColumn.setCellValueFactory(cellDataFeatures -> {
            if (deadlineColumn.validateValue(cellDataFeatures)) {
                return cellDataFeatures.getValue().getValue().getDeadlineProperty();
            } else {
                return deadlineColumn.getComputedValue(cellDataFeatures);
            }
        });
        return deadlineColumn;
    }

    private JFXTreeTableColumn<TodoTreeItem, String> createSoftDeadlineColumn() {
        JFXTreeTableColumn<TodoTreeItem, String> softDeadlineColumn = new JFXTreeTableColumn<>("SoftDeadline");
        softDeadlineColumn.setPrefWidth(150);
        softDeadlineColumn.setCellValueFactory(cellDataFeatures -> {
            if (softDeadlineColumn.validateValue(cellDataFeatures)) {
                return cellDataFeatures.getValue().getValue().getSoftDeadlineProperty();
            } else {
                return softDeadlineColumn.getComputedValue(cellDataFeatures);
            }
        });
        return softDeadlineColumn;
    }

    private JFXTreeTableColumn<TodoTreeItem, String> createPriorityColumn() {
        JFXTreeTableColumn<TodoTreeItem, String> priorityColumn = new JFXTreeTableColumn<>("Priority");
        priorityColumn.setPrefWidth(100);
        priorityColumn.setCellValueFactory(cellDataFeatures -> {
            if (priorityColumn.validateValue(cellDataFeatures)) {
                return cellDataFeatures.getValue().getValue().getPriorityProperty();
            } else {
                return priorityColumn.getComputedValue(cellDataFeatures);
            }
        });
        return priorityColumn;
    }

    private void setDescriptionCallback() {
        descriptionField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (selectedTodo == null) {
                return;
            }

            selectedTodo.setDescription(newValue);
            selectedTodoItem.setDescription(newValue);
        });

        descriptionField.focusedProperty().addListener((observable, oldValue, newValue) -> updateSelected());
    }

    private void setDeadlineCallback() {
        deadlinePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (selectedTodo == null) {
                return;
            }

            selectedTodo.setDeadline(newValue.atStartOfDay());
            selectedTodoItem.setDeadline(newValue);
        });

        deadlinePicker.focusedProperty().addListener((observable, oldValue, newValue) -> updateSelected());
    }

    private void setSoftDeadlineCallback() {
        softDeadlinePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (selectedTodo == null) {
                return;
            }

            selectedTodo.setSoftDeadline(newValue != null ? newValue.atStartOfDay() : null);
            selectedTodoItem.setSoftDeadline(newValue);
        });

        softDeadlinePicker.focusedProperty().addListener((observable, oldValue, newValue) -> updateSelected());
    }

    private void setPrioritySliderCallback() {
        prioritySlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (selectedTodo == null) {
                return;
            }

            selectedTodo.setPriority(newValue.intValue());
            selectedTodoItem.setPriority(newValue.intValue());
        });

        prioritySlider.focusedProperty().addListener((observable, oldValue, newValue) -> updateSelected());
    }

    public void add(Todo todo) {
        todoItems.add(new TodoTreeItem(todo));
        todoItems.add(new TodoTreeItem(todo));
    }

    private void updateSelected() {
        if (selectedTodo == null) {
            return;
        }

        todoDao.update(selectedTodo);
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

    private String categoryColorOfTodo(Todo todo) {
        if (!todo.getCategories().isEmpty()) {
            return todo.getCategories().get(0).getColor();
        } else {
            return ChiclePadColor.toHex(ChiclePadColor.WHITE);
        }
    }

    @Override
    public void setCategoryToSelectedEntry(Category category) {
        this.selectedTodo.getCategories().forEach(unboundCategory -> {
            this.todoDao.unbind(unboundCategory, this.selectedTodo);
        });
        this.selectedTodo.getCategories().clear();
        this.selectedTodo.getCategories().add(category);
        this.todoDao.bind(category, this.selectedTodo);
    }

    @Override
    public void clearEntries() {
    }

}
