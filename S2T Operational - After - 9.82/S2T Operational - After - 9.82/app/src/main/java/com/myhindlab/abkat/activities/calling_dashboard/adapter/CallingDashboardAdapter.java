package com.myhindlab.abkat.activities.calling_dashboard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.attendance_details.model.AttendanceDetailsModel;
import com.myhindlab.abkat.activities.calling_dashboard.model.CallingDashboardModel;

import java.util.List;

public class CallingDashboardAdapter extends RecyclerView.Adapter<CallingDashboardAdapter.myview> {
    List<CallingDashboardModel.Output> invoiceList;

    Context context;


    public CallingDashboardAdapter(Context context, List<CallingDashboardModel.Output> invoiceList) {
        this.context = context;
        this.invoiceList = invoiceList;

    }

    @NonNull
    @Override
    public myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_calling_dashboard, parent, false);
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
        CallingDashboardModel.Output item = invoiceList.get(position);
        holder.setIsRecyclable(false);


        holder.tv_status.setText(item.getColumnName());
        holder.tv_count.setText(item.getValue());





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
        private TextView tv_status,tv_count;


        public myview(View view) {
            super(view);
            tv_status = view.findViewById(R.id.tv_status);
            tv_count = view.findViewById(R.id.tv_count);



        }
    }
}
