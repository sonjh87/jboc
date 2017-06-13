package com.jboc.mapcam;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.jboc.mapcam.activities.AlbumActivity;
import com.jboc.mapcam.activities.GoogleMapActivity;
import com.jboc.mapcam.activities.HomeActivity;
import com.jboc.mapcam.googleservice.GpsClient;
import com.jboc.mapcam.http.RestHttpClient;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        InitManager();
        StartActivity(HomeActivity.class);
    }

    public void StartActivity(Class activityClass) {

        try {

            Intent intent = new Intent(this, activityClass);
            startActivity(intent);
            finish();

        } catch (Exception e){

            Log.d("TAG", e.getMessage());

            Intent intent = new Intent(this, this.getClass());
            startActivity(intent);
            finish();
        }
    }

    public static void SetButton(final Activity activity, final int nHomeButtonId, final int nAlbumButtonId, final int nMapButtonId) {

        Button homeButton = (Button) activity.findViewById(nHomeButtonId);
        Button albumButton = (Button) activity.findViewById(nAlbumButtonId);
        Button mapButton = (Button) activity.findViewById(nMapButtonId);

        if (activity.getClass().equals(HomeActivity.class)) {

            SetButtonListener(albumButton, activity, AlbumActivity.class);
            SetButtonListener(mapButton, activity, GoogleMapActivity.class);

        } else if (activity.getClass().equals(AlbumActivity.class)) {

            SetButtonListener(homeButton, activity, HomeActivity.class);
            SetButtonListener(mapButton, activity, GoogleMapActivity.class);

        } else if (activity.getClass().equals(GoogleMapActivity.class)) {

            SetButtonListener(homeButton, activity, HomeActivity.class);
            SetButtonListener(albumButton, activity, AlbumActivity.class);
        }
    }

    private static void SetButtonListener(final Button button, final Activity currentActivity, final Class activityClass) {

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    Intent intent = new Intent(currentActivity, activityClass);
                    currentActivity.startActivity(intent);
                    currentActivity.finish();

                } catch (Exception e) {

                    Log.d("TAG", e.getMessage());

                    Intent intent = new Intent(currentActivity, MainActivity.class);
                    currentActivity.startActivity(intent);
                    currentActivity.finish();
                }
            }
        });
    }

    private void InitManager() {

        InitHttpClient();
        InitGpsClient();


        InitTestImage();
    }

    private void InitHttpClient() {

        String ip = getResources().getString(R.string.web_domain);
        int port = getResources().getInteger(R.integer.web_port);
        RestHttpClient.SetServerInfo(ip, port);
    }

    private void InitGpsClient() {

        GpsClient.GetInstance().Init(this, null);
    }

    public static Bitmap icon;

    private void InitTestImage() {

        icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
/*
        GpsClient gpsClient = new GpsClient(this, new GpsCallbackInterface() {
            @Override
            public void OnConnected(LatLng latLng) {

                TestLoadImageClass.GetInstance().Init(latLng, getResources(), R.mipmap.ic_launcher);
            }

            @Override
            public void OnLocationChanged(LatLng latLng) {

            }
        });
        gpsClient.CloseGps();*/
    }
}
