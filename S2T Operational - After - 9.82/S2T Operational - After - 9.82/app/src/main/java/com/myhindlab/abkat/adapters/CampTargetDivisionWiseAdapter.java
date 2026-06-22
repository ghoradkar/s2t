package com.myhindlab.abkat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.DivisionWiseTargetListModel;
import com.myhindlab.abkat.utilities.Utilities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class CampTargetDivisionWiseAdapter extends RecyclerView.Adapter<CampTargetDivisionWiseAdapter.TragetViewHolder> {

    private Context context;
    private ArrayList<DivisionWiseTargetListModel> outputArrayList;
    private String selectedDate;

    public CampTargetDivisionWiseAdapter(Context context, ArrayList<DivisionWiseTargetListModel> outputArrayList, String selectedDate) {
        this.context = context;
        this.outputArrayList = outputArrayList;
        this.selectedDate = selectedDate;
    }

    @NonNull
    @Override
    public TragetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.camp_target_division_wise_list_row, parent, false);
        return new TragetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TragetViewHolder holder, int position) {

        DivisionWiseTargetListModel output = outputArrayList.get(position);

        holder.tvDivisionName.setText(output.getDivname());

        ArrayList<DivisionWiseTargetListModel.TargetListItem> targetListItems = new ArrayList<>();
        targetListItems.addAll(outputArrayList.get(position).getDistrictList());
        holder.rvDistrict.setLayoutManager(new LinearLayoutManager(context));
        holder.rvDistrict.setHasFixedSize(true);
        holder.rvDistrict.setAdapter(new DistrictTargetAdapter(context, targetListItems));
        int totalWorkers = 0;
        int totalMonthlyTarget = 0;
        int totalDailyTargetAchieved = 0;
        int totalMonthlyTargetAchieved = 0;
        int monthlyCount = 0;
        int perDayTarget = 0;
        double totalPercentage = 0;

        for (DivisionWiseTargetListModel.TargetListItem o :
                targetListItems) {
            totalWorkers += o.getTotalBeneficiary();
            totalMonthlyTarget += o.getMonthlyTarget();
            totalMonthlyTargetAchieved += o.getTotalMonthlyTargetAchived();
            monthlyCount += o.getThreeMonthsTarget();
            perDayTarget += o.getDailyTarget();
            totalDailyTargetAchieved += o.getCurrentDateTargetAchived();


        }
        totalPercentage = ((float) totalDailyTargetAchieved / perDayTarget) * 100;


        try {
            holder.targetRemain.setText("Target Achieved\n" + Utilities.dfDate5.format(new SimpleDateFormat("yyyy/MM/dd").parse(selectedDate)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.tvTotalTotalWorkers.setText(String.valueOf(totalWorkers));
        holder.tvTotalMonthlyCount.setText(String.valueOf(monthlyCount));
        holder.tvTotalMonthlyTargetToAchieved.setText(String.valueOf(totalMonthlyTarget));
        holder.tvTotalMonthlyTargetAchieved.setText(String.valueOf(totalMonthlyTargetAchieved));
        holder.tvTotalPerDayTarget.setText(String.valueOf(perDayTarget));
        holder.tvTotalPerDayTargetAchieved.setText(String.valueOf(totalDailyTargetAchieved));
        holder.tvTotalPercentage.setText(String.format("%,.2f", totalPercentage));


        if (position % 2 == 1) {
            holder.llMain.setBackgroundColor(context.getResources().getColor(R.color.devider));
        } else {
            holder.llMain.setBackgroundColor(context.getResources().getColor(R.color.white));

        }

    }

    @Override
    public int getItemCount() {
        return outputArrayList.size();
    }

    class TragetViewHolder extends RecyclerView.ViewHolder {
        TextView distName, targetToAchieve, perDayTarget, targetAchieved, targetRemain, tvDivisionName, tvTotalTotalWorkers, tvTotalMonthlyTargetAchieved, tvTotalMonthlyTargetToAchieved, tvTotalMonthlyCount, tvTotalPerDayTarget, tvTotalPerDayTargetAchieved, tvTotalPercentage;
        LinearLayoutCompat llMain;
        RecyclerView rvDistrict;

        public TragetViewHolder(@NonNull View itemView) {
            super(itemView);
            distName = itemView.findViewById(R.id.distName);
            targetToAchieve = itemView.findViewById(R.id.targetToAchieve);
            perDayTarget = itemView.findViewById(R.id.perDayTarget);
            targetAchieved = itemView.findViewById(R.id.targetAchieved);
            targetRemain = itemView.findViewById(R.id.targetRemain);
            llMain = itemView.findViewById(R.id.llMain);
            rvDistrict = itemView.findViewById(R.id.rvDistrict);
            tvDivisionName = itemView.findViewById(R.id.tvDivisionName);

            tvTotalMonthlyTargetToAchieved = itemView.findViewById(R.id.tvTotalMonthlyTargetToAchieved);
            tvTotalTotalWorkers = itemView.findViewById(R.id.tvTotalTotalWorkers);
            tvTotalMonthlyCount = itemView.findViewById(R.id.tvTotalMonthlyCount);
            tvTotalMonthlyTargetAchieved = itemView.findViewById(R.id.tvTotalMonthlyTargetAchieved);
            tvTotalPerDayTarget = itemView.findViewById(R.id.tvTotalPerDayTarget);
            tvTotalPerDayTargetAchieved = itemView.findViewById(R.id.tvTotalPerDayTargetAchieved);
            tvTotalPercentage = itemView.findViewById(R.id.tvTotalPercentage);


        }
    }

}
