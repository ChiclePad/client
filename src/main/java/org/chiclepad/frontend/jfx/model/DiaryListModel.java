package org.chiclepad.frontend.jfx.model;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.effects.JFXDepthManager;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.chiclepad.backend.Dao.DaoFactory;
import org.chiclepad.backend.Dao.DiaryPageDao;
import org.chiclepad.backend.entity.DiaryPage;
import org.chiclepad.constants.ChiclePadColor;

import java.util.ArrayList;
import java.util.List;

public class DiaryListModel {

    private List<DiaryPage> diaryPages;

    private VBox layout;

    private HBox selectedDiaryPageLine;

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
        HBox diaryPageLine = new HBox();
        diaryPageLine.getStyleClass().add("form");
        diaryPageLine.setPadding(new Insets(15, 15, 15, 15));
        JFXDepthManager.setDepth(diaryPageLine, 1);

        JFXTextArea text = new JFXTextArea(diaryPage.getText());
        text.getStyleClass().add("small-normal-text");
        text.setFocusColor(ChiclePadColor.PRIMARY);
        text.setUnFocusColor(ChiclePadColor.GREY_TEXT);
        text.setPrefRowCount(2);
        HBox.setHgrow(text, Priority.ALWAYS);

        diaryPageLine.getChildren().add(text);
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

}
