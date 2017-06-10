package com.jboc.mapcam.activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.os.Parcel;
import android.widget.ImageView;

import com.google.android.gms.maps.model.LatLng;
import com.jboc.mapcam.MainActivity;
import com.jboc.mapcam.R;
import com.jboc.mapcam.googleservice.GpsCallbackInterface;
import com.jboc.mapcam.googleservice.GpsClient;
import com.jboc.mapcam.http.RestHttpClient;
import com.jboc.mapcam.threadhandler.HomeProcessHandler;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Ztkmk on 2017-06-04.
 */

public class HomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.cast_mini_controller_gradient_light);

        MainActivity.SetButton(HomeActivity.this, R.id.home_button, R.id.album_button, R.id.map_button);

        /*GpsClient gpsClient = new GpsClient(this, new GpsCallbackInterface() {
            @Override
            public void OnConnected(LatLng latLng) {

                GetImageFromServer(latLng);
            }

            @Override
            public void OnLocationChanged(LatLng latLng) {

            }
        });*/
        GetImageFromServer();
    }

    private void GetImageFromServer() {

        //String url = String.format("image_list/my/get?latitude=%d&longitude=%d", latLng.latitude, latLng.longitude);
        String url = String.format("image_list/my/");
        //String url = String.format("");
        RestHttpClient.Get(url, new RequestParams(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                JSONArray jsonArray;
                try {

                    String response = new String(responseBody);
                    jsonArray = new JSONArray(response);
                } catch (JSONException e) {

                    e.printStackTrace();
                    return;
                }

                for(int i=0; i < jsonArray.length(); i++){
                    try {
                        Integer jObject = (Integer)jsonArray.getInt(0);  // JSONObject 추출
                        Message msg = new Message();
                        msg.obj = new Long(jObject);
                        HomeProcessHandler.GetInstance().sendMessage(msg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                int errorCode = statusCode;
            }
        });
    }
}
