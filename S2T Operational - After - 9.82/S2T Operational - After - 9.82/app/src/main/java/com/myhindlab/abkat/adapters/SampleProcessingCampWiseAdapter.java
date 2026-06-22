package com.myhindlab.abkat.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.SampleProcessingCampModel;

import java.util.List;

public class SampleProcessingCampWiseAdapter extends RecyclerView.Adapter<SampleProcessingCampWiseAdapter.MyViewHolder> {

    private Context context;
    private List<SampleProcessingCampModel.OutputBean> campList;
    private int type;

    public SampleProcessingCampWiseAdapter(Context context, List<SampleProcessingCampModel.OutputBean> campList, int type) {
        this.context = context;
        this.campList = campList;
        this.type = type;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_row_sampleprocess_campwise, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int pos) {
        int position = holder.getAdapterPosition();
        SampleProcessingCampModel.OutputBean campDetails = campList.get(position);

        holder.tv_labname.setText(campDetails.getCampName());
        holder.tv_date.setText(campDetails.getCampDate());

        switch (type) {
            case 1:
                holder.tv_count.setText(campDetails.getCOMPLETED() + "");
                break;
            case 2:
                holder.tv_count.setText(campDetails.getPENDING() + "");
                break;
            case 3:
                holder.tv_count.setText(campDetails.getREJECTED() + "");
                break;
        }
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
