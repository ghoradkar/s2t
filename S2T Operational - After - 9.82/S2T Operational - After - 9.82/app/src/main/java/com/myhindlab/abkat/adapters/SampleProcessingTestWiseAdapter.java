package com.myhindlab.abkat.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.PendingTestModel;

import java.util.List;

public class SampleProcessingTestWiseAdapter extends RecyclerView.Adapter<SampleProcessingTestWiseAdapter.MyViewHolder> {

    private Context context;
    private List<PendingTestModel.OutputBean> campList;

    public SampleProcessingTestWiseAdapter(Context context, List<PendingTestModel.OutputBean> campList) {
        this.context = context;
        this.campList = campList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_row_sampleprocess_labwise, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int pos) {
        int position = holder.getAdapterPosition();
        PendingTestModel.OutputBean campDetails = campList.get(position);

        holder.tv_labname.setText(campDetails.getServiceName());
        holder.tv_count.setText(campDetails.getPENDING() + "");
    }

    @Override
    public int getItemCount() {
        return campList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_labname, tv_date, tv_count;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_labname = itemView.findViewById(R.id.tv_labname);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_count = itemView.findViewById(R.id.tv_count);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
