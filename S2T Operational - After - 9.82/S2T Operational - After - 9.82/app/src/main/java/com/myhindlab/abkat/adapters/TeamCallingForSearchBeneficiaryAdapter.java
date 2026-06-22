package com.myhindlab.abkat.adapters;

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

public class TeamCallingForSearchBeneficiaryAdapter extends RecyclerView.Adapter<TeamCallingForSearchBeneficiaryAdapter.MyViewHolder> {

    private final List<TeamCallingModel.Output> teamList;
    private Context context;


    public TeamCallingForSearchBeneficiaryAdapter(Context context, List<TeamCallingModel.Output> teamList) {
        this.context = context;
        this.teamList = teamList;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_d2d_teams_calling_for_search_beneficiary, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        TeamCallingModel.Output data = teamList.get(position);

        holder.tvSrNo.setText("" + data.getRegdDate());


        if (data.getCampType()!=null){

            if (data.getCampType() == 3){
                holder.tvMemberName.setText("D2D");
            }else if (data.getCampType()==1){
                holder.tvMemberName.setText("Regular");
            }else if (data.getCampType()== 2){
                holder.tvMemberName.setText("CSC REGULAR CAMP");
            }else if (data.getCampType() == 4){
                holder.tvMemberName.setText("CSC D2D");

            }
        }

        holder.tvDesignation.setText(String.valueOf(data.getCampId()));
      //  holder.tvWorkingTeamNew.setText(String.valueOf(data.getWorkingTeamCount()));

        holder.imvCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + data.getMobno()));
                context.startActivity(intent);
            }

        });




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