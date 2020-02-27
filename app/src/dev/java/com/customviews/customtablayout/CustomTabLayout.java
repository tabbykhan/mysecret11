package com.customviews.customtablayout;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.R;
import com.fonts.FontUtils;

import java.util.ArrayList;

/**
 * @author Manish Kumar
 * @since 16/8/17
 */

/**
 * This class is use for setup tab on screen
 */
public class CustomTabLayout extends HorizontalScrollView {

    private static final int TITLE_OFFSET_DIPS = 0;
    private static final int TAB_VIEW_PADDING_DIPS = 0;
    private static final int TAB_VIEW_TEXT_SIZE_SP = 12;
    private final CustomTabStrip mTabStrip;
    private final ArrayList<TextView> mTextViews = new ArrayList<TextView>();
    private boolean mShouldExpand;
    private int mTitleOffset;
    private int mTitlePaddingLeft;
    private int mTitlePaddingTop;
    private int mTitlePaddingRight;
    private int mTitlePaddingBottom;
    private float mTitleTextSize;
    private Typeface mTitleTypeFace;
    private int defTextColor, selTextColor, mLastPos = 0;
    private int mTabViewLayoutId;
    private int mTabViewTextViewId;
    private int mTextIndicator;
    private ViewPager mViewPager;
    private ViewPager.OnPageChangeListener mViewPagerPageChangeListener;

    public CustomTabLayout(Context context) {
        this(context, null);
    }

    public CustomTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomTabLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        // Disable the Scroll Bar
        setHorizontalScrollBarEnabled(false);
        // Make sure that the Tab Strips fills this View
        setFillViewport(true);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomTabLayout, 0, 0);
        try {
            mTitlePaddingLeft = ta.getDimensionPixelSize(R.styleable.CustomTabLayout_defaultPaddingLeft, 0);
            mTitlePaddingTop = ta.getDimensionPixelSize(R.styleable.CustomTabLayout_defaultPaddingTop, 0);
            mTitlePaddingRight = ta.getDimensionPixelSize(R.styleable.CustomTabLayout_defaultPaddingRight, 0);
            mTitlePaddingBottom = ta.getDimensionPixelSize(R.styleable.CustomTabLayout_defaultPaddingBottom, 0);
            mTitleTextSize = ta.getDimension(R.styleable.CustomTabLayout_defaultTextSize, TAB_VIEW_TEXT_SIZE_SP);
            String fontName = ta.getString(R.styleable.TypefaceTextView_custom_font);
            mTitleTypeFace = FontUtils.getInstance().getFont(context, fontName);
            defTextColor = ta.getColor(R.styleable.CustomTabLayout_defaultTextColor, Color.BLACK);
            selTextColor = ta.getColor(R.styleable.CustomTabLayout_selTextColor, Color.CYAN);
            setShouldExpand(ta.getBoolean(R.styleable.CustomTabLayout_shouldExpand, false));
        } finally {
            ta.recycle();
        }
        mTitleOffset = (int) (TITLE_OFFSET_DIPS * getResources().getDisplayMetrics().density);

        mTabStrip = new CustomTabStrip(context, attrs);

        addView(mTabStrip, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    }

    public CustomTabStrip getTabStrip() {
        return mTabStrip;
    }

    private ArrayList<TextView> getTextViews() {
        return mTextViews;
    }

    public void setShouldExpand(boolean b) {
        mShouldExpand = b;
    }

    /**
     * Set the custom {@link TabColorizer} to be used.
     * <p/>
     * If you only require simple custmisation then you can use
     * {@link #setSelectedIndicatorColors(int...)} and {@link #setDividerColors(int...)} to achieve
     * similar effects.
     */
    public void setCustomTabColorizer(TabColorizer tabColorizer) {
        mTabStrip.setCustomTabColorizer(tabColorizer);
    }

    /**
     * Sets the colors to be used for indicating the selected tab. These colors are treated as a
     * circular array. Providing one color will mean that all tabs are indicated with the same color.
     */
    public void setSelectedIndicatorColors(int... colors) {
        mTabStrip.setSelectedIndicatorColors(colors);
    }

    /**
     * Sets the colors to be used for tab dividers. These colors are treated as a circular array.
     * Providing one color will mean that all tabs are indicated with the same color.
     */
    public void setDividerColors(int... colors) {
        mTabStrip.setDividerColors(colors);
    }

    /**
     * Set the {@link ViewPager.OnPageChangeListener}. When using {@link CustomTabLayout} you are
     * required to set any {@link ViewPager.OnPageChangeListener} through this method. This is so
     * that the item_choose_enter_free_adapter can update it's scroll position correctly.
     *
     * @see ViewPager#setOnPageChangeListener(ViewPager.OnPageChangeListener)
     */
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        mViewPagerPageChangeListener = listener;
    }

    /**
     * Set the custom item_choose_enter_free_adapter to be inflated for the tab views.
     *
     * @param layoutResId Layout id to be inflated
     * @param textViewId  id of the {@link TextView} in the inflated view
     */
    public void setCustomTabView(int layoutResId, int textViewId) {
        mTabViewLayoutId = layoutResId;
        mTabViewTextViewId = textViewId;
    }

    /**
     * Sets the associated view pager. Note that the assumption here is that the pager content
     * (number of tabs and tab titles) does not change after this call has been made.
     */
    public void setViewPager(ViewPager viewPager) {
        mTabStrip.removeAllViews();

        mViewPager = viewPager;
        if (viewPager != null) {
            viewPager.addOnPageChangeListener(new InternalViewPagerListener());
            populateTabStrip();
        }
    }

    /**
     * Create a default view to be used for tabs. This is called if a custom tab view is not set via
     * {@link #setCustomTabView(int, int)}.
     */
    protected TextView createDefaultTabView(Context context) {
        TextView textView = new TextView(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setGravity(Gravity.CENTER);
        textView.setLayoutParams(lp);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTitleTextSize);
        if (mTitleTypeFace != null)
            textView.setTypeface(mTitleTypeFace);


        textView.setPadding(mTitlePaddingLeft, mTitlePaddingTop, mTitlePaddingRight, mTitlePaddingBottom);
        if (mShouldExpand) {
            lp = (LinearLayout.LayoutParams) textView.getLayoutParams();
//            lp.width=0;
            lp.weight = 1;
        }

        return textView;
    }

    private void populateTabStrip() {
        final PagerAdapter adapter = mViewPager.getAdapter();
        final OnClickListener tabClickListener = new TabClickListener();

        for (int i = 0; i < adapter.getCount(); i++) {
            View tabView = null;
            TextView tabTitleView = null;

            if (mTabViewLayoutId != 0) {
                // If there is a custom tab view item_choose_enter_free_adapter id set, try and inflate it
                tabView = LayoutInflater.from(getContext()).inflate(mTabViewLayoutId, mTabStrip,
                        false);
                tabTitleView = (TextView) tabView.findViewById(mTabViewTextViewId);
            }

            if (tabView == null) {
                tabView = createDefaultTabView(getContext());
            }

            if (tabTitleView == null && TextView.class.isInstance(tabView)) {
                tabTitleView = (TextView) tabView;
            }
            if (i == 0)
                tabTitleView.setTextColor(selTextColor);
            else
                tabTitleView.setTextColor(defTextColor);
            tabTitleView.setText(adapter.getPageTitle(i));
            tabView.setOnClickListener(tabClickListener);
            mTextViews.add(tabTitleView);
            mTabStrip.addView(tabView);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (mViewPager != null) {
            scrollToTab(mViewPager.getCurrentItem(), 0);
        }
    }

    private void scrollToTab(int tabIndex, int positionOffset) {
        final int tabStripChildCount = mTabStrip.getChildCount();
        if (tabStripChildCount == 0 || tabIndex < 0 || tabIndex >= tabStripChildCount) {
            return;
        }

        View selectedChild = mTabStrip.getChildAt(tabIndex);
        if (selectedChild != null) {
            int targetScrollX = selectedChild.getLeft() + positionOffset;

            if (tabIndex > 0 || positionOffset > 0) {
                // If we're not at the first child and are mid-scroll, make sure we obey the offset
                targetScrollX -= mTitleOffset;
            }

            scrollTo(targetScrollX, 0);
        }
    }

    /**
     * Allows complete control over the colors drawn in the tab item_choose_enter_free_adapter. Set with
     * {@link #setCustomTabColorizer(TabColorizer)}.
     */
    public interface TabColorizer {

        /**
         * @return return the color of the indicator used when {@code position} is selected.
         */
        int getIndicatorColor(int position);

        /**
         * @return return the color of the divider drawn to the right of {@code position}.
         */
        int getDividerColor(int position);

    }

    private class InternalViewPagerListener implements ViewPager.OnPageChangeListener {
        private int mScrollState;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int tabStripChildCount = mTabStrip.getChildCount();
            if ((tabStripChildCount == 0) || (position < 0) || (position >= tabStripChildCount)) {
                return;
            }

            mTabStrip.onViewPagerPageChanged(position, positionOffset);


            if (position < mLastPos) {
                int color = CustomTabStrip.blendColors(selTextColor, defTextColor, positionOffset);
                getTextViews().get(mLastPos).setTextColor(color);

                color = CustomTabStrip.blendColors(defTextColor, selTextColor, positionOffset);
                getTextViews().get(position).setTextColor(color);
            } else {
                int color = CustomTabStrip.blendColors(defTextColor, selTextColor, positionOffset);
                getTextViews().get(mLastPos).setTextColor(color);
                if (getTextViews().size() - 1 > mLastPos) {
                    color = CustomTabStrip.blendColors(selTextColor, defTextColor, positionOffset);
                    getTextViews().get(mLastPos + 1).setTextColor(color);
                }
            }


            View selectedTitle = mTabStrip.getChildAt(position);
            int extraOffset = (selectedTitle != null)
                    ? (int) (positionOffset * selectedTitle.getWidth())
                    : 0;
            scrollToTab(position, extraOffset);

            if (mViewPagerPageChangeListener != null) {
                mViewPagerPageChangeListener.onPageScrolled(position, positionOffset,
                        positionOffsetPixels);
            }
        }

        @Override
        public void onPageSelected(int position) {
            if (mScrollState == ViewPager.SCROLL_STATE_IDLE) {
                mTabStrip.onViewPagerPageChanged(position, 0f);
                scrollToTab(position, 0);

            }
            getTextViews().get(mLastPos).setTextColor(defTextColor);
            getTextViews().get(position).setTextColor(selTextColor);
            mLastPos = position;
            if (mViewPagerPageChangeListener != null) {

                mViewPagerPageChangeListener.onPageSelected(position);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            mScrollState = state;

            if (mViewPagerPageChangeListener != null) {
                mViewPagerPageChangeListener.onPageScrollStateChanged(state);
            }
        }

    }

    private class TabClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                if (v == mTabStrip.getChildAt(i)) {
                    mViewPager.setCurrentItem(i);
                    return;
                }
            }
        }
    }

}