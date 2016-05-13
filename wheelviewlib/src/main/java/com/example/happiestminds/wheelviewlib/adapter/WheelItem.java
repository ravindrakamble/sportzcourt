package com.example.happiestminds.wheelviewlib.adapter;

/**
 * Created by Narasimha.HS on 1/5/2016.
 *
 * Individual list item of a wheel view
 */
public class WheelItem {
    private int color;
    private String text;
    private int drawable;

    public WheelItem(int color, String text, int drawable){
        this.color = color;
        this.text = text;
        this.drawable = drawable;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
