package com.myhindlab.abkat.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.myhindlab.abkat.BuildConfig;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.MedicineDeliveryAcknowledgementNewActivity;
import com.myhindlab.abkat.activities.MedicineDeliveryAcknowledgement_Activity;
import com.myhindlab.abkat.activities.PostCampThumbSignatureUpload_Activity;
import com.myhindlab.abkat.models.PostCampBeneficiaryListModel;

import java.util.List;

public class MedicineBeneficiaryAdapterNew extends RecyclerView.Adapter<MedicineBeneficiaryAdapterNew.MyViewHolder> {

    private List<PostCampBeneficiaryListModel.OutputBean> resultArrayList;
    private Context context;
    private String isAdmin, callType;

    public MedicineBeneficiaryAdapterNew(Context context, List<PostCampBeneficiaryListModel.OutputBean> resultArrayList, String isAdmin, String callType) {
        this.context = context;
        this.resultArrayList = resultArrayList;
        this.isAdmin = isAdmin;
        this.callType = callType;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_row_medicine_patient_new, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int pos) {
        final int position = holder.getAdapterPosition();
        final PostCampBeneficiaryListModel.OutputBean beneficiaryDetails = resultArrayList.get(position);
        holder.tv_sr_no.setText((position + 1) + "");

        holder.tv_patient.setText(beneficiaryDetails.getPatient_Name());
        holder.tv_age.setText(beneficiaryDetails.getDeliveryChallanID());
        holder.tv_gender.setText(beneficiaryDetails.getGender());

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

//        if (beneficiaryDetails.getDelivarystatus().equals("N"))
//            holder.imv_fingerprint.setColorFilter(ContextCompat.getColor(context, R.color.red));
//        else
//            holder.imv_fingerprint.setColorFilter(ContextCompat.getColor(context, R.color.green));

        if (beneficiaryDetails.getDeliveryStatusRemarkID()!=null){

            if (beneficiaryDetails.getDeliveryStatusRemarkID().equals("0")){
                holder.imv_fingerprint.setColorFilter(ContextCompat.getColor(context, R.color.yellow));

            }else if (beneficiaryDetails.getDeliveryStatusRemarkID().equals("1")){
                holder.imv_fingerprint.setColorFilter(ContextCompat.getColor(context, R.color.green));

            }else if (beneficiaryDetails.getDeliveryStatusRemarkID().equals("2")){
                holder.imv_fingerprint.setColorFilter(ContextCompat.getColor(context, R.color.blue));

            }else if (beneficiaryDetails.getDeliveryStatusRemarkID().equals("3")){
                holder.imv_fingerprint.setColorFilter(ContextCompat.getColor(context, R.color.red));

            }

        }

//        if (beneficiaryDetails.getOTPStatus().equals("NA"))
//            holder.imv_fingerprint.setColorFilter(ContextCompat.getColor(context,R.color.red));
//        else
//            holder.imv_fingerprint.setColorFilter(ContextCompat.getColor(context, R.color.green));

//9765424584


        if (BuildConfig.isBeta){
            holder.ll_row.setOnClickListener(v ->


                    context.startActivity(new Intent(context, MedicineDeliveryAcknowledgementNewActivity.class)
                            .putExtra("beneficiaryDetails", beneficiaryDetails)
                            .putExtra("isAdmin", isAdmin))

            );

        }else {
            holder.ll_row.setOnClickListener(v ->


                    context.startActivity(new Intent(context, MedicineDeliveryAcknowledgementNewActivity.class)
                            .putExtra("beneficiaryDetails", beneficiaryDetails)
                            .putExtra("isAdmin", isAdmin))

            );
        }

//
//        holder.ll_row.setOnClickListener(v ->
//
//
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
        private TextView tv_sr_no, tv_patient, tv_age, tv_gender, tv_view_upload;
        private ImageView imv_remark, imv_fingerprint;
        private LinearLayout ll_buttons;

        public MyViewHolder(View view) {
            super(view);
            ll_row = view.findViewById(R.id.ll_row);
            tv_sr_no = view.findViewById(R.id.tv_sr_no);
            tv_patient = view.findViewById(R.id.tv_patient);
            tv_age = view.findViewById(R.id.tv_age);
            tv_gender = view.findViewById(R.id.tv_gender);
            ll_buttons = view.findViewById(R.id.ll_buttons);
            tv_view_upload = view.findViewById(R.id.tv_view_upload);
            imv_remark = view.findViewById(R.id.imv_remark);
            imv_fingerprint = view.findViewById(R.id.imv_fingerprint);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
