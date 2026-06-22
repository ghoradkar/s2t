package com.myhindlab.abkat.utilities.network_monitor;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ProgressInterceptor implements Interceptor {
    private final ProgressListener listener;
    public ProgressInterceptor(ProgressListener listener) { this.listener = listener; }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request original = chain.request();
        Request.Builder requestBuilder = original.newBuilder();
        RequestBody body = original.body();
        if (body != null) {
            body = new CountingRequestBody(body, listener);
            requestBuilder.method(original.method(), body);
        }
        Response response = chain.proceed(requestBuilder.build());
        // Wrap response body
        ResponseBody responseBody = response.body();
        if (responseBody != null) {
            ResponseBody countingBody = new CountingResponseBody(responseBody, listener);
            return response.newBuilder().body(countingBody).build();
        }
        return response;
    }
}
