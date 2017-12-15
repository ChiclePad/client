package org.chiclepad.frontend.jfx.model;

import com.jfoenix.controls.JFXRippler;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.chiclepad.backend.entity.Category;
import org.chiclepad.frontend.jfx.ChiclePadColor;

import java.util.HashMap;
import java.util.Map;

public class CategoryListModel {

    private VBox ripplerArea;

    private VBox categories;

    private Map<HBox, Boolean> categorySelected;

    public CategoryListModel(VBox categories, VBox ripplerArea) {
        this.categories = categories;
        this.ripplerArea = ripplerArea;
        categorySelected = new HashMap<>();
    }


    public void add(Category category) {
        HBox line = createCategoryListLine(category);
        categorySelected.put(line, false);
        categories.getChildren().add(line);
    }

    private HBox createCategoryListLine(Category category) {
        HBox line = new HBox();
        styleCategoryLine(line);
        addLineRippler(line);
        addLineIcon(line, category);
        addLineName(line, category);
        addMousePressListener(line);
        return line;
    }

    private void addMousePressListener(HBox line) {
        line.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            categorySelected.put(line, !categorySelected.get(line));

            if (categorySelected.get(line)) {
                styleAsSelectedLine(line);
            } else {
                styleAsDeselectedLine(line);
            }
        });
    }

    private void styleAsDeselectedLine(HBox line) {
        line.setStyle("");
        line.getStyleClass().removeIf(styleClass -> styleClass.equals("bold") || styleClass.equals("white-text"));
    }

    private void styleAsSelectedLine(HBox line) {
        line.setStyle("-fx-background-color: " + ChiclePadColor.toHex(ChiclePadColor.PRIMARY));
        line.getStyleClass().addAll("white-text", "bold");
    }

    private void addLineIcon(HBox line, Category category) {
        FontAwesomeIcon icon = new FontAwesomeIcon();
        icon.setFill(Color.web(category.getColor()));
        icon.setIconName(category.getIcon());

        line.getChildren().add(icon);
    }

    private void addLineName(HBox line, Category category) {
        Label name = new Label(category.getName());
        name.getStyleClass().addAll("text-on-grey", "small-normal-text");

        line.getChildren().add(name);
    }

    private void addLineRippler(HBox line) {
        JFXRippler rippler = new JFXRippler(line);
        ripplerArea.getChildren().add(rippler);
    }

    private void styleCategoryLine(HBox line) {
        line.setSpacing(15);
        line.setPadding(new Insets(10, 10, 10, 40));
        line.getStyleClass().addAll("highlighted", "grey-dark-background", "grey-dark-background-hover");
    }


}

