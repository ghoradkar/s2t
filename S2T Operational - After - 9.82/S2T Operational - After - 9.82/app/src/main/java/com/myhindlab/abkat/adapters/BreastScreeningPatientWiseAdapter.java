package com.myhindlab.abkat.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.BreastScreeningPatientWiseModel;

import java.util.List;

public class BreastScreeningPatientWiseAdapter extends RecyclerView.Adapter<BreastScreeningPatientWiseAdapter.MyViewHolder> {

    private Context context;
    private List<BreastScreeningPatientWiseModel.OutputBean> patientList;

    public BreastScreeningPatientWiseAdapter(Context context, List<BreastScreeningPatientWiseModel.OutputBean> patientList) {
        this.context = context;
        this.patientList = patientList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.list_row_bscreening_patientwise, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        int position = holder.getAdapterPosition();
        BreastScreeningPatientWiseModel.OutputBean outputBean = patientList.get(position);

        holder.tv_reg_no.setText(String.valueOf(outputBean.getRegdNo()));
        holder.tv_name.setText(outputBean.getEnglishName());
        holder.tv_age.setText(String.valueOf(outputBean.getAge()));
    }

    @Override
    public int getItemCount() {
        return patientList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_reg_no, tv_name, tv_age;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_reg_no = itemView.findViewById(R.id.tv_reg_no);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_age = itemView.findViewById(R.id.tv_age);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

}
