package com.myhindlab.abkat.adapters.safey;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.safey.TestMeasurements;
import com.myhindlab.abkat.utilities.safey.Utility;

import java.util.List;

public class TestResultAdapter extends RecyclerView.Adapter<TestResultAdapter.MyViewHolder> {
    Context context;
    List<TestMeasurements> airTestValues;


    int TYPE_HEADER = 1;
    int TYPE_DATA = 2;


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView lblMeasurement;
        public TextView lblMeasureValue;
        public TextView lblPercentage;
        public TextView lblLNN;
        public TextView lblZscore;

        MyViewHolder(View view) {
            super(view);
            lblMeasurement = view.findViewById(R.id.lblMeasurement);
            lblMeasureValue = view.findViewById(R.id.lblMeasureValue);
            lblZscore = view.findViewById(R.id.lblZscore);
            lblPercentage = view.findViewById(R.id.lblPercentage);
            lblLNN = view.findViewById(R.id.lblLNN);


        }
    }

    public TestResultAdapter(Context context, List<TestMeasurements> airTestValues) {
        this.context = context;
        this.airTestValues = airTestValues;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER)

            return new MyViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_test_result_header, parent, false));
        else
            return new MyViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_test_result, parent, false));


    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (position > 0) {

            TestMeasurements data = airTestValues.get(position - 1);
            holder.lblMeasurement.setText(data.getMeasurement());

            holder.lblMeasureValue.setText(String.format(
                    "%.2f",
                    data.getMeasuredValue()
            ) + " " + data.getUnit());


            if (!data.getPredictedValue().equals(" -  ")) {
                double predicatedPer = data.getPredictedPer(); //data.predictedValue
                holder.lblPercentage.setText(
                        String.format("%.2f", predicatedPer) + "%");
                if (predicatedPer >= 100)
                    holder.lblPercentage.setTextColor(Color.parseColor("#00D16C"));
                else
                    holder.lblPercentage.setTextColor(Color.parseColor("#000000"));
            } else
                holder.lblPercentage.setText(" -  ");


            if (!data.getLln().equals(" -  ")) {
                holder.lblLNN.setText(String.valueOf(Utility.rounded(Double.parseDouble(data.getLln()))));
            } else
                holder.lblLNN.setText(data.getLln().toString());

            if (!data.getzScore().equals(" -  ")) {
                holder.lblZscore.setText(String.valueOf(Utility.rounded(Double.parseDouble(data.getzScore()))));
            } else
                holder.lblZscore.setText(data.getzScore().toString());

        }
    }

    @Override
    public int getItemCount() {
        return airTestValues.size() + 1;
    }


    @Override
    public int getItemViewType(int position) {

        if (position == 0)
            return TYPE_HEADER;
        else
            return TYPE_DATA;


    }

    interface setonClickListner {
        void onClick(int position);
    }
}
