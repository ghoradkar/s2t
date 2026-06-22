package com.myhindlab.abkat.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
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

public class ELearning extends AppCompatActivity {
    private ProgressDialog dialog;
    private UserSessionManager session;
    Context context;
    private Context appcontext;
    private String EmpCode;
    String DESGID = "";
    private String method = "GetMenuDashboard";
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elearning);

        startActivity(new Intent(Intent.ACTION_VIEW,   Uri.parse("https://www.youtube.com/channel/UC_rxJXf5LP6yIxvQoRPUqJg")));
        finish();

//        init();
//        setDefaults();
//        setUpToolBar();
//        new GetListdata().execute();
    }

    private void init() {
        context = ELearning.this;
        session = new UserSessionManager(context);
        dialog = new ProgressDialog(context);
        list = (ListView) findViewById(R.id.list);

        AdapterView.OnItemClickListener myListViewClicked = null;

        myListViewClicked = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(ELearning.this, "Clicked at positon = " + position, Toast.LENGTH_SHORT).show();
            }
        };

        list.setOnItemClickListener(myListViewClicked);
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
        getSupportActionBar().setTitle("ELearning");

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
            pDialog = new ProgressDialog(ELearning.this);
            pDialog.setMessage("Getting Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "[]";
            res = WebServiceCall.GetMenuDashboard();
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

                        ArrayList<ElearningMenuList> emu =
                                new ArrayList<ElearningMenuList>();

                        JSONArray jsonarr = obj1.getJSONArray("output");
                        if (jsonarr.length() > 0) {
                            for (int i = 0; i < jsonarr.length(); i++) {

                                JSONObject json = jsonarr.getJSONObject(i);

                                ElearningMenuList menulist = new ElearningMenuList();
                                menulist.setMenuName(json.getString("MenuName"));

                                emu.add(menulist);
                            }
                            list.setAdapter(new ELearningMenuAdapter(context, emu));

                        } else
                            Utilities.showAlertDialog(context, "Alert",
                                    "Empty responce from service", false);
                    } else {
                        Utilities.showAlertDialog(ELearning.this, status, message, false);
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                Utilities.showAlertDialog(ELearning.this, "Alert", "Service not responding.", false);
            }
        }
    }

    public class ElearningMenuList {

        private String MenuName;

        public String getMenuName() {
            return MenuName;
        }

        public void setMenuName(String menuName) {
            MenuName = menuName;
        }
    }

    class ELearningMenuAdapter extends BaseAdapter {
        private ArrayList<ElearningMenuList> emu;
        public Context _context;
        private LayoutInflater mInflater;

        public ELearningMenuAdapter(Context context,
                                    ArrayList<ElearningMenuList> results) {
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
        public View getView(final int position, View convertView, final ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.elearninglist_item, null);

                holder = new ViewHolder();
                holder.MenuName = (TextView) convertView.findViewById(R.id.menuname);

                holder.MenuName.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        final CharSequence[] items = {
                                "Videos", "PPT / MANUAL"
                        };
                        AlertDialog.Builder builder;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP || Build.VERSION.SDK_INT < 22) {
                            builder = new AlertDialog.Builder(ELearning.this);
                            builder.setCancelable(false);
                            builder.setTitle("Make your selection");
                            builder.setItems(items, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int item) {
                                    // Do something with the selection
                                    //Toast.makeText(getApplicationContext(), "Clicked" + items[item].toString(), Toast.LENGTH_SHORT).show();
                                    if (items[item].toString().equals("PPT / MANUAL")) {
                                        Intent i = new Intent(ELearning.this, ELearning_Document_Activity.class);
                                        Bundle extras = new Bundle();
                                        extras.putString("TrainingType", 2 + "");
                                        extras.putString("DESGID", DESGID);
                                        extras.putString("ServiceTypeID", String.valueOf(position + 1));
                                        i.putExtras(extras);
                                        startActivity(i);
                                    } else {
                                        Intent i = new Intent(ELearning.this, ELearning_Video_Activity.class);
                                        Bundle extras = new Bundle();
                                        extras.putString("TrainingType", 10 + "");
                                        extras.putString("DESGID", DESGID);
                                        extras.putString("ServiceTypeID", String.valueOf(position + 1));
                                        Log.d("Position ID IS", String.valueOf(position + 1));
                                        i.putExtras(extras);
                                        startActivity(i);
                                    }
                                }
                            });
                            builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    }
                });

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.MenuName.setText(emu.get(position).getMenuName());

            Animation animation = null;
            animation = new ScaleAnimation((float) 1.0, (float) 1.0, (float) 0, (float) 1.0);

            animation.setDuration(500);
            convertView.startAnimation(animation);
            animation = null;

            return convertView;
        }

        class ViewHolder {
            TextView MenuName;
        }
    }
}
