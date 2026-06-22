package com.myhindlab.abkat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.PatientStatusModel;

import java.util.List;

public class CampPatientStatusForBeneficiaryVerificationAdapter extends RecyclerView.Adapter<CampPatientStatusForBeneficiaryVerificationAdapter.MyViewHolder> {

    private List<PatientStatusModel> resultArrayList;
    private Context context;

    public CampPatientStatusForBeneficiaryVerificationAdapter(Context context, List<PatientStatusModel> resultArrayList) {
        this.context = context;
        this.resultArrayList = resultArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_row_camp_patientstatus, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int pos) {
        final int position = holder.getAdapterPosition();
        PatientStatusModel patientStatus = resultArrayList.get(position);

        holder.tv_patientname.setText(patientStatus.getPatientName() + " (" + patientStatus.getRegdNo() + ")");

        if (patientStatus.getBasicDetails().equals("NA")) {
            holder.tv_basic.setVisibility(View.GONE);
            holder.tv_basic_na.setVisibility(View.VISIBLE);
        } else {
            holder.tv_basic_na.setVisibility(View.GONE);
            holder.tv_basic.setImageResource(patientStatus.getBasicDetails().equals("0") ? R.drawable.icon_cross : R.drawable.icon_check);
        }

        if (patientStatus.getPhysicalExamination().equals("NA")) {
            holder.tv_PE.setVisibility(View.GONE);
            holder.tv_PE_na.setVisibility(View.VISIBLE);
        } else {
            holder.tv_PE_na.setVisibility(View.GONE);
            holder.tv_PE.setImageResource(patientStatus.getPhysicalExamination().equals("0") ? R.drawable.icon_cross : R.drawable.icon_check);
        }

        if (patientStatus.getLungFunctioinTest().equals("NA")) {
            holder.tv_LFT.setVisibility(View.GONE);
            holder.tv_LFT_na.setVisibility(View.VISIBLE);
        } else {
            holder.tv_LFT_na.setVisibility(View.GONE);
            holder.tv_LFT.setImageResource(patientStatus.getLungFunctioinTest().equals("0") ? R.drawable.icon_cross : R.drawable.icon_check);
        }

        if (patientStatus.getVisionScreening().equals("NA")) {
            holder.tv_VST.setVisibility(View.GONE);
            holder.tv_VST_na.setVisibility(View.VISIBLE);
        } else {
            holder.tv_VST_na.setVisibility(View.GONE);
            holder.tv_VST.setImageResource(patientStatus.getVisionScreening().equals("0") ? R.drawable.icon_cross : R.drawable.icon_check);
        }

        if (patientStatus.getAudioScreeningTest().equals("NA")) {
            holder.tv_AST.setVisibility(View.GONE);
            holder.tv_AST_na.setVisibility(View.VISIBLE);
        } else {
            holder.tv_AST_na.setVisibility(View.GONE);
            holder.tv_AST.setImageResource(patientStatus.getAudioScreeningTest().equals("0") ? R.drawable.icon_cross : R.drawable.icon_check);
        }

        if (patientStatus.getBarcode().equals("NA")) {
            holder.tv_SC.setVisibility(View.GONE);
            holder.tv_SC_na.setVisibility(View.VISIBLE);
        } else {

            holder.tv_SC_na.setVisibility(View.GONE);
            holder.tv_SC.setImageResource(patientStatus.getBarcode().equals("0") ? R.drawable.icon_cross : R.drawable.icon_check);
        }

//        if (patientStatus.getPPSampleCollection().equals("NA")) {
//            holder.tv_PP.setVisibility(View.GONE);
//            holder.tv_PP_na.setVisibility(View.VISIBLE);
//        } else {
//            holder.tv_PP_na.setVisibility(View.GONE);
//            holder.tv_PP.setImageResource(patientStatus.getPPSampleCollection().equals("0") ? R.drawable.icon_cross : R.drawable.icon_check);
//        }

        if (patientStatus.getAckowledgement().equals("NA")) {
            holder.tv_acknowlege.setVisibility(View.GONE);
            holder.tv_acknowlege_na.setVisibility(View.GONE);
        } else {
            holder.tv_acknowlege_na.setVisibility(View.GONE);
            holder.tv_acknowlege.setImageResource(patientStatus.getAckowledgement().equals("0") ? R.drawable.icon_cross : R.drawable.icon_check);
        }

        if (patientStatus.getUrineSampleCollection().equals("NA")) {
            holder.tv_urinesample.setVisibility(View.GONE);
            holder.tv_urinesample_na.setVisibility(View.GONE);
        } else {
            holder.tv_urinesample_na.setVisibility(View.GONE);

            holder.tv_urinesample.setImageResource(patientStatus.getUrineSampleCollection().equals("0") ? R.drawable.icon_cross : R.drawable.icon_check);
        }

//        if (patientStatus.getBreastScreening().equals("NA")) {
//            holder.tv_breastscreening.setVisibility(View.GONE);
//            holder.tv_breastscreening_na.setVisibility(View.VISIBLE);
//        } else {
//            if (patientStatus.getGender().equalsIgnoreCase("F")) {
//                holder.tv_breastscreening.setImageResource(patientStatus.getBreastScreening().equals("0") ? R.drawable.icon_cross : R.drawable.icon_check);
//                holder.tv_breastscreening_na.setVisibility(View.GONE);
//            } else {
//                holder.tv_breastscreening.setVisibility(View.GONE);
//                holder.tv_breastscreening_na.setVisibility(View.VISIBLE);
//            }
//        }

//        if (patientStatus.getAntigen().equals("NA")) {
//            holder.tv_antigenTest.setVisibility(View.GONE);
//            holder.tv_antigen_na.setVisibility(View.VISIBLE);
//        } else {
//            holder.tv_antigen_na.setVisibility(View.GONE);
//            holder.tv_antigenTest.setImageResource(patientStatus.getAntigen().equals("0") ? R.drawable.icon_cross : R.drawable.icon_check);
//        }
//        if (patientStatus.getAntigen().equals("NA")) {
//            holder.tv_rtpcr.setVisibility(View.GONE);
//            holder.tv_rtpcr_na.setVisibility(View.VISIBLE);
//        } else {
//            holder.tv_rtpcr_na.setVisibility(View.GONE);
//            holder.tv_rtpcr.setImageResource(patientStatus.getRTPCR().equals("0") ? R.drawable.icon_cross : R.drawable.icon_check);
//        }
    }

    @Override
    public int getItemCount() {
        return resultArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_patientname, tv_basic_na, tv_PE_na, tv_LFT_na, tv_VST_na, tv_AST_na, tv_SC_na, tv_PP_na, tv_acknowlege_na, tv_breastscreening_na, tv_antigen_na,
                tv_rtpcr_na, tv_urinesample_na;
        private ImageView tv_basic, tv_PE, tv_LFT, tv_VST, tv_AST, tv_SC, tv_PP, tv_acknowlege, tv_urinesample, tv_breastscreening,
                tv_antigenTest, tv_rtpcr;

        public MyViewHolder(View view) {
            super(view);
            tv_patientname = view.findViewById(R.id.tv_patientname);
            tv_basic = view.findViewById(R.id.tv_basic);
            tv_PE = view.findViewById(R.id.tv_PE);
            tv_LFT = view.findViewById(R.id.tv_LFT);
            tv_VST = view.findViewById(R.id.tv_VST);
            tv_AST = view.findViewById(R.id.tv_AST);
            tv_SC = view.findViewById(R.id.tv_SC);
            tv_PP = view.findViewById(R.id.tv_PP);
            tv_basic_na = view.findViewById(R.id.tv_basic_na);
            tv_PE_na = view.findViewById(R.id.tv_PE_na);
            tv_LFT_na = view.findViewById(R.id.tv_LFT_na);
            tv_VST_na = view.findViewById(R.id.tv_VST_na);
            tv_AST_na = view.findViewById(R.id.tv_AST_na);
            tv_SC_na = view.findViewById(R.id.tv_SC_na);
            tv_PP_na = view.findViewById(R.id.tv_PP_na);
            tv_acknowlege_na = view.findViewById(R.id.tv_acknowlege_na);
            tv_acknowlege = view.findViewById(R.id.tv_acknowlege);
            tv_urinesample = view.findViewById(R.id.tv_urinesample);
            tv_breastscreening = view.findViewById(R.id.tv_breastscreening);
            tv_breastscreening_na = view.findViewById(R.id.tv_breastscreening_na);
            tv_antigen_na = view.findViewById(R.id.tv_antigen_na);
            tv_rtpcr_na = view.findViewById(R.id.tv_rtpcr_na);
            tv_antigenTest = view.findViewById(R.id.tv_antigenTest);
            tv_rtpcr = view.findViewById(R.id.tv_rtpcr);
            tv_urinesample_na = view.findViewById(R.id.tv_urinesample_na);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
