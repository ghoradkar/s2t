package com.myhindlab.abkat.adapters;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.DashboardPostCampReadiness_Activity;
import com.myhindlab.abkat.models.InvoiceDashboardModel;

import java.util.List;

public class DashboardPostCampPendingAdapter extends RecyclerView.Adapter<DashboardPostCampPendingAdapter.MyViewHolder> {

    private Context context;
    private List<InvoiceDashboardModel.OutputBean> invoiceList;
    private String districtId, selectedMonthId, selectedYearId;

    public DashboardPostCampPendingAdapter(Context context, List<InvoiceDashboardModel.OutputBean> invoiceList, String districtId, String selectedMonthId, String selectedYearId) {
        this.context = context;
        this.invoiceList = invoiceList;
        this.districtId = districtId;
        this.selectedMonthId = selectedMonthId;
        this.selectedYearId = selectedYearId;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_row_postcamp_pending, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int pos) {
        int position = holder.getAdapterPosition();

        InvoiceDashboardModel.OutputBean outputBean = invoiceList.get(position);

        holder.tv_district.setText(outputBean.getDISTNAME());
        holder.tv_camps_done.setText(String.valueOf(outputBean.getTotalCamps()));
        holder.tv_camps_pending.setText(String.valueOf(outputBean.getPostCampsPending()));
        holder.tv_beneficiaries_screened.setText(String.valueOf(outputBean.getApplicableForBilling()));
        holder.tv_pending_beneficiaries_screened.setText(String.valueOf(outputBean.getPendingWorkers()));

        holder.cv_main_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, DashboardPostCampReadiness_Activity.class)
                        .putExtra("districtId", String.valueOf(outputBean.getDISTLGDCODE()))
                        .putExtra("selectedMonthId", selectedMonthId)
                        .putExtra("selectedYearId", selectedYearId));
            }
        });
    }

    @Override
    public int getItemCount() {
        return invoiceList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private CardView cv_main_layout;
        private TextView tv_district, tv_camps_done, tv_camps_pending, tv_beneficiaries_screened, tv_pending_beneficiaries_screened;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cv_main_layout = itemView.findViewById(R.id.cv_main_layout);
            tv_district = itemView.findViewById(R.id.tv_district);
            tv_camps_done = itemView.findViewById(R.id.tv_camps_done);
            tv_camps_pending = itemView.findViewById(R.id.tv_camps_pending);
            tv_beneficiaries_screened = itemView.findViewById(R.id.tv_beneficiaries_screened);
            tv_pending_beneficiaries_screened = itemView.findViewById(R.id.tv_pending_beneficiaries_screened);
        }
    }
}
