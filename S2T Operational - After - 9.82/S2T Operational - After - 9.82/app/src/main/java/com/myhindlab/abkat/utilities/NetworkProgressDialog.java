package com.myhindlab.abkat.utilities;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.myhindlab.abkat.R;

import java.util.Locale;

public class NetworkProgressDialog extends Dialog {
    private TextView tvMessage, tvUploadSpeed, tvDownloadSpeed, tvSentBytes, tvReceivedBytes;

    public NetworkProgressDialog(@NonNull Context context) {
        super(context);
        setCancelable(false);
        setContentView(R.layout.dialog_network_progress);
        tvMessage = findViewById(R.id.tvMessage);
        tvUploadSpeed = findViewById(R.id.tvUploadSpeed);
        tvDownloadSpeed = findViewById(R.id.tvDownloadSpeed);
        tvSentBytes = findViewById(R.id.tvSentBytes);
        tvReceivedBytes = findViewById(R.id.tvReceivedBytes);
    }

    public void setMessage(String msg) {
        runOnUiThread(() -> tvMessage.setText(msg));
    }

    public void updateUploadSpeed(final String speedText) {
        runOnUiThread(() -> tvUploadSpeed.setText("Sent: " + speedText));
    }

    public void updateDownloadSpeed(final String speedText) {
        runOnUiThread(() -> tvDownloadSpeed.setText("Receive: " + speedText));
    }

    public void updateSentBytes(final long bytes) {
        runOnUiThread(() -> tvSentBytes.setText("Total Data Sent: " + formatBytes(bytes)));
    }

    public void updateReceivedBytes(final long bytes) {
        runOnUiThread(() -> tvReceivedBytes.setText("Total Data Received: " + formatBytes(bytes)));
    }

    private String formatBytes(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = "KMGT".charAt(exp - 1) + "B";
        return String.format(Locale.getDefault(), "%.2f %s", bytes / Math.pow(1024, exp), pre);
    }

    private void runOnUiThread(Runnable r) {
        if (getOwnerActivity() != null) {
            getOwnerActivity().runOnUiThread(r);
        } else {
            // fallback
            new Handler(Looper.getMainLooper()).post(r);
        }
    }
}
