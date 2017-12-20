package org.chiclepad.frontend.jfx.homepage;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.effects.JFXDepthManager;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.chiclepad.backend.Dao.CategoryDao;
import org.chiclepad.backend.Dao.ChiclePadUserDao;
import org.chiclepad.backend.Dao.DaoFactory;
import org.chiclepad.backend.Dao.GoalDao;
import org.chiclepad.backend.business.session.UserSessionManager;
import org.chiclepad.backend.entity.Category;
import org.chiclepad.backend.entity.ChiclePadUser;
import org.chiclepad.backend.entity.Goal;
import org.chiclepad.constants.ChiclePadColor;
import org.chiclepad.frontend.jfx.ChiclePadApp;
import org.chiclepad.frontend.jfx.Popup.CategoryPopup;
import org.chiclepad.frontend.jfx.Popup.UserPopup;
import org.chiclepad.frontend.jfx.model.CategoryListModel;
import org.chiclepad.frontend.jfx.model.GoalChartModel;
import org.chiclepad.frontend.jfx.model.GoalListModel;

import java.util.List;

public class GoalSceneController {

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
    private VBox categoriesRippler;

    @FXML
    private JFXComboBox categoryPicker;

    @FXML
    private VBox goalList;

    @FXML
    private FontAwesomeIcon addCategoryIcon;

    @FXML
    private BarChart successChart;

    @FXML
    private PieChart dayChart;

    private GoalListModel goals;

    private GoalChartModel goalCharts;

    private CategoryListModel categories;

    private ChiclePadUser loggedInUser;

    private ChiclePadUserDao userDao = DaoFactory.INSTANCE.getChiclePadUserDao();

    private CategoryDao categoryDao = DaoFactory.INSTANCE.getCategoryDao();

    private GoalDao goalDao = DaoFactory.INSTANCE.getGoalDao();

    @FXML
    public void initialize() {
        initializeAdditionalStyles();
        initializeUser();
        initializeGoals();
        initializeGoalCharts();
        initializeCategories();

        categories.subscribeListModel(goals);
        categories.subscribeListModel(goalCharts);
    }

    private void initializeAdditionalStyles() {
        JFXDepthManager.setDepth(header, 1);

        makeIconGreenOnHover(addCategoryIcon);
    }

    private void makeIconGreenOnHover(FontAwesomeIcon icon) {
        icon.setOnMouseEntered(event -> icon.setFill(ChiclePadColor.PRIMARY));
        icon.setOnMouseExited(event -> icon.setFill(ChiclePadColor.BLACK));
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

    private void initializeGoals() {
        goals = new GoalListModel(goalList);
        goalDao.getAllGoalsNotCompletedToday(loggedInUser.getId()).forEach(goal -> goals.add(goal));
    }

    private void initializeGoalCharts() {
        this.goalCharts = new GoalChartModel(successChart, dayChart);
    }

    @FXML
    public void clearScene() {
        goals.clearGoals();
    }

    @FXML
    public void refreshFilter() {
        String filter = searchTextField.getText();
        goals.setNewTextFilter(filter);
        goalCharts.refreshWithFilter(filter);
    }

    @FXML
    public void addGoal() {
        Goal created = goalDao.create(loggedInUser.getId(), "");
        goals.add(created);
    }

    @FXML
    public void deleteSelected() {
        Goal deleted = goals.deleteSelected();

        if (deleted == null) {
            return;
        }

        goalDao.markDeleted(deleted);
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
    public void switchToTodoScene() {
        ChiclePadApp.switchScene(new TodoSceneController(), "homepage/todoScene.fxml");
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
