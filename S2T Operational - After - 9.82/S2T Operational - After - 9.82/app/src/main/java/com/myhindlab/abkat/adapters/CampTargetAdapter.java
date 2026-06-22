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
import com.myhindlab.abkat.models.CampTargetListModel;

import java.util.ArrayList;

public class CampTargetAdapter extends RecyclerView.Adapter<CampTargetAdapter.TragetViewHolder> {

    private Context context;
    private ArrayList<CampTargetListModel.Output> outputArrayList;

    public CampTargetAdapter(Context context, ArrayList<CampTargetListModel.Output> outputArrayList) {
        this.context = context;
        this.outputArrayList = outputArrayList;
    }

    @NonNull
    @Override
    public TragetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.camp_target_list_row, parent, false);
        return new TragetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TragetViewHolder holder, int position) {

        CampTargetListModel.Output output = outputArrayList.get(position);

        holder.distName.setText(output.getDistrictName());
        holder.perDayTarget.setText(String.valueOf(output.getDailyTarget()));
        holder.targetAchieved.setText(String.valueOf(output.getTotalMonthlyTargetAchived()));
        holder.targetRemain.setText(String.valueOf(output.getCurrentDateTargetAchived()));
        holder.targetToAchieve.setText(String.valueOf(output.getMonthlyTarget()));

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
        TextView distName, targetToAchieve, perDayTarget, targetAchieved, targetRemain;
        LinearLayoutCompat llMain;

        public TragetViewHolder(@NonNull View itemView) {
            super(itemView);
            distName = itemView.findViewById(R.id.distName);
            targetToAchieve = itemView.findViewById(R.id.targetToAchieve);
            perDayTarget = itemView.findViewById(R.id.perDayTarget);
            targetAchieved = itemView.findViewById(R.id.targetAchieved);
            targetRemain = itemView.findViewById(R.id.targetRemain);
            llMain = itemView.findViewById(R.id.llMain);


        }
    }

}
