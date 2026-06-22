package com.myhindlab.abkat.adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.CreateCamp_Activity_v3;
import com.myhindlab.abkat.models.CSCPreCampInfoForApprovalModel;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CSCCampRequestListAdapterForCC extends RecyclerView.Adapter<CSCCampRequestListAdapterForCC.CampRequestVH> {

    private Context context;
    private String userId, distLgdCode;
    private UpdateCamp updateCamp;
    boolean isRejected = false;

    public interface UpdateCamp {
        void onUpdateCamp();
    }

    private List<CSCPreCampInfoForApprovalModel.Output> cscPreCampInfoForApprovalModelList;

    public CSCCampRequestListAdapterForCC(Context context, List<CSCPreCampInfoForApprovalModel.Output> cscPreCampInfoForApprovalModelList, UpdateCamp updateCamp) {
        this.context = context;
        this.cscPreCampInfoForApprovalModelList = cscPreCampInfoForApprovalModelList;
        this.updateCamp = updateCamp;
        getSessionDetails();
    }

    @Override
    public CampRequestVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.camp_request_list_item_for_cc, parent, false);
        return new CampRequestVH(v);
    }

    private void getSessionDetails() {
        try {
            JSONArray user_info = new JSONArray(new UserSessionManager(context).getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                userId = json.getString("EmpCode");
                distLgdCode = json.getString("DISTLGDCODE");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBindViewHolder(CampRequestVH holder, int position) {

        CSCPreCampInfoForApprovalModel.Output output = cscPreCampInfoForApprovalModelList.get(position);
        holder.tvCampType.setText(String.valueOf(output.getCampTypeDescription()));
        holder.tvTaluka.setText(String.valueOf(output.getTallgdcode()));
        holder.tvCampName.setText(output.getCampName());
        holder.tvGP.setText(output.getGpName());
        holder.tvDate.setText(output.getEstimatedCampDate());
        holder.tvCampID.setText(String.valueOf(output.getCSCPreAprrovedid()));
        holder.tvContactNo.setText(output.getCSCUserMobile() + "/" + output.getCSCUserName());
//        if (output.getCampStatus() == 1) {
//            holder.tvStatus.setTextColor(context.getResources().getColor(R.color.green));
//            holder.tvStatus.setText("Approved");
//
//            holder.tvStatusNote.setVisibility(View.GONE);
//            holder.tvStatusNoteText.setVisibility(View.GONE);
//            holder.lastDivider.setVisibility(View.GONE);
//            holder.lastDivider1.setVisibility(View.GONE);
//        } else if (output.getCampStatus() == 2) {
//            holder.tvStatus.setTextColor(context.getResources().getColor(R.color.red));
//            holder.tvStatus.setText("Rejected");
//            holder.tvStatusNote.setText(output.getReason());
//
//            holder.tvStatusNote.setVisibility(View.VISIBLE);
//            holder.tvStatusNoteText.setVisibility(View.VISIBLE);
//            holder.lastDivider.setVisibility(View.VISIBLE);
//            holder.lastDivider1.setVisibility(View.VISIBLE);
//
//        } else {
//            holder.tvStatus.setTextColor(context.getResources().getColor(R.color.chartyellow));
//            holder.tvStatus.setText("Pending");
//
//            holder.tvStatusNote.setVisibility(View.GONE);
//            holder.tvStatusNoteText.setVisibility(View.GONE);
//            holder.lastDivider.setVisibility(View.GONE);
//            holder.lastDivider1.setVisibility(View.GONE);
//
//        }
//
//        if (output.getCampStatus() != 0) {
//            holder.btnApprove.setVisibility(View.GONE);
//            holder.btnReject.setVisibility(View.GONE);
//
//        } else {
//            holder.btnApprove.setVisibility(View.VISIBLE);
//            holder.btnReject.setVisibility(View.VISIBLE);
//        }

        holder.btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isRejected = false;
                Utilities.showAlertDialog(context, "Confirmation", "Do you want to create camp?", true,
                        "Create Camp", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                context.startActivity(new Intent(context, CreateCamp_Activity_v3.class).putExtra("requestDetails", output));
//                                            updateCamp.onUpdateCamp();
                            }
                        });
            }
        });
        holder.btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                View promptView = layoutInflater.inflate(R.layout.image_popup, null);
                builder.setView(promptView);
                AlertDialog dialog = builder.create();
                ImageView imageView = promptView.findViewById(R.id.imvLocation);
                ProgressBar progressBar = promptView.findViewById(R.id.progressBar);
                TextView tvLocationError = promptView.findViewById(R.id.tvLocationError);
                try {

                    progressBar.setVisibility(View.VISIBLE);
                    Picasso.with(context)
                            .load(ApplicationConstants.CSCPreCampImage + output.getLocationImagePath())
                            .fit()
                            .into(imageView, new Callback() {
                                @Override
                                public void onSuccess() {
                                    progressBar.setVisibility(View.GONE);
                                    tvLocationError.setVisibility(View.GONE);

                                }

                                @Override
                                public void onError() {
                                    // TODO Auto-generated method stub
                                    progressBar.setVisibility(View.GONE);
                                    tvLocationError.setVisibility(View.VISIBLE);


                                }
                            });

                } catch (Exception e) {
                    Log.d("TAG", "onClick: " + e.getMessage());
                }


                dialog.show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return cscPreCampInfoForApprovalModelList.size();
    }

    public static class CampRequestVH extends RecyclerView.ViewHolder {

        TextView tvTaluka, tvGP, tvCampType, tvCampName, tvCampID, tvContactNo, tvStatus, tvStatusNote, tvStatusNoteText, tvDate;
        View lastDivider, lastDivider1;
        AppCompatButton btnReject, btnApprove, btnView;


        public CampRequestVH(View itemView) {
            super(itemView);

            tvTaluka = itemView.findViewById(R.id.tvTaluka);
            tvGP = itemView.findViewById(R.id.tvGP);
            tvCampName = itemView.findViewById(R.id.tvCampName);
            tvCampID = itemView.findViewById(R.id.tvCampID);
            tvContactNo = itemView.findViewById(R.id.tvContactNo);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvStatusNote = itemView.findViewById(R.id.tvStatusNote);
            tvStatusNoteText = itemView.findViewById(R.id.tvStatusNoteText);
            lastDivider = itemView.findViewById(R.id.lastDivider);
            lastDivider1 = itemView.findViewById(R.id.lastDivider1);
            btnApprove = itemView.findViewById(R.id.btnApprove);
            btnView = itemView.findViewById(R.id.btnViewCamp);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvCampType = itemView.findViewById(R.id.tvCampType);
        }
    }

    private class CSCCampApprovalUpdate extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        CSCPreCampInfoForApprovalModel.Output output;
        boolean isRejected = false;
        String reason;

        public CSCCampApprovalUpdate(CSCPreCampInfoForApprovalModel.Output output, boolean isRejected, String reason) {
            this.output = output;
            this.isRejected = isRejected;
            this.reason = reason;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Loading...");
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            String res = "";

            List<ParamsPojo> paramsPojos = new ArrayList<>();
            paramsPojos.add(new ParamsPojo("CSCPreAprrovedid", String.valueOf(output.getCSCPreAprrovedid())));
            paramsPojos.add(new ParamsPojo("ApprovedBy", userId));
            paramsPojos.add(new ParamsPojo("CampStatus", isRejected ? "2" : "1"));
            paramsPojos.add(new ParamsPojo("Reason", reason));

            res = WebServiceCall.APICall(ApplicationConstants.CSCCampApprovalUpdate, ApplicationConstants.webservice, paramsPojos);

            return res;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.d("GETCSCPreCampInfo", "onPostExecute: " + result);
            progressDialog.dismiss();

            if (!result.equals("") || !result.equals("[]")) {

                try {

                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("success")) {
                        if (isRejected) {
                            Utilities.showAlertDialog(context, "Success", "Camp Request Rejected Successfully!", true,
                                    "Okay", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            updateCamp.onUpdateCamp();

                                        }
                                    });

                        } else {
                            updateCamp.onUpdateCamp();

//                            Utilities.showAlertDialog(context, "Success", "Camp Request Approved Successfully!", true,
//                                    "Create Camp", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialogInterface, int i) {
////                                            context.startActivity(new Intent(context, CreateCamp_Activity_v3.class).putExtra("requestDetails", output));
//                                            updateCamp.onUpdateCamp();
//
//                                        }
//                                    });

                            Utilities.showToastMessage("Camp Request Approved Successfully!", context, true);
                        }

                    } else {
                        Utilities.showAlertDialog(context, "Failed", "Camp Request failed!", false,
                                "Okay", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                        updateCamp.onUpdateCamp();
                                        dialogInterface.dismiss();

                                    }
                                });

                    }

                } catch (Exception jsonException) {
                    Log.e("TAG", "Response: " + jsonException.getMessage());
                }

            }


        }

    }

    private class InsertDivisionalManagerCampRequestApproval extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        CSCPreCampInfoForApprovalModel.Output output;
        boolean isRejected = false;
        String reason;

        public InsertDivisionalManagerCampRequestApproval(CSCPreCampInfoForApprovalModel.Output output, boolean isRejected, String reason) {
            this.output = output;
            this.isRejected = isRejected;
            this.reason = reason;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Loading...");
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            String res = "";

            List<ParamsPojo> paramsPojos = new ArrayList<>();
            paramsPojos.add(new ParamsPojo("CSCPreAprrovedid", String.valueOf(output.getCSCPreAprrovedid())));
            paramsPojos.add(new ParamsPojo("ApprovedBy", userId));
            paramsPojos.add(new ParamsPojo("CampStatus", isRejected ? "2" : "1"));
            paramsPojos.add(new ParamsPojo("Reason", reason));

            res = WebServiceCall.APICall(ApplicationConstants.Insert_DivisionalManagerCampRequestApproval, ApplicationConstants.webservice_d2d, paramsPojos);

            return res;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.d("GETCSCPreCampInfo", "onPostExecute: " + result);
            progressDialog.dismiss();

            if (!result.equals("") || !result.equals("[]")) {

                try {

                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("success")) {
                        if (isRejected) {
                            Utilities.showAlertDialog(context, "Success", "Camp Request Rejected Successfully!", true,
                                    "Okay", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            updateCamp.onUpdateCamp();

                                        }
                                    });

                        } else {
                            Utilities.showAlertDialog(context, "Success", "Camp Request Approved Successfully!", true,
                                    "Create Camp", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            context.startActivity(new Intent(context, CreateCamp_Activity_v3.class).putExtra("requestDetails", output));
//                                            updateCamp.onUpdateCamp();

                                        }
                                    });

                        }

                    } else {
                        Utilities.showAlertDialog(context, "Failed", "Camp Request failed!", false,
                                "Okay", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                        updateCamp.onUpdateCamp();
                                        dialogInterface.dismiss();

                                    }
                                });

                    }

                } catch (Exception jsonException) {
                    Log.e("TAG", "Response: " + jsonException.getMessage());
                }

            }


        }

    }

}
