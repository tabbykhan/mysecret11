package com.app.ui.main.soccer.team.playerpreview;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.R;

import java.util.ArrayList;
import java.util.List;


public class SoccerPlayerView extends LinearLayout {

    List<View> itemViews = new ArrayList<>();
    int perItemSize = 0;

    public SoccerPlayerView(Context context) {
        super(context);
    }

    public SoccerPlayerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SoccerPlayerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SoccerPlayerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }


    protected void init(Context context, AttributeSet attrs) {
        setOrientation(LinearLayout.HORIZONTAL);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    public void setPerItemSize(int perItemSize) {
        this.perItemSize = perItemSize;
    }

    public void setupItems(int count) {
        setMinimumHeight(this.perItemSize);
        itemViews.clear();
        removeAllViews();
        for (int i = 0; i < count; i++) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.player_view, this, false);
            ViewGroup.LayoutParams layoutParams1 = view.findViewById(R.id.rl_layout).getLayoutParams();
            layoutParams1.width = perItemSize;
            layoutParams1.height = perItemSize;
            view.findViewById(R.id.rl_layout).setLayoutParams(layoutParams1);

            LayoutParams layoutParams = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1);
            layoutParams.gravity = Gravity.CENTER;
            addView(view, layoutParams);
            itemViews.add(view);
        }

    }

    public List<View> getItemViews() {
        return itemViews;
    }
}
