package org.chiclepad.frontend.jfx.homepage;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.effects.JFXDepthManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.chiclepad.backend.LocaleUtils;
import org.chiclepad.frontend.jfx.ChiclePadApp;
import org.chiclepad.frontend.jfx.ChiclePadColor;
import org.chiclepad.frontend.jfx.ChiclePadDialog;
import org.chiclepad.frontend.jfx.MOCKUP;

public class SettingsSceneController {

    @FXML
    private AnchorPane content;

    @FXML
    private BorderPane header;

    @FXML
    private VBox passwodPanel;

    @FXML
    private VBox detailsPanel;

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
    private StackPane stackPane;

    private boolean passwordValid;

    private boolean verifyPasswordValid;

    @FXML
    public void initialize() {
        initializeAdditionalStyles();
        initializeUserName();
        initializePasswordVerifiaction();
        initializeLanguagePicker();
    }

    private void initializeAdditionalStyles() {
        JFXDepthManager.setDepth(header, 1);
    }

    private void initializePasswordVerifiaction() {
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            passwordValid = !newValue.isEmpty();
            passwordButton.setDisable(!(passwordValid && verifyPasswordValid));
        });

        verifyPasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
            verifyPasswordValid = !newValue.isEmpty() && newValue.equals(passwordField.getText());
            setTextFieldColor(verifyPasswordField, verifyPasswordValid ? ChiclePadColor.PRIMARY : ChiclePadColor.SECONDARY);
            passwordButton.setDisable(!(passwordValid && verifyPasswordValid));
        });
    }

    private void initializeUserName() {
        // TODO get real user
        MOCKUP.USER.getName().ifPresent(name -> {
            nameTextField.setText(name);
            usernameLabel.setText(name);
        });

        nameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            MOCKUP.USER.setName(newValue);
            usernameLabel.setText(newValue);
        });
        // TODO send data
        // nameTextField.focusColorProperty().addListener((observable, oldValue, newValue) -> SEND DATA);
    }

    private void initializeLanguagePicker() {
        languageComboBox.getItems().addAll(LocaleUtils.getReadableLocales());
        languageComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            String localeCode = LocaleUtils.getCodeFromReadableLocale(newValue);
            MOCKUP.USER.setLocale(LocaleUtils.localeFromCode(localeCode));
            // TODO send
        });
    }

    private void setTextFieldColor(JFXPasswordField textField, Color color) {
        textField.setFocusColor(color);
        textField.setUnFocusColor(color);
    }

    @FXML
    public void changePassword() {
        String newPassword = this.passwordField.getText();
        MOCKUP.USER.setPassword(newPassword);

        // TODO send
        boolean passwordChanged = true;
        if (passwordChanged) {
            ChiclePadDialog.show("Success!", "Password changed", stackPane);
            passwordField.setText("");
            verifyPasswordField.setText("");
        } else {
            ChiclePadDialog.show("Error!", "Password failed to change", stackPane);
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
