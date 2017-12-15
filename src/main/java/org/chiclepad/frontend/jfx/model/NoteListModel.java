package org.chiclepad.frontend.jfx.model;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXMasonryPane;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import com.jfoenix.effects.JFXDepthManager;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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
        addNoteToLayout(addedNote);
    }

    private void addNoteToLayout(Note addedNote) {
        VBox postIt = new VBox();
        stylePostIt(addedNote, postIt);

        postIt.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            deselectPreviousPostIt();
            selectNewPostIt(addedNote, postIt);

            descriptionField.setText(addedNote.getContent());
            descriptionField.textProperty().addListener(((observable, oldValue, newValue) -> {

            }));

            descriptionField.focusedProperty().addListener((observable, oldValue, newValue) -> {

            });

            reminderDate.setValue(LocalDate.now());
            reminderDate.valueProperty().addListener((observable, oldValue, newValue) -> {

            });
            reminderDate.focusedProperty().addListener((observable, oldValue, newValue) -> {

            });

            reminderTime.setValue(LocalTime.now());
            reminderTime.valueProperty().addListener((observable, oldValue, newValue) -> {

            });
            reminderTime.focusedProperty().addListener((observable, oldValue, newValue) -> {

            });
        });

        Label bodyText = new Label(addedNote.getContent());
        styleText(bodyText);

        postIt.getChildren().add(bodyText);
        layout.getChildren().add(postIt);
    }

    private void selectNewPostIt(Note addedNote, VBox postIt) {
        setSelectedPostIt(postIt);
        setSelectedNote(addedNote);
        selectedPostIt.getStyleClass().add("bordered");
        JFXDepthManager.setDepth(selectedPostIt, 2);
    }

    private void deselectPreviousPostIt() {
        if (selectedPostIt != null) {
            selectedPostIt.getStyleClass().removeIf(cssClass -> cssClass.equals("bordered"));
            JFXDepthManager.setDepth(selectedPostIt, 1);
        }
    }

    private void styleText(Label bodyText) {
        bodyText.setWrapText(true);
        bodyText.setAlignment(Pos.CENTER_LEFT);
        VBox.setVgrow(bodyText, Priority.ALWAYS);
        bodyText.getStyleClass().addAll("normal-text");
    }

    private void stylePostIt(Note addedNote, VBox postIt) {
        JFXDepthManager.setDepth(postIt, 1);
        postIt.setAlignment(Pos.CENTER_RIGHT);
        postIt.getStyleClass().addAll("form");
        postIt.setStyle("-fx-background-color: " + categoryColorOfNote(addedNote));
        postIt.setPadding(new Insets(15, 15, 15, 15));
        postIt.setMinSize(110, 65);
        postIt.setPrefSize(115, 70);
        postIt.setMaxSize(120, 75);

        setHighlightOnHover(addedNote, postIt);
    }

    private void setHighlightOnHover(Note addedNote, VBox postIt) {
        postIt.setOnMouseEntered(event -> {
            System.out.println(darken(categoryColorOfNote(addedNote), 0.9));
            postIt.setStyle("-fx-background-color: " + darken(categoryColorOfNote(addedNote), 0.9));
        });

        postIt.setOnMouseExited(event -> {
            postIt.setStyle("-fx-background-color: " + categoryColorOfNote(addedNote));
        });
    }

    private String darken(String hexColor, double amount) {
        Color color = Color.web(hexColor);

        double r = color.getRed();
        double b = color.getBlue();
        double g = color.getGreen();

        return String.format("#%02x%02x%02x", (int) (r * amount), (int) (g * amount), (int) (b * amount));
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
                .forEach(note -> Platform.runLater(() -> addNoteToLayout(note)));
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
