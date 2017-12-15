package org.chiclepad.frontend.jfx.startup;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.effects.JFXDepthManager;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.chiclepad.backend.Dao.ChiclePadUserDao;
import org.chiclepad.backend.Dao.DaoFactory;
import org.chiclepad.business.LocaleUtils;
import org.chiclepad.business.session.Authentificator;
import org.chiclepad.business.session.UserAlreadyExistsException;
import org.chiclepad.business.session.UserSession;
import org.chiclepad.frontend.jfx.ChiclePadApp;
import org.chiclepad.frontend.jfx.ChiclePadColor;
import org.chiclepad.frontend.jfx.ChiclePadDialog;
import org.chiclepad.frontend.jfx.homepage.HomeSceneController;

import java.util.Locale;

public class RegisterSceneController {

    @FXML
    private StackPane overlay;

    @FXML
    private VBox formLayout;

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

    private ChiclePadUserDao userDao = DaoFactory.INSTANCE.getChiclePadUserDao();

    @FXML
    public void initialize() {
        initializeAdditionalStyles();
        addEmailValiditator();
        addPasswordValiditator();
        initializeLocaleChooser();
    }

    private void initializeAdditionalStyles() {
        JFXDepthManager.setDepth(formLayout, 3);
    }

    private void addEmailValiditator() {
        emailTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            emailValid = EmailValiditator.INSTANCE.validEmail(newValue);
            setTextFieldColor(emailTextField, emailValid ? ChiclePadColor.PRIMARY : ChiclePadColor.SECONDARY);
            registerButton.setDisable(!(passwordValid && emailValid));
        });
    }

    private void addPasswordValiditator() {
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            passwordValid = !newValue.isEmpty();
            setTextFieldColor(passwordField, passwordValid ? ChiclePadColor.PRIMARY : ChiclePadColor.SECONDARY);
            registerButton.setDisable(!(passwordValid && emailValid));
        });
    }

    private void initializeLocaleChooser() {
        languageComboBox.getItems().addAll(LocaleUtils.getReadableLocales());
    }

    private void setTextFieldColor(JFXTextField textField, Color color) {
        textField.setFocusColor(color);
        textField.setUnFocusColor(color);
    }

    private void setTextFieldColor(JFXPasswordField textField, Color color) {
        textField.setFocusColor(color);
        textField.setUnFocusColor(color);
    }

    private Locale getSelectedLocale() {
        String readableLocale = languageComboBox.getSelectionModel().selectedItemProperty().getValue();
        String code = LocaleUtils.getCodeFromReadableLocale(readableLocale);
        return code == null ? null : LocaleUtils.localeFromCode(code);
    }

    @FXML
    public void onBackPressed() {
        ChiclePadApp.switchScene(new LoginSceneController(), "startup/loginScene.fxml");
    }

    @FXML
    public void onRegisterPressed() {
        try {
            Authentificator authentificator = Authentificator.INSTANCE;
            UserSession userSession = authentificator.register(this.emailTextField.getText(), this.passwordField.getText());

            String name = this.nameTextField.getText();
            userSession.getLoggedUser().setName(name);
            userSession.getLoggedUser().setLocale(this.getSelectedLocale());
            userDao.updateDetails(userSession.getLoggedUser());

            ChiclePadApp.switchScene(new HomeSceneController(), "homepage/homeScene.fxml");

        } catch (UserAlreadyExistsException e) {
            ChiclePadDialog.show("Registration Failed!", "Email already in use.", overlay);
        }
    }

}
