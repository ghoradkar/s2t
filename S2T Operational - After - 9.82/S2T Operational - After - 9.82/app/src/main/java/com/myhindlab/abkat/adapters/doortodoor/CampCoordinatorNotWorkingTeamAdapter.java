package com.myhindlab.abkat.adapters.doortodoor;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.doortodoor.D2dWorkingTeamModel;

import java.util.List;

public class CampCoordinatorNotWorkingTeamAdapter extends RecyclerView.Adapter<CampCoordinatorNotWorkingTeamAdapter.MyViewHolder> {

    private final List<D2dWorkingTeamModel.Output> nonWorkingList;
    private Context context;
    private ProgressDialog pd;
    private CampCoordinatorNotWorkingTeamEvent eventListener;

     public   interface CampCoordinatorNotWorkingTeamEvent{
        void onCallClick(D2dWorkingTeamModel.Output team);
    }

    public CampCoordinatorNotWorkingTeamAdapter(Context context, List<D2dWorkingTeamModel.Output> nonWorkingList, CampCoordinatorNotWorkingTeamEvent eventListener) {
        this.context = context;
        this.nonWorkingList = nonWorkingList;
        this.eventListener =eventListener;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_d2d_not_working_teams, parent, false);
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        D2dWorkingTeamModel.Output data = nonWorkingList.get(holder.getAbsoluteAdapterPosition());


        holder.tvTeamNumber.setText("" + (position + 1));
//        holder.tvTeamNumber.setText("" + (data.getCampId()));
      //  holder.tvTeamNumber.setText(String.valueOf(data.getTeamid()));
        holder.tvMemberFirst.setText(data.getMember1());
        holder.tvMemberSecond.setText(String.valueOf(data.getMember2()));
        holder.tvRegisterBeneficiary.setText(String.valueOf(data.getRegBeneficieries()));
      //  holder.tvCallNew.setText(String.valueOf(data.getWorkingTeamCount()));

        if(holder.getAbsoluteAdapterPosition()%2==0){
         holder.llHeader.setBackgroundColor(context.getResources().getColor(R.color.greylight));
        }else{
            holder.llHeader.setBackgroundColor(context.getResources().getColor(R.color.white));
        }

        holder.imvCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventListener.onCallClick(data);
            }
        });
//        holder.tv_ResourceName.setText(data.getMemberName() + "(Team No. :- " + data.getTeamNumber() + ")");
    }



    @Override
    public int getItemCount() {
        return nonWorkingList!=null?nonWorkingList.size():0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTeamNumber,tvMemberFirst,tvMemberSecond,tvRegisterBeneficiary,tvCallNew;
        public ImageView imvCall;
        public LinearLayoutCompat llHeader;



        public MyViewHolder(final View view) {
            super(view);
            tvTeamNumber = view.findViewById(R.id.tvTeamNumber);
            tvMemberFirst = view.findViewById(R.id.tvMemberFirst);
            tvMemberSecond = view.findViewById(R.id.tvMemberSecond);
            tvRegisterBeneficiary = view.findViewById(R.id.tvRegisterBeneficiary);
            tvCallNew = view.findViewById(R.id.tvCallNew);
            imvCall = view.findViewById(R.id.imvCall);
            llHeader = view.findViewById(R.id.llHeader);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }






}