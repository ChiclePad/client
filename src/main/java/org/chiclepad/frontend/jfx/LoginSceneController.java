package org.chiclepad.frontend.jfx;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class LoginSceneController {

    @FXML
    private JFXTextField emailTextField;

    @FXML
    private JFXPasswordField passwordField;

    public void switchToRegisterScene() {
        RegisterSceneController registerSceneController = new RegisterSceneController();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("registerScene.fxml"));
        fxmlLoader.setController(registerSceneController);

        try {
            ChiclePadApp.switchScene(fxmlLoader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void validitateLogin() {
        // TODO validitate
        if (emailTextField.getText().isEmpty()) {
            return;
        }

        if (passwordField.getText().isEmpty()) {
            return;
        }
//        JFXTextField field = new JFXTextField();
//        field.setLabelFloat(true);
//        field.setPromptText("Floating prompt");
//
//        JFXTextField validationField = new JFXTextField();
//        validationField.setPromptText("With Validation..");
//        RequiredFieldValidator validator = new RequiredFieldValidator();
//        validator.setMessage("Input Required");
//        validator.setAwsomeIcon(new Icon(AwesomeIcon.WARNING, "2em", ";", "error"));
//        validationField.getValidators().add(validator);
//        validationField.focusedProperty().addListener((o, oldVal, newVal) -> {
//            if (!newVal) validationField.validate();
//        });

        switchToHomepageScene();
    }

    private void switchToHomepageScene() {
        HomepageSceneController homepageSceneController = new HomepageSceneController();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("homepageScene.fxml"));
        fxmlLoader.setController(homepageSceneController);

        try {
            ChiclePadApp.switchScene(fxmlLoader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
