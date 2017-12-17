package org.chiclepad.frontend.jfx.model;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.effects.JFXDepthManager;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconName;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.chiclepad.backend.Dao.DaoFactory;
import org.chiclepad.backend.Dao.GoalDao;
import org.chiclepad.backend.entity.Goal;
import org.chiclepad.frontend.jfx.ChiclePadColor;

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
        diaryPageLine.getStyleClass().add("form");
        diaryPageLine.setPadding(new Insets(10, 35, 10, 20));
        diaryPageLine.setAlignment(Pos.CENTER_LEFT);
        diaryPageLine.setSpacing(10);

        FontAwesomeIcon checkMark = new FontAwesomeIcon();
        checkMark.setIcon(FontAwesomeIconName.CHECK);
        checkMark.setSize("1.65em");

        JFXTextField text = new JFXTextField(goal.getDescription());
        text.getStyleClass().add("normal-text");
        text.setPromptText("Description");
        text.setFocusColor(ChiclePadColor.PRIMARY);
        text.setUnFocusColor(ChiclePadColor.GREY_TEXT);
        HBox.setHgrow(text, Priority.ALWAYS);
        text.textProperty().addListener((observable, oldValue, newValue) -> {
            goal.setDescription(newValue);
        });

        text.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                return;
            }

            goalDao.update(goal);
        });

        diaryPageLine.getChildren().addAll(checkMark, text);
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
