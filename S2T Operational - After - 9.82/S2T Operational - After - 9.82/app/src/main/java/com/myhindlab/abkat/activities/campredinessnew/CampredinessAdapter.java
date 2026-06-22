package com.myhindlab.abkat.activities.campredinessnew;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myhindlab.abkat.R;

import java.util.List;

public class CampredinessAdapter extends RecyclerView.Adapter<CampredinessAdapter.myview> {
    private List<OutputItem> outputItems;
    private int startPosition = 0;
    private static final int ITEMS_PER_PAGE = 15;
    RBOnclicklisner rbOnclicklisner;

    public interface RBOnclicklisner {
        void onclickphone(OutputItem OutputItem);
    }

    public CampredinessAdapter(List<OutputItem> outputItems, RBOnclicklisner rbOnclicklisner) {
        this.outputItems = outputItems;
        this.rbOnclicklisner = rbOnclicklisner;
    }

    public void updateItemList(List<OutputItem> newItemList) {
        this.outputItems = newItemList;
        notifyDataSetChanged(); // Refreshes the RecyclerView
    }

    public void nextPage() {
        if (startPosition + ITEMS_PER_PAGE < outputItems.size()) {
            startPosition += ITEMS_PER_PAGE;
            notifyDataSetChanged();
        }
    }

    public void prevPage() {
        if (startPosition - ITEMS_PER_PAGE >= 0) {
            startPosition -= ITEMS_PER_PAGE;
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.camprediness_adapterlayout, parent, false);
        return new myview(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myview holder, int pos) {
       //int position = holder.getAbsoluteAdapterPosition();
        OutputItem item = outputItems.get(pos);
        holder.tv_SRNO.setText(String.valueOf(item.getItemId()));
        holder.tv_item_name.setText(String.valueOf(item.getItemName()));

//        if (item.getItemStatus()==0){
//            item.setItemStatus(1);
//        }


        if (item.getItemStatus() == 1) {
            holder.rb_available.setChecked(true);
        } else if (item.getItemStatus() == 2) {
            holder.rb_ntavailable.setChecked(true);
        } else if (item.getItemStatus() == 3) {
            holder.rb_ntworking.setChecked(true);
        }

        holder.rb_available.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {

                item.setItemStatus(1);
               /* if (rbOnclicklisner != null) {
                    rbOnclicklisner.onclickphone(item);
                }*/
            }
        });

        holder.rb_ntavailable.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
//                outputItems.get(pos).setItemStatus(2);
                item.setItemStatus(2);

                /*if (rbOnclicklisner != null) {
                    rbOnclicklisner.onclickphone(item);
                }*/
            }
        });

        holder.rb_ntworking.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
//                outputItems.get(pos).setItemStatus(3);
                item.setItemStatus(3);

               /* if (rbOnclicklisner != null) {
                               item.setItemStatus(1);

                    rbOnclicklisner.onclickphone(item);
                }*/
            }
        });

    }
    @Override
    public int getItemCount() {
//        int remainingItems = outputItems.size() - startPosition;
//        return Math.min(ITEMS_PER_PAGE, remainingItems);
        return outputItems.size();
    }
    public int getItemViewType(int position) {
        return position;
    }

    class myview extends RecyclerView.ViewHolder {
        TextView tv_SRNO, tv_item_name;
        RadioButton rb_available, rb_ntavailable, rb_ntworking;
        public myview(@NonNull View itemView) {
            super(itemView);
            tv_SRNO = itemView.findViewById(R.id.tv_SRNO);
            tv_item_name = itemView.findViewById(R.id.tv_item_name);
            rb_available = itemView.findViewById(R.id.rb_available);
            rb_ntavailable = itemView.findViewById(R.id.rb_ntavailable);
            rb_ntworking = itemView.findViewById(R.id.rb_ntworking);
        }
    }
    public List<OutputItem> getOutputItems() {
        return outputItems;
    }

}
