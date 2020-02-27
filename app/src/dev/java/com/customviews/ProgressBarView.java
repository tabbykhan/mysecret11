package com.customviews;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.R;
import com.utilities.DeviceScreenUtil;

import java.util.ArrayList;
import java.util.List;


public class ProgressBarView extends LinearLayout {

    private List<View> viewList = new ArrayList<>();

    int selectedPosition = 0;

    public void initialize() {
        for (int i = 0; i < viewList.size(); i++) {
            if (i < viewList.size() - 1) {
                View view = viewList.get(i);
                TextView textView = view.findViewById(R.id.tv_text);
                textView.setText("");
            }
        }
    }

    public void setMaxPlayer(int position) {
        this.selectedPosition = position;
        View view = viewList.get(viewList.size() - 1);
        TextView textView = view.findViewById(R.id.tv_text);
        textView.setText(String.valueOf(viewList.size()));
        if (position == viewList.size())
            textView.setTextColor(getResources().getColor(R.color.colorWhite));
        else
            textView.setTextColor(getResources().getColor(R.color.colorBlack));
    }

    public float getSelectedPositionX() {
        float defaultPosition=DeviceScreenUtil.getInstance().convertDpToPixel(35);
        if (viewList == null || viewList.size() == 0) return 0;
        if (selectedPosition == 0) return 0;
        View view = viewList.get(selectedPosition - 1);
        return defaultPosition+view.getX() + (view.getWidth() * 0.5f);
    }

    public void changeColor(int position) {
        for (int i = 0; i < viewList.size(); i++) {
            View view = viewList.get(i);
            TextView textView = view.findViewById(R.id.tv_text);
            ImageView imageView = view.findViewById(R.id.iv_image);
            if (i < position) {
                Resources res = getResources();
                Drawable background = res.getDrawable(R.drawable.parallelogram_image2);
                imageView.setBackground(background);

                imageView.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorActivateGreen), android.graphics.PorterDuff.Mode.MULTIPLY);
            } else {
                Resources res = getResources();
                Drawable background = res.getDrawable(R.drawable.parallelogram_image);
                imageView.setBackground(background);
            }
            if (i == position - 1) {
                textView.setText(String.valueOf(position));
                textView.setTextColor(getResources().getColor(R.color.colorWhite));
            } else if (i < viewList.size() - 1) {
                textView.setText("");
            }
        }
    }

    public ProgressBarView(Context context) {
        super(context);
    }

    public ProgressBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ProgressBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ProgressBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }


    protected void init(Context context, AttributeSet attrs) {
        if (!isInEditMode()) {
            if (attrs != null) {
                TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ProgressView);
                int imageColor = a.getInt(R.styleable.ProgressView_imageColor, R.color.colorWhite);
                int textColor = a.getInt(R.styleable.ProgressView_textColor, R.color.colorBlack);
                int itemCount = a.getInt(R.styleable.ProgressView_itemCount, 11);
                for (int i = 1; i <= itemCount; i++) {
                    LayoutInflater inflater = (LayoutInflater) context
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View view = inflater.inflate(R.layout.progressbar_view, this, false);
                    TextView textView = view.findViewById(R.id.tv_text);
                    textView.setText(String.valueOf(i));
                    LayoutParams params = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
                    int width = DeviceScreenUtil.getInstance().getWidth();
                    float item = (width - DeviceScreenUtil.getInstance().convertDpToPixel(70)) / 11.0f;
                    params.width = Math.round(item);
                    params.height = Math.round(params.width / 1.80f);
                    view.setLayoutParams(params);
                    addView(view);
                    viewList.add(view);
                }
            }

        }
    }


}
