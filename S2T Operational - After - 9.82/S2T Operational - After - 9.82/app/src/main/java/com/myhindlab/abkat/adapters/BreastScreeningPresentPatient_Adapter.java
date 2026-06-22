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
import com.myhindlab.abkat.activities.HealthScreeningBreast_Activity_v3;
import com.myhindlab.abkat.models.PresentPatientList_Model;

import java.util.List;

public class BreastScreeningPresentPatient_Adapter extends RecyclerView.Adapter<BreastScreeningPresentPatient_Adapter.MyViewHolder> {

    private List<PresentPatientList_Model> resultArrayList;
    private Context context;
    private String campId, type, breastScreeningDeviceId, breastScreeningDeviceName;
    public static int itemClickedPosition = 0;

    public BreastScreeningPresentPatient_Adapter(Context context,
                                                 List<PresentPatientList_Model> resultArrayList,
                                                 String campId,
                                                 String type,
                                                 String breastScreeningDeviceId,
                                                 String breastScreeningDeviceName) {
        this.context = context;
        this.resultArrayList = resultArrayList;
        this.campId = campId;
        this.type = type;
        this.breastScreeningDeviceId = breastScreeningDeviceId;
        this.breastScreeningDeviceName = breastScreeningDeviceName;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_row_presentpatient, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int pos) {
        final int position = holder.getAdapterPosition();
        final PresentPatientList_Model patientDetails = resultArrayList.get(position);

        holder.tv_srno.setText("" + (position + 1));
        holder.tv_patientname.setText(patientDetails.getEnglishName());
        holder.tv_cardno.setText(String.valueOf(patientDetails.getRegdNo()));

        holder.cv_title.setOnClickListener(v -> {
            itemClickedPosition = position;
            context.startActivity(new Intent(context, HealthScreeningBreast_Activity_v3.class)
                    .putExtra("patientDetails", patientDetails)
                    .putExtra("healthScreentype", type)
                    .putExtra("campId", campId)
                    .putExtra("breastScreeningDeviceId", breastScreeningDeviceId)
                    .putExtra("breastScreeningDeviceName", breastScreeningDeviceName));
        });

    }

    @Override
    public int getItemCount() {
        return resultArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private CardView cv_title;
        private TextView tv_srno, tv_patientname, tv_cardno;

        public MyViewHolder(View view) {
            super(view);
            cv_title = view.findViewById(R.id.cv_title);
            tv_srno = view.findViewById(R.id.tv_srno);
            tv_patientname = view.findViewById(R.id.tv_patientname);
            tv_cardno = view.findViewById(R.id.tv_cardno);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
