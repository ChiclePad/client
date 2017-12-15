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
import org.chiclepad.backend.Dao.DaoFactory;
import org.chiclepad.backend.Dao.NoteDao;
import org.chiclepad.backend.entity.Note;
import org.chiclepad.frontend.jfx.ChiclePadColor;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    private NoteDao noteDao;

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
        this.noteDao = DaoFactory.INSTANCE.getNoteDao();
    }

    public void add(Note addedNote) {
        notes.add(addedNote);
        addNoteToLayout(addedNote);
    }

    private void addNoteToLayout(Note addedNote) {
        VBox postIt = new VBox();
        stylePostIt(addedNote, postIt);

        postIt.addEventFilter(MouseEvent.MOUSE_PRESSED, selectNote(addedNote, postIt));

        Label bodyText = new Label(addedNote.getContent());
        styleText(bodyText);

        postIt.getChildren().add(bodyText);

        layout.getChildren().add(postIt);
    }

    private EventHandler<MouseEvent> selectNote(Note selectedNote, VBox postIt) {
        return event -> {
            deselectPreviousPostIt();
            selectNewPostIt(selectedNote, postIt);

            setDescriptionTextField();
            setReminderTimeFields();
        };
    }

    private void deselectPreviousPostIt() {
        if (selectedPostIt == null) {
            return;
        }

        noteDao.update(selectedNote);

        selectedPostIt.getStyleClass().removeIf(cssClass -> cssClass.equals("bordered"));
        JFXDepthManager.setDepth(selectedPostIt, 1);
    }

    private void selectNewPostIt(Note addedNote, VBox postIt) {
        this.selectedPostIt = postIt;
        this.selectedNote = addedNote;

        setSelectedStyles();
    }

    private void setSelectedStyles() {
        selectedPostIt.getStyleClass().add("bordered");
        JFXDepthManager.setDepth(selectedPostIt, 2);
    }

    private void setDescriptionTextField() {
        descriptionField.setText(selectedNote.getContent());
        descriptionField.textProperty().addListener(((observable, oldValue, newValue) -> {
            selectedNote.setContent(newValue);

            Label label = (Label) selectedPostIt.getChildren().get(0);
            label.setText(newValue);
        }));

        descriptionField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            noteDao.update(this.selectedNote);
        });
    }

    private void setReminderTimeFields() {
        selectedNote.getReminderTime().ifPresent(time -> {
            reminderDate.setValue(time.toLocalDate());
            reminderTime.setValue(time.toLocalTime());
        });

        setReminderDateField();
        setReminderTimeField();
    }

    private void setReminderDateField() {
        reminderDate.valueProperty().addListener((observable, oldValue, newValue) -> {
            LocalTime localTime = selectedNote.getReminderTime()
                    .map(LocalDateTime::toLocalTime)
                    .orElse(LocalTime.now());

            LocalDateTime newReminderDate = LocalDateTime.of(newValue, localTime);
            selectedNote.setReminderTime(newReminderDate);
        });

        reminderDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            noteDao.update(selectedNote);
        });
    }

    private void setReminderTimeField() {
        reminderTime.valueProperty().addListener((observable, oldValue, newValue) -> {
            LocalDate localDate = selectedNote.getReminderTime()
                    .map(LocalDateTime::toLocalDate)
                    .orElse(LocalDate.now());

            LocalDateTime newReminderTime = LocalDateTime.of(localDate, newValue);
            selectedNote.setReminderTime(newReminderTime);
        });

        reminderTime.focusedProperty().addListener((observable, oldValue, newValue) -> {
            noteDao.update(selectedNote);
        });
    }

    private void styleText(Label bodyText) {
        bodyText.setWrapText(true);
        VBox.setVgrow(bodyText, Priority.ALWAYS);
        bodyText.getStyleClass().addAll("normal-text");
    }

    private void stylePostIt(Note addedNote, VBox postIt) {
        JFXDepthManager.setDepth(postIt, 1);
        postIt.setMinSize(113, 70);
        postIt.setPrefSize(113, 70);
        postIt.setMaxSize(113, 70);

        postIt.setPadding(new Insets(15, 15, 15, 15));

        postIt.getStyleClass().addAll("form");
        postIt.setStyle("-fx-background-color: " + categoryColorOfNote(addedNote));
        postIt.setAlignment(Pos.CENTER_LEFT);

        setHighlightOnHover(addedNote, postIt);
    }

    private void setHighlightOnHover(Note addedNote, VBox postIt) {
        postIt.setOnMouseEntered(event -> {
            postIt.setStyle("-fx-background-color: " + darken(categoryColorOfNote(addedNote), 0.95));
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

        return String.format("#%02x%02x%02x", (int) (r * amount * 255), (int) (g * amount * 255), (int) (b * amount * 255));
    }

    public Note deleteSelected() {
        layout.getChildren().removeIf(child -> child == selectedPostIt);
        notes.remove(selectedNote);
        return selectedNote;
    }

    public synchronized void setNewFilter(String filter) {
        layout.getChildren().clear();
        notes.stream()
                .filter(note -> fitsFilter(note, filter))
                .forEach(note -> Platform.runLater(() -> addNoteToLayout(note)));
    }

    private boolean fitsFilter(Note note, String filter) {
        return note.getContent().contains(filter) ||
                note.getReminderTime().map(time -> time.toString().contains(filter)).orElse(false);
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
