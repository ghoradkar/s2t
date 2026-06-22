package com.myhindlab.abkat.adapters.doortodoor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.pojos.doortodoor.DependentPojo;
import com.myhindlab.abkat.pojos.doortodoor.DependentPojo;
import com.myhindlab.abkat.utilities.ConstantData;

import java.util.List;

public class DependentSearchAdapter extends RecyclerView
        .Adapter<DependentSearchAdapter
        .DataObjectHolder> {
    private Context mContext;
    private ConstantData constantData;
    private List<DependentPojo.Output> dataList;
    String userId;

    public DependentSearchAdapter(Context context, List<DependentPojo.Output> list) {
        this.mContext = context;
        this.dataList = list;
        this.userId = userId;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_list_patient, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        final DependentPojo.Output record = dataList.get(position);

        final String name = record.getDependentName();
        holder.patient_name.setText(name);
    }


    @Override
    public int getItemCount() {
        if (dataList == null)
            return 0;
        return dataList.size();
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder {
        private TextView patient_name;

        public DataObjectHolder(View itemView) {
            super(itemView);

            patient_name = (TextView) itemView.findViewById(R.id.patient_name);

        }
    }
}
