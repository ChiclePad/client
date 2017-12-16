package org.chiclepad.frontend.jfx.model;

import javafx.scene.layout.VBox;
import org.chiclepad.backend.entity.Goal;

import java.util.ArrayList;
import java.util.List;

public class GoalListModel {

    private List<Goal> goals;

    private VBox layout;

    private String filter;

    public GoalListModel(VBox layout) {
        this.goals = new ArrayList<>();
        this.layout = layout;
    }

    public void add(Goal goalPage) {
        goals.add(goalPage);
    }


    public void setNewFilter(String filter) {
        this.filter = filter;
    }

    public Goal deleteSelected() {
        return null;
    }

}
