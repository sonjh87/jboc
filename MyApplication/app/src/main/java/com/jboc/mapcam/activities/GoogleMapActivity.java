package com.jboc.mapcam.activities;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.jboc.mapcam.MainActivity;
import com.jboc.mapcam.R;
import com.jboc.mapcam.googleservice.GpsCallbackInterface;
import com.jboc.mapcam.googleservice.GpsClient;
import com.jboc.mapcam.mapactivity.MapClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.MapFragment;
import com.jboc.mapcam.mapactivity.TestLoadImageClass;

/**
 * Created by Ztkmk on 2017-05-27.
 */

public class GoogleMapActivity extends FragmentActivity {

    private MapClient mapClient;
    private GpsClient googleApiClient;

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_googlemap);

        MainActivity.SetButton(this, R.id.home_button_map, R.id.album_button_map, R.id.map_button_map);

        InitGoogleApiClient();
    }

    public void InitMapFragMent(LatLng latLng) {

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.googleMap);
        mapClient = new MapClient(latLng, mapFragment);
    }

    private void InitGoogleApiClient(){
/*
        googleApiClient = new GpsClient(this, new GpsCallbackInterface() {
            @Override
            public void OnConnected(LatLng latLng) {

                InitMapFragMent(latLng);
                GetImageFromServer(latLng);
            }

            @Override
            public void OnLocationChanged(LatLng latLng) {

                mapClient.LocationChanged(latLng);
            }
        });*/
        super.onStart();
    }

    private void GetImageFromServer(LatLng latLng) {

        //TODO - Change Code Here to Get From Server

    }
}
