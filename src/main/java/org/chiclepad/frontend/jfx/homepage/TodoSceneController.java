package org.chiclepad.frontend.jfx.homepage;

import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.effects.JFXDepthManager;
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
import org.chiclepad.backend.entity.Category;
import org.chiclepad.backend.entity.ChiclePadUser;
import org.chiclepad.backend.entity.Todo;
import org.chiclepad.business.UserSessionManager;
import org.chiclepad.frontend.jfx.ChiclePadApp;
import org.chiclepad.frontend.jfx.MOCKUP;
import org.chiclepad.frontend.jfx.model.CategoryListModel;
import org.chiclepad.frontend.jfx.model.TodoListModel;
import org.chiclepad.frontend.jfx.model.TodoTreeItem;

import java.time.LocalDateTime;

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

    private TodoListModel todos;

    private CategoryListModel categories;

    private String filter = "";
    private ChiclePadUserDao userDao = DaoFactory.INSTANCE.getChiclePadUserDao();
    private ChiclePadUser loggedInUser;
    private CategoryDao categoryDao = DaoFactory.INSTANCE.getCategoryDao();
    private TodoDao todoDao = DaoFactory.INSTANCE.getTodoDao();

    @FXML
    public void initialize() {
        initializeAdditionalStyles();
        initializeUser();
        initializeCategories();
        initializeTodos();
    }

    private void initializeAdditionalStyles() {
        JFXDepthManager.setDepth(header, 1);
    }

    private void initializeUser() {
        int userId = UserSessionManager.INSTANCE.getCurrentUserSession().getUserId();
        loggedInUser = userDao.get(userId);
        loggedInUser.getName().ifPresent(name -> usernameLabel.setText(name));
    }

    private void initializeCategories() {
        this.categories = new CategoryListModel(categoryList, categoriesRippler);
        List<Category> categories = this.categoryDao.getAll(this.loggedInUser.getId());
        categories.forEach(category -> this.categories.add(category));
    }

    private void initializeTodos() {
        todos = new TodoListModel(todoList);
        this.todoDao.getAll(this.loggedInUser.getId()).forEach(todo -> this.todos.add(todo));
    }

    @FXML
    public void refreshFilter() {
        filter = searchTextField.getText();
        // TODO reload for Simon
    }

    @FXML
    public void addTodo() {
        todos.add(new Todo(1, 1, "", LocalDateTime.now().plusDays(20), 0));
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
