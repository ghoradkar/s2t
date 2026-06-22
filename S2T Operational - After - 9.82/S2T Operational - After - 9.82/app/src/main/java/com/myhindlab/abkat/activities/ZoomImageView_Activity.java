package com.myhindlab.abkat.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.myhindlab.abkat.R;
import com.squareup.picasso.Picasso;

public class ZoomImageView_Activity extends Activity {

    private Context context;
    private ImageView imageView;

    private ScaleGestureDetector scaleGestureDetector;
    private float mScaleFactor = 1.0f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_image_view);

        init();
        setDefaults();
        setEventListener();
        setUpToolBar();
    }

    private void init() {
        context = ZoomImageView_Activity.this;
        imageView = findViewById(R.id.imageView);
        scaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener());
    }

    private void setDefaults() {
        String url = getIntent().getStringExtra("url");

        Picasso.with(context)
                .load(url)
                .into(imageView);
    }

    private void setEventListener() {
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        scaleGestureDetector.onTouchEvent(motionEvent);
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));
            imageView.setScaleX(mScaleFactor);
            imageView.setScaleY(mScaleFactor);
            return true;
        }
    }

    private void setUpToolBar() {
        ImageButton back_btn = findViewById(R.id.tool_leftbtn);
        back_btn.setBackgroundResource(R.drawable.icon_back);

        back_btn.setOnClickListener(view -> finish());
    }
}