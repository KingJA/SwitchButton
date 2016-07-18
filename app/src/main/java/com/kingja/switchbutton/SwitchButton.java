package com.kingja.switchbutton;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * 项目名称：常用工具类
 * 类描述：TODO
 * 创建人：KingJA
 * 创建时间：2016/7/1820:56
 * 修改备注：
 */
public class SwitchButton extends View {
    private static final String TAG = "SwitchButton";
    private Context context;
    private final int DEFAULT_WIDTH_DP=280;
    private final int DEFAULT_HEIGHT_DP=50;
    private Paint mStrokePaint;
    private Paint mFillPaint;
    private int width;
    private int measuredwidth;
    private int measuredheight;
    private int mBorderWidth;
    private int mRadius;
    private TextPaint mSelectedTextPaint;
    private TextPaint mNormalTextPaint;
    private Rect mSelectedTextBounds;
    private Rect mNormalTextBounds;

    public SwitchButton(Context context) {
        this(context,null);
    }

    public SwitchButton(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SwitchButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        mBorderWidth = dip2px(2);
        mRadius = dip2px(14);
        // 创建描边画笔
        mStrokePaint = new Paint();
        mStrokePaint.setColor(context.getResources().getColor(R.color.background));
        mStrokePaint.setStyle(Paint.Style.STROKE);
        mStrokePaint.setAntiAlias(true);
        mStrokePaint.setStrokeWidth(mBorderWidth);
        mStrokePaint.setStrokeJoin(Paint.Join.MITER);
        // 创建填充画笔
        mFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mFillPaint.setColor(context.getResources().getColor(R.color.background));
        mFillPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mStrokePaint.setAntiAlias(true);
        mFillPaint.setStrokeWidth(mBorderWidth);
        // 选中画笔
        mSelectedTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mSelectedTextPaint.setTextSize(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 14, getResources()
                        .getDisplayMetrics()));
        mSelectedTextPaint.setColor(context.getResources().getColor(R.color.white));
        // 未选中画笔
        mNormalTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mNormalTextPaint.setTextSize(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 14, getResources()
                        .getDisplayMetrics()));
        mNormalTextPaint.setColor(context.getResources().getColor(R.color.background));

        mSelectedTextBounds = new Rect();
        mNormalTextBounds = new Rect();




    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int defaultWidth= dip2px(DEFAULT_WIDTH_DP)+getPaddingLeft()+getPaddingRight();
        int defaultHeight= dip2px(DEFAULT_HEIGHT_DP)+getPaddingTop()+getPaddingBottom();
        setMeasuredDimension(getMeasureSize(defaultWidth,widthMeasureSpec),getMeasureSize(defaultHeight,heightMeasureSpec));
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
                result=Math.min(size,specSize);
                break;
        }
        return result;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int left = mBorderWidth / 2;
        int top = mBorderWidth / 2;
        int right = measuredwidth - mBorderWidth / 2;
        int bottom = measuredheight - mBorderWidth / 2;

        Path leftPath = new Path();
        leftPath.moveTo(left + mRadius, top);
        leftPath.lineTo(measuredwidth/2, top);
        leftPath.lineTo(measuredwidth/2, bottom);
        leftPath.lineTo(left + mRadius, bottom);
        leftPath.arcTo(new RectF(left, bottom-2 * mRadius, left + 2 * mRadius, bottom),
                90, 90);
        leftPath.lineTo(left, top+mRadius);
        leftPath.arcTo(new RectF(left, top, left + 2 * mRadius, top+ 2 * mRadius),
                180, 90);

        canvas.drawPath(leftPath, mFillPaint);

        Path rightPath = new Path();
        rightPath.moveTo(right-mRadius, top);
        rightPath.lineTo(measuredwidth/2, top);
        rightPath.lineTo(measuredwidth/2, bottom);
        rightPath.lineTo(right- mRadius, bottom);
        rightPath.arcTo(new RectF(right-2 * mRadius, bottom-2 * mRadius, right, bottom),
                90, -90);
        rightPath.lineTo(right, top+mRadius);
        rightPath.arcTo(new RectF(right-2 * mRadius, top, right, top+ 2 * mRadius),
                0, -90);

        canvas.drawPath(rightPath, mStrokePaint);


        mSelectedTextPaint.getTextBounds("选中", 0,
                "选中".length(), mSelectedTextBounds);


        canvas.drawText("选中", (int) (measuredwidth *0.25f - mSelectedTextBounds.width() / 2.0), (int) (measuredheight / 2.0 - (mSelectedTextPaint.ascent() + mSelectedTextPaint.descent()) / 2.0f), mSelectedTextPaint);
        mNormalTextPaint.getTextBounds("未选中", 0,
                "未选中".length(), mNormalTextBounds);

        canvas.drawText("未选中", (int) (measuredwidth *0.75f - mNormalTextBounds.width() / 2.0), (int) (measuredheight / 2.0 - (mNormalTextPaint.ascent() + mNormalTextPaint.descent()) / 2.0f), mNormalTextPaint);
    }

    private  int dip2px(float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        measuredwidth = getMeasuredWidth();
        measuredheight = getMeasuredHeight();
    }
}
