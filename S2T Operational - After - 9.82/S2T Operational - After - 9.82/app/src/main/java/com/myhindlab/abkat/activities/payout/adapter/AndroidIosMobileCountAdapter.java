package com.myhindlab.abkat.activities.payout.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.AndroidMobileCountModel;

import java.util.List;

public class AndroidIosMobileCountAdapter extends RecyclerView.Adapter<AndroidIosMobileCountAdapter.CountViewHolder> {
    private final List<AndroidMobileCountModel.Count> countList;
    private final Context context;

    public AndroidIosMobileCountAdapter(Context context, List<AndroidMobileCountModel.Count> countList) {
        this.context = context;
        this.countList = countList;
    }

    @NonNull
    @Override
    public CountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_android_ios_count, parent, false);
        return new CountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CountViewHolder holder, int position) {
        AndroidMobileCountModel.Count item = countList.get(position);
        holder.tv_sr_No.setText(String.valueOf(position + 1));
        holder.tv_district.setText("" + item.getDistrict());
        holder.tv_count.setText(" " + item.getiOS_Count());

    }

    @Override
    public int getItemCount() {
        return countList.size();
    }

    static class CountViewHolder extends RecyclerView.ViewHolder {
        TextView tv_sr_No, tv_district, tv_count;

        public CountViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_sr_No = itemView.findViewById(R.id.tv_sr_No);
            tv_district = itemView.findViewById(R.id.tv_district);
            tv_count = itemView.findViewById(R.id.tv_count);
        }
    }
}