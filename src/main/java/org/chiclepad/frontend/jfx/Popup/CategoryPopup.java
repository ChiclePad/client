package org.chiclepad.frontend.jfx.Popup;

import com.jfoenix.controls.*;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconName;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import org.chiclepad.backend.Dao.CategoryDao;
import org.chiclepad.backend.Dao.DaoFactory;
import org.chiclepad.backend.business.session.UserSessionManager;
import org.chiclepad.backend.entity.Category;
import org.chiclepad.constants.ChiclePadColor;
import org.chiclepad.frontend.jfx.model.CategoryListModel;

public class CategoryPopup {

    private static CategoryDao categoryDao = DaoFactory.INSTANCE.getCategoryDao();

    public static void showEditCategoryAtPosition(HBox parent, Category category, CategoryListModel categoryListModel) {
        JFXPopup popup = new JFXPopup();
        VBox layout = createEditPopupLayout(popup, parent, category, categoryListModel);
        popup.setPopupContent(layout);
        popup.show(parent, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT, 50, 25);
    }

    private static VBox createEditPopupLayout(
            JFXPopup popup,
            HBox parent,
            Category category,
            CategoryListModel categoryListModel
    ) {
        JFXTextField categoryNameField = new JFXTextField(category.getName());
        styleCategoryName(categoryNameField);

        HBox categoryDetails = new HBox();
        styleCategoryDetails(categoryDetails);

        JFXColorPicker categoryColorPicker = new JFXColorPicker();
        styleCategoryColorPicker(categoryColorPicker);
        categoryColorPicker.valueProperty().setValue(Color.web(category.getColor()));

        JFXComboBox<FontAwesomeIconName> categoryIconChooser = new JFXComboBox<>();
        fillCategoryIconChooser(categoryIconChooser);
        styleCategoryIconChooser(categoryIconChooser);
        categoryIconChooser.getSelectionModel().select(FontAwesomeIconName.valueOf(category.getIcon()));

        categoryDetails.getChildren().addAll(categoryColorPicker, categoryIconChooser);

        HBox buttons = new HBox();
        buttons.setSpacing(10);

        JFXButton updateButton = new JFXButton("Update");
        styleGreenButton(updateButton);
        updateButton.setPrefWidth(10000);
        setUpdateButtonCallback(
                popup,
                parent,
                category,
                categoryListModel,
                categoryNameField,
                categoryColorPicker,
                categoryIconChooser,
                updateButton
        );

        JFXButton removeButton = new JFXButton("Remove");
        styleRedButton(removeButton);
        removeButton.setPrefWidth(10000);
        setRemoveButtonCallback(popup, parent, category, categoryListModel, removeButton);

        buttons.getChildren().addAll(updateButton, removeButton);

        VBox layout = new VBox(categoryNameField, categoryDetails, buttons);
        styleLayout(layout);

        return layout;
    }

    private static void setUpdateButtonCallback(JFXPopup popup, HBox parent, Category category, CategoryListModel categoryListModel, JFXTextField categoryNameField, JFXColorPicker categoryColorPicker, JFXComboBox<FontAwesomeIconName> categoryIconChooser, JFXButton updateButton) {
        updateButton.setOnAction(event -> {
            category.setName(categoryNameField.getText());
            category.setColor(ChiclePadColor.toHex(categoryColorPicker.getValue()));
            category.setIcon(categoryIconChooser.getSelectionModel().getSelectedItem().name());

            categoryDao.update(category);
            categoryListModel.update(parent, category);
            popup.hide();
        });
    }

    private static void setRemoveButtonCallback(JFXPopup popup, HBox parent, Category category, CategoryListModel categoryListModel, JFXButton removeButton) {
        removeButton.setOnAction(event -> {
            categoryDao.delete(category);
            categoryListModel.remove(parent, category);
            popup.hide();
        });
    }

    public static void showAddCategoryUnderParent(Node parent, CategoryListModel categoryListModel) {
        JFXPopup popup = new JFXPopup();
        VBox layout = createAddPopupLayout(popup, categoryListModel);
        popup.setPopupContent(layout);
        popup.show(parent, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT, 0, 5);
    }

    private static VBox createAddPopupLayout(JFXPopup popup, CategoryListModel categoryListModel) {
        JFXTextField categoryNameField = new JFXTextField();
        styleCategoryName(categoryNameField);

        HBox categoryDetails = new HBox();
        styleCategoryDetails(categoryDetails);

        JFXColorPicker categoryColorPicker = new JFXColorPicker();
        styleCategoryColorPicker(categoryColorPicker);

        JFXComboBox<FontAwesomeIconName> categoryIconChooser = new JFXComboBox<>();
        fillCategoryIconChooser(categoryIconChooser);
        styleCategoryIconChooser(categoryIconChooser);

        categoryDetails.getChildren().addAll(categoryColorPicker, categoryIconChooser);

        JFXButton createButton = new JFXButton("Create");
        styleGreenButton(createButton);
        setButtonDisabledListener(categoryNameField, createButton);
        setButtonOnClick(
                categoryListModel,
                categoryNameField,
                categoryColorPicker,
                categoryIconChooser,
                createButton,
                popup
        );

        VBox layout = new VBox(categoryNameField, categoryDetails, createButton);
        styleLayout(layout);

        return layout;
    }

    private static void setButtonOnClick(
            CategoryListModel categoryListModel,
            JFXTextField categoryNameField,
            JFXColorPicker categoryColorPicker,
            JFXComboBox<FontAwesomeIconName> categoryIconChooser,
            JFXButton createButton,
            JFXPopup parent) {

        int userId = UserSessionManager.INSTANCE.getCurrentUserSession().getUserId();

        createButton.setOnAction(event -> {
            String newCategoryName = categoryNameField.getText();
            String newCategoryIcon = categoryIconChooser.getSelectionModel().getSelectedItem().name();
            String newCategoryColor = ChiclePadColor.toHex(categoryColorPicker.getValue());

            Category category = categoryDao.create(userId, newCategoryName, newCategoryIcon, newCategoryColor);
            categoryListModel.add(category);
            parent.hide();
        });
    }

    private static void setButtonDisabledListener(JFXTextField categoryNameField, JFXButton createButton) {
        createButton.setDisable(true);
        categoryNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            createButton.setDisable(newValue.isEmpty());
        });
    }

    private static void styleGreenButton(JFXButton createButton) {
        styleButton(createButton, "#8DC44E");
        createButton.setPrefWidth(10000);
    }

    private static void styleRedButton(JFXButton createButton) {
        styleButton(createButton, "#A24936");
        createButton.setPrefWidth(10000);
    }

    private static void styleButton(JFXButton createButton, String color) {
        createButton.setStyle("-fx-background-color: " + color + ";" +
                "-fx-font-size: 18px;" +
                "-fx-font-family: Roboto;" +
                "-fx-text-fill: white;");
    }

    private static void styleCategoryName(JFXTextField categoryNameField) {
        categoryNameField.setPromptText("Category Name");
        categoryNameField.getStyleClass().add("small-normal-text");

        categoryNameField.setFocusColor(ChiclePadColor.PRIMARY);
        categoryNameField.setUnFocusColor(ChiclePadColor.GREY_TEXT);

        VBox.setVgrow(categoryNameField, Priority.ALWAYS);
    }

    private static void styleCategoryDetails(HBox categoryDetails) {
        categoryDetails.setSpacing(10);
        VBox.setVgrow(categoryDetails, Priority.ALWAYS);
    }

    private static void styleCategoryColorPicker(JFXColorPicker categoryColorPicker) {
        categoryColorPicker.getStyleClass().add("small-text");
        categoryColorPicker.setPrefWidth(1000);

        categoryColorPicker.valueProperty().setValue(ChiclePadColor.SECONDARY);
    }

    private static void styleCategoryIconChooser(JFXComboBox categoryIconChooser) {
        categoryIconChooser.getStyleClass().add("small-text");
        categoryIconChooser.setPrefWidth(20);

        categoryIconChooser.setFocusColor(ChiclePadColor.PRIMARY);
        categoryIconChooser.setUnFocusColor(ChiclePadColor.GREY_TEXT);

        categoryIconChooser.getSelectionModel().selectFirst();
    }

    private static ListCell<FontAwesomeIconName> createComboBoxLine() {
        return new ListCell<>() {

            @Override
            protected void updateItem(FontAwesomeIconName item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setGraphic(null);

                } else {
                    FontAwesomeIcon icon = new FontAwesomeIcon();
                    icon.setIcon(item);
                    icon.setSize("1.25em");

                    setGraphic(icon);
                }
            }

        };
    }

    private static void fillCategoryIconChooser(JFXComboBox<FontAwesomeIconName> categoryIconChooser) {
        Callback cellFactory = param -> createComboBoxLine();

        categoryIconChooser.setButtonCell((ListCell) cellFactory.call(null));
        categoryIconChooser.setCellFactory(cellFactory);

        categoryIconChooser.getItems().addAll(FontAwesomeIconName.values());
    }

    private static void styleLayout(VBox layout) {
        layout.getStyleClass().addAll("form", "bordered");

        layout.setAlignment(Pos.TOP_RIGHT);
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.setSpacing(20);

        layout.setPrefWidth(245);
    }

}
