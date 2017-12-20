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
import javafx.util.Callback;
import org.chiclepad.backend.Dao.CategoryDao;
import org.chiclepad.backend.Dao.DaoFactory;
import org.chiclepad.backend.business.session.UserSessionManager;
import org.chiclepad.backend.entity.Category;
import org.chiclepad.constants.ChiclePadColor;
import org.chiclepad.frontend.jfx.model.CategoryListModel;

public class CategoryPopup {

    private static CategoryDao categoryDao = DaoFactory.INSTANCE.getCategoryDao();

    public static void showEditCategoryAtPosition(HBox line, Category category, CategoryListModel categoryListModel) {
        JFXPopup popup = new JFXPopup();
        VBox layout = createPopupLayout(popup, categoryListModel);
        popup.setPopupContent(layout);
        popup.show(layout, 20, 40);
    }

    public static void showAddCategoryUnderParent(Node parent, CategoryListModel categoryListModel) {
        JFXPopup popup = new JFXPopup();
        VBox layout = createPopupLayout(popup, categoryListModel);
        popup.setPopupContent(layout);
        popup.show(parent, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT, 0, 5);
    }

    private static VBox createPopupLayout(JFXPopup popup, CategoryListModel categoryListModel) {
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
        styleCreateButton(createButton);
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

    private static void styleCreateButton(JFXButton createButton) {
        createButton.getStyleClass().addAll("green-background", "small-normal-text", "white-text");
        createButton.setPrefWidth(10000);

        VBox.setVgrow(createButton, Priority.ALWAYS);
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
