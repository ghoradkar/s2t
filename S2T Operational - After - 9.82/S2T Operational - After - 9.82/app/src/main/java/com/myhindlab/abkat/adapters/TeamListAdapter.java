package com.myhindlab.abkat.adapters;

import android.content.Context;
import android.content.Intent;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.ResourceUserwise_Activity;
import com.myhindlab.abkat.models.TeamList;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class TeamListAdapter extends RecyclerView.Adapter<TeamListAdapter.MyViewHolder> {

    private List<TeamList> resultArrayList;
    private Context context;
    private int[] colorArray;

    public TeamListAdapter(Context context, int[] colorArray, List<TeamList> resultArrayList) {
        this.context = context;
        this.resultArrayList = resultArrayList;
        this.colorArray = colorArray;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.grid_row_teamdesig, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int pos) {
        final int position = holder.getAdapterPosition();
        TeamList desigDetail = resultArrayList.get(position);
        String facilityCount = NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(desigDetail.getUSERS_COUNT()));
        holder.tv_facilitycount.setText(facilityCount);
        holder.tv_facilitytype.setText(desigDetail.getDESGNAME());

        if (position < colorArray.length) {
            holder.cv_innercardview.setCardBackgroundColor(colorArray[position]);
        } else {
            int random = (int) (Math.random() * colorArray.length);
            holder.cv_innercardview.setCardBackgroundColor(colorArray[random]);
        }

        holder.cv_innercardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ResourceUserwise_Activity.class);
                intent.putExtra("DESIGID", desigDetail.getDESGID());
                intent.putExtra("DesigName", desigDetail.getDESGNAME());
                context.startActivity(intent);

            }
        });

//        holder.cv_facility.setCardBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
    }

    @Override
    public int getItemCount() {
        return resultArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private CardView cv_innercardview;
        private TextView tv_facilitycount, tv_facilitytype;

        private MyViewHolder(View view) {
            super(view);
            cv_innercardview = view.findViewById(R.id.cv_innercardview);
            tv_facilitycount = view.findViewById(R.id.tv_facilitycount);
            tv_facilitytype = view.findViewById(R.id.tv_facilitytype);

        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}

