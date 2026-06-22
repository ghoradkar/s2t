package com.myhindlab.abkat.adapters;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.HearingData;

import java.util.ArrayList;

public class HearingDataAdapter extends RecyclerView.Adapter<HearingDataAdapter.MyViewHolder> {

    private ArrayList<HearingData> hearingRecordList;

    public HearingDataAdapter(ArrayList<HearingData> hearingRecordList) {
        this.hearingRecordList = hearingRecordList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_hearingrecorddetails, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        HearingData data = new HearingData();
        data = hearingRecordList.get(position);

        holder.tv_frequency.setText(String.valueOf(data.getFrequency()) + " Hz");
        holder.tv_leftvolume.setText(String.valueOf(data.getLefttVolume()) + " dB");
        holder.tv_rightvolume.setText(String.valueOf(data.getRightVolume()) + " dB");
    }

    @Override
    public int getItemCount() {
        return hearingRecordList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_frequency, tv_leftvolume, tv_rightvolume;

        public MyViewHolder(final View view) {
            super(view);
            tv_frequency = view.findViewById(R.id.tv_frequency);
            tv_leftvolume = view.findViewById(R.id.tv_leftvolume);
            tv_rightvolume = view.findViewById(R.id.tv_rightvolume);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}