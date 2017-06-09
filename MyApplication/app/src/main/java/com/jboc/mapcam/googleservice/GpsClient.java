package com.jboc.mapcam.googleservice;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Ztkmk on 2017-06-10.
 */

public class GpsClient implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    final private GoogleApiClient googleApiClient;
    final private Activity currentActivity;
    final private GpsCallbackInterface gpsCallbackInterface;

    public GpsClient(Activity activity, final GpsCallbackInterface gpsCallbackInterface) {

        currentActivity = activity;
        this.gpsCallbackInterface = gpsCallbackInterface;

        googleApiClient = new GoogleApiClient
                .Builder(currentActivity)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        googleApiClient.connect();
    }

    private LatLng lastLatLng;
    private LocationRequest locationRequest;

    public void CloseGps() {

        googleApiClient.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            //insertDummyContact();
            return;
        }

        if (currentActivity == null) {

            Log.d("ERROR", String.format("Activity is null"));
            return;
        }

        if (ActivityCompat.checkSelfPermission(currentActivity, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(currentActivity, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            if (ActivityCompat.shouldShowRequestPermissionRationale(currentActivity, android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                ActivityCompat.requestPermissions(currentActivity, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            } else {

                ActivityCompat.requestPermissions(currentActivity, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }

            if (ActivityCompat.shouldShowRequestPermissionRationale(currentActivity, android.Manifest.permission.ACCESS_COARSE_LOCATION)) {

                ActivityCompat.requestPermissions(currentActivity, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            } else {

                ActivityCompat.requestPermissions(currentActivity, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }
        }

        CreateLocationRequest();
        Location location = LocationServices.FusedLocationApi.
                getLastLocation(googleApiClient);

        if (location == null) {

            Log.d("TAG", "Error Execute GoogleAPIClient");
            currentActivity.finish();
            return;
        }

        lastLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        gpsCallbackInterface.OnConnected(lastLatLng);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        lastLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        gpsCallbackInterface.OnLocationChanged(lastLatLng);
    }

    private void CreateLocationRequest() {

        //remove location updates so that it resets
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);

        //import should be **import com.google.android.gms.location.LocationListener**;
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)
                .setFastestInterval(1 * 1000);

        if (currentActivity == null) {

            Log.d("ERROR", String.format("Activity is null"));
            return;
        }

        //restart location updates with the new interval
        //Already get the permission dont worry bout this
        if (ActivityCompat.checkSelfPermission(currentActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(currentActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }
}
