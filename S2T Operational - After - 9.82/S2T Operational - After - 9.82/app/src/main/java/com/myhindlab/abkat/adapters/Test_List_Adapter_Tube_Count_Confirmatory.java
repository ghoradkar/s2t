package com.myhindlab.abkat.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.DependentForSearchBeneficiaryModel;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.List;

public class Test_List_Adapter_Tube_Count_Confirmatory extends RecyclerView.Adapter<Test_List_Adapter_Tube_Count_Confirmatory.MyViewHolder>{

    private List<DependentForSearchBeneficiaryModel> dependentList;
    private Context context;
    private String campId, type;
    public static int itemClickedPosition = 0;
//    private DepedentDetailsConfimatory eventListener;

    public Test_List_Adapter_Tube_Count_Confirmatory(Context context, List<DependentForSearchBeneficiaryModel> resultArrayList) {
        this.context = context;
        this.dependentList = resultArrayList;
//        this.eventListener = eventListener;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_row_test_list_tube_countconfirmatory, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int pos) {
        final int position = holder.getAbsoluteAdapterPosition();
        final DependentForSearchBeneficiaryModel patientDetails = dependentList.get(position);


        holder.tv_patientname.setText(patientDetails.getTubName());
//        holder.tv_type.set(patientDetails.getCatName());

       if (patientDetails.getTubeId().equalsIgnoreCase("1")){
           holder.Imv_color.setColorFilter(ContextCompat.getColor(context, R.color.red));

       }


        holder.edt_tubeCount.setText("1");
        dependentList.get(position).setCount("1");


        holder.edt_tubeCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                 {
                    if (!s.toString().equalsIgnoreCase(""))
                        dependentList.get(position).setCount(s.toString());
                    else dependentList.get(position).setCount("0");

                }
            }




            @Override
            public void afterTextChanged(Editable s) {

            }
        });


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
        private TextView tv_srno, tv_patientname, tv_cardno,tv_date,tv_type;
        private ImageView imvCall,Imv_color;
        private MaterialEditText edt_tubeCount;
        private LinearLayout headerLL;

        public MyViewHolder(View view) {
            super(view);
            cv_title = view.findViewById(R.id.cv_title);
            tv_srno = view.findViewById(R.id.tv_srno);
            tv_patientname = view.findViewById(R.id.tv_patientname);
            tv_date = view.findViewById(R.id.tv_date);
            tv_cardno = view.findViewById(R.id.tv_cardno);
            imvCall = view.findViewById(R.id.imvCall);
            Imv_color = view.findViewById(R.id.Imv_color);
            tv_type = view.findViewById(R.id.tv_type);
            headerLL = view.findViewById(R.id.headerLL);
            edt_tubeCount = view.findViewById(R.id.edt_tubeCount);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


}
