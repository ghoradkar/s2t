package com.myhindlab.abkat.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.CampListModel;
import com.myhindlab.abkat.pojos.Lab_OutPut_Pojo;

import java.util.ArrayList;
import java.util.List;

public class CampListAdpterForReadinessAdapter extends RecyclerView.Adapter<CampListAdpterForReadinessAdapter.MyViewHolder> {

    private final List<CampListModel.OutputBean> labList;
    private Context context;

    public CampListAdpterForReadinessAdapter(Context context, List<CampListModel.OutputBean> labList) {
        this.context = context;

        this.labList = labList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_lab, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        CampListModel.OutputBean data = labList.get(position);

        holder.tv_labName.setText(data.getCampId() + "(" + data.getCampCreatedBy() + ")");



        if (data.getCampType().equalsIgnoreCase("1")){

            if (data.getCampReadinessFlag().equalsIgnoreCase("1")){
                holder.tv_labName.setTextColor(context.getResources().getColor(R.color.green));
            }else {
                holder.tv_labName.setTextColor(context.getResources().getColor(R.color.black));
            }
        }
    }

    @Override
    public int getItemCount() {
        return labList!=null?labList.size():0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_labName;
      //  public ImageButton btn_remove;


        public MyViewHolder(final View view) {
            super(view);
            tv_labName = view.findViewById(R.id.tv_labName);
           // btn_remove = view.findViewById(R.id.btn_remove);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}