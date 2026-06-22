package com.myhindlab.abkat.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.DoctorDetailsModel;

import java.util.ArrayList;

public class DoctorMultipleSelectDetailsAdapter extends RecyclerView.Adapter<DoctorMultipleSelectDetailsAdapter.MyViewHolder> {

    private final ArrayList<DoctorDetailsModel.OutputBean> doctorlist;

    public DoctorMultipleSelectDetailsAdapter(ArrayList<DoctorDetailsModel.OutputBean> doctorlist) {
        this.doctorlist = doctorlist;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_remove, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        DoctorDetailsModel.OutputBean data = doctorlist.get(position);

        holder.tv_ResourceName.setText(data.getMemberName() + "(Team No. :- " + data.getTeamNumber() + ")");

//        if (data.getIsActive()!=null){
//            if (data.getIsActive().equalsIgnoreCase("1")){
//                holder.btn_remove.setVisibility(View.VISIBLE);
//
//            }else if (data.getIsActive().equalsIgnoreCase("0")){
//                holder.btn_remove.setVisibility(View.GONE);
//
//            }
//        }


    }

    @Override
    public int getItemCount() {

        return doctorlist != null ? doctorlist.size() : 0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_ResourceName;
        public ImageButton btn_remove;


        public MyViewHolder(final View view) {
            super(view);
            tv_ResourceName = view.findViewById(R.id.tv_ResourceName);
            btn_remove = view.findViewById(R.id.btn_remove);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}