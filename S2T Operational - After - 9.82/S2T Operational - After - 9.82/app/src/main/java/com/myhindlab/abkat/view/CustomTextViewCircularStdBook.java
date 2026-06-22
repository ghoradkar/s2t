package com.myhindlab.abkat.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class CustomTextViewCircularStdBook extends androidx.appcompat.widget.AppCompatTextView {
    public CustomTextViewCircularStdBook(Context context) {
        super(context);
        initTypeface(context);
    }
    public CustomTextViewCircularStdBook( Context context,
                AttributeSet attrs) { super(context, attrs);
        initTypeface(context);
    }
    public CustomTextViewCircularStdBook(Context context,
                AttributeSet attrs,
                int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTypeface(context);
    }
    private void initTypeface(Context context) {
        Typeface tf = Typeface.createFromAsset(
                context.getAssets(),
                "fonts/circularstd_book.otf");
        this.setTypeface(tf);
    }
}

