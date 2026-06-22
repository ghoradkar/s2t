package com.myhindlab.abkat.adapters;

import android.app.Fragment;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.pojos.SelectAddress_OutPut_pojo;

import java.util.ArrayList;


public class HospitalListAdapter extends RecyclerView
        .Adapter<HospitalListAdapter
        .DataObjectHolder> {

    private Context mContext;


    private Fragment fragment;
    ArrayList<SelectAddress_OutPut_pojo> dataList;
    String userId;

    public HospitalListAdapter(Context context, ArrayList<SelectAddress_OutPut_pojo> dataList) {
        this.mContext = context;
        this.dataList = dataList;
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
        final SelectAddress_OutPut_pojo record = dataList.get(position);

        final String name = record.getHospitalName();
        final String dob = record.getHospitalId();
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

            patient_name=(TextView)itemView.findViewById(R.id.patient_name);

        }
    }


}