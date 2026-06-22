package com.myhindlab.abkat.appointment_confirmation.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.appointment_confirmation.pojo.OutputItem;

import java.util.List;

public class CallListAdaptor extends RecyclerView.Adapter<CallListAdaptor.myview> {
    List<OutputItem> outputItems;
    Onclicklisner onclicklisner;
    Context context;

    public interface Onclicklisner {
        public void onclick(OutputItem item);

        public void onImvClick(OutputItem item);
    }

    public CallListAdaptor(Context context, List<OutputItem> outputItems, Onclicklisner onclicklisner ) {
        this.outputItems = outputItems;
        this.onclicklisner = onclicklisner;
        this.context = context;
    }

    @NonNull
    @Override
    public myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.calllist_layout, parent, false);
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
        OutputItem item = outputItems.get(position);
        holder.setIsRecyclable(false);
        holder.tv_SrName.setText(String.valueOf(position + 1));
        holder.tv_sr_no.setText(item.getBeneficiaryName());
        holder.tv_beniname.setText(item.getBeneficiaryName());
        holder.tv_Beneficiary_No.setText(String.valueOf(item.getBeneficiaryNo()));
        holder.tv_date.setText(String.valueOf(item.getNextRenewalDate()));
        holder.Area.setText(String.valueOf(item.getArea()));
        holder.DependantScreeningPending.setText(String.valueOf(item.getDependantScreeningPending()));
        holder.PhleboRemark.setText(item.getPhleboRemark());
        holder.tv_screenedStatus.setText(item.getIsWorkerScreened() + "/" + item.getLastScreeningDate());
        if (item.getAppoinmentDate()!=null){
            holder.tv_appointmentDate.setText(item.getAppoinmentDate() + "/"+ item.getAppoinmentTime());
        }
//        if (item.getAppoinmentTime()!=null){
//            holder.tv_appointmentTime.setText(item.getAppoinmentTime());
//
//        }

        holder.cvMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onclicklisner.onclick(item);
            }
        });

        if (item.getGroupID().equalsIgnoreCase("2")) {
            holder.llMain.setBackgroundColor(context.getResources().getColor(R.color.greenLight));
            holder.ll_appointmentDate.setVisibility(View.VISIBLE);
//            holder.ll_appointmentTime.setVisibility(View.VISIBLE);
        } else if (item.getGroupID().equalsIgnoreCase("3")) {
            holder.llMain.setBackgroundColor(context.getResources().getColor(R.color.redLight));
        } else if (item.getGroupID().equalsIgnoreCase("4")) {
            holder.llMain.setBackgroundColor(context.getResources().getColor(R.color.light_blue));
        } else if (item.getGroupID().equalsIgnoreCase("1")) {
            holder.llMain.setBackgroundColor(context.getResources().getColor(R.color.greylight));
        }else if (item.getGroupID().equalsIgnoreCase("6")){
            holder.llMain.setBackgroundColor(context.getResources().getColor(R.color.light_yellow));
        }
        if (item.getGroupID().equalsIgnoreCase("5") ){
            holder.llMain.setBackgroundColor(context.getResources().getColor(R.color.purpleLightColor));
            holder.ll_appointmentDate.setVisibility(View.VISIBLE);
//            holder.ll_appointmentTime.setVisibility(View.VISIBLE);
        } else if (item.getGroupID().equalsIgnoreCase("7")) {
            holder.llMain.setBackgroundColor(context.getResources().getColor(R.color.light_orange));
            holder.ll_appointmentDate.setVisibility(View.GONE);
        }
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


        holder.iv_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onclicklisner.onImvClick(item);


//                Intent intent = new Intent(Intent.ACTION_DIAL);
//                intent.setData(Uri.parse("tel:"+ item.getMobile()));
//                context.startActivity(intent);

//                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + item.getMobile()));
//                context.startActivity(intent);
            }

        });


    }

    @Override
    public int getItemCount() {
        return outputItems.size();
    }

    class myview extends RecyclerView.ViewHolder {
        TextView tv_sr_no,tv_screenedStatus,DependantScreeningPending,Area,
                tv_beniname, tv_Beneficiary_No, tv_date,tv_SrName,
                tv_appointmentDate,PhleboRemark,tv_appointmentTime;
        ImageView iv_call,imvCall,iv_Newcall;
        TableLayout tableLayout;
        MaterialCardView cvMain;
        LinearLayoutCompat llMain,ll_appointmentTime,ll_NewImvCall,ll_appointmentDate,ll_ImvCall;

        public myview(@NonNull View itemView) {
            super(itemView);
            tv_sr_no = itemView.findViewById(R.id.tv_sr_no);
            tv_beniname = itemView.findViewById(R.id.tv_beniname);
            tv_Beneficiary_No = itemView.findViewById(R.id.tv_Beneficiary_No);
            tv_date = itemView.findViewById(R.id.tv_date);
            iv_call = itemView.findViewById(R.id.iv_call);
            cvMain = itemView.findViewById(R.id.cvMain);
            llMain = itemView.findViewById(R.id.llMain);
            tv_SrName = itemView.findViewById(R.id.tv_SrName);
            imvCall = itemView.findViewById(R.id.imvCall);
            iv_Newcall = itemView.findViewById(R.id.iv_Newcall);
//            ll_appointmentTime = itemView.findViewById(R.id.ll_appointmentTime);
            ll_appointmentDate = itemView.findViewById(R.id.ll_appointmentDate);
            tv_appointmentDate = itemView.findViewById(R.id.tv_appointmentDate);
//            tv_appointmentTime = itemView.findViewById(R.id.tv_appointmentTime);
            tv_screenedStatus = itemView.findViewById(R.id.tv_screenedStatus);
            Area = itemView.findViewById(R.id.Area);
            DependantScreeningPending = itemView.findViewById(R.id.DependantScreeningPending);
            PhleboRemark = itemView.findViewById(R.id.PhleboRemark);

        }
    }
}
