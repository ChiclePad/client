package org.chiclepad.frontend.jfx;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;

import java.util.regex.Pattern;

public class LoginSceneController {

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
        String emailRegex = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08" +
                "\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:" +
                "[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|" +
                "[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08" +
                "\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

        Pattern emailPattern = Pattern.compile(emailRegex);

        emailTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            emailValid = emailPattern.matcher(newValue).matches();

            if (!emailValid) {
                emailTextField.setFocusColor(ChiclePadApp.SECONDARY_COLOR);
                emailTextField.setUnFocusColor(ChiclePadApp.SECONDARY_COLOR);

            } else {
                emailTextField.setFocusColor(ChiclePadApp.PRIMARY_COLOR);
                emailTextField.setUnFocusColor(ChiclePadApp.PRIMARY_COLOR);
            }

            loginButton.setDisable(!(passwordValid && emailValid));
        });

        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            passwordValid = !newValue.isEmpty();

            if (!passwordValid) {
                passwordField.setFocusColor(ChiclePadApp.SECONDARY_COLOR);
                passwordField.setUnFocusColor(ChiclePadApp.SECONDARY_COLOR);

            } else {
                passwordField.setFocusColor(ChiclePadApp.PRIMARY_COLOR);
                passwordField.setUnFocusColor(ChiclePadApp.PRIMARY_COLOR);
            }

            loginButton.setDisable(!(passwordValid && emailValid));
        });
    }

    public void onRegisterPressed() {
        ChiclePadApp.switchScene(new RegisterSceneController(), "registerScene.fxml");
    }

    public void onLoginPressed() {
        ChiclePadApp.switchScene(new HomepageSceneController(), "homepageScene.fxml");
    }

}
