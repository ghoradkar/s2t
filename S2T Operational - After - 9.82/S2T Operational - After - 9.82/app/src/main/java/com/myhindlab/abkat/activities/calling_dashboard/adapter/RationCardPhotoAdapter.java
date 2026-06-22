package com.myhindlab.abkat.activities.calling_dashboard.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.calling_dashboard.model.CallingDashboardModel;
import com.myhindlab.abkat.models.RationCardPhotoModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
public class RationCardPhotoAdapter
        extends RecyclerView.Adapter<RationCardPhotoAdapter.ViewHolder> {

    Context context;
    ArrayList<RationCardPhotoModel> list;

    public RationCardPhotoAdapter(
            Context context,
            ArrayList<RationCardPhotoModel> list
    ) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.list_row_ration_card,
                        parent,
                        false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position
    ) {

        RationCardPhotoModel model = list.get(position);

        // Image Name
        holder.txtName.setText(model.getImageName());

        // Image Preview
        Bitmap bitmap =
                BitmapFactory.decodeFile(model.getImagePath());

        holder.imageView.setImageBitmap(bitmap);


        holder.imageView.setOnClickListener(v -> {

            showFullImageDialog(model.getImagePath());

        });

        // Delete
        holder.txtDelete.setOnClickListener(v -> {

            int adapterPosition = holder.getAdapterPosition();

            if (adapterPosition != RecyclerView.NO_POSITION
                    && adapterPosition < list.size()) {

                File file = new File(
                        list.get(adapterPosition).getImagePath()
                );

                if (file.exists()) {
                    file.delete();
                }

                list.remove(adapterPosition);

                notifyItemRemoved(adapterPosition);

                notifyItemRangeChanged(
                        adapterPosition,
                        list.size()
                );

                Toast.makeText(
                        context,
                        "Image Deleted",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView txtName, txtDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            txtName = itemView.findViewById(R.id.txtName);
            txtDelete = itemView.findViewById(R.id.txtDelete);
        }
    }



    private void showFullImageDialog(String imagePath) {

        Dialog dialog = new Dialog(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.dialog_full_image);

        ImageView imageView =
                dialog.findViewById(R.id.fullImageView);

        ImageView btnClose =
                dialog.findViewById(R.id.btnClose);

        Bitmap bitmap =
                BitmapFactory.decodeFile(imagePath);

        imageView.setImageBitmap(bitmap);

        btnClose.setOnClickListener(v -> dialog.dismiss());

        dialog.show();

        Window window = dialog.getWindow();

        if (window != null) {

            DisplayMetrics metrics = new DisplayMetrics();

            ((Activity) context).getWindowManager()
                    .getDefaultDisplay()
                    .getMetrics(metrics);

            int width = (int) (metrics.widthPixels * 0.95);
            int height = (int) (metrics.heightPixels * 0.45);

            window.setLayout(width, height);

            window.setBackgroundDrawable(
                    new ColorDrawable(Color.TRANSPARENT)
            );
        }
    }
}