package com.myhindlab.abkat.activities.payout.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.se.omapi.Session;
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
import com.myhindlab.abkat.activities.doortodoor.D2DSelectCampActivity;
import com.myhindlab.abkat.activities.payout.InvoiceGenModel;
import com.myhindlab.abkat.activities.payout.RaiseRequestActivity;
import com.myhindlab.abkat.activities.payout.RaiseRequestForDoctorActivity;
import com.myhindlab.abkat.appointment_confirmation.pojo.OutputItem;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.UserSessionManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.myview> {
    List<InvoiceGenModel.Output> invoiceList;
    onTouchListner onTouchListner;
    Context context;

    private int desigId;



    public interface onTouchListner {
        public void onDataClick(InvoiceGenModel.Output item);


    }

    public InvoiceAdapter(Context context, List<InvoiceGenModel.Output> invoiceList, onTouchListner onTouchListner) {
        this.invoiceList = invoiceList;
        this.onTouchListner = onTouchListner;
        this.context = context;
        getSessionDetails();

    }

    @NonNull
    @Override
    public myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_payout_invoice, parent, false);
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
        InvoiceGenModel.Output item = invoiceList.get(position);
        holder.setIsRecyclable(false);
//        holder.tv_SrName.setText(String.valueOf(position + 1));
        holder.tv_invoiceMonth.setText("" + item.getInvoiceMonth());
        holder.tv_serviceDays.setText("" + item.getServiceDays());
        holder.tv_billableBeneficiary.setText("" + item.getTotalIndividualBillable());
//        holder.tv_inVoice.setText(""+item.getTotalIndividualBillable());


        if (item.getInvoiceStatus().equalsIgnoreCase("Pending")) {
            holder.tv_inVoice.setText("Pending");
            holder.tv_inVoice.setTextColor(Color.parseColor("#BF0107"));
        } else if (item.getInvoiceStatus().equalsIgnoreCase("Raise")) {
            holder.tv_inVoice.setText("Raise");
            holder.tv_inVoice.setTextColor(Color.parseColor("#FFFFFF"));
            holder.tv_inVoice.setBackgroundColor(Color.parseColor("#8B60C9"));

        } else if (item.getInvoiceStatus().equalsIgnoreCase("VIEW")) {

            holder.tv_inVoice.setText("View");
            if (item.getInvoiceApprovedStatus().equalsIgnoreCase("1")){
                holder.tv_inVoice.setTextColor(Color.parseColor("#00B050"));

            }else if (item.getInvoiceApprovedStatus().equalsIgnoreCase("2")){
                holder.tv_inVoice.setTextColor(Color.parseColor("#0096FF"));

            }
//            holder.tv_inVoice.setBackgroundColor(Color.parseColor("#2E76B5"));

        }







        holder.tv_invoiceMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (desigId == 34){
                    context.startActivity(new Intent(context, RaiseRequestForDoctorActivity.class)
                            .putExtra("month", item.getMonth())
                            .putExtra("year", item.getInvoiceYear())
                            .putExtra("userInvoice", item.getUserInviceID())
                            .putExtra("isSendForVerification", item.getSendForVerification())
                            .putExtra("raisedStatus", item.getInvoiceApprovedStatus())

                    );

                }else{

                    context.startActivity(new Intent(context, RaiseRequestActivity.class)
                            .putExtra("month", item.getMonth())
                            .putExtra("year", item.getInvoiceYear())
                            .putExtra("userInvoice", item.getUserInviceID())
                            .putExtra("isSendForVerification", item.getSendForVerification())
                            .putExtra("raisedStatus", item.getInvoiceApprovedStatus())

                    );

                }


            }
        });

        holder.tv_inVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (item.getInvoiceStatus().equalsIgnoreCase("Raise")) {
//                    context.startActivity(new Intent(context, RaiseRequestActivity.class)
//                            .putExtra("month", item.getMonth())
//                            .putExtra("year", item.getInvoiceYear())
//                            .putExtra("userInvoice", item.getUserInviceID())
//                            .putExtra("isSendForVerification", item.getSendForVerification())
//                            .putExtra("raisedStatus", item.getInvoiceApprovedStatus())
//
//                    );



                    if (desigId == 34){
                        context.startActivity(new Intent(context, RaiseRequestForDoctorActivity.class)
                                .putExtra("month", item.getMonth())
                                .putExtra("year", item.getInvoiceYear())
                                .putExtra("userInvoice", item.getUserInviceID())
                                .putExtra("isSendForVerification", item.getSendForVerification())
                                .putExtra("raisedStatus", item.getInvoiceApprovedStatus())

                        );

                    }else{

                        context.startActivity(new Intent(context, RaiseRequestActivity.class)
                                .putExtra("month", item.getMonth())
                                .putExtra("year", item.getInvoiceYear())
                                .putExtra("userInvoice", item.getUserInviceID())
                                .putExtra("isSendForVerification", item.getSendForVerification())
                                .putExtra("raisedStatus", item.getInvoiceApprovedStatus())

                        );

                    }

                }else if (item.getInvoiceStatus().equalsIgnoreCase("VIEW")){
                    String url = item.getInvoiceUrl();

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    intent.setPackage("com.android.chrome");

                    // Check if Chrome is installed
                    if (intent.resolveActivity(context.getPackageManager()) != null) {
                        context.startActivity(intent);
                    } else {
                        // Fallback to default browser
                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    }
                }

            }
        });


//        holder.tv_inVoice.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onTouchListner.onDataClick(item);
//            }
//        });

//        if (item.getGroupID().equalsIgnoreCase("2")) {
//            holder.llMain.setBackgroundColor(context.getResources().getColor(R.color.greenLight));
//            holder.ll_appointmentDate.setVisibility(View.VISIBLE);
////            holder.ll_appointmentTime.setVisibility(View.VISIBLE);
//        } else if (item.getGroupID().equalsIgnoreCase("3")) {
//            holder.llMain.setBackgroundColor(context.getResources().getColor(R.color.redLight));
//        } else if (item.getGroupID().equalsIgnoreCase("4")) {
//            holder.llMain.setBackgroundColor(context.getResources().getColor(R.color.light_blue));
//        } else if (item.getGroupID().equalsIgnoreCase("1")) {
//            holder.llMain.setBackgroundColor(context.getResources().getColor(R.color.greylight));
//        }else if (item.getGroupID().equalsIgnoreCase("6")){
//            holder.llMain.setBackgroundColor(context.getResources().getColor(R.color.light_yellow));
//        }
//        if (item.getGroupID().equalsIgnoreCase("5") ){
//            holder.llMain.setBackgroundColor(context.getResources().getColor(R.color.purpleLightColor));
//            holder.ll_appointmentDate.setVisibility(View.VISIBLE);
////            holder.ll_appointmentTime.setVisibility(View.VISIBLE);
//        } else if (item.getGroupID().equalsIgnoreCase("7")) {
//            holder.llMain.setBackgroundColor(context.getResources().getColor(R.color.light_orange));
//            holder.ll_appointmentDate.setVisibility(View.GONE);
//        }
//        if (item.getIsWorkerScreened()!=null){
//            if (item.getIsWorkerScreened().equalsIgnoreCase("NO")){
//                holder.ll_NewImvCall.setVisibility(View.GONE);
//            } else {
//                holder.ll_ImvCall.setVisibility(View.VISIBLE);
//            }
//            if (item.getIsWorkerScreened().equalsIgnoreCase("YES")){
//                holder.ll_NewImvCall.setVisibility(View.VISIBLE);
//
//            }else {
//                holder.ll_ImvCall.setVisibility(View.GONE);
//            }
//        }


    }


    private void getSessionDetails() {
        try {
            UserSessionManager session = new UserSessionManager(context);
            JSONArray user_info = new JSONArray(session.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                desigId = json.getInt("DESGID");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return invoiceList.size();
    }

    class myview extends RecyclerView.ViewHolder {
        TextView tv_invoiceMonth, tv_serviceDays, tv_billableBeneficiary, tv_inVoice;


        public myview(@NonNull View itemView) {
            super(itemView);

            tv_invoiceMonth = itemView.findViewById(R.id.tv_invoiceMonth);
            tv_serviceDays = itemView.findViewById(R.id.tv_serviceDays);
            tv_billableBeneficiary = itemView.findViewById(R.id.tv_billableBeneficiary);
            tv_inVoice = itemView.findViewById(R.id.tv_inVoice);


        }
    }
}
