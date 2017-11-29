package org.chiclepad.frontend.jfx.homepage;

import com.jfoenix.controls.JFXListView;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconName;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.chiclepad.frontend.jfx.ChiclePadApp;

public class GoalSceneController {

    @FXML
    private TextField searchTextField;

    @FXML
    private JFXListView tagList;

    private String filter = "";

    @FXML
    public void initialize() {
        FontAwesomeIconName[] icons = FontAwesomeIconName.values();

        for (int i = 0; i < 4; i++) {
            Label line = new Label("test");
            FontAwesomeIcon icon = new FontAwesomeIcon();
            icon.setIcon(icons[(int) (Math.random() * icons.length)]);
            line.setGraphic(icon);

            tagList.getItems().add(line);
            //        FontAwesomeIconName.TROPHY
        }
    }


    @FXML
    public void refreshFilter() {
        filter = searchTextField.getText();
        // TODO reload
    }

    @FXML
    public void switchToTodoScene() {
        ChiclePadApp.switchScene(new TodoSceneController(), "todoScene.fxml");
    }

    @FXML
    public void switchToDiaryScene() {
        ChiclePadApp.switchScene(new DiarySceneController(), "diaryScene.fxml");
    }

    @FXML
    public void switchToNoteScene() {
        ChiclePadApp.switchScene(new NoteSceneController(), "noteScene.fxml");
    }

}
