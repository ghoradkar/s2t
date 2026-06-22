package com.myhindlab.abkat.activities.regularcampcreation.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.regularcampcreation.model.AllocatedSubDeviceListModel;
import com.myhindlab.abkat.activities.regularcampcreation.model.DeviceDetailsForApprovalModel;
import com.myhindlab.abkat.activities.regularcampcreation.model.DeviceListModel;
import com.myhindlab.abkat.activities.regularcampcreation.model.SubDeviceListModel;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.List;

public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.DeviceListViewHolder> {
    private List<DeviceListModel.Output> list;
    private List<DeviceDetailsForApprovalModel.Output> approvalList;
    @Nullable
    private DeviceEvent deviceEvent;
    private int type = 0;

    public interface DeviceEvent {
        void onDeviceSelected(DeviceListModel.Output output);

        void onApproveDeviceSelected(DeviceDetailsForApprovalModel.Output output);

    }

    public DeviceListAdapter(List<DeviceListModel.Output> list, @Nullable DeviceEvent deviceEvent) {
        this.list = list;
        this.deviceEvent = deviceEvent;
    }

    public DeviceListAdapter(List<DeviceDetailsForApprovalModel.Output> list, @Nullable DeviceEvent deviceEvent, int type) {
        this.approvalList = list;
        this.type = type;
        this.deviceEvent = deviceEvent;

    }

    @NonNull
    @Override
    public DeviceListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.device_list_item, parent, false);

        return new DeviceListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceListViewHolder holder, int position) {
        if (type == 0) {
            DeviceListModel.Output output = list.get(holder.getAdapterPosition());

            holder.deviceName.setText(output.getDeviceName());
            holder.tvQuantity.setText(String.valueOf(output.getRequiredDevice()));
            if (output.getSubDevices() != null) {
                int selSubDevices = 0;
                for (SubDeviceListModel.Output subDevice : output.getSubDevices()) {
                    if (subDevice.isChecked()) {
                        selSubDevices = selSubDevices + 1;
                    }
                }

                if (selSubDevices >= output.getRequiredDevice())
                    holder.edtSubDevices.setText(String.valueOf(selSubDevices));
                else
                    holder.edtSubDevices.setError("Select " + output.getRequiredDevice() + " device");

            }
            holder.edtSubDevices.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deviceEvent.onDeviceSelected(output);
                }
            });

        } else {

            DeviceDetailsForApprovalModel.Output output = approvalList.get(holder.getAdapterPosition());

            holder.deviceName.setText(output.getDeviceName());
            holder.tvQuantity.setText(String.valueOf(output.getDevicecount()));
            holder.edtSubDevices.setText(output.getDeviceSerialNumbers());
            if (output.getSubDevices() != null) {
                int selSubDevices = 0;
                for (AllocatedSubDeviceListModel.Output subDevice : output.getSubDevices()) {
                    if (subDevice.isChecked() || subDevice.getDeviceStatus().equalsIgnoreCase("Selected")) {
                        selSubDevices = selSubDevices + 1;
                    }
                }

                if (selSubDevices >= output.getDevicecount())
                    holder.edtSubDevices.setText(String.valueOf(selSubDevices));
                else
                    holder.edtSubDevices.setError("Select " + output.getDevicecount() + " device");

            }

            holder.edtSubDevices.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deviceEvent.onApproveDeviceSelected(output);
                }
            });


        }


    }

    @Override
    public int getItemCount() {
        return type == 0 ? list.size() : approvalList.size();
    }

    public int getItemViewType(int position) {
        return position;
    }

    public class DeviceListViewHolder extends RecyclerView.ViewHolder {
        private TextView deviceName, tvQuantity, expectedBeneficiary;
        public MaterialEditText edtSubDevices;

        public DeviceListViewHolder(@NonNull View itemView) {
            super(itemView);

            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            deviceName = itemView.findViewById(R.id.tvDeviceName);
            edtSubDevices = itemView.findViewById(R.id.edtSubDevices);
        }
    }
}
