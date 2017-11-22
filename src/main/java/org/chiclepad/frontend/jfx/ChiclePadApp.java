package org.chiclepad.frontend.jfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ChiclePadApp extends Application {

    private static Stage primaryStage;

    private static final Logger logger = LoggerFactory.getLogger(ChiclePadApp.class);

    public static final Color PRIMARY_COLOR = Color.valueOf("#8DC44E");

    public static final Color SECONDARY_COLOR = Color.valueOf("#A24936");

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

}
