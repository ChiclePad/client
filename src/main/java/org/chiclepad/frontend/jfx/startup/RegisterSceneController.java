package org.chiclepad.frontend.jfx.startup;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import org.chiclepad.backend.LocaleUtils;
import org.chiclepad.frontend.jfx.ChiclePadApp;
import org.chiclepad.frontend.jfx.ChiclePadColor;
import org.chiclepad.frontend.jfx.homepage.TodoSceneController;

import java.util.List;
import java.util.stream.Collectors;

public class RegisterSceneController {

    @FXML
    private StackPane overlay;

    @FXML
    private JFXTextField emailTextField;

    @FXML
    private JFXPasswordField passwordField;

    @FXML
    private JFXTextField nameTextField;

    @FXML
    private JFXComboBox<String> languageComboBox;

    @FXML
    private JFXButton registerButton;

    private boolean emailValid;

    private boolean passwordValid;

    @FXML
    public void initialize() {
        emailTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            emailValid = EmailValiditator.INSTANCE.validitate(newValue);
            setTextFieldColor(emailTextField, emailValid ? ChiclePadColor.PRIMARY : ChiclePadColor.SECONDARY);
            registerButton.setDisable(!(passwordValid && emailValid));
        });

        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            passwordValid = !newValue.isEmpty();
            setTextFieldColor(passwordField, passwordValid ? ChiclePadColor.PRIMARY : ChiclePadColor.SECONDARY);
            registerButton.setDisable(!(passwordValid && emailValid));
        });

        List<String> locales = LocaleUtils.getAllLocals()
                .stream()
                .map(LocaleUtils::localeName)
                .collect(Collectors.toList());

        languageComboBox.getItems().addAll(locales);
    }

    private void setTextFieldColor(JFXTextField textField, Color color) {
        textField.setFocusColor(color);
        textField.setUnFocusColor(color);
    }

    private void setTextFieldColor(JFXPasswordField textField, Color color) {
        textField.setFocusColor(color);
        textField.setUnFocusColor(color);
    }

    @FXML
    public void onBackPressed() {
        ChiclePadApp.switchScene(new LoginSceneController(), "loginScene.fxml");
    }

    @FXML
    public void onRegisterPressed() {
        // TODO register user
        boolean registerSuccesfull = true;
        if (registerSuccesfull) {
            ChiclePadApp.switchScene(new TodoSceneController(), "todoScene.fxml");
        } else {
            ChiclePadApp.showDialog("Registration Failed!", "Email already in use.", overlay);
        }
    }

}
