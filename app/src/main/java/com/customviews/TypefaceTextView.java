package com.customviews;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.R;
import com.fonts.FontUtils;


/**
 * Created by ubuntu on 28/12/17.
 */

public class TypefaceTextView extends TextView {
    public TypefaceTextView (Context context) {
        super(context);
    }

    public TypefaceTextView (Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TypefaceTextView (Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TypefaceTextView (Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);

    }

    protected void init (Context context, AttributeSet attributeSet) {
//        if (!isInEditMode()) {
            if (attributeSet != null) {
                TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.TypefaceTextView);
                String fontName = typedArray.getString(R.styleable.TypefaceTextView_custom_font);
                int anInt = typedArray.getInt(R.styleable.TypefaceTextView_android_textStyle, 0);
                Typeface typeface = FontUtils.getInstance().getFont(context, fontName);
                if (typeface != null) {
                    setTypeface(typeface,anInt);

                }
            }
//        }
    }

    public void setCustomFont(String fontName){
        Typeface typeface = FontUtils.getInstance().getFont(getContext(), fontName);
        if (typeface != null) {
            setTypeface(typeface);

        }
    }

    public void setCustomFont(String fontName, int style){
        Typeface typeface = FontUtils.getInstance().getFont(getContext(), fontName);
        if (typeface != null) {
            setTypeface(typeface,style);

        }
    }


}
