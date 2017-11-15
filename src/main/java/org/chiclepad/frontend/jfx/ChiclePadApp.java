package org.chiclepad.frontend.jfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ChiclePadApp extends Application {

    private static Stage primaryStage;

    public void start(final Stage primaryStage) throws Exception {
        ChiclePadApp.primaryStage = primaryStage;

        LoginSceneController loginSceneController = new LoginSceneController();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("loginScene.fxml"));
        fxmlLoader.setController(loginSceneController);
        Parent parentPane = fxmlLoader.load();

        Scene scene = new Scene(parentPane);

        primaryStage.setScene(scene);
        primaryStage.setTitle("ChiclePad");
        primaryStage.show();
    }

    public static void switchScene(FXMLLoader fxmlLoader) throws IOException {
        Parent parentPane = fxmlLoader.load();
        Scene scene = new Scene(parentPane);
        ChiclePadApp.primaryStage.setScene(scene);
    }

}
