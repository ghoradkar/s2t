package com.myhindlab.abkat.activities.regularcampcreation.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.regularcampcreation.model.ResourceListForMappingModel;
import com.myhindlab.abkat.activities.regularcampcreation.model.TestListForResourceMappingModel;
import com.rengwuxian.materialedittext.MaterialEditText;


import java.util.List;

public class TestListForResourceMappingAdapter extends RecyclerView.Adapter<TestListForResourceMappingAdapter.ResourceListViewHolder> {
    private List<TestListForResourceMappingModel.Output> list;
    private TestListEvent testListEvent;

    public interface TestListEvent {
        void onTestSelected(TestListForResourceMappingModel.Output output);

    }

    public TestListForResourceMappingAdapter(List<TestListForResourceMappingModel.Output> list, TestListEvent designationEvent) {
        this.list = list;
        this.testListEvent = designationEvent;
    }

    @NonNull
    @Override
    public ResourceListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.deisgnation_list_item, parent, false);

        return new ResourceListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResourceListViewHolder holder, int position) {
        TestListForResourceMappingModel.Output output = list.get(holder.getAdapterPosition());

        holder.tvRoleName.setText(output.getTestName());
        if (output.getResources() != null) {
            int selResources = 0;
            for (ResourceListForMappingModel.Output resource : output.getResources()) {
                if (resource.isChecked()) {
                    selResources = selResources + 1;
                }
            }

            if (selResources >= 1)
                holder.edtSelResource.setText(String.valueOf(selResources));
            else
                holder.edtSelResource.setError("Select at least 1 resource");

        }

        holder.edtSelResource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testListEvent.onTestSelected(output);
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


    public class ResourceListViewHolder extends RecyclerView.ViewHolder {
        private TextView tvRoleName;
        public MaterialEditText edtSelResource;

        public ResourceListViewHolder(@NonNull View itemView) {
            super(itemView);

            tvRoleName = itemView.findViewById(R.id.tvRoleName);
            edtSelResource = itemView.findViewById(R.id.edtSelResource);
        }
    }
}
