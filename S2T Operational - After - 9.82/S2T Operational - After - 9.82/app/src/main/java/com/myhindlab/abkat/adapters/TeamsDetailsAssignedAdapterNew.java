package com.myhindlab.abkat.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.TeamsDetailsModel;
import com.myhindlab.abkat.models.TeamsDetailsModelNew;

import java.util.ArrayList;

public class TeamsDetailsAssignedAdapterNew extends RecyclerView.Adapter<TeamsDetailsAssignedAdapterNew.MyViewHolder> {

    private final ArrayList<TeamsDetailsModelNew.OutputBean> teamList;

    public TeamsDetailsAssignedAdapterNew(ArrayList<TeamsDetailsModelNew.OutputBean> teamList) {
        this.teamList = teamList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_teamsassign_remove_, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        TeamsDetailsModelNew.OutputBean data = teamList.get(position);

        holder.tvTeamId.setText("( Team Number :- " + data.getTeamNumber() + ")" );
        holder.tvMemberOne.setText(data.getMember1());
        holder.tvMemberTwo.setText(data.getMember2());
//
//        holder.cb_checked.setText(data.getTeamNumber());
//        holder.cb_checked.setChecked(data.isChecked());
//        holder.cb_checked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
//                data.setChecked(checked);
//            }
//        });

//        holder.cb_checked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                data.setChecked(b);
//            }
//        });


        // holder.btnRemove.setText(data.getMember2());
    }

    @Override
    public int getItemCount() {
        return teamList!=null?teamList.size():0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTeamId, tvMemberOne, tvMemberTwo;
        public ImageButton btnRemove;


        public MyViewHolder(final View view) {
            super(view);
            tvMemberOne = view.findViewById(R.id.tvMemberOne);
            tvTeamId = view.findViewById(R.id.tvTeamId);
            tvMemberTwo = view.findViewById(R.id.tvMemberTwo);
            btnRemove = view.findViewById(R.id.btnRemove);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}