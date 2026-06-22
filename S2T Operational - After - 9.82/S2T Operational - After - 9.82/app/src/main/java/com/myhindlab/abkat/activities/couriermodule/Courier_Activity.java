package com.myhindlab.abkat.activities.couriermodule;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

//import com.innowavehealthcare.pluscarecollectionboy.R;
import com.myhindlab.abkat.R;

public class Courier_Activity extends Activity implements View.OnClickListener {
    TextView textviewSentCourier, textviewReceivedCourier, tv_forward;
    LinearLayout layoutSentCourier, layoutReceivedCourier, ll_forward;
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier);
        init();
        setUpToolBar();
        setDefaults();
        setEventHandler();
    }

    private void init() {
        context = Courier_Activity.this;
        textviewSentCourier = findViewById(R.id.tv_sender);
        textviewReceivedCourier = findViewById(R.id.tv_receiver);
        layoutSentCourier = findViewById(R.id.ll_sender);
        layoutReceivedCourier = findViewById(R.id.ll_receiver);
        tv_forward = findViewById(R.id.tv_forward);
        ll_forward = findViewById(R.id.ll_forward);

    }

    private void setUpToolBar() {
        ImageButton back_btn = (ImageButton) findViewById(R.id.tool_leftbtn);
        TextView Title = (TextView) findViewById(R.id.tool_titile);

        back_btn.setBackgroundResource(R.drawable.icon_back);
        Title.setText("Courier");

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setDefaults() {

    }

    private void setEventHandler() {
        textviewSentCourier.setOnClickListener(this);
        textviewReceivedCourier.setOnClickListener(this);
        layoutSentCourier.setOnClickListener(this);
        layoutReceivedCourier.setOnClickListener(this);
        tv_forward.setOnClickListener(this);
        ll_forward.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_sender: {
//                startActivity(new Intent(context, SendCourier_Activity.class));
                startActivity(new Intent(context, CourierSendStep1_Activity.class)
                        .putExtra("forwardedCourierIds", "0"));
                break;
            }
            case R.id.tv_receiver: {
//                startActivity(new Intent(context, ReceivedCourier_Activity.class));
                startActivity(new Intent(context, CourierReceivedList_Activity.class));
                break;
            }
            case R.id.tv_forward:
            case R.id.ll_forward: {
                startActivity(new Intent(context, CourierForwardList_Activity.class));
                break;
            }
        }
    }
}
