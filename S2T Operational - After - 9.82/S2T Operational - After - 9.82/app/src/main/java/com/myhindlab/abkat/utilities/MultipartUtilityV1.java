package com.myhindlab.abkat.utilities;


import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * MultipartUtility with upload progress callback.
 */
public class MultipartUtilityV1 {
    private final String boundary;
    private static final String LINE_FEED = "\r\n";
    private HttpURLConnection httpConn;
    private String charset;
    private OutputStream outputStream;
    private PrintWriter writer;
    private ProgressListener progressListener; // optional

    /**
     * Progress listener interface.
     * onProgress is called with bytesWritten so far and totalBytes for the file being uploaded.
     *
     * NOTE: onProgress is called on the same thread that's performing the upload (usually background).
     * If you update UI from the listener, remember to post to the main thread.
     */
    public interface ProgressListener {
        void onProgress(long bytesWritten, long totalBytes);
    }

    /**
     * Backwards-compatible constructor (no progress listener).
     */
    public MultipartUtilityV1(String requestURL, String charset) throws IOException {
        this(requestURL, charset, null);
    }

    /**
     * New constructor that accepts an optional ProgressListener.
     *
     * @param requestURL server URL
     * @param charset    e.g. "UTF-8"
     * @param listener   progress listener (can be null)
     * @throws IOException
     */
    public MultipartUtilityV1(String requestURL, String charset, ProgressListener listener)
            throws IOException {
        this.charset = charset;
        this.progressListener = listener;
        boundary = "AaB03x";

        URL url = new URL(requestURL);
        httpConn = (HttpURLConnection) url.openConnection();
//		httpConn.setReadTimeout(10000);
        httpConn.setUseCaches(false);
        httpConn.setDoOutput(true); // indicates POST method
        httpConn.setDoInput(true);
        // System.setProperty("http.keepAlive", "false");
        httpConn.setRequestProperty("Content-Type",
                "multipart/form-data; boundary=" + boundary);
        httpConn.setRequestProperty("Connection", "Keep-Alive");
        outputStream = httpConn.getOutputStream();
        writer = new PrintWriter(new OutputStreamWriter(outputStream, charset),
                true);

        Log.i("URL", "APICall: " + requestURL);
    }

    /**
     * Adds a form field to the request
     *
     * @param name  field name
     * @param value field value
     */
    public void addFormField(String name, String value) {
        writer.append("--").append(boundary).append(LINE_FEED);
        writer.append("Content-Disposition: form-data; name=\"").append(name).append("\"")
                .append(LINE_FEED);
        writer.append("Content-Type: text/plain; charset=").append(charset).append(LINE_FEED);
        writer.append(LINE_FEED);
        writer.append(value).append(LINE_FEED);
        writer.flush();

        Log.i("Params", "APICall: Param:" + name + " Value:" + value);
    }

    /**
     * Adds an upload file section to the request and reports upload progress.
     *
     * @param fieldName  name attribute in <input type="file" name="..." />
     * @param uploadFile a File to be uploaded
     * @throws IOException
     */
    public void addFilePart(String fieldName, File uploadFile) throws IOException {
        String fileName = uploadFile.getName();
        writer.append("--").append(boundary).append(LINE_FEED);
        writer.append("Content-Disposition: form-data; name=\"").append(fieldName)
                .append("\"; filename=\"").append(fileName).append("\"")
                .append(LINE_FEED);

        String contentType = URLConnection.guessContentTypeFromName(fileName);
        if (contentType == null) contentType = "application/octet-stream";

        writer.append("Content-Type: ").append(contentType).append(LINE_FEED);
        writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
        writer.append(LINE_FEED);
        writer.flush();

        // Write file and report progress
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(uploadFile);
            byte[] buffer = new byte[4096];
            int bytesRead = -1;
            long totalBytes = uploadFile.length();
            long bytesWritten = 0L;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
                bytesWritten += bytesRead;

                // flush occasionally to ensure progress actually flows
                outputStream.flush();

                // Notify listener (if present). Called on the uploading thread.
                if (progressListener != null) {
                    try {
                        progressListener.onProgress(bytesWritten, totalBytes);
                    } catch (Exception ex) {
                        // swallow listener exceptions so upload isn't interrupted
                        Log.w("MultipartUtility", "ProgressListener threw", ex);
                    }
                }
            }
            outputStream.flush();
        } finally {
            if (inputStream != null) {
                try { inputStream.close(); } catch (IOException ignored) {}
            }
        }

        writer.append(LINE_FEED);
        writer.flush();

        Log.i("TAG", "addFilePart: " + fieldName + ":" + uploadFile.getAbsoluteFile());
    }

    public void addHeaderField(String name, String value) {
        writer.append(name).append(": ").append(value).append(LINE_FEED);
        writer.flush();
    }

    /**
     * Completes the request and receives response from the server.
     *
     * @return a list of Strings as response in case the server returned status
     * OK, otherwise an exception is thrown.
     * @throws IOException
     */
    public List<String> finish() throws IOException {
        List<String> response = new ArrayList<String>();

        writer.append(LINE_FEED).flush();
        writer.append("--").append(boundary).append("--").append(LINE_FEED);
        writer.close();

        // checks server's status code first
        int status = httpConn.getResponseCode();
        if (status == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    httpConn.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                response.add(line);
            }
            reader.close();
            httpConn.disconnect();
        } else {
            // read error stream to get server message (optional)
            StringBuilder errorSb = new StringBuilder();
            try {
                BufferedReader errReader = new BufferedReader(new InputStreamReader(httpConn.getErrorStream()));
                String l;
                while (errReader != null && (l = errReader.readLine()) != null) {
                    errorSb.append(l).append('\n');
                }
            } catch (Exception ignored) {}
            throw new IOException("Server returned non-OK status: " + status + " body: " + errorSb.toString());
        }

        return response;
    }
}
