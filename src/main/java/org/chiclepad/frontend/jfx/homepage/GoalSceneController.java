package org.chiclepad.frontend.jfx.homepage;

import com.jfoenix.effects.JFXDepthManager;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.chiclepad.backend.Dao.CategoryDao;
import org.chiclepad.backend.Dao.ChiclePadUserDao;
import org.chiclepad.backend.Dao.DaoFactory;
import org.chiclepad.backend.Dao.GoalDao;
import org.chiclepad.backend.entity.Category;
import org.chiclepad.backend.entity.ChiclePadUser;
import org.chiclepad.backend.entity.Goal;
import org.chiclepad.business.session.UserSessionManager;
import org.chiclepad.frontend.jfx.ChiclePadApp;
import org.chiclepad.frontend.jfx.ChiclePadColor;
import org.chiclepad.frontend.jfx.model.CategoryListModel;
import org.chiclepad.frontend.jfx.model.GoalListModel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    private VBox goalList;

    @FXML
    private FontAwesomeIcon loadNextButton;

    @FXML
    private FontAwesomeIcon loadPreviousButton;

    @FXML
    private FontAwesomeIcon addCategoryIcon;

    @FXML
    private BarChart successChart;

    @FXML
    private PieChart dayChart;

    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM");

    private GoalListModel goals;

    private CategoryListModel categories;

    private ChiclePadUser loggedInUser;

    private ChiclePadUserDao userDao = DaoFactory.INSTANCE.getChiclePadUserDao();

    private CategoryDao categoryDao = DaoFactory.INSTANCE.getCategoryDao();

    private GoalDao goalDao = DaoFactory.INSTANCE.getGoalDao();

    @FXML
    public void initialize() {
        initializeAdditionalStyles();
        initializeUser();
        initializeCategories();
        initializeGoals();
    }

    private void initializeAdditionalStyles() {
        JFXDepthManager.setDepth(header, 1);

        makeIconGreenOnHover(addCategoryIcon);
        makeIconGreenOnHover(loadNextButton);
        makeIconGreenOnHover(loadPreviousButton);
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
        this.categories = new CategoryListModel(categoryList, categoriesRippler);
        List<Category> categories = this.categoryDao.getAll(this.loggedInUser.getId());
        categories.forEach(category -> this.categories.add(category));
    }

    private void initializeGoals() {
        goals = new GoalListModel(goalList);
        goalDao.getAllGoalsNotCompletedToday(loggedInUser.getId()).forEach(goal -> goals.add(goal));

        XYChart.Series data = new XYChart.Series<>();
        data.getData().addAll(
                new XYChart.Data(dateFormatter.format(LocalDate.now()), 2),
                new XYChart.Data(dateFormatter.format(LocalDate.now().minusDays(1)), 1),
                new XYChart.Data(dateFormatter.format(LocalDate.now().minusDays(2)), 3),
                new XYChart.Data(dateFormatter.format(LocalDate.now().minusDays(3)), 4),
                new XYChart.Data(dateFormatter.format(LocalDate.now().minusDays(4)), 1),
                new XYChart.Data(dateFormatter.format(LocalDate.now().minusDays(5)), 0),
                new XYChart.Data(dateFormatter.format(LocalDate.now().minusDays(6)), 7),
                new XYChart.Data(dateFormatter.format(LocalDate.now().minusDays(7)), 3)
        );
        successChart.setData(FXCollections.observableArrayList(data));

        dayChart.setData(FXCollections.observableArrayList(
                new PieChart.Data("Mon", 8),
                new PieChart.Data("Tue", 2),
                new PieChart.Data("Wed", 4),
                new PieChart.Data("Thr", 3),
                new PieChart.Data("Fri", 2),
                new PieChart.Data("Sat", 7),
                new PieChart.Data("Sun", 2)
        ));
    }

    @FXML
    public void clearScene() {
        goals.clearGoals();
    }

    @FXML
    public void refreshFilter() {
        String filter = searchTextField.getText();
        goals.setNewFilter(filter);
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

        goalDao.delete(deleted);
    }

    @FXML
    public void loadPrevious() {

    }

    @FXML
    public void loadNext() {

    }

    @FXML
    public void addCategory() {
        CategoryPopup.showUnderParent(addCategoryIcon);
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
