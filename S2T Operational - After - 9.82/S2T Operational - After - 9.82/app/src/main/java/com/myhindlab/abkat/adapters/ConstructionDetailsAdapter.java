package com.myhindlab.abkat.adapters;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.GetListOfBuilderDetails;

import java.util.ArrayList;

public class ConstructionDetailsAdapter extends RecyclerView.Adapter<ConstructionDetailsAdapter.MyViewHolder> {

    private ArrayList<GetListOfBuilderDetails> contractorDetails;

    public ConstructionDetailsAdapter(ArrayList<GetListOfBuilderDetails> contractorDetails) {
        this.contractorDetails = contractorDetails;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_contractordetails, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        GetListOfBuilderDetails data = new GetListOfBuilderDetails();
        data = contractorDetails.get(position);

        holder.edt_contractorname.setText(data.getContractorName());
        holder.edt_contractornumber.setText(data.getContractorContactNo());
//        holder.edt_emailid.setText(data.getContractorEmailId());
    }

    @Override
    public int getItemCount() {
        return contractorDetails.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView edt_contractorname, edt_contractornumber;
//        public TextView edt_emailid;

        public MyViewHolder(final View view) {
            super(view);
            edt_contractorname = view.findViewById(R.id.edt_contractorname);
            edt_contractornumber = view.findViewById(R.id.edt_contractornumber);
//            edt_emailid = view.findViewById(R.id.edt_emailid);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}