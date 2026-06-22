package com.myhindlab.abkat.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.pojos.GetAttandanceReportUserwiseOutPut_Pojo;

import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.MyViewHolder> {

    private List<GetAttandanceReportUserwiseOutPut_Pojo> resultArrayList;
    private Context context;
    private int[] colorArray;

    public UserListAdapter(Context context, List<GetAttandanceReportUserwiseOutPut_Pojo> resultArrayList) {
        this.context = context;
        this.resultArrayList = resultArrayList;
        this.colorArray = colorArray;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_row_teamdrilldown, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int pos) {
        final int position = holder.getAdapterPosition();
        GetAttandanceReportUserwiseOutPut_Pojo desigDetail = resultArrayList.get(position);
//        String facilityCount = NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(desigDetail.getUSERS_COUNT()));
        holder.tv_districtname.setText(desigDetail.getDISTNAME());
        holder.tv_resourcename.setText(desigDetail.getFullName());
        holder.tv_status.setText(desigDetail.getATTENDANCE());
      //  holder.tv_designation.setText(desigDetail.getDesgName());

        holder.img_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+desigDetail.getMOBNO()));
                context.startActivity(intent);
            }
        });

//        holder.cv_innercardview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, ResourceUserwise_Activity.class);
//                intent.putExtra("DESIGID", desigDetail.getDESGID());
//                intent.putExtra("DesigName", desigDetail.getDesgName());
//                intent.putExtra("DISTLGDCODE", desigDetail.getDISTLGDCODE());
//                context.startActivity(intent);
//
//            }
//        });

//        holder.cv_facility.setCardBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
    }

    @Override
    public int getItemCount() {
        return resultArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private CardView cv_innercardview;
        private TextView tv_resourcename,tv_districtname,tv_status,tv_designation;
        private ImageView img_call;

        private MyViewHolder(View view) {
            super(view);
            tv_resourcename = view.findViewById(R.id.tv_resourcename);
            tv_districtname = view.findViewById(R.id.tv_districtname);
            tv_resourcename = view.findViewById(R.id.tv_resourcename);
            tv_designation = view.findViewById(R.id.tv_designation);
            tv_status = view.findViewById(R.id.tv_status);
            img_call = view.findViewById(R.id.img_call);

        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}

