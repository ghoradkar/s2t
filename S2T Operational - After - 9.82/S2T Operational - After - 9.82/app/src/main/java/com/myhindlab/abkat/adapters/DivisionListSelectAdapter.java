package com.myhindlab.abkat.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.RecyclerView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.DistrictList_Model;
import com.myhindlab.abkat.models.SubDivisionModel;

import java.util.ArrayList;
import java.util.List;

public class DivisionListSelectAdapter extends RecyclerView.Adapter<DivisionListSelectAdapter.MyViewHolder> {

    private final List<SubDivisionModel.Output> divisionlist;

    public DivisionListSelectAdapter(List<SubDivisionModel.Output> divisionlist) {
        this.divisionlist = divisionlist;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_checkbox, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        SubDivisionModel.Output data = divisionlist.get(position);

        holder.cbItem.setText(data.getDivname());
        holder.cbItem.setChecked(data.isChecked());
//        holder.cbItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
//                data.setChecked(checked);
//            }
//        });


        holder.cbItem.setOnCheckedChangeListener(null); // To prevent unwanted triggers during recycling

        holder.cbItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    // Uncheck all other checkboxes
                    for (int i = 0; i < divisionlist.size(); i++) {
                        if (i != holder.getAbsoluteAdapterPosition()) {
                            divisionlist.get(i).setChecked(false); // Uncheck others
                        }
                    }
                    // Notify the adapter of the changes
                    notifyDataSetChanged();
                }
                // Update the current item's checked status
                data.setChecked(checked);
            }
        });



    }

    @Override
    public int getItemCount() {
        return divisionlist.size();
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