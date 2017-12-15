package org.chiclepad.frontend.jfx.model;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXMasonryPane;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import com.jfoenix.effects.JFXDepthManager;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.chiclepad.backend.entity.Note;
import org.chiclepad.frontend.jfx.ChiclePadColor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class NoteListModel {

    private List<Note> notes;

    private JFXMasonryPane layout;

    private VBox selectedPostIt;

    private Note selectedNote;

    private JFXTextField descriptionField;

    private JFXDatePicker reminderDate;

    private JFXTimePicker reminderTime;

    public NoteListModel(
            JFXMasonryPane layout,
            JFXTextField descriptionField,
            JFXDatePicker reminderDate,
            JFXTimePicker reminderTime
    ) {
        this.notes = new ArrayList<>();
        this.layout = layout;
        this.descriptionField = descriptionField;
        this.reminderDate = reminderDate;
        this.reminderTime = reminderTime;
    }

    public void add(Note addedNote) {
        notes.add(addedNote);

        VBox postIt = new VBox();
        JFXDepthManager.setDepth(postIt, 1);
        postIt.setAlignment(Pos.CENTER_RIGHT);
        postIt.getStyleClass().addAll("form");
        postIt.setStyle("-fx-background-color: " + categoryColorOfNote(addedNote));
        postIt.setPadding(new Insets(15, 15, 15, 15));
        postIt.setMinSize(110, 65);
        postIt.setPrefSize(115, 70);
        postIt.setMaxSize(120, 75);

        postIt.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            if (selectedPostIt != null) {
                selectedPostIt.getStyleClass().removeIf(cssClass -> cssClass.equals("bordered"));
            }

            setSelectedPostIt(postIt);
            setSelectedNote(addedNote);
            selectedPostIt.getStyleClass().add("bordered");

            descriptionField.setText(addedNote.getContent());
            reminderDate.setValue(LocalDate.now());
            reminderTime.setValue(LocalTime.now());
        });

        Label bodyText = new Label(addedNote.getContent());
        bodyText.setWrapText(true);
        bodyText.setAlignment(Pos.CENTER_LEFT);
        VBox.setVgrow(bodyText, Priority.ALWAYS);
        bodyText.getStyleClass().addAll("normal-text");

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.setSpacing(10);

        FontAwesomeIcon edit = new FontAwesomeIcon();
        FontAwesomeIcon delete = new FontAwesomeIcon();

        hBox.getChildren().addAll(edit, delete);

        postIt.getChildren().addAll(bodyText, hBox);

        layout.getChildren().add(postIt);
    }

    public Note deleteSelected() {
        layout.getChildren().removeIf(child -> child == selectedPostIt);
        notes.remove(selectedNote);
        return selectedNote;
    }

    public void setNewFilter(String filter) {
        layout.getChildren().clear();
        notes.stream()
                .filter(note -> fitsFilter(note, filter))
                .forEach(this::add);
    }

    private boolean fitsFilter(Note note, String filter) {
        return note.getContent().contains(filter) ||
                note.getReminderTime().map(time -> time.toString().contains(filter)).orElse(false);
    }

    private void setSelectedPostIt(VBox selectedPostIt) {
        this.selectedPostIt = selectedPostIt;
    }

    private void setSelectedNote(Note selectedNote) {
        this.selectedNote = selectedNote;
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

    private String categoryIconOfNote(Note note) {
        if (!note.getCategories().isEmpty()) {
            return note.getCategories().get(0).getIcon();
        } else {
            return "CIRCLE";
        }
    }

}
