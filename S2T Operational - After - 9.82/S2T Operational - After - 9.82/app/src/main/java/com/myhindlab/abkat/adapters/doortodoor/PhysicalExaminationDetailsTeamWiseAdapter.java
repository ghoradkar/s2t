package com.myhindlab.abkat.adapters.doortodoor;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.AttendanceMarkedPatients_Activity;
import com.myhindlab.abkat.activities.doortodoor.D2dTeamsNotWorkingTeamActivity;
import com.myhindlab.abkat.models.doortodoor.D2dWorkingTeamModel;
import com.myhindlab.abkat.models.doortodoor.PhysicalExaminationDetailsModel;
import com.myhindlab.abkat.models.doortodoor.PhysicalExaminationDetailsTeamWiseModel;

import java.util.List;

public class PhysicalExaminationDetailsTeamWiseAdapter extends RecyclerView.Adapter<PhysicalExaminationDetailsTeamWiseAdapter.MyViewHolder> {

    private final List<PhysicalExaminationDetailsTeamWiseModel.Output> adminList;
    private Context context;

    private PhysicalExminationTeamEvent eventListener;


    public   interface PhysicalExminationTeamEvent{
        void onCallClick(PhysicalExaminationDetailsTeamWiseModel.Output team);
    }


    public PhysicalExaminationDetailsTeamWiseAdapter(Context context, List<PhysicalExaminationDetailsTeamWiseModel.Output> adminList, PhysicalExminationTeamEvent eventListener) {
        this.context = context;
        this.adminList = adminList;
        this.eventListener =eventListener;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_d2d_physical_examination_details_team_wise, parent, false);
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        PhysicalExaminationDetailsTeamWiseModel.Output data = adminList.get(position);

        holder.tvDistrictNew.setText(data.getTeamNo());
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
                        .putExtra("flag", 3)
                        .putExtra("Type",16)

                );
            }
        });

        holder.tvWorkingTeamNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, D2dTeamsNotWorkingTeamActivity.class)
                        .putExtra("district", data.getDistlgdcode())
                        .putExtra("Lab","0")
                        .putExtra("flag", 5)


                );
            }
        });



        holder.tvDistrictNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                eventListener.onCallClick(data);

            }
        });
        holder.imvCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                eventListener.onCallClick(data);

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