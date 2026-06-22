package com.myhindlab.abkat.adapters;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.GetListOfBuilderDetails;

import java.util.ArrayList;

public class OtherPersonDetailsAdapter extends RecyclerView.Adapter<OtherPersonDetailsAdapter.MyViewHolder> {

    private ArrayList<GetListOfBuilderDetails> otherPersonDetails;

    public OtherPersonDetailsAdapter(ArrayList<GetListOfBuilderDetails> otherPersonDetails) {
        this.otherPersonDetails = otherPersonDetails;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_otherpersondetails, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        GetListOfBuilderDetails data = new GetListOfBuilderDetails();
        data = otherPersonDetails.get(position);

        holder.edt_otherpersonname.setText(data.getContractorName());
        holder.edt_otherpersonnumber.setText(data.getContractorContactNo());
//        holder.edt_emailid.setText(data.getContractorEmailId());
    }

    @Override
    public int getItemCount() {
        return otherPersonDetails.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView edt_otherpersonname, edt_otherpersonnumber;
//        public TextView edt_emailid;

        public MyViewHolder(final View view) {
            super(view);
            edt_otherpersonname = view.findViewById(R.id.edt_otherpersonname);
            edt_otherpersonnumber = view.findViewById(R.id.edt_otherpersonnumber);
//            edt_emailid = view.findViewById(R.id.edt_emailid);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}