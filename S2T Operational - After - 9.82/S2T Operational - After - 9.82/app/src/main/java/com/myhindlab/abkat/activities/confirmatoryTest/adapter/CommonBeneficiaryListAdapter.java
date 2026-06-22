package com.myhindlab.abkat.activities.confirmatoryTest.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.confirmatoryTest.AssignTeamConfirmatoryTestActivity;
import com.myhindlab.abkat.activities.confirmatoryTest.Model.CommonBeneficiaryListModel;

import java.util.List;

public class CommonBeneficiaryListAdapter extends RecyclerView.Adapter<CommonBeneficiaryListAdapter.myview> {
    List<CommonBeneficiaryListModel.Output> countList;
    onTouchListner onTouchListner;
    Context context;

    public interface onTouchListner {
        public void onDataClick(CommonBeneficiaryListModel.Output item);

    }

    public CommonBeneficiaryListAdapter(Context context, List<CommonBeneficiaryListModel.Output> countList, onTouchListner onTouchListner) {
        this.countList = countList;
        this.onTouchListner = onTouchListner;
        this.context = context;
    }

    @NonNull
    @Override
    public myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_ct_medicine_beneficiary_list, parent, false);
        return new myview(view);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull myview holder, int position) {
        CommonBeneficiaryListModel.Output item = countList.get(position);
        holder.setIsRecyclable(false);
//        holder.tv_SrName.setText(String.valueOf(position + 1));
        holder.tv_beneficiary_Name.setText(item.getPatientName());
        holder.tv_pincode.setText("" + item.getPincode());
        holder.tv_CT_Status.setText(item.getCTStatus());
        holder.tv_medicine_status.setText(item.getMedicineStatus());


        holder.tv_beneficiary_Name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTouchListner.onDataClick(item);
            }
        });



        if (item.getCTStatus()!=null){

            if (item.getCTStatus().equalsIgnoreCase("Assignment Pending")){

                holder.tv_CT_Status.setTextColor(context.getResources().getColor(R.color.red));

            }else if (item.getCTStatus().equalsIgnoreCase("Sample Collected")){
                holder.tv_CT_Status.setTextColor(context.getResources().getColor(R.color.green));

            }else {
                holder.tv_CT_Status.setTextColor(context.getResources().getColor(R.color.blue));

            }


        }





        if (item.getMedicineStatus()!=null){


            if (item.getMedicineStatus().equalsIgnoreCase("Prescription Delivered")){

                holder.tv_medicine_status.setTextColor(context.getResources().getColor(R.color.green));
            }else {
                holder.tv_medicine_status.setTextColor(context.getResources().getColor(R.color.blue));

            }
        }


        if (item.getCTStatus()!=null){

            if (item.getCTStatus().equalsIgnoreCase("Assignment Pending")){

                holder.tv_CT_Status.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        context.startActivity(new Intent(context, AssignTeamConfirmatoryTestActivity.class)
                                .putExtra("beneficiaryDetails", item)
                                .putExtra("flag", 2)
                        );
                    }
                });

            }
        }


    }

    @Override
    public int getItemCount() {
        return countList.size();
    }

    class myview extends RecyclerView.ViewHolder {
        TextView tv_beneficiary_Name,tv_pincode,tv_CT_Status,tv_medicine_status;


        public myview(@NonNull View itemView) {
            super(itemView);

            tv_beneficiary_Name = itemView.findViewById(R.id.tv_beneficiary_Name);
            tv_pincode = itemView.findViewById(R.id.tv_pincode);
            tv_CT_Status = itemView.findViewById(R.id.tv_CT_Status);
            tv_medicine_status = itemView.findViewById(R.id.tv_medicine_status);


        }
    }
}
