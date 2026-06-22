package com.myhindlab.abkat.adapters.couriermodule;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.couriermodule.ReceivedCourierDetails_Activity;
import com.myhindlab.abkat.models.couriermodule.ReceivedCourierModel;
import com.myhindlab.abkat.utilities.Utilities;

import java.util.List;

public class ReceivedCourierAdapter extends RecyclerView.Adapter<ReceivedCourierAdapter.MyViewHolder> {
    private List<ReceivedCourierModel> resultArrayList;
    private Context context;
    String userId, desgId, labcode, fromDate, toDate, fromLab;

    public ReceivedCourierAdapter(List<ReceivedCourierModel> resultArrayList, Context context, String userId, String desgId, String labcode, String fromDate, String toDate) {
        this.resultArrayList = resultArrayList;
        this.context = context;
        this.userId = userId;
        this.desgId = desgId;
        this.labcode = labcode;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_received_courier, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int pos) {
        final int position = holder.getAdapterPosition();
        ReceivedCourierModel courierModel = resultArrayList.get(position);
        holder.txt_from_date.setText(courierModel.getCourierDate());
        holder.txt_from_lab.setText(courierModel.getSentByLab());
        holder.tv_sample_count.setText(courierModel.getSampleCount());
        holder.txt_sample_type.setText(courierModel.getSampleType());
        holder.txt_sender_name.setText(courierModel.getSenderName());
        if (courierModel.getCourierStatus().equalsIgnoreCase("Courier Accepted"))
            holder.row_accept.setVisibility(View.VISIBLE);
        holder.ll_mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ReceivedCourierDetails_Activity.class);
                intent.putExtra("CourierModel", courierModel);
                intent.putExtra("type", "received");
                intent.putExtra("userId", userId);
                intent.putExtra("desgId", desgId);
                intent.putExtra("labcode", labcode);
                intent.putExtra("fromDate", fromDate);
                intent.putExtra("toDate", toDate);
                context.startActivity(intent);
            }
        });
        holder.imv_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {
                    context.startActivity(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", context.getPackageName(), null)));
                    Utilities.showMessageString("Please provide permission for making call", context);

                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setTitle("Alert");
                    alertDialogBuilder.setMessage("Are you sure, You want to make a call?");
                    alertDialogBuilder.setCancelable(true);
                    alertDialogBuilder.setPositiveButton(
                            "Yes", new DialogInterface.OnClickListener() {
                                @SuppressLint("MissingPermission")
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    ((Activity) context).startActivity(new Intent(Intent.ACTION_CALL,
                                            Uri.parse("tel:" + resultArrayList.get(position).getSenderMobNo())));
                                }
                            });
                    alertDialogBuilder.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert11 = alertDialogBuilder.create();
                    alert11.show();
                }
            }
        });
    }

    @Override

    public int getItemCount() {
        return resultArrayList.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_from_date, txt_from_lab, txt_sample_type, tv_sample_count, txt_sender_name;
        private ImageView imv_call;
        public LinearLayout ll_mainLayout;
        public TableRow row_accept;

        private MyViewHolder(View view) {
            super(view);
            txt_from_date = view.findViewById(R.id.txt_from_date);
            txt_from_lab = view.findViewById(R.id.txt_from_lab);
            txt_sample_type = view.findViewById(R.id.txt_sample_type);
            tv_sample_count = view.findViewById(R.id.tv_sample_count);
            ll_mainLayout = view.findViewById(R.id.main_layout);
            row_accept = view.findViewById(R.id.table_row_accept_courier);
            txt_sender_name = view.findViewById(R.id.txt_sender_name);
            imv_call = view.findViewById(R.id.imv_call);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
