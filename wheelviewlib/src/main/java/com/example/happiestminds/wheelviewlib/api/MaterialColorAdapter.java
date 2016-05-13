package com.example.happiestminds.wheelviewlib.api;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;

import com.example.happiestminds.wheelviewlib.adapter.WheelArrayAdapter;
import com.example.happiestminds.wheelviewlib.adapter.WheelItem;
import com.example.happiestminds.wheelviewlib.ui.TextDrawable;

import java.util.List;


/**
 * Created by Narasimha.HS on 1/5/2016.
 * <p/>
 * Adapter class for wheel view
 */
public class MaterialColorAdapter extends WheelArrayAdapter<WheelItem> {
    private Context mCxt;
    private TextDrawable.TextPosition textPosition;

    public MaterialColorAdapter(List<WheelItem> entries, Context cxt, TextDrawable.TextPosition textPosition) {
        super(entries);
        mCxt = cxt;
        this.textPosition = textPosition;
    }

    @Override
    public Drawable getDrawable(int position) {

        Drawable[] drawable = new Drawable[]{
                createOvalDrawable(getItem(position).getColor()),
                mCxt.getResources().getDrawable(getItem(position).getDrawable()),
                new TextDrawable(getItem(position).getText(), textPosition)
        };

        return new LayerDrawable(drawable);
    }

    private Drawable createOvalDrawable(int color) {
        ShapeDrawable shapeDrawable = new ShapeDrawable(new OvalShape());
        shapeDrawable.getPaint().setColor(color);
        return shapeDrawable;
    }

    public void setTextPosition(TextDrawable.TextPosition textPosition) {
        this.textPosition = textPosition;
    }
}