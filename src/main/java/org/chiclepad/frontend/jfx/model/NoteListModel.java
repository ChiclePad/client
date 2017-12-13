package org.chiclepad.frontend.jfx.model;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXMasonryPane;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import com.jfoenix.effects.JFXDepthManager;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.chiclepad.backend.entity.Note;
import org.chiclepad.frontend.jfx.ChiclePadColor;

import java.time.LocalDate;
import java.time.LocalTime;

public class NoteListModel {

    private VBox selectedPostIt;

    private JFXMasonryPane layout;

    private JFXTextField descriptionField;

    private JFXDatePicker reminderDate;

    private JFXTimePicker reminderTime;

    public NoteListModel(
            JFXMasonryPane layout,
            JFXTextField descriptionField,
            JFXDatePicker reminderDate,
            JFXTimePicker reminderTime
    ) {
        this.layout = layout;
        this.descriptionField = descriptionField;
        this.reminderDate = reminderDate;
        this.reminderTime = reminderTime;
    }

    public void add(Note note) {
        VBox postIt = new VBox();
        postIt.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            setSelectedPostIt(postIt);
            descriptionField.setText(note.getContent());
            reminderDate.setValue(LocalDate.now());
            reminderTime.setValue(LocalTime.now());
        });

        setPostItStyle(note, postIt);

        Label bodyText = new Label(note.getContent());
        bodyText.setMinSize(30, 30);
        bodyText.setPrefSize(30, 30);
        bodyText.setMaxSize(100, 100);
        bodyText.getStyleClass().addAll("normal-text");

        Pane divider = new Pane();
        divider.minHeight(1.5);
        divider.prefHeight(1.5);
        divider.maxHeight(1.5);
        divider.setPrefWidth(100);
        divider.getStyleClass().add("grey-dark-background");

        HBox category = new HBox();
        FontAwesomeIcon icon = new FontAwesomeIcon();
        icon.setIconName(categoryIconOfNote(note));

        Label name = new Label(categoryNameOfNote(note));
        name.getStyleClass().addAll("small-text");

        category.getChildren().addAll(icon, name);

        postIt.getChildren().addAll(bodyText, divider, category);

        layout.getChildren().add(postIt);
    }

    private void setPostItStyle(Note note, VBox postIt) {
        JFXDepthManager.setDepth(postIt, 1);
        postIt.getStyleClass().addAll("form");
        postIt.setStyle("-fx-background-color: " + categoryColorOfNote(note));
        postIt.setPadding(new Insets(10, 10, 10, 10));
    }

    private String categoryIconOfNote(Note note) {
        if (!note.getCategories().isEmpty()) {
            return note.getCategories().get(0).getIcon();
        } else {
            return "CIRCLE";
        }
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

    public void deleteSelected() {
        layout.getChildren().removeIf(child -> child == selectedPostIt);
    }

    public void setNewFilter(String filter) {

    }

    private void setSelectedPostIt(VBox selectedPostIt) {
        this.selectedPostIt = selectedPostIt;
    }

}
