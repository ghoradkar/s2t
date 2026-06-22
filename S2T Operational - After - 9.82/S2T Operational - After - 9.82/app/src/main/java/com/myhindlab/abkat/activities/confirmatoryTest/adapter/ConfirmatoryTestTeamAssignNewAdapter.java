package com.myhindlab.abkat.activities.confirmatoryTest.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.confirmatoryTest.AssignTeamConfirmatoryTestActivity;
import com.myhindlab.abkat.activities.confirmatoryTest.AssignTeamConfirmatoryTestNewActivity;
import com.myhindlab.abkat.models.CampBeneficiaryListModel;

import java.util.List;

public class ConfirmatoryTestTeamAssignNewAdapter extends RecyclerView.Adapter<ConfirmatoryTestTeamAssignNewAdapter.MyViewHolder> {

    private final List<CampBeneficiaryListModel.OutputBean> teamList;
    private Context context;


    public ConfirmatoryTestTeamAssignNewAdapter(Context context, List<CampBeneficiaryListModel.OutputBean> teamList) {
        this.context = context;
        this.teamList = teamList;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.calllist_layout_team_assign_new, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int pos) {
        int position = holder.getAbsoluteAdapterPosition();
        final CampBeneficiaryListModel.OutputBean beneficiaryDetails = teamList.get(position);

//        holder.tvSr_No.setText("" + (position + 1));
        holder.tv_beniname.setText(beneficiaryDetails.getBeneficiaryName());
        holder.tv_sr_no.setText(beneficiaryDetails.getBeneficiaryName());
        holder.tv_Beneficiary_No.setText(beneficiaryDetails.getRegdno());
        holder.tv_address.setText(beneficiaryDetails.getAddress());
        holder.tv_date.setText(beneficiaryDetails.getPinCode());
        holder.tv_sampleCollection.setText(beneficiaryDetails.getSampleCollection());
        holder.tv_isteamAssigned.setText(beneficiaryDetails.getIsTeamAssign());
        holder.lin_samplecollection.setVisibility(View.GONE);
        holder.lin_team_assign.setVisibility(View.GONE);
        holder.tv_ctassigignremark.setText(beneficiaryDetails.getAssignmentRemarks());
        if (beneficiaryDetails.getAppointmentDate()!=null){
            holder.tv_appointment_date.setText(beneficiaryDetails.getAppointmentDate());
        }

        if (beneficiaryDetails.getIsEmergency()!=null){
            holder.tv_emergency.setText(beneficiaryDetails.getIsEmergency());
        }


//        holder.tv_address.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

//        holder.tvDesignation.setText(String.valueOf(data.getMemberCount()));
        //  holder.tvWorkingTeamNew.setText(String.valueOf(data.getWorkingTeamCount()));


//        holder.imvCall.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + beneficiaryDetails.getMobileNo()));
//                context.startActivity(intent);
//            }
//        });


        holder.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (beneficiaryDetails.getArId()!=null){
//                    if (beneficiaryDetails.getArId().equalsIgnoreCase("4")||beneficiaryDetails.getArId().equalsIgnoreCase("5")){
//                        Utilities.showAlertDialog(context,"Alert","You cannot assign this beneficiary any team or executive if sample collection  denied by beneficiary",false);
//                        return;
//                    }
//                }


                context.startActivity(new Intent(context, AssignTeamConfirmatoryTestNewActivity.class)
                        .putExtra("beneficiaryDetails", beneficiaryDetails));

            }
        });


//        holder.tv_ResourceName.setText(data.getMemberName() + "(Team No. :- " + data.getTeamNumber() + ")");


    }

    @Override
    public int getItemCount() {
        return teamList != null ? teamList.size() : 0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvSrNo, tv_sr_no, tvSr_No, tv_ctassigignremark,tv_appointment_date,tv_emergency,tv_isteamAssigned, tv_beniname, tv_sampleCollection, tv_Beneficiary_No, tv_date, tvDesignation;
        public ImageView imvCall;
        public LinearLayoutCompat llMain;
        public EditText tv_address;
        private LinearLayoutCompat lin_samplecollection;
        private LinearLayoutCompat lin_team_assign;

        public MyViewHolder(final View view) {
            super(view);
            tvSrNo = view.findViewById(R.id.tvSrNo);
            tvSr_No = view.findViewById(R.id.tvSr_No);
            tv_sr_no = view.findViewById(R.id.tv_sr_no);
            tv_beniname = view.findViewById(R.id.tv_beniname);
            tv_date = view.findViewById(R.id.tv_date);
            tvDesignation = view.findViewById(R.id.tvDesignation);
            tv_sampleCollection = view.findViewById(R.id.tv_sampleCollection);
            tv_Beneficiary_No = view.findViewById(R.id.tv_Beneficiary_No);
            imvCall = view.findViewById(R.id.imvCall);
            tv_address = view.findViewById(R.id.tv_address);
            llMain = view.findViewById(R.id.llMain);
            tv_isteamAssigned = view.findViewById(R.id.tv_isteamAssigned);
            lin_samplecollection = view.findViewById(R.id.lin_samplecollection);
            lin_team_assign = view.findViewById(R.id.lin_team_assign);
            tv_appointment_date = view.findViewById(R.id.tv_appointment_date);
            tv_ctassigignremark = view.findViewById(R.id.tv_ctassigignremark);
            tv_emergency = view.findViewById(R.id.tv_emergency);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}