package com.myhindlab.abkat.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class CustomTextViewCircularStd extends androidx.appcompat.widget.AppCompatTextView {
    public CustomTextViewCircularStd( Context context) {
        super(context);
        initTypeface(context);
    }
    public CustomTextViewCircularStd(Context context, AttributeSet attrs)
    {
        super(context, attrs) ;
        initTypeface(context);
    }
    public CustomTextViewCircularStd(Context context,
                AttributeSet attrs,
                int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTypeface(context);
    }
    private void initTypeface(Context context) {
        Typeface tf = Typeface.createFromAsset(
                context.getAssets(),
                "fonts/circularstd_medium.otf");
        this.setTypeface( tf);
    }
}