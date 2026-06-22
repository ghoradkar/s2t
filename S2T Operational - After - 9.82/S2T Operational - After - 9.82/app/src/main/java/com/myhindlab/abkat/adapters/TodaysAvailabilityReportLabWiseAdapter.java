package com.myhindlab.abkat.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.pojos.AttendanceReportDistrictWise_OutPut_Pojo;

import java.util.List;

public class TodaysAvailabilityReportLabWiseAdapter extends RecyclerView.Adapter<TodaysAvailabilityReportLabWiseAdapter.MyViewHolder> {

    private List<AttendanceReportDistrictWise_OutPut_Pojo> availabilityList;
    private Context context;

    public TodaysAvailabilityReportLabWiseAdapter(Context context, List<AttendanceReportDistrictWise_OutPut_Pojo> availabilityList) {
        this.availabilityList = availabilityList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_reportstatic, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        AttendanceReportDistrictWise_OutPut_Pojo ldetails = new AttendanceReportDistrictWise_OutPut_Pojo();
        ldetails = availabilityList.get(position);

        holder.location.setText(ldetails.getDISTNAME());
        holder.marked.setText(ldetails.getTotalAttendance());
        holder.notMarked.setText(ldetails.getNotMarked());
        holder.total.setText(ldetails.getOnBoardUsersCount());
    }

    @Override
    public int getItemCount() {
        return availabilityList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView location, marked, notMarked, total;

        public MyViewHolder(final View view) {
            super(view);
            location = view.findViewById(R.id.text_locationname);
            marked = view.findViewById(R.id.text_row1);
            notMarked = view.findViewById(R.id.text_row2);
            total = view.findViewById(R.id.text_row3);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}