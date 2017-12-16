package org.chiclepad.frontend.jfx.homepage;

import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXTextField;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

public class CategoryPopup {

    public static void showUnderParent(Node parent) {
        JFXPopup popup = new JFXPopup();
        VBox layout = createPopupLayout(popup);
        popup.setPopupContent(layout);
        popup.show(parent, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT, 0, 0);
    }

    private static VBox createPopupLayout(JFXPopup popup) {
        JFXColorPicker a = new JFXColorPicker();
        JFXTextField b = new JFXTextField();

        VBox layout = new VBox(a, b);
        styleLayout(layout);

        return layout;
    }

    private static void styleLayout(VBox layout) {
        layout.setPadding(new Insets(15, 10, 15, 10));
        layout.setSpacing(10);
    }
//
//    private static JFXButton createSettingsButton(JFXPopup parent) {
//        JFXButton settingsButton = new JFXButton("Settings");
//        settingsButton.setPadding(new Insets(10, 10, 10, 10));
//
//        FontAwesomeIcon settingsIcon = new FontAwesomeIcon();
//        settingsIcon.setIcon(FontAwesomeIconName.GEAR);
//        settingsButton.setGraphic(settingsIcon);
//
//        settingsButton.setOnAction(event -> {
//            ChiclePadApp.switchScene(new SettingsSceneController(), "homepage/settingsScene.fxml");
//            parent.hide();
//        });
//
//        return settingsButton;
//    }
//
//    private static JFXButton createLogoutButton(JFXPopup parent) {
//        JFXButton logoutButton = new JFXButton("Log Out");
//        logoutButton.setPadding(new Insets(10, 10, 10, 10));
//
//        FontAwesomeIcon logoutIcon = new FontAwesomeIcon();
//        logoutIcon.setIcon(FontAwesomeIconName.SIGN_OUT);
//        logoutButton.setGraphic(logoutIcon);
//
//        logoutButton.setOnAction(event -> {
//            ChiclePadApp.switchScene(new LoginSceneController(), "startup/loginScene.fxml");
//            parent.hide();
//        });
//
//        return logoutButton;
//    }

}
