package com.customviews.customtablayout;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import com.R;


/**
 * @author Manish Kumar
 * @since 16/8/17
 */

/**
 * *This class is use for tab indicator set on screen
 */
public class CustomTabStrip extends LinearLayout {

    private static final int DEFAULT_BOTTOM_BORDER_THICKNESS_DIPS = 0;
    private static final byte DEFAULT_BOTTOM_BORDER_COLOR_ALPHA = 0x26;
    private static final int SELECTED_INDICATOR_THICKNESS_DIPS = 0;
    private static final int SELECTED_INDICATOR_PADDING_DIPS = 0;
    private static final int DEFAULT_SELECTED_INDICATOR_COLOR = 0xFF000000;

    private static final int DEFAULT_DIVIDER_THICKNESS_DIPS = 0;
    private static final byte DEFAULT_DIVIDER_COLOR_ALPHA = 0x20;
    private static final float DEFAULT_DIVIDER_HEIGHT = 0.0f;

    private final int mBottomBorderThickness;
    private final Paint mBottomBorderPaint;

    private final boolean mTriangleIndicatiorVisible;

    private final boolean mSelectedIndicatorFullHeight;
    private final int mSelectedIndicatorThickness;
    private final int mSelectedIndicatorPadding;
    private final Paint mSelectedIndicatorPaint;

    private final int mDefaultBottomBorderColor;

    private final Paint mDividerPaint;
    private final float mDividerHeight;
    private final SimpleTabColorizer mDefaultTabColorizer;
    private int mSelectedPosition;
    private float mSelectionOffset;
    private CustomTabLayout.TabColorizer mCustomTabColorizer;
    private Drawable stripDrawable;

    CustomTabStrip(Context context) {
        this(context, null);
    }

    CustomTabStrip(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        final float density = getResources().getDisplayMetrics().density;

        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.colorForeground, outValue, true);
        final int themeForegroundColor = outValue.data;

        mDefaultBottomBorderColor = setColorAlpha(themeForegroundColor,
                DEFAULT_BOTTOM_BORDER_COLOR_ALPHA);

        mDefaultTabColorizer = new SimpleTabColorizer();
        mDefaultTabColorizer.setIndicatorColors(DEFAULT_SELECTED_INDICATOR_COLOR);
        mDefaultTabColorizer.setDividerColors(setColorAlpha(themeForegroundColor,
                DEFAULT_DIVIDER_COLOR_ALPHA));

        mBottomBorderThickness = (int) (DEFAULT_BOTTOM_BORDER_THICKNESS_DIPS * density);
        mBottomBorderPaint = new Paint();
        mBottomBorderPaint.setColor(mDefaultBottomBorderColor);

        int defaultSelectedIndicatorThickness = (int) (SELECTED_INDICATOR_THICKNESS_DIPS * density);
        int defaultSelectedIndicatorPadding = (int) (SELECTED_INDICATOR_PADDING_DIPS * density);
        mSelectedIndicatorPaint = new Paint();

        mDividerHeight = DEFAULT_DIVIDER_HEIGHT;
        mDividerPaint = new Paint();
        mDividerPaint.setStrokeWidth((int) (DEFAULT_DIVIDER_THICKNESS_DIPS * density));

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomTabStrip, 0, 0);
        try {
            int indicatorColor = ta.getColor(R.styleable.CustomTabStrip_indicatorColor,
                    DEFAULT_SELECTED_INDICATOR_COLOR);
            mDefaultTabColorizer.setIndicatorColors(indicatorColor);
            mSelectedIndicatorThickness = ta.getDimensionPixelSize(R.styleable.CustomTabStrip_indicatorHeight,
                    defaultSelectedIndicatorThickness);
            mSelectedIndicatorPadding = ta.getDimensionPixelSize(R.styleable.CustomTabStrip_indicatorPadding,
                    defaultSelectedIndicatorPadding);
            mSelectedIndicatorFullHeight = ta.getBoolean(R.styleable.CustomTabStrip_fullHeightIndicator, false);
            mTriangleIndicatiorVisible = ta.getBoolean(R.styleable.CustomTabStrip_triangleIndicatorVisible, false);
            stripDrawable = ta.getDrawable(R.styleable.CustomTabStrip_indicatordrawable);

        } finally {
            ta.recycle();
        }
    }

    /**
     * Set the alpha value of the {@code color} to be the given {@code alpha} value.
     */
    private static int setColorAlpha(int color, byte alpha) {
        return Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color));
    }

    /**
     * Blend {@code color1} and {@code color2} using the given ratio.
     *
     * @param ratio of which to blend. 1.0 will return {@code color1}, 0.5 will give an even blend,
     *              0.0 will return {@code color2}.
     */
    public static int blendColors(int color1, int color2, float ratio) {
        final float inverseRation = 1f - ratio;
        float r = (Color.red(color1) * ratio) + (Color.red(color2) * inverseRation);
        float g = (Color.green(color1) * ratio) + (Color.green(color2) * inverseRation);
        float b = (Color.blue(color1) * ratio) + (Color.blue(color2) * inverseRation);
        return Color.rgb((int) r, (int) g, (int) b);
    }

    void setCustomTabColorizer(CustomTabLayout.TabColorizer customTabColorizer) {
        mCustomTabColorizer = customTabColorizer;
        invalidate();
    }

    void setSelectedIndicatorColors(int... colors) {
        // Make sure that the custom colorizer is removed
        mCustomTabColorizer = null;
        mDefaultTabColorizer.setIndicatorColors(colors);
        invalidate();
    }

    void setDividerColors(int... colors) {
        // Make sure that the custom colorizer is removed
        mCustomTabColorizer = null;
        mDefaultTabColorizer.setDividerColors(colors);
        invalidate();
    }

    void onViewPagerPageChanged(int position, float positionOffset) {
        mSelectedPosition = position;
        mSelectionOffset = positionOffset;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int height = getHeight();
        final int childCount = getChildCount();
        final int dividerHeightPx = (int) (Math.min(Math.max(0f, mDividerHeight), 1f) * height);
        final CustomTabLayout.TabColorizer tabColorizer = mCustomTabColorizer != null
                ? mCustomTabColorizer
                : mDefaultTabColorizer;

        // Thick colored underline below the current selection
        if (childCount > 0) {
            View selectedTitle = getChildAt(mSelectedPosition);
            int left = selectedTitle.getLeft();
            int right = selectedTitle.getRight();
            int color = tabColorizer.getIndicatorColor(mSelectedPosition);

            if (mSelectionOffset > 0f && mSelectedPosition < (getChildCount() - 1)) {
                int nextColor = tabColorizer.getIndicatorColor(mSelectedPosition + 1);
                if (color != nextColor) {
                    color = blendColors(nextColor, color, mSelectionOffset);
                }

                // Draw the selection partway between the tabs
                View nextTitle = getChildAt(mSelectedPosition + 1);
                left = (int) (mSelectionOffset * nextTitle.getLeft() +
                        (1.0f - mSelectionOffset) * left);
                right = (int) (mSelectionOffset * nextTitle.getRight() +
                        (1.0f - mSelectionOffset) * right);
            }
            if (mSelectedIndicatorPadding > 0) {
                left += mSelectedIndicatorPadding;
                right -= mSelectedIndicatorPadding;
            }

            mSelectedIndicatorPaint.setColor(color);
            int indicatorTop = height - mSelectedIndicatorThickness;
            if (mSelectedIndicatorFullHeight)
                indicatorTop = 0;
            if (stripDrawable != null) {
                stripDrawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
                stripDrawable.setBounds(left, indicatorTop, right, height);
                stripDrawable.draw(canvas);
            } else {
                canvas.drawRect(left, indicatorTop, right,
                        height, mSelectedIndicatorPaint);
            }


            if (mTriangleIndicatiorVisible) {
                Path path = new Path();
                float center = left + ((right - left) * 0.5f);
                float indicatorWidth = (right - left) * 0.25f;
                float indicatorHeight = indicatorWidth * 0.50f;
                float indicatorLeft = center - ((indicatorWidth * 0.5f));
                path.moveTo(indicatorLeft, height);
                path.lineTo(center, height + indicatorHeight);
                path.lineTo(indicatorLeft + indicatorWidth, height);
                path.close();
                canvas.drawPath(path, mSelectedIndicatorPaint);
            }
        }

        // Thin underline along the entire bottom edge
        // canvas.drawRect(0, height - mBottomBorderThickness, getWidth(), height, mBottomBorderPaint);

        // Vertical separators between the titles
        int separatorTop = (height - dividerHeightPx) / 2;
        for (int i = 0; i < childCount - 1; i++) {
            View child = getChildAt(i);
            mDividerPaint.setColor(tabColorizer.getDividerColor(i));
            canvas.drawLine(child.getRight(), separatorTop, child.getRight(),
                    separatorTop + dividerHeightPx, mDividerPaint);
        }
    }

    private static class SimpleTabColorizer implements CustomTabLayout.TabColorizer {
        private int[] mIndicatorColors;
        private int[] mDividerColors;

        @Override
        public final int getIndicatorColor(int position) {
            return mIndicatorColors[position % mIndicatorColors.length];
        }

        @Override
        public final int getDividerColor(int position) {
            return mDividerColors[position % mDividerColors.length];
        }

        void setIndicatorColors(int... colors) {
            mIndicatorColors = colors;
        }

        void setDividerColors(int... colors) {
            mDividerColors = colors;
        }
    }
}
