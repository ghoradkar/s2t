package com.myhindlab.abkat.adapters;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.pojos.MenuListPojo;

import java.util.ArrayList;

public class MenuListAdapter extends RecyclerView.Adapter<MenuListAdapter.ViewHolder> {

    private ArrayList<MenuListPojo> toolMenus;

    public MenuListAdapter(ArrayList<MenuListPojo> toolMenus) {
        this.toolMenus = toolMenus;
    }

    @Override
    public void onBindViewHolder(final MenuListAdapter.ViewHolder holder, final int position) {
        MenuListPojo menu = new MenuListPojo();
        menu = toolMenus.get(position);

        if (menu.getMenuIcon() != 0) {
            holder.iconMenu.setImageResource(menu.getMenuIcon());
        }
        holder.menuName.setText(menu.getMenuName());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_row_homemenu, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        if (toolMenus == null)
            return 0;
        return toolMenus.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView iconMenu;
        private TextView menuName;

        public ViewHolder(View view) {
            super(view);
            iconMenu = view.findViewById(R.id.img_toolmenu);
            menuName = view.findViewById(R.id.tv_toolmenu);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
