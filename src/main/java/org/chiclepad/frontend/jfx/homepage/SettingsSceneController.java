package org.chiclepad.frontend.jfx.homepage;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.effects.JFXDepthManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import org.chiclepad.backend.Dao.ChiclePadUserDao;
import org.chiclepad.backend.Dao.DaoFactory;
import org.chiclepad.backend.business.LocaleUtils;
import org.chiclepad.backend.business.session.Authenticator;
import org.chiclepad.backend.business.session.UserSessionManager;
import org.chiclepad.backend.entity.ChiclePadUser;
import org.chiclepad.constants.ChiclePadColor;
import org.chiclepad.frontend.jfx.ChiclePadApp;
import org.chiclepad.frontend.jfx.Popup.ChiclePadDialog;
import org.chiclepad.frontend.jfx.Popup.UserPopup;

public class SettingsSceneController {

    @FXML
    private BorderPane header;

    @FXML
    private HBox userArea;

    @FXML
    private Label usernameLabel;

    @FXML
    private JFXPasswordField passwordField;

    @FXML
    private JFXPasswordField verifyPasswordField;

    @FXML
    private JFXButton passwordButton;

    @FXML
    private JFXTextField nameTextField;

    @FXML
    private JFXComboBox<String> languageComboBox;

    @FXML
    private StackPane dialogArea;

    private boolean passwordValid;

    private boolean verifyPasswordValid;

    private ChiclePadUser loggedInUser;

    private ChiclePadUserDao userDao = DaoFactory.INSTANCE.getChiclePadUserDao();

    @FXML
    public void initialize() {
        initializeAdditionalStyles();
        initializeUserName();
        initializePasswordVerification();
        initializeLanguagePicker();
    }

    private void initializeAdditionalStyles() {
        JFXDepthManager.setDepth(header, 1);
    }

    private void initializePasswordVerification() {
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            passwordValid = !newValue.isEmpty();

            verifyPasswordValid = !newValue.isEmpty() && newValue.equals(passwordField.getText());
            setTextFieldColor(verifyPasswordField, verifyPasswordValid ? ChiclePadColor.PRIMARY : ChiclePadColor.SECONDARY);

            passwordButton.setDisable(!(passwordValid && verifyPasswordValid));
        });

        verifyPasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
            verifyPasswordValid = !newValue.isEmpty() && newValue.equals(passwordField.getText());
            setTextFieldColor(verifyPasswordField, verifyPasswordValid ? ChiclePadColor.PRIMARY : ChiclePadColor.SECONDARY);
            passwordButton.setDisable(!(passwordValid && verifyPasswordValid));
        });
    }

    private void initializeUserName() {
        int userId = UserSessionManager.INSTANCE.getCurrentUserSession().getUserId();
        loggedInUser = userDao.get(userId);
        loggedInUser.getName().ifPresent(name -> {
                    usernameLabel.setText(name);
                    nameTextField.setText(name);
                }
        );

        nameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            this.loggedInUser.setName(newValue);
            usernameLabel.setText(newValue);
        });

        nameTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                return;
            }

            this.userDao.updateDetails(this.loggedInUser);
        });
    }

    private void initializeLanguagePicker() {
        languageComboBox.getItems().addAll(LocaleUtils.getReadableLocales());
        languageComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            String localeCode = LocaleUtils.getCodeFromReadableLocale(newValue);
            this.loggedInUser.setLocale(LocaleUtils.localeFromCode(localeCode));
            this.userDao.updateDetails(this.loggedInUser);
        });

        loggedInUser.getLocale().ifPresent(locale -> {
            int index = LocaleUtils.getAllLocals().indexOf(locale);
            languageComboBox.getSelectionModel().select(index);
        });
    }

    private void setTextFieldColor(JFXPasswordField textField, Color color) {
        textField.setFocusColor(color);
        textField.setUnFocusColor(color);
    }

    @FXML
    public void changePassword() {
        String newPassword = this.passwordField.getText();
        String newHashedPassword = Authenticator.INSTANCE.hashPassword(newPassword);
        this.loggedInUser.setPassword(newHashedPassword);

        try {
            this.userDao.updatePassword(loggedInUser);
            ChiclePadDialog.show("Success!", "Password changed", dialogArea);

            passwordField.setText("");
            verifyPasswordField.setText("");

        } catch (Exception e) {
            ChiclePadDialog.show("Error!", "Password failed to change", dialogArea);
            passwordButton.setDisable(true);
        }
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

    @FXML
    public void switchToNoteScene() {
        ChiclePadApp.switchScene(new NoteSceneController(), "homepage/noteScene.fxml");
    }

}
