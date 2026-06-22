package com.myhindlab.abkat.adapters;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.doortodoor.BeneficiaryVerificationActivity;
import com.myhindlab.abkat.activities.doortodoor.BeneficiaryVerificationActivityNew;
import com.myhindlab.abkat.activities.doortodoor.BeneficiaryVerificationRationCardActivity;
import com.myhindlab.abkat.models.CampBeneficiaryListModel;

import java.util.List;

public class CampBeneficiaryAdapter extends RecyclerView.Adapter<CampBeneficiaryAdapter.MyViewHolder> {

    private List<CampBeneficiaryListModel.OutputBean> resultArrayList;
    private Context context;

    public CampBeneficiaryAdapter(Context context, List<CampBeneficiaryListModel.OutputBean> resultArrayList) {
        this.context = context;
        this.resultArrayList = resultArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_row_campbeneficiary, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int pos) {
        final int position = holder.getAdapterPosition();
        final CampBeneficiaryListModel.OutputBean beneficiaryDetails = resultArrayList.get(position);
        holder.tv_srno.setText((position + 1) + "");

        holder.tv_regno.setText(beneficiaryDetails.getScreeningPatientID());
        holder.tv_name.setText(beneficiaryDetails.getName());
        holder.tv_genderage.setText(beneficiaryDetails.getGENDER() + " / " + beneficiaryDetails.getAGE());

        if ((position % 2) == 0) {
            holder.ll_row.setBackgroundColor(context.getResources().getColor(R.color.white));
        } else {
            holder.ll_row.setBackgroundColor(context.getResources().getColor(R.color.lightGray));
        }
//
        if (beneficiaryDetails.getIsApproved() != null) {

            if (beneficiaryDetails.getIsApproved().equalsIgnoreCase("2")) {
                holder.ll_row.setBackgroundColor(context.getResources().getColor(R.color.redLight));
            } else if (beneficiaryDetails.getIsApproved().equalsIgnoreCase("1")) {
                holder.ll_row.setBackgroundColor(context.getResources().getColor(R.color.light_green));
            } else if (beneficiaryDetails.getIsApproved().equalsIgnoreCase("3")) {
                holder.ll_row.setBackgroundColor(context.getResources().getColor(R.color.light_blue));
            }
        }

//        if (beneficiaryDetails.getIsAudioTestReverification().equalsIgnoreCase("1")
//                || beneficiaryDetails.getIsVisionTestReverification().equalsIgnoreCase("1")
//                || beneficiaryDetails.getIsLFTTestReverification().equalsIgnoreCase("1")
//                || beneficiaryDetails.getPhotoVerificationStatusID() > 0) {
//            holder.ll_row.setBackgroundColor(context.getResources().getColor(R.color.light_blue));
//
//        }

        holder.ll_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, BeneficiaryVerificationRationCardActivity.class)
                        .putExtra("beneficiaryDetails", beneficiaryDetails));
            }
        });
    }

    @Override
    public int getItemCount() {
        return resultArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout ll_row;
        private TextView tv_regno, tv_name, tv_genderage, tv_srno;

        public MyViewHolder(View view) {
            super(view);
            ll_row = view.findViewById(R.id.ll_row);
            tv_regno = view.findViewById(R.id.tv_regno);
            tv_name = view.findViewById(R.id.tv_name);
            tv_genderage = view.findViewById(R.id.tv_genderage);
            tv_srno = view.findViewById(R.id.tv_srno);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
