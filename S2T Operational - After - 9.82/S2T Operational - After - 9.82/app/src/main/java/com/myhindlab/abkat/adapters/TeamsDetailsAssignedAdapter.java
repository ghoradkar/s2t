package com.myhindlab.abkat.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.TeamsDetailsModel;

import java.util.ArrayList;

public class TeamsDetailsAssignedAdapter extends RecyclerView.Adapter<TeamsDetailsAssignedAdapter.MyViewHolder> {

    private final ArrayList<TeamsDetailsModel.OutputBean> teamList;

    public TeamsDetailsAssignedAdapter(ArrayList<TeamsDetailsModel.OutputBean> teamList) {
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
        TeamsDetailsModel.OutputBean data = teamList.get(position);
        holder.tvTeamId.setText("( Team Number :- " + data.getTeamNumber() + ")" );
        holder.tvMemberOne.setText(data.getMember1());
        holder.tvMemberTwo.setText(data.getMember2());
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