package com.myhindlab.abkat.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.MedicineDeliveryAcknowledgement_Activity;
import com.myhindlab.abkat.activities.PayoutDetailsMonthWise;
import com.myhindlab.abkat.models.PayoutDetailsModel;
import com.myhindlab.abkat.models.PostCampBeneficiaryListModel;
import com.myhindlab.abkat.models.doortodoor.D2dWorkingTeamModel;

import java.util.List;

public class PayoutDetailsAdapterNew extends RecyclerView.Adapter<PayoutDetailsAdapterNew.MyViewHolder> {

    private List<PayoutDetailsModel.OutputBean> resultArrayList;
    private Context context;
    private String isAdmin, callType;

    private Paymentreceivedlisner paymentreceivedlisner;

    public   interface Paymentreceivedlisner{
        void onPaymentClick(PayoutDetailsModel.OutputBean team);
    }


    public PayoutDetailsAdapterNew(Context context, List<PayoutDetailsModel.OutputBean> resultArrayList, String isAdmin, String callType,Paymentreceivedlisner paymentreceivedlisner) {
        this.context = context;
        this.resultArrayList = resultArrayList;
        this.isAdmin = isAdmin;
        this.callType = callType;
        this.paymentreceivedlisner =paymentreceivedlisner;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_row_payout_details_new, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int pos) {
        final int position = holder.getAdapterPosition();
        final PayoutDetailsModel.OutputBean beneficiaryDetails = resultArrayList.get(position);
//        holder.tv_sr_no.setText((position + 1) + "");

        holder.tv_billing_cycle.setText(beneficiaryDetails.getBillingCycle());
        holder.tv_worker_count.setText(beneficiaryDetails.getBillableWorker());
        holder.tv_dependent_count.setText(""+beneficiaryDetails.getBillableDependent());
        holder.tv_penalty.setText(""+beneficiaryDetails.getTotalPenalty());
        holder.tv_TDS.setText(beneficiaryDetails.getTDSAmount());
        holder.tv_playable_amt.setText(beneficiaryDetails.getPayableAmount());


        if (beneficiaryDetails.getIsPaymentRecieved().equalsIgnoreCase("1")){
            holder.tv_billing_cycle.setTextColor(ContextCompat.getColor(context, R.color.green));
        }


        holder.tv_billing_cycle.setOnClickListener(v ->
                context.startActivity(new Intent(context, PayoutDetailsMonthWise.class)
                        .putExtra("beneficiaryDetails", beneficiaryDetails)
                        .putExtra("isAdmin", isAdmin))
        );


        holder.tv_playable_amt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paymentreceivedlisner.onPaymentClick(beneficiaryDetails);
            }
        });

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
        private TextView tv_sr_no,tv_billing_cycle,tv_worker_count,tv_dependent_count,tv_playable_amt,tv_penalty,tv_TDS;
        private ImageView imv_remark, imv_fingerprint;
        private LinearLayout ll_buttons;

        public MyViewHolder(View view) {
            super(view);
            ll_row = view.findViewById(R.id.ll_row);
            tv_sr_no = view.findViewById(R.id.tv_sr_no);
            ll_buttons = view.findViewById(R.id.ll_buttons);
            imv_remark = view.findViewById(R.id.imv_remark);
            imv_fingerprint = view.findViewById(R.id.imv_fingerprint);
            tv_billing_cycle = view.findViewById(R.id.tv_billing_cycle);
            tv_worker_count = view.findViewById(R.id.tv_worker_count);
            tv_dependent_count = view.findViewById(R.id.tv_dependent_count);
            tv_penalty = view.findViewById(R.id.tv_penalty);
            tv_TDS = view.findViewById(R.id.tv_TDS);
            tv_playable_amt = view.findViewById(R.id.tv_playable_amt);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
