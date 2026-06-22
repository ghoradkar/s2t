package com.myhindlab.abkat.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ELearning_Video_Activity extends AppCompatActivity {

    private ProgressDialog dialog;
    private UserSessionManager session;
    Context context;
    private Context appcontext;
    private String EmpCode, DESGID = "";
    private ListView list;
    int ttype, desg_id, service_type_id;
    String URL, TRType, MTI;
    double rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elearning__video_);

        init();
        setUpToolBar();
        setDefaults();

        Bundle extras = getIntent().getExtras();
        String stringVariableName = extras.getString("TrainingType");
        ttype = Integer.parseInt(stringVariableName);

        String stringVariableName2 = extras.getString("DESGID");
        desg_id = Integer.parseInt(stringVariableName2);

        String stringVariableName3 = extras.getString("ServiceTypeID");
        service_type_id = Integer.parseInt(stringVariableName3);

        new GetListdata().execute();
    }

    private void init() {
        context = ELearning_Video_Activity.this;
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
        getSupportActionBar().setTitle("ELearning Videos");

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
            pDialog = new ProgressDialog(ELearning_Video_Activity.this);
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
                        ArrayList<ElearningVideoList> emu = new ArrayList<ElearningVideoList>();
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
                                ElearningVideoList menulist = new ElearningVideoList();
                                menulist.setMaterialTrainingId(json.getString("MaterialTrainingId"));
                                menulist.setTitleInEnglish(json.getString("TitleInEnglish"));
                                menulist.setTrainingtype(json.getString("Trainingtype"));
                                menulist.setUrlPath(json.getString("UrlPath"));
                                menulist.setUtubUrlPath(json.getString("UtubUrlPath"));
                                menulist.setRateFrmUser(json.getString("rateFrmUser"));

                                URL = json.getString("UtubUrlPath");
                                MTI = json.getString("MaterialTrainingId");
                                TRType = json.getString("Trainingtype");
                                rating = Double.parseDouble(json.getString("rateFrmUser"));
                                emu.add(menulist);
                            }
                            list.setAdapter(new ELearningVideoMenuAdapter(context, emu));
                        } else
                            Utilities.showAlertDialog(context, "Alert",
                                    "Empty responce from service", false);
                    } else {
                        Utilities.showAlertDialog(ELearning_Video_Activity.this, status, message, false);
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    public class ElearningVideoList {

        String MaterialTrainingId;
        String Trainingtype;
        private String Title;
        public String TitleInEnglish;
        public String UrlPath;
        public String UtubUrlPath;
        public String rateFrmUser;

        public String getMaterialTrainingId() {
            return MaterialTrainingId;
        }

        public void setMaterialTrainingId(String materialTrainingId) {
            MaterialTrainingId = materialTrainingId;
        }

        public String getTrainingtype() {
            return Trainingtype;
        }

        public void setTrainingtype(String trainingtype) {
            Trainingtype = trainingtype;
        }

        public String getTitle() {
            return Title;
        }

        public void setTitle(String title) {
            Title = title;
        }

        public String getUrlPath() {
            return UrlPath;
        }

        public void setUrlPath(String urlPath) {
            UrlPath = urlPath;
        }

        public String getUtubUrlPath() {
            return UtubUrlPath;
        }

        public String setUtubUrlPath(String utubUrlPath) {
            UtubUrlPath = utubUrlPath;
            return utubUrlPath;
        }

        public String getRateFrmUser() {
            return rateFrmUser;
        }

        public void setRateFrmUser(String rateFrmUser) {
            this.rateFrmUser = rateFrmUser;
        }

        public String getTitleInEnglish() {
            return TitleInEnglish;
        }

        public void setTitleInEnglish(String titleInEnglish) {
            TitleInEnglish = titleInEnglish;
        }
    }

    class ELearningVideoMenuAdapter extends BaseAdapter {
        private ArrayList<ElearningVideoList> emu;
        public Context _context;
        private LayoutInflater mInflater;
        ViewHolder holder;
        // ELearning_Document_Activity.ELearningDocumentMenuAdapter.ViewHolder holder;

        public ELearningVideoMenuAdapter(Context context, ArrayList<ElearningVideoList> results) {
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
                convertView = mInflater.inflate(R.layout.elearning_video_list_item, null);
                holder = new ViewHolder();

                //holder.MaterialTrainingName = (TextView) convertView.findViewById(R.id.menuname);
                holder.ImageView = (ImageView) convertView.findViewById(R.id.image_view);
                holder.Title = (TextView) convertView.findViewById(R.id.title);
                holder.RatingBar = (RatingBar) convertView.findViewById(R.id.rating_bar);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.Title.setText(emu.get(position).getTitleInEnglish());
            Log.d(String.valueOf(ELearning_Video_Activity.this), "Tilte Is " + emu.get(position).getTitle());

            holder.RatingBar.setNumStars((int) Double.parseDouble(String.valueOf(rating)));

            getYoutubeVideoId();

            String pattern = "(?<=watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";
            Pattern compiledPattern = Pattern.compile(pattern);
            final Matcher matcher = compiledPattern.matcher(emu.get(position).getUtubUrlPath());

            String url;
            if (matcher.find()) {
                url = "http://img.youtube.com/vi/" + matcher.group() + "/maxresdefault.jpg";
                Picasso.with(context)
                        .load(url)
                        .memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE)
                        .placeholder(R.drawable.icon_videoplay)
                        .error(R.drawable.icon_videoplay)
                        .into(holder.ImageView);
                holder.ImageView.setScaleType(ImageView.ScaleType.FIT_XY);

            }
            holder.ImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(ELearning_Video_Activity.this, YouTube_Player.class);
                    Bundle extras = new Bundle();
                    extras.putString("YoutubeURL", video_id);
                    extras.putString("MaterialTrainingId", MTI);
                    extras.putString("Trainingtype", TRType);
                    i.putExtras(extras);
                    startActivity(i);
                }
            });
            return convertView;
        }

        class ViewHolder {
            TextView Title;
            android.widget.RatingBar RatingBar;
            public android.widget.ImageView ImageView;
            public android.widget.ImageView thumbnail_micro;
        }
    }

    String video_id = "";

    public String getYoutubeVideoId() {
        if (URL != null && URL.trim().length() > 0 && URL.startsWith("http")) {
            String expression = "^.*((youtu.be" + "\\/)"
                    + "|(v\\/)|(\\/u\\/w\\/)|(embed\\/)|(watch\\?))\\??v?=?([^#\\&\\?]*).*";
            CharSequence input = URL;
            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(input);
            if (matcher.matches()) {
                String groupIndex1 = matcher.group(7);
                if (groupIndex1 != null && groupIndex1.length() == 11)
                    video_id = groupIndex1;
            }
        }
        return video_id;
    }
}
