package com.myhindlab.abkat.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.CampListForMapping_Model;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CampMapping_Adapter extends RecyclerView.Adapter<CampMapping_Adapter.MyViewHolder> {

    private UserSessionManager session;
    private String USERID, DocTypeId, DISTLGDCODE;
    private List<CampListForMapping_Model> resultArrayList;
    private Context context;

    public CampMapping_Adapter(Context context, List<CampListForMapping_Model> resultArrayList) {
        this.context = context;
        this.resultArrayList = resultArrayList;

        session = new UserSessionManager(context);

        try {
            JSONArray user_info = new JSONArray(session.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                USERID = json.getString("EmpCode");
                DocTypeId = json.getString("DocTypeId");
                DISTLGDCODE = json.getString("DISTLGDCODE");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_row_campmapping, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int pos) {
        final int position = holder.getAdapterPosition();
        final CampListForMapping_Model campDetails = resultArrayList.get(position);

        holder.tv_campid.setText(campDetails.getCampId());
        holder.tv_district.setText("District - " + campDetails.getDISTNAME());
        holder.tv_lab.setText(campDetails.getLabName());
        holder.tv_facility.setText(campDetails.getFacilityName());
        holder.tv_startdate.setText(campDetails.getCampStartDate());
        holder.tv_enddate.setText(campDetails.getCampEndDate());


        holder.tv_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Alert");
                builder.setCancelable(false);
                builder.setMessage("Are you sure you want to map this camp ?");
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (Utilities.isNetworkAvailable(context)) {
                            new MappCWCampWithDoctor().execute(
                                    campDetails.getCampRequestId(),
                                    USERID,
                                    DocTypeId,
                                    DISTLGDCODE,
                                    USERID);
                        } else {
                            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                        }
                    }
                });
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();

            }
        });


    }

    @Override
    public int getItemCount() {
        return resultArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_campid, tv_district, tv_lab, tv_facility, tv_startdate, tv_enddate, tv_map;

        public MyViewHolder(View view) {
            super(view);
            tv_campid = view.findViewById(R.id.tv_campid);
            tv_district = view.findViewById(R.id.tv_district);
            tv_lab = view.findViewById(R.id.tv_lab);
            tv_facility = view.findViewById(R.id.tv_facility);
            tv_startdate = view.findViewById(R.id.tv_startdate);
            tv_enddate = view.findViewById(R.id.tv_enddate);
            tv_map = view.findViewById(R.id.tv_map);
        }
    }

    public class MappCWCampWithDoctor extends AsyncTask<String, Void, String> {
        private ProgressDialog pd;

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
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("CampRequestId", params[0]));
            param.add(new ParamsPojo("DocUSERID", params[1]));
            param.add(new ParamsPojo("DocTypeId", params[2]));
            param.add(new ParamsPojo("DISTLGDCODE", params[3]));
            param.add(new ParamsPojo("CreatedBy", params[4]));
            res = WebServiceCall.APICall(ApplicationConstants.MappCWCampWithDoctor, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                pd.dismiss();

                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
                    JSONObject obj = new JSONObject(result);
                    String status = obj.getString("status");
                    String message = obj.getString("message");
                    if (status.equalsIgnoreCase("Success")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setIcon(R.drawable.icon_success);
                        builder.setTitle("Success");
                        builder.setCancelable(false);
                        builder.setMessage(message);
                        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        builder.show();

                    } else {
                        Utilities.showAlertDialog(context, status, message, false);
                    }
                } else
                    Utilities.showAlertDialog(context, "Please try again", "Server not responding.", false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
