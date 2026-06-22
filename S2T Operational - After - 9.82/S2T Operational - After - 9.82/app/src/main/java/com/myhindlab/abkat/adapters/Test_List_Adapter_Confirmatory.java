package com.myhindlab.abkat.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.confirmatoryTest.HealthScreeningSamplecollectionConfirmatory_Activity;
import com.myhindlab.abkat.models.DependentForSearchBeneficiaryModel;

import java.util.List;

public class Test_List_Adapter_Confirmatory extends RecyclerView.Adapter<Test_List_Adapter_Confirmatory.MyViewHolder>{

    private List<DependentForSearchBeneficiaryModel> dependentList;
    private Context context;
    private String campId, type;
    public static int itemClickedPosition = 0;
//    private DepedentDetailsConfimatory eventListener;

    public Test_List_Adapter_Confirmatory(Context context, List<DependentForSearchBeneficiaryModel> resultArrayList) {
        this.context = context;
        this.dependentList = resultArrayList;
//        this.eventListener = eventListener;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_row_test_list_confirmatory, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int pos) {
        final int position = holder.getAbsoluteAdapterPosition();
        final DependentForSearchBeneficiaryModel patientDetails = dependentList.get(position);


        holder.tv_patientname.setText(patientDetails.getServiceName());
        holder.tv_type.setText(patientDetails.getSampleQuantity());

//        holder.tv_cardno.setText(patientDetails.getScreeningDate());

   //     holder.tv_date.setText(patientDetails.getLastDependantScreeningDate());

//        holder.imvCall.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                eventListener.onBeneficiaryClick(patientDetails);
//
//            }
//        });

//        holder.tv_patientname.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                context.startActivity(new Intent(context, HealthScreeningSamplecollectionConfirmatory_Activity.class)
//                        .putExtra("regdId", patientDetails.getRegdid())
//                        .putExtra("regNo",patientDetails.getRegdNo())
//                        .putExtra("distCode",patientDetails.getdISTLGDCODE()));
//
//            }
//        });
    }



    @Override
    public int getItemCount() {
        return dependentList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private CardView cv_title;
        private TextView tv_srno, tv_patientname, tv_cardno,tv_date, tv_type;
        private ImageView imvCall;
        private LinearLayout headerLL;

        public MyViewHolder(View view) {
            super(view);
            cv_title = view.findViewById(R.id.cv_title);
            tv_srno = view.findViewById(R.id.tv_srno);
            tv_patientname = view.findViewById(R.id.tv_patientname);
            tv_date = view.findViewById(R.id.tv_date);
            tv_cardno = view.findViewById(R.id.tv_cardno);
            imvCall = view.findViewById(R.id.imvCall);
            tv_type = view.findViewById(R.id.tv_type);
            headerLL = view.findViewById(R.id.headerLL);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


}
