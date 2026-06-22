package com.myhindlab.abkat.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.RecyclerView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.DistrictOrgModel;
import com.myhindlab.abkat.models.PincodeListModel;

import java.util.List;

public class PincodeListSelectAdapter extends RecyclerView.Adapter<PincodeListSelectAdapter.MyViewHolder> {

    private final List<PincodeListModel.Output> pincodeList;

    public PincodeListSelectAdapter(List<PincodeListModel.Output> pincodeList) {
        this.pincodeList = pincodeList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_checkbox, parent, false);
        return new MyViewHolder(itemView);
    }

//    @Override
//    public void onBindViewHolder(final MyViewHolder holder, final int position) {
//        DistrictList_Model data = districtlist.get(position);
//
//        holder.cbItem.setText(data.getDISTNAME());
//        holder.cbItem.setChecked(data.isChecked());
//        holder.cbItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
//                data.setChecked(checked);
//            }
//        });
//
//
//    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int pos) {
        PincodeListModel.Output data = pincodeList.get(holder.getAbsoluteAdapterPosition());

        holder.cbItem.setText(data.getPincode());
        holder.cbItem.setChecked(data.isChecked());


        holder.cbItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                data.setChecked(checked);
            }
        });


//        holder.cbItem.setOnCheckedChangeListener(null); // To prevent unwanted triggers during recycling
//
//        holder.cbItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
//                if (checked) {
//                    // Uncheck all other checkboxes
//                    for (int i = 0; i < pincodeList.size(); i++) {
//                        if (i != holder.getAbsoluteAdapterPosition()) {
//                            pincodeList.get(i).setChecked(false); // Uncheck others
//                        }
//                    }
//                    // Notify the adapter of the changes
//                    notifyDataSetChanged();
//                }
//                // Update the current item's checked status
//                data.setChecked(checked);
//            }
//        });


    }


    @Override
    public int getItemCount() {
        return pincodeList.size();
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