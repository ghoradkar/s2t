package com.myhindlab.abkat.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;

import com.myhindlab.abkat.models.ScreeningTestModel;
import com.myhindlab.abkat.R;
import java.util.List;

public class ResourceAllocationActivity extends AppCompatActivity  {

    private List<ScreeningTestModel.OutputBean> selectedScreeningTestList;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource_allocation);


    }

    void init(){
        context=ResourceAllocationActivity.this;



    }
}