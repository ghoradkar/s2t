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
import com.myhindlab.abkat.activities.AttendanceMarkedPatients_Activity;
import com.myhindlab.abkat.activities.doortodoor.D2dPhysicalExaminationDetailsTeamWiseActivity;
import com.myhindlab.abkat.activities.doortodoor.D2dTeamsNotWorkingTeamActivity;
import com.myhindlab.abkat.models.doortodoor.AdminActiveInactiveModel;
import com.myhindlab.abkat.models.doortodoor.PhysicalExaminationDetailsModel;

import java.util.List;

public class PhysicalExaminationDetailsAdapter extends RecyclerView.Adapter<PhysicalExaminationDetailsAdapter.MyViewHolder> {

    private final List<PhysicalExaminationDetailsModel.Output> adminList;
    private Context context;

    public PhysicalExaminationDetailsAdapter(Context context, List<PhysicalExaminationDetailsModel.Output> adminList) {
        this.context = context;
        this.adminList = adminList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_d2d_physical_examination_details, parent, false);
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        PhysicalExaminationDetailsModel.Output data = adminList.get(position);

        holder.tvDistrictNew.setText(data.getDistrict());
        holder.tvCampCoordinatorName.setText(""+data.getCampId());
        holder.tvNotWTeamNew.setText(String.valueOf(data.getAssigned()));
        holder.tvWorkingTeamNew.setText(String.valueOf(data.getCallingPending()));
        holder.tvPhysicalPending.setText(String.valueOf(data.getPhyExamPending()));

        holder.tvNotWTeamNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, AttendanceMarkedPatients_Activity.class)
                        .putExtra("District", data.getDistlgdcode())
                        .putExtra("campId",data.getCampId())
                        .putExtra("Lab","0")
                        .putExtra("flag", 2)
                        .putExtra("Type",16)

                );
            }
        });

        holder.tvCampCoordinatorName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, D2dPhysicalExaminationDetailsTeamWiseActivity.class)
                        .putExtra("District", data.getDistrict())
                        .putExtra("campId",data.getCampId())
                        .putExtra("campDate",data.getCampDate())
                        .putExtra("Lab","0")


                );
            }
        });


//        holder.imvCall.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + data.getCampCoordinatorMobNo()));
//                context.startActivity(intent);
//            }
//
//        });




//        holder.tv_ResourceName.setText(data.getMemberName() + "(Team No. :- " + data.getTeamNumber() + ")");


    }

    @Override
    public int getItemCount() {
        return adminList!=null?adminList.size():0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvDistrictNew,tvPhysicalPending,tvCampCoordinatorName,tvNotWTeamNew,tvWorkingTeamNew,tvCallNew;
        ImageView imvCall;



        public MyViewHolder(final View view) {
            super(view);
            tvDistrictNew = view.findViewById(R.id.tvDistrictNew);
            tvCampCoordinatorName = view.findViewById(R.id.tvCampCoordinatorName);
            tvNotWTeamNew = view.findViewById(R.id.tvNotWTeamNew);
            tvWorkingTeamNew = view.findViewById(R.id.tvWorkingTeamNew);
            tvPhysicalPending = view.findViewById(R.id.tvPhysicalPending);
            imvCall = view.findViewById(R.id.imvCall);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}