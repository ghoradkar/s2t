package com.myhindlab.abkat.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.MiniCampQuestionsFilteredModel;
import com.myhindlab.abkat.models.MiniCampQuestionsModel;
import com.myhindlab.abkat.models.PresentPatientList_Model;
import com.myhindlab.abkat.rest.ApiClient;
import com.myhindlab.abkat.rest.ApiInterface;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.myhindlab.abkat.utilities.ApplicationConstants.OKSUCCESS;

public class HealthScreeningMiniCamp_Activity extends AppCompatActivity {

    private Context context;
    private UserSessionManager session;
    private ProgressDialog pd;
    private MaterialEditText edt_beneficiaryname, edt_gender, edt_age, edt_height, edt_weight;
    private RecyclerView rv_questions;
    private Button btn_register;

    private PresentPatientList_Model patientDetails;
    private String userID, campId, healthScreentype;
    private List<MiniCampQuestionsFilteredModel> filteredList;

    private List<MiniCampQuestionsModel.OutputBean> questionsWithAnsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_healthscreening_minicamp);

        init();
        getSessionData();
        setDefaults();
        setEventHandler();
        setUpToolbar();
    }

    private void init() {
        context = HealthScreeningMiniCamp_Activity.this;
        session = new UserSessionManager(context);
        pd = new ProgressDialog(context);
        pd.setMessage("Please Wait...");
        pd.setCancelable(false);

        rv_questions = findViewById(R.id.rv_questions);
        rv_questions.setLayoutManager(new LinearLayoutManager(context));

        edt_beneficiaryname = findViewById(R.id.edt_beneficiaryname);
        edt_gender = findViewById(R.id.edt_gender);
        edt_age = findViewById(R.id.edt_age);
        edt_height = findViewById(R.id.edt_height);
        edt_weight = findViewById(R.id.edt_weight);
        btn_register = findViewById(R.id.btn_register);

        questionsWithAnsList = new ArrayList<>();
    }

    private void getSessionData() {
        try {
            JSONArray user_info = new JSONArray(session.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                userID = json.getString("EmpCode");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDefaults() {

        patientDetails = (PresentPatientList_Model) getIntent().getSerializableExtra("patientDetails");
        campId = getIntent().getStringExtra("campId");
        healthScreentype = getIntent().getStringExtra("healthScreentype");

        edt_beneficiaryname.setText(patientDetails.getEnglishName());
        edt_age.setText(patientDetails.getAge());
        edt_height.setText(String.valueOf(patientDetails.getHeightCMs()));
        edt_weight.setText(String.valueOf(patientDetails.getWeightKGs()));

        if (patientDetails.getGender().equalsIgnoreCase("M")) {
            edt_gender.setText("Male");
        } else if (patientDetails.getGender().equalsIgnoreCase("F")) {
            edt_gender.setText("Female");
        } else if (patientDetails.getGender().equalsIgnoreCase("O")) {
            edt_gender.setText("Other");
        }
        getQuestionsListApi();
    }

    private void setEventHandler() {
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonArray ansJsonArray = new JsonArray();
                for (int i = 0; i < questionsWithAnsList.size(); i++) {
                    if (questionsWithAnsList.get(i).getIsYesNo().equals("")) {
                        Utilities.showToastMessage("Please provide answer all the questions", context, false);
                        return;
                    } else {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("QLevel2Id", questionsWithAnsList.get(i).getQLevel2Id());
                        jsonObject.addProperty("RegdId", patientDetails.getRegdId());
                        jsonObject.addProperty("CampId", campId);
                        jsonObject.addProperty("QuestionAns", questionsWithAnsList.get(i).getIsYesNo());
                        jsonObject.addProperty("CreatedBy", userID);
                        ansJsonArray.add(jsonObject);

                    }
                }

                Log.d("MINICAMPQUEANS", ansJsonArray.toString());

                if (Utilities.isNetworkAvailable(context)) {
                    new InsertQuestionnaireDetails().execute(ansJsonArray.toString());
                } else {
                    Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                }

            }
        });
    }

    private void getQuestionsListApi() {
        pd.show();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<MiniCampQuestionsModel> call = apiInterface.getMiniCampQuestions();
        call.enqueue(new Callback<MiniCampQuestionsModel>() {
            @Override
            public void onResponse(Call<MiniCampQuestionsModel> call, Response<MiniCampQuestionsModel> response) {
                pd.dismiss();
                if (response.code() == OKSUCCESS) {
                    if (response.body().getStatus().equalsIgnoreCase("Success")) {
                        if (response.body().getOutput().size() != 0) {
                            String jsonString = new Gson().toJson(response.body().getOutput());
                            Log.d("MINICAMPQUELIST", jsonString);
                            filterQuestions(response.body().getOutput());
                            questionsWithAnsList = response.body().getOutput();
                            btn_register.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Utilities.showAlertDialog(context, "Fail", response.body().getMessage(), false);
                    }
                } else {
                    Utilities.showAlertDialog(context, "Alert", "Server not responding", false);
                }
            }

            @Override
            public void onFailure(Call<MiniCampQuestionsModel> call, Throwable t) {
                pd.dismiss();
                t.printStackTrace();
                Utilities.showAlertDialog(context, "Alert", "Server not responding", false);
            }
        });

//        String JSON = "[\n" + "  {\n" + "    \"QHeaderId\": \"1\",\n" + "    \"QHeaderMName\": \"हृदयाची तपासणीसाठी\",\n" + "    \"QLevel1Id\": \"1\",\n" + "    \"QLevel1MName\": \"तुमच्या छातीत दुखते का?\",\n" + "    \"QLevel2Id\": \"1\",\n" + "    \"QLevel2MName\": \"तुमच्या छातीत दुखते का?\",\n" + "    \"QuestionType\": \"1\"\n" + "  },\n" + "  {\n" + "    \"QHeaderId\": \"1\",\n" + "    \"QHeaderMName\": \"हृदयाची तपासणीसाठी\",\n" + "    \"QLevel1Id\": \"2\",\n" + "    \"QLevel1MName\": \"तुमच्या छातीत धडधड होते का?\",\n" + "    \"QLevel2Id\": \"2\",\n" + "    \"QLevel2MName\": \"तुमच्या छातीत धडधड होते का?\",\n" + "    \"QuestionType\": \"1\"\n" + "  },\n" + "  {\n" + "    \"QHeaderId\": \"1\",\n" + "    \"QHeaderMName\": \"हृदयाची तपासणीसाठी\",\n" + "    \"QLevel1Id\": \"3\",\n" + "    \"QLevel1MName\": \"तुमचे डोक जास्त दुखते का?\",\n" + "    \"QLevel2Id\": \"3\",\n" + "    \"QLevel2MName\": \"तुमचे डोक जास्त दुखते का?\",\n" + "    \"QuestionType\": \"1\"\n" + "  },\n" + "  {\n" + "    \"QHeaderId\": \"1\",\n" + "    \"QHeaderMName\": \"हृदयाची तपासणीसाठी\",\n" + "    \"QLevel1Id\": \"4\",\n" + "    \"QLevel1MName\": \"तुम्हाला जास्त घाम येतो का?\",\n" + "    \"QLevel2Id\": \"4\",\n" + "    \"QLevel2MName\": \"तुम्हाला जास्त घाम येतो का?\",\n" + "    \"QuestionType\": \"1\"\n" + "  },\n" + "  {\n" + "    \"QHeaderId\": \"1\",\n" + "    \"QHeaderMName\": \"हृदयाची तपासणीसाठी\",\n" + "    \"QLevel1Id\": \"5\",\n" + "    \"QLevel1MName\": \"तुम्हाला लहानपणी हृदयाचा त्रास झाला का?\",\n" + "    \"QLevel2Id\": \"5\",\n" + "    \"QLevel2MName\": \"तुम्हाला लहानपणी हृदयाचा त्रास झाला का?\",\n" + "    \"QuestionType\": \"1\"\n" + "  },\n" + "  {\n" + "    \"QHeaderId\": \"2\",\n" + "    \"QHeaderMName\": \"श्वसनाची तपासणी\",\n" + "    \"QLevel1Id\": \"6\",\n" + "    \"QLevel1MName\": \"श्वास घेण्यासाठी त्रास होतो का?\",\n" + "    \"QLevel2Id\": \"6\",\n" + "    \"QLevel2MName\": \"श्वास घेण्यासाठी त्रास होतो का?\",\n" + "    \"QuestionType\": \"1\"\n" + "  },\n" + "  {\n" + "    \"QHeaderId\": \"2\",\n" + "    \"QHeaderMName\": \"श्वसनाची तपासणी\",\n" + "    \"QLevel1Id\": \"7\",\n" + "    \"QLevel1MName\": \"तुम्हाला दम लागतो का?\",\n" + "    \"QLevel2Id\": \"7\",\n" + "    \"QLevel2MName\": \"छोटी टेकडी चालल्यावर\",\n" + "    \"QuestionType\": \"2\"\n" + "  },\n" + "  {\n" + "    \"QHeaderId\": \"2\",\n" + "    \"QHeaderMName\": \"श्वसनाची तपासणी\",\n" + "    \"QLevel1Id\": \"7\",\n" + "    \"QLevel1MName\": \"तुम्हाला दम लागतो का?\",\n" + "    \"QLevel2Id\": \"8\",\n" + "    \"QLevel2MName\": \"2-3 जिना  चढ़ल्यावर\",\n" + "    \"QuestionType\": \"2\"\n" + "  },\n" + "  {\n" + "    \"QHeaderId\": \"2\",\n" + "    \"QHeaderMName\": \"श्वसनाची तपासणी\",\n" + "    \"QLevel1Id\": \"7\",\n" + "    \"QLevel1MName\": \"तुम्हाला दम लागतो का?\",\n" + "    \"QLevel2Id\": \"9\",\n" + "    \"QLevel2MName\": \"1 जिना  चढल्यावर\",\n" + "    \"QuestionType\": \"2\"\n" + "  },\n" + "  {\n" + "    \"QHeaderId\": \"2\",\n" + "    \"QHeaderMName\": \"श्वसनाची तपासणी\",\n" + "    \"QLevel1Id\": \"7\",\n" + "    \"QLevel1MName\": \"तुम्हाला दम लागतो का?\",\n" + "    \"QLevel2Id\": \"10\",\n" + "    \"QLevel2MName\": \"बसून बसून दम लागणे\",\n" + "    \"QuestionType\": \"2\"\n" + "  },\n" + "  {\n" + "    \"QHeaderId\": \"2\",\n" + "    \"QHeaderMName\": \"श्वसनाची तपासणी\",\n" + "    \"QLevel1Id\": \"20\",\n" + "    \"QLevel1MName\": \"श्वास घेताना घरघर आवाज येतो का?\",\n" + "    \"QLevel2Id\": \"23\",\n" + "    \"QLevel2MName\": \"श्वास घेताना घरघर आवाज येतो का?\",\n" + "    \"QuestionType\": \"2\"\n" + "  },\n" + "  {\n" + "    \"QHeaderId\": \"2\",\n" + "    \"QHeaderMName\": \"श्वसनाची तपासणी\",\n" + "    \"QLevel1Id\": \"21\",\n" + "    \"QLevel1MName\": \" सर्दी खोकला त्रास आहे का?\",\n" + "    \"QLevel2Id\": \"24\",\n" + "    \"QLevel2MName\": \"सर्दी खोकला त्रास आहे का?\",\n" + "    \"QuestionType\": \"2\"\n" + "  },\n" + "  {\n" + "    \"QHeaderId\": \"3\",\n" + "    \"QHeaderMName\": \"पोटाची तपासणीसाठी\",\n" + "    \"QLevel1Id\": \"8\",\n" + "    \"QLevel1MName\": \"पोटात दुखते का?\",\n" + "    \"QLevel2Id\": \"11\",\n" + "    \"QLevel2MName\": \"पोटात दुखते का?\",\n" + "    \"QuestionType\": \"1\"\n" + "  },\n" + "  {\n" + "    \"QHeaderId\": \"3\",\n" + "    \"QHeaderMName\": \"पोटाची तपासणीसाठी\",\n" + "    \"QLevel1Id\": \"9\",\n" + "    \"QLevel1MName\": \"पोटावर गाठ, सूज आहे का?\",\n" + "    \"QLevel2Id\": \"12\",\n" + "    \"QLevel2MName\": \"पोटावर गाठ, सूज आहे का?\",\n" + "    \"QuestionType\": \"1\"\n" + "  },\n" + "  {\n" + "    \"QHeaderId\": \"3\",\n" + "    \"QHeaderMName\": \"पोटाची तपासणीसाठी\",\n" + "    \"QLevel1Id\": \"10\",\n" + "    \"QLevel1MName\": \"जास्त जुलाब व मूळव्याधचा त्रास आहे का?\",\n" + "    \"QLevel2Id\": \"13\",\n" + "    \"QLevel2MName\": \"जास्त जुलाब व मूळव्याधचा त्रास आहे का?\",\n" + "    \"QuestionType\": \"1\"\n" + "  },\n" + "  {\n" + "    \"QHeaderId\": \"4\",\n" + "    \"QHeaderMName\": \"केंद्रीय मज्जासंस्था संबंधित\",\n" + "    \"QLevel1Id\": \"11\",\n" + "    \"QLevel1MName\": \"अंगावर खाज, खुजली , नायटा आहे का?\",\n" + "    \"QLevel2Id\": \"14\",\n" + "    \"QLevel2MName\": \"अंगावर खाज, खुजली , नायटा आहे का?\",\n" + "    \"QuestionType\": \"1\"\n" + "  },\n" + "  {\n" + "    \"QHeaderId\": \"4\",\n" + "    \"QHeaderMName\": \"केंद्रीय मज्जासंस्था संबंधित\",\n" + "    \"QLevel1Id\": \"12\",\n" + "    \"QLevel1MName\": \"अलर्जी  चा त्रास आहे का?\",\n" + "    \"QLevel2Id\": \"15\",\n" + "    \"QLevel2MName\": \"अलर्जी  चा त्रास आहे का?\",\n" + "    \"QuestionType\": \"1\"\n" + "  },\n" + "  {\n" + "    \"QHeaderId\": \"4\",\n" + "    \"QHeaderMName\": \"केंद्रीय मज्जासंस्था संबंधित\",\n" + "    \"QLevel1Id\": \"13\",\n" + "    \"QLevel1MName\": \"अंगावर डाग, चट्टा आहे का?\",\n" + "    \"QLevel2Id\": \"16\",\n" + "    \"QLevel2MName\": \"अंगावर डाग, चट्टा आहे का?\",\n" + "    \"QuestionType\": \"1\"\n" + "  },\n" + "  {\n" + "    \"QHeaderId\": \"5\",\n" + "    \"QHeaderMName\": \"स्त्रीरोग तपासणी संबंधी\",\n" + "    \"QLevel1Id\": \"14\",\n" + "    \"QLevel1MName\": \"अंगावरून पांढरे जाते का?\",\n" + "    \"QLevel2Id\": \"17\",\n" + "    \"QLevel2MName\": \"तुमच्या छातीत दुखते का?\",\n" + "    \"QuestionType\": \"1\"\n" + "  },\n" + "  {\n" + "    \"QHeaderId\": \"5\",\n" + "    \"QHeaderMName\": \"स्त्रीरोग तपासणी संबंधी\",\n" + "    \"QLevel1Id\": \"15\",\n" + "    \"QLevel1MName\": \"लघवीला जळजळ होते का?\",\n" + "    \"QLevel2Id\": \"18\",\n" + "    \"QLevel2MName\": \"तुमच्या छातीत धडधड होते का?\",\n" + "    \"QuestionType\": \"1\"\n" + "  },\n" + "  {\n" + "    \"QHeaderId\": \"5\",\n" + "    \"QHeaderMName\": \"स्त्रीरोग तपासणी संबंधी\",\n" + "    \"QLevel1Id\": \"16\",\n" + "    \"QLevel1MName\": \"अंगावरून जास्त रक्त जाते का?\",\n" + "    \"QLevel2Id\": \"19\",\n" + "    \"QLevel2MName\": \"तुमचे डोक जास्त दुखते का?\",\n" + "    \"QuestionType\": \"1\"\n" + "  },\n" + "  {\n" + "    \"QHeaderId\": \"5\",\n" + "    \"QHeaderMName\": \"स्त्रीरोग तपासणी संबंधी\",\n" + "    \"QLevel1Id\": \"17\",\n" + "    \"QLevel1MName\": \"पाळी नियमित येते का?\",\n" + "    \"QLevel2Id\": \"20\",\n" + "    \"QLevel2MName\": \"तुम्हाला जास्त घाम येतो का?\",\n" + "    \"QuestionType\": \"1\"\n" + "  },\n" + "  {\n" + "    \"QHeaderId\": \"5\",\n" + "    \"QHeaderMName\": \"स्त्रीरोग तपासणी संबंधी\",\n" + "    \"QLevel1Id\": \"18\",\n" + "    \"QLevel1MName\": \"पिशवीचा काही त्रास आहे का?\",\n" + "    \"QLevel2Id\": \"21\",\n" + "    \"QLevel2MName\": \"तुम्हाला लहानपणी हृदयाचा त्रास झाला का?\",\n" + "    \"QuestionType\": \"1\"\n" + "  },\n" + "  {\n" + "    \"QHeaderId\": \"5\",\n" + "    \"QHeaderMName\": \"स्त्रीरोग तपासणी संबंधी\",\n" + "    \"QLevel1Id\": \"19\",\n" + "    \"QLevel1MName\": \"मुल बंद ऑपरेशन झाले का?\",\n" + "    \"QLevel2Id\": \"22\",\n" + "    \"QLevel2MName\": \"तुमच्या छातीत दुखते का?\",\n" + "    \"QuestionType\": \"1\"\n" + "  }\n" + "]";

//        String JSON = "[\n" + "  {\n" + "    \"QHeaderId\": \"1\",\n" + "    \"QHeaderMName\": \"हृदयाची तपासणीसाठी\",\n" + "    \"QLevel1Id\": \"1\",\n" + "    \"QLevel1MName\": \"तुमच्या छातीत दुखते का?\",\n" + "    \"QLevel2Id\": \"1\",\n" + "    \"QLevel2MName\": \"तुमच्या छातीत दुखते का?\",\n" + "    \"QuestionType\": \"1\"\n" + "  },\n" + "  {\n" + "    \"QHeaderId\": \"1\",\n" + "    \"QHeaderMName\": \"हृदयाची तपासणीसाठी\",\n" + "    \"QLevel1Id\": \"2\",\n" + "    \"QLevel1MName\": \"तुमच्या छातीत धडधड होते का?\",\n" + "    \"QLevel2Id\": \"2\",\n" + "    \"QLevel2MName\": \"तुमच्या छातीत धडधड होते का?\",\n" + "    \"QuestionType\": \"1\"\n" + "  },\n" + "  {\n" + "    \"QHeaderId\": \"1\",\n" + "    \"QHeaderMName\": \"हृदयाची तपासणीसाठी\",\n" + "    \"QLevel1Id\": \"3\",\n" + "    \"QLevel1MName\": \"तुमचे डोक जास्त दुखते का?\",\n" + "    \"QLevel2Id\": \"3\",\n" + "    \"QLevel2MName\": \"तुमचे डोक जास्त दुखते का?\",\n" + "    \"QuestionType\": \"1\"\n" + "  },\n" + "  {\n" + "    \"QHeaderId\": \"1\",\n" + "    \"QHeaderMName\": \"हृदयाची तपासणीसाठी\",\n" + "    \"QLevel1Id\": \"4\",\n" + "    \"QLevel1MName\": \"तुम्हाला जास्त घाम येतो का?\",\n" + "    \"QLevel2Id\": \"4\",\n" + "    \"QLevel2MName\": \"तुम्हाला जास्त घाम येतो का?\",\n" + "    \"QuestionType\": \"1\"\n" + "  },\n" + "  {\n" + "    \"QHeaderId\": \"1\",\n" + "    \"QHeaderMName\": \"हृदयाची तपासणीसाठी\",\n" + "    \"QLevel1Id\": \"5\",\n" + "    \"QLevel1MName\": \"तुम्हाला लहानपणी हृदयाचा त्रास झाला का?\",\n" + "    \"QLevel2Id\": \"5\",\n" + "    \"QLevel2MName\": \"तुम्हाला लहानपणी हृदयाचा त्रास झाला का?\",\n" + "    \"QuestionType\": \"1\"\n" + "  },\n" + "  {\n" + "    \"QHeaderId\": \"2\",\n" + "    \"QHeaderMName\": \"श्वसनाची तपासणी\",\n" + "    \"QLevel1Id\": \"6\",\n" + "    \"QLevel1MName\": \"श्वास घेण्यासाठी त्रास होतो का?\",\n" + "    \"QLevel2Id\": \"6\",\n" + "    \"QLevel2MName\": \"श्वास घेण्यासाठी त्रास होतो का?\",\n" + "    \"QuestionType\": \"1\"\n" + "  },\n" + "  {\n" + "    \"QHeaderId\": \"2\",\n" + "    \"QHeaderMName\": \"श्वसनाची तपासणी\",\n" + "    \"QLevel1Id\": \"7\",\n" + "    \"QLevel1MName\": \"तुम्हाला दम लागतो का?\",\n" + "    \"QLevel2Id\": \"7\",\n" + "    \"QLevel2MName\": \"छोटी टेकडी चालल्यावर\",\n" + "    \"QuestionType\": \"2\"\n" + "  },\n" + "  {\n" + "    \"QHeaderId\": \"2\",\n" + "    \"QHeaderMName\": \"श्वसनाची तपासणी\",\n" + "    \"QLevel1Id\": \"7\",\n" + "    \"QLevel1MName\": \"तुम्हाला दम लागतो का?\",\n" + "    \"QLevel2Id\": \"8\",\n" + "    \"QLevel2MName\": \"2-3 जिनाचढ़ल्यावर\",\n" + "    \"QuestionType\": \"2\"\n" + "  },\n" + "  {\n" + "    \"QHeaderId\": \"2\",\n" + "    \"QHeaderMName\": \"श्वसनाची तपासणी\",\n" + "    \"QLevel1Id\": \"7\",\n" + "    \"QLevel1MName\": \"तुम्हाला दम लागतो का?\",\n" + "    \"QLevel2Id\": \"9\",\n" + "    \"QLevel2MName\": \"1 जिनाचढल्यावर\",\n" + "    \"QuestionType\": \"2\"\n" + "  },\n" + "  {\n" + "    \"QHeaderId\": \"2\",\n" + "    \"QHeaderMName\": \"श्वसनाची तपासणी\",\n" + "    \"QLevel1Id\": \"7\",\n" + "    \"QLevel1MName\": \"तुम्हाला दम लागतो का?\",\n" + "    \"QLevel2Id\": \"10\",\n" + "    \"QLevel2MName\": \"बसून बसून दम लागणे\",\n" + "    \"QuestionType\": \"2\"\n" + "  },\n" + "  {\n" + "    \"QHeaderId\": \"2\",\n" + "    \"QHeaderMName\": \"श्वसनाची तपासणी\",\n" + "    \"QLevel1Id\": \"20\",\n" + "    \"QLevel1MName\": \"श्वास घेताना घरघर आवाज येतो का?\",\n" + "    \"QLevel2Id\": \"23\",\n" + "    \"QLevel2MName\": \"श्वास घेताना घरघर आवाज येतो का?\",\n" + "    \"QuestionType\": \"1\"\n" + "  },\n" + "  {\n" + "    \"QHeaderId\": \"2\",\n" + "    \"QHeaderMName\": \"श्वसनाची तपासणी\",\n" + "    \"QLevel1Id\": \"21\",\n" + "    \"QLevel1MName\": \" सर्दी खोकला त्रास आहे का?\",\n" + "    \"QLevel2Id\": \"24\",\n" + "    \"QLevel2MName\": \"सर्दी खोकला त्रास आहे का?\",\n" + "    \"QuestionType\": \"1\"\n" + "  },\n" + "  {\n" + "    \"QHeaderId\": \"3\",\n" + "    \"QHeaderMName\": \"पोटाची तपासणीसाठी\",\n" + "    \"QLevel1Id\": \"8\",\n" + "    \"QLevel1MName\": \"पोटात दुखते का?\",\n" + "    \"QLevel2Id\": \"11\",\n" + "    \"QLevel2MName\": \"पोटात दुखते का?\",\n" + "    \"QuestionType\": \"1\"\n" + "  },\n" + "  {\n" + "    \"QHeaderId\": \"3\",\n" + "    \"QHeaderMName\": \"पोटाची तपासणीसाठी\",\n" + "    \"QLevel1Id\": \"9\",\n" + "    \"QLevel1MName\": \"पोटावर गाठ, सूज आहे का?\",\n" + "    \"QLevel2Id\": \"12\",\n" + "    \"QLevel2MName\": \"पोटावर गाठ, सूज आहे का?\",\n" + "    \"QuestionType\": \"1\"\n" + "  },\n" + "  {\n" + "    \"QHeaderId\": \"3\",\n" + "    \"QHeaderMName\": \"पोटाची तपासणीसाठी\",\n" + "    \"QLevel1Id\": \"10\",\n" + "    \"QLevel1MName\": \"जास्त जुलाब व मूळव्याधचा त्रास आहे का?\",\n" + "    \"QLevel2Id\": \"13\",\n" + "    \"QLevel2MName\": \"जास्त जुलाब व मूळव्याधचा त्रास आहे का?\",\n" + "    \"QuestionType\": \"1\"\n" + "  },\n" + "  {\n" + "    \"QHeaderId\": \"4\",\n" + "    \"QHeaderMName\": \"केंद्रीय मज्जासंस्था संबंधित\",\n" + "    \"QLevel1Id\": \"11\",\n" + "    \"QLevel1MName\": \"अंगावर खाज, खुजली , नायटा आहे का?\",\n" + "    \"QLevel2Id\": \"14\",\n" + "    \"QLevel2MName\": \"अंगावर खाज, खुजली , नायटा आहे का?\",\n" + "    \"QuestionType\": \"1\"\n" + "  },\n" + "  {\n" + "    \"QHeaderId\": \"4\",\n" + "    \"QHeaderMName\": \"केंद्रीय मज्जासंस्था संबंधित\",\n" + "    \"QLevel1Id\": \"12\",\n" + "    \"QLevel1MName\": \"अलर्जीचा त्रास आहे का?\",\n" + "    \"QLevel2Id\": \"15\",\n" + "    \"QLevel2MName\": \"अलर्जीचा त्रास आहे का?\",\n" + "    \"QuestionType\": \"1\"\n" + "  },\n" + "  {\n" + "    \"QHeaderId\": \"4\",\n" + "    \"QHeaderMName\": \"केंद्रीय मज्जासंस्था संबंधित\",\n" + "    \"QLevel1Id\": \"13\",\n" + "    \"QLevel1MName\": \"अंगावर डाग, चट्टा आहे का?\",\n" + "    \"QLevel2Id\": \"16\",\n" + "    \"QLevel2MName\": \"अंगावर डाग, चट्टा आहे का?\",\n" + "    \"QuestionType\": \"1\"\n" + "  },\n" + "  {\n" + "    \"QHeaderId\": \"5\",\n" + "    \"QHeaderMName\": \"स्त्रीरोग तपासणी संबंधी\",\n" + "    \"QLevel1Id\": \"14\",\n" + "    \"QLevel1MName\": \"अंगावरून पांढरे जाते का?\",\n" + "    \"QLevel2Id\": \"17\",\n" + "    \"QLevel2MName\": \"तुमच्या छातीत दुखते का?\",\n" + "    \"QuestionType\": \"1\"\n" + "  },\n" + "  {\n" + "    \"QHeaderId\": \"5\",\n" + "    \"QHeaderMName\": \"स्त्रीरोग तपासणी संबंधी\",\n" + "    \"QLevel1Id\": \"15\",\n" + "    \"QLevel1MName\": \"लघवीला जळजळ होते का?\",\n" + "    \"QLevel2Id\": \"18\",\n" + "    \"QLevel2MName\": \"तुमच्या छातीत धडधड होते का?\",\n" + "    \"QuestionType\": \"1\"\n" + "  },\n" + "  {\n" + "    \"QHeaderId\": \"5\",\n" + "    \"QHeaderMName\": \"स्त्रीरोग तपासणी संबंधी\",\n" + "    \"QLevel1Id\": \"16\",\n" + "    \"QLevel1MName\": \"अंगावरून जास्त रक्त जाते का?\",\n" + "    \"QLevel2Id\": \"19\",\n" + "    \"QLevel2MName\": \"तुमचे डोक जास्त दुखते का?\",\n" + "    \"QuestionType\": \"1\"\n" + "  },\n" + "  {\n" + "    \"QHeaderId\": \"5\",\n" + "    \"QHeaderMName\": \"स्त्रीरोग तपासणी संबंधी\",\n" + "    \"QLevel1Id\": \"17\",\n" + "    \"QLevel1MName\": \"पाळी नियमित येते का?\",\n" + "    \"QLevel2Id\": \"20\",\n" + "    \"QLevel2MName\": \"तुम्हाला जास्त घाम येतो का?\",\n" + "    \"QuestionType\": \"1\"\n" + "  },\n" + "  {\n" + "    \"QHeaderId\": \"5\",\n" + "    \"QHeaderMName\": \"स्त्रीरोग तपासणी संबंधी\",\n" + "    \"QLevel1Id\": \"18\",\n" + "    \"QLevel1MName\": \"पिशवीचा काही त्रास आहे का?\",\n" + "    \"QLevel2Id\": \"21\",\n" + "    \"QLevel2MName\": \"तुम्हाला लहानपणी हृदयाचा त्रास झाला का?\",\n" + "    \"QuestionType\": \"1\"\n" + "  },\n" + "  {\n" + "    \"QHeaderId\": \"5\",\n" + "    \"QHeaderMName\": \"स्त्रीरोग तपासणी संबंधी\",\n" + "    \"QLevel1Id\": \"19\",\n" + "    \"QLevel1MName\": \"मुल बंद ऑपरेशन झाले का?\",\n" + "    \"QLevel2Id\": \"22\",\n" + "    \"QLevel2MName\": \"तुमच्या छातीत दुखते का?\",\n" + "    \"QuestionType\": \"1\"\n" + "  }\n" + "]";

//        List<MiniCampQuestionsModel.OutputBean> list = new Gson().fromJson(JSON, new TypeToken<List<MiniCampQuestionsModel.OutputBean>>() {
//        }.getType());
//        filterQuestions(list);
//        questionsWithAnsList = list;
    }

    private void filterQuestions(List<MiniCampQuestionsModel.OutputBean> questionsList) {
        filteredList = new ArrayList<>();
        List<MiniCampQuestionsModel.OutputBean> questionsListForMOnly = new ArrayList<>();

        if (patientDetails.getGender().equalsIgnoreCase("M")) {
            for (MiniCampQuestionsModel.OutputBean queDetails : questionsList) {
                if (!queDetails.getQHeaderId().equals("5")) {
                    questionsListForMOnly.add(queDetails);
                }
            }
            questionsList.clear();
            questionsList.addAll(questionsListForMOnly);
        }

        for (int i = 0; i < questionsList.size(); i++) {
            if (filteredList.size() == 0) {
                MiniCampQuestionsFilteredModel filteredModel = new MiniCampQuestionsFilteredModel();
                filteredModel.setQHeaderId(questionsList.get(i).getQHeaderId());
                filteredModel.setQHeaderMName(questionsList.get(i).getQHeaderMName());

                List<MiniCampQuestionsFilteredModel.OutputBean> beansList = new ArrayList<>();
                MiniCampQuestionsFilteredModel.OutputBean bean = new MiniCampQuestionsFilteredModel.OutputBean();
                bean.setQuestionType(questionsList.get(i).getQuestionType());
                bean.setQHeaderId(questionsList.get(i).getQHeaderId());
                bean.setQHeaderMName(questionsList.get(i).getQHeaderMName());
                bean.setQLevel1Id(questionsList.get(i).getQLevel1Id());
                bean.setQLevel1MName(questionsList.get(i).getQLevel1MName());
                bean.setQLevel2Id(questionsList.get(i).getQLevel2Id());
                bean.setQLevel2MName(questionsList.get(i).getQLevel2MName());
                beansList.add(bean);

                filteredModel.setOutput(beansList);

                filteredList.add(filteredModel);
            } else {
                boolean isPresent = false;
                MiniCampQuestionsFilteredModel filteredModel = new MiniCampQuestionsFilteredModel();
                for (int j = 0; j < filteredList.size(); j++) {
                    if (questionsList.get(i).getQHeaderId().equalsIgnoreCase(filteredList.get(j).getQHeaderId())) {
                        isPresent = true;
                        filteredModel = filteredList.get(j);
                    }
                }

                if (isPresent) {
                    MiniCampQuestionsFilteredModel.OutputBean bean = new MiniCampQuestionsFilteredModel.OutputBean();
                    bean.setQuestionType(questionsList.get(i).getQuestionType());
                    bean.setQHeaderId(questionsList.get(i).getQHeaderId());
                    bean.setQHeaderMName(questionsList.get(i).getQHeaderMName());
                    bean.setQLevel1Id(questionsList.get(i).getQLevel1Id());
                    bean.setQLevel1MName(questionsList.get(i).getQLevel1MName());
                    bean.setQLevel2Id(questionsList.get(i).getQLevel2Id());
                    bean.setQLevel2MName(questionsList.get(i).getQLevel2MName());

                    filteredModel.getOutput().add(bean);
                } else {
                    MiniCampQuestionsFilteredModel filteredModel1 = new MiniCampQuestionsFilteredModel();
                    filteredModel1.setQHeaderId(questionsList.get(i).getQHeaderId());
                    filteredModel1.setQHeaderMName(questionsList.get(i).getQHeaderMName());

                    List<MiniCampQuestionsFilteredModel.OutputBean> beansList = new ArrayList<>();
                    MiniCampQuestionsFilteredModel.OutputBean bean = new MiniCampQuestionsFilteredModel.OutputBean();
                    bean.setQuestionType(questionsList.get(i).getQuestionType());
                    bean.setQHeaderId(questionsList.get(i).getQHeaderId());
                    bean.setQHeaderMName(questionsList.get(i).getQHeaderMName());
                    bean.setQLevel1Id(questionsList.get(i).getQLevel1Id());
                    bean.setQLevel1MName(questionsList.get(i).getQLevel1MName());
                    bean.setQLevel2Id(questionsList.get(i).getQLevel2Id());
                    bean.setQLevel2MName(questionsList.get(i).getQLevel2MName());
                    beansList.add(bean);

                    filteredModel1.setOutput(beansList);

                    filteredList.add(filteredModel1);
                }

            }
        }

        rv_questions.setAdapter(new HeaderAdapter());

    }

    private class HeaderAdapter extends RecyclerView.Adapter<HeaderAdapter.MyViewHolder> {

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.list_row_minique_header, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int pos) {
            int position = holder.getAdapterPosition();
            MiniCampQuestionsFilteredModel filteredDetails = filteredList.get(position);
            holder.rv_mainques.setLayoutManager(new LinearLayoutManager(context));

            holder.tv_header.setText(filteredDetails.getQHeaderMName());
            holder.rv_mainques.setAdapter(new MainQuestionsAdapter(filteredDetails.getOutput()));

        }

        @Override
        public int getItemCount() {
            return filteredList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            private TextView tv_header;
            private RecyclerView rv_mainques;

            public MyViewHolder(@NonNull View view) {
                super(view);
                tv_header = view.findViewById(R.id.tv_header);
                rv_mainques = view.findViewById(R.id.rv_mainques);
            }
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }
    }

    private class MainQuestionsAdapter extends RecyclerView.Adapter<MainQuestionsAdapter.MyViewHolder> {
        private List<MiniCampQuestionsFilteredModel.OutputBean> mainQuesList;
        int posSubQues = 1;
        int posMainSub;
        boolean isSubWerePresent = false;

        public MainQuestionsAdapter(List<MiniCampQuestionsFilteredModel.OutputBean> mainQuesList) {
            this.mainQuesList = mainQuesList;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.list_row_minique_mainsubques, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int pos) {
            int position = holder.getAdapterPosition();
            int posMainQues = position + 1;

            MiniCampQuestionsFilteredModel.OutputBean outputBean = mainQuesList.get(position);
            MiniCampQuestionsFilteredModel.OutputBean outputBeanPrev = new MiniCampQuestionsFilteredModel.OutputBean();
            if (position != 0)
                outputBeanPrev = mainQuesList.get(position - 1);

            if (outputBean.getQuestionType().equals("1")) {
                posSubQues = 1;
                holder.tv_mainques_withsubs.setVisibility(View.GONE);
                holder.ll_subques.setVisibility(View.VISIBLE);

                if (isSubWerePresent) {
                    posMainSub++;
                    holder.tv_subques.setText(posMainSub + ".   " + outputBean.getQLevel2MName());
                } else {
                    holder.tv_subques.setText(posMainQues + ".   " + outputBean.getQLevel2MName());
                }
            } else if (outputBean.getQuestionType().equals("2")) {
                if (!outputBean.getQuestionType().equals(outputBeanPrev.getQuestionType())) {
                    holder.tv_mainques_withsubs.setVisibility(View.VISIBLE);
                    holder.ll_subques.setVisibility(View.VISIBLE);
                    holder.tv_mainques_withsubs.setText(posMainQues + ".   " + outputBean.getQLevel1MName());
                    holder.tv_subques.setText(posMainQues + "." + posSubQues + "   " + outputBean.getQLevel2MName());
                    posMainSub = posMainQues;
                    posSubQues++;
                    isSubWerePresent = true;
                } else {
                    if (!outputBean.getQLevel1Id().equals(outputBeanPrev.getQLevel1Id())) {
                        posSubQues = 1;
                        posMainSub++;
                        holder.tv_mainques_withsubs.setVisibility(View.VISIBLE);
                        holder.ll_subques.setVisibility(View.VISIBLE);
                        holder.tv_mainques_withsubs.setText(posMainSub + ".   " + outputBean.getQLevel1MName());
                        holder.tv_subques.setText(posMainSub + "." + posSubQues + "   " + outputBean.getQLevel2MName());
                        posSubQues++;
                        isSubWerePresent = true;
                    } else {
                        holder.tv_mainques_withsubs.setVisibility(View.GONE);
                        holder.ll_subques.setVisibility(View.VISIBLE);
                        holder.tv_subques.setText(posMainSub + "." + posSubQues + "   " + outputBean.getQLevel2MName());
                        posSubQues++;
                        isSubWerePresent = true;
                    }
                }
            }

            holder.rb_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.rb_yes.isChecked()) {
                        for (int i = 0; i < questionsWithAnsList.size(); i++) {
                            if (outputBean.getQLevel2Id().equals(questionsWithAnsList.get(i).getQLevel2Id())) {
                                questionsWithAnsList.get(i).setIsYesNo("1");
                                break;
                            }
                        }
                    }
                }
            });

            holder.rb_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.rb_no.isChecked()) {
                        for (int i = 0; i < questionsWithAnsList.size(); i++) {
                            if (outputBean.getQLevel2Id().equals(questionsWithAnsList.get(i).getQLevel2Id())) {
                                questionsWithAnsList.get(i).setIsYesNo("0");
                                break;
                            }
                        }
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mainQuesList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            private TextView tv_mainques_withsubs, tv_subques;
            private LinearLayout ll_subques;
            private RadioGroup rg_yesno;
            private RadioButton rb_yes, rb_no;


            public MyViewHolder(@NonNull View view) {
                super(view);
                tv_mainques_withsubs = view.findViewById(R.id.tv_mainques_withsubs);
                tv_subques = view.findViewById(R.id.tv_subques);
                ll_subques = view.findViewById(R.id.ll_subques);
                rg_yesno = view.findViewById(R.id.rg_yesno);
                rb_yes = view.findViewById(R.id.rb_yes);
                rb_no = view.findViewById(R.id.rb_no);
            }
        }
    }

    private class InsertQuestionnaireDetails extends AsyncTask<String, Void, String> {

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
                        .add("jsonstring", params[0])
                        .build();

                OkHttpClient client = new OkHttpClient();
                String url = ApplicationConstants.webservice + ApplicationConstants.InsertQuestionnaireDetails;
                Request request = new Request.Builder()
                        .url(url)
                        .post(formBody)
                        .build();

                okhttp3.Response response = client.newCall(request).execute();
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
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("AttendanceMarkedPatients_Activity"));
                        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                                .setIcon(R.drawable.icon_success)
                                .setTitle("Success")
                                .setMessage("Answers saved successfully")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    } else if (status.equalsIgnoreCase("fail")) {
                        Utilities.showAlertDialog(context, status, message, false);
                    }
                }
            } catch (Exception e) {
                Utilities.showAlertDialog(context, "Fail",
                        "Server not connected", false);
                e.printStackTrace();
            }
        }
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Mini Camp Questions");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
