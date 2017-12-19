package org.chiclepad.frontend.jfx.model;

import org.chiclepad.backend.entity.Category;

import java.util.List;

public interface ListModel {
    void filterByCategory(List<Category> categories);

    void setCategoryToSelectedEntry(Category category);
}

