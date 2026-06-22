package com.myhindlab.abkat.adapters;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.DashboardBreastScreeningPatientWise_Activity;
import com.myhindlab.abkat.models.BreastScreeningCampWiseModel;

import java.util.List;

public class BreastScreeningCampWiseAdapter extends RecyclerView.Adapter<BreastScreeningCampWiseAdapter.MyViewHolder> {

    private Context context;
    private List<BreastScreeningCampWiseModel.OutputBean> campList;
    private String fromdate, toDate, statusType, districtCode;

    public BreastScreeningCampWiseAdapter(String fromdate, String toDate, Context context, List<BreastScreeningCampWiseModel.OutputBean> campList, String statusType, String districtCode) {
        this.context = context;
        this.fromdate = fromdate;
        this.toDate = toDate;
        this.campList = campList;
        this.statusType = statusType;
        this.districtCode = districtCode;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.list_row_bscreening_campwise, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        int position = holder.getAdapterPosition();
        BreastScreeningCampWiseModel.OutputBean outputBean = campList.get(position);

        holder.tv_camp_name.setText(outputBean.getCampName());

        switch (statusType) {
            case "1":
                holder.tv_count.setText(String.valueOf(outputBean.getPOSITIVE_PATIENTS()));
                break;
            case "2":
                holder.tv_count.setText(String.valueOf(outputBean.getNEGATIVE_PATIENTS()));
                break;
            case "3":
                holder.tv_count.setText(String.valueOf(outputBean.getNO_STATUS_PATIENTS()));
                break;
        }

        holder.ll_main_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, DashboardBreastScreeningPatientWise_Activity.class)
                        .putExtra("fromdate", fromdate)
                        .putExtra("toDate", toDate)
                        .putExtra("districtCode", String.valueOf(outputBean.getDISTLGDCODE()))
                        .putExtra("campId", String.valueOf(outputBean.getCAMPID()))
                        .putExtra("statusType", statusType));
            }
        });
    }

    @Override
    public int getItemCount() {
        return campList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout ll_main_layout;
        private TextView tv_camp_name, tv_count;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_camp_name = itemView.findViewById(R.id.tv_camp_name);
            tv_count = itemView.findViewById(R.id.tv_count);
            ll_main_layout = itemView.findViewById(R.id.ll_main_layout);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
