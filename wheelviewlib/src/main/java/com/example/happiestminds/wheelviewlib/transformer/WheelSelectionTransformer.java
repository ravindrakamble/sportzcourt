package com.example.happiestminds.wheelviewlib.transformer;

import android.graphics.drawable.Drawable;

import com.example.happiestminds.wheelviewlib.ui.WheelView;

public interface WheelSelectionTransformer {
    void transform(Drawable drawable, WheelView.ItemState itemState);
}
