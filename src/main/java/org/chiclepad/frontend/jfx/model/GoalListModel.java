package org.chiclepad.frontend.jfx.model;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.effects.JFXDepthManager;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconName;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.chiclepad.backend.Dao.DaoFactory;
import org.chiclepad.backend.Dao.GoalDao;
import org.chiclepad.backend.entity.Goal;
import org.chiclepad.constants.ChiclePadColor;

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
        HBox goalLine = new HBox();
        setGoalLineStyle(goalLine);
        goalLine.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            if (selectedGoalLine != null) {
                selectedGoalLine.getStyleClass().removeIf(styleClass -> styleClass.equals("bordered"));
                JFXDepthManager.setDepth(selectedGoalLine, 1);
            }

            selectedGoalLine = goalLine;
            selectedGoal = goal;

            selectedGoalLine.getStyleClass().add("bordered");
            JFXDepthManager.setDepth(selectedGoalLine, 2);
        });

        FontAwesomeIcon checkMark = new FontAwesomeIcon();
        setCheckMarkStyle(checkMark);
        setCheckMarkOnPressed(goal, checkMark);

        JFXTextField descriptionField = new JFXTextField(goal.getDescription());
        setDescriptionFieldStyle(descriptionField);

        descriptionField.textProperty().addListener((observable, oldValue, newValue) -> {
            goal.setDescription(newValue);
        });

        descriptionField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                return;
            }

            goalDao.update(goal);
        });

        goalLine.getChildren().addAll(checkMark, descriptionField);
        layout.getChildren().add(goalLine);
    }

    private void setGoalLineStyle(HBox goalLine) {
        JFXDepthManager.setDepth(goalLine, 1);
        goalLine.getStyleClass().add("form");

        goalLine.setAlignment(Pos.CENTER_LEFT);

        goalLine.setPadding(new Insets(10, 35, 10, 20));
        goalLine.setSpacing(10);
    }

    private void setCheckMarkStyle(FontAwesomeIcon checkMark) {
        checkMark.setIcon(FontAwesomeIconName.CHECK);
        checkMark.setSize("1.65em");

        checkMark.setOnMouseEntered(event -> checkMark.setFill(ChiclePadColor.PRIMARY));
        checkMark.setOnMouseExited(event -> checkMark.setFill(ChiclePadColor.BLACK));
    }

    private void setCheckMarkOnPressed(Goal goal, FontAwesomeIcon checkMark) {
        checkMark.setOnMousePressed(event -> {
            goalDao.createCompletedGoal(goal);
            deleteSelected();
        });
    }

    private void setDescriptionFieldStyle(JFXTextField descriptionField) {
        descriptionField.setFocusColor(ChiclePadColor.PRIMARY);
        descriptionField.setUnFocusColor(ChiclePadColor.GREY_TEXT);

        descriptionField.getStyleClass().add("normal-text");
        descriptionField.setPromptText("Description");

        HBox.setHgrow(descriptionField, Priority.ALWAYS);
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
