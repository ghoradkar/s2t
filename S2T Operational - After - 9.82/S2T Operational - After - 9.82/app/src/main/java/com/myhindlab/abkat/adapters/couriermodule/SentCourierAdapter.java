package com.myhindlab.abkat.adapters.couriermodule;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.couriermodule.ReceivedCourierDetails_Activity;
import com.myhindlab.abkat.models.couriermodule.ReceivedCourierModel;

import java.util.List;

public class SentCourierAdapter extends RecyclerView.Adapter<SentCourierAdapter.MyViewHolder> {
    private List<ReceivedCourierModel> resultArrayList;
    private Context context;

    public SentCourierAdapter(List<ReceivedCourierModel> resultArrayList, Context context) {
        this.resultArrayList = resultArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_sent_courier, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        position = holder.getAdapterPosition();
        ReceivedCourierModel courierModel = resultArrayList.get(position);
        holder.txt_from_date.setText(courierModel.getCourierDate());
        holder.txt_to_lab.setText(courierModel.getSentToLab());
        holder.tv_sample_count.setText(courierModel.getSampleCount());
        holder.txt_sample_type.setText(courierModel.getSampleType());

        if (courierModel.getCourierStatus().equalsIgnoreCase("Courier Received"))
            holder.row_accept.setVisibility(View.VISIBLE);

        holder.ll_mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ReceivedCourierDetails_Activity.class);
                intent.putExtra("CourierModel", courierModel);
                intent.putExtra("type", "sent");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return resultArrayList.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_from_date, txt_to_lab, txt_sample_type, tv_sample_count;
        public LinearLayout ll_mainLayout;
        public TableRow row_accept;

        private MyViewHolder(View view) {
            super(view);
            txt_from_date = view.findViewById(R.id.txt_from_date);
            txt_to_lab = view.findViewById(R.id.txt_to_lab);
            txt_sample_type = view.findViewById(R.id.txt_sample_type);
            tv_sample_count = view.findViewById(R.id.tv_sample_count);
            row_accept = view.findViewById(R.id.table_row_accept_courier);
            ll_mainLayout = view.findViewById(R.id.main_layout);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
