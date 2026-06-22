package com.myhindlab.abkat.activities.campApproval.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.campApproval.models.AllocatedResourceListModel;
import com.myhindlab.abkat.activities.campApproval.models.ResourceDetailsForApprovalModel;
import com.rengwuxian.materialedittext.MaterialEditText;


import java.util.List;

public class ResourceListForApprovalAdapter extends RecyclerView.Adapter<ResourceListForApprovalAdapter.DesignationListViewHolder> {
    private List<ResourceDetailsForApprovalModel.Output> list;
    private ResourceListForApprovalEvent resourceListForApprovalEvent;

    public interface ResourceListForApprovalEvent {
        void onDesignationSelected(ResourceDetailsForApprovalModel.Output output);

    }


    public ResourceListForApprovalAdapter(List<ResourceDetailsForApprovalModel.Output> list, ResourceListForApprovalEvent resourceListForApprovalEvent) {
        this.list = list;
        this.resourceListForApprovalEvent = resourceListForApprovalEvent;
    }

    @NonNull
    @Override
    public DesignationListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.deisgnation_list_item, parent, false);

        return new DesignationListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DesignationListViewHolder holder, int position) {
        ResourceDetailsForApprovalModel.Output output = list.get(holder.getAdapterPosition());

        holder.tvRoleName.setText(output.getTestName());

        holder.edtSelResource.setText(output.getResourceName());
        if (output.getResources() != null) {
            int selResources = 0;
            for (AllocatedResourceListModel.Output resource : output.getResources()) {
                if (resource.isChecked() || resource.getResStatus().equalsIgnoreCase("Selected")) {
                    selResources = selResources + 1;
                }
            }

            if (selResources >= 1)
                holder.edtSelResource.setText(String.valueOf(selResources));
            else {
                holder.edtSelResource.setText("");
                holder.edtSelResource.setError("Select at least 1 resource");
            }
        }

        holder.edtSelResource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resourceListForApprovalEvent.onDesignationSelected(output);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public int getItemViewType(int position) {
        return position;
    }


  public  class DesignationListViewHolder extends RecyclerView.ViewHolder {
        private TextView tvRoleName;
        public MaterialEditText edtSelResource;

        public DesignationListViewHolder(@NonNull View itemView) {
            super(itemView);

            tvRoleName = itemView.findViewById(R.id.tvRoleName);
            edtSelResource = itemView.findViewById(R.id.edtSelResource);
        }
    }
}
