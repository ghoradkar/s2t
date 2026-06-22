package com.myhindlab.abkat.utilities.network_monitor;

// Listener used by interceptor
public interface ProgressListener {
    void onRequestProgress(long bytesWritten, long contentLength);
    void onResponseProgress(long bytesRead, long contentLength);
}