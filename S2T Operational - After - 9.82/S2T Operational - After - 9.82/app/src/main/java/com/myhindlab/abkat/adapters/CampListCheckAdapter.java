package com.myhindlab.abkat.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.RecyclerView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.DoctorModel;
import com.myhindlab.abkat.models.couriermodule.CamplistOnLandingLabModel;

import java.util.ArrayList;

public class CampListCheckAdapter extends RecyclerView.Adapter<CampListCheckAdapter.MyViewHolder> {

    private final ArrayList<CamplistOnLandingLabModel.Output> camplist;

    public CampListCheckAdapter(ArrayList<CamplistOnLandingLabModel.Output> camplist) {
        this.camplist = camplist;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_checkbox, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        CamplistOnLandingLabModel.Output data = camplist.get(position);

        holder.cbItem.setText(String.valueOf(data.getCampId()));
        holder.cbItem.setChecked(data.isChecked());
        holder.cbItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                data.setChecked(checked);
            }
        });

//        holder.cbItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if (b) {
//                    selectedcamplist.add(camplist.get(position));
//                    camplist.get(position).setChecked(b);
//
//                } else {
//                    selectedBarcodeList.remove(camplist.get(position));
//                    camplist.get(position).setChecked(b);
//
//
//                }
//            }
//        });



    }

    @Override
    public int getItemCount() {
        return camplist.size();
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