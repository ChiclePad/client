package org.chiclepad.frontend.jfx.startup;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.effects.JFXDepthManager;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.chiclepad.backend.business.session.Authenticator;
import org.chiclepad.backend.business.session.BadPasswordException;
import org.chiclepad.constants.ChiclePadColor;
import org.chiclepad.frontend.jfx.ChiclePadApp;
import org.chiclepad.frontend.jfx.Popup.ChiclePadDialog;
import org.chiclepad.frontend.jfx.homepage.HomeSceneController;
import org.springframework.dao.EmptyResultDataAccessException;

public class LoginSceneController {

    @FXML
    private StackPane overlay;

    @FXML
    private VBox formLayout;

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
        initializeAdditionalStyles();
        addEmailValidator();
        addPasswordValidator();
    }

    private void initializeAdditionalStyles() {
        JFXDepthManager.setDepth(formLayout, 3);
    }

    private void addEmailValidator() {
        emailTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            emailValid = EmailValidator.INSTANCE.validEmail(newValue);
            setTextFieldColor(emailTextField, emailValid ? ChiclePadColor.PRIMARY : ChiclePadColor.SECONDARY);
            loginButton.setDisable(!(passwordValid && emailValid));
        });
    }

    private void addPasswordValidator() {
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
        Authenticator authenticator = Authenticator.INSTANCE;
        try {
            authenticator.logIn(this.emailTextField.getText(), this.passwordField.getText());
            ChiclePadApp.switchScene(new HomeSceneController(), "homepage/homeScene.fxml");

        } catch (BadPasswordException | IllegalArgumentException e1) {
            ChiclePadDialog.show("Login Failed!", "Bad password entered.", overlay);

        } catch (EmptyResultDataAccessException e) {
            ChiclePadDialog.show("Login Failed!", "Bad email entered.", overlay);
        }

        loginButton.setDisable(true);
    }

}
