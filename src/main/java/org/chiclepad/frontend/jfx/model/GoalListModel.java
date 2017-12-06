package org.chiclepad.frontend.jfx.model;

import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.chiclepad.backend.entity.Goal;

public class GoalListModel {

    private JFXListView<Goal> goalList;

    private ObservableList<Goal> goals;

    public GoalListModel(JFXListView<Goal> goalList) {
        this.goalList = goalList;
        goals = FXCollections.observableArrayList();
        this.goalList.setItems(goals);
    }

    public void add() {

    }

    public void add(Goal goalPage) {
        goals.add(goalPage);
    }


}
