package com.myhindlab.abkat.activities.liver_scanning_dashboard.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.payout.RaiseRequestActivity;
import com.myhindlab.abkat.models.FibroscanDistrictWiseCountModel;

import java.util.List;

public class LiverCountDistrictWiseAdapter extends RecyclerView.Adapter<LiverCountDistrictWiseAdapter.myview> {
    List<FibroscanDistrictWiseCountModel.Output> countList;
    onTouchListner onTouchListner;
    Context context;

    public interface onTouchListner {
        public void onDataClick(FibroscanDistrictWiseCountModel.Output item);


    }

    public LiverCountDistrictWiseAdapter(Context context, List<FibroscanDistrictWiseCountModel.Output> countList, onTouchListner onTouchListner) {
        this.countList = countList;
        this.onTouchListner = onTouchListner;
        this.context = context;
    }

    @NonNull
    @Override
    public myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_liver_machine, parent, false);
        return new myview(view);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull myview holder, int position) {
        FibroscanDistrictWiseCountModel.Output item = countList.get(position);
        holder.setIsRecyclable(false);
//        holder.tv_SrName.setText(String.valueOf(position + 1));
        holder.tv_district.setText(item.getDistrict());
        holder.tv_patient_count.setText("" + item.getPatientCount());
        holder.tv_abnormal.setText("" + item.getAbnormalPatientCount());
        holder.tv_success_shot.setText("" + item.getSuccessfullShots());
        holder.tv_total_shot.setText("" + item.getTotalShots());
        holder.tv_warranted.setText("" + item.getModerateSevereCount());


        holder.tv_district.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTouchListner.onDataClick(item);
            }
        });


    }

    @Override
    public int getItemCount() {
        return countList.size();
    }

    class myview extends RecyclerView.ViewHolder {
        TextView tv_district, tv_patient_count, tv_abnormal, tv_success_shot, tv_total_shot,tv_warranted;


        public myview(@NonNull View itemView) {
            super(itemView);

            tv_district = itemView.findViewById(R.id.tv_district);
            tv_patient_count = itemView.findViewById(R.id.tv_patient_count);
            tv_abnormal = itemView.findViewById(R.id.tv_abnormal);
            tv_success_shot = itemView.findViewById(R.id.tv_success_shot);
            tv_total_shot = itemView.findViewById(R.id.tv_total_shot);
            tv_warranted = itemView.findViewById(R.id.tv_warranted);


        }
    }
}
