package com.myhindlab.abkat.abha.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myhindlab.abkat.R;

import java.util.ArrayList;

public class AuthModesListAdapter extends RecyclerView.Adapter<AuthModesListAdapter.AuthModesViewHolder> {

    private ArrayList<String> authModeList;
    private AuthModeListener authModeListener;
    private int selectedPosition = -1;
    private RadioButton lastChecked;

    public interface AuthModeListener {
        void onModeSelect(String mode);
    }

    public AuthModesListAdapter(ArrayList<String> authModeList, AuthModeListener authModeListener) {
        this.authModeList = authModeList;
        this.authModeListener = authModeListener;
    }

    @NonNull
    @Override
    public AuthModesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_auth_modes, parent, false);
        return new AuthModesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AuthModesViewHolder holder, int pos) {

        int position = holder.getAdapterPosition();
        String mode = authModeList.get(holder.getAdapterPosition());
        holder.rbAuthMode.setText(mode);
        holder.rbAuthMode.setChecked(selectedPosition == position);

        holder.rbAuthMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selectedPosition
                            = holder.getAdapterPosition();
                    notifyDataSetChanged();
                    authModeListener.onModeSelect(mode);

                }
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return authModeList.size();
    }

    class AuthModesViewHolder extends RecyclerView.ViewHolder {
        RadioButton rbAuthMode;

        public AuthModesViewHolder(@NonNull View itemView) {
            super(itemView);
            rbAuthMode = itemView.findViewById(R.id.rbAuthMode);
        }
    }
}
