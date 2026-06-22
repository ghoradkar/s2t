package com.myhindlab.abkat.activities.campApproval.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.campApproval.CampApprovalActivity;
import com.myhindlab.abkat.activities.campApproval.CampStatusListActivity;
import com.myhindlab.abkat.activities.campApproval.models.CampStatusListModel;


import java.util.List;

public class CampStatusListAdapter extends RecyclerView.Adapter<CampStatusListAdapter.CampStatusListViewHolder> {

    private List<CampStatusListModel.Output> list;
    private int status;
    private Context mContext;

    public CampStatusListAdapter(List<CampStatusListModel.Output> list, int status) {
        this.list = list;
        this.status = status;
    }

    @NonNull
    @Override
    public CampStatusListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.camp_status_list_item, parent, false);
        return new CampStatusListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CampStatusListViewHolder holder, int position) {
        CampStatusListModel.Output output = list.get(holder.getAbsoluteAdapterPosition());

        holder.tvCampId.setText(String.valueOf(output.getCampId()));
        holder.tvCampName.setText(output.getCampName());
        holder.tv_date.setText(output.getCampDate());
        holder.tv_location.setText(output.getCampLocation());
        boolean isPartnerCamp = output.getInternalCampFlag().equalsIgnoreCase("IS Partenr Camp");
        boolean isRegularPartnerCamp = output.getInternalCampFlag().equalsIgnoreCase("IS Partenr Camp") && (output.getPartnerIDForCampCreate() == null || output.getPartnerIDForCampCreate() == 0);
        if (isPartnerCamp) {
            holder.tvCampType.setText(output.getiSCampType().equalsIgnoreCase("0") ? "Regular Camp" + " (" + "Channel Partner" + ")" : "D2D Camp" + " (" + "Channel Partner" + ")");

        } else {
            holder.tvCampType.setText(output.getiSCampType().equalsIgnoreCase("0") ? "Regular Camp" : "D2D Camp");

        }
        holder.tv_status.setText(status == 0 ? "Pending" : status == 1 ? "Approved" : status == 3 ? "Hold" : "Rejected");
        if (status == 0) {
            holder.tv_status.setTextColor(mContext.getResources().getColor(R.color.orange));
        } else if (status == 1) {
            holder.tv_status.setTextColor(mContext.getResources().getColor(R.color.green));
        } else if (status == 3) {
            holder.tv_status.setTextColor(mContext.getResources().getColor(R.color.red1));
        } else {
            holder.tv_status.setTextColor(mContext.getResources().getColor(R.color.red));

        }
        holder.tv_registered.setText(String.valueOf(output.getExpectedbeneficiarycount()));
        holder.tv_districtname.setText("District-" + output.getDistname());

        holder.ll_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((CampStatusListActivity) mContext).startActivityForResult(new Intent(mContext, CampApprovalActivity.class).putExtra("campId", output.getCampId()).putExtra("campDate", output.getCampDate()).putExtra("campStatus", status).putExtra("campDistLgdCode", output.getDistlgdcode()).putExtra("campType", output.getiSCampType()).putExtra("campStatus", status).putExtra("isPartnerCamp", isRegularPartnerCamp), 101);
            }
        });

        if (output.getRemark() != null) {
            if (!output.getRemark().isEmpty()) {
                String[] remarkArr = output.getRemark().split(",");
                holder.tv_device_remark.setText(remarkArr[0]);
                if (remarkArr.length > 1) {
                    if (remarkArr[1] != null)
                        holder.tv_consumable_remark.setText(remarkArr[1]);
                    if (remarkArr[2] != null)
                        holder.tv_resource_remark.setText(remarkArr[2]);
                }
            }
        } else {
            holder.llDeviceRemark.setVisibility(View.GONE);
            holder.llConsumableRemark.setVisibility(View.GONE);
            holder.llResourceRemark.setVisibility(View.GONE);
        }

//        if (isPartnerCamp) {
//            holder.llDeviceRemark.setVisibility(View.GONE);
//            holder.llConsumableRemark.setVisibility(View.GONE);
//            if (output.getRemark() != null) {
//                if (!output.getRemark().isEmpty()) {
//                    holder.tv_resource_remark.setText(output.getRemark());
//
//                }
//            }
//        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class CampStatusListViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCampType, tv_districtname, tv_date, tvCampName, tv_status, tv_location, tv_registered, tvCampId, tv_device_remark, tv_consumable_remark, tv_resource_remark;
        private LinearLayout ll_row;
        private LinearLayoutCompat llDeviceRemark, llConsumableRemark, llResourceRemark;

        public CampStatusListViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_registered = itemView.findViewById(R.id.tv_registered);
            tv_districtname = itemView.findViewById(R.id.tv_districtname);
            tv_date = itemView.findViewById(R.id.tv_date);
            tvCampName = itemView.findViewById(R.id.tvCampName);
            tv_status = itemView.findViewById(R.id.tv_status);
            tv_location = itemView.findViewById(R.id.tv_location);
            ll_row = itemView.findViewById(R.id.ll_row);
            tvCampId = itemView.findViewById(R.id.tvCampId);
            tv_device_remark = itemView.findViewById(R.id.tv_device_remark);
            tv_consumable_remark = itemView.findViewById(R.id.tv_consumable_remark);
            tv_resource_remark = itemView.findViewById(R.id.tv_resource_remark);
            llResourceRemark = itemView.findViewById(R.id.llResourceRemark);
            llConsumableRemark = itemView.findViewById(R.id.llConsumableRemark);
            llDeviceRemark = itemView.findViewById(R.id.llDeviceRemark);
            tvCampType = itemView.findViewById(R.id.tvCampType);

        }
    }
}
