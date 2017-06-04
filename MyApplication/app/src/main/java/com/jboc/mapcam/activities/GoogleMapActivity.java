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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.jboc.mapcam.MainActivity;
import com.jboc.mapcam.R;
import com.jboc.mapcam.mapactivity.MapClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.MapFragment;

/**
 * Created by Ztkmk on 2017-05-27.
 */

public class GoogleMapActivity extends FragmentActivity
        implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private MapClient mapClient;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_googlemap);

        MainActivity.SetButton(this, R.id.home_button_map, R.id.album_button_map, R.id.map_button_map);

        //InitGoogleApiClient();
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.googleMap);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(0, 0), 17.0f));
            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            //insertDummyContact();
            return;
        }

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)){

                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            } else {

                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)){

                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            } else {

                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }
        }

        Location location = LocationServices.FusedLocationApi.
                getLastLocation(googleApiClient);

        if (location == null) {

            Log.d("TAG", "Error Execute GoogleAPIClient");
            finish();
            return;
        }

        //MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.googleMap);
        //mapClient = new MapClient(location, mapFragment);
    }

    @Override
    public void onConnectionSuspended(int i) {

        Log.d("", String.format("%d", i));
        finish();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Log.d("", connectionResult.getErrorMessage());
        finish();
    }

    @Override
    public void onLocationChanged(Location location) {

        Log.d("TAG", "Location Changed");
        mapClient.LocationChanged(location);;
    }

    private void InitGoogleApiClient(){

        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)
                .setFastestInterval(1 * 1000);

        if (googleApiClient == null){

            googleApiClient = new GoogleApiClient
                    .Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();

            googleApiClient.connect();
            super.onStart();
        }
    }


}
