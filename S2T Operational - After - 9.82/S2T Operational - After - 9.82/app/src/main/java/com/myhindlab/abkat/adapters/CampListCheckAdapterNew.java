package com.myhindlab.abkat.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.RecyclerView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.couriermodule.CamplistOnLandingLabModel;
import com.myhindlab.abkat.utilities.Utilities;

import java.util.ArrayList;

public class CampListCheckAdapterNew extends RecyclerView.Adapter<CampListCheckAdapterNew.MyViewHolder> {


    private final ArrayList<CamplistOnLandingLabModel.Output> camplist;

    public CampListCheckAdapterNew(ArrayList<CamplistOnLandingLabModel.Output> camplist) {
        this.camplist = camplist;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_checkbox, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        CamplistOnLandingLabModel.Output data = camplist.get(position);

        holder.cbItem.setText(String.valueOf(data.getCampid()));
        holder.cbItem.setChecked(data.isChecked());
        holder.cbItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                data.setChecked(checked);
            }
        });

//        holder.cbItem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                   /* if (screeningTestList.get(position).getTestName().equalsIgnoreCase("Lung Functioin Test")) {
//                        screeningTestList.get(position).setChecked(false);
//                        holder.cb_select.setChecked(false);
//                        Utilities.showToastMessage("This test is not functional.", context, false);
//                    } else*/
//                if (camplist.get(position).getCampDate().equalsIgnoreCase("Antigen")) {
//                    camplist.get(position).setChecked(false);
//                    holder.cbItem.setChecked(false);
//                    Utilities.showToastMessage("This test is not functional.", context, false);
//                }
//            }
//        });


//        holder.cbItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if (b) {
//                    selectedcamplist.add(camplist.get(position));
//                    camplist.get(position).setChecked(b);
//
//                } else {
//                    selectedBarcodeList.remove(camplist.get(position));
//                    camplist.get(position).setChecked(b);
//
//
//                }
//            }
//        });



    }

    @Override
    public int getItemCount() {
        return camplist.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public CheckBox cbItem;


        public MyViewHolder(final View view) {
            super(view);
            cbItem = view.findViewById(R.id.cbItem);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}