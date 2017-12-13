package org.chiclepad.frontend.jfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.chiclepad.frontend.jfx.homepage.TodoSceneController;
import org.chiclepad.frontend.jfx.startup.LoginSceneController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ChiclePadApp extends Application {

    private static Stage primaryStage;

    private static final Logger logger = LoggerFactory.getLogger(ChiclePadApp.class);

    public void start(final Stage primaryStage) throws Exception {
        ChiclePadApp.primaryStage = primaryStage;
        Scene scene = loadStartupScene();

        primaryStage.setScene(scene);
        configurePrimaryStage(primaryStage);
        primaryStage.show();

        switchScene(new TodoSceneController(), "homepage/todoScene.fxml");
    }

    private Scene loadStartupScene() throws IOException {
        LoginSceneController loginSceneController = new LoginSceneController();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("startup/loginScene.fxml"));
        fxmlLoader.setController(loginSceneController);
        Parent parentPane = fxmlLoader.load();

        return new Scene(parentPane);
    }

    private void configurePrimaryStage(Stage primaryStage) {
        primaryStage.setTitle("ChiclePad");
        primaryStage.getIcons().add(new Image(ChiclePadApp.class.getResourceAsStream("../favicon.png")));
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

}
