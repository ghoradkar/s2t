package com.myhindlab.abkat.adapters.doortodoor;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.doortodoor.TeamCallingModel;

import java.util.List;

public class CallingNumberAdapter extends RecyclerView.Adapter<CallingNumberAdapter.MyViewHolder> {

    private final List<TeamCallingModel.Output> teamList;
    private Context context;

    private OnCallClickListener listener;


    public interface OnCallClickListener {
        void onCallClick(TeamCallingModel.Output data);
    }

    public CallingNumberAdapter(Context context, List<TeamCallingModel.Output> list, OnCallClickListener listener) {
        this.context = context;
        this.teamList = list;
        this.listener = listener;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_calling_number, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        TeamCallingModel.Output data = teamList.get(position);

        holder.tvSrNo.setText("" + (position + 1));
        holder.tvMemberName.setText(data.getNumberType());
        holder.tvDesignation.setText(data.getMobileNo());
      //  holder.tvWorkingTeamNew.setText(String.valueOf(data.getWorkingTeamCount()));

        holder.imvCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onCallClick(data); // 👈 send event to Activity
            }
        });

        if (data.getMobileNo().equalsIgnoreCase("NA")){

            holder.imvCall.setVisibility(View.INVISIBLE);
        }else {
            holder.imvCall.setVisibility(View.VISIBLE);

        }




//        holder.tv_ResourceName.setText(data.getMemberName() + "(Team No. :- " + data.getTeamNumber() + ")");


    }

    @Override
    public int getItemCount() {
        return teamList!=null?teamList.size():0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvSrNo,tvMemberName,tvDesignation;
        public ImageView imvCall;



        public MyViewHolder(final View view) {
            super(view);
            tvSrNo = view.findViewById(R.id.tvSrNo);
            tvMemberName = view.findViewById(R.id.tvMemberName);
            tvDesignation = view.findViewById(R.id.tvDesignation);
            imvCall = view.findViewById(R.id.imvCall);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}