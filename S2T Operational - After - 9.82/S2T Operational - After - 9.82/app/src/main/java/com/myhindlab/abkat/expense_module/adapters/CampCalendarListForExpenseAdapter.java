package com.myhindlab.abkat.expense_module.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.CampCalendarModel;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ConstantData;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.expense_module.activities.AdvanceRequestActivityV1;
import com.myhindlab.abkat.expense_module.activities.SeeRequestedAdvanceActivity;
import com.myhindlab.abkat.utilities.Utilities;


import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CampCalendarListForExpenseAdapter extends RecyclerView.Adapter<CampCalendarListForExpenseAdapter.MyViewHolder> {

    private List<CampCalendarModel.OutputBean> resultArrayList;
    private UserSessionManager session;
    private Context context;
    private String DESGID;
    private int campTypeId;

    public CampCalendarListForExpenseAdapter(Context context, List<CampCalendarModel.OutputBean> resultArrayList, int campTypeId) {
        this.context = context;
        this.resultArrayList = resultArrayList;
        this.campTypeId = campTypeId;

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
        View view = inflater.inflate(R.layout.camp_list_item_v1_new, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int pos) {
        final int position = holder.getAbsoluteAdapterPosition();
        final CampCalendarModel.OutputBean campDetails = resultArrayList.get(position);
        final boolean[] isVisible = {false};
        try {


            holder.tv_districtname.setText(campDetails.getDISTNAME());
            holder.tv_date.setText(campDetails.getCampDate());
            holder.tv_campid.setText(campDetails.getCampId());
            holder.tv_campno.setText(campDetails.getCampName());
            holder.tv_location.setText(campDetails.getCampLocation());
            holder.tv_registered.setText(campDetails.getREGISTERWORKERS() + "/" + campDetails.getExpectedbeneficiarycount());
            holder.tvExpectedBeneficiary.setText(campDetails.getExpectedbeneficiarycount());
            holder.tvCampType.setText(campDetails.getCampTypeDescription().equalsIgnoreCase("regular") ? "R" : "D");
            holder.tvSrNo.setText(String.valueOf(position + 1));
            holder.tvApprovedDate.setText(campDetails.getApprovedDate());
            holder.tvAdvanceStatus.setText(campDetails.getAdvanceFundStatus());
            holder.tvCurrentApprovalLevel.setText(campDetails.getCurrentApprovalLevel());
            holder.tvApprovedAmount.setText(campDetails.getApprovedAmt());
            holder.tvDemandAdvAmount.setText(campDetails.getDemandAdvAmount());

            if (campDetails.getDemandAdvAmount() != null) {
                holder.btnSeeRequestedAdv.setVisibility(View.VISIBLE);
                holder.btnSeeRequestedAdv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        context.startActivity(new Intent(context, SeeRequestedAdvanceActivity.class).putExtra("campId", campDetails.getCampId()).putExtra("screenType", 1));
                    }
                });

            } else {
                holder.btnSeeRequestedAdv.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        holder.viewToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isVisible[0] = !isVisible[0];

                if (isVisible[0]) {
                    holder.llContent.setVisibility(View.VISIBLE);
                    holder.viewToggle.setImageResource(R.drawable.ic_baseline_arrow_drop_up_24);
                } else {
                    holder.llContent.setVisibility(View.GONE);
                    holder.viewToggle.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24);

                }
            }
        });

        holder.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConstantData constantData = ConstantData.getInstance();
                constantData.setCalendarCampId(campDetails.getCampId());


                if (campDetails.getAdvanceFundStatus() != null && campDetails.getAdvanceFundStatus().equalsIgnoreCase("Approved")) {
                    Utilities.showAlertDialog(context, "Warning", "This camp's advance request already Approved", false);
                } else {
                    context.startActivity(new Intent(context, AdvanceRequestActivityV1.class)
                            .putExtra("Date", campDetails.getCampDate())
                            .putExtra("DISTLGDCODE", campDetails.getDISTLGDCODE())
                            .putExtra("campDetails", campDetails)
                            .putExtra("campTypeId", campTypeId)
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


    }

    @Override
    public int getItemCount() {
        return resultArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private CardView cv_title;
        private TextView tvDemandAdvAmount, tvCurrentApprovalLevel, tvApprovedAmount, tvApprovedDate, tvAdvanceStatus, tvCampType, tvExpectedBeneficiary, tv_districtname, tv_date, tv_campid, tv_campno, tv_status, tv_location, tv_registered, tvSrNo;
        LinearLayoutCompat llMain, llContent, ll_row;
        ImageView viewToggle;
        Button btnSeeRequestedAdv;

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
            llContent = view.findViewById(R.id.llContent);
            tvExpectedBeneficiary = view.findViewById(R.id.tvExpectedBeneficiary);
            tvCampType = view.findViewById(R.id.tvCampType);
            viewToggle = view.findViewById(R.id.viewToggle);
            ll_row = view.findViewById(R.id.ll_row);
            tvSrNo = view.findViewById(R.id.tvSrNo);
            tvAdvanceStatus = view.findViewById(R.id.tvAdvanceStatus);
            tvApprovedDate = view.findViewById(R.id.tvApprovedDate);
            tvApprovedAmount = view.findViewById(R.id.tvApprovedAmount);
            tvCurrentApprovalLevel = view.findViewById(R.id.tvCurrentApprovalLevel);
            tvDemandAdvAmount = view.findViewById(R.id.tvDemandAdvAmount);
            btnSeeRequestedAdv = view.findViewById(R.id.btnSeeRequestedAdv);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
