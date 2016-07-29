/*
 *  Copyright (C) 2016 KingJA
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package lib.kingja.switchbutton;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import java.util.Arrays;
import java.util.List;

/**
 * Description：A smart switchable button,support multiple tabs.
 * Create Time：2016/7/27 14:57
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
public class SwitchMultiButton extends View {

    /*default value*/
    private List<String> mTabTextList = Arrays.asList("R", "L");
    private int mTabNum = mTabTextList.size();
    private static final float DEFAULT_WIDTH_DP = 280f;
    private static final float DEFAULT_HEIGHT_DP = 30f;
    private static final float STROKE_RADIUS = 0;
    private static final float STROKE_WIDTH = 2;
    private static final float TEXT_SIZE = 14;
    private static final int SELECTED_COLOR = 0xffeb7b00;
    private static final int SELECTED_TAB = 0;
    /*other*/
    private Paint mStrokePaint;
    private Paint mFillPaint;
    private int mWidth;
    private int mHeight;
    private TextPaint mSelectedTextPaint;
    private TextPaint mUnselectedTextPaint;
    private OnSwitchListener onSwitchListener;
    private float mStrokeRadius;
    private float mStrokeWidth;
    private int mSelectedColor;
    private float mTextSize;
    private int mSelectedTab;
    private float perWidth;
    private float mTextHeightOffset;


    public SwitchMultiButton(Context context) {
        this(context, null);
    }

    public SwitchMultiButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwitchMultiButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        initPaint();
    }

    /**
     * get the values of attributes
     */
    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SwitchMultiButton);
        mStrokeRadius = typedArray.getDimension(R.styleable.SwitchMultiButton_strokeRadius, dp2px(STROKE_RADIUS));
        mStrokeWidth = typedArray.getDimension(R.styleable.SwitchMultiButton_strokeWidth, dp2px(STROKE_WIDTH));
        mTextSize = typedArray.getDimension(R.styleable.SwitchMultiButton_textSize, sp2px(TEXT_SIZE));
        mSelectedColor = typedArray.getColor(R.styleable.SwitchMultiButton_selectedColor, SELECTED_COLOR);
        mSelectedTab = typedArray.getInteger(R.styleable.SwitchMultiButton_selectedTab, SELECTED_TAB);
        typedArray.recycle();
    }

    /**
     * define paints
     */
    private void initPaint() {
        // round rectangle paint
        mStrokePaint = new Paint();
        mStrokePaint.setColor(mSelectedColor);
        mStrokePaint.setStyle(Paint.Style.STROKE);
        mStrokePaint.setAntiAlias(true);
        mStrokePaint.setStrokeWidth(mStrokeWidth);
        // selected paint
        mFillPaint = new Paint();
        mFillPaint.setColor(mSelectedColor);
        mFillPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mStrokePaint.setAntiAlias(true);
        // selected text paint
        mSelectedTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mSelectedTextPaint.setTextSize(mTextSize);
        mSelectedTextPaint.setColor(mSelectedColor);
        mStrokePaint.setAntiAlias(true);
        // unselected text paint
        mUnselectedTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mUnselectedTextPaint.setTextSize(mTextSize);
        mUnselectedTextPaint.setColor(0xffffffff);
        mStrokePaint.setAntiAlias(true);
        mTextHeightOffset = -(mSelectedTextPaint.ascent() + mSelectedTextPaint.descent()) * 0.5f;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int defaultWidth = dp2px(DEFAULT_WIDTH_DP);
        int defaultHeight = dp2px(DEFAULT_HEIGHT_DP);
        setMeasuredDimension(getExpectSize(defaultWidth, widthMeasureSpec), getExpectSize(defaultHeight, heightMeasureSpec));
    }

    /**
     * get expect size
     */
    private int getExpectSize(int size, int measureSpec) {
        int result = size;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                result = size;
                break;
            case MeasureSpec.AT_MOST:
                result = Math.min(size, specSize);
                break;
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float left = mStrokeWidth * 0.5f;
        float top = mStrokeWidth * 0.5f;
        float right = mWidth - mStrokeWidth * 0.5f;
        float bottom = mHeight - mStrokeWidth * 0.5f;

        //draw rounded rectangle
        canvas.drawRoundRect(new RectF(left, top, right, bottom), mStrokeRadius, mStrokeRadius, mStrokePaint);

        //draw line
        for (int i = 0; i < mTabNum - 1; i++) {
            canvas.drawLine(perWidth * (i + 1), top, perWidth * (i + 1), bottom, mStrokePaint);
        }
        //draw tab and line
        for (int i = 0; i < mTabNum; i++) {
            String tabText = mTabTextList.get(i);
            float tabTextWidth = mSelectedTextPaint.measureText(tabText);
            if (i == mSelectedTab) {
                //draw selected tab
                if (i == 0) {
                    drawLeftPath(canvas, left, top, bottom);

                } else if (i == mTabNum - 1) {
                    drawRightPath(canvas, top, right, bottom);

                } else {
                    canvas.drawRect(new RectF(perWidth * i, top, perWidth * (i + 1), bottom), mFillPaint);
                }
                // draw selected text
                canvas.drawText(tabText, 0.5f * perWidth * (2 * i + 1) - 0.5f * tabTextWidth, mHeight * 0.5f + mTextHeightOffset, mUnselectedTextPaint);

            } else {
                //draw unselected text
                canvas.drawText(tabText, 0.5f * perWidth * (2 * i + 1) - 0.5f * tabTextWidth, mHeight * 0.5f + mTextHeightOffset, mSelectedTextPaint);
            }
        }
    }

    /**
     * draw right path
     */

    private void drawLeftPath(Canvas canvas, float left, float top, float bottom) {
        Path leftPath = new Path();
        leftPath.moveTo(left + mStrokeRadius, top);
        leftPath.lineTo(perWidth, top);
        leftPath.lineTo(perWidth, bottom);
        leftPath.lineTo(left + mStrokeRadius, bottom);
        leftPath.arcTo(new RectF(left, bottom - 2 * mStrokeRadius, left + 2 * mStrokeRadius, bottom), 90, 90);
        leftPath.lineTo(left, top + mStrokeRadius);
        leftPath.arcTo(new RectF(left, top, left + 2 * mStrokeRadius, top + 2 * mStrokeRadius), 180, 90);
        canvas.drawPath(leftPath, mFillPaint);
    }
    /**
     * draw left path
     */
    private void drawRightPath(Canvas canvas, float top, float right, float bottom) {
        Path rightPath = new Path();
        rightPath.moveTo(right - mStrokeRadius, top);
        rightPath.lineTo(right - perWidth, top);
        rightPath.lineTo(right - perWidth, bottom);
        rightPath.lineTo(right - mStrokeRadius, bottom);
        rightPath.arcTo(new RectF(right - 2 * mStrokeRadius, bottom - 2 * mStrokeRadius, right, bottom), 90, -90);
        rightPath.lineTo(right, top + mStrokeRadius);
        rightPath.arcTo(new RectF(right - 2 * mStrokeRadius, top, right, top + 2 * mStrokeRadius), 0, -90);
        canvas.drawPath(rightPath, mFillPaint);
    }


    /**
     * convert dp to px
     */
    protected int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    /**
     * convert sp to px
     */
    protected int sp2px(float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }

    /**
     * called after onMeasure
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        perWidth = mWidth / mTabNum;
        checkAttrs();
    }

    /**
     * check attribute whehere suitable
     */
    private void checkAttrs() {
        if (mStrokeRadius > 0.5f * mHeight) {
            mStrokeRadius = 0.5f * mHeight;
        }
    }

    /**
     * receive the event when touched
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            float x = event.getX();
            for (int i = 0; i < mTabNum; i++) {
                if (x > perWidth * i && x < perWidth * (i + 1)) {
                    if (mSelectedTab == i) {
                        return true;
                    }
                    mSelectedTab = i;
                    if (onSwitchListener != null) {
                        onSwitchListener.onSwitch(i, mTabTextList.get(i));
                    }
                }
            }
            invalidate();
        }
        return true;
    }

    /*=========================================Interface=========================================*/

    /**
     * called when swtiched
     */
    public interface OnSwitchListener {
        void onSwitch(int position, String tabText);
    }

    public void setOnSwitchListener(@NonNull OnSwitchListener onSwitchListener) {
        this.onSwitchListener = onSwitchListener;
    }

    /*=========================================Set and Get=========================================*/

    /**
     * get position of selected tab
     */
    public int getSelectedTab() {
        return mSelectedTab;
    }

    /**
     * set selected tab
     */
    public SwitchMultiButton setSelectedTab(int mSelectedTab) {
        this.mSelectedTab = mSelectedTab;
        invalidate();
        return this;
    }

    /**
     * set data for the switchbutton
     */
    public SwitchMultiButton setText(@NonNull List<String> list) {
        if (list.size() > 1) {
            this.mTabTextList = list;
            mTabNum = list.size();
            invalidate();
            return this;
        } else {
            throw new IllegalArgumentException("the size of list should greater then 1");
        }
    }
    /*======================================save and restore======================================*/

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("View", super.onSaveInstanceState());
        bundle.putFloat("StrokeRadius", mStrokeRadius);
        bundle.putFloat("StrokeWidth", mStrokeWidth);
        bundle.putFloat("TextSize", mTextSize);
        bundle.putInt("SelectedColor", mSelectedColor);
        bundle.putInt("SelectedTab", mSelectedTab);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            mStrokeRadius = bundle.getInt("StrokeRadius");
            mStrokeWidth = bundle.getInt("StrokeWidth");
            mTextSize = bundle.getInt("TextSize");
            mSelectedColor = bundle.getInt("SelectedColor");
            mSelectedTab = bundle.getInt("SelectedTab");
            super.onRestoreInstanceState(bundle.getParcelable("View"));
        } else {
            super.onRestoreInstanceState(state);
        }
    }
}
