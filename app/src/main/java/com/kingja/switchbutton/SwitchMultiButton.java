package com.kingja.switchbutton;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;

/**
 * 项目名称：切换按钮
 * 类描述：SwitchButton
 * 支持自定义属性：
 * 创建人：KingJA
 * 创建时间：2016/7/26 11:56
 * 修改备注：
 */
public class SwitchMultiButton extends View {
    private  final String TAG =getClass().getSimpleName() ;
    private Context context;
    private Paint mStrokePaint;
    private Paint mFillPaint;
    private int mWidth;
    private int mHeight;
    private TextPaint mLeftTextPaint;
    private TextPaint mRightTextPaint;
    private Rect mSelectedTextBounds;
    private Rect mNormalTextBounds;
    private OnSwitchListener onSwitchListener;
    private float mStrokeRadius;
    private float mStrokeWidth;
    private int mSelectedColor;
    private float mTextSize;
    private int mSelectedTab;
    private int mTabNum;
    private List<String> mSwitchTextList;
    private float perWidth;

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

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SwitchMultiButton);
        mStrokeRadius = typedArray.getDimension(R.styleable.SwitchMultiButton_strokeRadius, dp2px(10));
        mStrokeWidth = typedArray.getDimension(R.styleable.SwitchMultiButton_strokeWidth, dp2px(2));
        mTextSize = typedArray.getDimension(R.styleable.SwitchMultiButton_textSize, dp2px(14));
        mSelectedColor = typedArray.getColor(R.styleable.SwitchMultiButton_selectedColor, 0xffeb7b00);
        mSelectedTab = typedArray.getInteger(R.styleable.SwitchMultiButton_selectedTab, 0);
        mTabNum = typedArray.getInteger(R.styleable.SwitchMultiButton_tabNum, 2);
        typedArray.recycle();
    }


    private void initPaint() {
        // 创建描边画笔
        mStrokePaint = new Paint();
        mStrokePaint.setColor(mSelectedColor);
        mStrokePaint.setStyle(Paint.Style.STROKE);
        mStrokePaint.setAntiAlias(true);
        mStrokePaint.setStrokeWidth(mStrokeWidth);
        mStrokePaint.setStrokeJoin(Paint.Join.MITER);
        // 创建填充画笔
        mFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mFillPaint.setColor(mSelectedColor);
        mFillPaint.setStyle(Paint.Style.FILL);
        mStrokePaint.setAntiAlias(true);
        mFillPaint.setStrokeWidth(mStrokeWidth);
        // 选中文字画笔
        mLeftTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mLeftTextPaint.setTextSize(mTextSize);
        mLeftTextPaint.setColor(mSelectedColor);
        // 未选中文字画笔

        mRightTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mRightTextPaint.setTextSize(mTextSize);
        mRightTextPaint.setColor(0x000000);
        mSelectedTextBounds = new Rect();
        mNormalTextBounds = new Rect();


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        float DEFAULT_WIDTH_DP = 280f;
        float DEFAULT_HEIGHT_DP = 30f;
        int defaultWidth = dp2px(DEFAULT_WIDTH_DP);
        int defaultHeight = dp2px(DEFAULT_HEIGHT_DP);
        setMeasuredDimension(getMeasureSize(defaultWidth, widthMeasureSpec), getMeasureSize(defaultHeight, heightMeasureSpec));
    }

    public int getMeasureSize(int size, int measureSpec) {
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
        if (mStrokeRadius == 0) {
            mStrokeWidth=0;
        }
        float left = mStrokeWidth * 0.5f;
        float top = mStrokeWidth * 0.5f;
        float right = mWidth - mStrokeWidth * 0.5f;
        float bottom = mHeight - mStrokeWidth * 0.5f;
        canvas.drawRoundRect(new RectF(left, top, right, bottom), mStrokeRadius, mStrokeRadius, mStrokePaint);
        perWidth = mWidth / mTabNum;
        for (int i = 0; i < mTabNum - 1; i++) {
            canvas.drawLine(perWidth * (i + 1), top, perWidth * (i + 1), bottom, mStrokePaint);
        }

        for (int i = 0; i < mTabNum; i++) {
            if (i == mSelectedTab) {
                if (i == 0) {
                    drawLeftPath(canvas, left, top, bottom);

                } else if (i == mTabNum - 1) {

                    drawRightPath(canvas, top, right, bottom);
                } else {
                    canvas.drawRect(new RectF(perWidth * i, top, perWidth * (i + 1), bottom), mFillPaint);
                }

            }
        }

    }

    private void drawRightPath(Canvas canvas, float top, float right, float bottom) {
        Path rightPath = new Path();
        rightPath.moveTo(right - mStrokeRadius, top);
        rightPath.lineTo(right-perWidth, top);
        rightPath.lineTo(right-perWidth, bottom);
        rightPath.lineTo(right - mStrokeRadius, bottom);
        rightPath.arcTo(new RectF(right - 2 * mStrokeRadius, bottom - 2 * mStrokeRadius, right, bottom), 90, -90);
        rightPath.lineTo(right, top + mStrokeRadius);
        rightPath.arcTo(new RectF(right - 2 * mStrokeRadius, top, right, top + 2 * mStrokeRadius), 0, -90);
        canvas.drawPath(rightPath,mFillPaint);
    }

    private void drawLeftPath(Canvas canvas, float left, float top, float bottom) {
        Path leftPath = new Path();
        leftPath.moveTo(left + mStrokeRadius, top);
        leftPath.lineTo(perWidth, top);
        leftPath.lineTo(perWidth , bottom);
        leftPath.lineTo(left + mStrokeRadius, bottom);
        leftPath.arcTo(new RectF(left, bottom - 2 * mStrokeRadius, left + 2 * mStrokeRadius, bottom), 90, 90);
        leftPath.lineTo(left, top + mStrokeRadius);
        leftPath.arcTo(new RectF(left, top, left + 2 * mStrokeRadius, top + 2 * mStrokeRadius), 180, 90);
        canvas.drawPath(leftPath,mFillPaint);
    }


    protected int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    protected int sp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, dp, getResources().getDisplayMetrics());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }


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
                        onSwitchListener.onSwitch(i);
                    }
                }
            }
            invalidate();
        }
        return true;
    }

    public interface OnSwitchListener {
        void onSwitch(int position);
    }

    public void setOnSwitchListener(OnSwitchListener onSwitchListener) {
        this.onSwitchListener = onSwitchListener;
    }

    public void setText(List<String> list) {
        if (list != null && list.size() > 1) {
            this.mSwitchTextList = list;
            mTabNum = list.size();
            invalidate();
        }
    }
    public int getSelectedTab() {
        return mSelectedTab;
    }

    public void setSelectedTab(int mSelectedTab) {
        this.mSelectedTab = mSelectedTab;
        invalidate();
    }
}
