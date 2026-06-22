package com.myhindlab.abkat.adapters.doortodoor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.doortodoor.D2DCampDetails;

import java.util.ArrayList;

public class D2DCampListAdapter extends RecyclerView.Adapter<D2DCampListAdapter.MyViewHolder> {

    private ArrayList<D2DCampDetails.Output> campList;

    public D2DCampListAdapter(ArrayList<D2DCampDetails.Output> campList) {
        this.campList = campList;
    }

    @Override
    public D2DCampListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.d2d_camp_list_item, parent, false);
        return new D2DCampListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final D2DCampListAdapter.MyViewHolder holder, final int position) {
        D2DCampDetails.Output data = campList.get(position);

        holder.tvCampId.setText(String.valueOf(data.getCampId()));
        holder.tvAddress.setText(data.getCampLocation());
        holder.tvToday.setText(data.getCampDate());
        holder.tvDistrict.setText(data.getDISTNAME());
        holder.tvCampName.setText(data.getCampName());
        holder.tvCampType.setText(data.getCampTypeDescription());
        holder.tv_InitiatedBy.setText(data.getInitiatedBy1());
        holder.tv_CreatedBy.setText(data.getCampCreatedBy());




    }

    @Override
    public int getItemCount() {
        return campList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvAddress, tvToday, tvCampId, tvDistrict,tvCampName,tvCampType,tv_InitiatedBy,tv_CreatedBy;
        private LinearLayoutCompat cvCampSelection;

        public MyViewHolder(final View view) {
            super(view);
            tvCampId = view.findViewById(R.id.tvCampId);
            tvAddress = view.findViewById(R.id.tvAddress);
            tvToday = view.findViewById(R.id.tvToday);
            tvDistrict = view.findViewById(R.id.tvDistrict);
            cvCampSelection = view.findViewById(R.id.cvCampSelection);
            tvCampName = view.findViewById(R.id.tvCampName);
            tvCampType = view.findViewById(R.id.tvCampType);
            tv_InitiatedBy = view.findViewById(R.id.tv_InitiatedBy);
            tv_CreatedBy = view.findViewById(R.id.tv_CreatedBy);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
