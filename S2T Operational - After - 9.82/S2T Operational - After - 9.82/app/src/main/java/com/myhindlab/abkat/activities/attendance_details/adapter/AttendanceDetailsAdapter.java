package com.myhindlab.abkat.activities.attendance_details.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.attendance_details.model.AttendanceDetailsModel;
import com.myhindlab.abkat.activities.payout.model.InvoiceMonthWiseDetailsnModel;

import java.util.List;

public class AttendanceDetailsAdapter extends RecyclerView.Adapter<AttendanceDetailsAdapter.myview> {
    List<AttendanceDetailsModel.Output> invoiceList;

    Context context;


    public AttendanceDetailsAdapter(Context context, List<AttendanceDetailsModel.Output> invoiceList) {
        this.context = context;
        this.invoiceList = invoiceList;

    }

    @NonNull
    @Override
    public myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_attendance_list, parent, false);
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
        AttendanceDetailsModel.Output item = invoiceList.get(position);
        holder.setIsRecyclable(false);


        holder.tv_team_name.setText(item.getMemberName());
        holder.tv_in_time.setText(item.getInTime());
        holder.tv_team_in_distance.setText(item.getINDistanceInKM());
        holder.tv_out_time.setText(item.getOuttime());
        holder.tv_out_distance.setText(item.getOutDistanceInKM());




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
        private TextView tv_team_name,tv_in_time,tv_team_in_distance,tv_out_time,tv_out_distance;


        public myview(View view) {
            super(view);
            tv_team_name = view.findViewById(R.id.tv_team_name);
            tv_in_time = view.findViewById(R.id.tv_in_time);
            tv_team_in_distance = view.findViewById(R.id.tv_team_in_distance);
            tv_out_time = view.findViewById(R.id.tv_out_time);
            tv_out_distance = view.findViewById(R.id.tv_out_distance);


        }
    }
}
