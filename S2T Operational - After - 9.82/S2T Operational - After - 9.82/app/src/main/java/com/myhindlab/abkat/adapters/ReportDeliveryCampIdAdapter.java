package com.myhindlab.abkat.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.CampListModel;
import com.myhindlab.abkat.models.TeamsDetailsModel;

import java.util.ArrayList;
import java.util.List;

public class ReportDeliveryCampIdAdapter extends RecyclerView.Adapter<ReportDeliveryCampIdAdapter.MyViewHolder> {

    private final List<CampListModel.OutputBean> teamList;

    public ReportDeliveryCampIdAdapter(List<CampListModel.OutputBean> teamList) {
        this.teamList = teamList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_camplist_report_delivery_, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        CampListModel.OutputBean data = teamList.get(position);
        holder.tv_camp_id.setText(data.getCampId());
        holder.tv_camp_name.setText(data.getCampName());
        holder.tv_camp_date.setText(data.getCampDate());
        holder.tv_camp_location.setText(data.getCampLocation());
//        // holder.btnRemove.setText(data.getMember2());
    }

    @Override
    public int getItemCount() {
        return teamList!=null?teamList.size():0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTeamId, tvMemberOne,tv_camp_id,tv_camp_name,tv_camp_date,tv_camp_location, tvMemberTwo;
        public ImageButton btnRemove;


        public MyViewHolder(final View view) {
            super(view);
            tvMemberOne = view.findViewById(R.id.tvMemberOne);
            tvTeamId = view.findViewById(R.id.tvTeamId);
            tvMemberTwo = view.findViewById(R.id.tvMemberTwo);
            btnRemove = view.findViewById(R.id.btnRemove);
            tv_camp_id = view.findViewById(R.id.tv_camp_id);
            tv_camp_name = view.findViewById(R.id.tv_camp_name);
            tv_camp_date = view.findViewById(R.id.tv_camp_date);
            tv_camp_location = view.findViewById(R.id.tv_camp_location);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}