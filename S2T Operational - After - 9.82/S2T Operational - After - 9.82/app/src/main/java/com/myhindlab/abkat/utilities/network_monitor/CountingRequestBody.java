package com.myhindlab.abkat.utilities.network_monitor;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;



// Wrapping RequestBody to measure upload progress
public class CountingRequestBody extends RequestBody {
    private final RequestBody delegate;
    private final ProgressListener listener;

    public CountingRequestBody(RequestBody delegate, ProgressListener listener) {
        this.delegate = delegate;
        this.listener = listener;
    }
    @Override public MediaType contentType() { return delegate.contentType(); }
    @Override public long contentLength() throws IOException { return delegate.contentLength(); }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        long contentLength = contentLength();
        CountingSink countingSink = new CountingSink(sink, listener, contentLength);
        BufferedSink buffered = Okio.buffer(countingSink);
        delegate.writeTo(buffered);
        buffered.flush();
    }

    static final class CountingSink extends ForwardingSink {
        private long bytesWritten = 0L;
        private final ProgressListener listener;
        private final long contentLength;
        CountingSink(Sink delegate, ProgressListener listener, long contentLength) { super(delegate); this.listener = listener; this.contentLength = contentLength; }
        @Override public void write(Buffer source, long byteCount) throws IOException {
            super.write(source, byteCount);
            bytesWritten += byteCount;
            listener.onRequestProgress(bytesWritten, contentLength);
        }
    }
}


