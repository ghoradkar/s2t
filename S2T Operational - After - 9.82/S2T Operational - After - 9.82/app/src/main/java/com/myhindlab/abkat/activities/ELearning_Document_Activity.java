package com.myhindlab.abkat.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ELearning_Document_Activity extends AppCompatActivity {

    private ProgressDialog dialog;
    private UserSessionManager session;
    Context context;
    private Context appcontext;
    private String EmpCode, DESGID = "";
    private String method = "GetMenuDashboard";
    private ListView list;
    int ttype, desg_id, service_type_id;
    String trainingtype, designationid, servicetypeid;
//    private String URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elearning__document_);

        Bundle extras = getIntent().getExtras();
        String stringVariableName = extras.getString("TrainingType");
        ttype = Integer.parseInt(stringVariableName);

        String stringVariableName2 = extras.getString("DESGID");
        desg_id = Integer.parseInt(stringVariableName2);

        String stringVariableName3 = extras.getString("ServiceTypeID");
        service_type_id = Integer.parseInt(stringVariableName3);

        init();
        setDefaults();
        setUpToolBar();
        new GetListdata().execute();
    }

    private void init() {
        context = ELearning_Document_Activity.this;
        session = new UserSessionManager(context);
        dialog = new ProgressDialog(context);
        list = (ListView) findViewById(R.id.list);
    }

    private void setDefaults() {
        try {
            JSONArray user_info = new JSONArray(session.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                EmpCode = json.getString("EmpCode");
                DESGID = json.getString("DESGID").trim();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setUpToolBar() {

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("ELearning Manual");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private class GetListdata extends AsyncTask<String, String, String> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ELearning_Document_Activity.this);
            pDialog.setMessage("Getting Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "[]";
            String ttp = String.valueOf(ttype);
            String did = String.valueOf(desg_id);
            String sti = String.valueOf(service_type_id);

            try {
                res = WebServiceCall.SelectVidieoLinkData(ttp, did, sti);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            pDialog.dismiss();

            if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
                try {
                    JSONObject obj1 = new JSONObject(result);
                    String status = obj1.getString("status");
                    String message = obj1.getString("message");
                    if (status.equalsIgnoreCase("Success")) {
                        ArrayList<ElearningDocumentList> emu = new ArrayList<ElearningDocumentList>();
                        //ElearningDocumentList menulist = new ElearningDocumentList();
                        final JSONArray jsonarr = obj1.getJSONArray("output");
                        if (jsonarr.length() > 0) {
                            for (int i = 0; i < jsonarr.length(); i++) {

                                JSONObject json = jsonarr.getJSONObject(i);
//                                int MaterialtrainingId = json.getInt("MaterialTrainingId");
//                               // Log.d("Menu Id Is", String.valueOf(MaterialtrainingId));
//                                String MaterialTrainingName = json.getString("MaterialTrainingName");
//                               // Log.d("Menu Name Is", MaterialTrainingName);
//                                String TitleInEnglish=json.getString("TitleInEnglish");
//                              //  Log.d("TitleInEnglish", TitleInEnglish);
//                                String UrlPath=json.getString("UrlPath");
//                               // Log.d("You Tube UrlPath", UrlPath);
//                                String rateFrmUser=json.getString("rateFrmUser");
                                //  Log.d("Rating from User", rateFrmUser);
                              /* menulist.setMaterialTrainingName(json.getString("MaterialTrainingName"));
                               menulist.setUrlPath(json.getString("UrlPath"));

                                emu.add(menulist);*/
                                ElearningDocumentList menulist = new ElearningDocumentList();
                                menulist.setMaterialTrainingName(json.getString("MaterialTrainingName"));
                                menulist.setUrlPath(json.getString("UrlPath"));
//                                URL = json.getString("UrlPath");
                                emu.add(menulist);

                               /* ELearning.ElearningMenuList menulist = new ELearning.ElearningMenuList();
                                menulist.setMenuName(json.getString("MenuName"));

                                emu.add(menulist);*/
                            }
                            list.setAdapter(new ELearningDocumentMenuAdapter(context, emu));
                        } else
                            Utilities.showAlertDialog(context, "Alert",
                                    "Empty responce from service", false);
                    } else {
                        Utilities.showAlertDialog(ELearning_Document_Activity.this, status, message, false);
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                Utilities.showAlertDialog(ELearning_Document_Activity.this,
                        "Alert", "Service not responding.", false);
            }
        }
    }

    public class ElearningDocumentList {

        public String getUrlPath() {
            return UrlPath;
        }

        public void setUrlPath(String urlPath) {
            UrlPath = urlPath;
        }

        private String MaterialTrainingName;

        public String UrlPath;

        public String getMaterialTrainingName() {
            return MaterialTrainingName;
        }

        public void setMaterialTrainingName(String materialTrainingName) {
            MaterialTrainingName = materialTrainingName;
        }
    }

    class ELearningDocumentMenuAdapter extends BaseAdapter {
        private ArrayList<ElearningDocumentList> emu;
        public Context _context;
        private LayoutInflater mInflater;
        ViewHolder holder;

        public ELearningDocumentMenuAdapter(Context context, ArrayList<ElearningDocumentList> results) {
            emu = results;
            _context = context;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return emu.size();
        }

        @Override
        public Object getItem(int arg0) {
            return emu.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            // TODO Auto-generated method stub
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.elearningdocumentlist_item, null);
                holder = new ViewHolder();

                holder.MaterialTrainingName = (TextView) convertView.findViewById(R.id.menuname);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.MaterialTrainingName.setText(emu.get(position).getMaterialTrainingName());

            holder.MaterialTrainingName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(ELearning_Document_Activity.this, Document.class);
                    Bundle extras = new Bundle();
                    extras.putString("URL", emu.get(position).getUrlPath());
                    i.putExtras(extras);
                    startActivity(i);
                }
            });
            return convertView;
        }

        class ViewHolder {
            TextView MaterialTrainingName;
        }
    }
}
