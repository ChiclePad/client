package org.chiclepad.frontend.jfx.homepage;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPopup;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconName;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.chiclepad.backend.entity.Category;
import org.chiclepad.frontend.jfx.ChiclePadApp;
import org.chiclepad.frontend.jfx.MOCKUP;
import org.chiclepad.frontend.jfx.startup.LoginSceneController;

import java.util.List;

public class HomeSceneController {

    @FXML
    private HBox userArea;

    @FXML
    private Label usernameLabel;

    @FXML
    private Label welcomeUsernameLabel;

    @FXML
    private TextField searchTextField;

    @FXML
    private JFXListView<Label> categories;

    private String filter = "";

    @FXML
    public void initialize() {
        // TODO get real user
        MOCKUP.USER.getName().ifPresent(name -> usernameLabel.setText(name));
        MOCKUP.USER.getName().ifPresent(name -> welcomeUsernameLabel.setText(name));

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


    public static void showUserPopup(Node parent) {
        JFXPopup popup = new JFXPopup();

        JFXButton settingsButton = createSettingsButton(popup);
        JFXButton logoutButton = createLogoutButton(popup);
        VBox layout = new VBox(settingsButton, logoutButton);

        popup.setPopupContent(layout);
        popup.show(parent, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.RIGHT, 0, 40);
    }

    private static JFXButton createSettingsButton(JFXPopup parent) {
        JFXButton settingsButton = new JFXButton("Settings");
        settingsButton.setPadding(new Insets(10, 10, 10, 10));

        FontAwesomeIcon settingsIcon = new FontAwesomeIcon();
        settingsIcon.setIcon(FontAwesomeIconName.GEAR);
        settingsButton.setGraphic(settingsIcon);

        settingsButton.setOnAction(event -> {
            ChiclePadApp.switchScene(new SettingsSceneController(), "homepage/settingsScene.fxml");
            parent.hide();
        });

        return settingsButton;
    }

    private static JFXButton createLogoutButton(JFXPopup parent) {
        JFXButton logoutButton = new JFXButton("Log Out");
        logoutButton.setPadding(new Insets(10, 10, 10, 10));

        FontAwesomeIcon logoutIcon = new FontAwesomeIcon();
        logoutIcon.setIcon(FontAwesomeIconName.SIGN_OUT);
        logoutButton.setGraphic(logoutIcon);

        logoutButton.setOnAction(event -> {
            ChiclePadApp.switchScene(new LoginSceneController(), "startup/loginScene.fxml");
            parent.hide();
        });

        return logoutButton;
    }

    public static void addCategory(Category category, List<Label> categoryItems) {
        Label line = new Label();

        line.setText(category.getName());
        FontAwesomeIcon icon = new FontAwesomeIcon();
        icon.setIconName(category.getIcon());
        line.setGraphic(icon);

        categoryItems.add(line);
    }

}
