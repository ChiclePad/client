package org.chiclepad.frontend.jfx.homepage;

import com.jfoenix.controls.*;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconName;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.chiclepad.backend.Dao.CategoryDao;
import org.chiclepad.backend.Dao.DaoFactory;
import org.chiclepad.business.session.UserSessionManager;
import org.chiclepad.frontend.jfx.ChiclePadColor;

public class CategoryPopup {

    private static CategoryDao categoryDao = DaoFactory.INSTANCE.getCategoryDao();

    public static void showUnderParent(Node parent) {
        JFXPopup popup = new JFXPopup();
        VBox layout = createPopupLayout(popup);
        popup.setPopupContent(layout);
        popup.show(parent, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT, 0, 5);
    }

    private static VBox createPopupLayout(JFXPopup popup) {
        JFXColorPicker categoryColorPicker = new JFXColorPicker();
        VBox.setVgrow(categoryColorPicker, Priority.ALWAYS);

        JFXTextField categoryNameField = new JFXTextField();
        categoryNameField.setPromptText("Category Name");
        VBox.setVgrow(categoryNameField, Priority.ALWAYS);

        JFXComboBox categoryIconChooser = new JFXComboBox();
        categoryIconChooser.getItems().addAll(FontAwesomeIconName.values());
        VBox.setVgrow(categoryIconChooser, Priority.ALWAYS);

        JFXButton confirmButton = new JFXButton("Create");
        confirmButton.getStyleClass().add("green-background");
        VBox.setVgrow(confirmButton, Priority.ALWAYS);

        int userId = UserSessionManager.INSTANCE.getCurrentUserSession().getUserId();
        confirmButton.setOnAction(event -> {
            categoryDao.create(
                    userId,
                    categoryNameField.getText(),
                    "CIRCLE",
                    ChiclePadColor.toHex(categoryColorPicker.getValue())
            );
        });

        VBox layout = new VBox(categoryColorPicker, categoryNameField, categoryIconChooser, confirmButton);
        styleLayout(layout);

        return layout;
    }

    private static void styleLayout(VBox layout) {
        layout.setPadding(new Insets(15, 10, 15, 10));
        layout.setSpacing(10);
    }

}
