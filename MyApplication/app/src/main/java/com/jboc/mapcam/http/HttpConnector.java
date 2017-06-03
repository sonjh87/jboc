package com.jboc.mapcam.http;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Ztkmk on 2017-05-28.
 */

public class HttpConnector {

    private HttpURLConnection httpURLConnection = null;

    private static String boundary =  "*****";

    public HttpConnector(String connectUrl) throws IOException {

        URL url = new URL(connectUrl);
        httpURLConnection = (HttpURLConnection) url.openConnection();

        httpURLConnection.setUseCaches(false);
        httpURLConnection.setDoOutput(true);

        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
        httpURLConnection.setRequestProperty("Cache-Control", "no-cache");
        httpURLConnection.setRequestProperty(
                "Content-Type", "multipart/form-data;boundary=" + boundary);
    }

    public void Post(byte[] bytes, String respone) {

        try {

            DataOutputStream request = new DataOutputStream(
                    httpURLConnection.getOutputStream());
            request.write(bytes);
            request.flush();
            request.close();

            respone = GetRespone();

        } catch (IOException e) {

            Log.d("TAG", e.getMessage());

        } finally {

            httpURLConnection.disconnect();
        }
    }

    private String GetRespone() throws IOException {

        InputStream responseStream = new
                BufferedInputStream(httpURLConnection.getInputStream());

        BufferedReader responseStreamReader =
                new BufferedReader(new InputStreamReader(responseStream));

        String line = "";
        StringBuilder stringBuilder = new StringBuilder();

        while ((line = responseStreamReader.readLine()) != null) {
            stringBuilder.append(line).append("\n");
        }
        responseStreamReader.close();

        return stringBuilder.toString();
    }
}
