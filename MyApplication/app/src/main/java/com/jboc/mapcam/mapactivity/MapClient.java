package com.jboc.mapcam.mapactivity;

import android.location.Location;
import android.os.Message;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

/**
 * Created by Ztkmk on 2017-05-27.
 */

public class MapClient implements OnMapReadyCallback, GoogleMap.OnCameraMoveListener {

    private final int CAMERA_UPDATE_MIN_TIME = 1000;

    private GoogleMap googleMap;
    private LatLng lastLatLng;

    private Long lastClickedTime;
    private final MapFragment mapFragment;

    private final MapAction mapAction;

    public MapClient(Location location, MapFragment mapFragment) {

        lastLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        lastClickedTime = (new Date()).getTime();
        this.mapFragment = mapFragment;
        this.mapFragment.getMapAsync(this);

        mapAction = new MapAction();
    }

    @Override
    public void onMapReady(final GoogleMap map) {

        googleMap = map;
        mapAction.SetGoogleMap(googleMap);
        MoveCamera();
    }

    public void LocationChanged(final Location location) {

        lastLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        MoveCamera();
        UpdateImage(lastLatLng);
    }

    private void MoveCamera() {

        //googleMap.addMarker(new MarkerOptions().position(currentLatLng).title("Seoul"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLatLng, 17.0f));
    }

    @Override
    public void onCameraMove() {

        Long timeDiff = (new Date()).getTime() - lastClickedTime;
        //잦은 이동에 항상 업데이트 안 하도록 시간 설정
        if (timeDiff < CAMERA_UPDATE_MIN_TIME) {

            return;
        }

        UpdateImage(googleMap.getCameraPosition().target);
    }

    private void UpdateImage(LatLng latLng) {

        Message message = new Message();
        message.what = MapAction.DEFAULT_HANDLER_MESSAGE;
        message.obj = latLng;
        mapAction.sendMessage(message);
    }
}
