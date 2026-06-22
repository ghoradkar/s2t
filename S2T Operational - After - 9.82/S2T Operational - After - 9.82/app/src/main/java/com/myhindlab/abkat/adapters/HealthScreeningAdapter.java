package com.myhindlab.abkat.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.HealthScreeningListModel;

import java.util.ArrayList;

public class HealthScreeningAdapter extends RecyclerView.Adapter<HealthScreeningAdapter.ViewHolder> {

    private ArrayList<HealthScreeningListModel> toolMenus;
    private Context context;

    public HealthScreeningAdapter(Context context, ArrayList<HealthScreeningListModel> toolMenus) {
        this.context = context;
        this.toolMenus = toolMenus;
    }

    @Override
    public void onBindViewHolder(final HealthScreeningAdapter.ViewHolder holder, final int position) {
        HealthScreeningListModel menu = toolMenus.get(position);

        if (menu.getMenuIcon() != 0) {
            holder.imv_image.setImageResource(menu.getMenuIcon());
        }
        holder.tv_menu.setText(menu.getMenuName());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_row_healthscreening, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        if (toolMenus == null)
            return 0;
        return toolMenus.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imv_image;
        private TextView tv_menu;

        public ViewHolder(View view) {
            super(view);
            imv_image = view.findViewById(R.id.imv_image);
            tv_menu = view.findViewById(R.id.tv_menu);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
