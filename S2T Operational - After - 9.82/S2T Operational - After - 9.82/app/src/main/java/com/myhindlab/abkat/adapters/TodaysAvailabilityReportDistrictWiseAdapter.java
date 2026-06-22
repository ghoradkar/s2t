package com.myhindlab.abkat.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.pojos.AttendanceReportUserList_OutPut_Pojo;

import java.util.List;

public class TodaysAvailabilityReportDistrictWiseAdapter extends RecyclerView.Adapter<TodaysAvailabilityReportDistrictWiseAdapter.MyViewHolder> {

    private List<AttendanceReportUserList_OutPut_Pojo> availabilityList;
    private Context context;

    public TodaysAvailabilityReportDistrictWiseAdapter(Context context, List<AttendanceReportUserList_OutPut_Pojo> availabilityList) {
        this.availabilityList = availabilityList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_availabilityuser, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        AttendanceReportUserList_OutPut_Pojo ldetails = new AttendanceReportUserList_OutPut_Pojo();
        ldetails = availabilityList.get(position);

        holder.txt_facility.setText(ldetails.getDISTNAME());
        holder.txt_name.setText(ldetails.getFullname());
        holder.txt_designations.setText(ldetails.getDesgShortCode());


        if (availabilityList.get(position).getNotMarked().equalsIgnoreCase("1")) {
            holder.img_call.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_phonered));
        } else if (availabilityList.get(position).getNotMarked().equalsIgnoreCase("N")) {
            holder.img_call.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_phonered));
        }

        AttendanceReportUserList_OutPut_Pojo finalLdetails = ldetails;
        holder.img_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+ finalLdetails.getMOBNO().toString()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return availabilityList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_facility, txt_name,txt_designations;
        private ImageView img_call;

        public MyViewHolder(final View view) {
            super(view);
            txt_designations = view.findViewById(R.id.txt_designations);
            txt_facility = view.findViewById(R.id.txt_facility);
            txt_name = view.findViewById(R.id.txt_name);
            img_call = view.findViewById(R.id.img_call);

        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}