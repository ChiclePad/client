package org.chiclepad.frontend.jfx.startup;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import org.chiclepad.frontend.jfx.ChiclePadApp;
import org.chiclepad.frontend.jfx.ChiclePadColor;
import org.chiclepad.frontend.jfx.ChiclePadDialog;
import org.chiclepad.frontend.jfx.homepage.HomeSceneController;

public class LoginSceneController {

    @FXML
    private StackPane overlay;

    @FXML
    private JFXTextField emailTextField;

    @FXML
    private JFXPasswordField passwordField;

    @FXML
    private JFXButton loginButton;

    private boolean emailValid;

    private boolean passwordValid;

    @FXML
    public void initialize() {
        emailTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            emailValid = EmailValiditator.INSTANCE.validEmail(newValue);
            setTextFieldColor(emailTextField, emailValid ? ChiclePadColor.PRIMARY : ChiclePadColor.SECONDARY);
            loginButton.setDisable(!(passwordValid && emailValid));
        });

        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            passwordValid = !newValue.isEmpty();
            setTextFieldColor(passwordField, passwordValid ? ChiclePadColor.PRIMARY : ChiclePadColor.SECONDARY);
            loginButton.setDisable(!(passwordValid && emailValid));
        });
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
    public void onRegisterPressed() {
        ChiclePadApp.switchScene(new RegisterSceneController(), "startup/registerScene.fxml");
    }

    @FXML
    public void onLoginPressed() {
        // TODO login user
        boolean loginSuccesfull = true;
        if (loginSuccesfull) {
            ChiclePadApp.switchScene(new HomeSceneController(), "homepage/homeScene.fxml");
        } else {
            ChiclePadDialog.show("Login Failed!", "Check if you entered correct email and password.", overlay);
        }
    }

}
