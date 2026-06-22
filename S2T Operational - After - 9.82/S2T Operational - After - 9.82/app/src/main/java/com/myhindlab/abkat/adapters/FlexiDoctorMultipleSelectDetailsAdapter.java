package com.myhindlab.abkat.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.DoctorDetailsModel;
import com.myhindlab.abkat.models.FlexiDoctorDetailsModel;

import java.util.ArrayList;

public class FlexiDoctorMultipleSelectDetailsAdapter extends RecyclerView.Adapter<FlexiDoctorMultipleSelectDetailsAdapter.MyViewHolder> {

    private final ArrayList<FlexiDoctorDetailsModel.OutputBean> doctorlist;

    public FlexiDoctorMultipleSelectDetailsAdapter(ArrayList<FlexiDoctorDetailsModel.OutputBean> doctorlist) {
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
        FlexiDoctorDetailsModel.OutputBean data = doctorlist.get(position);

        holder.tv_ResourceName.setText(data.getMemberName() + "(Team No. :- " + data.getTeamNumber() + ")");


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