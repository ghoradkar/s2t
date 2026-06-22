package com.myhindlab.abkat.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.SampleProcessingLabModel;

import java.util.List;

public class SampleProcessingLabWiseAdapter extends RecyclerView.Adapter<SampleProcessingLabWiseAdapter.MyViewHolder> {

    private Context context;
    private int type;
    private List<SampleProcessingLabModel.OutputBean> labList;


    public SampleProcessingLabWiseAdapter(Context context, List<SampleProcessingLabModel.OutputBean> labList, int type) {
        this.context = context;
        this.labList = labList;
        this.type = type;
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

        SampleProcessingLabModel.OutputBean labDetails = labList.get(position);
        holder.tv_labname.setText(labDetails.getLabName());

        switch (type) {
            case 1:
                holder.tv_count.setText(labDetails.getCOMPLETED()+"");
                break;
            case 2:
                holder.tv_count.setText(labDetails.getPENDING()+"");
                break;
            case 3:
                holder.tv_count.setText(labDetails.getREJECTED()+"");
                break;
        }
    }

    @Override
    public int getItemCount() {
        return labList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_labname, tv_count;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_labname = itemView.findViewById(R.id.tv_labname);
            tv_count = itemView.findViewById(R.id.tv_count);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
