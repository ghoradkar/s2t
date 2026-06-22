package com.myhindlab.abkat.activities.couriermodule;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.couriermodule.CourierReceivedRunnerBoy;
import com.myhindlab.abkat.models.couriermodule.RunnerBoyOnMultipleLabCodeModel;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CourierRunnerBoyTransferList_Activity extends Activity {

    private Context context;
    private UserSessionManager session;
    private ProgressDialog pd;

    private RecyclerView rv_couriers;
    private String userId, courierToBeTransferredId;
    private List<CourierReceivedRunnerBoy.OutputBean> runnerBoyCourierList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier_runner_boy_transfer_list);

        init();
        getSessionDetails();
        setDefaults();
        setEventListener();
        setUpToolBar();
    }

    private void init() {
        context = CourierRunnerBoyTransferList_Activity.this;
        session = new UserSessionManager(context);
        pd = new ProgressDialog(context);

        rv_couriers = findViewById(R.id.rv_couriers);
        rv_couriers.setLayoutManager(new LinearLayoutManager(context));
        runnerBoyCourierList = new ArrayList<>();
    }

    private void getSessionDetails() {
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
    }

    private void setDefaults() {
        getCourierForRunnerBoyApi();
    }

    private void setEventListener() {
    }

    private void getCourierForRunnerBoyApi() {

        if (Utilities.isNetworkAvailable(context)) {
            new GetCourierDetailsByRunner().execute(userId);
        } else {
            Utilities.showMessage(R.string.msgt_nointernetconnection, context);
        }
    }

    private class GetCourierDetailsByRunner extends AsyncTask<String, Integer, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(context);
            dialog.setMessage("please wait...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "[]";
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();

            param.add(new ParamsPojo("UserID", params[0]));
            res = WebServiceCall.HLLAPICall(ApplicationConstants.GetCourierDetailsByRunner, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            dialog.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    CourierReceivedRunnerBoy pojoDetails = new Gson().fromJson(result, CourierReceivedRunnerBoy.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();

                    if (type.equalsIgnoreCase("success")) {
                        runnerBoyCourierList = pojoDetails.getOutput();
                        if (runnerBoyCourierList.size() <= 0) {
                            runnerBoyCourierList = new ArrayList<>();
                        }
                    } else {
                        runnerBoyCourierList = new ArrayList<>();
                    }
                    rv_couriers.setAdapter(new CourierReceivedRunnerBoyAdapter());
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
            }
        }
    }

    private void setUpToolBar() {
        ImageButton back_btn = findViewById(R.id.tool_leftbtn);
        TextView Title = findViewById(R.id.tool_titile);
        back_btn.setBackgroundResource(R.drawable.icon_back);
        Title.setText("Transfer Courier");
        back_btn.setOnClickListener(v -> finish());
    }

    private class CourierReceivedRunnerBoyAdapter extends RecyclerView.Adapter<CourierReceivedRunnerBoyAdapter.MyViewHolder> {
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.list_row_courier_runnerboy_transfer, viewGroup, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int pos) {
            int position = holder.getAdapterPosition();
            CourierReceivedRunnerBoy.OutputBean outputBean = runnerBoyCourierList.get(position);

            holder.tv_from_lab.setText(outputBean.getRegistrationLab());
            holder.tv_process_lab.setText(outputBean.getCourierToLab());
            holder.tv_courier_type.setText(outputBean.getCategoryName());
            holder.tv_sent_date.setText(outputBean.getCourierDate());

            holder.btn_transfer.setOnClickListener(v -> {
                courierToBeTransferredId = outputBean.getCourierID();
                if (Utilities.isNetworkAvailable(context)) {
                    new GetRunnerBoyOnMultipleLabcode().execute(outputBean.getRegistrationLabID(), outputBean.getCourierToLabID());
                } else {
                    Utilities.showMessage(R.string.msgt_nointernetconnection, context);
                }
            });
        }

        @Override
        public int getItemCount() {
            return runnerBoyCourierList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            private TextView tv_from_lab, tv_process_lab, tv_courier_type, tv_sent_date;
            private Button btn_transfer;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                tv_from_lab = itemView.findViewById(R.id.tv_from_lab);
                tv_process_lab = itemView.findViewById(R.id.tv_process_lab);
                tv_courier_type = itemView.findViewById(R.id.tv_courier_type);
                tv_sent_date = itemView.findViewById(R.id.tv_sent_date);
                btn_transfer = itemView.findViewById(R.id.btn_transfer);
            }
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }
    }

    private class GetRunnerBoyOnMultipleLabcode extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Please wait ...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String res = "[]";
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("LabCode", strings[0] + "," + strings[1]));
            res = WebServiceCall.HLLAPICall(ApplicationConstants.GetRunnerBoyOnMultipleLabcode, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    RunnerBoyOnMultipleLabCodeModel pojo = new Gson().fromJson(result, RunnerBoyOnMultipleLabCodeModel.class);
                    type = pojo.getStatus();
                    message = pojo.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        List<RunnerBoyOnMultipleLabCodeModel.OutputBean> runnerBoyList = pojo.getOutput();
                        List<RunnerBoyOnMultipleLabCodeModel.OutputBean> filteredRunnerBoyList = new ArrayList<>();
                        for (RunnerBoyOnMultipleLabCodeModel.OutputBean outputBean : runnerBoyList)
                            if (!outputBean.getUserid().equals(userId))
                                filteredRunnerBoyList.add(outputBean);

                        runnerBoyList.clear();
                        runnerBoyList.addAll(filteredRunnerBoyList);
                        listRunnerBoyListDialog(runnerBoyList);
                    } else {
                        Utilities.showAlertDialog(context, type, message, false);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
            }

        }
    }

    private void listRunnerBoyListDialog(List<RunnerBoyOnMultipleLabCodeModel.OutputBean> runnerBoyList) {
        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        builderSingle.setTitle("Select Runner Boy");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row_city_state);

        for (int i = 0; i < runnerBoyList.size(); i++) {
            arrayAdapter.add(runnerBoyList.get(i).getRunnerBoyName());
        }

        builderSingle.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
            RunnerBoyOnMultipleLabCodeModel.OutputBean objMain = runnerBoyList.get(which);

            if (Utilities.isNetworkAvailable(context)) {
                new InsertCourierTransferToRunnerBoy().execute(
                        courierToBeTransferredId,
                        objMain.getUserid()
                );
            } else {
                Utilities.showMessage(R.string.msgt_nointernetconnection, context);
            }
        });
        builderSingle.show();
    }

    private class InsertCourierTransferToRunnerBoy extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "[]";
            try {
                RequestBody formBody = new FormBody.Builder()
                        .add("CourierID", params[0])
                        .add("RunnerBoyReceiveUserID", params[1])
                        .build();

                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(5, TimeUnit.MINUTES)
                        .writeTimeout(5, TimeUnit.MINUTES)
                        .readTimeout(5, TimeUnit.MINUTES)
                        .build();
                String url = ApplicationConstants.webservice + ApplicationConstants.InsertCourierTransferToRunnerBoy;
                Request request = new Request.Builder()
                        .url(url)
                        .post(formBody)
                        .build();

                Response response = client.newCall(request).execute();
                res = response.body().string();

            } catch (SocketTimeoutException ste) {
                ste.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String status = "", message = "";
            try {
                pd.dismiss();
                if (!result.equals("")) {
                    JSONObject mainObj = new JSONObject(result);
                    status = mainObj.getString("status");
                    message = mainObj.getString("message");
                    if (status.equalsIgnoreCase("Success")) {
                        getCourierForRunnerBoyApi();
                        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                        alertDialog.setTitle("Success");
                        alertDialog.setMessage("Courier transferred successfully!");
                        alertDialog.setIcon((R.drawable.icon_success));
                        alertDialog.setButton("OK", (dialog, which) -> {

                        });
                        alertDialog.show();
                    } else if (status.equalsIgnoreCase("fail")) {
                        Utilities.showAlertDialog(context, status, message, false);
                    }
                }
            } catch (Exception e) {
                Utilities.showAlertDialog(context, "Fail", "Server not connected", false);
                e.printStackTrace();
            }
        }

    }
}