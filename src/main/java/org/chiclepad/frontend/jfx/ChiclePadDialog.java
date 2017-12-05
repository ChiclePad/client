package org.chiclepad.frontend.jfx;

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

        dialogLayout.setHeading(toText(header, 20));
        dialogLayout.setBody(toText(body, 16));

        JFXDialog dialog = new JFXDialog(parent, dialogLayout, JFXDialog.DialogTransition.TOP, true);

        JFXButton closeButton = createCloseButton();
        closeButton.setOnAction(event -> {
            dialog.close();
            parent.setVisible(false);
        });
        dialogLayout.setActions(closeButton);

        parent.setVisible(true);
        dialog.show();
    }

    private static Text toText(String string, int fontSize) {
        Text result = new Text(string);
        result.setStyle("-fx-text-fill: #464947;" +
                "-fx-font-size: " + fontSize + "px");

        return result;
    }

    private static JFXButton createCloseButton() {
        JFXButton closeButton = new JFXButton("Okay");
        closeButton.setStyle("-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 15, 0, 1, 3);" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 16px;" +
                "-fx-background-color: #3E5641");
        closeButton.setPadding(new Insets(7, 18, 7, 18));
        return closeButton;
    }

}
