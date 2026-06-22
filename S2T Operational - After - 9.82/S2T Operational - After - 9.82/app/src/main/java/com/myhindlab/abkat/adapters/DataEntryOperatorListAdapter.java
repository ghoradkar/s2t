package com.myhindlab.abkat.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.RecyclerView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.DataEntryOperatorModel;
import com.myhindlab.abkat.models.PhleboModel;

import java.util.ArrayList;

public class DataEntryOperatorListAdapter extends RecyclerView.Adapter<DataEntryOperatorListAdapter.MyViewHolder> {

    private final ArrayList<DataEntryOperatorModel.OutputBean> DeList;

    public DataEntryOperatorListAdapter(ArrayList<DataEntryOperatorModel.OutputBean> DeList) {
        this.DeList = DeList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_checkbox, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        DataEntryOperatorModel.OutputBean data = DeList.get(position);

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
        return DeList.size();
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