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
import org.chiclepad.backend.entity.Category;
import org.chiclepad.backend.entity.Goal;
import org.chiclepad.constants.ChiclePadColor;
import org.chiclepad.frontend.jfx.ChiclePadApp;

import java.util.ArrayList;
import java.util.List;

public class GoalListModel implements ListModel {

    private List<Goal> goals;

    private VBox layout;

    private HBox selectedGoalLine;

    private Goal selectedGoal;

    private GoalDao goalDao;

    private String textFilter = "";

    private List<Category> categoriesFilter = new ArrayList<>();

    private boolean clearedScene;

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
        setGoalLineStyle(goalLine, goal);
        setSelectionListener(goal, goalLine);
        setHighlightOnHover(goal, goalLine);

        FontAwesomeIcon checkMark = new FontAwesomeIcon();
        setCheckMarkStyle(checkMark);
        setCheckMarkOnPressed(goal, checkMark);

        JFXTextField descriptionField = new JFXTextField(goal.getDescription());
        setDescriptionFieldStyle(descriptionField);
        setDescriptionChangeListener(goal, descriptionField);

        goalLine.getChildren().addAll(checkMark, descriptionField);
        layout.getChildren().add(goalLine);
    }

    private void setHighlightOnHover(Goal addedGoal, HBox goalLine) {
        goalLine.setOnMouseEntered(event -> {
            goalLine.setStyle("-fx-background-color: " + ChiclePadApp.darken(categoryColorOfGoal(addedGoal)));
        });

        goalLine.setOnMouseExited(event -> {
            goalLine.setStyle("-fx-background-color: " + categoryColorOfGoal(addedGoal));
        });
    }

    private void setDescriptionChangeListener(Goal goal, JFXTextField descriptionField) {
        descriptionField.textProperty().addListener((observable, oldValue, newValue) -> {
            goal.setDescription(newValue);
        });

        descriptionField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                return;
            }

            goalDao.update(goal);
        });
    }

    private void setSelectionListener(Goal goal, HBox goalLine) {
        goalLine.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            deselectPreviousGoal();
            selectGoal(goal, goalLine);
            styleSelectedGoal();
        });
    }

    private void deselectPreviousGoal() {
        if (selectedGoalLine != null) {
            selectedGoalLine.getStyleClass().removeIf(styleClass -> styleClass.equals("bordered"));
            JFXDepthManager.setDepth(selectedGoalLine, 1);
        }
    }

    private void selectGoal(Goal goal, HBox goalLine) {
        selectedGoalLine = goalLine;
        selectedGoal = goal;
    }

    private void styleSelectedGoal() {
        selectedGoalLine.getStyleClass().add("bordered");
        JFXDepthManager.setDepth(selectedGoalLine, 2);
    }

    private void setGoalLineStyle(HBox goalLine, Goal goal) {
        JFXDepthManager.setDepth(goalLine, 1);
        goalLine.getStyleClass().add("form");

        goalLine.setAlignment(Pos.CENTER_LEFT);

        goalLine.setPadding(new Insets(10, 35, 10, 20));
        goalLine.setSpacing(10);

        goalLine.setStyle("-fx-background-color: " + categoryColorOfGoal(goal));
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
        this.clearedScene = true;
    }

    public void setNewTextFilter(String filter) {
        this.textFilter = filter;
        filter();
    }

    private void filter() {
        if (this.clearedScene) {
            goals.stream()
                    .filter(this::fitsTextFilter)
                    .filter(goal -> fitsCategoryFilter(goal, this.categoriesFilter))
                    .forEach(this::addGoalToLayout);

            this.clearedScene = false;
        }
    }

    private boolean fitsTextFilter(Goal goal) {
        return goal.getDescription().contains(this.textFilter);
    }

    @Override
    public void filterByCategory(List<Category> categories) {
        this.categoriesFilter = categories;
        this.filter();
    }

    public Goal deleteSelected() {
        layout.getChildren().removeIf(child -> child == selectedGoalLine);
        goals.remove(selectedGoal);
        return selectedGoal;
    }

    private String categoryColorOfGoal(Goal goal) {
        if (!goal.getCategories().isEmpty()) {
            return goal.getCategories().get(0).getColor();
        } else {
            return ChiclePadColor.toHex(ChiclePadColor.WHITE);
        }
    }

    @Override
    public void setCategoryToSelectedEntry(Category category) {
        this.selectedGoal.getCategories().forEach(unboundCategory -> {
            this.goalDao.unbind(unboundCategory, this.selectedGoal);
        });
        this.selectedGoal.getCategories().clear();
        this.selectedGoal.getCategories().add(category);
        this.goalDao.bind(category, this.selectedGoal);
        this.selectedGoalLine.setStyle("-fx-background-color: " + categoryColorOfGoal(this.selectedGoal));
    }

    @Override
    public void clearEntries() {
        this.clearGoals();
    }

    @Override
    public void deleteCategoriesForEntry() {
        this.selectedGoal.getCategories().forEach(category -> this.goalDao.unbind(category, this.selectedGoal));
        this.selectedGoal.getCategories().clear();
        this.selectedGoalLine.setStyle("-fx-background-color: " + categoryColorOfGoal(this.selectedGoal));
    }
}
