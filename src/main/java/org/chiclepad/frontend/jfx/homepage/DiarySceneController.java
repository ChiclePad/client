package org.chiclepad.frontend.jfx.homepage;

import com.jfoenix.controls.JFXListView;
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
import org.chiclepad.backend.Dao.DiaryPageDao;
import org.chiclepad.backend.entity.Category;
import org.chiclepad.backend.entity.ChiclePadUser;
import org.chiclepad.business.UserSessionManager;
import org.chiclepad.frontend.jfx.ChiclePadApp;
import org.chiclepad.frontend.jfx.model.CategoryListModel;
import org.chiclepad.frontend.jfx.model.DiaryListModel;

import java.util.List;

public class DiarySceneController {

    @FXML
    private BorderPane header;

    @FXML
    private HBox userArea;

    @FXML
    private Label usernameLabel;

    @FXML
    private TextField searchTextField;

    @FXML
    private VBox categoryList;

    @FXML
    private JFXListView diaryList;

    @FXML
    private VBox categoriesRippler;

    private DiaryListModel diaryPages;

    private CategoryListModel categories;

    private String filter = "";
    private ChiclePadUserDao userDao = DaoFactory.INSTANCE.getChiclePadUserDao();
    private ChiclePadUser loggedInUser;
    private CategoryDao categoryDao = DaoFactory.INSTANCE.getCategoryDao();
    private DiaryPageDao diaryPageDao = DaoFactory.INSTANCE.getDiaryPageDao();

    @FXML
    public void initialize() {
        initializeAdditionalStyles();
        initializeUser();
        initializeCategories();
        initializeDiaryPages();
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

    private void initializeDiaryPages() {
        diaryPages = new DiaryListModel(diaryList);
        this.diaryPageDao.getAll(this.loggedInUser.getId()).forEach(diaryPage -> this.diaryPages.add(diaryPage));
    }

    @FXML
    public void refreshFilter() {
        filter = searchTextField.getText();
        // TODO reload for Simon
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
    public void switchToTodoScene() {
        ChiclePadApp.switchScene(new TodoSceneController(), "homepage/todoScene.fxml");
    }

    @FXML
    public void switchToGoalScene() {
        ChiclePadApp.switchScene(new GoalSceneController(), "homepage/goalScene.fxml");
    }

    @FXML
    public void switchToNoteScene() {
        ChiclePadApp.switchScene(new NoteSceneController(), "homepage/noteScene.fxml");
    }

}
