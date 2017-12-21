package org.chiclepad.frontend.jfx.model;

import org.chiclepad.backend.entity.Category;
import org.chiclepad.backend.entity.Entry;

import java.util.List;

public interface ListModel {

    void filterByCategory(List<Category> categories);

    void setCategoryToSelectedEntry(Category category);

    default boolean fitsCategoryFilter(Entry entry, List<Category> categoriesFilter) {
        if (categoriesFilter.isEmpty()) {
            return true;
        }

        List<Category> categories = entry.getCategories();
        return !categories.isEmpty() && categories.stream().allMatch(categoriesFilter::contains);
    }

    void clearEntries();

    void deleteCategoriesForEntry();
}

