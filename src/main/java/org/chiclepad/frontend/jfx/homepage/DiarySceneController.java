package org.chiclepad.frontend.jfx.homepage;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.effects.JFXDepthManager;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.chiclepad.backend.Dao.CategoryDao;
import org.chiclepad.backend.Dao.ChiclePadUserDao;
import org.chiclepad.backend.Dao.DaoFactory;
import org.chiclepad.backend.Dao.DiaryPageDao;
import org.chiclepad.backend.business.session.UserSessionManager;
import org.chiclepad.backend.entity.Category;
import org.chiclepad.backend.entity.ChiclePadUser;
import org.chiclepad.backend.entity.DiaryPage;
import org.chiclepad.constants.ChiclePadColor;
import org.chiclepad.frontend.jfx.ChiclePadApp;
import org.chiclepad.frontend.jfx.Popup.CategoryPopup;
import org.chiclepad.frontend.jfx.Popup.UserPopup;
import org.chiclepad.frontend.jfx.model.CategoryListModel;
import org.chiclepad.frontend.jfx.model.DiaryListModel;

import java.time.LocalDate;
import java.util.List;

public class DiarySceneController {

    @FXML
    private ScrollPane scrollPane;

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
    private JFXComboBox categoryPicker;

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

    @FXML
    private JFXButton addDiaryPageButton;

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
        initializeDiaryPages();
        initializeCategories();

        categories.subscribeListModel(diaryPages);
    }

    private void initializeAdditionalStyles() {
        JFXDepthManager.setDepth(header, 1);

        addCategoryIcon.setOnMouseEntered(event -> addCategoryIcon.setFill(ChiclePadColor.PRIMARY));
        addCategoryIcon.setOnMouseExited(event -> addCategoryIcon.setFill(ChiclePadColor.BLACK));

        colorLoadButtonGreenOnHover(loadNextButton, loadNextIcon, loadNextText);
        colorLoadButtonGreenOnHover(loadPreviousButton, loadPreviousIcon, loadPreviousText);
    }

    private void colorLoadButtonGreenOnHover(HBox button, FontAwesomeIcon icon, Label text) {
        button.setOnMouseEntered(event -> {
            icon.setFill(ChiclePadColor.PRIMARY);
            text.setTextFill(ChiclePadColor.PRIMARY);
        });
        button.setOnMouseExited(event -> {
            icon.setFill(ChiclePadColor.BLACK);
            text.setTextFill(ChiclePadColor.BLACK);
        });
    }

    private void initializeUser() {
        int userId = UserSessionManager.INSTANCE.getCurrentUserSession().getUserId();
        loggedInUser = userDao.get(userId);
        loggedInUser.getName().ifPresent(name -> usernameLabel.setText(name));
    }

    private void initializeDiaryPages() {
        diaryPages = new DiaryListModel(
                diaryPagesList,
                loadPreviousText.textProperty(),
                loadNextText.textProperty(),
                scrollPane
        );

        List<DiaryPage> diaryPagesList = this.diaryPageDao.getAll(this.loggedInUser.getId());
        diaryPagesList.forEach(diaryPage -> this.diaryPages.add(diaryPage));

        diaryPages.filterByText("");

        Boolean addingDisabled = diaryPagesList.size() != 0 &&
                diaryPagesList.get(0).getRecordedDay().isEqual(LocalDate.now());
        addDiaryPageButton.setDisable(addingDisabled);
    }

    private void initializeCategories() {
        this.categories = new CategoryListModel(categoryList, categoriesRippler, categoryPicker);
        List<Category> categories = this.categoryDao.getAll(this.loggedInUser.getId());
        categories.forEach(category -> this.categories.add(category));
    }

    @FXML
    public void clearScene() {
        diaryPages.clearDiaryPages();
    }

    @FXML
    public void refreshFilter() {
        String filter = searchTextField.getText();
        diaryPages.filterByText(filter);
    }

    @FXML
    public void addDiaryPage() {
        DiaryPage created = diaryPageDao.create(loggedInUser.getId(), "", LocalDate.now());
        diaryPages.add(created);

        addDiaryPageButton.setDisable(true);
    }

    @FXML
    public void deleteSelected() {
        DiaryPage deleted = diaryPages.deleteSelected();

        if (deleted == null) {
            return;
        }

        diaryPageDao.markDeleted(deleted);

        List<DiaryPage> diaryPages = this.diaryPageDao.getAll(this.loggedInUser.getId());
        Boolean addingDisabled = diaryPages.size() != 0 && diaryPages.get(0).getRecordedDay().isEqual(LocalDate.now());
        addDiaryPageButton.setDisable(addingDisabled);
    }

    @FXML
    public void loadPrevious() {
        diaryPages.goToPreviousPage();
    }

    @FXML
    public void loadNext() {
        diaryPages.goToNextPage();
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
    public void switchToGoalScene() {
        ChiclePadApp.switchScene(new GoalSceneController(), "homepage/goalScene.fxml");
    }

    @FXML
    public void switchToNoteScene() {
        ChiclePadApp.switchScene(new NoteSceneController(), "homepage/noteScene.fxml");
    }

}
