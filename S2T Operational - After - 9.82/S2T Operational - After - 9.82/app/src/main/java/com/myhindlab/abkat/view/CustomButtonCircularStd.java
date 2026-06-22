package com.myhindlab.abkat.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class CustomButtonCircularStd extends androidx.appcompat.widget.AppCompatButton {
    public CustomButtonCircularStd(Context context) {
        super(context);
        initTypeface(context);
    }

    public CustomButtonCircularStd(Context context,
                                   AttributeSet attrs) {
        super(context, attrs);
        initTypeface(context);
    }

    public CustomButtonCircularStd(Context context,
                                   AttributeSet attrs,
                                   int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTypeface(context);
    }

    private void initTypeface(Context context) {
        Typeface tf = Typeface.createFromAsset(
                context.getAssets(),
                "fonts/circularstd_medium.otf");
        this.setTypeface(tf);
    }
}
