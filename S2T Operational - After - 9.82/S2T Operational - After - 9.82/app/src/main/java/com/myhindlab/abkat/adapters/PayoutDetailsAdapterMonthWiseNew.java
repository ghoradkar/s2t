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

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.MedicineDeliveryAcknowledgement_Activity;
import com.myhindlab.abkat.models.PayoutDetailsModel;
import com.myhindlab.abkat.models.PostCampBeneficiaryListModel;

import java.util.List;

public class PayoutDetailsAdapterMonthWiseNew extends RecyclerView.Adapter<PayoutDetailsAdapterMonthWiseNew.MyViewHolder> {

    private List<PayoutDetailsModel.OutputBean> resultArrayList;
    private Context context;
    private String isAdmin, callType;

    public PayoutDetailsAdapterMonthWiseNew(Context context, List<PayoutDetailsModel.OutputBean> resultArrayList, String isAdmin, String callType) {
        this.context = context;
        this.resultArrayList = resultArrayList;
        this.isAdmin = isAdmin;
        this.callType = callType;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_row_payout_details_month_wise_new, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int pos) {
        final int position = holder.getAdapterPosition();
        final PayoutDetailsModel.OutputBean beneficiaryDetails = resultArrayList.get(position);
//        holder.tv_sr_no.setText((position + 1) + "");


        holder.tv_date.setText(beneficiaryDetails.getCampDate());
        holder.tv_camp_id.setText(beneficiaryDetails.getCampID());
        holder.tv_reg_beneficiary.setText(""+beneficiaryDetails.getTotalRegistartions());
        holder.tv_rejected_bene.setText(""+beneficiaryDetails.getRejection());
        holder.tv_screening_pending.setText(""+beneficiaryDetails.getScreeningPending());
        holder.tv_woker_count.setText(""+beneficiaryDetails.getBillableWorkers());
        holder.tv_dependent_count.setText(""+beneficiaryDetails.getBillableDependent());
        holder.tv_penaulty.setText(""+beneficiaryDetails.getPenaltyAmount());


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
        private TextView tv_sr_no,tv_date,tv_camp_id,tv_reg_beneficiary,tv_penaulty,tv_dependent_count,tv_woker_count,tv_screening_pending,tv_rejected_bene;
        private ImageView imv_remark, imv_fingerprint;
        private LinearLayout ll_buttons;

        public MyViewHolder(View view) {
            super(view);
            ll_row = view.findViewById(R.id.ll_row);
            tv_sr_no = view.findViewById(R.id.tv_sr_no);

            ll_buttons = view.findViewById(R.id.ll_buttons);
            imv_remark = view.findViewById(R.id.imv_remark);
            imv_fingerprint = view.findViewById(R.id.imv_fingerprint);
            tv_date = view.findViewById(R.id.tv_date);
            tv_camp_id = view.findViewById(R.id.tv_camp_id);
            tv_reg_beneficiary = view.findViewById(R.id.tv_reg_beneficiary);
            tv_rejected_bene = view.findViewById(R.id.tv_rejected_bene);
            tv_screening_pending = view.findViewById(R.id.tv_screening_pending);
            tv_woker_count = view.findViewById(R.id.tv_woker_count);
            tv_dependent_count = view.findViewById(R.id.tv_dependent_count);
            tv_penaulty = view.findViewById(R.id.tv_penaulty);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
