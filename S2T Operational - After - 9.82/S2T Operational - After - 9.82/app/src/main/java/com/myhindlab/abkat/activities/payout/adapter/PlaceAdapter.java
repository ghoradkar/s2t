package com.myhindlab.abkat.activities.payout.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.libraries.places.api.model.AutocompletePrediction;

import java.util.List;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder> {

    private List<AutocompletePrediction> predictions;
    private OnPlaceClickListener listener;

    // 🔹 Constructor
    public PlaceAdapter(List<AutocompletePrediction> predictions,
                        OnPlaceClickListener listener) {
        this.predictions = predictions;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {

        AutocompletePrediction prediction = predictions.get(position);

        holder.textName.setText(
                prediction.getFullText(null).toString()
        );

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPlaceClick(prediction.getPlaceId());
            }
        });
    }


    @Override
    public int getItemCount() {
        return predictions != null ? predictions.size() : 0;
    }

    // 🔹 ViewHolder
    static class PlaceViewHolder extends RecyclerView.ViewHolder {
        TextView textName;

        public PlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(android.R.id.text1);
        }
    }

    // ✅ MISSING INTERFACE (THIS FIXES THE ERROR)
    public interface OnPlaceClickListener {
        void onPlaceClick(String placeId);
    }
}
