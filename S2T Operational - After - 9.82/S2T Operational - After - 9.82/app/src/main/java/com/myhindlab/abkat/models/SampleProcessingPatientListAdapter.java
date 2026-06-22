package com.myhindlab.abkat.models;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.myhindlab.abkat.R;

import java.util.List;

public class SampleProcessingPatientListAdapter extends RecyclerView.Adapter<SampleProcessingPatientListAdapter.MyViewHolder> {
    private Context context;
    private List<SampleProcessingPatientModel.OutputBean> patientList;
    private int type;

    public SampleProcessingPatientListAdapter(Context context, List<SampleProcessingPatientModel.OutputBean> patientList, int type) {
        this.context = context;
        this.patientList = patientList;
        this.type = type;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_row_sampleprocess_patient, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int pos) {
        int position = holder.getAdapterPosition();
        SampleProcessingPatientModel.OutputBean pateientDetails = patientList.get(position);

        holder.tv_srno.setText(position + 1 + "");
        holder.tv_barcode.setText(pateientDetails.getOrderId());
        holder.tv_name.setText(pateientDetails.getEnglishName());
    }

    @Override
    public int getItemCount() {
        return patientList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_srno, tv_barcode, tv_name;

        public MyViewHolder(@NonNull View view) {
            super(view);
            tv_srno = view.findViewById(R.id.tv_srno);
            tv_barcode = view.findViewById(R.id.tv_barcode);
            tv_name = view.findViewById(R.id.tv_name);
        }
    }
}
