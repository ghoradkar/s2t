package com.myhindlab.abkat.utilities.network_monitor;

import android.os.FileUtils;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;

// Wrapping ResponseBody to measure download progress
public class CountingResponseBody extends ResponseBody {
    private final ResponseBody delegate;
    private final ProgressListener listener;
    private BufferedSource bufferedSource;

    public CountingResponseBody(ResponseBody delegate, ProgressListener listener) {
        this.delegate = delegate;
        this.listener = listener;
    }

    @Override
    public MediaType contentType() {
        return delegate.contentType();
    }

    @Override
    public long contentLength() {
        return delegate.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(new ForwardingSource(delegate.source()) {
                long totalBytesRead = 0L;
                final long contentLength = delegate.contentLength();

                @Override
                public long read(Buffer sink, long byteCount) throws IOException {
                    long bytesRead = super.read(sink, byteCount);
                    totalBytesRead += bytesRead == -1 ? 0 : bytesRead;
                    listener.onResponseProgress(totalBytesRead, contentLength);
                    return bytesRead;
                }
            });
        }
        return bufferedSource;
    }

}