package com.myhindlab.abkat.adapters;

import android.app.ProgressDialog;
import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.pojos.SiteSurveyRequestList_OutPut_Pojo;

import java.util.List;

public class CampRequestListAdapter extends RecyclerView.Adapter<CampRequestListAdapter.MyViewHolder> {
    private List<SiteSurveyRequestList_OutPut_Pojo> resultArrayList;
    private Context context;
    private ProgressDialog pd;
    private String userId;

    public CampRequestListAdapter(Context context, List<SiteSurveyRequestList_OutPut_Pojo> resultArrayList, String userId) {
        this.context = context;
        this.resultArrayList = resultArrayList;
        this.userId = userId;
        pd = new ProgressDialog(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_row_c_list, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int pos) {
        final int position = holder.getAdapterPosition();
        SiteSurveyRequestList_OutPut_Pojo desigDetail = resultArrayList.get(position);

        holder.tv_siteid.setText(desigDetail.getSiteDetailId());
        holder.tv_survet_date.setText(desigDetail.getSITESURVEYDATE());
        holder.tv_status.setText(desigDetail.getFinalStatus());
        holder.tv_sitelocation.setText(desigDetail.getSITELOCATION());
        holder.tv_sitename.setText(desigDetail.getSiteName());
        holder.tv_sitetype.setText(desigDetail.getSiteTypeName());
        holder.tv_regdworker.setText(desigDetail.getREGISTERWORKERS());
        holder.tv_unregdworker.setText(desigDetail.getUNREGISTEREDWORKERS());
        holder.tv_pin_code.setText(desigDetail.getPinCode());
        holder.tv_surveyor_name.setText(desigDetail.getSURVEYORNAME());

//        if (Integer.parseInt(desigDetail.getREGISTERWORKERS()) + Integer.parseInt(desigDetail.getUNREGISTEREDWORKERS()) >= 200) {
//            holder.ll_row.setBackgroundColor(context.getResources().getColor(R.color.light_green));
//        } else if (Integer.parseInt(desigDetail.getREGISTERWORKERS()) + Integer.parseInt(desigDetail.getUNREGISTEREDWORKERS()) < 200 &&
//                Integer.parseInt(desigDetail.getREGISTERWORKERS()) + Integer.parseInt(desigDetail.getUNREGISTEREDWORKERS()) > 100) {
//            holder.ll_row.setBackgroundColor(context.getResources().getColor(R.color.dashb3));
//        } else {
//            holder.ll_row.setBackgroundColor(context.getResources().getColor(R.color.white));
//        }

        holder.cb_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return resultArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_status, tv_date, tv_siteid, tv_surveyor_name, tv_sitelocation, tv_sitename, tv_sitetype, tv_regdworker, tv_unregdworker, tv_pin_code,
                tv_survet_date;
        private Button btn_approve, btn_reject;
        private CheckBox cb_check;
        private LinearLayout ll_row;

        private MyViewHolder(View view) {
            super(view);
            tv_date = view.findViewById(R.id.tv_date);
            tv_siteid = view.findViewById(R.id.tv_siteid);
            tv_sitelocation = view.findViewById(R.id.tv_sitelocation);
            tv_status = view.findViewById(R.id.tv_status);
            tv_sitename = view.findViewById(R.id.tv_sitename);
            tv_sitetype = view.findViewById(R.id.tv_sitetype);
            tv_regdworker = view.findViewById(R.id.tv_regdworker);
            tv_unregdworker = view.findViewById(R.id.tv_unregdworker);
            tv_pin_code = view.findViewById(R.id.tv_pin_code);
            tv_survet_date = view.findViewById(R.id.tv_survet_date);
            tv_surveyor_name = view.findViewById(R.id.tv_surveyor_name);
//            btn_approve = view.findViewById(R.id.btn_approve);
//            btn_reject = view.findViewById(R.id.btn_reject);
            ll_row = view.findViewById(R.id.ll_row);
            cb_check = view.findViewById(R.id.cb_check);

        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


}

