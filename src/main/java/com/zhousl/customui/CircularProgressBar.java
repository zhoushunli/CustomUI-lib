package com.zhousl.customui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.zhousl.customui.utils.UIUtil;

/**
 * Created by inshot-user on 2017/7/17.
 */

public class CircularProgressBar extends View {

    private Paint mPaint;
    private int sweepAngle;
    private int backgroundLineWidth;
    private int progressLineWidth;
    private int backgroundColor;
    private int progressColor;
    private int maxValue;
    private int progress;
    private int width;
    private int height;
    private RectF bounds;
    private onProgressChangeListener onProgressChangeListener;

    interface onProgressChangeListener {
        void onProgressChanged(int progress, CircularProgressBar progressBar);
    }

    public CircularProgressBar(Context context) {
        this(context, null);
    }

    public CircularProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CircularProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CircularProgressBar);
        sweepAngle = ta.getInteger(R.styleable.CircularProgressBar_sweepAngle, 300);
        backgroundLineWidth = ta.getDimensionPixelSize(R.styleable.CircularProgressBar_backgroundLineWidth, UIUtil.dp2px(5, getContext()));
        progressLineWidth = ta.getDimensionPixelSize(R.styleable.CircularProgressBar_progressLineWidth, UIUtil.dp2px(5, getContext()));
        backgroundColor = ta.getColor(R.styleable.CircularProgressBar_backgroundColor, Color.GRAY);
        progressColor = ta.getColor(R.styleable.CircularProgressBar_progressColor, getResources().getColor(R.color.colorAccent));
        maxValue = ta.getInteger(R.styleable.CircularProgressBar_maxValue, 100);
        progress = ta.getInteger(R.styleable.CircularProgressBar_progress, 0);
        ta.recycle();
        this.setMax(maxValue);
        this.setProgress(progress);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureDimen(widthMeasureSpec), measureDimen(heightMeasureSpec));
    }

    private int measureDimen(int measureSpec) {
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            return size;
        } else {
            return UIUtil.dp2px(100, getContext());
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        bounds = new RectF();
        bounds.set(0, 0, width, height);
        bounds.inset(Math.min(backgroundLineWidth, progressLineWidth) / 2, Math.min(backgroundLineWidth, progressLineWidth) / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBg(canvas);
        drawProgress(canvas);
    }

    private void drawBg(Canvas canvas) {
        mPaint.setColor(backgroundColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(backgroundLineWidth);
        canvas.drawArc(bounds, 90 + (360 - sweepAngle) / 2, sweepAngle, false, mPaint);
    }

    private void drawProgress(Canvas canvas) {
        mPaint.setColor(progressColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(progressLineWidth);
        float sweepAngle = this.sweepAngle * 1.0f * progress / maxValue;
        canvas.drawArc(bounds, 90 + (360 - this.sweepAngle) / 2, sweepAngle, false, mPaint);
    }

    public void setMax(int maxValue) {
        this.maxValue = maxValue;
    }

    public int getMax() {
        return maxValue;
    }

    public void setProgress(int progress) {
        if (progress<0)
            progress=0;
        if (progress>maxValue)
            progress=maxValue;
        this.progress = progress;
        invalidate();
        if (onProgressChangeListener != null) {
            onProgressChangeListener.onProgressChanged(this.progress,this);
        }
    }

    public int getProgress() {
        return progress;
    }

    public void setOnProgressChangeListener(CircularProgressBar.onProgressChangeListener onProgressChangeListener) {
        this.onProgressChangeListener = onProgressChangeListener;
    }
}
