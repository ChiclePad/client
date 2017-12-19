package org.chiclepad.frontend.jfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.chiclepad.frontend.jfx.startup.LoginSceneController;

import java.io.IOException;

public class ChiclePadApp extends Application {

    private static Stage primaryStage;

    public void start(final Stage primaryStage) throws Exception {
        ChiclePadApp.primaryStage = primaryStage;
        Scene scene = loadStartupScene();

        primaryStage.setScene(scene);
        configurePrimaryStage(primaryStage);
        primaryStage.show();
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
            System.err.println("Failed switching to scene: " + fxmlPath + "\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    public static String darken(String hexColor, double amount) {
        Color color = Color.web(hexColor);

        double r = color.getRed();
        double b = color.getBlue();
        double g = color.getGreen();

        return String.format("#%02x%02x%02x", (int) (r * amount * 255), (int) (g * amount * 255), (int) (b * amount * 255));
    }

    public static String darken(String hexColor) {
        return darken(hexColor, 0.95);
    }

}
