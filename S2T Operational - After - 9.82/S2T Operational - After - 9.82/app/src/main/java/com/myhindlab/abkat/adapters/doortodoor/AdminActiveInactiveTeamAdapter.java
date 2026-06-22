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
import com.myhindlab.abkat.activities.doortodoor.D2dTeamsNotWorkingTeamActivity;
import com.myhindlab.abkat.models.doortodoor.AdminActiveInactiveModel;

import java.util.List;

public class AdminActiveInactiveTeamAdapter extends RecyclerView.Adapter<AdminActiveInactiveTeamAdapter.MyViewHolder> {

    private final List<AdminActiveInactiveModel.Output> adminList;
    private Context context;


    public AdminActiveInactiveTeamAdapter(Context context, List<AdminActiveInactiveModel.Output> adminList) {
        this.context = context;
        this.adminList = adminList;


    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_d2d_teams, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        AdminActiveInactiveModel.Output data = adminList.get(position);

        holder.tvDistrictNew.setText(data.getDistname());
        holder.tvCampCoordinatorName.setText(data.getCampCoordinatorName());
        holder.tvNotWTeamNew.setText(String.valueOf(data.getNonWorkingTeamCount()));
        holder.tvWorkingTeamNew.setText(String.valueOf(data.getWorkingTeamCount()));
        if (holder.tvCampCoordinatorName.getText().toString().matches("NA")){
            holder.imvCall.setEnabled(false);
        }

        holder.tvNotWTeamNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, D2dTeamsNotWorkingTeamActivity.class)
                        .putExtra("district", data.getDistlgdcode())
                        .putExtra("campType", data.getCampType())
                        .putExtra("divisionId", data.getDivId())
                        .putExtra("CampCoId", data.getCampCoId())
                        .putExtra("DesId", data.getDesgId())
                        .putExtra("Lab","0")
                        .putExtra("flag", 4)

                );
            }
        });

        holder.tvWorkingTeamNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, D2dTeamsNotWorkingTeamActivity.class)
                        .putExtra("district", data.getDistlgdcode())
                        .putExtra("campType", data.getCampType())
                        .putExtra("divisionId", data.getDivId())
                        .putExtra("CampCoId", data.getCampCoId())
                        .putExtra("DesId", data.getDesgId())
                        .putExtra("Lab","0")
                        .putExtra("flag", 5)


                );
            }
        });

        holder.imvCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + data.getCampCoordinatorMobNo()));
                context.startActivity(intent);
            }

        });




//        holder.tv_ResourceName.setText(data.getMemberName() + "(Team No. :- " + data.getTeamNumber() + ")");


    }

    @Override
    public int getItemCount() {
        return adminList!=null?adminList.size():0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvDistrictNew,tvCampCoordinatorName,tvNotWTeamNew,tvWorkingTeamNew,tvCallNew;
        ImageView imvCall;



        public MyViewHolder(final View view) {
            super(view);
            tvDistrictNew = view.findViewById(R.id.tvDistrictNew);
            tvCampCoordinatorName = view.findViewById(R.id.tvCampCoordinatorName);
            tvNotWTeamNew = view.findViewById(R.id.tvNotWTeamNew);
            tvWorkingTeamNew = view.findViewById(R.id.tvWorkingTeamNew);
            imvCall = view.findViewById(R.id.imvCall);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}