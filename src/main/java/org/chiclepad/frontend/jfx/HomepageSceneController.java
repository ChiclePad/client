package org.chiclepad.frontend.jfx;

import com.jfoenix.controls.JFXListView;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconName;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class HomepageSceneController {

    @FXML
    private TextField searchTextField;

    @FXML
    private FontAwesomeIcon todoIcon;

    @FXML
    private Label todoLabel;

    @FXML
    private FontAwesomeIcon goalsIcon;

    @FXML
    private Label goalsLabel;

    @FXML
    private FontAwesomeIcon diaryIcon;

    @FXML
    private Label diaryLabel;

    @FXML
    private FontAwesomeIcon notesIcon;

    @FXML
    private Label notesLabel;

    @FXML
    private JFXListView tagList;

    private ChiclePadScene selectedScene;

    private String filter = "";

    @FXML
    public void initialize() {
        switchToTodoScene();

        FontAwesomeIconName[] icons = FontAwesomeIconName.values();

        for (int i = 0; i < 4; i++) {
            Label line = new Label("test");
            FontAwesomeIcon icon = new FontAwesomeIcon();
            icon.setIcon(icons[(int) (Math.random() * icons.length)]);
            line.setGraphic(icon);

            tagList.getItems().add(line);
            //        FontAwesomeIconName.TROPHY
        }

        tagList.setExpanded(true);
    }


    @FXML
    public void refreshFilter() {
        filter = searchTextField.getText();
        // TODO reload
    }

    @FXML
    public void switchToTodoScene() {
        switchScene(ChiclePadScene.TODO, todoIcon);
    }

    @FXML
    public void switchToGoalScene() {
        switchScene(ChiclePadScene.GOAL, goalsIcon);
    }

    @FXML
    public void switchToDiaryScene() {
        switchScene(ChiclePadScene.DIARY, diaryIcon);
    }

    @FXML
    public void switchToNoteScene() {
        switchScene(ChiclePadScene.NOTE, notesIcon);
    }

    private void switchScene(ChiclePadScene newScene, FontAwesomeIcon icon) {
        if (selectedScene == newScene) {
            return;
        }

        selectedScene = newScene;

        goalsIcon.setFill(ChiclePadColor.GREY_TEXT);
        diaryIcon.setFill(ChiclePadColor.GREY_TEXT);
        notesIcon.setFill(ChiclePadColor.GREY_TEXT);
        todoIcon.setFill(ChiclePadColor.GREY_TEXT);

        icon.setFill(ChiclePadColor.PRIMARY);
    }

}
