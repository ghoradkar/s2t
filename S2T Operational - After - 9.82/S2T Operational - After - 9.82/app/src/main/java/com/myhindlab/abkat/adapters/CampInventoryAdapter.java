package com.myhindlab.abkat.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.CampInventoryListModel;

import java.util.List;

public class CampInventoryAdapter extends RecyclerView.Adapter<CampInventoryAdapter.MyViewHolder> {

    private List<CampInventoryListModel.OutputBean> resultArrayList;
    private Context context;

    public CampInventoryAdapter(Context context, List<CampInventoryListModel.OutputBean> resultArrayList) {
        this.context = context;
        this.resultArrayList = resultArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_row_campinventory, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int pos) {
        final int position = holder.getAdapterPosition();
        final CampInventoryListModel.OutputBean campDetails = resultArrayList.get(position);
        holder.tv_inventory.setText(campDetails.getInventory());
        holder.tv_quantity.setText(campDetails.getQuantityRequired());

        if ((position % 2) == 0) {
            holder.ll_row.setBackgroundColor(context.getResources().getColor(R.color.white));
        } else {
            holder.ll_row.setBackgroundColor(context.getResources().getColor(R.color.lightGray));
        }
    }

    @Override
    public int getItemCount() {
        return resultArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout ll_row;
        private TextView tv_inventory, tv_quantity;

        public MyViewHolder(View view) {
            super(view);
            ll_row = view.findViewById(R.id.ll_row);
            tv_inventory = view.findViewById(R.id.tv_inventory);
            tv_quantity = view.findViewById(R.id.tv_quantity);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
