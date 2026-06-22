package com.myhindlab.abkat.activities.regularcampcreation.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.regularcampcreation.model.ConsumableDetailsForApprovalModel;
import com.myhindlab.abkat.activities.regularcampcreation.model.ConsumableListModel;


import java.util.List;

public class ConsumableListAdapter extends RecyclerView.Adapter<ConsumableListAdapter.DeviceListViewHolder> {
    private List<ConsumableListModel.Output> list;
    private List<ConsumableDetailsForApprovalModel.Output> approvalList;
    private int type = 0;

    public ConsumableListAdapter(List<ConsumableListModel.Output> list) {
        this.list = list;
    }

    public ConsumableListAdapter(List<ConsumableDetailsForApprovalModel.Output> list, int type) {
        this.approvalList = list;
        this.type = type;
    }

    @NonNull
    @Override
    public DeviceListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.consumable_list_item_camp_creation, parent, false);

        return new DeviceListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceListViewHolder holder, int position) {
        if (type == 0) {
            ConsumableListModel.Output output = list.get(holder.getAdapterPosition());

            holder.tvConsumableName.setText(output.getProductName());
            holder.tvPerPersonInventory.setText(String.valueOf(output.getProductQuantity()));
            holder.tvConsumableExpectedQuantity.setText(String.valueOf(output.getExpectedQuantity()));
            holder.tvStockAvailableInInventory.setText(String.valueOf(output.getAvailabelstock()).equalsIgnoreCase("null") ? "-" : String.valueOf(output.getAvailabelstock()));

        } else {
            ConsumableDetailsForApprovalModel.Output output = approvalList.get(holder.getAdapterPosition());

            holder.tvConsumableName.setText(output.getProductName());
            holder.tvPerPersonInventory.setVisibility(View.GONE);
            holder.tvConsumableExpectedQuantity.setText(String.valueOf(output.getExpectedQuantity()));
            holder.tvStockAvailableInInventory.setText(String.valueOf(output.getAvailabelstock()).equalsIgnoreCase("null") ? "-" : String.valueOf(output.getAvailabelstock()));


        }
    }

    @Override
    public int getItemCount() {
        return type == 0 ? list.size() : approvalList.size();
    }


    class DeviceListViewHolder extends RecyclerView.ViewHolder {
        private TextView tvConsumableName, tvPerPersonInventory, tvConsumableExpectedQuantity, tvStockAvailableInInventory;

        public DeviceListViewHolder(@NonNull View itemView) {
            super(itemView);

            tvConsumableName = itemView.findViewById(R.id.tvConsumableName);
            tvPerPersonInventory = itemView.findViewById(R.id.tvPerPersonInventory);
            tvConsumableExpectedQuantity = itemView.findViewById(R.id.tvConsumableExpectedQuantity);
            tvStockAvailableInInventory = itemView.findViewById(R.id.tvStockAvailableInInventory);
        }
    }
}
