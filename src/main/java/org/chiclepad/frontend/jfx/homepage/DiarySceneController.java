package org.chiclepad.frontend.jfx.homepage;

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
import org.chiclepad.backend.Dao.DiaryPageDao;
import org.chiclepad.backend.entity.Category;
import org.chiclepad.backend.entity.ChiclePadUser;
import org.chiclepad.backend.entity.DiaryPage;
import org.chiclepad.business.session.UserSessionManager;
import org.chiclepad.frontend.jfx.ChiclePadApp;
import org.chiclepad.frontend.jfx.ChiclePadColor;
import org.chiclepad.frontend.jfx.model.CategoryListModel;
import org.chiclepad.frontend.jfx.model.DiaryListModel;

import java.time.LocalDate;
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
    private VBox diaryPagesList;

    @FXML
    private VBox categoriesRippler;

    @FXML
    private HBox loadNextButton;

    @FXML
    private FontAwesomeIcon loadNextIcon;

    @FXML
    private Label loadNextText;

    @FXML
    private HBox loadPreviousButton;

    @FXML
    private FontAwesomeIcon loadPreviousIcon;

    @FXML
    private Label loadPreviousText;

    @FXML
    private FontAwesomeIcon addCategoryIcon;

    private DiaryListModel diaryPages;

    private CategoryListModel categories;

    private ChiclePadUser loggedInUser;

    private ChiclePadUserDao userDao = DaoFactory.INSTANCE.getChiclePadUserDao();

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

        addCategoryIcon.setOnMouseEntered(event -> addCategoryIcon.setFill(ChiclePadColor.PRIMARY));
        addCategoryIcon.setOnMouseExited(event -> addCategoryIcon.setFill(ChiclePadColor.BLACK));

        loadNextButton.setOnMouseEntered(event -> {
            loadNextIcon.setFill(ChiclePadColor.PRIMARY);
            loadNextText.setTextFill(ChiclePadColor.PRIMARY);
        });
        loadNextButton.setOnMouseExited(event -> {
            loadNextIcon.setFill(ChiclePadColor.BLACK);
            loadNextText.setTextFill(ChiclePadColor.BLACK);
        });

        loadPreviousButton.setOnMouseEntered(event -> {
            loadPreviousIcon.setFill(ChiclePadColor.PRIMARY);
            loadPreviousText.setTextFill(ChiclePadColor.PRIMARY);
        });
        loadPreviousButton.setOnMouseExited(event -> {
            loadPreviousIcon.setFill(ChiclePadColor.BLACK);
            loadPreviousText.setTextFill(ChiclePadColor.BLACK);
        });
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
        diaryPages = new DiaryListModel(diaryPagesList);
        this.diaryPageDao.getAll(this.loggedInUser.getId()).forEach(diaryPage -> this.diaryPages.add(diaryPage));
    }

    @FXML
    public void clearScene() {
        diaryPages.clearDiaryPages();
    }

    @FXML
    public void refreshFilter() {
        String filter = searchTextField.getText();
        diaryPages.setNewFilter(filter);
    }

    @FXML
    public void addDiaryPage() {
        DiaryPage created = diaryPageDao.create(loggedInUser.getId(), "", LocalDate.now());
        diaryPages.add(created);
    }

    @FXML
    public void deleteSelected() {
        DiaryPage deleted = diaryPages.deleteSelected();
        diaryPageDao.delete(deleted);
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
    public void switchToGoalScene() {
        ChiclePadApp.switchScene(new GoalSceneController(), "homepage/goalScene.fxml");
    }

    @FXML
    public void switchToNoteScene() {
        ChiclePadApp.switchScene(new NoteSceneController(), "homepage/noteScene.fxml");
    }

}
