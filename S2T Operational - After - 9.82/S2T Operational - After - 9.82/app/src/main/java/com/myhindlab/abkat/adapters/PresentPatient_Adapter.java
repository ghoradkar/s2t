package com.myhindlab.abkat.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.abha.activities.ViewQueuePatientActivity;
import com.myhindlab.abkat.activities.HealthScreeningAcknowledgementConfirmation_Activity;
import com.myhindlab.abkat.activities.HealthScreeningAudioTest_Activity_New;
import com.myhindlab.abkat.activities.HealthScreeningBasicHealth_Activity;
import com.myhindlab.abkat.activities.HealthScreeningBloodSugarPP_Activity;
import com.myhindlab.abkat.activities.HealthScreeningBreast_Activity_v3;
import com.myhindlab.abkat.activities.HealthScreeningLFT_Activity;
import com.myhindlab.abkat.activities.HealthScreeningLFT__Safey_Activity;
import com.myhindlab.abkat.activities.HealthScreeningMiniCamp_Activity;
import com.myhindlab.abkat.activities.HealthScreeningPhysicalExaminationNewSecond_Activity;
import com.myhindlab.abkat.activities.HealthScreeningPhysicalExaminationNew_Activity;
import com.myhindlab.abkat.activities.HealthScreeningPhysicalExaminationNew_Mmu_Activity;
import com.myhindlab.abkat.activities.HealthScreeningPhysicalExamination_Activity;
import com.myhindlab.abkat.activities.HealthScreeningRationCard_Activity;
import com.myhindlab.abkat.activities.HealthScreeningSamplecollection_Activity;
import com.myhindlab.abkat.activities.HealthScreeningUrineSampleCollection_Activity;
import com.myhindlab.abkat.activities.HealthScreeningVisualTest_Activity;
import com.myhindlab.abkat.activities.HealthscreeningAntigenTest_Activity;
import com.myhindlab.abkat.activities.doortodoor.CallToDoctorRequestActivity;
import com.myhindlab.abkat.activities.doortodoor.D2DHealthScreeningPhysicalExamination_Activity;
import com.myhindlab.abkat.models.GetDocListD2D;
import com.myhindlab.abkat.models.PresentPatientList_Model;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PresentPatient_Adapter extends RecyclerView.Adapter<PresentPatient_Adapter.MyViewHolder> {

    private List<PresentPatientList_Model> resultArrayList;
    private Context context;


    OnImvClick onImvClick;

    public interface OnImvClick {

        public void onImvclick(PresentPatientList_Model item);

    }

    private String campId, type;
    public static int itemClickedPosition = 0;
    private ArrayList<GetDocListD2D.Output> getDocListD2DArrayList;
    private String userId, SubOrgId, distLgdCode, labCode, labCodeOne, labCodeTwo, hospitalId, imagePath = "", labCodeThree;
    private int desigId;

    public PresentPatient_Adapter(Context context, List<PresentPatientList_Model> resultArrayList, String campId, String type, OnImvClick onImvClick) {
        this.context = context;
        this.resultArrayList = resultArrayList;
        this.onImvClick = onImvClick;
        this.campId = campId;
        this.type = type;
        getSessionDetails();

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_row_presentpatient, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int pos) {
        final int position = holder.getAdapterPosition();
        final PresentPatientList_Model patientDetails = resultArrayList.get(position);

        SubOrgId = patientDetails.getIsPhy();

        holder.tv_srno.setText("" + (position + 1));



        if (new UserSessionManager(context).isHllUser()){


            if (patientDetails.getIsDependent()!=null){
                if (patientDetails.getIsDependent() == 0){
                    holder.tv_type.setText("W");
                }else if (patientDetails.getIsDependent() == 1){
                    holder.tv_type.setText("D");
                }

            }
        }else {

            if (patientDetails.getIssDependent()!=null){
                if (patientDetails.getIssDependent() == 0){
                    holder.tv_type.setText("W");
                }else if (patientDetails.getIssDependent() == 1){
                    holder.tv_type.setText("D");
                }

            }
        }



        holder.tv_patientname.setText(patientDetails.getEnglishName());
        if (type.equalsIgnoreCase("16")|type.equalsIgnoreCase("3")){

            if (patientDetails.getScreeningPatientID()!=null){
                holder.tv_cardno.setText(String.valueOf(patientDetails.getScreeningPatientID()));

            }else {

                holder.tv_cardno.setText("NA");

            }
        }else {
            holder.tv_cardno.setText(String.valueOf(patientDetails.getRegdNo()));
        }

        if (type.equalsIgnoreCase("16")) {
            if (patientDetails.getIsCall().equalsIgnoreCase("1")) {
                holder.cv_title.setBackgroundColor(context.getResources().getColor(R.color.light_green));


                //  holder.tv_cardno.setBackgroundColor(context.getResources().getColor(R.color.light_green));
            } else if (patientDetails.getIsCall().equalsIgnoreCase("0")) {
                holder.cv_title.setBackgroundColor(context.getResources().getColor(R.color.white));

            }

        }

        if (type.equalsIgnoreCase("16")) {
            holder.imvCall.setEnabled(true);
        } else {
            holder.imvCall.setEnabled(false);

        }


        holder.imvCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onImvClick.onImvclick(patientDetails);

//                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + patientDetails.getMobileNo()));
//                context.startActivity(intent);
            }

        });

//        holder.imvCall.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (Utilities.isNetworkAvailable(context))
//                    new GetServiceGroupListForRtpcr().execute();
//                else {
//                    Utilities.showToastMessage("Please Check your internet connection", context, false);
//                }
//            }
//
//        });
        if (new UserSessionManager(context).isHllUser()) {
            if ((desigId == 4 || desigId == 35 || desigId == 32) && (type.equalsIgnoreCase("3") || type.equalsIgnoreCase("16"))) {
                holder.cv_title.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        context.startActivity(new Intent(context, CallToDoctorRequestActivity.class)
                                .putExtra("patientDetails", patientDetails)
                                .putExtra("healthScreentype", type)
                                .putExtra("campId", campId));


//
//                        if (Utilities.isNetworkAvailable(context))
//                            new GetD2dDoctors().execute();
//                        else {
//                            Utilities.showToastMessage("Please Check your internet connection", context, false);
//                        }
                    }
                });
            } else {
                holder.cv_title.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemClickedPosition = position;
                        switch (type) {
                            case "2":




                                if (patientDetails.getIsAckAndRCPending() == 2){
                                    Utilities.showAlertDialog(context,"Alert","या लाभार्थ्याची रेशन कार्ड प्राप्त झालेले नसल्यामुळे बेसिक स्क्रीनिंग करता येणार नाही.\n कृपया प्रथम मागील प्रक्रिया पूर्ण करा.",false);
                                    return;
                                }

                                if (patientDetails.getIsAckAndRCPending() == 3){
                                    Utilities.showAlertDialog(context,"Alert","या लाभार्थ्याची Acknowledgment, Finger Print  प्राप्त झालेले नसल्यामुळे बेसिक स्क्रीनिंग करता येणार नाही.\n कृपया प्रथम मागील प्रक्रिया पूर्ण करा.",false);
                                    return;
                                }


                                if (patientDetails.getIsAckAndRCPending() == 4){
                                    Utilities.showAlertDialog(context,"Alert","या लाभार्थ्याची Acknowledgment, Finger Print, रेशन कार्ड प्राप्त झालेले नसल्यामुळे बेसिक स्क्रीनिंग करता येणार नाही.\n कृपया प्रथम मागील प्रक्रिया पूर्ण करा.",false);
                                    return;
                                }

                                context.startActivity(new Intent(context, HealthScreeningBasicHealth_Activity.class)
                                        .putExtra("patientDetails", patientDetails)
                                        .putExtra("healthScreentype", type)
                                        .putExtra("campId", campId));
                                break;
                            case "3":

                                if (SubOrgId != null) {
                                    if (SubOrgId.equals("1")) {
//old one
                                        context.startActivity(new Intent(context, HealthScreeningPhysicalExamination_Activity.class)
                                                .putExtra("patientDetails", patientDetails)
                                                .putExtra("healthScreentype", type)
                                                .putExtra("campId", campId));

///second
                                    }else if (SubOrgId.equals("2")){
                                        context.startActivity(new Intent(context, HealthScreeningPhysicalExaminationNewSecond_Activity.class)
                                                .putExtra("patientDetails", patientDetails)
                                                .putExtra("healthScreentype", type)
                                                .putExtra("campId", campId));

                                    }else if (SubOrgId.equals("3")){
//                                        context.startActivity(new Intent(context, HealthScreeningPhysicalExaminationNew_Activity.class)
                                        context.startActivity(new Intent(context, HealthScreeningPhysicalExaminationNew_Mmu_Activity.class)
                                                .putExtra("patientDetails", patientDetails)
                                                .putExtra("healthScreentype", type)
                                                .putExtra("campId", campId));

                                    }
                                }
                                break;

//                                case "3":
//                                context.startActivity(new Intent(context, HealthScreeningPhysicalExaminationNew_Activity.class)
//                                        .putExtra("patientDetails", patientDetails)
//                                        .putExtra("healthScreentype", type)
//                                        .putExtra("campId", campId));
//                                break;
                            case "4":
//                                context.startActivity(new Intent(context, HealthScreeningLFT__Safey_Activity.class)
//                                        .putExtra("patientDetails", patientDetails)
//                                        .putExtra("healthScreentype", type)
//                                        .putExtra("campId", campId));
//                                break;
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setCancelable(true);
                                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, R.layout.list_row);
                                builder.setTitle("Select Device");
                                builder.setMessage("Which Spirometer do you have?");
                                arrayAdapter.add("Safey");
                                // arrayAdapter.add("BOLT");
                                builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        dialogInterface.dismiss();
                                        if (i == 0) {
                                            context.startActivity(new Intent(context, HealthScreeningLFT_Activity.class)
                                                    .putExtra("patientDetails", patientDetails)
                                                    .putExtra("healthScreentype", type)
                                                    .putExtra("campId", campId));
                                        } else {
                                            if (Utilities.isBluetoothEnabled()) {
                                                context.startActivity(new Intent(context, HealthScreeningLFT__Safey_Activity.class)
                                                        .putExtra("patientDetails", patientDetails)
                                                        .putExtra("healthScreentype", type)
                                                        .putExtra("campId", campId));
                                            } else {
                                                Utilities.showAlertDialog(context, "Bluetooth Not Enabled", "Please enable Bluetooth", false);
                                            }
                                        }
                                    }
                                });
                                AlertDialog alertDialog = builder.create();
//                                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "BOLT", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                        dialogInterface.dismiss();
//                                        context.startActivity(new Intent(context, HealthScreeningLFT_Activity.class)
//                                                .putExtra("patientDetails", patientDetails)
//                                                .putExtra("healthScreentype", type)
//                                                .putExtra("campId", campId));
//
//                                    }
//                                });
                                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Safey", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        if (Utilities.isBluetoothEnabled()) {
                                            context.startActivity(new Intent(context, HealthScreeningLFT__Safey_Activity.class)
                                                    .putExtra("patientDetails", patientDetails)
                                                    .putExtra("healthScreentype", type)
                                                    .putExtra("campId", campId));
                                        } else {
                                            Utilities.showAlertDialog(context, "Bluetooth Not Enabled", "Please enable Bluetooth", false);
                                        }
                                    }
                                });
                                alertDialog.show();
                            }
                            break;
                            case "5":
                                context.startActivity(new Intent(context, HealthScreeningAudioTest_Activity_New.class)
                                        .putExtra("patientDetails", patientDetails)
                                        .putExtra("healthScreentype", type)
                                        .putExtra("campId", campId));
                                break;
                            case "6":
                                context.startActivity(new Intent(context, HealthScreeningVisualTest_Activity.class)
                                        .putExtra("patientDetails", patientDetails)
                                        .putExtra("healthScreentype", type)
                                        .putExtra("campId", campId));
                                break;
                            case "7":
                                context.startActivity(new Intent(context, HealthScreeningSamplecollection_Activity.class)
                                        .putExtra("patientDetails", patientDetails)
                                        .putExtra("healthScreentype", type)
                                        .putExtra("campId", campId));
                                break;
                            case "8":
                                context.startActivity(new Intent(context, HealthScreeningBloodSugarPP_Activity.class)
                                        .putExtra("patientDetails", patientDetails)
                                        .putExtra("healthScreentype", type)
                                        .putExtra("campId", campId));
                                break;
                            case "9":
                                context.startActivity(new Intent(context, HealthScreeningAcknowledgementConfirmation_Activity.class)
                                        .putExtra("patientDetails", patientDetails)
                                        .putExtra("healthScreentype", type)
                                        .putExtra("campId", campId));
                                break;


                                case "100":
                                context.startActivity(new Intent(context, HealthScreeningRationCard_Activity.class)
                                        .putExtra("patientDetails", patientDetails)
                                        .putExtra("healthScreentype", type)
                                        .putExtra("campId", campId));
                                break;
                            case "11":
                                if (patientDetails.getBarcode1().isEmpty() || patientDetails.getBarcode1().equalsIgnoreCase("null")) {
                                    Utilities.showAlertDialog(context, "Blood Sample Not Collected", "Please collect Blood sample first", false, "Okay", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
//                                            ((Activity) context).finish();

                                            dialogInterface.dismiss();
                                        }
                                    });
                                } else {
                                    context.startActivity(new Intent(context, HealthScreeningUrineSampleCollection_Activity.class)
                                            .putExtra("patientDetails", patientDetails)
                                            .putExtra("healthScreentype", type)
                                            .putExtra("campId", campId));
                                }
                                break;
                            case "12":
                                context.startActivity(new Intent(context, HealthScreeningMiniCamp_Activity.class)
                                        .putExtra("patientDetails", patientDetails)
                                        .putExtra("healthScreentype", type)
                                        .putExtra("campId", campId));
                                break;
                            case "13":
                                context.startActivity(new Intent(context, HealthScreeningBreast_Activity_v3.class)
                                        .putExtra("patientDetails", patientDetails)
                                        .putExtra("healthScreentype", type)
                                        .putExtra("campId", campId));
                            case "15":
                                context.startActivity(new Intent(context, HealthscreeningAntigenTest_Activity.class)
                                        .putExtra("patientDetails", patientDetails)
                                        .putExtra("healthScreentype", type)
                                        .putExtra("campId", campId));
                                break;
                            case "16":
                                if (patientDetails.getIsCall().equalsIgnoreCase("0")) {
                                    Utilities.showAlertDialog(context, "Alert", "Call to Beneficiary to open PHY. Examination form", false);
                                    return;
                                } else if (patientDetails.getIsCall().equalsIgnoreCase("1")) {

                                    if (SubOrgId != null) {
                                        if (SubOrgId.equals("1")) {

                                            context.startActivity(new Intent(context, HealthScreeningPhysicalExamination_Activity.class)
                                                    .putExtra("patientDetails", patientDetails)
                                                    .putExtra("healthScreentype", type)
                                                    .putExtra("campId", campId));


                                        }else if (SubOrgId.equals("2")){
                                            context.startActivity(new Intent(context, HealthScreeningPhysicalExaminationNewSecond_Activity.class)
                                                    .putExtra("patientDetails", patientDetails)
                                                    .putExtra("healthScreentype", type)
                                                    .putExtra("campId", campId));

                                        }else if (SubOrgId.equals("3")){
//                                            context.startActivity(new Intent(context, HealthScreeningPhysicalExaminationNew_Activity.class)
                                            context.startActivity(new Intent(context, HealthScreeningPhysicalExaminationNew_Mmu_Activity.class)
                                                    .putExtra("patientDetails", patientDetails)
                                                    .putExtra("healthScreentype", type)
                                                    .putExtra("campId", campId));

                                        }
                                    }
                                }
                                break;



                            case "0":

//                            int age = 0;
//                            try {
//                                Date parse = Utilities.dfDate.parse(patientDetails.getDob());
//                                Calendar c = Calendar.getInstance();
//                                c.setTime(parse);
//                                age = Utilities.getAge(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
//                            } catch (ParseException e) {
//                                e.printStackTrace();
//                            }

                                context.startActivity(new Intent(context, ViewQueuePatientActivity.class)
                                        .putExtra("name", patientDetails.getEnglishName())
                                        .putExtra("abhaNumber", patientDetails.getABHANumber())
                                        .putExtra("abhaAddress", patientDetails.getABHAAddress())
                                        .putExtra("gender", patientDetails.getGender())
                                        .putExtra("dob", patientDetails.getDob())
                                        .putExtra("ageInYears",patientDetails.getAge())
                                        .putExtra("mobileNum", patientDetails.getMobileNo())
                                        .putExtra("addressLine", patientDetails.getLocalAddress())
                                        .putExtra("permAddress", patientDetails.getPermanentAddress())
                                        .putExtra("token", 0)
                                        .putExtra("identityID", "")
                                        .putExtra("response", "")
                                        .putExtra("type", "ReadyOnly"));
                                break;
                        }
                    }
                });

            }

        } else {
            holder.cv_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickedPosition = position;
                    switch (type) {
                        case "2":

                            if (patientDetails.getIsAckAndRCPending() == 2){
                                Utilities.showAlertDialog(context,"Alert","या लाभार्थ्याची रेशन कार्ड प्राप्त झालेले नसल्यामुळे बेसिक स्क्रीनिंग करता येणार नाही.\n कृपया प्रथम मागील प्रक्रिया पूर्ण करा.",false);
                                return;
                            }

                            if (patientDetails.getIsAckAndRCPending() == 3){
                                Utilities.showAlertDialog(context,"Alert","या लाभार्थ्याची Acknowledgment, Finger Print  प्राप्त झालेले नसल्यामुळे बेसिक स्क्रीनिंग करता येणार नाही.\n कृपया प्रथम मागील प्रक्रिया पूर्ण करा.",false);
                                return;
                            }


                            if (patientDetails.getIsAckAndRCPending() == 4){
                                Utilities.showAlertDialog(context,"Alert","या लाभार्थ्याची Acknowledgment, Finger Print, रेशन कार्ड प्राप्त झालेले नसल्यामुळे बेसिक स्क्रीनिंग करता येणार नाही.\n कृपया प्रथम मागील प्रक्रिया पूर्ण करा.",false);
                                return;
                            }

                            context.startActivity(new Intent(context, HealthScreeningBasicHealth_Activity.class)
                                    .putExtra("patientDetails", patientDetails)
                                    .putExtra("healthScreentype", type)
                                    .putExtra("campId", campId));
                            break;


                        case "3":

//                            context.startActivity(new Intent(context, HealthScreeningPhysicalExamination_Activity.class)
//                                    .putExtra("patientDetails", patientDetails)
//                                    .putExtra("healthScreentype", type)
//                                    .putExtra("campId", campId));


                            if (SubOrgId != null) {
                                if (SubOrgId.equals("1")) {

                                    context.startActivity(new Intent(context, HealthScreeningPhysicalExamination_Activity.class)
                                            .putExtra("patientDetails", patientDetails)
                                            .putExtra("healthScreentype", type)
                                            .putExtra("campId", campId));


                                }else if (SubOrgId.equals("2")){
                                    context.startActivity(new Intent(context, HealthScreeningPhysicalExaminationNewSecond_Activity.class)
                                            .putExtra("patientDetails", patientDetails)
                                            .putExtra("healthScreentype", type)
                                            .putExtra("campId", campId));

                                }else if (SubOrgId.equals("3")){
//                                    context.startActivity(new Intent(context, HealthScreeningPhysicalExaminationNew_Activity.class)
                                    context.startActivity(new Intent(context, HealthScreeningPhysicalExaminationNew_Mmu_Activity.class)
                                            .putExtra("patientDetails", patientDetails)
                                            .putExtra("healthScreentype", type)
                                            .putExtra("campId", campId));

                                }
                            }




                            break;

//                            case "3":
//                            context.startActivity(new Intent(context, HealthScreeningPhysicalExaminationNew_Activity.class)
//                                    .putExtra("patientDetails", patientDetails)
//                                    .putExtra("healthScreentype", type)
//                                    .putExtra("campId", campId));
//                            break;

                        case "4":
//                            context.startActivity(new Intent(context, HealthScreeningLFT__Safey_Activity.class)
//                                    .putExtra("patientDetails", patientDetails)
//                                    .putExtra("healthScreentype", type)
//                                    .putExtra("campId", campId));
//                            break;
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setCancelable(true);
                            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, R.layout.list_row);
                            builder.setTitle("Select Device");
                            builder.setMessage("Which Spirometer do you have?");
                            arrayAdapter.add("Safey");
                            //   arrayAdapter.add("BOLT");
                            builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    dialogInterface.dismiss();
                                    if (i == 0) {
                                        context.startActivity(new Intent(context, HealthScreeningLFT_Activity.class)
                                                .putExtra("patientDetails", patientDetails)
                                                .putExtra("healthScreentype", type)
                                                .putExtra("campId", campId));
                                    } else {
                                        if (Utilities.isBluetoothEnabled()) {
                                            context.startActivity(new Intent(context, HealthScreeningLFT__Safey_Activity.class)
                                                    .putExtra("patientDetails", patientDetails)
                                                    .putExtra("healthScreentype", type)
                                                    .putExtra("campId", campId));
                                        } else {
                                            Utilities.showAlertDialog(context, "Bluetooth Not Enabled", "Please enable Bluetooth", false);
                                        }
                                    }
                                }
                            });
                            AlertDialog alertDialog = builder.create();
//                            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "BOLT", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    dialogInterface.dismiss();
//                                    context.startActivity(new Intent(context, HealthScreeningLFT_Activity.class)
//                                            .putExtra("patientDetails", patientDetails)
//                                            .putExtra("healthScreentype", type)
//                                            .putExtra("campId", campId));
//
//                                }
//                            });
                            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Safey", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    if (Utilities.isBluetoothEnabled()) {
                                        context.startActivity(new Intent(context, HealthScreeningLFT__Safey_Activity.class)
                                                .putExtra("patientDetails", patientDetails)
                                                .putExtra("healthScreentype", type)
                                                .putExtra("campId", campId));
                                    } else {
                                        Utilities.showAlertDialog(context, "Bluetooth Not Enabled", "Please enable Bluetooth", false);
                                    }
                                }
                            });
                            alertDialog.show();
                        }
                        break;
                        case "5":
                            context.startActivity(new Intent(context, HealthScreeningAudioTest_Activity_New.class)
                                    .putExtra("patientDetails", patientDetails)
                                    .putExtra("healthScreentype", type)
                                    .putExtra("campId", campId));
                            break;
                        case "6":
                            context.startActivity(new Intent(context, HealthScreeningVisualTest_Activity.class)
                                    .putExtra("patientDetails", patientDetails)
                                    .putExtra("healthScreentype", type)
                                    .putExtra("campId", campId));
                            break;
                        case "7":
                            context.startActivity(new Intent(context, HealthScreeningSamplecollection_Activity.class)
                                    .putExtra("patientDetails", patientDetails)
                                    .putExtra("healthScreentype", type)
                                    .putExtra("campId", campId));
                            break;
                        case "8":
                            context.startActivity(new Intent(context, HealthScreeningBloodSugarPP_Activity.class)
                                    .putExtra("patientDetails", patientDetails)
                                    .putExtra("healthScreentype", type)
                                    .putExtra("campId", campId));
                            break;
                        case "9":
                            context.startActivity(new Intent(context, HealthScreeningAcknowledgementConfirmation_Activity.class)
                                    .putExtra("patientDetails", patientDetails)
                                    .putExtra("healthScreentype", type)
                                    .putExtra("campId", campId));
                            break;



                        case "100":
                            context.startActivity(new Intent(context, HealthScreeningRationCard_Activity.class)
                                    .putExtra("patientDetails", patientDetails)
                                    .putExtra("healthScreentype", type)
                                    .putExtra("campId", campId));
                            break;
                        case "11":
                            if (patientDetails.getBarcode1().isEmpty() || patientDetails.getBarcode1().equalsIgnoreCase("null")) {
                                Utilities.showAlertDialog(context, "Blood Sample Not Collected", "Please collect Blood sample first", false, "Okay", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        ((Activity) context).finish();
                                    }
                                });
                            } else {
                                context.startActivity(new Intent(context, HealthScreeningUrineSampleCollection_Activity.class)
                                        .putExtra("patientDetails", patientDetails)
                                        .putExtra("healthScreentype", type)
                                        .putExtra("campId", campId));
                            }
                            break;
                        case "12":
                            context.startActivity(new Intent(context, HealthScreeningMiniCamp_Activity.class)
                                    .putExtra("patientDetails", patientDetails)
                                    .putExtra("healthScreentype", type)
                                    .putExtra("campId", campId));
                            break;
                        case "13":
                            context.startActivity(new Intent(context, HealthScreeningBreast_Activity_v3.class)
                                    .putExtra("patientDetails", patientDetails)
                                    .putExtra("healthScreentype", type)
                                    .putExtra("campId", campId));
                        case "15":
                            context.startActivity(new Intent(context, HealthscreeningAntigenTest_Activity.class)
                                    .putExtra("patientDetails", patientDetails)
                                    .putExtra("healthScreentype", type)
                                    .putExtra("campId", campId));
                            break;
//                        case "16":
//                            context.startActivity(new Intent(context, D2DHealthScreeningPhysicalExamination_Activity.class)
//                                    .putExtra("patientDetails", patientDetails)
//                                    .putExtra("healthScreentype", type)
//                                    .putExtra("campId", campId));
//                            break;

                        case "16":
//                            context.startActivity(new Intent(context, HealthScreeningPhysicalExaminationNew_Activity.class)
                            context.startActivity(new Intent(context, HealthScreeningPhysicalExaminationNew_Mmu_Activity.class)
                                    .putExtra("patientDetails", patientDetails)
                                    .putExtra("healthScreentype", type)
                                    .putExtra("campId", campId));
                            break;



                        case "0":

//                            int age = 0;
//                            try {
//                                Date parse = Utilities.dfDate.parse(patientDetails.getDob());
//                                Calendar c = Calendar.getInstance();
//                                c.setTime(parse);
//                                age = Utilities.getAge(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
//                            } catch (ParseException e) {
//                                e.printStackTrace();
//                            }

                            context.startActivity(new Intent(context, ViewQueuePatientActivity.class)
                                    .putExtra("name", patientDetails.getEnglishName())
                                    .putExtra("abhaNumber", patientDetails.getABHANumber())
                                    .putExtra("abhaAddress", patientDetails.getABHAAddress())
                                    .putExtra("gender", patientDetails.getGender())
                                    .putExtra("dob", patientDetails.getDob())
                                    .putExtra("ageInYears",patientDetails.getAge())
                                    .putExtra("mobileNum", patientDetails.getMobileNo())
                                    .putExtra("addressLine", patientDetails.getLocalAddress())
                                    .putExtra("permAddress", patientDetails.getPermanentAddress())
                                    .putExtra("token", 0)
                                    .putExtra("identityID", "")
                                    .putExtra("response", "")
                                    .putExtra("type", "ReadyOnly"));
                            break;
                    }
                }
            });

        }

    }

    private void getSessionDetails() {
        try {
            UserSessionManager session = new UserSessionManager(context);
            JSONArray user_info = new JSONArray(session.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                userId = json.getString("EmpCode");
                distLgdCode = json.getString("DISTLGDCODE");
                desigId = json.getInt("DESGID");
//                SubOrgId = json.getString("SubOrgId");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void voiceCall(String id) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);

        intent.setDataAndType(Uri.parse("content://com.android.contacts/data/" + id),
                "vnd.android.cursor.item/vnd.com.whatsapp.voip.call");
        intent.setPackage("com.whatsapp");

        context.startActivity(intent);

    }

    public void videoCall(String id) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);

        intent.setDataAndType(Uri.parse("content://com.android.contacts/data/" + id),
                "vnd.android.cursor.item/vnd.com.whatsapp.video.call");
        intent.setPackage("com.whatsapp");

        context.startActivity(intent);

    }

    @Override
    public int getItemCount() {
        return resultArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private CardView cv_title;
        private TextView tv_srno, tv_patientname, tv_cardno,tv_type;
        private ImageView imvCall;

        public MyViewHolder(View view) {
            super(view);
            cv_title = view.findViewById(R.id.cv_title);
            tv_srno = view.findViewById(R.id.tv_srno);
            tv_patientname = view.findViewById(R.id.tv_patientname);
            tv_cardno = view.findViewById(R.id.tv_cardno);
            imvCall = view.findViewById(R.id.imvCall);
            tv_type = view.findViewById(R.id.tv_type);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private class GetD2dDoctors extends AsyncTask<String, Void, String> {

        ProgressDialog pd = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Please wait . . . ");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "";
            List<ParamsPojo> param = new ArrayList<>();
            param.add(new ParamsPojo("DocUserid", "0"));
            res = WebServiceCall.APICall(ApplicationConstants.GetDoctorListCSCCampAvailaible, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                pd.dismiss();
                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
                    GetDocListD2D pojoDetails = new Gson().fromJson(result, GetDocListD2D.class);
                    String status = pojoDetails.getStatus();
                    String message = pojoDetails.getMessage();
                    if (status.equalsIgnoreCase("success")) {
                        getDocListD2DArrayList = pojoDetails.getOutput();
                        if (getDocListD2DArrayList.size() > 0) {
                            showServiceGroupDialogThree();
                        }
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


    private void showServiceGroupDialogThree() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_check_list, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        builder.setTitle("Available Doctors");
        builder.setCancelable(false);

        RecyclerView rv_checklist = view.findViewById(R.id.rv_checklist);
        rv_checklist.setLayoutManager(new LinearLayoutManager(context));
        rv_checklist.setAdapter(new GetD2DDocAdapter());

        builder.setPositiveButton("Cancel", (dialog, which) -> {
            dialog.dismiss();

        });

        builder.create().show();
    }

    private class GetD2DDocAdapter extends RecyclerView.Adapter<GetD2DDocAdapter.MyViewHolder> {

        @NonNull
        @Override
        public GetD2DDocAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.d2d_doctor_list_item, parent, false);
            return new GetD2DDocAdapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull GetD2DDocAdapter.MyViewHolder holder, int pos) {
            final int position = holder.getAdapterPosition();

            holder.tvDocName.setText(getDocListD2DArrayList.get(position).getDoctorName());

            holder.listItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    imv_whatsapp.setOnClickListener(v -> {
                    Toast.makeText(context, "Calling..", Toast.LENGTH_SHORT).show();
                    String URL = "https://wa.me/" + "91" + getDocListD2DArrayList.get(position).getMobno();
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(URL)));
//                    });

//                    if (!contactExists(context, getDocListD2DArrayList.get(position).getMobno())) {
//                        addContact(getDocListD2DArrayList.get(position).getDoctorName(), getDocListD2DArrayList.get(position).getDoctorName(),
//                                getDocListD2DArrayList.get(position).getMobno(), "Doctor", getDocListD2DArrayList.get(position).getDoctorName() + "@gmail.com");
//
//                        Toast.makeText(context, "Not Exist Adding", Toast.LENGTH_SHORT).show();
//                    } else {
//
////                        String contactid26 = null;
////                        String contactName="";
////
////                        ContentResolver contentResolver = context.getContentResolver();
////
////                        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(getDocListD2DArrayList.get(position).getMobno()));
////
////                        Cursor cursor =
////                                contentResolver.query(
////                                        uri,
////                                        new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup._ID},
////                                        null,
////                                        null,
////                                        null);
////
////                        if (cursor != null) {
////                            while (cursor.moveToNext()) {
////
////                                contactName = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup.DISPLAY_NAME));
////                                contactid26 = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup._ID));
////
////                            }
////                            cursor.close();
////                        }
////                        if (contactid26 == null) {
////                            Log.e("WA Call", "Contact Not Found");
//////                            Toast.makeText(DetailedCallHistory.this, "No contact found associated with this number", Toast.LENGTH_SHORT).show();
////                        } else {
////                            Toast.makeText(context, "Calling..."+contactid26+" "+contactName, Toast.LENGTH_SHORT).show();
////                            Log.d("Video Call Id",contactid26);
////                            voiceCall(contactid26);
//////                            Intent intent_contacts = new Intent(Intent.ACTION_VIEW, Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(contactid26)));
//////                            //Intent intent_contacts = new Intent(Intent.ACTION_VIEW, Uri.parse("content://contacts/people/" + contactid26));
//////                            startActivity(intent_contacts);
////                        }
//
////                     here is how to make a projection. you have to use an array. My example only returns the ID, Name of Contact and Mimetype.
//                        String displayName = "";
//                        String[] projection = new String[]{ContactsContract.Data._ID, ContactsContract.Data.DISPLAY_NAME, ContactsContract.Data.MIMETYPE};
//
//                        ContentResolver resolver = context.getContentResolver();
//                        Cursor cursor = resolver.query(
//                                ContactsContract.Data.CONTENT_URI,
//                                projection, null, null,
//                                ContactsContract.Contacts.DISPLAY_NAME);
//                        String voiceCallID = "";
//                        String videoCallID = "";
//                        while (cursor.moveToNext()) {
//                            long _id = cursor.getLong(cursor.getColumnIndex(ContactsContract.Data._ID));
//
//                            displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
//                            String mimeType = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.MIMETYPE));
//
//                            if (mimeType.equals("vnd.android.cursor.item/vnd.com.whatsapp.voip.call") || mimeType.equals("vnd.android.cursor.item/vnd.com.whatsapp.video.call")) {
//                                // store in database
//
//                                if (mimeType.equals("vnd.android.cursor.item/vnd.com.whatsapp.voip.call")) {
//                                    if (displayName.contains(getDocListD2DArrayList.get(position).getDoctorName())) {
//                                        voiceCallID = Long.toString(_id);
//                                        break;
//                                    }
//
//                                } else {
//
//                                    if (displayName.contains(getDocListD2DArrayList.get(position).getDoctorName())) {
//                                        videoCallID = Long.toString(_id);
//                                        break;
//                                    }
//                                }
//
//                            }
//                            videoCallID = Long.toString(_id);
//
//
//                        }
//
//
//                        Toast.makeText(context, "Calling" + displayName + videoCallID, Toast.LENGTH_SHORT).show();
//                        videoCall(videoCallID);
//                    }

                }
            });

        }

        @Override
        public int getItemCount() {
            return getDocListD2DArrayList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            private TextView tvDocName;
            private ConstraintLayout listItem;

            public MyViewHolder(@NonNull View view) {
                super(view);
                tvDocName = view.findViewById(R.id.tvDocName);
                listItem = view.findViewById(R.id.listItem);
            }
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }
    }

    public boolean contactExists(Context context, String number) {
        // number is the phone number
        Uri lookupUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
        String[] mPhoneNumberProjection = {ContactsContract.PhoneLookup._ID, ContactsContract.PhoneLookup.NUMBER, ContactsContract.PhoneLookup.DISPLAY_NAME};
        Cursor cur = context.getContentResolver().query(lookupUri, mPhoneNumberProjection, null, null, null);
        try {
            if (cur.moveToFirst()) {
                cur.close();
                return true;
            }
        } finally {
            if (cur != null)
                cur.close();
        }


        return false;
    }

    private void addContact(String given_name, String name, String mobile, String home, String email) {


        ArrayList<ContentProviderOperation> contact = new ArrayList<ContentProviderOperation>();
        contact.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        // first and last names
        contact.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.RawContacts.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.RawContacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, given_name)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME, name)
                .build());

        // Contact No Mobile
        contact.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.RawContacts.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, mobile)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                .build());
//Contact Whatsapp
//        contact.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
//                .withValueBackReference(ContactsContract.RawContacts.Data.RAW_CONTACT_ID, 0)
//                .withValue(ContactsContract.Data.MIMETYPE, "vnd.android.cursor.item/vnd.com.whatsapp.video.call")
//                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, mobile)
//                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
//                .build());

        // Contact Home
        contact.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.RawContacts.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, home)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
                .build());

        // Email    `
        contact.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.RawContacts.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Email.DATA, email)
                .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                .build());

        try {
            ContentProviderResult[] results = context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, contact);


//                     here is how to make a projection. you have to use an array. My example only returns the ID, Name of Contact and Mimetype.

//            String[] projection = new String[]{ContactsContract.Data._ID, ContactsContract.Data.DISPLAY_NAME, ContactsContract.Data.MIMETYPE};
//
//            ContentResolver resolver = context.getContentResolver();
//            Cursor cursor = resolver.query(
//                    ContactsContract.Data.CONTENT_URI,
//                    projection, null, null,
//                    ContactsContract.Contacts.DISPLAY_NAME);
//            String voiceCallID = "";
//            String videoCallID = "";
//            while (cursor.moveToNext()) {
//                long _id = cursor.getLong(cursor.getColumnIndex(ContactsContract.Data._ID));
//                String displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
//                String mimeType = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.MIMETYPE));
//
//                if (mimeType.equals("vnd.android.cursor.item/vnd.com.whatsapp.voip.call") || mimeType.equals("vnd.android.cursor.item/vnd.com.whatsapp.video.call")) {
//                    // store in database
//
//                    if (mimeType.equals("vnd.android.cursor.item/vnd.com.whatsapp.voip.call")) {
//                        voiceCallID = Long.toString(_id);
//
//                    } else {
//
//                        videoCallID = Long.toString(_id);
//                    }
//
//                }
//
//
//            }


//            videoCall(videoCallID);


            String contactid26 = null;

            ContentResolver contentResolver = context.getContentResolver();

            Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(mobile));

            Cursor cursor =
                    contentResolver.query(
                            uri,
                            new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup._ID},
                            null,
                            null,
                            null);

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String contactName = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup.DISPLAY_NAME));
                    contactid26 = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup._ID));

                }
                cursor.close();
            }
            if (contactid26 == null) {
                Log.e("WA Call", "Contact Not Found");
//                            Toast.makeText(DetailedCallHistory.this, "No contact found associated with this number", Toast.LENGTH_SHORT).show();
            } else {
                videoCall(contactid26);
//                            Intent intent_contacts = new Intent(Intent.ACTION_VIEW, Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(contactid26)));
//                            //Intent intent_contacts = new Intent(Intent.ACTION_VIEW, Uri.parse("content://contacts/people/" + contactid26));
//                            startActivity(intent_contacts);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
