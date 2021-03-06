package com.jboc.mapcam.threadhandler;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.jboc.mapcam.ImageList;
import com.jboc.mapcam.MainActivity;
import com.jboc.mapcam.activities.HomeActivity;
import com.jboc.mapcam.http.RestHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Ztkmk on 2017-06-10.
 */

public class HomeProcessHandler extends Handler {

    final public static int GET_IMAGE_FROM_SERVER = 1;

    private static HomeProcessHandler instance = new HomeProcessHandler();
    private HomeProcessHandler() {}
    public static HomeProcessHandler GetInstance() { return instance; }

    private HomeActivity homeActivity;
    public void SetHomeActivity(HomeActivity homeActivity) { this.homeActivity = homeActivity; }

    @Override
    public void handleMessage(Message msg) {

        super.handleMessage(msg);

        switch (msg.what) {

            case GET_IMAGE_FROM_SERVER: {

                final int imgId = (int)msg.obj;
                String url = String.format("image/get?image_id=%d", imgId);
                RestHttpClient.Get(url, new RequestParams(), new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                        //Here to Depress the File to Bitmap

                        ImageList.GetInstance().AddImageToList(MainActivity.icon);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                        Log.d("ERROR", String.format("GET IMAGE FROM SERVER FAILED.[IMAGE_ID: %d]", imgId));

                        //ImageList.GetInstance().AddImageToList(MainActivity.icon);
                        homeActivity.UpdateGridView(MainActivity.icon);
                    }
                });
            }

            default: {


            }
        }
    }
}
