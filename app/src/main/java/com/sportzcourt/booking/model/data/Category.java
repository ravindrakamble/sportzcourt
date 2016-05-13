package com.sportzcourt.booking.model.data;

/**
 * Created by Narasimha.HS on 1/12/2016.
 *
 * Category Maodel
 */
public class Category {
    private String categoryName;
    private int categoryDrawable;

    public Category(String categoryName, int categoryDrawable) {
        this.categoryName = categoryName;
        this.categoryDrawable = categoryDrawable;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getCategoryDrawable() {
        return categoryDrawable;
    }

    public void setCategoryDrawable(int categoryDrawable) {
        this.categoryDrawable = categoryDrawable;
    }
}
