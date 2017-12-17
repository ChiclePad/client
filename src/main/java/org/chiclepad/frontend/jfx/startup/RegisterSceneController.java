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
import org.chiclepad.backend.business.LocaleUtils;
import org.chiclepad.backend.business.session.Authenticator;
import org.chiclepad.backend.business.session.UserAlreadyExistsException;
import org.chiclepad.backend.business.session.UserSession;
import org.chiclepad.constants.ChiclePadColor;
import org.chiclepad.frontend.jfx.ChiclePadApp;
import org.chiclepad.frontend.jfx.Popup.ChiclePadDialog;
import org.chiclepad.frontend.jfx.homepage.HomeSceneController;

import java.util.Locale;
import java.util.Optional;

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
        addEmailValidator();
        addPasswordValidator();
        initializeLocaleChooser();
    }

    private void initializeAdditionalStyles() {
        JFXDepthManager.setDepth(formLayout, 3);
    }

    private void addEmailValidator() {
        emailTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            emailValid = EmailValidator.INSTANCE.validEmail(newValue);
            setTextFieldColor(emailTextField, emailValid ? ChiclePadColor.PRIMARY : ChiclePadColor.SECONDARY);
            registerButton.setDisable(!(passwordValid && emailValid));
        });
    }

    private void addPasswordValidator() {
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

    private Optional<Locale> getSelectedLocale() {
        String readableLocale = languageComboBox.getSelectionModel().selectedItemProperty().getValue();
        String code = LocaleUtils.getCodeFromReadableLocale(readableLocale);
        return Optional.ofNullable(code).map(LocaleUtils::localeFromCode);
    }

    @FXML
    public void onBackPressed() {
        ChiclePadApp.switchScene(new LoginSceneController(), "startup/loginScene.fxml");
    }

    @FXML
    public void onRegisterPressed() {
        try {
            Authenticator authenticator = Authenticator.INSTANCE;
            UserSession userSession = authenticator.register(this.emailTextField.getText(), this.passwordField.getText());

            String name = this.nameTextField.getText();
            userSession.getLoggedUser().setName(name);
            this.getSelectedLocale().ifPresent(locale -> userSession.getLoggedUser().setLocale(locale));
            userDao.updateDetails(userSession.getLoggedUser());

            ChiclePadApp.switchScene(new HomeSceneController(), "homepage/homeScene.fxml");

        } catch (UserAlreadyExistsException e) {
            ChiclePadDialog.show("Registration Failed!", "Email already in use.", overlay);
        }

        registerButton.setDisable(true);
    }

}
