package com.myhindlab.abkat.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.fragments.CampBeneficiaryList_Fragment;
import com.myhindlab.abkat.fragments.CampIntentoryList_Fragment;
import com.myhindlab.abkat.fragments.CampPatientStatusList_Fragment;
import com.myhindlab.abkat.fragments.CampResourceList_Fragment;
import com.myhindlab.abkat.fragments.CampSampleProcessingList_Fragment;
import com.myhindlab.abkat.fragments.CampScreeningTestList_Fragment;
import com.myhindlab.abkat.utilities.ConstantData;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.ArrayList;
import java.util.List;

public class CampDetails_Activity extends AppCompatActivity {

    private Context context;
    private UserSessionManager session;
    private ProgressDialog pd;
    private SmartTabLayout tabs;
    private ViewPager viewpager;
    private String date, DISTLGDCODE,campId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camp_details);

        init();
        setDefaults();
        setEventHandler();
        setUpToolBar();

    }

    private void init() {
        context = CampDetails_Activity.this;
        session = new UserSessionManager(context);
        pd = new ProgressDialog(context);
        tabs = findViewById(R.id.tabs);
        viewpager = findViewById(R.id.viewpager);
        Intent intent = getIntent();
        date = intent.getStringExtra("Date");
        DISTLGDCODE = intent.getStringExtra("DISTLGDCODE");
    }

    private void setDefaults() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        Bundle bundle = new Bundle();
        bundle.putString("Date", date);
        bundle.putString("DISTLGDCODE", DISTLGDCODE);
        bundle.putString("campId", campId);
        bundle.putSerializable("campDetails", getIntent().getSerializableExtra("campDetails"));
        bundle.putSerializable("campTypeId", ConstantData.getInstance().getCampType());

        CampScreeningTestList_Fragment campScreeningTestList_fragment = new CampScreeningTestList_Fragment();
        campScreeningTestList_fragment.setArguments(bundle);

        adapter.addFrag(campScreeningTestList_fragment, "Screening Test");

        adapter.addFrag(new CampBeneficiaryList_Fragment(), "Beneficiary");

        if (getIntent().getStringExtra("Type").equals("1")) {

//            adapter.addFrag(new CampResourceList_Fragment(), "Resource");

//            adapter.addFrag(new CampIntentoryList_Fragment(), "Inventory");

//            adapter.addFrag(new CampSampleProcessingList_Fragment(), "Sample Processing");

        }

        CampPatientStatusList_Fragment campPatientStatusList_fragment = new CampPatientStatusList_Fragment();
        campPatientStatusList_fragment.setArguments(bundle);
        adapter.addFrag(campPatientStatusList_fragment, "Patient Status");

        viewpager.setAdapter(adapter);
        viewpager.setOffscreenPageLimit(6);
        tabs.setViewPager(viewpager);
    }

    private void setEventHandler() {

//        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int i, float v, int i1) {
//
//            }
//
//            @Override
//            public void onPageSelected(int i) {
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int i) {
//                if (getIntent().getStringExtra("Type").equals("1")) {
//                    switch (i){
//                        case 1:
//                            new CampBeneficiaryList_Fragment().setDefault();
//                            break;
//                        case 2:
//                            new CampResourceList_Fragment().setDefault();
//                            break;
//                        case 3:
//                            new CampIntentoryList_Fragment().setDefault();
//                            break;
//                        case 4:
//                            new CampPatientStatusList_Fragment().setDefault();
//                            break;
//                        case 5:
//                            new CampSampleProcessingList_Fragment().setDefault();
//                            break;
//                    }
//                } else {
//                    switch (i){
//                        case 1:
//                            new CampBeneficiaryList_Fragment().setDefault();
//                            break;
//                        case 2:
//                            new CampPatientStatusList_Fragment().setDefault();
//                            break;
//                        case 3:
//                            new CampSampleProcessingList_Fragment().setDefault();
//                            break;
//                    }
//                }
//            }
//        });
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void setUpToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Camp Details");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
