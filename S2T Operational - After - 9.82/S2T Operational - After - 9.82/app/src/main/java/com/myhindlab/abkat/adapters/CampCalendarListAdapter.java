package com.myhindlab.abkat.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.CampBeficiaryList_Activity;
import com.myhindlab.abkat.activities.CampDetails_Activity;
import com.myhindlab.abkat.models.CampCalendarModel;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ConstantData;
import com.myhindlab.abkat.utilities.UserSessionManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CampCalendarListAdapter extends RecyclerView.Adapter<CampCalendarListAdapter.MyViewHolder> {

    private List<CampCalendarModel.OutputBean> resultArrayList;
    private UserSessionManager session;
    private Context context;
    private String DESGID;
    private int campTypeId = 0;

    public CampCalendarListAdapter(Context context, List<CampCalendarModel.OutputBean> resultArrayList) {
        this.context = context;
        this.resultArrayList = resultArrayList;

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

    public CampCalendarListAdapter(Context context, List<CampCalendarModel.OutputBean> resultArrayList, int campTypeId) {
        this.context = context;
        this.resultArrayList = resultArrayList;
        this.campTypeId = campTypeId;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.camp_list_item_v1, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int pos) {
        final int position = holder.getAdapterPosition();
        final CampCalendarModel.OutputBean campDetails = resultArrayList.get(position);
        holder.tv_districtname.setText(campDetails.getDISTNAME());
        holder.tv_date.setText(campDetails.getCampDate());
        holder.tv_campid.setText(campDetails.getCampId());
//        holder.tv_campno.setText(campDetails.getCampId());
//        holder.tv_status.setText(campDetails.getDescription());
//        holder.tv_location.setText(campDetails.getCampLocation());
        holder.tv_registered.setText(campDetails.getREGISTERWORKERS());
        holder.tvSrNo.setText(String.valueOf(position + 1));

        holder.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConstantData constantData = ConstantData.getInstance();
                constantData.setCalendarCampId(campDetails.getCampId());

                if (DESGID.equals("71")){
                    return;
                }

                if (DESGID.equals("63") || DESGID.equals("24") ) {
                    context.startActivity(new Intent(context, CampBeficiaryList_Activity.class));
                } else {
                    context.startActivity(new Intent(context, CampDetails_Activity.class)
                            .putExtra("Date", campDetails.getCampDate())
                            .putExtra("DISTLGDCODE", campDetails.getDISTLGDCODE())
                            .putExtra("campDetails", campDetails)
                            .putExtra("Type", "1"));
                }
            }
        });

        Date campDate = null;
        Date currentDate = new Date();
        try {
            campDate = new SimpleDateFormat("yyyy-MM-dd").parse(campDetails.getCampDate());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            currentDate = sdf.parse(sdf.format(currentDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        if (campDetails.getCampStatus() != null) {
            if (campDetails.getCampStatus().equalsIgnoreCase("Camp Open")) {

                if (campDate.before(currentDate)) {
                    holder.llMain.setBackgroundColor(context.getResources().getColor(R.color.red1));
                } else if (campDate.equals(currentDate)) {
                    holder.llMain.setBackgroundColor(context.getResources().getColor(R.color.green1));
                } else {
                    if (position % 2 == 1) {
                        holder.llMain.setBackgroundColor(context.getResources().getColor(R.color.devider));
                    } else {
                        holder.llMain.setBackgroundColor(context.getResources().getColor(R.color.white));

                    }
                }

            } else if (position % 2 == 1) {
                holder.llMain.setBackgroundColor(context.getResources().getColor(R.color.devider));
            } else {
                holder.llMain.setBackgroundColor(context.getResources().getColor(R.color.white));
            }
        } else if (position % 2 == 1) {
            holder.llMain.setBackgroundColor(context.getResources().getColor(R.color.devider));
        } else {
            holder.llMain.setBackgroundColor(context.getResources().getColor(R.color.white));

        }

    }

    @Override
    public int getItemCount() {
        return resultArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private CardView cv_title;
        private TextView tv_districtname, tv_date, tv_campid, tv_campno, tv_status, tv_location, tv_registered, tvSrNo;
        LinearLayoutCompat llMain;

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
            llMain = view.findViewById(R.id.llMain);
            tvSrNo = view.findViewById(R.id.tvSrNo);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
