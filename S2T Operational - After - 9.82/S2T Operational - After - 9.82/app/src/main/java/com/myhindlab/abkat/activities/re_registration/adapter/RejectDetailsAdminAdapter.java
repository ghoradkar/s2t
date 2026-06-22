package com.myhindlab.abkat.activities.re_registration.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.doortodoor.D2dTeamsNotWorkingTeamActivity;
import com.myhindlab.abkat.activities.re_registration.model.BeneficiaryCountForPageLoadModel;
import com.myhindlab.abkat.models.doortodoor.AdminActiveInactiveModel;

import java.util.List;

public class RejectDetailsAdminAdapter extends RecyclerView.Adapter<RejectDetailsAdminAdapter.MyViewHolder> {

    private final List<BeneficiaryCountForPageLoadModel.Output> adminList;
    private Context context;


    public RejectDetailsAdminAdapter(Context context, List<BeneficiaryCountForPageLoadModel.Output> adminList) {
        this.context = context;
        this.adminList = adminList;


    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_reject_teacking_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        BeneficiaryCountForPageLoadModel.Output data = adminList.get(position);
//
        holder.tv_patientname.setText(""+data.getDistrict());
        holder.tv_type.setText(""+data.getRejectedBeneficiaries());
        holder.tv_cardno.setText(""+data.getInterestedInScreening());
        holder.tv_notInterested.setText(""+data.getNotInterestedInScreening());
        holder.tv_denied.setText(""+data.getDeniedForScreening());
        holder.tv_rescreening.setText(""+data.getReScreenedBeneficiaries());
//        holder.tvCampCoordinatorName.setText(data.getCampCoordinatorName());
//        holder.tvNotWTeamNew.setText(String.valueOf(data.getNonWorkingTeamCount()));
//        holder.tvWorkingTeamNew.setText(String.valueOf(data.getWorkingTeamCount()));
//        if (holder.tvCampCoordinatorName.getText().toString().matches("NA")){
//            holder.imvCall.setEnabled(false);
//        }



//        holder.imvCall.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + data.getCampCoordinatorMobNo()));
//                context.startActivity(intent);
//            }
//
//        });




//        holder.tv_ResourceName.setText(data.getMemberName() + "(Team No. :- " + data.getTeamNumber() + ")");


    }

    @Override
    public int getItemCount() {
        return adminList!=null?adminList.size():0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_type,tv_patientname,tv_cardno,tv_notInterested,tv_denied,tv_rescreening;
        ImageView imvCall;



        public MyViewHolder(final View view) {
            super(view);
            tv_type = view.findViewById(R.id.tv_type);
            tv_patientname = view.findViewById(R.id.tv_patientname);
            tv_cardno = view.findViewById(R.id.tv_cardno);
            tv_notInterested = view.findViewById(R.id.tv_notInterested);
            tv_denied = view.findViewById(R.id.tv_denied);
            tv_rescreening = view.findViewById(R.id.tv_rescreening);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}