package com.myhindlab.abkat.adapters;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.GetApprovedCampListDetailsForAppList;

import java.util.ArrayList;

public class CampListAdapter extends RecyclerView.Adapter<CampListAdapter.MyViewHolder> {

    private ArrayList<GetApprovedCampListDetailsForAppList> campList;

    public CampListAdapter(ArrayList<GetApprovedCampListDetailsForAppList> campList) {
        this.campList = campList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_campdetails, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        GetApprovedCampListDetailsForAppList data = campList.get(position);

        holder.tv_campname.setText(data.getCampId());
        holder.tv_address.setText(data.getCampLocation());
       // holder.tv_CampName.setText(data.getCampLocation());
        holder.tv_CampType.setText(data.getCampTypeDescription());
        holder.tv_InitiatedBy.setText(data.getInitiatedBy1());

    }

    @Override
    public int getItemCount() {
        return campList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_campname, tv_address,tv_CampType,tv_CampName,tv_InitiatedBy;

        public MyViewHolder(final View view) {
            super(view);
            tv_campname = view.findViewById(R.id.tv_campname);
            tv_address = view.findViewById(R.id.tv_address);
            tv_InitiatedBy = view.findViewById(R.id.tv_InitiatedBy);
            tv_CampType = view.findViewById(R.id.tv_CampType);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}