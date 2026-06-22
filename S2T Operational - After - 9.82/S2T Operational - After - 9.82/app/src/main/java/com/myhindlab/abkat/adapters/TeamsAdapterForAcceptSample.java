package com.myhindlab.abkat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.TeamsDetailsModel;

import java.util.ArrayList;

public class TeamsAdapterForAcceptSample extends RecyclerView.Adapter<TeamsAdapterForAcceptSample.MyViewHolder> {

    private final ArrayList<TeamsDetailsModel.OutputBean> teamList;
    private Context context;
    public TeamEvents teamEvents;

    public interface TeamEvents {
        void onTeamSelected(TeamsDetailsModel.OutputBean outputBean);
    }

    public TeamsAdapterForAcceptSample(ArrayList<TeamsDetailsModel.OutputBean> teamList) {
        this.teamList = teamList;
        this.teamEvents = teamEvents;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_teams_assign_, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        TeamsDetailsModel.OutputBean data = teamList.get(holder.getAbsoluteAdapterPosition());
        holder.tvTeamId.setText(data.getTeamNumber());
        holder.tvMemberOne.setText(data.getMember1());
        holder.tvMemberTwo.setText(data.getMember2());
//        holder.cbAssign.setChecked(data.isChecked());
        holder.btnAssign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data.setChecked(true);
                teamEvents.onTeamSelected(data);
            }
        });


//        holder.cbAssign.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                for (int i = 0; i < teamList.size(); i++) {
//                    if (teamList.get(i).isChecked() && holder.cbAssign.isChecked()) {
//                        if (teamList.get(i).getMemberUserID1().equalsIgnoreCase(data.getMemberUserID1()) || teamList.get(i).getMemberUserID2().equalsIgnoreCase(data.getMemberUserID2())) {
//                            data.setChecked(false);
//                            holder.cbAssign.setChecked(false);
////                            Utilities.showToastMessage("User already selected", context, false);
//                            Utilities.showAlertDialog(context, "Warning", "One of the user in this team already available in previously selected team", false);
//                            notifyItemChanged(holder.getAbsoluteAdapterPosition());
//                            return;
//                        } else {
//                            data.setChecked(!data.isChecked());
//                            holder.cbAssign.setChecked(!holder.cbAssign.isChecked());
//                            notifyItemChanged(holder.getAbsoluteAdapterPosition());
//
//                        }
//
//                    } else {
//                        data.setChecked(!data.isChecked());
//                        holder.cbAssign.setChecked(!holder.cbAssign.isChecked());
//                        notifyItemChanged(holder.getAbsoluteAdapterPosition());
//                    }
//
//                }
//
//            }
//        });

//        holder.cbAssign.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
//                data.setChecked(checked);
//                notifyItemChanged(holder.getAbsoluteAdapterPosition());
//            }
//        });


    }

    @Override
    public int getItemCount() {
        return teamList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTeamId, tvMemberOne, tvMemberTwo;
        public CheckBox cbAssign;
        public Button btnAssign;


        public MyViewHolder(final View view) {
            super(view);
            tvMemberOne = view.findViewById(R.id.tvMemberOne);
            tvTeamId = view.findViewById(R.id.tvTeamId);
            tvMemberTwo = view.findViewById(R.id.tvMemberTwo);
            btnAssign = view.findViewById(R.id.btnAssign);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}