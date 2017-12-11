package org.chiclepad.frontend.jfx.model;

import com.jfoenix.controls.JFXMasonryPane;
import com.jfoenix.effects.JFXDepthManager;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.chiclepad.backend.entity.Note;
import org.chiclepad.frontend.jfx.ChiclePadColor;

public class NoteListModel {

    private JFXMasonryPane layout;

    public NoteListModel(JFXMasonryPane layout) {
        this.layout = layout;
    }

    public void add(Note note) {
        StackPane postIt = new StackPane();

        JFXDepthManager.setDepth(postIt, 1);
        postIt.getStyleClass().addAll("form");

//        StackPane header = createNoteHeader(note);

        StackPane body = new StackPane();
//        body.setMinHeight(Math.random() * 20 + 50);
        Label bodyText = new Label(note.getContent());
        bodyText.setPadding(new Insets(8, 8, 8, 8));
        bodyText.getStyleClass().addAll("normal-text");
        body.getChildren().add(bodyText);
        VBox content = new VBox();
//        content.getChildren().addAll(header, body);
        content.getChildren().add(body);
        body.setStyle("-fx-background-radius: 0 0 2 2; -fx-background-color: rgb(255,255,255,0.87);");

        postIt.getChildren().add(content);
        layout.getChildren().add(postIt);
    }

    private StackPane createNoteHeader(Note note) {
        StackPane header = new StackPane();
        header.setPadding(new Insets(8, 8, 8, 8));
        header.setStyle("-fx-background-color: " + categoryColorOfNote(note) + ";");


        Label text = new Label(categoryNameOfNote(note));
        text.getStyleClass().add("normal-text");
        header.getChildren().add(text);

        return header;
    }

    private String categoryNameOfNote(Note note) {
        if (!note.getCategories().isEmpty()) {
            return note.getCategories().get(0).getName();
        } else {
            return "Category";
        }
    }

    private String categoryColorOfNote(Note note) {
        if (!note.getCategories().isEmpty()) {
            return note.getCategories().get(0).getColor();
        } else {
            return ChiclePadColor.toHex(ChiclePadColor.CATEGORY_DEFAULT);
        }
    }

}
