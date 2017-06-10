package com.jboc.mapcam.http;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by Ztkmk on 2017-06-06.
 */


// 참고: http://loopj.com/android-async-http/
public class RestHttpClient {

    //TODO - Have to Change Read From Property
    static private String BASE_URL;

    public static void SetServerInfo(String domain, int port) {

        BASE_URL = String.format("http://%s:%d/", domain, port);
        Log.d("TAG", String.format("Server Base Url Init Done: %s", BASE_URL));
    }

    private static AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

    public static void Get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {

        asyncHttpClient.get(GetAbsoluteUrl(url), params, responseHandler);
    }

    public static void GetFile(String url, FileAsyncHttpResponseHandler fileAsyncHttpResponseHandler) {

        asyncHttpClient.get(GetAbsoluteUrl(url), fileAsyncHttpResponseHandler);
    }

    public static void Post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {

        asyncHttpClient.post(GetAbsoluteUrl(url), params, responseHandler);
    }

    public static void PostFile(String url, File file, String fileName, AsyncHttpResponseHandler responseHandler) {

        RequestParams params = new RequestParams();
        try {

            params.put(fileName, file);

        } catch (FileNotFoundException e) {

            Log.d("TAG", e.getMessage());
            responseHandler.sendFailureMessage(HttpStatus.RESOURCE_ERROR.GetValue(), null, null, null);
            return;
        }

        Post(url, params, responseHandler);
    }

    private static String GetAbsoluteUrl(String relativeUrl){

        return String.format(BASE_URL + relativeUrl);
    }
}
