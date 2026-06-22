package com.myhindlab.abkat.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.RecyclerView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.FlexiDoctorModel;
import com.myhindlab.abkat.models.MMUDoctorModel;

import java.util.ArrayList;

public class MMUDoctorMultipleSelectAdapter extends RecyclerView.Adapter<MMUDoctorMultipleSelectAdapter.MyViewHolder> {

    private final ArrayList<MMUDoctorModel.OutputBean> doctorList;

    public MMUDoctorMultipleSelectAdapter(ArrayList<MMUDoctorModel.OutputBean> doctorList) {
        this.doctorList = doctorList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_checkbox, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        MMUDoctorModel.OutputBean data = doctorList.get(position);

        holder.cbItem.setText(data.getResourceName());
        holder.cbItem.setChecked(data.isChecked());
        holder.cbItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                data.setChecked(checked);
            }
        });


    }

    @Override
    public int getItemCount() {
        return doctorList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public CheckBox cbItem;


        public MyViewHolder(final View view) {
            super(view);
            cbItem = view.findViewById(R.id.cbItem);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}