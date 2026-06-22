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

import com.myhindlab.abkat.BuildConfig;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.confirmatoryTest.AppointmentConfirmationConfirmatoryActivity;
import com.myhindlab.abkat.activities.confirmatoryTest.HealthScreeningSamplecollectionConfirmatoryNewActivity;
import com.myhindlab.abkat.activities.confirmatoryTest.HealthScreeningSamplecollectionConfirmatory_Activity;
import com.myhindlab.abkat.models.DependentForSearchBeneficiaryModel;
import com.myhindlab.abkat.utilities.Utilities;

import java.util.List;

public class Dependent_Adapter_For_Search_Beneficiary_Confirmatory extends RecyclerView.Adapter<Dependent_Adapter_For_Search_Beneficiary_Confirmatory.MyViewHolder>{

    private List<DependentForSearchBeneficiaryModel> dependentList;
    private Context context;
    private String campId, type;
    public static int itemClickedPosition = 0;
    private DepedentDetailsConfimatory eventListener;

    public   interface DepedentDetailsConfimatory{
        void onBeneficiaryClick(DependentForSearchBeneficiaryModel team);
    }




    public Dependent_Adapter_For_Search_Beneficiary_Confirmatory(Context context, List<DependentForSearchBeneficiaryModel> resultArrayList, DepedentDetailsConfimatory eventListener) {
        this.context = context;
        this.dependentList = resultArrayList;
        this.eventListener = eventListener;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_row_dependent_for_search_beneficiary_confirmatory, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int pos) {
        final int position = holder.getAbsoluteAdapterPosition();
        final DependentForSearchBeneficiaryModel patientDetails = dependentList.get(position);


        holder.tv_patientname.setText(patientDetails.getBeneficiaryName() );
        holder.tv_type.setText(patientDetails.getRelationWithWorker() );



        if (patientDetails.getSampleCollection()!=null){
            if (patientDetails.getSampleCollection().equalsIgnoreCase("Y")){
                holder.headerLL.setBackgroundColor(context.getResources().getColor(R.color.greenLight));
                holder.tv_patientname.setClickable(false);
            }else if (patientDetails.getSampleCollection().equalsIgnoreCase("N")){
                holder.headerLL.setBackgroundColor(context.getResources().getColor(R.color.white));
                holder.tv_patientname.setClickable(true);
            }
        }


//        holder.tv_cardno.setText(patientDetails.getScreeningDate());

   //     holder.tv_date.setText(patientDetails.getLastDependantScreeningDate());

        holder.imvCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                eventListener.onBeneficiaryClick(patientDetails);

            }
        });

        holder.tv_patientname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (patientDetails.getArId().equalsIgnoreCase("4")){
                    Utilities.showAlertDialog(context,"Alert","You are not allowed to perform CT as the beneficiary is not interested in CT.",false);
                    return;
                }


                if (BuildConfig.isBeta){
                    context.startActivity(new Intent(context, HealthScreeningSamplecollectionConfirmatoryNewActivity.class)
                            .putExtra("regdId", patientDetails.getRegdid())
                            .putExtra("regNo",patientDetails.getRegdNo())
                            .putExtra("distCode",patientDetails.getdISTLGDCODE())
                            .putExtra("appointmentConfirm",patientDetails.getIsAppointmentDone())
                            .putExtra("isSampleCollected",patientDetails.getSampleCollection())
                            .putExtra("mobileNo",patientDetails.getMobileNo())
                            .putExtra("remarkId",patientDetails.getArId())
                            .putExtra("remark",patientDetails.getAssignmentRemarks())
                            .putExtra("workerMobile",patientDetails.getWorkersMob())
                            .putExtra("alternateMobile",patientDetails.getAlternateMobNo())
                    );

                }else {
                    context.startActivity(new Intent(context, HealthScreeningSamplecollectionConfirmatoryNewActivity.class)
                            .putExtra("regdId", patientDetails.getRegdid())
                            .putExtra("regNo",patientDetails.getRegdNo())
                            .putExtra("distCode",patientDetails.getdISTLGDCODE())
                            .putExtra("appointmentConfirm",patientDetails.getIsAppointmentDone())
                            .putExtra("isSampleCollected",patientDetails.getSampleCollection())
                            .putExtra("mobileNo",patientDetails.getMobileNo())
                            .putExtra("remarkId",patientDetails.getArId())
                            .putExtra("remark",patientDetails.getAssignmentRemarks())
                            .putExtra("workerMobile",patientDetails.getWorkersMob())
                            .putExtra("alternateMobile",patientDetails.getAlternateMobNo())
                    );

                }


            }
        });
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
