package com.example.happiestminds.wheelviewlib.api;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.happiestminds.wheelviewlib.exceptions.InterfaceNotImplementedException;
import com.example.happiestminds.wheelviewlib.ui.TextDrawable;
import com.example.happiestminds.wheelviewlib.ui.WheelView;
import com.lukedeighton.wheelview.R;

/**
 * Created by Narasimha.HS on 1/5/2016.
 * <p/>
 * A composite class which has an anchor view and one or more wheel views
 */
public class CompositeWheelView extends FrameLayout implements WheelView.OnWheelItemClickListener, WheelView.OnWheelItemSelectListener {

    /**
     * Anchor View which can be dragged around. Wheels will be drawn around this view
     */
    private ImageView mAnchorView;

    private WheelView mInnerWheelView;
    private WheelView mOuterWheelView;

    private static final int POSITION_LEFT = 1;
    private static final int POSITION_RIGHT = 2;
    private static final int POSITION_BOTTOM_LEFT = 9;
    private static final int POSITION_BOTTOM_RIGHT = 10;
    private static final float PERCENT_LEVEL = 65;

    /**
     * Minimal item count for wheel view for a good looking UI
     */
    private static final int IDEAL_ITEM_COUNT_FOR_UI = 12;

    /**
     * Width of the screen available
     */
    private int screenWidth;

    /**
     * Height of the screen available
     */
    private int screenHeight;

    /**
     * Height of the status bar. Standard is 48
     */
    private int statusBarHeight = 48;

    private int currentPosition;

    public CompositeWheelView(Context context, AttributeSet attrs) throws InterfaceNotImplementedException {
        this(context, attrs, 0);
    }

    public CompositeWheelView(Context context, AttributeSet attrs, int defStyleAttr) throws InterfaceNotImplementedException {
        super(context, attrs, defStyleAttr);

        if (!(context instanceof OnWheelItemSelectListener) || !(context instanceof OnWheelItemClickListener))
            throw new InterfaceNotImplementedException("Activity must implement OnWheelItemSelectListener and OnWheelItemClickListener");

        initializeUI(context, attrs);
        initializeScreenDimensions(context);

        handleAnchorDragging();
        setListeners();
    }

    /**
     * Method to handle UI initialization
     *
     * @param context - Activity instance
     * @param attrs   - custom attributes for the view
     */
    private void initializeUI(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.composite_wheel_view, this, true);
        mAnchorView = (ImageView) findViewById(R.id.anchor_image);
        mInnerWheelView = (WheelView) findViewById(R.id.inner_wheel_view);
        mOuterWheelView = (WheelView) findViewById(R.id.outer_wheel_view);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CompositeWheelView, 0, 0);
        try {
            int noOfWheels = ta.getInt(R.styleable.CompositeWheelView_noOfWheels, 1);

            if (noOfWheels == 1) {
                removeView(mOuterWheelView);
                mOuterWheelView = null;
            }

            if (ta.getDrawable(R.styleable.CompositeWheelView_anchorDrawable) != null) {
                mAnchorView.setImageDrawable(ta.getDrawable(R.styleable.CompositeWheelView_anchorDrawable));
            }

            drawOutlineInWheelViews(ta.getBoolean(R.styleable.CompositeWheelView_wheelCircleDrawable, false));

        } finally {
            ta.recycle();
        }
    }

    /**
     * Whether to draw the outline of circle in wheelviews or not
     *
     * @param drawOutline - whether to draw or not
     */
    private void drawOutlineInWheelViews(boolean drawOutline) {
        if (!drawOutline)
            return;

        mInnerWheelView.setCricleDrawable(true);
        if (mOuterWheelView != null)
            mOuterWheelView.setCricleDrawable(true);
    }

    /**
     * Method to add needed listeners
     */
    private void setListeners() {
        mInnerWheelView.setOnWheelItemSelectedListener(this);
        mInnerWheelView.setOnWheelItemClickListener(this);

        if (mOuterWheelView != null) {
            mOuterWheelView.setOnWheelItemSelectedListener(this);
            mOuterWheelView.setOnWheelItemClickListener(this);
        }
    }

    /**
     * Method to determing screen width and height
     */
    private void initializeScreenDimensions(Context context) {
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
    }

    /**
     * Set the Adapter for inner wheel
     *
     * @param adapter - {@link MaterialColorAdapter}
     */
    public void setInnerWheelAdapter(MaterialColorAdapter adapter) {
        if (adapter == null)
            throw new NullPointerException("Adapter cannot be null");

        mInnerWheelView.setAdapter(adapter);
        mInnerWheelView.setWheelItemCount(adapter.getCount() > IDEAL_ITEM_COUNT_FOR_UI ? adapter.getCount() : IDEAL_ITEM_COUNT_FOR_UI);
    }

    /**
     * Set the Adapter for outer wheel
     *
     * @param adapter - {@link MaterialColorAdapter}
     */
    public void setOuterWheelAdapter(MaterialColorAdapter adapter) {
        if (adapter == null)
            throw new NullPointerException("Adapter cannot be null");

        if (mOuterWheelView != null) {
            mOuterWheelView.setAdapter(adapter);
            mOuterWheelView.setWheelItemCount(adapter.getCount() > IDEAL_ITEM_COUNT_FOR_UI ? adapter.getCount() : IDEAL_ITEM_COUNT_FOR_UI);
        }
    }

    public void setStatusBarHeight(int statusBarHeight) {
        this.statusBarHeight = statusBarHeight;
    }

    /**
     * Method to handle dragging the anchor
     */
    private void handleAnchorDragging() {
        mAnchorView.setOnTouchListener(new OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            /**
             * Threshold to differentiate between touch and drag operations
             */
            private final float SCROLL_THRESHOLD = 10;
            private boolean isOnClick;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        isOnClick = true;
                        initialX = (int) mAnchorView.getX();
                        initialY = (int) mAnchorView.getY();
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        if (isOnClick) {
                            performClickForAnchor();
                        } else {
                            int imgPosition = decidePosition(event.getRawX(), event.getRawY());

                            if (imgPosition == POSITION_LEFT) {
                                setAnchorPosition(0, (screenHeight - mAnchorView.getHeight() - statusBarHeight) >> 1);
                            } else if (imgPosition == POSITION_RIGHT) {
                                setAnchorPosition(screenWidth - mAnchorView.getWidth(), (screenHeight - mAnchorView.getHeight() - statusBarHeight) >> 1);
                            } else if (imgPosition == POSITION_BOTTOM_LEFT) {
                                setAnchorPosition(0, screenHeight - mAnchorView.getHeight() - statusBarHeight);
                            } else if (imgPosition == POSITION_BOTTOM_RIGHT) {
                                setAnchorPosition(screenWidth - mAnchorView.getWidth(), screenHeight - mAnchorView.getHeight() - statusBarHeight);
                            }
                            setWheelDirection(imgPosition);
                        }
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        if (isOnClick && (Math.abs(initialX - event.getX()) > SCROLL_THRESHOLD || Math.abs(initialY - event.getY()) > SCROLL_THRESHOLD)) {
                            isOnClick = false;
                        }
                        setAnchorPosition(initialX + (int) (event.getRawX() - initialTouchX),
                                initialY + (int) (event.getRawY() - initialTouchY));

                        return true;
                }
                return false;
            }
        });
    }

    /**
     * Method to set the direction of wheelview with respect to anchor
     *
     * @param position - position of the item selected
     */
    private void setWheelDirection(int position) {
        currentPosition = position;

        mInnerWheelView.setWheelPosition(position);
        if (mOuterWheelView != null)
            mOuterWheelView.setWheelPosition(position);

        changeSelectionAngle(mInnerWheelView, position);
        changeSelectionAngle(mOuterWheelView, position);

        changeTextPosition(mInnerWheelView, position);
        changeTextPosition(mOuterWheelView, position);
    }

    /**
     * Method to change the selection angle of the wheel view
     *
     * @param wheelView - {@link WheelView} object
     * @param position  - position of the anchor
     */
    private void changeSelectionAngle(WheelView wheelView, int position) {
        if (wheelView == null)
            return;

        switch (position) {
            case POSITION_LEFT:
                wheelView.setSelectionAngle(0);
                break;
            case POSITION_RIGHT:
                wheelView.setSelectionAngle(180);
                break;
            case POSITION_BOTTOM_LEFT:
                wheelView.setSelectionAngle(45);
                break;
            case POSITION_BOTTOM_RIGHT:
                wheelView.setSelectionAngle(135);
                break;
        }
    }

    /**
     * Method to change the position of text w.r.t the wheel view item
     *
     * @param wheelView - {@link WheelView} object
     * @param position  - {@link TextDrawable.TextPosition} value
     */
    private void changeTextPosition(WheelView wheelView, int position) {
        if (wheelView == null)
            return;

        TextDrawable.TextPosition textPosition = TextDrawable.TextPosition.RIGHT;

        if (position == POSITION_RIGHT || position == POSITION_BOTTOM_RIGHT) {
            textPosition = TextDrawable.TextPosition.LEFT;
        }

        ((MaterialColorAdapter) wheelView.getAdapter()).setTextPosition(textPosition);
        wheelView.invalidateWheelItemDrawables();
    }

    /**
     * Method to change the anchor's position
     *
     * @param XPos - x coordinate to be set
     * @param YPos - y coordinate to be set
     */
    private void setAnchorPosition(int XPos, int YPos) {
        mAnchorView.setX(XPos);
        mAnchorView.setY(YPos);
        mAnchorView.invalidate();
    }

    /**
     * Handle the click on anchor
     */
    private void performClickForAnchor() {
        if (mInnerWheelView.isShown()) {
            mInnerWheelView.setVisibility(View.GONE);
            if (mOuterWheelView != null)
                mOuterWheelView.setVisibility(View.GONE);
        } else {
            mInnerWheelView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Method to decide the postion of the anchor when it is dragged by the user
     *
     * @param xPOS - x coordinate
     * @param yPOS - y coordinate
     * @return - A constant indicating the position of the anchor
     */
    private int decidePosition(float xPOS, float yPOS) {
        int flag;
        float xPercent = getPercentage(xPOS, screenWidth);
        float yPercent = getPercentage(yPOS, screenHeight);
        if (xPercent <= 50) {
            if (yPercent < PERCENT_LEVEL) {
                flag = POSITION_LEFT;
            } else {
                flag = POSITION_BOTTOM_LEFT;
            }
        } else {
            if (yPercent < PERCENT_LEVEL) {
                flag = POSITION_RIGHT;
            } else {
                flag = POSITION_BOTTOM_RIGHT;
            }
        }
        Log.i("decidePosition", "xPOS:" + xPOS + " yPOS:" + yPOS + " POS:" + flag);
        return flag;
    }

    private float getPercentage(float n, float total) {
        float proportion = n / total;
        return proportion * 100;
    }

    @Override
    public void onWheelItemClick(WheelView wheelView, int position, boolean isSelected) {
        Context ctx = getContext();

        if (wheelView == mInnerWheelView)
            if (mOuterWheelView != null) {

                if (mOuterWheelView.getVisibility() == VISIBLE) {

                    if (currentPosition == position)
                        mOuterWheelView.setVisibility(INVISIBLE);
                    /*removeView(mOuterWheelView);
                    addView(mOuterWheelView, 0);*/
                } else {
                    mOuterWheelView.setVisibility(VISIBLE);
                }
                /*if (mOuterWheelView.getVisibility() == VISIBLE) {
                    mOuterWheelView.setVisibility(INVISIBLE);
                    removeView(mOuterWheelView);
                    addView(mOuterWheelView, 0);
                } else {
                    removeView(mInnerWheelView);
                    addView(mInnerWheelView, 0);
                    mOuterWheelView.setVisibility(VISIBLE);
                    mOuterWheelView.setFocusable(true);
                }*/
            }

        /*if (mOuterWheelView != null && wheelView == mOuterWheelView) {
            mOuterWheelView.setVisibility(INVISIBLE);
            removeView(mOuterWheelView);
            addView(mOuterWheelView, 0);
        }*/

        currentPosition = position;

        if (ctx != null && ctx instanceof OnWheelItemClickListener) {
            if (wheelView == mInnerWheelView)
                ((OnWheelItemClickListener) ctx).onInnerWheelItemClick(wheelView, position, isSelected);
            else
                ((OnWheelItemClickListener) ctx).onOuterWheelItemClick(wheelView, position, isSelected);
        }
    }

    @Override
    public void onWheelItemSelected(WheelView wheelView, Drawable itemDrawable, int position) {
        Context ctx = getContext();

        if (ctx != null && ctx instanceof OnWheelItemSelectListener) {

            if (wheelView == mInnerWheelView)
                ((OnWheelItemSelectListener) ctx).onInnerWheelItemSelected(wheelView, itemDrawable, position);
            else
                ((OnWheelItemSelectListener) ctx).onOuterWheelItemSelected(wheelView, itemDrawable, position);
        }
    }

    /**
     * A listener to be implemented by the host activity. Notifies when a wheel item is selected.
     */
    public interface OnWheelItemSelectListener {
        /**
         * @param wheelView    WheelView that calls this listener
         * @param itemDrawable - The Drawable of the wheel item that is closest to the selection angle
         *                     (or closest to the selection angle)
         * @param position     of the adapter that is closest to the selection angle
         */
        void onInnerWheelItemSelected(WheelView wheelView, Drawable itemDrawable, int position);

        void onOuterWheelItemSelected(WheelView wheelView, Drawable itemDrawable, int position);

        //TODO onWheelItemSettled?
    }

    /**
     * A listener to be implemented by the host activity. Notifies when a wheel item is clicked by the user.
     */
    public interface OnWheelItemClickListener {
        void onInnerWheelItemClick(WheelView wheelView, int position, boolean isSelected);

        void onOuterWheelItemClick(WheelView wheelView, int position, boolean isSelected);
    }
}