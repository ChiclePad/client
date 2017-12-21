package org.chiclepad.backend.entity;

/**
 * Category of an entry, used for filtering and recognition
 */
public class Category {

    /**
     * Unique category id
     */
    private final int id;

    /**
     * Name of the category (eg. 'Work', 'House')
     */
    private String name;

    /**
     * Font-Awesome 4 icon
     */
    private String icon;

    /**
     * Hexadecimal color (ie. '#ccbb11')
     */
    private String color;

    /**
     * Basic constructor
     */
    public Category(int id, String name, String icon, String color) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.color = color;
    }

    /**
     * @return Category id
     */
    public int getId() {
        return id;
    }

    /**
     * @return Category name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name Change name of the category
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Category Icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     * @param icon New Font-Awesome icon
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * @return Category color
     */
    public String getColor() {
        return color;
    }

    /**
     * @param color New hexadecimal color
     */
    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o != null &&
                getClass() == o.getClass() &&
                id == ((Category) o).id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", color='" + color + '\'' +
                '}';
    }

}
