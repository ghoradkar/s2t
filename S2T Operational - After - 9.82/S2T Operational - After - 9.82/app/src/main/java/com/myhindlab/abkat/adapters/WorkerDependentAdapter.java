package com.myhindlab.abkat.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.WorkerDependentModel;

import java.util.ArrayList;

public class WorkerDependentAdapter extends RecyclerView.Adapter<WorkerDependentAdapter.WorkerDependentViewHolder> {

    private ArrayList<WorkerDependentModel.Output> outputs;
    private Context context;

    public WorkerDependentAdapter(ArrayList<WorkerDependentModel.Output> outputs) {
        this.outputs = outputs;
    }

    @NonNull
    @Override
    public WorkerDependentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_worker_dependent, parent, false);
        return new WorkerDependentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkerDependentViewHolder holder, int position) {
        WorkerDependentModel.Output output = outputs.get(position);

       // holder.tv_srno.setText("" + (position + 1));
        holder.tv_patientname.setText(output.getEnglishName());
        holder.tv_type.setText(output.getType());


        holder.cb_doctor_assign.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                output.setChecked(b);
            }
        });

//        if (output.getDoctorMappedStatus() != 0) {
//            holder.tv_cardno.setText(output.getDoctorName());
//            holder.cb_doctor_assign.setVisibility(View.VISIBLE);
//        } else {
//            holder.cb_doctor_assign.setVisibility(View.VISIBLE);
//            holder.tv_cardno.setVisibility(View.GONE);
//        }
        if (output.getPEStatus() == 1) {
            holder.cv_title.setCardBackgroundColor(context.getResources().getColor(R.color.dark_green));
//            holder.tv_cardno.setText(output.getDoctorName());
//            holder.tv_cardno.setVisibility(View.VISIBLE);
            holder.tv_cardno.setText(output.getDoctorName());

            holder.cb_doctor_assign.setVisibility(View.INVISIBLE);
        } else if (output.getDoctorMappedStatus() != 0) {
            holder.cv_title.setCardBackgroundColor(context.getResources().getColor(R.color.light_yellow));
            holder.cb_doctor_assign.setVisibility(View.VISIBLE);
            holder.tv_cardno.setText(output.getDoctorName());
//            holder.tv_cardno.setVisibility(View.GONE);

        } else {
            holder.cv_title.setCardBackgroundColor(context.getResources().getColor(R.color.white));
            holder.cb_doctor_assign.setVisibility(View.VISIBLE);
            holder.tv_cardno.setText("NA");

        }
        holder.tv_cardno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + output.getDoctorMobile()));
                context.startActivity(intent);            }
        });

    }

    @Override
    public int getItemCount() {
        return outputs.size();
    }

    class WorkerDependentViewHolder extends RecyclerView.ViewHolder {

        TextView tv_patientname, tv_type, tv_cardno, tv_srno;
        CheckBox cb_doctor_assign;
        CardView cv_title;

        public WorkerDependentViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_cardno = itemView.findViewById(R.id.tv_cardno);
            tv_patientname = itemView.findViewById(R.id.tv_patientname);
            tv_type = itemView.findViewById(R.id.tv_type);
            tv_srno = itemView.findViewById(R.id.tv_srno);
            cb_doctor_assign = itemView.findViewById(R.id.cb_doctor_assign);
            cv_title = itemView.findViewById(R.id.cv_title);
        }
    }
}
