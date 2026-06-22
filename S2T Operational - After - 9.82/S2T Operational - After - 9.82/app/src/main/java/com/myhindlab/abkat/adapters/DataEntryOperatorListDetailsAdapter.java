package com.myhindlab.abkat.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.DataEntryOperatorDetailsModel;
import com.myhindlab.abkat.models.DataEntryOperatorModel;

import java.util.ArrayList;

public class DataEntryOperatorListDetailsAdapter extends RecyclerView.Adapter<DataEntryOperatorListDetailsAdapter.MyViewHolder> {

    private final ArrayList<DataEntryOperatorDetailsModel.OutputBean> DeList;

    public DataEntryOperatorListDetailsAdapter(ArrayList<DataEntryOperatorDetailsModel.OutputBean> DeList) {
        this.DeList = DeList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_remove, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        DataEntryOperatorDetailsModel.OutputBean data = DeList.get(position);

        holder.tv_ResourceName.setText(data.getMemberName() + "(Team No. :- " + data.getTeamNumber() + ")");



    }

    @Override
    public int getItemCount() {
        return DeList!=null?DeList.size():0;
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