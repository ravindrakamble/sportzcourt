package com.example.happiestminds.wheelviewlib.ui;

import android.graphics.Rect;

/**
 * Created by Narasimha.HS on 1/5/2016.
 * <p/>
 * Class which outlines the circle for wheel view
 */
public class Circle {
    float mCenterX, mCenterY;
    float mRadius;

    Circle() {
    }

    Circle(float centerX, float centerY, float radius) {
        mCenterX = centerX;
        mCenterY = centerY;
        mRadius = radius;
    }

    boolean contains(float x, float y) {
        x = mCenterX - x;
        y = mCenterY - y;
        //return x * x + y * y <= mRadius * mRadius;
        //FIXME - TO handle scrolling of inner circles(if any)
        float diff = mRadius * mRadius - (x * x + y * y);
        return (diff >= 0 && Math.sqrt(diff) < 500);
    }

    public float getCenterX() {
        return mCenterX;
    }

    public float getCenterY() {
        return mCenterY;
    }

    public float getRadius() {
        return mRadius;
    }

    Rect getBoundingRect() {
        return new Rect(Math.round(mCenterX - mRadius), Math.round(mCenterY - mRadius),
                Math.round(mCenterX + mRadius), Math.round(mCenterY + mRadius));
    }

    Rect getBoundingRectSmall() {
        return new Rect(Math.round(mCenterX - mRadius - 10), Math.round(mCenterY - mRadius - 10),
                Math.round(mCenterX + mRadius - 10), Math.round(mCenterY + mRadius - 10));
    }

    /**
     * The Angle from this circle's center to the position x, y
     * y is considered to go down (like android view system)
     */
    float angleTo(float x, float y) {
        return (float) Math.atan2((mCenterY - y), (x - mCenterX));
    }

    float angleToDegrees(float x, float y) {
        return (float) Math.toDegrees(angleTo(x, y));
    }

    /**
     * Clamps the value to a number between 0 and the upperLimit
     */
    static int clamp(int value, int upperLimit) {
        if (value < 0) {
            return value + (-1 * (int) Math.floor(value / (float) upperLimit)) * upperLimit;
        } else {
            return value % upperLimit;
        }
    }

    static float clamp180(float value) {
        //TODO clamp(int value, int upperLimit) could use this code? + test it
        return (((value + 180f) % 360f + 360f) % 360f) - 180f;
    }

    /**
     * Returns the shortest angle difference when the inputs range between -180 and 180 (such as from Math.atan2)
     */
    static float shortestAngle(float angleA, float angleB) {
        float angle = angleA - angleB;
        if (angle > 180f) {
            angle -= 360f;
        } else if (angle < -180f) {
            angle += 360f;
        }
        return angle;
    }

    @Override
    public String toString() {
        return "Radius: " + mRadius + " X: " + mCenterX + " Y: " + mCenterY;
    }
}
