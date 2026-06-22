package com.myhindlab.abkat.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.CampBeficiaryList_Activity;
import com.myhindlab.abkat.activities.CampDetails_Activity;
import com.myhindlab.abkat.activities.NestedCampList_Activity;
import com.myhindlab.abkat.models.CampCalendarModel;
import com.myhindlab.abkat.models.CampNestedListModel;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ConstantData;
import com.myhindlab.abkat.utilities.UserSessionManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NestedCampListAdapter extends RecyclerView.Adapter<NestedCampListAdapter.MyViewHolder> {

    private List<CampNestedListModel> resultArrayList;
    private UserSessionManager session;
    private Context context;
    private String DESGID;
    private int campTypeId = 0;

    NestedListEvent nestedListEvent;

   public interface NestedListEvent{
       void onToggle(CampNestedListModel campNestedListModel,int position);
    }

    public NestedCampListAdapter(Context context, List<CampNestedListModel> resultArrayList,NestedListEvent nestedListEvent) {
        this.context = context;
        this.resultArrayList = resultArrayList;
        this.nestedListEvent=nestedListEvent;

        session = new UserSessionManager(context);
        try {
            JSONArray user_info = new JSONArray(session.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                DESGID = json.getString("DESGID");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.nested_camp_list_adapter, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int pos) {
        final int position = holder.getAbsoluteAdapterPosition();
        final CampNestedListModel campDetails = resultArrayList.get(position);
        try {
            holder.rvCampList.setHasFixedSize(true);
            holder.rvCampList.setLayoutManager(new LinearLayoutManager(context));
            holder.rvCampList.setAdapter(new NestedCampListItemAdapter(context, campDetails.getCampList()));
            holder.tv_districtname.setText(campDetails.getDistrict());

            holder.tv_registered.setText(String.valueOf(campDetails.getRegWorker()));
            holder.tvSrNo.setText(String.valueOf(position + 1));


            holder.llHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                     nestedListEvent.onToggle(campDetails,position);
                    holder.llRV.setVisibility(holder.llRV.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                    if (holder.llRV.getVisibility() == View.VISIBLE) {
                        holder.imvIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_arrow_drop_up_24));
                    } else {
                        holder.imvIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_arrow_drop_down_24));


                    }
                }
            });



        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return resultArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private CardView cv_title;
        private TextView tv_districtname, tv_date, tv_campid, tv_campno, tv_status, tv_location, tv_registered, tvSrNo;
        LinearLayoutCompat llHeader,llRV;
        RecyclerView rvCampList;

        ImageView imvIcon;

        public MyViewHolder(View view) {
            super(view);
            tv_districtname = view.findViewById(R.id.tv_districtname);
            tv_date = view.findViewById(R.id.tv_date);
            tv_campid = view.findViewById(R.id.tv_campid);
            tv_campno = view.findViewById(R.id.tv_campno);
            tv_status = view.findViewById(R.id.tv_status);
            tv_location = view.findViewById(R.id.tv_location);
            tv_registered = view.findViewById(R.id.tv_registered);
            cv_title = view.findViewById(R.id.cv_title);
            llHeader = view.findViewById(R.id.llHeader);
            tvSrNo = view.findViewById(R.id.tvSrNo);
            rvCampList = view.findViewById(R.id.rvCampList);
            imvIcon = view.findViewById(R.id.imvIcon);
            llRV = view.findViewById(R.id.llRV);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
