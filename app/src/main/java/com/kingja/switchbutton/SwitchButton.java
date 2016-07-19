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
import android.view.MotionEvent;
import android.view.View;

/**
 * 项目名称：
 * 类描述：SwitchButton(切换按钮)
 * 支持自定义属性：
 * 选中颜色:selected_color
 * 左边文字:left_text
 * 右边文字:right_text
 * 描边宽度:stroke_width
 * 描边圆角:stroke_radius
 * 创建人：KingJA
 * 创建时间：2016/7/18 20:56
 * 修改备注：
 * 7/19增加自定义属性
 */
public class SwitchButton extends View {
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
    private int mUnselectedColor;
    private String mLeftText;
    private String mRightText;
    private boolean isLeft = true;
    private float mTextSize;
    private int mSelected;

    public SwitchButton(Context context) {
        this(context, null);
    }

    public SwitchButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwitchButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SwitchButton);
        mStrokeRadius = typedArray.getDimension(R.styleable.SwitchButton_stroke_radius, dp2px(14));
        mStrokeWidth = typedArray.getDimension(R.styleable.SwitchButton_stroke_width, dp2px(2));
        mTextSize = typedArray.getDimension(R.styleable.SwitchButton_text_size, dp2px(14));
        mSelectedColor = typedArray.getColor(R.styleable.SwitchButton_selected_color, 0xff4c8b4f);
        mUnselectedColor = typedArray.getColor(R.styleable.SwitchButton_unselected_color, 0xffffffff);
        mLeftText = typedArray.getString(R.styleable.SwitchButton_left_text);
        mRightText = typedArray.getString(R.styleable.SwitchButton_right_text);
        mSelected = typedArray.getInteger(R.styleable.SwitchButton_selected, 0);
        isLeft = mSelected == 0;
        mLeftText = mLeftText == null ? "LEFT" : mLeftText;
        mRightText = mRightText == null ? "RIGHT" : mRightText;
        typedArray.recycle();
        init();
    }

    private void init() {
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
        mFillPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mStrokePaint.setAntiAlias(true);
        mFillPaint.setStrokeWidth(mStrokeWidth);
        // 选中文字画笔
        mLeftTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mLeftTextPaint.setTextSize(mTextSize);
        mLeftTextPaint.setColor(mSelectedColor);
        // 未选中文字画笔
        mRightTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mRightTextPaint.setTextSize(mTextSize);
        mRightTextPaint.setColor(mUnselectedColor);

        mSelectedTextBounds = new Rect();
        mNormalTextBounds = new Rect();


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        float DEFAULT_WIDTH_DP = 280f;
        float DEFAULT_HEIGHT_DP = 30f;
        int defaultWidth = dp2px(DEFAULT_WIDTH_DP) + getPaddingLeft() + getPaddingRight();
        int defaultHeight = dp2px(DEFAULT_HEIGHT_DP) + getPaddingTop() + getPaddingBottom();
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
            mStrokeWidth = 0;
        }
        float left = mStrokeWidth * 0.5f;
        float top = mStrokeWidth * 0.5f;
        float right = mWidth - mStrokeWidth * 0.5f;
        float bottom = mHeight - mStrokeWidth * 0.5f;
        Path leftPath = new Path();
        leftPath.moveTo(left + mStrokeRadius, top);
        leftPath.lineTo(mWidth * 0.5f, top);
        leftPath.lineTo(mWidth * 0.5f, bottom);
        leftPath.lineTo(left + mStrokeRadius, bottom);
        leftPath.arcTo(new RectF(left, bottom - 2 * mStrokeRadius, left + 2 * mStrokeRadius, bottom), 90, 90);
        leftPath.lineTo(left, top + mStrokeRadius);
        leftPath.arcTo(new RectF(left, top, left + 2 * mStrokeRadius, top + 2 * mStrokeRadius), 180, 90);
        canvas.drawPath(leftPath, isLeft ? mFillPaint : mStrokePaint);
        Path rightPath = new Path();
        rightPath.moveTo(right - mStrokeRadius, top);
        rightPath.lineTo(mWidth * 0.5f, top);
        rightPath.lineTo(mWidth * 0.5f, bottom);
        rightPath.lineTo(right - mStrokeRadius, bottom);
        rightPath.arcTo(new RectF(right - 2 * mStrokeRadius, bottom - 2 * mStrokeRadius, right, bottom), 90, -90);
        rightPath.lineTo(right, top + mStrokeRadius);
        rightPath.arcTo(new RectF(right - 2 * mStrokeRadius, top, right, top + 2 * mStrokeRadius), 0, -90);
        canvas.drawPath(rightPath, isLeft ? mStrokePaint : mFillPaint);
        mLeftTextPaint.setColor(isLeft ? mUnselectedColor : mSelectedColor);
        mLeftTextPaint.getTextBounds(mLeftText, 0, mLeftText.length(), mSelectedTextBounds);
        canvas.drawText(mLeftText, mWidth * 0.25f - mSelectedTextBounds.width() * 0.5f, mHeight * 0.5f - (mLeftTextPaint.ascent() + mLeftTextPaint.descent()) * 0.5f, mLeftTextPaint);
        mRightTextPaint.setColor(!isLeft ? mUnselectedColor : mSelectedColor);
        mRightTextPaint.getTextBounds(mRightText, 0, mRightText.length(), mNormalTextBounds);
        canvas.drawText(mRightText, mWidth * 0.75f - mNormalTextBounds.width() * 0.5f, mHeight * 0.5f - (mRightTextPaint.ascent() + mRightTextPaint.descent()) * 0.5f, mRightTextPaint);
    }

    /**
     * dp转px
     */
    private int dp2px(float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 获取测量过的尺寸
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }


    /**
     * 接收点击事件
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (event.getX() > 0 && event.getX() < mWidth / 2) {
                if (isLeft) {
                    return true;
                }
                isLeft = true;
            } else {
                if (!isLeft) {
                    return true;
                }
                isLeft = false;
            }
            invalidate();
            if (onSwitchListener != null) {
                onSwitchListener.onSwitch(isLeft);
            }

        }
        return true;
    }

    public interface OnSwitchListener {
        void onSwitch(boolean isLeft);
    }

    /**
     * 设置选择监听器
     */
    public void setOnSwitchListener(OnSwitchListener onSwitchListener) {
        this.onSwitchListener = onSwitchListener;
    }
}
