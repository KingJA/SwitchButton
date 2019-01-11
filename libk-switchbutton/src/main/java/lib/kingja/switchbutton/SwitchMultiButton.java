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
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Description  A smart switchable button,support multiple tabs.
 * Create Time  2016/7/27 14:57
 * Author   KingJA
 * Email    kingjavip@gmail.com
 */
public class SwitchMultiButton extends View {

    private static final String TAG = "SwitchMultiButton";
    /*default value*/
    private String[] mTabTexts = {"L", "R"};
    private int mTabNum = mTabTexts.length;
    private static final float STROKE_RADIUS = 0;
    private static final float STROKE_WIDTH = 2;
    private static final float TEXT_SIZE = 14;
    private static final int SELECTED_COLOR = 0xffeb7b00;
    private static final int DISABLE_COLOR = 0xffcccccc;
    private static final int ENABLE_COLOR = 0xffffffff;
    private static final int SELECTED_TAB = 0;
    private static final String FONTS_DIR = "fonts/";
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
    private int mDisableColor;
    private float mTextSize;
    private int mSelectedTab;
    private float perWidth;
    private float mTextHeightOffset;
    private Paint.FontMetrics mFontMetrics;
    private Typeface typeface;
    private boolean mEnable = true;


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
     *
     * @param context
     * @param attrs
     */
    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SwitchMultiButton);
        mStrokeRadius = typedArray.getDimension(R.styleable.SwitchMultiButton_strokeRadius, STROKE_RADIUS);
        mStrokeWidth = typedArray.getDimension(R.styleable.SwitchMultiButton_strokeWidth, STROKE_WIDTH);
        mTextSize = typedArray.getDimension(R.styleable.SwitchMultiButton_textSize, TEXT_SIZE);
        mSelectedColor = typedArray.getColor(R.styleable.SwitchMultiButton_selectedColor, SELECTED_COLOR);
        mDisableColor = typedArray.getColor(R.styleable.SwitchMultiButton_disableColor, DISABLE_COLOR);
        mSelectedTab = typedArray.getInteger(R.styleable.SwitchMultiButton_selectedTab, SELECTED_TAB);
        String mTypeface = typedArray.getString(R.styleable.SwitchMultiButton_typeface);
        int mSwitchTabsResId = typedArray.getResourceId(R.styleable.SwitchMultiButton_switchTabs, 0);
        if (mSwitchTabsResId != 0) {
            mTabTexts = getResources().getStringArray(mSwitchTabsResId);
            mTabNum = mTabTexts.length;
        }
        if (!TextUtils.isEmpty(mTypeface)) {
            typeface = Typeface.createFromAsset(context.getAssets(), FONTS_DIR + mTypeface);
        }
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
        mSelectedTextPaint.setColor(0xffffffff);
        mStrokePaint.setAntiAlias(true);
        // unselected text paint
        mUnselectedTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mUnselectedTextPaint.setTextSize(mTextSize);
        mUnselectedTextPaint.setColor(mSelectedColor);
        mStrokePaint.setAntiAlias(true);
        mTextHeightOffset = -(mSelectedTextPaint.ascent() + mSelectedTextPaint.descent()) * 0.5f;
        mFontMetrics = mSelectedTextPaint.getFontMetrics();
        if (typeface != null) {
            mSelectedTextPaint.setTypeface(typeface);
            mUnselectedTextPaint.setTypeface(typeface);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int defaultWidth = getDefaultWidth();
        int defaultHeight = getDefaultHeight();
        setMeasuredDimension(getExpectSize(defaultWidth, widthMeasureSpec), getExpectSize(defaultHeight,
                heightMeasureSpec));
    }

    /**
     * get default height when android:layout_height="wrap_content"
     */
    private int getDefaultHeight() {
        return (int) (mFontMetrics.bottom - mFontMetrics.top) + getPaddingTop() + getPaddingBottom();
    }

    /**
     * get default width when android:layout_width="wrap_content"
     */
    private int getDefaultWidth() {
        float tabTextWidth = 0f;
        int tabs = mTabTexts.length;
        for (int i = 0; i < tabs; i++) {
            tabTextWidth = Math.max(tabTextWidth, mSelectedTextPaint.measureText(mTabTexts[i]));
        }
        float totalTextWidth = tabTextWidth * tabs;
        float totalStrokeWidth = (mStrokeWidth * tabs);
        int totalPadding = (getPaddingRight() + getPaddingLeft()) * tabs;
        return (int) (totalTextWidth + totalStrokeWidth + totalPadding);
    }


    /**
     * get expect size
     *
     * @param size
     * @param measureSpec
     * @return
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
            default:
                break;
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!mEnable) {
            mStrokePaint.setColor(mDisableColor);
            mFillPaint.setColor(mDisableColor);
            mSelectedTextPaint.setColor(ENABLE_COLOR);
            mUnselectedTextPaint.setColor(mDisableColor);
        }
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
            String tabText = mTabTexts[i];
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
                canvas.drawText(tabText, 0.5f * perWidth * (2 * i + 1) - 0.5f * tabTextWidth, mHeight * 0.5f +
                        mTextHeightOffset, mSelectedTextPaint);
            } else {
                //draw unselected text
                canvas.drawText(tabText, 0.5f * perWidth * (2 * i + 1) - 0.5f * tabTextWidth, mHeight * 0.5f +
                        mTextHeightOffset, mUnselectedTextPaint);
            }
        }
    }

    /**
     * draw right path
     *
     * @param canvas
     * @param left
     * @param top
     * @param bottom
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
     *
     * @param canvas
     * @param top
     * @param right
     * @param bottom
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
     * called after onMeasure
     *
     * @param w
     * @param h
     * @param oldw
     * @param oldh
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
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mEnable) {
            return true;
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            float x = event.getX();
            for (int i = 0; i < mTabNum; i++) {
                if (x > perWidth * i && x < perWidth * (i + 1)) {
                    if (mSelectedTab == i) {
                        return true;
                    }
                    mSelectedTab = i;
                    if (onSwitchListener != null) {
                        onSwitchListener.onSwitch(i, mTabTexts[i]);
                    }
                }
            }
            invalidate();
        }
        return true;
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (enabled == isEnabled()) {
            return;
        }
        mEnable = enabled;
        invalidate();
    }

    @Override
    public boolean isEnabled() {
        return mEnable;
    }
    /*=========================================Interface=========================================*/

    /**
     * called when swtiched
     */
    public interface OnSwitchListener {
        void onSwitch(int position, String tabText);
    }

    public SwitchMultiButton setOnSwitchListener(OnSwitchListener onSwitchListener) {
        this.onSwitchListener = onSwitchListener;
        return this;
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
     *
     * @param mSelectedTab
     * @return
     */
    public SwitchMultiButton setSelectedTab(int mSelectedTab) {
        this.mSelectedTab = mSelectedTab;
        invalidate();
        if (onSwitchListener != null) {
            onSwitchListener.onSwitch(mSelectedTab, mTabTexts[mSelectedTab]);
        }
        return this;
    }

    public void clearSelection() {
        this.mSelectedTab = -1;
        invalidate();
    }

    /**
     * set data for the switchbutton
     *
     * @param tagTexts
     * @return
     */
    public SwitchMultiButton setText(String... tagTexts) {
        if (tagTexts.length > 1) {
            this.mTabTexts = tagTexts;
            mTabNum = tagTexts.length;
            requestLayout();
            return this;
        } else {
            throw new IllegalArgumentException("the size of tagTexts should greater then 1");
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
        bundle.putInt("DisableColor", mDisableColor);
        bundle.putInt("SelectedTab", mSelectedTab);
        bundle.putBoolean("Enable", mEnable);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            mStrokeRadius = bundle.getFloat("StrokeRadius");
            mStrokeWidth = bundle.getFloat("StrokeWidth");
            mTextSize = bundle.getFloat("TextSize");
            mSelectedColor = bundle.getInt("SelectedColor");
            mDisableColor = bundle.getInt("DisableColor");
            mSelectedTab = bundle.getInt("SelectedTab");
            mEnable = bundle.getBoolean("Enable");
            super.onRestoreInstanceState(bundle.getParcelable("View"));
        } else {
            super.onRestoreInstanceState(state);
        }
    }
}
