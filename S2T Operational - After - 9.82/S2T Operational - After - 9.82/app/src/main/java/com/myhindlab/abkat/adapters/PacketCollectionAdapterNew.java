package com.myhindlab.abkat.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.MedicineDeliveryAcknowledgement_Activity;
import com.myhindlab.abkat.models.PacketCollectModel;
import com.myhindlab.abkat.models.PostCampBeneficiaryListModel;

import java.util.List;

public class PacketCollectionAdapterNew extends RecyclerView.Adapter<PacketCollectionAdapterNew.MyViewHolder> {

    private List<PacketCollectModel.Output> resultArrayList;
    private Context context;
    private String isAdmin, callType;

    public PacketCollectionAdapterNew(Context context, List<PacketCollectModel.Output> resultArrayList, String isAdmin, String callType) {
        this.context = context;
        this.resultArrayList = resultArrayList;
        this.isAdmin = isAdmin;
        this.callType = callType;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_row_packet_collection, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int pos) {
        final int position = holder.getAdapterPosition();
        final PacketCollectModel.Output data = resultArrayList.get(position);


//
        holder.tv_from_lab.setText(data.getLabName());
        holder.tv_process_lab.setText(data.getDISTNAME());
        holder.tv_campInfo.setText(data.getPacketNumber());


        holder.cb_collect.setChecked(data.isChecked());
        holder.cb_collect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                data.setChecked(checked);
            }
        });


//        if (callType.equals("2"))
//            holder.ll_buttons.setVisibility(View.GONE);

//        if (isAdmin.equals("1") && callType.equals("1")) {
////            holder.ll_buttons.setVisibility(View.GONE);
//            if (beneficiaryDetails.getPostFilePath().equals("NA")) {
//                holder.tv_sr_no.setTextColor(context.getResources().getColor(R.color.red));
//                holder.tv_patient.setTextColor(context.getResources().getColor(R.color.red));
//                holder.tv_age.setTextColor(context.getResources().getColor(R.color.red));
//                holder.tv_gender.setTextColor(context.getResources().getColor(R.color.red));
//            } else {
//                holder.tv_sr_no.setTextColor(context.getResources().getColor(R.color.green));
//                holder.tv_patient.setTextColor(context.getResources().getColor(R.color.green));
//                holder.tv_age.setTextColor(context.getResources().getColor(R.color.green));
//                holder.tv_gender.setTextColor(context.getResources().getColor(R.color.green));
//            }
//        }

//        if (beneficiaryDetails.getPostFilePath().equals("NA"))
//            holder.imv_remark.setColorFilter(ContextCompat.getColor(context, R.color.red));
//        else
//            holder.imv_remark.setColorFilter(ContextCompat.getColor(context, R.color.green));
//
//        if (beneficiaryDetails.getDelivarystatus().equals("N"))
//            holder.cv_MainLL.setCardBackgroundColor(ContextCompat.getColor(context, R.color.red));
//        else
//            holder.imv_fingerprint.setColorFilter(ContextCompat.getColor(context, R.color.green));

//        if (beneficiaryDetails.getOTPStatus().equals("NA"))
//            holder.imv_fingerprint.setColorFilter(ContextCompat.getColor(context,R.color.red));
//        else
//            holder.imv_fingerprint.setColorFilter(ContextCompat.getColor(context, R.color.green));

//9765424584

//        holder.ll_row.setOnClickListener(v ->
//                context.startActivity(new Intent(context, MedicineDeliveryAcknowledgement_Activity.class)
//                        .putExtra("beneficiaryDetails", beneficiaryDetails)
//                        .putExtra("isAdmin", isAdmin))
//        );

//        holder.ll_row.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isAdmin.equals("1") && callType.equals("1"))
//                    context.startActivity(new Intent(context, PostCampDoctorRemarkPhotoUpload_Activity.class)
//                            .putExtra("beneficiaryDetails", beneficiaryDetails)
//                            .putExtra("isAdmin", isAdmin));
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return resultArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout ll_row;
        private TextView tv_sr_no, tv_from_lab, tv_process_lab, tv_campInfo, tv_view_upload;
        private ImageView imv_remark, imv_fingerprint;
        private LinearLayout ll_buttons;
        private CheckBox cb_collect;

        private CardView cv_MainLL;

        public MyViewHolder(View view) {
            super(view);
            ll_row = view.findViewById(R.id.ll_row);
            tv_sr_no = view.findViewById(R.id.tv_sr_no);
            tv_from_lab = view.findViewById(R.id.tv_from_lab);
            tv_process_lab = view.findViewById(R.id.tv_process_lab);
            tv_campInfo = view.findViewById(R.id.tv_campInfo);
            ll_buttons = view.findViewById(R.id.ll_buttons);
            tv_view_upload = view.findViewById(R.id.tv_view_upload);
            imv_remark = view.findViewById(R.id.imv_remark);
            imv_fingerprint = view.findViewById(R.id.imv_fingerprint);
            cb_collect = view.findViewById(R.id.cb_collect);
            cv_MainLL = view.findViewById(R.id.cv_MainLL);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
