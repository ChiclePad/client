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
import org.chiclepad.backend.Dao.NoteDao;
import org.chiclepad.backend.business.session.UserSessionManager;
import org.chiclepad.backend.entity.Category;
import org.chiclepad.backend.entity.ChiclePadUser;
import org.chiclepad.backend.entity.Note;
import org.chiclepad.constants.ChiclePadColor;
import org.chiclepad.frontend.jfx.ChiclePadApp;
import org.chiclepad.frontend.jfx.Popup.CategoryPopup;
import org.chiclepad.frontend.jfx.Popup.UserPopup;
import org.chiclepad.frontend.jfx.model.CategoryListModel;
import org.chiclepad.frontend.jfx.model.NoteListModel;

import java.util.List;

public class NoteSceneController {

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
    private JFXMasonryPane noteMasonry;

    @FXML
    private JFXTextField descriptionField;

    @FXML
    private JFXDatePicker reminderDate;

    @FXML
    private JFXTimePicker reminderTime;

    @FXML
    private FontAwesomeIcon addCategoryIcon;

    private CategoryListModel categories;

    private NoteListModel notes;

    private ChiclePadUser loggedInUser;

    private ChiclePadUserDao userDao = DaoFactory.INSTANCE.getChiclePadUserDao();

    private CategoryDao categoryDao = DaoFactory.INSTANCE.getCategoryDao();

    private NoteDao noteDao = DaoFactory.INSTANCE.getNoteDao();

    @FXML
    public void initialize() {
        initializeAdditionalStyles();
        initializeUser();
        initializeNotes();
        initializeCategories();

        categories.subscribeListModel(notes);
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

    private void initializeNotes() {
        notes = new NoteListModel(noteMasonry, descriptionField, reminderDate, reminderTime);
        this.noteDao.getAll(this.loggedInUser.getId()).forEach(note -> this.notes.add(note));
    }

    @FXML
    public void clearScene() {
        notes.clearNotes();
    }

    @FXML
    public void refreshFilter() {
        String filter = searchTextField.getText();
        notes.setNewTextFilter(filter);
    }

    @FXML
    public void addNote() {
        Note created = noteDao.create(loggedInUser.getId(), "");
        notes.add(created);
    }

    @FXML
    public void deleteSelected() {
        Note deleted = notes.deleteSelected();

        if (deleted == null) {
            return;
        }

        noteDao.markDeleted(deleted);
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
    public void switchToDiaryScene() {
        ChiclePadApp.switchScene(new DiarySceneController(), "homepage/diaryScene.fxml");
    }

}
