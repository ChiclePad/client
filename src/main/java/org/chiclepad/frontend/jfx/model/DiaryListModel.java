package org.chiclepad.frontend.jfx.model;

import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.chiclepad.backend.entity.DiaryPage;

public class DiaryListModel {

    private JFXListView<DiaryPage> diaryList;

    private ObservableList<DiaryPage> diaryPages;

    public DiaryListModel(JFXListView<DiaryPage> diaryList) {
        this.diaryList = diaryList;
        diaryPages = FXCollections.observableArrayList();
        this.diaryList.setItems(diaryPages);
    }

    public void add() {

    }

    public void add(DiaryPage diaryPage) {
        diaryPages.add(diaryPage);
    }

}
