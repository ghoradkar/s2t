package com.myhindlab.abkat.adapters;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.PostCampReferredNormalPatientCount_Activity;
import com.myhindlab.abkat.models.PostCampListModel;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.UserSessionManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class PostCampAdapter extends RecyclerView.Adapter<PostCampAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<PostCampListModel.OutputBean> campList;
    private String isAdmin = "0", DESGID;

    public PostCampAdapter(Context context, ArrayList<PostCampListModel.OutputBean> campList) {
        this.context = context;
        this.campList = campList;


        try {
            JSONArray user_info = new JSONArray(new UserSessionManager(context).getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                DESGID = json.getString("DESGID");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (DESGID.equals("51")) {
            isAdmin = "1";
        }


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_row_post_camp, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        final int position = holder.getAdapterPosition();
        final PostCampListModel.OutputBean campDetails = campList.get(position);

        holder.tv_camp_id.setText(campDetails.getCAMPID());
        holder.tv_camp_name.setText(campDetails.getCampName());
        holder.tv_camp_location.setText(campDetails.getCampLocation());
        holder.tv_camp_date.setText(campDetails.getCampDate());

        holder.cv_title.setOnClickListener(v ->
                context.startActivity(new Intent(context, PostCampReferredNormalPatientCount_Activity.class)
                        .putExtra("campId", campDetails.getCAMPID())
                        .putExtra("isAdmin", isAdmin))
        );
    }

    @Override
    public int getItemCount() {
        return campList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private CardView cv_title;
        private TextView tv_camp_id, tv_camp_name, tv_camp_location, tv_camp_date;

        public MyViewHolder(View view) {
            super(view);
            cv_title = view.findViewById(R.id.cv_title);
            tv_camp_id = view.findViewById(R.id.tv_camp_id);
            tv_camp_name = view.findViewById(R.id.tv_camp_name);
            tv_camp_location = view.findViewById(R.id.tv_camp_location);
            tv_camp_date = view.findViewById(R.id.tv_camp_date);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
