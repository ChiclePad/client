package org.chiclepad.frontend.jfx.model;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.effects.JFXDepthManager;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconName;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.chiclepad.backend.Dao.DaoFactory;
import org.chiclepad.backend.Dao.DiaryPageDao;
import org.chiclepad.backend.entity.DiaryPage;
import org.chiclepad.constants.ChiclePadColor;
import org.chiclepad.frontend.jfx.ChiclePadApp;

import java.util.ArrayList;
import java.util.List;

public class DiaryListModel {

    private List<DiaryPage> diaryPages;

    private VBox layout;

    private VBox selectedDiaryPageLine;

    private DiaryPage selectedDiaryPage;

    private DiaryPageDao diaryPageDao;

    private String filter = "";

    public DiaryListModel(VBox layout) {
        this.layout = layout;
        this.diaryPageDao = DaoFactory.INSTANCE.getDiaryPageDao();
        this.diaryPages = new ArrayList<>();
    }

    public void add(DiaryPage diaryPage) {
        diaryPages.add(diaryPage);
        addDiaryPageToLayout(diaryPage);
    }

    private void addDiaryPageToLayout(DiaryPage diaryPage) {
        VBox diaryPageLine = new VBox();
        diaryPageLine.addEventFilter(MouseEvent.MOUSE_PRESSED, this.selectDiaryPage(diaryPageLine, diaryPage));

        // Hbox containing date and button to display textarea
        HBox headline = new HBox();
        Label dateLabel = new Label(diaryPage.getRecordedDay().toString());
        FontAwesomeIcon dropdownIcon = new FontAwesomeIcon();
        dropdownIcon.setIcon(FontAwesomeIconName.CARET_DOWN);
        diaryPageLine.getStyleClass().add("form");
        diaryPageLine.setPadding(new Insets(15, 15, 15, 15));
        JFXDepthManager.setDepth(diaryPageLine, 1);

        dropdownIcon.setOnMousePressed(event -> {
            if (diaryPageLine.getChildren().size() == 2) {
                // Remove textarea
                diaryPageLine.getChildren().remove(1);
            } else if (diaryPageLine.getChildren().size() == 1) {
                // Add textarea

                // TextArea
                JFXTextArea text = new JFXTextArea(diaryPage.getText());
                text.textProperty().addListener(((observable, oldValue, newValue) -> {
                    diaryPage.setText(newValue);
                }));

                text.focusedProperty().addListener(((observable, oldValue, newValue) -> {
                    if (newValue) {
                        return;
                    }
                    this.diaryPageDao.update(diaryPage);
                }));

                text.getStyleClass().add("small-normal-text");
                text.setFocusColor(ChiclePadColor.PRIMARY);
                text.setUnFocusColor(ChiclePadColor.GREY_TEXT);
                if (text.getText().length() < 100) {
                    text.setPrefRowCount(2);
                } else if (text.getText().length() > 100) {
                    text.setPrefRowCount(5);
                }

                HBox.setHgrow(text, Priority.ALWAYS);
                diaryPageLine.getChildren().add(text);
            }
        });

        headline.getChildren().addAll(dateLabel, dropdownIcon);
        headline.setSpacing(10);
        diaryPageLine.getChildren().add(headline);
        setHighlightOnHover(diaryPageLine, diaryPage);
        layout.getChildren().add(diaryPageLine);
    }

    public void clearDiaryPages() {
        layout.getChildren().clear();
    }

    public void setNewFilter(String filter) {
        this.filter = filter;

        diaryPages.stream()
                .filter(diaryPage -> fitsFilter(diaryPage, filter))
                .forEach(this::addDiaryPageToLayout);
    }

    private boolean fitsFilter(DiaryPage diaryPage, String filter) {
        return diaryPage.getText().contains(filter) ||
                diaryPage.getRecordedDay().toString().contains(filter);
    }

    public DiaryPage deleteSelected() {
        layout.getChildren().removeIf(child -> child == selectedDiaryPageLine);
        diaryPages.remove(selectedDiaryPage);
        return selectedDiaryPage;
    }

    public EventHandler<MouseEvent> selectDiaryPage(VBox selectedDiaryPageLine, DiaryPage selectedDiaryPage) {
        return event -> {
            this.deselectPreviousDiaryPage();
            setSelectedDiaryPage(selectedDiaryPageLine, selectedDiaryPage);
            JFXDepthManager.setDepth(selectedDiaryPageLine, 2);
            this.selectedDiaryPageLine.getStyleClass().add("bordered");
        };
    }

    private void setSelectedDiaryPage(VBox selectedDiaryPageLine, DiaryPage selectedDiaryPage) {
        this.selectedDiaryPageLine = selectedDiaryPageLine;
        this.selectedDiaryPage = selectedDiaryPage;
    }

    private void setHighlightOnHover(VBox diaryPageLine, DiaryPage diaryPage) {
        diaryPageLine.setOnMouseEntered(event -> {
            diaryPageLine.setStyle("-fx-background-color: " + ChiclePadApp.darken(categoryColorOfDiaryPage(diaryPage), 0.95));
        });

        diaryPageLine.setOnMouseExited(event -> {
            diaryPageLine.setStyle("-fx-background-color: " + categoryColorOfDiaryPage(diaryPage));
        });
    }

    private String categoryColorOfDiaryPage(DiaryPage diaryPage) {
        if (!diaryPage.getCategories().isEmpty()) {
            return diaryPage.getCategories().get(0).getColor();
        } else {
            return ChiclePadColor.toHex(ChiclePadColor.WHITE);
        }
    }

    private void deselectPreviousDiaryPage() {
        if (selectedDiaryPageLine == null || selectedDiaryPage == null) {
            return;
        }
        selectedDiaryPageLine.getStyleClass().removeIf(cssClass -> cssClass.equals("bordered"));
        JFXDepthManager.setDepth(selectedDiaryPageLine, 1);
    }

}
