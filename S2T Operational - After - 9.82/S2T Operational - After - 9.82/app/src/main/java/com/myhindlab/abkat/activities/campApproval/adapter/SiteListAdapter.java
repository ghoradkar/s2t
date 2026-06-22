package com.myhindlab.abkat.activities.campApproval.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.regularcampcreation.model.SiteListModel;


import java.util.List;

public class SiteListAdapter extends RecyclerView.Adapter<SiteListAdapter.SiteListViewHolder> {
    private List<SiteListModel.Output> list;
    private SiteListEvent siteListEvent;
    private int type = 0;

    public interface SiteListEvent {
        void onSiteSelected(SiteListModel.Output output);
    }


    public SiteListAdapter(List<SiteListModel.Output> list, SiteListEvent siteListEvent) {
        this.list = list;
        this.siteListEvent = siteListEvent;
    }

    public SiteListAdapter(List<SiteListModel.Output> list, SiteListEvent siteListEvent, int type) {
        this.list = list;
        this.siteListEvent = siteListEvent;
        this.type = type;
    }

    @NonNull
    @Override
    public SiteListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.site_list_item, parent, false);

        return new SiteListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SiteListViewHolder holder, int position) {
        SiteListModel.Output output = list.get(holder.getAbsoluteAdapterPosition());

        holder.siteName.setText(output.getSiteName());
        holder.siteAddress.setText(output.getCampLocation());

        if (output.isChecked()) {
            holder.cbSite.setChecked(true);
        } else {

            holder.cbSite.setChecked(false);

        }

        if (type == 0) {
            holder.clExpectedBeneficiary.setVisibility(View.VISIBLE);
            holder.expectedBeneficiary.setText(String.valueOf(output.getNoWorkersRegister()));

            holder.cbSite.setOnCheckedChangeListener((v, checked) -> {
                output.setChecked(checked);
                siteListEvent.onSiteSelected(output);

            });
        } else {
            holder.clExpectedBeneficiary.setVisibility(View.GONE);

            holder.expectedBeneficiary.setText(String.valueOf(output.getExpectedbeneficiarycount()));

            holder.cbSite.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    class SiteListViewHolder extends RecyclerView.ViewHolder {
        private TextView siteName, siteAddress, expectedBeneficiary;
        private CheckBox cbSite;
        private LinearLayoutCompat clExpectedBeneficiary;

        public SiteListViewHolder(@NonNull View itemView) {
            super(itemView);

            siteAddress = itemView.findViewById(R.id.tvSiteAddressValue);
            siteName = itemView.findViewById(R.id.tvSiteNameValue);
            expectedBeneficiary = itemView.findViewById(R.id.tvExpectedBeneficiaryValue);
            cbSite = itemView.findViewById(R.id.cbSelectedSite);
            clExpectedBeneficiary = itemView.findViewById(R.id.clExpectedBeneficiary);
        }
    }
}
