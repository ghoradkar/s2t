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
import com.myhindlab.abkat.activities.UnregisteredWorkerRegNumEntry_Activity;
import com.myhindlab.abkat.models.UnregisteredWorkersModel;

import java.util.List;

public class UnregisteredRegisteredWorkerAdapter extends RecyclerView.Adapter<UnregisteredRegisteredWorkerAdapter.MyViewHolder> {

    private List<UnregisteredWorkersModel> resultArrayList;
    private Context context;
    private String SiteDetailId;

    public UnregisteredRegisteredWorkerAdapter(Context context, List<UnregisteredWorkersModel> resultArrayList, String SiteDetailId) {
        this.context = context;
        this.resultArrayList = resultArrayList;
        this.SiteDetailId = SiteDetailId;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_row_unreg_reg_worker, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int pos) {
        final int position = holder.getAdapterPosition();
        final UnregisteredWorkersModel patientDetails = resultArrayList.get(position);

        holder.tv_patientname.setText(patientDetails.getFname() + " " + patientDetails.getMname() + " " + patientDetails.getLname());
        holder.tv_dob.setText(patientDetails.getBirth_Date());

        if (patientDetails.getGender().equalsIgnoreCase("M")){
            holder.tv_gender.setText("Male");
        } else if (patientDetails.getGender().equalsIgnoreCase("F")){
            holder.tv_gender.setText("Female");
        } else if (patientDetails.getGender().equalsIgnoreCase("O")){
            holder.tv_gender.setText("Other");
        }

        holder.cv_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, UnregisteredWorkerRegNumEntry_Activity.class)
                .putExtra("patientDetails", patientDetails)
                .putExtra("SiteDetailId", SiteDetailId));
            }
        });
    }

    @Override
    public int getItemCount() {
        return resultArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private CardView cv_title;
        private TextView tv_patientname, tv_dob, tv_gender;

        public MyViewHolder(View view) {
            super(view);
            cv_title = view.findViewById(R.id.cv_title);
            tv_patientname = view.findViewById(R.id.tv_patientname);
            tv_dob = view.findViewById(R.id.tv_dob);
            tv_gender = view.findViewById(R.id.tv_gender);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
