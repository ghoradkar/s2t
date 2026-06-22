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

import java.util.List;

public class Dependent_Adapter extends RecyclerView.Adapter<Dependent_Adapter.MyViewHolder>{

    private List<DependentModel> dependentList;
    private Context context;
    private String campId, type;
    public static int itemClickedPosition = 0;
    DependentEvents dependentEvents;

    public interface DependentEvents {
        void onDelete(DependentModel dependentModel);
    }


    public Dependent_Adapter(Context context, List<DependentModel> resultArrayList, DependentEvents dependentEvents) {
        this.context = context;
        this.dependentList = resultArrayList;
        this.dependentEvents = dependentEvents;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_row_dependent, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int pos) {
        final int position = holder.getAbsoluteAdapterPosition();
        final DependentModel patientDetails = dependentList.get(position);

      //  holder.imvCall.

//        holder.tv_srno.setText("" + (position + 1));
        holder.tv_patientname.setText(patientDetails.getFirstName()+" " +patientDetails.getMiddleName()+" "+patientDetails.getLastName() );
//        holder.tv_cardno.setText(String.valueOf(patientDetails.getRelId()));
        if (patientDetails.getRelId().equalsIgnoreCase("7")){
            holder.tv_type.setText("Son");
        }else if (patientDetails.getRelId().equalsIgnoreCase("8")){
            holder.tv_type.setText("Daughter");
        }else if (patientDetails.getRelId().equalsIgnoreCase("10")){
            holder.tv_type.setText("Wife");
        } else if (patientDetails.getRelId().equalsIgnoreCase("9")) {
            holder.tv_type.setText("Husband");
        }else if (patientDetails.getRelId().equalsIgnoreCase("1")){
            holder.tv_type.setText("Father");

        }else if (patientDetails.getRelId().equalsIgnoreCase("2")){
            holder.tv_type.setText("Mother");

        }else if (patientDetails.getRelId().equalsIgnoreCase("21")){
            holder.tv_type.setText("Mother in law");

        }else if (patientDetails.getRelId().equalsIgnoreCase("22")){
            holder.tv_type.setText("Father in law");

        }
        holder.tv_cardno.setText(patientDetails.getLastDependantScreeningDate());
        holder.tv_age.setText(patientDetails.getAge());

   //     holder.tv_date.setText(patientDetails.getLastDependantScreeningDate());

        holder.imvCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String getItem = String.valueOf(dependentList.get(position));
                dependentList.remove(getItem);
              //  dependentList.remove(holder.getAbsoluteAdapterPosition());
                dependentEvents.onDelete(patientDetails);

            }
        });




    }









    @Override
    public int getItemCount() {
        return dependentList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private CardView cv_title;
        private TextView tv_srno, tv_patientname, tv_cardno,tv_date, tv_type,tv_age;
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
            tv_age = view.findViewById(R.id.tv_age);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }







}
