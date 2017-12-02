package org.chiclepad.frontend.jfx;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.chiclepad.frontend.jfx.homepage.HomeSceneController;
import org.chiclepad.frontend.jfx.startup.LoginSceneController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ChiclePadApp extends Application {

    private static Stage primaryStage;

    private static final Logger logger = LoggerFactory.getLogger(ChiclePadApp.class);

    public void start(final Stage primaryStage) throws Exception {
        ChiclePadApp.primaryStage = primaryStage;

        LoginSceneController loginSceneController = new LoginSceneController();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("startup/loginScene.fxml"));
        fxmlLoader.setController(loginSceneController);
        Parent parentPane = fxmlLoader.load();

//        FontAwesomeIconName.CIRCLE_ALT
        Scene scene = new Scene(parentPane);

        primaryStage.setScene(scene);
        primaryStage.setTitle("ChiclePad");
        primaryStage.getIcons().add(new Image(ChiclePadApp.class.getResourceAsStream("../favicon.png")));
        primaryStage.show();

        // TODO remove - only for testing
        switchScene(new HomeSceneController(), "homepage/homeScene.fxml");
    }

    public static void switchScene(Object controller, String fxmlPath) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(ChiclePadApp.class.getResource(fxmlPath));
            fxmlLoader.setController(controller);

            Parent parentPane = fxmlLoader.load();
            primaryStage.getScene().setRoot(parentPane);
        } catch (IOException e) {
            logger.error("Failed switching to scene: " + fxmlPath + "\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * @param header Header text
     * @param body   Body text
     * @param parent Invisible Stack Pane where the dialog should be displayed
     */
    public static void showDialog(String header, String body, StackPane parent) {
        JFXDialogLayout dialogLayout = new JFXDialogLayout();

        dialogLayout.setHeading(toText(header, 20));
        dialogLayout.setBody(toText(body, 16));

        JFXDialog dialog = new JFXDialog(parent, dialogLayout, JFXDialog.DialogTransition.TOP, true);

        JFXButton closeButton = createCloseButton();
        closeButton.setOnAction(event -> {
            dialog.close();
            parent.setVisible(false);
        });
        dialogLayout.setActions(closeButton);

        parent.setVisible(true);
        dialog.show();
    }

    private static Text toText(String string, int fontSize) {
        Text result = new Text(string);
        result.setStyle("-fx-text-fill: #464947;" +
                "-fx-font-size: " + fontSize + "px");

        return result;
    }

    private static JFXButton createCloseButton() {
        JFXButton closeButton = new JFXButton("Okay");
        closeButton.setStyle("-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 15, 0, 1, 3);" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 16px;" +
                "-fx-background-color: #3E5641");
        closeButton.setPadding(new Insets(7, 18, 7, 18));
        return closeButton;
    }

}
