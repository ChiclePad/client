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
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("loginScene.fxml"));
        fxmlLoader.setController(loginSceneController);
        Parent parentPane = fxmlLoader.load();

        Scene scene = new Scene(parentPane);

        primaryStage.setScene(scene);
        primaryStage.setTitle("ChiclePad");
        primaryStage.getIcons().add(new Image(ChiclePadApp.class.getResourceAsStream("../logo.png")));
        primaryStage.show();
    }

    public static void switchScene(Object controller, String fxmlPath) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(ChiclePadApp.class.getResource(fxmlPath));
            fxmlLoader.setController(controller);

            Parent parentPane = fxmlLoader.load();
            Scene scene = new Scene(parentPane);
            ChiclePadApp.primaryStage.setScene(scene);
        } catch (IOException e) {
            logger.error("Failed switching to scene: " + fxmlPath + "\n" + e.getMessage());
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

        JFXButton closeButton = new JFXButton("Okay");
        closeButton.setStyle("-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 15, 0, 1, 3);" +
                "-fx-font-family: Roboto;" +
                "-fx-text-fill: #fff;" +
                "-fx-font-size: 16px;" +
                "-fx-background-color: #3E5641");
        closeButton.setPadding(new Insets(7, 18, 7, 18));
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
        result.setStyle("-fx-font-family: Roboto;" +
                "-fx-text-fill: #464947;" +
                "-fx-font-size: " + fontSize + "px");

        return result;
    }

}
