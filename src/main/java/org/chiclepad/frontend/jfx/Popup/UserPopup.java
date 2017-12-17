package org.chiclepad.frontend.jfx.Popup;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPopup;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconName;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import org.chiclepad.frontend.jfx.ChiclePadApp;
import org.chiclepad.frontend.jfx.homepage.SettingsSceneController;
import org.chiclepad.frontend.jfx.startup.LoginSceneController;

public class UserPopup {

    public static void showUnderParent(Node parent) {
        JFXPopup popup = new JFXPopup();
        VBox layout = createPopupLayout(popup);
        popup.setPopupContent(layout);
        popup.show(parent, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.RIGHT, 0, 40);
    }

    private static VBox createPopupLayout(JFXPopup popup) {
        JFXButton settingsButton = createSettingsButton(popup);
        JFXButton logoutButton = createLogoutButton(popup);

        VBox layout = new VBox(settingsButton, logoutButton);
        styleLayout(layout);

        return layout;
    }

    private static void styleLayout(VBox layout) {
        layout.getStyleClass().addAll("form", "bordered");

        layout.setPadding(new Insets(15, 10, 15, 10));
        layout.setSpacing(10);
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

}
