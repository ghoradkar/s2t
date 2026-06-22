package com.myhindlab.abkat.expense_module.adapters;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.expense_module.models.ExpenseFileListModel;
import java.util.ArrayList;

public class ExpenseFileListAdapter extends RecyclerView.Adapter<ExpenseFileListAdapter.PostCampFileViewHolder> {
    private ArrayList<ExpenseFileListModel> expenseFileListModels;
    private ExpenseFileListEvents expenseFileListEvents;

    public interface ExpenseFileListEvents {
        void onDelete(ExpenseFileListModel postCampFileListModel, int pos);
    }

    public ExpenseFileListAdapter(ArrayList<ExpenseFileListModel> expenseFileListModels, ExpenseFileListEvents expenseFileListEvents) {
        this.expenseFileListModels = expenseFileListModels;
        this.expenseFileListEvents = expenseFileListEvents;
    }

    @NonNull
    @Override
    public PostCampFileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_camp_file_list_item, parent, false);

        return new PostCampFileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostCampFileViewHolder holder, int position) {
        ExpenseFileListModel postCampFile = expenseFileListModels.get(holder.getAbsoluteAdapterPosition());

        holder.tvFileName.setText(postCampFile.getFileName());
        holder.tvFilePath.setText(postCampFile.getFilePath());

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Confirmation");
                builder.setMessage("Are you sure, you want to delete " + postCampFile.getFileName() + "?");
                builder.setCancelable(false);
                AlertDialog alertDialog = builder.create();
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        expenseFileListEvents.onDelete(postCampFile, holder.getAbsoluteAdapterPosition());
                        notifyItemRemoved(holder.getAbsoluteAdapterPosition());
                        notifyItemRangeChanged(holder.getAbsoluteAdapterPosition(), expenseFileListModels.size());
                        dialog.dismiss();
                    }
                });
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alertDialog.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return expenseFileListModels.size();
    }

    class PostCampFileViewHolder extends RecyclerView.ViewHolder {
        private TextView tvFileName, tvFilePath;
        private Button btnDelete;

        public PostCampFileViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFileName = itemView.findViewById(R.id.tvFileName);
            tvFilePath = itemView.findViewById(R.id.tvFilePath);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
