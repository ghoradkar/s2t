package com.myhindlab.abkat.activities.payout.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.payout.InvoiceGenModel;
import com.myhindlab.abkat.activities.payout.RaiseRequestActivity;
import com.myhindlab.abkat.activities.payout.model.InvoiceMonthWiseDetailsnModel;

import java.util.List;

public class InvoiceDetailsMonthWiseAdapter extends RecyclerView.Adapter<InvoiceDetailsMonthWiseAdapter.myview> {
    List<InvoiceMonthWiseDetailsnModel.Output> invoiceList;

    Context context;


    public InvoiceDetailsMonthWiseAdapter(Context context, List<InvoiceMonthWiseDetailsnModel.Output> invoiceList) {
        this.context = context;
        this.invoiceList = invoiceList;

    }

    @NonNull
    @Override
    public myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_raise_request, parent, false);
        return new myview(view);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull myview holder, int position) {
        InvoiceMonthWiseDetailsnModel.Output item = invoiceList.get(position);
        holder.setIsRecyclable(false);


        holder.tv_date.setText(item.getCampDate());
        holder.tv_camp_id.setText(item.getCampID());
        holder.tv_reg_beneficiary.setText(""+item.getTotalIndividualBillable());
        holder.tv_rejected_bene.setText(""+item.getTotalIndividualRejected());
        holder.tv_woker_count.setText(""+item.getIndividualBillableWorker());
        holder.tv_dependent_count.setText(""+item.getIndividualBillableDependent());
        holder.tv_penaulty.setText(""+item.getIndividualPenaltyAmount());



//        holder.tv_inVoice.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (item.getInvoiceStatus().equalsIgnoreCase("Raise")) {
//                    context.startActivity(new Intent(context, RaiseRequestActivity.class)
//                            .putExtra("month", item.getInvoiceMonth())
//                            .putExtra("year", item.getInvoiceYear())
//                            .putExtra("userInvoice", item.getUserInviceID())
//
//                    );
//
//                }
//
//            }
//        });


//        holder.tv_inVoice.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onTouchListner.onDataClick(item);
//            }
//        });

//        if (item.getGroupID().equalsIgnoreCase("2")) {
//            holder.llMain.setBackgroundColor(context.getResources().getColor(R.color.greenLight));
//            holder.ll_appointmentDate.setVisibility(View.VISIBLE);
////            holder.ll_appointmentTime.setVisibility(View.VISIBLE);
//        } else if (item.getGroupID().equalsIgnoreCase("3")) {
//            holder.llMain.setBackgroundColor(context.getResources().getColor(R.color.redLight));
//        } else if (item.getGroupID().equalsIgnoreCase("4")) {
//            holder.llMain.setBackgroundColor(context.getResources().getColor(R.color.light_blue));
//        } else if (item.getGroupID().equalsIgnoreCase("1")) {
//            holder.llMain.setBackgroundColor(context.getResources().getColor(R.color.greylight));
//        }else if (item.getGroupID().equalsIgnoreCase("6")){
//            holder.llMain.setBackgroundColor(context.getResources().getColor(R.color.light_yellow));
//        }
//        if (item.getGroupID().equalsIgnoreCase("5") ){
//            holder.llMain.setBackgroundColor(context.getResources().getColor(R.color.purpleLightColor));
//            holder.ll_appointmentDate.setVisibility(View.VISIBLE);
////            holder.ll_appointmentTime.setVisibility(View.VISIBLE);
//        } else if (item.getGroupID().equalsIgnoreCase("7")) {
//            holder.llMain.setBackgroundColor(context.getResources().getColor(R.color.light_orange));
//            holder.ll_appointmentDate.setVisibility(View.GONE);
//        }
//        if (item.getIsWorkerScreened()!=null){
//            if (item.getIsWorkerScreened().equalsIgnoreCase("NO")){
//                holder.ll_NewImvCall.setVisibility(View.GONE);
//            } else {
//                holder.ll_ImvCall.setVisibility(View.VISIBLE);
//            }
//            if (item.getIsWorkerScreened().equalsIgnoreCase("YES")){
//                holder.ll_NewImvCall.setVisibility(View.VISIBLE);
//
//            }else {
//                holder.ll_ImvCall.setVisibility(View.GONE);
//            }
//        }


    }

    @Override
    public int getItemCount() {
        return invoiceList.size();
    }

    class myview extends RecyclerView.ViewHolder {
        private LinearLayout ll_row;
        private TextView tv_sr_no,tv_date,tv_camp_id,tv_reg_beneficiary,tv_penaulty,tv_dependent_count,tv_woker_count,tv_screening_pending,tv_rejected_bene;
        private ImageView imv_remark, imv_fingerprint;
        private LinearLayout ll_buttons;

        public myview(View view) {
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
}
