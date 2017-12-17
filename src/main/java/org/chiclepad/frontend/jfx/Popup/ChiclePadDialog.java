package org.chiclepad.frontend.jfx.Popup;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.geometry.Insets;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class ChiclePadDialog {

    /**
     * @param header Header text
     * @param body   Body text
     * @param parent Invisible Stack Pane where the dialog should be displayed
     */
    public static void show(String header, String body, StackPane parent) {
        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        setHeaderText(header, dialogLayout);
        setBodyText(body, dialogLayout);

        JFXDialog dialog = new JFXDialog(parent, dialogLayout, JFXDialog.DialogTransition.CENTER, true);
        addCloseButton(parent, dialogLayout, dialog);

        parent.setVisible(true);
        dialog.show();
    }

    private static void addCloseButton(StackPane parent, JFXDialogLayout dialogLayout, JFXDialog dialog) {
        JFXButton closeButton = createCloseButton();
        closeButton.setOnAction(event -> {
            dialog.close();
            parent.setVisible(false);
        });
        dialogLayout.setActions(closeButton);
    }

    private static void setBodyText(String body, JFXDialogLayout dialogLayout) {
        Text bodyText = new Text(body);
        bodyText.getStyleClass().addAll("text", "normal-text");
        dialogLayout.setBody(bodyText);
    }

    private static void setHeaderText(String header, JFXDialogLayout dialogLayout) {
        Text headerText = new Text(header);
        headerText.getStyleClass().addAll("text", "large-text", "red-text");
        dialogLayout.setHeading(headerText);
    }

    private static JFXButton createCloseButton() {
        JFXButton closeButton = new JFXButton("Okay");
        closeButton.getStyleClass().addAll("white-text", "normal-text", "secondary-background");
        closeButton.setButtonType(JFXButton.ButtonType.RAISED);
        closeButton.setPadding(new Insets(10, 25, 10, 25));
        return closeButton;
    }

}
