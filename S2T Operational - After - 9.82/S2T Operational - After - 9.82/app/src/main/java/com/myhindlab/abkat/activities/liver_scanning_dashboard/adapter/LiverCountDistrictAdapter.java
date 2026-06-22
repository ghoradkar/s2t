package com.myhindlab.abkat.activities.liver_scanning_dashboard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.FibroscanDistrictWiseCountModel;

import java.util.List;

public class LiverCountDistrictAdapter extends RecyclerView.Adapter<LiverCountDistrictAdapter.myview> {
    List<FibroscanDistrictWiseCountModel.Output> countList;
    onTouchListner onTouchListner;
    Context context;

    public interface onTouchListner {
        public void onDataClick(FibroscanDistrictWiseCountModel.Output item);


    }

    public LiverCountDistrictAdapter(Context context, List<FibroscanDistrictWiseCountModel.Output> countList, onTouchListner onTouchListner) {
        this.countList = countList;
        this.onTouchListner = onTouchListner;
        this.context = context;
    }

    @NonNull
    @Override
    public myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_liver_machine_data_for_district, parent, false);
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
        FibroscanDistrictWiseCountModel.Output item = countList.get(position);
        holder.setIsRecyclable(false);
//        holder.tv_SrName.setText(String.valueOf(position + 1));
        holder.tv_patientId.setText(item.getPatientID());
        holder.tv_Stiffness.setText( item.getStiffness());
        holder.tv_uap.setText(item.getuAP());
        holder.tv_success_shot.setText("" + item.getSuccessfullShots());
        holder.tv_total_shot.setText("" + item.getTotalShots());


//        holder.tv_patientId.setOnClickListener(new View.OnClickListener() {
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
        TextView tv_patientId, tv_Stiffness, tv_uap, tv_success_shot, tv_total_shot;


        public myview(@NonNull View itemView) {
            super(itemView);

            tv_patientId = itemView.findViewById(R.id.tv_patientId);
            tv_Stiffness = itemView.findViewById(R.id.tv_Stiffness);
            tv_uap = itemView.findViewById(R.id.tv_uap);
            tv_success_shot = itemView.findViewById(R.id.tv_success_shot);
            tv_total_shot = itemView.findViewById(R.id.tv_total_shot);


        }
    }
}
