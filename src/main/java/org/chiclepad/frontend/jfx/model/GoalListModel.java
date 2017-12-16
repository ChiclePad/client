package org.chiclepad.frontend.jfx.model;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.effects.JFXDepthManager;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.chiclepad.backend.Dao.DaoFactory;
import org.chiclepad.backend.Dao.GoalDao;
import org.chiclepad.backend.entity.Goal;

import java.util.ArrayList;
import java.util.List;

public class GoalListModel {

    private List<Goal> goals;

    private VBox layout;

    private HBox selectedGoalLine;

    private Goal selectedGoal;

    private GoalDao goalDao;

    private String filter = "";

    public GoalListModel(VBox layout) {
        this.layout = layout;
        this.goals = new ArrayList<>();
        this.goalDao = DaoFactory.INSTANCE.getGoalDao();
    }

    public void add(Goal goal) {
        goals.add(goal);
        addGoalToLayout(goal);
    }

    private void addGoalToLayout(Goal goal) {
        HBox diaryPageLine = new HBox();
        JFXDepthManager.setDepth(diaryPageLine, 1);

        JFXTextField text = new JFXTextField(goal.getDescription());

        diaryPageLine.getChildren().add(text);
        layout.getChildren().add(diaryPageLine);
    }

    public void clearGoals() {
        layout.getChildren().clear();
    }

    public void setNewFilter(String filter) {
        this.filter = filter;

        goals.stream()
                .filter(goal -> fitsFilter(goal, filter))
                .forEach(this::addGoalToLayout);
    }

    private boolean fitsFilter(Goal goal, String filter) {
        return goal.getDescription().contains(filter);
    }

    public Goal deleteSelected() {
        layout.getChildren().removeIf(child -> child == selectedGoalLine);
        goals.remove(selectedGoal);
        return selectedGoal;
    }

}
