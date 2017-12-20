package org.chiclepad.frontend.jfx.model;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRippler;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconName;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import org.chiclepad.backend.entity.Category;
import org.chiclepad.constants.ChiclePadColor;
import org.chiclepad.frontend.jfx.Popup.CategoryPopup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CategoryListModel {

    private JFXComboBox categoryPicker;

    private VBox ripplerArea;

    private VBox categories;

    private Map<Category, Boolean> categorySelected;

    public final static Category DESELECT_CATEGORY = new Category(
            -1,
            "Remove category",
            FontAwesomeIconName.EXCLAMATION_CIRCLE.name(),
            "#808080");

    private boolean lastClickWasPrimaryButton;

    private List<ListModel> listModels;

    public CategoryListModel(VBox categories, VBox ripplerArea) {
        this(categories, ripplerArea, new JFXComboBox() /* Invisible combo box, to prevent needless null checking*/);
    }

    public CategoryListModel(VBox categories, VBox ripplerArea, JFXComboBox categoryPicker) {
        this.categories = categories;
        this.ripplerArea = ripplerArea;
        this.categoryPicker = categoryPicker;
        this.listModels = new ArrayList<>();
        categorySelected = new HashMap<>();

        initializeCategoryPickerCellFactory();
        setSelectionCallback(categoryPicker);
        categoryPicker.getItems().add(CategoryListModel.DESELECT_CATEGORY);
    }

    public void subscribeListModel(ListModel listModel) {
        this.listModels.add(listModel);
    }

    private void initializeCategoryPickerCellFactory() {
        Callback cellFactory = param -> createComboBoxLine();
        Callback selectedCellFactory = param -> createSelectedComboBoxLine();

        categoryPicker.setButtonCell((ListCell) selectedCellFactory.call(null));
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
                    setGraphic(createComboBoxHboxLine(item));
                }
            }

        };
    }

    private static ListCell<Category> createSelectedComboBoxLine() {
        return new ListCell<>() {

            @Override
            protected void updateItem(Category item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setGraphic(null);
                } else {
                    setGraphic(createComboBoxSelectedHboxLine(item));
                }
            }

        };
    }

    private static HBox createComboBoxHboxLine(Category item) {
        HBox hBox = new HBox();
        hBox.setSpacing(10);

        FontAwesomeIcon icon = new FontAwesomeIcon();
        styleIcon(item, icon, 1.25);

        Label label = new Label(item.getName());

        hBox.getChildren().addAll(icon, label);
        return hBox;
    }

    private static HBox createComboBoxSelectedHboxLine(Category item) {
        HBox hBox = new HBox();
        hBox.setSpacing(10);

        FontAwesomeIcon icon = new FontAwesomeIcon();
        styleIcon(item, icon, 1);

        Label label = new Label(item.getName());
        label.getStyleClass().add("text");

        hBox.getChildren().addAll(icon, label);
        return hBox;
    }

    private void setSelectionCallback(JFXComboBox categoryPicker) {
        categoryPicker.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            listModels.forEach(listModel -> listModel.setCategoryToSelectedEntry((Category) newValue));
            Platform.runLater(() -> this.categoryPicker.setValue(null));
        });
    }

    private static void styleIcon(Category item, FontAwesomeIcon icon, double size) {
        icon.setIconName(item.getIcon());
        icon.setSize(size + "em");
        icon.setFill(Color.web(item.getColor()));
    }

    public void add(Category category) {

        categoryPicker.getItems().add(category);

        if (category == CategoryListModel.DESELECT_CATEGORY) {
            return;
        }

        HBox line = createCategoryListLine(category);
        categorySelected.put(category, false);
        categories.getChildren().add(line);
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
            lastClickWasPrimaryButton = event.isPrimaryButtonDown();

            if (event.isPrimaryButtonDown()) {
                selectCategory(line, category);
                listModels.forEach(ListModel::clearEntries);
                return;
            }

            if (event.isSecondaryButtonDown()) {
                editCategory(line, category);
            }
        });

        line.addEventFilter(MouseEvent.MOUSE_RELEASED, event -> {
            if (!lastClickWasPrimaryButton) {
                return;
            }

            List<Category> selectedCategories = this.categorySelected.entrySet().stream()
                    .filter(Map.Entry::getValue)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

            this.listModels.forEach(listModel -> listModel.filterByCategory(selectedCategories));
        });
    }

    private void selectCategory(HBox line, Category category) {
        categorySelected.put(category, !categorySelected.get(category));

        if (categorySelected.get(category)) {
            styleAsSelectedLine(line);
        } else {
            styleAsDeselectedLine(line);
        }

        listModels.forEach(ListModel::clearEntries);
    }

    private void editCategory(HBox line, Category category) {
        CategoryPopup.showEditCategoryAtPosition(line, category, this);
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
        line.setAlignment(Pos.CENTER_LEFT);
    }

    public void remove(HBox parent, Category category) {
        categoryPicker.getItems().remove(category);
        categorySelected.remove(category);
        categories.getChildren().remove(parent);
    }

    public void update(HBox parent, Category category) {
        categoryPicker.getItems().remove(category);
        categoryPicker.getItems().add(category);

        categories.getChildren().remove(parent);
        HBox line = createCategoryListLine(category);
        categories.getChildren().add(line);

        if (categorySelected.get(category)) {
            selectCategory(line, category);
        }
    }

}
