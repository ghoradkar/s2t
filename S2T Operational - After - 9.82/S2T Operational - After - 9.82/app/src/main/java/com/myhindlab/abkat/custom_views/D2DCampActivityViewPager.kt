package com.myhindlab.abkat.custom_views

import android.content.Context
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class D2DCampActivityViewPager(context: Context) : ViewPager(context) {

    override fun onInterceptHoverEvent(event: MotionEvent?): Boolean {
        return super.onInterceptHoverEvent(event)
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return super.onTouchEvent(ev)
    }
}