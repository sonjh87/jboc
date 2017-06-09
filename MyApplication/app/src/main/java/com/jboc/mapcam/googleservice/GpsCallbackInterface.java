package com.jboc.mapcam.googleservice;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Ztkmk on 2017-06-10.
 */

public interface GpsCallbackInterface {

    void OnConnected(LatLng latLng);
    void OnLocationChanged(LatLng latLng);
}
