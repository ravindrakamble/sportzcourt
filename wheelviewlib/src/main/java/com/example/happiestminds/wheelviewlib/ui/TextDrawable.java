package com.example.happiestminds.wheelviewlib.ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

public class TextDrawable extends Drawable {

    private final String text;
    private final Paint paint;

    /**
     * By default, draw to the right of the icon
     */
    private TextPosition textPosition = TextPosition.RIGHT;

    public TextDrawable(String text, TextPosition textPosition) {
        this.text = text;
        this.paint = new Paint();
        this.textPosition = textPosition;

        paint.setColor(Color.WHITE);
        paint.setTextSize(50f);
        paint.setAntiAlias(true);
        paint.setFakeBoldText(true);
        paint.setShadowLayer(25f, 0, 0, Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextAlign(Paint.Align.RIGHT);
    }

    @Override
    public void draw(Canvas canvas) {
        Rect bounds = getBounds();
        //Left side
        if (textPosition == TextPosition.LEFT)
            canvas.drawText(text, bounds.centerX() - 10f * text.length(), bounds.centerY() + 15f, paint);

        //Right Side
        if (textPosition == TextPosition.RIGHT)
            canvas.drawText(text, bounds.centerX() + 35f * text.length(), bounds.centerY() + 15f, paint);
    }

    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        paint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    public String getText() {
        return text;
    }

    public enum TextPosition {
        LEFT, RIGHT, TOP, BOTTOM
    }
}