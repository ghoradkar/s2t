package com.myhindlab.abkat.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.HomeLabHubLabModel;
import com.myhindlab.abkat.pojos.Lab_OutPut_Pojo;

import java.util.ArrayList;

public class HomeLabAdapter extends RecyclerView.Adapter<HomeLabAdapter.MyViewHolder> {

    private final ArrayList<HomeLabHubLabModel.Output> homelabList;

    public HomeLabAdapter(ArrayList<HomeLabHubLabModel.Output> homelabList) {
        this.homelabList = homelabList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_lab, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        HomeLabHubLabModel.Output data = homelabList.get(position);

        holder.tv_labName.setText(data.getLabName());


    }

    @Override
    public int getItemCount() {
        return homelabList!=null?homelabList.size():0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_labName;
      //  public ImageButton btn_remove;


        public MyViewHolder(final View view) {
            super(view);
            tv_labName = view.findViewById(R.id.tv_labName);
           // btn_remove = view.findViewById(R.id.btn_remove);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}