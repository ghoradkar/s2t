package com.myhindlab.abkat.adapters;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.CampSampleProcessingModel;

import java.util.List;

public class CampSampleProcessingAdapter extends RecyclerView.Adapter<CampSampleProcessingAdapter.MyViewHolder> {

    private List<CampSampleProcessingModel.OutputBean> resultArrayList;
    private Context context;

    public CampSampleProcessingAdapter(Context context, List<CampSampleProcessingModel.OutputBean> resultArrayList) {
        this.context = context;
        this.resultArrayList = resultArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_row_camp_sampleprosess, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int pos) {
        final int position = holder.getAdapterPosition();
        final CampSampleProcessingModel.OutputBean resourceDetails = resultArrayList.get(position);
        holder.tv_patientname.setText(resourceDetails.getPatientName() + " (" + resourceDetails.getRegdNo() + ")");
        holder.tv_barcode.setText(resourceDetails.getBarCode());
        holder.tv_total.setText(resourceDetails.getTotalTests());
        holder.tv_processed.setText(resourceDetails.getPROCESSED());
        holder.tv_pending.setText(resourceDetails.getPENDING());
        holder.tv_rejected.setText(resourceDetails.getRejected());
    }

    @Override
    public int getItemCount() {
        return resultArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout ll_row;
        private TextView tv_patientname, tv_barcode, tv_total, tv_processed, tv_pending, tv_rejected;

        public MyViewHolder(View view) {
            super(view);
            tv_patientname = view.findViewById(R.id.tv_patientname);
            tv_barcode = view.findViewById(R.id.tv_barcode);
            tv_total = view.findViewById(R.id.tv_total);
            tv_processed = view.findViewById(R.id.tv_processed);
            tv_pending = view.findViewById(R.id.tv_pending);
            tv_rejected = view.findViewById(R.id.tv_rejected);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
