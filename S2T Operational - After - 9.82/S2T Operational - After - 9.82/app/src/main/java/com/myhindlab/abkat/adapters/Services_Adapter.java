package com.myhindlab.abkat.adapters;

import android.content.Context;
import android.content.Intent;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.ServiceList_Model;
import com.myhindlab.abkat.utilities.Utilities;

import java.util.List;

public class Services_Adapter extends RecyclerView.Adapter<Services_Adapter.MyViewHolder> {

    private List<ServiceList_Model> resultArrayList;
    private Context context;

    public Services_Adapter(Context context, List<ServiceList_Model> resultArrayList) {
        this.context = context;
        this.resultArrayList = resultArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_row_service, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int pos) {
        final int position = holder.getAdapterPosition();
        final ServiceList_Model serviceDetails = resultArrayList.get(position);

        holder.imv_service.setImageDrawable(context.getResources().getDrawable(serviceDetails.getIcon()));
        holder.tv_servicename.setText(serviceDetails.getTitle());
        holder.tv_servicedecr.setText(serviceDetails.getDescription());

        holder.cv_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (position == 0) {
                        context.startActivity(new Intent(context, Class.forName(serviceDetails.getActivityname()))
                                .putExtra("TYPE", "1")
                                .putExtra("LABEL", "Physical Examination"));
                    } else if (position == 1) {
                        context.startActivity(new Intent(context, Class.forName(serviceDetails.getActivityname()))
                                .putExtra("TYPE", "2")
                                .putExtra("LABEL", "Lung Function Test"));
                    } else if (position == 2) {
                        context.startActivity(new Intent(context, Class.forName(serviceDetails.getActivityname()))
                                .putExtra("TYPE", "3")
                                .putExtra("LABEL", "Audio Screening Test"));
                    } else if (position == 3) {
                        context.startActivity(new Intent(context, Class.forName(serviceDetails.getActivityname()))
                                .putExtra("TYPE", "4")
                                .putExtra("LABEL", "Visual Screening Test"));
                    }

                } catch (ClassNotFoundException e) {
                    Utilities.showToastMessage(R.string.msgt_comingsoon, context, true);
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return resultArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private CardView cv_title;
        private ImageView imv_service;
        private TextView tv_servicename, tv_servicedecr;

        public MyViewHolder(View view) {
            super(view);
            cv_title = view.findViewById(R.id.cv_title);
            imv_service = view.findViewById(R.id.imv_service);
            tv_servicename = view.findViewById(R.id.tv_servicename);
            tv_servicedecr = view.findViewById(R.id.tv_servicedecr);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
