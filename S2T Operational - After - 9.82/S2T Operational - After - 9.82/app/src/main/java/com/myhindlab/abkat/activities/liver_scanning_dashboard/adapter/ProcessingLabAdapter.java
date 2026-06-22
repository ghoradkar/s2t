package com.myhindlab.abkat.activities.liver_scanning_dashboard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.ProcessingLabCountModel;

import java.util.List;

public class ProcessingLabAdapter extends RecyclerView.Adapter<ProcessingLabAdapter.myview> {
    List<ProcessingLabCountModel.Output> countList;
    Context context;


    public ProcessingLabAdapter(Context context, List<ProcessingLabCountModel.Output> countList ) {
        this.countList = countList;

        this.context = context;
    }

    @NonNull
    @Override
    public myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_processing_lab, parent, false);
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
        ProcessingLabCountModel.Output item = countList.get(position);
        holder.setIsRecyclable(false);
//        holder.tv_SrName.setText(String.valueOf(position + 1));
        holder.tv_month_years.setText(item.getMonthYear());
        holder.tv_lab_name.setText(item.getLabName());
        holder.tv_lab_type.setText(item.getLabType());
        holder.tv_processing_count.setText("" + item.getProcessingCount());


//        holder.tv_month_years.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onTouchListner.onDataClick(item);
//            }
//        });


    }

    @Override
    public int getItemCount() {
        return countList.size();
    }

    class myview extends RecyclerView.ViewHolder {
        TextView tv_month_years, tv_lab_name, tv_lab_type, tv_processing_count;


        public myview(@NonNull View itemView) {
            super(itemView);

            tv_month_years = itemView.findViewById(R.id.tv_month_years);
            tv_lab_name = itemView.findViewById(R.id.tv_lab_name);
            tv_lab_type = itemView.findViewById(R.id.tv_lab_type);
            tv_processing_count = itemView.findViewById(R.id.tv_processing_count);


        }
    }
}
