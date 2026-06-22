package com.myhindlab.abkat.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.TodaysPatientCountListModel;

import java.util.List;

public class TodaysPatientDistWiseListAdapter extends RecyclerView.Adapter<TodaysPatientDistWiseListAdapter.MyViewHolder> {

    private Context context;
    private List<TodaysPatientCountListModel.OutputBean> patientList;

    public TodaysPatientDistWiseListAdapter(Context context, List<TodaysPatientCountListModel.OutputBean> patientList) {
        this.context = context;
        this.patientList = patientList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = inflater.inflate(R.layout.list_row_todayspatient, viewGroup, false);
        View view = inflater.inflate(R.layout.list_row_todayspatient_v2, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int pos) {
        int position = holder.getAdapterPosition();
        TodaysPatientCountListModel.OutputBean patientDetails = patientList.get(position);

        holder.tv_district.setText(patientDetails.getDISTNAME());
        holder.tv_todayscamp.setText(patientDetails.getTodayConductedCamps() + "");
        holder.tv_todayscount.setText(patientDetails.getTodayFacilitatedWorkers() + "");

        if (patientDetails.getTodayFacilitatedWorkers() < 200) {
            holder.ll_main_layout.setBackgroundColor(context.getResources().getColor(R.color.light_red));
        } else if (patientDetails.getTodayFacilitatedWorkers() > 201 && patientDetails.getTodayFacilitatedWorkers() < 300) {
            holder.ll_main_layout.setBackgroundColor(context.getResources().getColor(R.color.light_yellow));
        } else if (patientDetails.getTodayFacilitatedWorkers() > 300) {
            holder.ll_main_layout.setBackgroundColor(context.getResources().getColor(R.color.light_green));
        }
    }

    @Override
    public int getItemCount() {
        return patientList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout ll_main_layout;
        private TextView tv_district, tv_todayscamp, tv_todayscount;

        public MyViewHolder(@NonNull View view) {
            super(view);
            ll_main_layout = view.findViewById(R.id.ll_main_layout);
            tv_district = view.findViewById(R.id.tv_district);
            tv_todayscamp = view.findViewById(R.id.tv_todayscamp);
            tv_todayscount = view.findViewById(R.id.tv_todayscount);
        }
    }
}
