package com.myhindlab.abkat.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.PostCampReadinessModel;

import java.util.List;

public class PostCampReadinessAdapter extends RecyclerView.Adapter<PostCampReadinessAdapter.MyViewHolder> {

    private Context context;
    private List<PostCampReadinessModel.OutputBean> campList;

    public PostCampReadinessAdapter(Context context, List<PostCampReadinessModel.OutputBean> campList) {
        this.context = context;
        this.campList = campList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_row_postcamp_readiness, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int pos) {
        int position = holder.getAdapterPosition();

        PostCampReadinessModel.OutputBean campDetails = campList.get(position);

        holder.tv_camp_name.setText(campDetails.getCampName());
        holder.tv_camp_id.setText(String.valueOf(campDetails.getCampId()));
        holder.tv_district.setText(campDetails.getDISTNAME());
        holder.tv_readiness_status.setText(campDetails.getReadinessStatus());
    }

    @Override
    public int getItemCount() {
        return campList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_camp_name, tv_district, tv_camp_id, tv_readiness_status;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_camp_name = itemView.findViewById(R.id.tv_camp_name);
            tv_camp_id = itemView.findViewById(R.id.tv_camp_id);
            tv_district = itemView.findViewById(R.id.tv_district);
            tv_readiness_status = itemView.findViewById(R.id.tv_readiness_status);
        }
    }
}
