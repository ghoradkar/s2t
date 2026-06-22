package com.myhindlab.abkat.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.DoctorMappingModel;

import java.util.ArrayList;

public class DoctorListAdapter extends RecyclerView.Adapter<DoctorListAdapter.DoctorListViewHolder> {

    ArrayList<DoctorMappingModel.Output> outputs;

    public DoctorListAdapter(ArrayList<DoctorMappingModel.Output> outputs) {
        this.outputs = outputs;
    }

    @NonNull
    @Override
    public DoctorListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_lab, parent, false);

        return new DoctorListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorListViewHolder holder, int position) {

        DoctorMappingModel.Output output = outputs.get(position);

        holder.text1.setText(output.getFullname());
//        holder.cbItem.setText(output.getName());
//        holder.cbItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                output.setChecked(b);
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return outputs.size();
    }

    class DoctorListViewHolder extends RecyclerView.ViewHolder {
        public CheckBox cbItem;
        private TextView text1;


        public DoctorListViewHolder(@NonNull View itemView) {
            super(itemView);
//            cbItem = itemView.findViewById(R.id.cbItem);
            text1 = itemView.findViewById(R.id.tv_labName);

        }
    }
}
