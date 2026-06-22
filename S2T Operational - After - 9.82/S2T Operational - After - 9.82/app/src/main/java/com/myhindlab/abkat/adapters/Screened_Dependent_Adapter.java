package com.myhindlab.abkat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.DependentModel;
import com.myhindlab.abkat.models.ScreenedDependentModel;

import java.util.List;

public class Screened_Dependent_Adapter extends RecyclerView.Adapter<Screened_Dependent_Adapter.MyViewHolder>{

    private List<ScreenedDependentModel.Output> screenedBeneficiaryList;
    private Context context;
    private String campId, type;
    public static int itemClickedPosition = 0;



    public Screened_Dependent_Adapter(Context context, List<ScreenedDependentModel.Output> resultArrayList) {
        this.context = context;
        this.screenedBeneficiaryList = resultArrayList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_row_screened_dependent, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int pos) {
        final int position = holder.getAbsoluteAdapterPosition();
        final ScreenedDependentModel.Output patientDetails = screenedBeneficiaryList.get(position);


        holder.tv_patientname.setText(patientDetails.getFirstName());
        holder.tv_cardno.setText(patientDetails.getScreeningDate());
        holder.tv_type.setText(patientDetails.getRelation());
        holder.tv_screening_date.setText(patientDetails.getAge());

    }



    @Override
    public int getItemCount() {
        return screenedBeneficiaryList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private CardView cv_title;
        private TextView tv_srno, tv_patientname, tv_cardno,tv_date, tv_type,tv_screening_date;
        private ImageView imvCall;

        public MyViewHolder(View view) {
            super(view);
            cv_title = view.findViewById(R.id.cv_title);
            tv_srno = view.findViewById(R.id.tv_srno);
            tv_patientname = view.findViewById(R.id.tv_patientname);
            tv_date = view.findViewById(R.id.tv_date);
            tv_cardno = view.findViewById(R.id.tv_cardno);
            imvCall = view.findViewById(R.id.imvCall);
            tv_type = view.findViewById(R.id.tv_type);
            tv_screening_date = view.findViewById(R.id.tv_screening_date);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }







}
