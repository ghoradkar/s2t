package com.myhindlab.abkat.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.CampInventoryListModel;
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

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class InventoryItemConsumption_Activity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private UserSessionManager sessionManager;
    private RecyclerView recyclerview_list;
    SwipeRefreshLayout swipeRefreshLayout;
    ProgressDialog pd;
    private String userID, name, campId;
    private Button btn_submit;
    private ArrayList<CampInventoryListModel.OutputBean> campInventoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_item_consumption);

        init();
        setToolBar();
        getDataFromSession();
        setDefault();
        setEventHandlers();
    }

    private void init() {
        context = InventoryItemConsumption_Activity.this;
        sessionManager = new UserSessionManager(context);
        pd = new ProgressDialog(context);
        btn_submit = findViewById(R.id.btn_submit);
        recyclerview_list = findViewById(R.id.recyclerview_list);
        recyclerview_list.setLayoutManager(new LinearLayoutManager(context));
        //  swipeRefreshLayout=findViewById(R.id.swipeRefreshLayout);
        campInventoryList = new ArrayList<>();

    }

    private void setToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Item Consumption");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getDataFromSession() {
        try {
            JSONArray user_info = new JSONArray(sessionManager.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                userID = json.getString("EmpCode");
                name = json.getString("name");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDefault() {
        campId = getIntent().getStringExtra("campId");
        if (Utilities.isNetworkAvailable(context)) {
            new GetuseInventoryDetailsOncampId().execute(campId);
            //swipeRefreshLayout.setRefreshing(false);
        } else {
            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
        }
    }

    private void setEventHandlers() {
        btn_submit.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                if (Utilities.isNetworkAvailable(context)) {
                    CreateJSON(campInventoryList);
                } else {
                    Utilities.showToastMessage("Please Check Your Internet connection", context, false);
                    return;
                }
                break;

//            case R.id.swipeRefreshLayout:
//                if (Utilities.isNetworkAvailable(context)) {
//                    new GetInventoryDetailsOncampId().execute(campId);
//                    swipeRefreshLayout.setRefreshing(false);
//                } else {
//                    Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
//                }
//
//            break;
        }
    }

    private void CreateJSON(ArrayList<CampInventoryListModel.OutputBean> campInventoryList) {
        Log.e("", "CreateJSON: " + campInventoryList);
        Gson gson = new Gson();
        String jarr = gson.toJson(campInventoryList);
        Log.d("Json IS->", jarr);

        new UpdateInventoryDetailsOncampId().execute(jarr, userID);

    }

    private class GetuseInventoryDetailsOncampId extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("CampId", params[0]));

            res = WebServiceCall.APICall(ApplicationConstants.GetuseInventoryDetailsOncampId, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            try {
                if (!result.equals("")) {

                    CampInventoryListModel pojoDetails = new Gson().fromJson(result, CampInventoryListModel.class);
                    String status = pojoDetails.getStatus();
                    String message = pojoDetails.getMessage();

                    if (status.equalsIgnoreCase("success")) {
                        campInventoryList = pojoDetails.getOutput();
                        if (campInventoryList.size() > 0) {
                            recyclerview_list.setAdapter(new CampInventoryIemConsuptionAdapter(context, campInventoryList));
                        } else {

                        }
                    } else {

                    }
                } else {

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // UpdateInventoryDetailsOncampId

    private class UpdateInventoryDetailsOncampId extends AsyncTask<String, Void, String> {

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
//            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
//            param.add(new ParamsPojo("jsonstring", params[0]));
//            param.add(new ParamsPojo("Userid", params[1]));
//
//            res = WebServiceCall.APICall(ApplicationConstants.UpdateInventoryDetailsOncampId, param);
//            return res;

            try {

                RequestBody formBody = new FormBody.Builder()
                        .add("jsonstring", params[0])
                        .add("Userid", params[1])
                        .build();

                OkHttpClient client = new OkHttpClient();
                String url = ApplicationConstants.webservice + ApplicationConstants.UpdateInventoryDetailsOncampId;
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
            pd.dismiss();
            try {
                if (!result.equals("")) {

                    JSONObject obj1 = new JSONObject(result);
                    String status = obj1.getString("status");
                    String message = obj1.getString("message");

                    if (status.equalsIgnoreCase("success")) {
                        Utilities.showAlertDialog(context, "Success", message, true);
                        if (Utilities.isNetworkAvailable(context)) {
                            new GetuseInventoryDetailsOncampId().execute(campId);
                        } else {
                            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                        }
                    } else {
                        Utilities.showAlertDialog(context, status, message, false);
                        return;
                    }
                } else {

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //------------------------------ Adapter Code  ---------------------------------//

    public class CampInventoryIemConsuptionAdapter extends RecyclerView.Adapter<CampInventoryIemConsuptionAdapter.MyViewHolder> {

        private List<CampInventoryListModel.OutputBean> resultArrayList;
        private Context context;

        public CampInventoryIemConsuptionAdapter(Context context, List<CampInventoryListModel.OutputBean> resultArrayList) {
            this.context = context;
            this.resultArrayList = resultArrayList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.list_row_inventoryconsumption, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int pos) {
            final int position = holder.getAdapterPosition();
            final CampInventoryListModel.OutputBean campDetails = resultArrayList.get(position);
            holder.tv_inventory.setText(campDetails.getInventory());
            holder.tv_quantity.setText(campDetails.getQuantityRequired());
            holder.et_consumedquantity.setText(campDetails.getUseQuantity());

            holder.et_consumedquantity.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    resultArrayList.get(position).setUseQuantity(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            if ((position % 2) == 0) {
                holder.ll_row.setBackgroundColor(context.getResources().getColor(R.color.white));
            } else {
                holder.ll_row.setBackgroundColor(context.getResources().getColor(R.color.lightGray));
            }
        }

        @Override
        public int getItemCount() {
            return resultArrayList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            private LinearLayout ll_row;
            private TextView tv_inventory, tv_quantity;
            private EditText et_consumedquantity;

            public MyViewHolder(View view) {
                super(view);
                ll_row = view.findViewById(R.id.ll_row);
                tv_inventory = view.findViewById(R.id.tv_inventory);
                tv_quantity = view.findViewById(R.id.tv_quantity);
                et_consumedquantity = view.findViewById(R.id.et_consumedquantity);
            }
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }
    }
}
