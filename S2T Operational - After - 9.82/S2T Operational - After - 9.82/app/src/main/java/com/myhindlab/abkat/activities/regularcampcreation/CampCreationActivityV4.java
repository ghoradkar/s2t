package com.myhindlab.abkat.activities.regularcampcreation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.regularcampcreation.model.CampCreationModel;

public class CampCreationActivityV4 extends AppCompatActivity implements FragmentChange {
    private int currentItem = 0;
    ViewPager viewPager;
    private TextView tvMiddle, tvLeft, tvRight;
    public static CampCreationModel campCreationModel;
    private ImageView imvLeft, imvRight;
    private final String TAG = CampCreationActivityV4.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instant_camp_creation);

        View mainView = findViewById(R.id.ccMain);
        ViewCompat.setOnApplyWindowInsetsListener(mainView,
                new androidx.core.view.OnApplyWindowInsetsListener() {
                    @Override
                    public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                        Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                        v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

                        return insets;
                    }
                });

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), this);
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);

        tvMiddle = findViewById(R.id.tvTestMapping);
        tvLeft = findViewById(R.id.tvSiteSelection);
        tvRight = findViewById(R.id.tvDeviceMapping);
        imvLeft = findViewById(R.id.imvLeft);
        imvRight = findViewById(R.id.imvTestMapping);

        campCreationModel = new CampCreationModel();

    }

    @Override
    public void onFragmentChange(int pos) {
        viewPager.setCurrentItem(pos);
        Gson gson = new GsonBuilder()
                .serializeNulls()
                .create();

        Log.i(TAG, "onFragmentChange: " + gson.toJson(campCreationModel));
        changeHeader(pos);
    }

    void changeHeader(int pos) {
        switch (pos) {
//            case 0:
//                tvLeft.setText("");
//                imvLeft.setVisibility(View.GONE);
//                tvMiddle.setText("Site \nSelection");
//                tvRight.setText("Camp \nDetails");
//                break;
//            case 1:
//                imvLeft.setVisibility(View.VISIBLE);
//                imvRight.setVisibility(View.VISIBLE);
//
//                tvLeft.setText("Site \nSelection");
//                tvMiddle.setText("Camp \nDetails");
//                tvRight.setText("Device \nMapping");
//                break;
            case 0:
                imvLeft.setVisibility(View.VISIBLE);
                imvRight.setVisibility(View.VISIBLE);
                tvLeft.setText("Camp \nDetails");
                tvMiddle.setText("Device \nMapping");
                tvRight.setText("Consumables");
                break;
            case 1:
                imvLeft.setVisibility(View.VISIBLE);
                imvRight.setVisibility(View.VISIBLE);
                tvLeft.setText("Device \nAllocation");
                tvMiddle.setText("Consumables");
                tvRight.setText("Resource \nAllocation");
                break;
            case 2:
                imvRight.setVisibility(View.GONE);
                tvLeft.setText("Consumables");
                tvMiddle.setText("Resource \nAllocation");
                tvRight.setText("");

                break;
        }

    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() != 0) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
            changeHeader(viewPager.getCurrentItem());

        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        campCreationModel = null;
        super.onDestroy();
    }
}