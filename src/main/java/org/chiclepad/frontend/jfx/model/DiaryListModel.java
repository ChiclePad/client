package org.chiclepad.frontend.jfx.model;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.effects.JFXDepthManager;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconName;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.chiclepad.backend.Dao.DaoFactory;
import org.chiclepad.backend.Dao.DiaryPageDao;
import org.chiclepad.backend.entity.Category;
import org.chiclepad.backend.entity.DiaryPage;
import org.chiclepad.constants.ChiclePadColor;
import org.chiclepad.frontend.jfx.ChiclePadApp;

import java.util.ArrayList;
import java.util.List;

public class DiaryListModel implements ListModel {

    private List<DiaryPage> diaryPages;

    private VBox layout;

    private VBox selectedDiaryPageLine;

    private DiaryPage selectedDiaryPage;

    private DiaryPageDao diaryPageDao;

    private String textFilter = "";
    private List<Category> categoriesFilter = new ArrayList<>();

    private boolean clearedScene;

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
        styleDiaryPageLine(diaryPageLine);
        setHighlightOnHover(diaryPageLine, diaryPage);
        diaryPageLine.addEventFilter(MouseEvent.MOUSE_PRESSED, this.selectDiaryPage(diaryPageLine, diaryPage));

        HBox headline = new HBox();
        Label dateLabel = createDateLabel(diaryPage);

        FontAwesomeIcon dropdownIcon = new FontAwesomeIcon();
        dropdownIcon.setIcon(FontAwesomeIconName.CARET_DOWN);
        dropdownIcon.setSize("1.75em");

        dropdownIcon.setOnMousePressed(event -> {
            if (isExpanded(diaryPageLine)) {
                diaryPageLine.getChildren().remove(1);

            } else {

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

        layout.getChildren().add(diaryPageLine);
        diaryPageLine.setStyle("-fx-background-color: " + categoryColorOfDiaryPage(diaryPage));
    }

    private boolean isExpanded(VBox diaryPageLine) {
        return diaryPageLine.getChildren().size() > 1;
        this.clearedScene = true;
    }

    private void styleDiaryPageLine(VBox diaryPageLine) {
        diaryPageLine.getStyleClass().add("form");
        diaryPageLine.setPadding(new Insets(15, 15, 15, 15));
        JFXDepthManager.setDepth(diaryPageLine, 1);
    public void setNewTextFilter(String textFilter) {
        this.textFilter = textFilter;
        this.filter();
    }

    private boolean fitsTextFilter(DiaryPage diaryPage) {
        return diaryPage.getText().contains(this.textFilter) ||
                diaryPage.getRecordedDay().toString().contains(this.textFilter);
    }

    private void filter() {
        if (clearedScene) {
            diaryPages.stream()
                    .filter(diaryPage -> fitsTextFilter(diaryPage))
                    .filter(diaryPage -> fitsCategoryFilter(diaryPage, this.categoriesFilter))
                    .forEach(diaryPage -> addDiaryPageToLayout(diaryPage));
            this.clearedScene = false;
        }
    }

    private Label createDateLabel(DiaryPage diaryPage) {
        Label dateLabel = new Label(diaryPage.getRecordedDay().toString());
        dateLabel.getStyleClass().add("normal-text");
        return dateLabel;
    @Override
    public void filterByCategory(List<Category> categories) {
        this.categoriesFilter = categories;
        this.filter();
    }

    private EventHandler<MouseEvent> selectDiaryPage(VBox selectedDiaryPageLine, DiaryPage selectedDiaryPage) {
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

    public void clearDiaryPages() {
        layout.getChildren().clear();
    }

    public DiaryPage deleteSelected() {
        layout.getChildren().removeIf(child -> child == selectedDiaryPageLine);
        diaryPages.remove(selectedDiaryPage);
        return selectedDiaryPage;
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

    @Override
    public void setCategoryToSelectedEntry(Category category) {
        this.selectedDiaryPage.getCategories().forEach(unboundCategory -> {
            this.diaryPageDao.unbind(unboundCategory, this.selectedDiaryPage);
        });
        this.selectedDiaryPage.getCategories().clear();
        this.selectedDiaryPage.getCategories().add(category);
        this.diaryPageDao.bind(category, this.selectedDiaryPage);
        this.selectedDiaryPageLine.setStyle("-fx-background-color: " + categoryColorOfDiaryPage(this.selectedDiaryPage));
    }

    @Override
    public void clearEntries() {
        this.clearDiaryPages();
    }

}
