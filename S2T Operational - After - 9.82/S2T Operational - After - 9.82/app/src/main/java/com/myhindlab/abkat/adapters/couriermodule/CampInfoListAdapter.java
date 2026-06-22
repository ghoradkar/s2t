package com.myhindlab.abkat.adapters.couriermodule;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.couriermodule.GetInfoModel;
import com.myhindlab.abkat.models.doortodoor.D2DCampDetails;

import java.util.ArrayList;

public class CampInfoListAdapter extends RecyclerView.Adapter<CampInfoListAdapter.MyViewHolder> {

    private ArrayList<GetInfoModel.Output> campinfolist;

    public CampInfoListAdapter(ArrayList<GetInfoModel.Output> campinfolist) {
        this.campinfolist = campinfolist;
    }

    @Override
    public CampInfoListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.d2d_camp_info_item, parent, false);
        return new CampInfoListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CampInfoListAdapter.MyViewHolder holder, final int position) {
        GetInfoModel.Output data = campinfolist.get(position);

        holder.tvCampId.setText(String.valueOf(data.getCampID()));
//        holder.tvAddress.setText(data.getCampLocation());
//        holder.tvToday.setText(data.getCampDate());
//        holder.tvDistrict.setText(data.getDISTNAME());
//        holder.tvCampName.setText(data.getCampName());
        holder.tvCampType.setText(data.getCampNo());
//        holder.tv_InitiatedBy.setText(data.getInitiatedBy1());
//        holder.tv_CreatedBy.setText(data.getCampCreatedBy());
        holder.tvDate.setText(data.getCampDate());





    }

    @Override
    public int getItemCount() {
        return campinfolist.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvAddress, tvToday, tvCampId, tvDistrict,tvCampName,tvCampType,tv_InitiatedBy,tv_CreatedBy,tvDate;
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
            tvDate = view.findViewById(R.id.tvDate);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
