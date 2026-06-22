package com.myhindlab.abkat.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.PatientStatusModel;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CampClosingPendingPatientsAdapter extends RecyclerView.Adapter<CampClosingPendingPatientsAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<PatientStatusModel> patientList;
    private String userId;
    private int month, year;

    public CampClosingPendingPatientsAdapter(Context context, ArrayList<PatientStatusModel> patientList, int month, int year) {
        this.context = context;
        this.patientList = patientList;
        this.month = month;
        this.year = year;

        UserSessionManager session = new UserSessionManager(context);
        try {
            JSONArray user_info = new JSONArray(session.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                userId = json.getString("EmpCode");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_row_patient_pending, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        final int position = holder.getAdapterPosition();
        final PatientStatusModel beneficiaryDetails = patientList.get(position);

        holder.tv_srno.setText((position + 1) + "");
        holder.tv_regno.setText(beneficiaryDetails.getRegdNo());
        holder.tv_name.setText(beneficiaryDetails.getPatientName());

        if ((position % 2) == 0) {
            holder.ll_row.setBackgroundColor(context.getResources().getColor(R.color.white));
        } else {
            holder.ll_row.setBackgroundColor(context.getResources().getColor(R.color.lightGray));
        }

        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utilities.isNetworkAvailable(context))
                    new DeleteRegistrationID().execute(beneficiaryDetails.getCampId(),
                            beneficiaryDetails.getRegdId(),
                            userId,
                            String.valueOf(month + 1),
                            String.valueOf(year),
                            String.valueOf(position));
                else
                    Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
            }
        });
    }

    @Override
    public int getItemCount() {
        return patientList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout ll_row;
        private Button btn_delete;
        private TextView tv_regno, tv_name, tv_srno;

        public MyViewHolder(View view) {
            super(view);
            ll_row = view.findViewById(R.id.ll_row);
            btn_delete = view.findViewById(R.id.btn_delete);
            tv_regno = view.findViewById(R.id.tv_regno);
            tv_name = view.findViewById(R.id.tv_name);
            tv_srno = view.findViewById(R.id.tv_srno);
        }
    }

    private class DeleteRegistrationID extends AsyncTask<String, Void, String> {

        private ProgressDialog pd;
        int position;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(context);
            pd.setMessage("Please wait . . . ");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "";
            position = Integer.parseInt(params[5]);
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("CampId", params[0]));
            param.add(new ParamsPojo("RegdId", params[1]));
            param.add(new ParamsPojo("UserId", params[2]));
            param.add(new ParamsPojo("MonthID", params[3]));
            param.add(new ParamsPojo("Year", params[4]));
            res = WebServiceCall.APICall(ApplicationConstants.DeleteRegistrationID, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            try {
                if (!result.equals("")) {
                    JSONObject jsonObject = new JSONObject(result);
                    String type = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    if (type.equalsIgnoreCase("success")) {
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("CampClosing_Activity"));

                        Utilities.showToastMessage("Patient deleted successfully", context, true);
                        patientList.remove(position);
                        notifyDataSetChanged();

                    } else {
                        Utilities.showToastMessage(message, context, false);
                    }
                } else {
                    Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Alert", e.getMessage(), false);
            }
        }
    }
}