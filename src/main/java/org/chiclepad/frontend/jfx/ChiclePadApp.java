package org.chiclepad.frontend.jfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ChiclePadApp extends Application {

    public void start(final Stage primaryStage) throws Exception {
        MainSceneController mainSceneController = new MainSceneController();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MainScene.fxml"));
        fxmlLoader.setController(mainSceneController);
        Parent parentPane = fxmlLoader.load();

        Scene scene = new Scene(parentPane);

        primaryStage.setScene(scene);
        primaryStage.setTitle("ChiclePad");
        primaryStage.show();
    }

}
