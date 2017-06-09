package com.jboc.mapcam.activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.widget.ImageView;

import com.google.android.gms.maps.model.LatLng;
import com.jboc.mapcam.MainActivity;
import com.jboc.mapcam.R;
import com.jboc.mapcam.googleservice.GpsCallbackInterface;
import com.jboc.mapcam.googleservice.GpsClient;
import com.jboc.mapcam.http.RestHttpClient;
import com.jboc.mapcam.threadhandler.HomeProcessHandler;
import com.loopj.android.http.AsyncHttpResponseHandler;

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

        GpsClient gpsClient = new GpsClient(this, new GpsCallbackInterface() {
            @Override
            public void OnConnected(LatLng latLng) {

                GetImageFromServer(latLng);
            }

            @Override
            public void OnLocationChanged(LatLng latLng) {

            }
        });
        gpsClient.CloseGps();
    }

    private void GetImageFromServer(LatLng latLng) {

        String url = String.format("image_list/my/get?latitude=%d&longitude=%d", latLng.latitude, latLng.longitude);
        RestHttpClient.Get(url, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                for (byte b : responseBody) {

                    Message msg = new Message();
                    msg.obj = new Long(b);
                    HomeProcessHandler.GetInstance().sendMessage(msg);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
}
