package com.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.R;

public class CircularProgressView extends View {

    Paint bgPaint;
    Paint progressPaint;
    Paint progressFillPaint;
    Paint textPaint;
    Paint textPaintSelected;

    float radius;
    float sweapAngle;

    RectF rectF = new RectF();
    RectF mTextRange = new RectF();

    float progress = 0.0f;
    float maxProgress = 11.0f;

    public CircularProgressView(Context context) {
        super(context);
    }

    public CircularProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inIt();
    }

    public CircularProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inIt();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CircularProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        inIt();
    }

    private void inIt() {
        sweapAngle = 360.0f / maxProgress;

        bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        textPaint = new Paint();
        textPaint.setFakeBoldText(false);
        textPaint.setColor(getContext().getResources().getColor(R.color.colorAppBlack));
        textPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 7,
                getResources().getDisplayMetrics()));

        textPaintSelected = new Paint();
        textPaintSelected.setFakeBoldText(false);
        textPaintSelected.setColor(getContext().getResources().getColor(R.color.colorWhite));
        textPaintSelected.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 7,
                getResources().getDisplayMetrics()));


        bgPaint.setColor(getContext().getResources().getColor(R.color.colorAppBlack));
        bgPaint.setStyle(Paint.Style.FILL);

        progressPaint.setColor(getContext().getResources().getColor(R.color.colorWhite));
        progressPaint.setStyle(Paint.Style.FILL);

        progressFillPaint.setColor(getContext().getResources().getColor(R.color.colorActivateGreen));
        progressFillPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        rectF.set(0, 0, (right-left), (bottom-top));
        mTextRange.set(0, 0, (right-left), (bottom-top));
        mTextRange.inset(rectF.width() * 0.20f, rectF.width() * 0.20f);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int min = Math.min(w, h);
        radius = min * 0.5f;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (rectF.width() == 0){
            return;
        }
        float widthCenter = getWidth() * 0.5f;
        float heightCenter = getHeight() * 0.5f;
        canvas.drawCircle(widthCenter, heightCenter, radius, bgPaint);

        float tmpAngle = -89;
        for (float i = 1; i <= maxProgress; i += 1) {
            canvas.drawArc(rectF, tmpAngle, sweapAngle, true, progressPaint);
            if (i == maxProgress) {
                drawText(canvas, tmpAngle, sweapAngle, String.valueOf((int)maxProgress), textPaint);
            }
            tmpAngle += sweapAngle;

        }

        float tmpAngle1 = -89;
        for (float i = 1; i <= progress; i += 1) {
            canvas.drawArc(rectF, tmpAngle1, sweapAngle, true, progressFillPaint);
            if (i == progress) {
                drawText(canvas, tmpAngle1, sweapAngle, String.valueOf((int)progress), textPaintSelected);
            }
            tmpAngle1 += sweapAngle;
        }

    }


    private void drawText(Canvas canvas, float tmpAngle, float sweepAngle, String mStr, Paint textPaint) {
        Path path = new Path();
        path.addArc(mTextRange, tmpAngle, sweepAngle);

        float textWidth = textPaint.measureText(mStr);
        float hOffset = (float) (mTextRange.width() * ((Math.PI / 11) * 0.5f));
        hOffset -= (textWidth * 0.50f);

        float vOffset = 0.0f;

        canvas.drawTextOnPath(mStr, path, hOffset, vOffset, textPaint);
    }

    public void setProgress(float progress) {
        this.progress = progress;
        invalidate();
    }
}
