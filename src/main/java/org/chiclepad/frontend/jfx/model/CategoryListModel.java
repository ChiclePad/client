package org.chiclepad.frontend.jfx.model;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXListView;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import org.chiclepad.backend.entity.Category;
import org.chiclepad.frontend.jfx.ChiclePadColor;

public class CategoryListModel {

    private JFXListView<JFXCheckBox> categoryList;

    public CategoryListModel(JFXListView<JFXCheckBox> categoryList) {
        this.categoryList = categoryList;
    }


    public void add(Category category) {
        JFXCheckBox line = new JFXCheckBox();
        line.setText(category.getName());
        line.setCheckedColor(ChiclePadColor.PRIMARY);

        FontAwesomeIcon icon = new FontAwesomeIcon();
        icon.setIconName(category.getIcon());
        line.setGraphic(icon);

        categoryList.getItems().add(line);
    }

}

