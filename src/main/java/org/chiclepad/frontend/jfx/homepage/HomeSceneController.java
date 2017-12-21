package org.chiclepad.frontend.jfx.homepage;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.effects.JFXDepthManager;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.chiclepad.backend.Dao.*;
import org.chiclepad.backend.business.session.UserSessionManager;
import org.chiclepad.backend.entity.Category;
import org.chiclepad.backend.entity.ChiclePadUser;
import org.chiclepad.backend.entity.Todo;
import org.chiclepad.constants.ChiclePadColor;
import org.chiclepad.frontend.jfx.ChiclePadApp;
import org.chiclepad.frontend.jfx.Popup.CategoryPopup;
import org.chiclepad.frontend.jfx.Popup.UserPopup;
import org.chiclepad.frontend.jfx.model.CategoryListModel;
import org.chiclepad.frontend.jfx.model.NotificationsListModel;
import org.chiclepad.frontend.jfx.model.UpcomingListModel;

import java.util.Comparator;
import java.util.List;

public class HomeSceneController {

    @FXML
    private BorderPane header;

    @FXML
    private HBox userArea;

    @FXML
    private Label usernameLabel;

    @FXML
    private TextField searchTextField;

    @FXML
    private JFXListView<String> upcomingListView;

    @FXML
    private JFXListView<String> notificationsListView;

    @FXML
    private VBox categoryList;

    @FXML
    private VBox categoriesRippler;

    @FXML
    private FontAwesomeIcon addCategoryIcon;

    private UpcomingListModel upcoming;

    private NotificationsListModel notifications;

    private CategoryListModel categories;

    private ChiclePadUser loggedInUser;

    private ChiclePadUserDao userDao = DaoFactory.INSTANCE.getChiclePadUserDao();

    private CategoryDao categoryDao = DaoFactory.INSTANCE.getCategoryDao();

    private TodoDao todoDao = DaoFactory.INSTANCE.getTodoDao();

    private NoteDao noteDao = DaoFactory.INSTANCE.getNoteDao();

    private GoalDao goalDao = DaoFactory.INSTANCE.getGoalDao();

    @FXML
    public void initialize() {
        initializeAdditionalStyles();
        initializeUser();
        initializeUpcoming();
        initializeNotifications();
        initializeCategories();

        categories.subscribeListModel(notifications);
        categories.subscribeListModel(upcoming);
    }

    private void initializeAdditionalStyles() {
        JFXDepthManager.setDepth(header, 1);

        addCategoryIcon.setOnMouseEntered(event -> addCategoryIcon.setFill(ChiclePadColor.PRIMARY));
        addCategoryIcon.setOnMouseExited(event -> addCategoryIcon.setFill(ChiclePadColor.BLACK));

        upcomingListView.setExpanded(true);
        notificationsListView.setExpanded(true);
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

    private void initializeUpcoming() {
        upcoming = new UpcomingListModel(upcomingListView);
        todoDao.getAll(loggedInUser.getId()).stream()
                .sorted(Comparator.comparing(Todo::getDeadline))
                .forEach(todo -> upcoming.add(todo));
    }

    private void initializeNotifications() {
        notifications = new NotificationsListModel(notificationsListView);

        noteDao.getAll(loggedInUser.getId()).stream()
                .filter(note -> note.getReminderTime().isPresent())
                .sorted(Comparator.comparing(note2 -> note2.getReminderTime().get()))
                .forEach(note -> notifications.addNote(note));

        goalDao.getAllGoalsNotCompletedToday(loggedInUser.getId())
                .forEach(goal -> notifications.addGoal(goal));
    }

    @FXML
    public void clearScene() {
        upcoming.clearUpcoming();
        notifications.clearNotifications();
    }

    @FXML
    public void refreshFilter() {
        String filter = searchTextField.getText();
        upcoming.filterByText(filter);
        notifications.filterByText(filter);
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
    public void switchToTodoScene() {
        ChiclePadApp.switchScene(new TodoSceneController(), "homepage/todoScene.fxml");
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
