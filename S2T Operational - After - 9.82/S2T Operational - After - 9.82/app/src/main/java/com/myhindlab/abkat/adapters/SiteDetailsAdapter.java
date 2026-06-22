package com.myhindlab.abkat.adapters;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.ConstructionSitesList_Model;

import java.util.ArrayList;

public class SiteDetailsAdapter extends RecyclerView.Adapter<SiteDetailsAdapter.MyViewHolder> {

    private ArrayList<ConstructionSitesList_Model> constructionSitesList;

    public SiteDetailsAdapter(ArrayList<ConstructionSitesList_Model> constructionSitesList) {
        this.constructionSitesList = constructionSitesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_sitedetails, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        ConstructionSitesList_Model data = new ConstructionSitesList_Model();
        data = constructionSitesList.get(position);

        holder.tv_sitename.setText(data.getSiteName());
//        holder.tv_reranumber.setText(data.getReraId());

        holder.tv_address.setText(data.getSiteAddress() + ", "
                + data.getDISTNAME() + ", "
                + data.getCity() + ", "
                + data.getTALNAME() + ", "
                + data.getPinCode());
    }

    @Override
    public int getItemCount() {
        return constructionSitesList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
//        public TextView tv_reranumber;
        TextView tv_sitename, tv_address;

        public MyViewHolder(final View view) {
            super(view);
//            tv_reranumber = view.findViewById(R.id.tv_reranumber);
            tv_sitename = view.findViewById(R.id.tv_sitename);
            tv_address = view.findViewById(R.id.tv_address);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}