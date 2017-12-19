package org.chiclepad.frontend.jfx.model;

import org.chiclepad.backend.entity.Category;
import org.chiclepad.backend.entity.Entry;
import org.chiclepad.backend.entity.Goal;

import java.util.List;

public interface ListModel {
    void filterByCategory(List<Category> categories);

    void setCategoryToSelectedEntry(Category category);

    default boolean fitsCategoryFilter(Entry entry, List<Category> categoriesFilter) {
        if (categoriesFilter.isEmpty()) {
            return true;
        }

        if (entry.getCategories().isEmpty()) {
            return false;
        }

        return entry.getCategories().stream().allMatch(category -> categoriesFilter.contains(category));
    }
}

