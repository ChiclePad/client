package org.chiclepad.frontend.jfx.homepage;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXListView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import org.chiclepad.frontend.jfx.ChiclePadApp;
import org.chiclepad.frontend.jfx.MOCKUP;

public class TodoSceneController {

    @FXML
    private HBox userArea;

    @FXML
    private Label usernameLabel;

    @FXML
    private TextField searchTextField;

    @FXML
    private JFXListView<JFXCheckBox> categories;

    private String filter = "";

    @FXML
    public void initialize() {
        // TODO get real user
        MOCKUP.USER.getName().ifPresent(name -> usernameLabel.setText(name));

        // TODO get real categories
        MOCKUP.CATEGORIES.forEach(category -> HomeSceneController.addCategory(category, categories.getItems()));
    }


    @FXML
    public void refreshFilter() {
        filter = searchTextField.getText();
        // TODO reload
    }

    @FXML
    public void userClick() {
        HomeSceneController.showUserPopup(userArea);
    }

    @FXML
    public void switchToHomeScene() {
        ChiclePadApp.switchScene(new HomeSceneController(), "homepage/homeScene.fxml");
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
