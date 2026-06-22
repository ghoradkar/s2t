package com.myhindlab.abkat.appointment_confirmation.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.doortodoor.BeneficiaryVerificationActivityNew;
import com.myhindlab.abkat.activities.doortodoor.D2dTeamsNotWorkingTeamActivity;
import com.myhindlab.abkat.activities.doortodoor.ExpectedBeneficiaryRemarkActivity;
import com.myhindlab.abkat.appointment_confirmation.AppoinmentConfirmationActivity;
import com.myhindlab.abkat.appointment_confirmation.pojo.OutputItem;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.UserSessionManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class CallListTeamAdaptor extends RecyclerView.Adapter<CallListTeamAdaptor.myview> {
    List<OutputItem> outputItems;

    private int desigId;

    OnImvClick onImvClick;
    public interface OnImvClick{

        public void onImvclick(OutputItem item);




    }


    Context context;


    public CallListTeamAdaptor(Context context, List<OutputItem> outputItems ,  OnImvClick onImvClick ) {
        this.outputItems = outputItems;
        this.onImvClick = onImvClick;
        this.context = context;
        getSessionDetails();
    }

    private void getSessionDetails() {
        try {
            UserSessionManager session = new UserSessionManager(context);
            JSONArray user_info = new JSONArray(session.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                desigId = json.getInt("DESGID");
//                SubOrgId = json.getString("SubOrgId");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @NonNull
    @Override
    public myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.calllist_layout_team, parent, false);
        return new myview(view);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull myview holder, int position) {
        OutputItem item = outputItems.get(position);
        holder.setIsRecyclable(false);
//        holder.tv_SrName.setText(String.valueOf(position + 1));
        holder.tv_sr_no.setText(item.getBeneficiaryName());
        holder.tv_beniname.setText(item.getBeneficiaryName());
        holder.tv_Beneficiary_No.setText(String.valueOf(item.getBeneficiaryNo()));
        holder.tv_date.setText(String.valueOf(item.getNextRenewalDate()));
        holder.tv_dateTime.setText(String.valueOf(item.getAppoinmentDateTime()));

        int a = Integer.parseInt(item.getDependantScreeningPending());
        int b = Integer.parseInt(item.getWorkerScreeninPending());
        int c = a+b;

        holder.tv_pendingCount.setText(""+c);
        holder.tv_area.setText(String.valueOf(item.getArea()));
        holder.tv_landmark.setText(String.valueOf(item.getLandMark()));
        holder.tv_worker_screened.setText(String.valueOf(item.getIsWorkerScreened()));
        holder.tv_phleboRemark.setText(String.valueOf(item.getPhleboRemark()));


        holder.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (desigId == 92  || desigId == 29 || desigId == 136 || desigId== 108 || desigId == 139){

                }else {

                    context.startActivity(new Intent(context, AppoinmentConfirmationActivity.class)
                            .putExtra("beneficiary", item)
                            .putExtra("flag", 2)

                    );

                }

            }
        });

//        holder.cvMain.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onclicklisner.onclick(item);

//            }
//        });

//        holder.cvMain.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                context.startActivity(new Intent(context, ExpectedBeneficiaryRemarkActivity.class)
//                        .putExtra("patientDetails", item)
////                        .putExtra("campType", data.getCampType())
////                        .putExtra("divisionId", data.getDivId())
////                        .putExtra("CampCoId", data.getCampCoId())
////                        .putExtra("Lab","0")
////                        .putExtra("flag", 4)
//
//                );
//            }
//        });

//        if (item.getAssignStatusID() == 2) {
//            holder.llMain.setBackgroundColor(context.getResources().getColor(R.color.greenLight));
//        } else if (item.getAssignStatusID() == 3) {
//            holder.llMain.setBackgroundColor(context.getResources().getColor(R.color.redLight));
//        } else if (item.getAssignStatusID() >= 4) {
//            holder.llMain.setBackgroundColor(context.getResources().getColor(R.color.light_blue));
//        } else if (item.getAssignStatusID() == 1) {
//            holder.llMain.setBackgroundColor(context.getResources().getColor(R.color.greylight));
//        }else if (item.getAssignStatusID() == 1){
//            holder.llMain.setBackgroundColor(context.getResources().getColor(R.color.purpleLightColor));
//        }

        holder.iv_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onImvClick.onImvclick(item);

//                Intent intent = new Intent(Intent.ACTION_DIAL);
//                intent.setData(Uri.parse("tel:"+ item.getMobile()));
//                context.startActivity(intent);

//                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + item.getMobile()));
//                context.startActivity(intent);
            }

        });


    }

    @Override
    public int getItemCount() {
        return outputItems.size();
    }

    class myview extends RecyclerView.ViewHolder {
        TextView tv_sr_no, tv_beniname,tv_worker_screened,tv_Beneficiary_No,tv_area,tv_phleboRemark, tv_date,tv_pendingCount,tv_landmark,tv_dateTime,tv_SrName;
        ImageView iv_call,imvCall;
        TableLayout tableLayout;
        MaterialCardView cvMain;
        LinearLayoutCompat llMain;

        public myview(@NonNull View itemView) {
            super(itemView);
            tv_sr_no = itemView.findViewById(R.id.tv_sr_no);
            tv_beniname = itemView.findViewById(R.id.tv_beniname);
            tv_Beneficiary_No = itemView.findViewById(R.id.tv_Beneficiary_No);
            tv_date = itemView.findViewById(R.id.tv_date);
            iv_call = itemView.findViewById(R.id.iv_call);
            cvMain = itemView.findViewById(R.id.cvMain);
            llMain = itemView.findViewById(R.id.llMain);
            tv_dateTime = itemView.findViewById(R.id.tv_dateTime);
            tv_SrName = itemView.findViewById(R.id.tv_SrName);
            tv_pendingCount = itemView.findViewById(R.id.tv_pendingCount);
            imvCall = itemView.findViewById(R.id.imvCall);
            tv_area = itemView.findViewById(R.id.tv_area);
            tv_landmark = itemView.findViewById(R.id.tv_landmark);
            tv_worker_screened = itemView.findViewById(R.id.tv_worker_screened);
            tv_phleboRemark = itemView.findViewById(R.id.tv_phleboRemark);
        }
    }
}
