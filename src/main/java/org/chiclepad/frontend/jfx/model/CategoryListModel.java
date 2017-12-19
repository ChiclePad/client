package org.chiclepad.frontend.jfx.model;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRippler;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import org.chiclepad.backend.entity.Category;
import org.chiclepad.constants.ChiclePadColor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CategoryListModel {

    private JFXComboBox categoryPicker;

    private VBox ripplerArea;

    private VBox categories;

    private Map<Category, Boolean> categorySelected;

    private ListModel listModel;

    public CategoryListModel(VBox categories, VBox ripplerArea, ListModel listModel) {
        this(categories, ripplerArea, new JFXComboBox() /* Invisible combo box, to prevent needless null checking*/,
                listModel);
    }

    public CategoryListModel(VBox categories, VBox ripplerArea, JFXComboBox categoryPicker, ListModel listModel) {
        this.categories = categories;
        this.ripplerArea = ripplerArea;
        this.categoryPicker = categoryPicker;
        this.listModel = listModel;
        categorySelected = new HashMap<>();

        initializeCategoryPickerCellFactory();

        categoryPicker.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            listModel.setCategoryToSelectedEntry((Category) newValue);
        });
    }

    private void initializeCategoryPickerCellFactory() {
        Callback cellFactory = param -> createComboBoxLine();

        categoryPicker.setButtonCell((ListCell) cellFactory.call(null));
        categoryPicker.setCellFactory(cellFactory);
    }

    private static ListCell<Category> createComboBoxLine() {
        return new ListCell<>() {

            @Override
            protected void updateItem(Category item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setGraphic(null);

                } else {
                    HBox hBox = new HBox();

                    Label label = new Label(item.getName());

                    FontAwesomeIcon icon = new FontAwesomeIcon();
                    icon.setIconName(item.getIcon());
                    icon.setSize("1.25em");
                    icon.setFill(Color.web(item.getColor()));

                    hBox.getChildren().addAll(label, icon);

                    setGraphic(hBox);
                }
            }

        };
    }

    public void add(Category category) {
        HBox line = createCategoryListLine(category);
        categorySelected.put(category, false);
        categories.getChildren().add(line);

        categoryPicker.getItems().add(category);
    }

    private HBox createCategoryListLine(Category category) {
        HBox line = new HBox();
        styleCategoryLine(line);
        addLineRippler(line);
        addLineIcon(line, category);
        addLineName(line, category);
        addMousePressListener(line, category);
        return line;
    }

    private void addMousePressListener(HBox line, Category category) {
        line.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            categorySelected.put(category, !categorySelected.get(category));

            if (categorySelected.get(category)) {
                styleAsSelectedLine(line);
            } else {
                styleAsDeselectedLine(line);
            }

            List<Category> selectedCategories = this.categorySelected.entrySet().stream()
                    .filter(entry -> entry.getValue())
                    .map(entry -> entry.getKey()).collect(Collectors.toList());

            this.listModel.filterByCategory(selectedCategories);

        });
    }

    private void styleAsDeselectedLine(HBox line) {
        line.setStyle("");
        line.getChildren()
                .get(1)
                .getStyleClass()
                .removeIf(styleClass -> styleClass.equals("bold") || styleClass.equals("white-text"));
    }

    private void styleAsSelectedLine(HBox line) {
        line.setStyle("-fx-background-color: " + ChiclePadColor.toHex(ChiclePadColor.PRIMARY));
        line.getChildren()
                .get(1)
                .getStyleClass()
                .addAll("white-text", "bold");
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
