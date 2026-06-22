package com.myhindlab.abkat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.DivisionWiseTargetListModel;

import java.util.ArrayList;

public class DistrictTargetAdapter extends RecyclerView.Adapter<DistrictTargetAdapter.TragetViewHolder> {

    private Context context;
    private ArrayList<DivisionWiseTargetListModel.TargetListItem> outputArrayList;

    public DistrictTargetAdapter(Context context, ArrayList<DivisionWiseTargetListModel.TargetListItem> outputArrayList) {
        this.context = context;
        this.outputArrayList = outputArrayList;
    }

    @NonNull
    @Override
    public TragetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_row_division_target, parent, false);
        return new TragetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TragetViewHolder holder, int position) {

        DivisionWiseTargetListModel.TargetListItem output = outputArrayList.get(position);

        holder.distName.setText(output.getDistrictName());
        holder.tvTotalWorkers.setText(String.valueOf(output.getTotalBeneficiary()));
        holder.tvMonthlyTargetToAchieved.setText(String.valueOf(output.getMonthlyTarget()));
        holder.tvMonthlyTargetAchieved.setText(String.valueOf(output.getTotalMonthlyTargetAchived()));
        holder.tvMonthlyCount.setText(String.valueOf(output.getThreeMonthsTarget()));
        holder.tvPerDayTarget.setText(String.valueOf(output.getDailyTarget()));
        holder.tvPerDayTargetAchieved.setText(String.valueOf(output.getCurrentDateTargetAchived()));
        holder.tvPercentage.setText(String.valueOf(output.getPercentage()));

        if (position % 2 == 1) {
            holder.llMain.setBackgroundColor(context.getColor(R.color.devider));
        } else {
            holder.llMain.setBackgroundColor(context.getColor(R.color.white));

        }

    }

    @Override
    public int getItemCount() {
        return outputArrayList.size();
    }

    class TragetViewHolder extends RecyclerView.ViewHolder {
        TextView distName, tvTotalWorkers, tvMonthlyTargetToAchieved, tvMonthlyCount, tvPerDayTarget, tvPerDayTargetAchieved, tvPercentage,tvMonthlyTargetAchieved;
        LinearLayoutCompat llMain;

        public TragetViewHolder(@NonNull View itemView) {
            super(itemView);
            distName = itemView.findViewById(R.id.distName);
            tvTotalWorkers = itemView.findViewById(R.id.tvTotalWorkers);
            tvMonthlyTargetToAchieved = itemView.findViewById(R.id.tvMonthlyTargetToAchieved);
            tvMonthlyCount = itemView.findViewById(R.id.tvMonthlyCount);
            tvPerDayTarget = itemView.findViewById(R.id.tvPerDayTarget);
            tvPerDayTargetAchieved = itemView.findViewById(R.id.tvPerDayTargetAchieved);
            tvPercentage = itemView.findViewById(R.id.tvPercentage);
            llMain = itemView.findViewById(R.id.llMain);
            tvMonthlyTargetAchieved = itemView.findViewById(R.id.tvMonthlyTargetAchieved);


        }
    }

}
