package com.sportzcourt.booking.model.data;

/**
 * Created by Narasimha.HS on 1/12/2016.
 * <p/>
 * Item Model
 */
public class Item {
    private String name;
    private int itemDrawable;
    private String price;

    public Item(String name, int itemDrawable, String price) {
        this.name = name;
        this.itemDrawable = itemDrawable;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getItemDrawable() {
        return itemDrawable;
    }

    public void setItemDrawable(int itemDrawable) {
        this.itemDrawable = itemDrawable;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
