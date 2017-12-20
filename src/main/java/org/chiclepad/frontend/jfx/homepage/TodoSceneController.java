package org.chiclepad.frontend.jfx.homepage;

import com.jfoenix.controls.*;
import com.jfoenix.effects.JFXDepthManager;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.chiclepad.backend.Dao.CategoryDao;
import org.chiclepad.backend.Dao.ChiclePadUserDao;
import org.chiclepad.backend.Dao.DaoFactory;
import org.chiclepad.backend.Dao.TodoDao;
import org.chiclepad.backend.business.session.UserSessionManager;
import org.chiclepad.backend.entity.Category;
import org.chiclepad.backend.entity.ChiclePadUser;
import org.chiclepad.backend.entity.Todo;
import org.chiclepad.constants.ChiclePadColor;
import org.chiclepad.frontend.jfx.ChiclePadApp;
import org.chiclepad.frontend.jfx.Popup.CategoryPopup;
import org.chiclepad.frontend.jfx.Popup.UserPopup;
import org.chiclepad.frontend.jfx.model.CategoryListModel;
import org.chiclepad.frontend.jfx.model.TodoListModel;
import org.chiclepad.frontend.jfx.model.TodoTreeItem;

import java.time.LocalDate;
import java.util.List;

public class TodoSceneController {

    @FXML
    private BorderPane header;

    @FXML
    private HBox userArea;

    @FXML
    private Label usernameLabel;

    @FXML
    private TextField searchTextField;

    @FXML
    private JFXTreeTableView<TodoTreeItem> todoList;

    @FXML
    private VBox categoryList;

    @FXML
    private VBox categoriesRippler;

    @FXML
    private JFXComboBox categoryPicker;

    @FXML
    private FontAwesomeIcon addCategoryIcon;

    @FXML
    private JFXTextField descriptionField;

    @FXML
    private JFXDatePicker deadlinePicker;

    @FXML
    private JFXDatePicker softDeadlinePicker;

    @FXML
    private JFXSlider prioritySlider;

    private TodoListModel todos;

    private CategoryListModel categories;

    private ChiclePadUser loggedInUser;

    private ChiclePadUserDao userDao = DaoFactory.INSTANCE.getChiclePadUserDao();

    private CategoryDao categoryDao = DaoFactory.INSTANCE.getCategoryDao();

    private TodoDao todoDao = DaoFactory.INSTANCE.getTodoDao();

    @FXML
    public void initialize() {
        initializeAdditionalStyles();
        initializeUser();
        initializeTodos();
        initializeCategories();

        categories.subscribeListModel(todos);
    }

    private void initializeAdditionalStyles() {
        JFXDepthManager.setDepth(header, 1);

        addCategoryIcon.setOnMouseEntered(event -> addCategoryIcon.setFill(ChiclePadColor.PRIMARY));
        addCategoryIcon.setOnMouseExited(event -> addCategoryIcon.setFill(ChiclePadColor.BLACK));
    }

    private void initializeUser() {
        int userId = UserSessionManager.INSTANCE.getCurrentUserSession().getUserId();
        loggedInUser = userDao.get(userId);
        loggedInUser.getName().ifPresent(name -> usernameLabel.setText(name));
    }

    private void initializeCategories() {
        this.categories = new CategoryListModel(categoryList, categoriesRippler, categoryPicker);
        List<Category> categories = this.categoryDao.getAll(this.loggedInUser.getId());
        categories.forEach(category -> this.categories.add(category));
    }

    private void initializeTodos() {
        todos = new TodoListModel(todoList, descriptionField, deadlinePicker, softDeadlinePicker, prioritySlider);
        this.todoDao.getAll(this.loggedInUser.getId()).forEach(todo -> this.todos.add(todo));
    }

    @FXML
    public void refreshFilter() {
        String filter = searchTextField.getText();
        todos.setNewFilter(filter);
    }

    @FXML
    public void addTodo() {
        Todo created = todoDao.create(
                loggedInUser.getId(),
                "Description",
                LocalDate.now().plusDays(1).atStartOfDay(),
                0
        );
        todos.add(created);
    }

    @FXML
    public void deleteSelected() {
        Todo deleted = todos.deleteSelected();

        if (deleted == null) {
            return;
        }

        todoDao.markDeleted(deleted);
    }

    @FXML
    public void addCategory() {
        CategoryPopup.showAddCategoryUnderParent(addCategoryIcon, categories);
    }

    @FXML
    public void userClick() {
        UserPopup.showUnderParent(userArea);
    }

    @FXML
    public void switchToHomeScene() {
        ChiclePadApp.switchScene(new HomeSceneController(), "homepage/homeScene.fxml");
    }

    @FXML
    public void switchToGoalScene() {
        ChiclePadApp.switchScene(new GoalSceneController(), "homepage/goalScene.fxml");
    }

    @FXML
    public void switchToDiaryScene() {
        ChiclePadApp.switchScene(new DiarySceneController(), "homepage/diaryScene.fxml");
    }

    @FXML
    public void switchToNoteScene() {
        ChiclePadApp.switchScene(new NoteSceneController(), "homepage/noteScene.fxml");
    }

}
